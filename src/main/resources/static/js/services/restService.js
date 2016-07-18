angular.module('services', ['ngStorage']).factory('restService', [ '$http', '$q', '$rootScope', '$location',
	function($http, $q, $rootScope, $location) {
	var restService = {
		self: this,
		appUrl: '/lunchandlearn',

		getLoggedInUser: function () {
			var deferred = $q.defer();
			if($rootScope.user) {
				deferred.resolve($rootScope.user);
			}
			else {
				return restService.get(restService.appUrl + '/user').then(function (response) {
					if (!_.isEmpty(response)) {
						$rootScope.authenticated = true;
						$rootScope.user = response.data;
						if($location.path() == '/login') {
							$location.path('/').search({logout: null});
						}
						$q.resolve($rootScope.user);
					} else {
						$rootScope.authenticated = false;
						$location.path('/login');
						$q.resolve(undefined);
					}
				});
			}
			return deferred.promise;
		},

		//Generic REST Calls
		get: function(url, config) {
			return $http.get(url, config);//return promise
		},

		post: function(url, data) {
			return $http.post(url, data, {headers: {'Content-Type': 'application/json'}});
		},

		post: function(url, data, headers) {
			return $http.post(url, data, headers);
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
	return restService;
}]).factory('loginInterceptor', [ '$rootScope', '$location', '$q', function($rootScope, $location, $q) {
	return {
		response: function(response) {
			if(response.status === 200 && _.isString(response.data) &&
				response.data.indexOf('<html><head><title>Login Page</title></head>') > -1) {
				if($location.path() !== '/login') {
					$location.path('/login');
				}
				return $q.resolve({});
			}
			return $q.resolve(response);
		}
	};
}]);