angular.module('services', ['ngStorage']).factory('restService', [ '$http', function($http) {
	return {
		appUrl: '/lunchandlearn',
		//Generic REST Calls
		get: function(url, config) {
			return $http.get(url, config);//return promise
		},

		post: function(url, data) {
			return $http.post(url, data, {headers: {'Content-Type': 'application/json'}});
		},

		put: function(url, data) {
			return $http.put(url, data, {headers: {'Content-Type': 'application/json'}});
		},

		delete: function(url, config) {
			return $http.delete(url, config);
		},

		isResponseOk: function(response) {
			return response.status === 200;
		}
	};
}]);