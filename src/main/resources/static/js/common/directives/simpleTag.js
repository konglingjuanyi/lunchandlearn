/**
 * Created by de007ra on 5/7/2016.
 */
angular.module('directives').directive('simpleTag', function() {
	return {
		templateUrl : '/lunchandlearn/html/main/simpleTag.html',
		replace : true,
		scope: {
			items: '=',
			linkable: '=',
			section: '=',
			sectionUrl: '=',
			onRemove: '=?'
		},
		controller: 'simpleTabController as self'
	};
}).controller('simpleTabController', ['$scope',
	function($scope) {
		var self = this;

		self.removeProperty = function (e, obj, prop) {
			e.preventDefault();
			e.stopPropagation();
			delete obj[prop];
			if(_.isFunction($scope.onRemove)) {
				$scope.onRemove($scope.section);
			}
		};
	}]);