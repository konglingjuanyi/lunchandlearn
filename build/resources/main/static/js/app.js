var lunchAndLearnApp = angular.module('lunchAndLearnApp', ['ngRoute', 'controllers', 'directives', 'ordinal']);
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
				controller: 'employeeController as self'
			}).
			when('/topics', {
				templateUrl: '/lunchandlearn/html/topic/topics.html',
				controller: 'topicController as self'
			}).
			otherwise({
				redirectTo: '/'
			});
}]);