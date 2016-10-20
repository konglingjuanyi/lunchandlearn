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
			onRemove: '=?',
			popoverEnable: '='
		},
		link: function(scope) {
			var setTagIcon = function() {
				switch (scope.section) {
					case 'managers':
					case 'employees':
					case 'employeesKnowAbout':
					case 'interestedEmployees':
					case 'trainees':
					case 'trainers':
						scope.tagIcon = 'users_single-05';
						break;
					case 'topics':
					case 'topicsKnown':
					case 'topicsInterestedIn':
					case 'prerequisites':
						scope.tagIcon = 'location_bookmark';
						break;
					case 'trainings':
					case 'trainingsInterestedIn':
					case 'trainingsAttended':
					case 'trainingsImparted':
						scope.tagIcon = 'education_board-51';
						break;
					case 'roles':
						scope.tagIcon = 'clothes_cap';
						break;
					case 'trainingrooms':
						scope.tagIcon = 'ui-1_home-minimal';
						break;
				}
			}
			setTagIcon();
		},
		controller: 'simpleTagController'
	};
}).controller('simpleTagController', ['$scope',
	function($scope) {
		$scope.removeProperty = function (e, obj, prop) {
			e.preventDefault();
			e.stopPropagation();
			delete obj[prop];
			if(_.isFunction($scope.onRemove)) {
				$scope.onRemove($scope.section);
			}
		};
	}]);