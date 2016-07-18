angular.module('controllers').controller('employeeController', ['$scope', 'employeeService',
	'$routeParams', 'restService', 'utilitiesService', 'topicService',
	function($scope, employeeService, $routeParams, restService, utilitiesService,
			 topicService) {
		var self = this;

		self.item = {};
		$scope.selected = {};

		self.init = function () {
			if (!_.isEmpty($routeParams.employeeId)) {
				self.employeeId = $routeParams.employeeId;
			}

			employeeService.isEditable(self.employeeId).then(function (response) {
				self.isEditable = response;
			});

			employeeService.listEmployeesMinimal().then(function (response) {
				if(angular.isDefined(response.data)) {
					self.managers = response.data.content;
					self.getEmployee();
				}
			});
			topicService.listTopics().then(function (res) {
				if(angular.isDefined(res.data)) {
					self.topics = res.data.content;
				}
			});

		};

		$scope.$watch('selected.manager', function () {
			if ($scope.selected.manager) {
				self.item.managers = utilitiesService.addUnique(self.item.managers, $scope.selected.manager, 'guid', 'name');
				self.saveByField('managers');
			}
		});

		$scope.$watch('selected.role', function () {
			if ($scope.selected.role) {
				self.item.roles = utilitiesService.addUnique(self.item.roles, $scope.selected.role, 'code', 'name');
				self.saveByField('roles');
			}
		});

		$scope.$watch('selected.topicKnown', function () {
			if ($scope.selected.topicKnown) {
				self.item.topicsKnown = utilitiesService.addUnique(self.item.topicsKnown, $scope.selected.topicKnown, 'id', 'name');
				self.saveByField('topicsKnown');
			}
		});

		$scope.$watch('selected.topicInterestedIn', function () {
			if ($scope.selected.topicInterestedIn) {
				self.item.topicsInterestedIn = utilitiesService.addUnique(self.item.topicsInterestedIn, $scope.selected.topicInterestedIn, 'id', 'name');
				self.saveByField('topicsInterestedIn');
			}
		});

		self.getEmployee = function () {
			employeeService.getEmployee(self.employeeId).then(function (response) {
				self.item = _.defaultsDeep(self.item, response.data);
				_.remove(self.managers, {guid: self.item.guid});
				self.item.trainingsInterestedIn = employeeService.setTrainingObj(self.item.trainingsInterestedIn);
				self.item.trainingsAttended = employeeService.setTrainingObj(self.item.trainingsAttended);
				self.item.trainingsImparted = employeeService.setTrainingObj(self.item.trainingsImparted);
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