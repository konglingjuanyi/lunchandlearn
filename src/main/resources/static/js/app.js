var pbAcademyApp = angular.module('pbAcademyApp', ['ngRoute', 'filters',
	'controllers', 'directives', 'ordinal', 'ui.select', 'ngSanitize', 'ui.bootstrap',
	'ui.bootstrap.datetimepicker', 'ngFileUpload', 'd3']);
//Define Routing for the app

pbAcademyApp.config(['$routeProvider', '$httpProvider',
	function($routeProvider, $httpProvider) {
		$routeProvider.
			when('/', {
				templateUrl: '/pbacademy/html/main/main.html'
				// controller: 'topicController'
			}).
			when('/trainings', {
				templateUrl: '/pbacademy/html/training/trainings.html',
				controller: 'trainingController as self'
			}).
			when('/employees', {
				templateUrl: '/pbacademy/html/employee/employees.html',
				controller: 'employeesController as self'
			}).
			when('/topics', {
				templateUrl: '/pbacademy/html/topic/topics.html',
				controller: 'topicsController as self'
			}).
			when('/trainings', {
				templateUrl: '/pbacademy/html/training/trainings.html',
				controller: 'trainingsController as self'
			}).
			when('/trainingrooms', {
				templateUrl: '/pbacademy/html/trainingRoom/trainingRooms.html',
				controller: 'trainingRoomsController as self'
			}).
			when('/employees/:employeeId', {
				templateUrl: '/pbacademy/html/employee/employeeEdit.html',
				controller: 'employeeController as self'
			}).
			when('/topics/:topicId', {
				templateUrl: '/pbacademy/html/topic/topicEdit.html',
				controller: 'topicController as self'
			}).
			when('/trainings/:trainingId', {
				templateUrl: '/pbacademy/html/training/trainingEdit.html',
				controller: 'trainingController as self'
			}).
			when('/login', {
				templateUrl: '/pbacademy/html/main/login.html',
				controller: 'loginController as self'
			}).
			when('/login?logout', {
				templateUrl: '/pbacademy/html/main/login.html',
				controller: 'loginController as self'
			}).
			otherwise({
				redirectTo: '/'
			});
		$httpProvider.interceptors.push('loginInterceptor');
}]);