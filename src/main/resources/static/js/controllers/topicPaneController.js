angular.module('controllers').controller('topicPaneController', [
	'$scope', 'topicService', 'utilitiesService', function($scope, topicService, utilitiesService) {
		var self = this;

		self.init = function () {
			self.getRecentTopics();
			self.getTopLikesTopics();
			self.getViewedTopics();
		};

		self.getRecentTopics = function() {
			topicService.listTopics({url: '/recent'}).then(function(response) {
				if(angular.isDefined(response.data)) {
					self.recentTopics = {data: response.data.content};
				}
			});
		};

		self.getViewedTopics = function() {
			var ids = topicService.getRecentTopicsId();
			if(!_.isEmpty(ids)) {
				topicService.listTopics({url: '/ids', params: {ids: ids}}).then(function(response) {
					if(angular.isDefined(response.data)) {
						self.viewedTopics = {data: []};
						var topics = response.data.content;
						_.forEach(ids, function (id) {
							var topic = _.find(topics, {'id': id});
							if (!_.isUndefined(topic)) {
								self.viewedTopics.data.push(topic);
							}
						});
					}
				});
			}
		};

		self.getTopLikesTopics = function() {
			topicService.listTopics({url: '/likes'}).then(function(response) {
				if(angular.isDefined(response.data)) {
					self.trendingTopics = {data: utilitiesService.filterByLikeCount(response.data.content)};
				}
			});
		}
		self.init();

		$scope.$on('topics.refresh', function () {
			self.init();
		})
	}]);