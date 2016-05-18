/**
 * Created by de007ra on 5/7/2016.
 */
var lunchAndLearnDirectives = angular.module('directives', [ 'services']);

lunchAndLearnDirectives.directive('mainTile', function() {
	return {
		templateUrl : '/lunchandlearn/html/main/tile.html',
		replace : true,
		scope: true,
		controller : 'mainTileController as self'
	};
}).controller('mainTileController',
	[ 'restService', function(restService) {
		var self = this;
		self.sectionsCountUrl = restService.appUrl + '/sections/count';

		self.setSectionsCount = function() {
			restService.get(self.sectionsCountUrl).then(function(response) {
				self.sectionsCount = response.data;
			}, function (response) {
				console.log(response);
			});
		}

		self.setSectionsCount();
	}]);