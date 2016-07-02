angular.module('controllers').controller('employeeController', ['$scope', 'employeeService',
	'$routeParams', 'restService', 'utilitiesService', 'employeeService', 'topicService',
	function($scope, employeeService, $routeParams, restService, utilitiesService,
			 employeeService, topicService) {
		var self = this;

		self.item = {};
		$scope.selected = {};

		self.init = function () {
			if (!_.isEmpty($routeParams.employeeId)) {
				self.employeeId = $routeParams.employeeId;
				// employeeService.pushRecentEmployeeId();
				// self.getEmployees('');
			}
			employeeService.listEmployeesMinimal().then(function (response) {
				self.managers = response.data.content;
				self.getEmployee();
			});
			topicService.listTopics().then(function (res) {
				self.topics = res.data.content;
			});

		};

		$scope.$watch('selected.manager', function () {
			if ($scope.selected.manager) {
				self.item.managers = utilitiesService.addUnique(self.item.managers, $scope.selected.manager, 'guid', 'name');
				self.saveByField('managers');
			}
		});

		$scope.$watch('selected.topic', function () {
			if ($scope.selected.topic) {
				self.item.topics = utilitiesService.addUnique(self.item.topics, $scope.selected.topic, 'id', 'name');
				self.saveByField('topics');
			}
		});

		self.getEmployee = function () {
			employeeService.getEmployee(self.employeeId).then(function (response) {
				self.item = _.defaultsDeep(self.item, response.data);
				_.remove(self.managers, {guid: self.item.guid});
			}, function (error) {
				self.error = true;
				self.errorMsg = error.data.message;
			});
		}

		self.saveByField = function (fieldName) {
			var data = {name: fieldName, value: _.get(self.item, fieldName)};
			employeeService.updateEmployeeByField(self.item.guid, data).then(function (response) {
				if (restService.isResponseOk(response)) {
					utilitiesService.setEditable(self, fieldName, false);
				}
			});
		}

		self.init();
	}]);