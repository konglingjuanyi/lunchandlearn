/**
 * Created by de007ra on 5/7/2016.
 */
var lunchAndLearnDirectives = angular.module('directives');

lunchAndLearnDirectives.directive('mainTraining', function() {
	return {
		templateUrl : '/lunchandlearn/html/main/training.html',
		replace : true,
		scope: true,
		controller : 'mainTrainingController as self'
	};
}).controller('mainTrainingController',
	[ 'restService', function(restService) {
		var self = this;
		self.recentTrainingsUrl = restService.appUrl + '/trainings/recent';

		self.setRecentTrainings = function () {
			restService.get(self.recentTrainingsUrl).then(function (response) {
				self.trendingTopics = response.data;
			}, function (response) {

			});
		}

		self.setRecentTrainings();
	}]);