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
}).controller('trainingListMiniController', ['$scope', 'trainingStatus', function($scope, trainingStatus) {
	$scope.trainingStatus = trainingStatus;
		$scope.trainingOption = {status: _.find($scope.trainingStatus, {code: null}).code};

	$scope.trainingOption.list = function() {
		$scope.onList($scope.trainingOption.status);
	}
}]);