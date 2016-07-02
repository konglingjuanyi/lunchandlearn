var lunchAndLearnApp = angular.module('lunchAndLearnApp', ['ngRoute', 'filters', 'controllers', 'directives',
	'ordinal', 'ui.select', 'ngSanitize', 'ui.bootstrap', 'ui.bootstrap.datetimepicker', 'ngFileUpload']);
//Define Routing for the app

lunchAndLearnApp.config(['$routeProvider',
	function($routeProvider) {
		$routeProvider.
			when('/', {
				templateUrl: '/lunchandlearn/html/main/main.html'
				// controller: 'topicController'
			}).
			when('/trainings', {
				templateUrl: '/lunchandlearn/html/training/trainings.html',
				controller: 'trainingController as self'
			}).
			when('/employees', {
				templateUrl: '/lunchandlearn/html/employee/employees.html',
				controller: 'employeesController as self'
			}).
			when('/topics', {
				templateUrl: '/lunchandlearn/html/topic/topics.html',
				controller: 'topicsController as self'
			}).
			when('/trainings', {
				templateUrl: '/lunchandlearn/html/training/trainings.html',
				controller: 'trainingsController as self'
			}).
			when('/employees/:employeeId', {
				templateUrl: '/lunchandlearn/html/employee/employeeEdit.html',
				controller: 'employeeController as self'
			}).
			when('/topics/:topicId', {
				templateUrl: '/lunchandlearn/html/topic/topicEdit.html',
				controller: 'topicController as self'
			}).
			when('/trainings/:trainingId', {
				templateUrl: '/lunchandlearn/html/training/trainingEdit.html',
				controller: 'trainingController as self'
			}).
			otherwise({
				redirectTo: '/'
			});
}]);