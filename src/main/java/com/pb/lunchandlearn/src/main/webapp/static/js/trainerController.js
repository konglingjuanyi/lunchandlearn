angular.module('controllers').controller('trainerController', [ '$scope',
		'$modal', 'restService', function($scope, $modal, restService) {
			var self = this;

			$scope.genders = [{id: 'MALE', label: 'Male'}, {id: 'FEMALE', label: 'Female'}];

			$scope.list = function() {
				restService.listTrainers().then(function(response) {
					$scope.trainers = response.data;
				});
			}

			$scope.showEdit = function(id) {
				restService.getTrainer(id).then(function(response) {
					$scope.trainer = response.data;
					$scope.mode = 'edit';
					$scope.showModal();
				});
			}

			$scope.save = function() {
				restService.updateTrainer($scope.trainer).then(function(response) {
					$scope.mode = null;
					$scope.list();
				});
			}

			$scope.add = function() {
				restService.postTrainer($scope.trainer).then(function(response) {
					$scope.mode = null;
					$scope.list();
				});
			}

			$scope.showAdd = function(id) {
				$scope.trainer = null;
				$scope.mode = 'add';
				$scope.showModal();
			}

			$scope.remove = function(id) {
				restService.deleteTrainer(id).then(function(response) {
					$scope.list();
				});
			}

			$scope.list();

			$scope.showModal = function() {
				$scope.opts = {
					templateUrl : '/lunchandlearn/html/trainerModal.html',
					controller : 'modalController',
					resolve: {
						data: function () {
							return {item: $scope.trainer, mode: $scope.mode, genders: $scope.genders};
						}
					}
				}
				var modalInstance = $modal.open($scope.opts);

				modalInstance.result.then(function (selectedItem) {
					$scope.trainer = selectedItem;
					if($scope.mode == 'add') {
						$scope.add();
					}
					else if($scope.mode == 'edit') {
						$scope.save();
					}
				}, function () {
					$scope.trainer = null;
					$scope.mode = 'edit';
				});
			}
		}]);