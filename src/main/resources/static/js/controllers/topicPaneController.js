angular.module('controllers').controller('topicPaneController', [
	'$scope', 'topicService', function($scope, topicService) {
		var self = this;

		self.init = function () {
			self.getRecentTopics();
			self.getTopLikesTopics();
			self.getViewedTopics();
		};

		self.getRecentTopics = function() {
			topicService.listTopics({url: '/recent'}).then(function(response) {
				self.recentTopics = {data: response.data.content};
			});
		};

		self.getViewedTopics = function() {
			var ids = topicService.getRecentTopicsId();
			if(!_.isEmpty(ids)) {
				topicService.listTopics({url: '/ids', params: {ids: ids}}).then(function(response) {
					self.viewedTopics = {data: []};
					var topics = response.data.content;
					_.forEach(ids, function(id) {
						var topic = _.find(topics, {'id': id});
						if(!_.isUndefined(topic)) {
							self.viewedTopics.data.push(topic);
						}
					});
				});
			}
		};

		self.getTopLikesTopics = function() {
			topicService.listTopics({url: '/likes'}).then(function(response) {
				self.trendingTopics = {data: response.data.content};
			});
		}
		self.init();

		$scope.$on('topics.refresh', function () {
			self.init();
		})
	}]);