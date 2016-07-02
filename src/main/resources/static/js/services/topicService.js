/**
 * Created by de007ra on 5/8/2016.
 */
angular.module('services').factory('topicService', [ 'restService', '$localStorage',
	function(restService, $localStorage) {
	return {
		topicsUrl: restService.appUrl + '/topics',
		topicUrl: restService.appUrl + '/topics/topic',
		//topic APIs
		maxRecentTopicsCount: 4,

		listTopics: function(config) {
			var url = this.topicsUrl;
			if(!_.isEmpty(config) && !_.isEmpty(config.url)) {
				url += config.url;
			}
			return restService.get(url, config);
		},

		likeTopic: function(topicId) {
			if(_.isUndefined(topicId)) {
				return;
			}
			return restService.post(this.topicsUrl + '/' + topicId + '/likes');
		},

		getTopicsName: function() {
			return restService.get(this.topicsUrl + '/names')
		},

		addTopic: function(item) {
			return restService.post(this.topicUrl, item);
		},

		updateTopic: function(item) {
			return restService.put(this.topicUrl  + "/" +  item.topicId, item);
		},

		updateTopicByField: function(topicId, item) {
			return restService.put(this.topicUrl  + "/" +  topicId + '/field', item);
		},

		removeTopic: function(topicId) {
			return restService.delete(this.topicUrl + "/" + topicId);
		},

		listTrainings: function(type, topicId) {
			type = angular.isUndefined(type) ? '' : type;
			return restService.get(this.topicUrl  + "/" + topicId + '/trainings/' + type);
		},

		getTopic: function(topicId) {
			return restService.get(this.topicUrl  + "/" + topicId);
		},

		getRecentTopicsId: function() {
			return $localStorage.recentTopicsId;
		},

		pushRecentTopicId: function (topicId) {
			topicId = parseInt(topicId);
			if(_.isUndefined(topicId) || !_.isNumber(topicId)) {
				return;
			}
			var recentTopicsId = $localStorage.recentTopicsId;
			if(_.isEmpty(recentTopicsId)) {
				recentTopicsId = [topicId];
			}
			else {
				var index = recentTopicsId.indexOf(topicId);
				if(index != -1) {
					recentTopicsId.splice(index, 1);
				}
				if(this.maxRecentTopicsCount < recentTopicsId.length) {
					recentTopicsId.splice(recentTopicsId.length - 1, recentTopicsId.length - this.maxRecentTopicsCount);
				}
				for(var count = this.maxRecentTopicsCount - 1;count >= 1;--count) {
					if(!_.isUndefined(recentTopicsId[count - 1]) && topicId != recentTopicsId[count - 1]) {
						recentTopicsId[count] = recentTopicsId[count - 1];
					}
				}
				recentTopicsId[0] = topicId;
			}
			$localStorage.recentTopicsId = recentTopicsId;
		}
	};
}]);