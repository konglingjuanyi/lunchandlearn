/**
 * Created by de007ra on 5/7/2016.
 */
angular.module('directives').directive('trainingListMini', function() {
	return {
		templateUrl : '/lunchandlearn/html/training/trainingListMini.html',
		replace : true,
		scope: {
			trainings: '=?',
			trainingHeading: '@',
			onList: '='
		},
		controller: 'trainingListMiniController'
	};
}).controller('trainingListMiniController', ['$scope', function($scope) {

	$scope.trainingStatus = [{label: 'All', code: null}, {label: 'Nominated', code: 'NOMINATED'},
		{label: 'Scheduled', code: 'SCHEDULED'}, {label: 'Completed', code: 'COMPLETED'},
		{label: 'Postponed', code: 'POSTPONED'}, {label: 'Cancelled', code: 'CANCELLED'},];

	$scope.trainingOption = {status: _.find($scope.trainingStatus, {code: null}).code};

	$scope.trainingOption.list = function() {
		$scope.onList($scope.trainingOption.status);
	}
}]);