/**
 * Created by de007ra on 5/7/2016.
 */
var pbAcademyDirectives = angular.module('directives', [ 'services']);

pbAcademyDirectives.directive('mainTile', function() {
	return {
		templateUrl : '/pbacademy/html/main/tile.html',
		replace : true,
		scope: true,
		controller : 'mainTileController as self'
	};
}).controller('mainTileController',
	[ 'restService', 'utilitiesService', function(restService, utilitiesService) {
		var self = this;
		self.sectionsCountUrl = restService.appUrl + '/sections/count';

		restService.getLoggedInUser().then(function(user) {
			if(user) {
				self.userGuid = user.guid;
				self.isAdmin = utilitiesService.isAdminUser(user.roles);
			}
		});

		self.setSectionsCount = function() {
			restService.get(self.sectionsCountUrl).then(function(response) {
				self.sectionsCount = response.data;
			}, function (response) {
				console.log(response);
			});
		}

		self.setSectionsCount();
	}]);