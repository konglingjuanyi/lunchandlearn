/**
 * Created by de007ra on 5/8/2016.
 */
angular.module('services').factory('employeeService', [ 'restService', 'utilitiesService', '$q',
	function(restService, utilitiesService, $q) {
	var employeeService = {
		getEmployeeByGuid: function(guid, employeeArray) {
			return _.find(employeeArray, function (emloyee) {
				return emloyee.guid === guid;
			});
		},

		getManagersGuidWithName: function(fullManagerArray, managersGuidWithName) {
		var selectedManagers = _.filter(fullManagerArray, function(manager) {
			var man = _.find(managersGuidWithName, function(guid) {
				return guid === manager.guid;
			});
			if(angular.isDefined(man)) {
				return man;
			}
		});
		if(angular.isDefined(selectedManagers)) {
			managersGuidWithName = {};
			_.forEach(selectedManagers, function(manager) {
				managersGuidWithName[manager.guid] = manager.name;
			});
			return managersGuidWithName;
		}
		return [];
	},

		getManagerGuidsArray: function(managersGuidWithNameMap, fullManagerArray) {
			var selectedManagers = _.filter(fullManagerArray, function(manager) {
				var man = _.find(managersGuidWithNameMap, function(name, guid) {
					return guid === manager.guid;
				});
				return angular.isDefined(man);
			});
			return selectedManagers;
		},

		employeesUrl: restService.appUrl + '/employees',
		employeeUrl: restService.appUrl + '/employees/employee',
		//Employee APIs
		listEmployees: function(config) {
			return restService.get(this.employeesUrl, config);
		},

		listEmployeesMinimal: function() {
			return restService.get(this.employeesUrl + '/minimal');
		},

		getManagers: function() {
			return restService.get(this.employeesUrl + '/managers')
		},

		isEditable: function (guid) {
			return restService.getLoggedInUser().then(function(user) {
				if(!user) {
					return false;
				}
				if(utilitiesService.isAdminUser(user)) {
					return true;
				}
				return _.upperCase(user.guid) === _.upperCase(guid);
			});
		},

		roles: [{label: 'Manager', code: 'MANAGER'}, {label: 'Admin', code: 'ADMIN'}],

		updateEmployeeByField: function(empGuid, item) {
			return restService.put(this.employeeUrl  + "/" +  empGuid + '/field', item);
		},

		addEmployee: function(item) {
			return restService.post(this.employeeUrl, item);
		},

		updateEmployee: function(item) {
			return restService.put(this.employeeUrl  + "/" +  item.guid, item);
		},

		removeEmployee: function(guid) {
			return restService.delete(this.employeeUrl + "/" + guid);
		},

		getEmployee: function(guid) {
			return restService.get(this.employeeUrl  + "/" + guid);
		},

		setTrainingObj: function(trainings) {
			if(trainings) {
				var trainingObj = {};
				_.each(trainings, function (training) {
					trainingObj[training.id] = training.name;
				});
				return trainingObj;
			}
		}
	};
	return employeeService;
}]);