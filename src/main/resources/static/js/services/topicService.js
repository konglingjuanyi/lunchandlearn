/**
 * Created by de007ra on 5/8/2016.
 */
angular.module('services').factory('topicService', [ 'restService', function(restService) {
	return {
		getTopicByGuid: function(guid, topicArray) {
			return _.find(topicArray, function (emloyee) {
				return emloyee.guid === guid;
			});
		},

		topicsUrl: restService.appUrl + '/topics',
		topicUrl: restService.appUrl + '/topics/topic',
		//topic APIs
		listTopics: function(config) {
			return restService.get(this.topicsUrl, config);
		},

		getTopicsName: function() {
			return restService.get(this.topicsUrl + '/names')
		},

		addTopic: function(item) {
			return restService.post(this.topicUrl, item);
		},

		updateTopic: function(item) {
			return restService.put(this.topicUrl  + "/" +  item.guid, item);
		},

		removeTopic: function(guid) {
			return restService.delete(this.topicUrl + "/" + guid);
		},

		getTopic: function(guid) {
			return restService.get(this.topicUrl  + "/" + guid);
		}
	};
}]);