angular.module('controllers').controller('employeesController', [
	'$uibModal', 'employeeService', 'pagingService', 'restService', 'utilitiesService', '$location',
	function($uibModal, employeeService, pagingService, restService, utilitiesService, $location) {
		var self = this;

		self.pageSizes = pagingService.pageSizes;
		self.sort = {};
		self.currentPage = 1;
		self.currentPageSize = pagingService.currentPageSize;
		self.maxPageSize = pagingService.maxPageSize;
		self.roles = employeeService.roles;

		restService.getLoggedInUser().then(function(user) {
			if(user) {
				self.userGuid = user.guid;
				self.isAdmin = utilitiesService.isAdminUser(user.roles);
			}
		});

		employeeService.isEditable().then(function (status) {
			self.isEditable = status;
		});

		var setManagers = function() {
			return employeeService.getManagers().then(function(response) {
				self.managers = response.data;
			});
		};

		self.checkSearch = function($event) {
			if($event.which === 13 || $event.keyCode === 13) {
				self.list();
			}
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
			self.searching = true;
			employeeService.listEmployees(pagingService.getConfigObj(self)).then(function(response) {
				var data = response.data;
				if(angular.isDefined(data)) {
					self.totalPages = data.totalPages;
					self.employees = data.content;
					self.totalCount = data.totalElements;
					self.fromCount = (data.size * data.number) + 1;
					self.toCount = (self.fromCount - 1) + data.numberOfElements;
					if(self.currentPageSize > self.totalCount) {
						self.currentPageSize = self.pageSizes[0];
					}
				}
			}).finally(function() {
				self.searching = false;
			});
		}

		self.showEmployee = function (guid) {
			$location.path('/employees/' + guid);
		};

		self.showEdit = function(guid) {
			if(self.isAdmin) {
				employeeService.getEmployee(guid).then(function (response) {
					if (angular.isDefined(response.data)) {
						self.employee = response.data;
						// self.employee.managers = _.keys(self.employee.managers);
						var roles = {};
						_.each(self.employee.roles, function (role) {
							var obj = _.find(self.roles, {code: role});
							if (obj) {
								roles[obj.code] = obj.label;
							}
						});
						self.employee.roles = roles;
						self.mode = 'edit';
						self.showModalDlg();
					}
				}, function (response) {
					console.log('error while fetching employee: '
						+ guid + ' with error: ' + response);
				});
			}
		}

		self.save = function() {
			// self.employee.managers = employeeService.getManagersGuidWithName(self.managers, self.employee.managers);
			self.employee.roles = _.keys(self.employee.roles);
			employeeService.updateEmployee(self.employee).then(function(response) {
				self.mode = null;
				self.list();
			});
		}

		var getRoles = function(roles) {
			var roles = [];
			_.each(roles, function (v, k) {
				roles.push(k);
			})
			return roles;
		}

		self.add = function() {
			self.employee.managers = employeeService.getManagersGuidWithName(self.managers, self.employee.managers);
			self.employee.roles = getRoles(self.employee.roles);
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
				templateUrl: '/pbacademy/html/main/confirmationDlg.html',
				controller: 'modalController as self',
				backdrop: 'static',
				resolve: {
					data: function () {
						return {msg: data.msg, item: {guid: data.guid}};
					}
				}
			}
			$uibModal.open(opts).result.then(function () {
				self.doRemove(data.guid)
			}, function () {

			});
		}

		self.showModalDlg = function () {
			setManagers().then(function () {
				var opts = {
					templateUrl: '/pbacademy/html/employee/employeeCreateEdit.html',
					backdrop: 'static',
					controller: 'modalController as self',
					resolve: {
						data: function () {
							return {item: self.employee, mode: self.mode, options: {managers : self.managers, roles: self.roles}};
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

				});
			});
		}

		self.refreshEmployees = function() {
			self.refreshingEmps = true;
			employeeService.refreshEmployees().then(function (response) {
				self.list();
			}, function (error) {
				utilitiesService.showMsgDlg({item:{error: true, title: 'Refresh Error'}, msg: 'Error while refreshing employees. Either LDAP server is down or some internal error!'});
			}).finally(function() {
				self.refreshingEmps = false;
			});
		}

		self.refreshEmployee = function(guid) {
			self.refreshingEmp = true;
			employeeService.refreshEmployee(guid).then(function (response) {
			}).finally(function() {
				self.refreshingEmp = false;
			});
		}

		self.list();
		setManagers();
}]);