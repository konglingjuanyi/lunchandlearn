angular.module('services', []).factory('restService', [ '$http', function($http) {
	return {
		//Topic APIs
		listTopics: function() {
			return this.get(this.getFullUrl('topics/'));
		},

		addTopic: function(item) {
			return this.post(this.getFullUrl('topics/' + item.id), item);
		},

		updateTopic: function(item) {
			return this.put(this.getFullUrl('topics/' + item.id), item);
		},

		deleteTopic: function(id) {
			return this.delete(this.getFullUrl('topics/' + id));
		},

		getTopic: function(id) {
			return this.get(this.getFullUrl('topics/' + id));
		},


		//Generic REST Calls
		get: function(url) {
			return $http.get(url);//return promise
		},

		post: function(url, data) {
			return $http.post(url, data, {headers: {'Content-Type': 'application/json'}});
		},

		put: function(url, data) {
			return $http.put(url, data, {headers: {'Content-Type': 'application/json'}});
		},

		delete: function(url) {
			return $http.delete(url);
		},

		getFullUrl: function(relativePath) {
			return '/lunchandlearn/' + relativePath;
		},

		//Trainer APIs
		listTrainers: function() {
			return this.get(this.getFullUrl('trainers/'));
		},

		addTrainer: function(item) {
			return this.post(this.getFullUrl('trainers/' + item.id), item);
		},

		updateTrainer: function(item) {
			return this.put(this.getFullUrl('trainers/' + item.id), item);
		},

		deleteTrainer: function(id) {
			return this.delete(this.getFullUrl('trainers/' + id));
		},

		getTrainer: function(id) {
			return this.get(this.getFullUrl('trainers/' + id));
		}
	};

}]);