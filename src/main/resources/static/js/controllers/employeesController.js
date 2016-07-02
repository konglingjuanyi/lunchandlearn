angular.module('controllers').controller('employeesController', [
	'$uibModal', 'employeeService', 'pagingService',
	function($uibModal, employeeService, pagingService) {
		var self = this;

		self.pageSizes = pagingService.pageSizes;
		self.sort = {};
		self.currentPage = 1;
		self.currentPageSize = pagingService.currentPageSize;
		self.maxPages = pagingService.maxPageSize;

		var setManagers = function() {
			employeeService.getEmployeesName().then(function(response) {
				self.managers = response.data;
			});
		};

		self.checkSearch = function($event) {
			if($event.which === 13 || $event.keyCode === 13) {
				self.list();
			}
		};

		self.sortList = function(colName) {
			self.sort.name = colName;
			self.list();
		};

		self.savePageSize = function() {
			pagingService.savePageSize(self.currentPageSize);
		};

		self.sortList = function(fieldName) {
			self.sort.name = fieldName;
			self.sort.direction = self.sort.direction === 'asc' ? 'desc' : 'asc';
			self.list();
		};

		self.list = function() {
			employeeService.listEmployees(pagingService.getConfigObj(self)).then(function(response) {
				var data = response.data;
				self.totalPages = data.totalPages;
				self.employees = data.content;
				self.totalCount = data.totalElements;
				self.fromCount = (data.size * data.number) + 1;
				self.toCount = (self.fromCount - 1) + data.numberOfElements;
			});
		}

		self.showEdit = function(guid) {
			employeeService.getEmployee(guid).then(function(response) {
				self.employee = response.data;
				self.employee.managers = _.keys(self.employee.managers);
				self.mode = 'edit';
				self.showModalDlg();
			});
		}

		self.save = function() {
			self.employee.managers = employeeService.getManagersGuidWithName(self.managers, self.employee.managers);
			employeeService.updateEmployee(self.employee).then(function(response) {
				self.mode = null;
				self.list();
			});
		}

		self.add = function() {
			self.employee.managers = employeeService.getManagersGuidWithName(self.managers, self.employee.managers);
			employeeService.addEmployee(self.employee).then(function(response) {
				self.mode = null;
				self.list();
			});
		}

		self.showAdd = function(guid) {
			self.employee = {};
			self.mode = 'add';
			self.showModalDlg();
		}

		self.remove = function(guid) {
			var employee = employeeService.getEmployeeByGuid(guid, self.employees);
			self.showConfirmationDlg({msg: 'Employee \'' + employee.name + '\'', guid: guid});
		}

		self.doRemove = function(guid) {
			employeeService.removeEmployee(guid).then(function (response) {
				self.list();
			}, function (response) {
				console.log('error removeEmployee: '+ response)
			});
		}

		self.showConfirmationDlg = function (data) {
			var opts = {
				templateUrl: '/lunchandlearn/html/main/confirmationDlg.html',
				controller: 'modalController as self',
				backdrop: 'static',
				resolve: {
					data: function () {
						return {msg: data.msg, item: {guid: data.guid}};
					}
				}
			}
			$uibModal.open(opts).result.then(function (selectedItem) {
				self.doRemove(data.guid)
			}, function () {
				console.log('confirmation modal cancelled')
			});
		}

		self.showModalDlg = function () {
			setManagers();
			var opts = {
				templateUrl: '/lunchandlearn/html/employee/employeeCreateEdit.html',
				backdrop: 'static',
				controller: 'modalController as self',
				resolve: {
					data: function () {
						return {item: self.employee, mode: self.mode, options: {managers : self.managers}};
					}
				}
			}
			$uibModal.open(opts).result.then(function (selectedItem) {
				self.employee = selectedItem;
				if(self.mode === 'add') {
					self.add();
				}
				else if(self.mode === 'edit') {
					self.save();
				}
			}, function () {
				console.log('confirmation modal cancelled')
			});
		}

		self.list();
		setManagers();
}]);