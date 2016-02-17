var lunchAndLearnApp = angular.module('lunchAndLearnApp', ['ngRoute', 'controllers']);
//Define Routing for the app

lunchAndLearnApp.config(['$routeProvider',
	function($routeProvider) {
		$routeProvider.
			when('/trainings/show', {
				templateUrl: '/lunchandlearn/html/trainings.html',
				controller: 'trainingController'
			}).
			when('/trainers/show', {
				templateUrl: '/lunchandlearn/html/trainers.html',
				controller: 'trainerController'
			}).
			when('/topics/show', {
				templateUrl: '/lunchandlearn/html/topics.html',
				controller: 'topicController'
			}).
			otherwise({
				redirectTo: '/'
			});
}]);