/**
 * Created by de007ra on 5/7/2016.
 */
var lunchAndLearnDirectives = angular.module('directives');

lunchAndLearnDirectives.directive('mainTopicTrainer', function() {
	return {
		templateUrl : '/lunchandlearn/html/main/topicTrainer.html',
		replace : true,
		scope: true,
		controller : 'mainTopicTrainerController as self'
	};
}).controller('mainTopicTrainerController',
	[ 'restService', function(restService) {
		var self = this;
		self.trendingTopicsUrl = restService.appUrl + '/topics/trending';
		self.topTrainersUrl = restService.appUrl + '/trainings/top';

		self.setTrendingTopics = function () {
			restService.get(self.trendingTopicsUrl).then(function (response) {
				self.trendingTopics = response.data;
			}, function (response) {

			});
		};

		self.setTopTrainers = function () {
			restService.get(self.topTrainersUrl).then(function (response) {
				self.topTrainers = response.data;
			}, function (response) {

			});
		};

		self.setTopTrainers();
		self.setTrendingTopics();
	}]);