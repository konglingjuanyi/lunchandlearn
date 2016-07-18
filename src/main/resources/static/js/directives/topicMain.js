/**
 * Created by de007ra on 5/7/2016.
 */
var lunchAndLearnDirectives = angular.module('directives');

lunchAndLearnDirectives.directive('topicMain', function() {
	return {
		templateUrl : '/lunchandlearn/html/main/topicMain.html',
		replace : true,
		scope: true,
		controller : 'topicMainController as self'
	};
}).controller('topicMainController',
	['$scope', 'restService', 'utilitiesService', function($scope, restService, utilitiesService) {
		var self = this;
		self.newTopicsUrl = restService.appUrl + '/topics/recent';
		self.topTrainersUrl = restService.appUrl + '/trainings/likes';
		self.trendingTopicsUrl = restService.appUrl + '/topics/likes';

		self.init = function() {
			self.getNewTopics();
			self.getTrendingTopics();
		};

		self.getTrendingTopics = function () {
			restService.get(self.trendingTopicsUrl).then(function (response) {
				if(angular.isDefined(response.data)) {
					if (_.isArray(response.data.content)) {
						self.trendingTopics = utilitiesService.filterByLikeCount(response.data.content);
					}
				}
			}, function (response) {
			});
		};

		self.getNewTopics = function () {
			restService.get(self.newTopicsUrl).then(function (response) {
				if(angular.isDefined(response.data)) {
					if (_.isArray(response.data.content)) {
						self.newTopics = response.data.content;
					}
				}
			}, function (response) {
			});
		};

		self.setTopTrainers = function () {
			restService.get(self.topTrainersUrl).then(function (response) {
				self.topTrainers = utilitiesService.filterByLikeCount(response.data);
			}, function (response) {
			});
		};

		$scope.$on('topics.refresh', function () {
			self.init();
		});

		self.init();
	}]);