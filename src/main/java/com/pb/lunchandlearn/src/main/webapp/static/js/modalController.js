angular.module('controllers').controller('modalController',
	['$scope', '$modalInstance', 'data', function($scope, $modalInstance, data) {
		$scope.mode = data.mode;
		$scope.item = data.item;
		$scope.genders = data.genders;

		$scope.save = function() {
			$modalInstance.close($scope.item);
		};

		$scope.cancel = function() {
			$modalInstance.dismiss('cancel');
		};
	}
]);