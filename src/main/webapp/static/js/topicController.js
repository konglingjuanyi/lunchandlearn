var lunchAndLearnControllers = angular.module('controllers', [ 'services', 'ui.bootstrap' ]);

lunchAndLearnControllers.controller('topicController', [ '$scope',
		'$modal', 'restService', function($scope, $modal, restService) {
			var self = this;

			$scope.list = function() {
				restService.listTopics().then(function(response) {
					$scope.topics = response.data;
				});
			}

			$scope.showEdit = function(id) {
				restService.getTopic(id).then(function(response) {
					$scope.topic = response.data;
					$scope.mode = 'edit';
					$scope.showModal();
				});
			}

			$scope.save = function() {
				restService.updateTopic($scope.topic).then(function(response) {
					$scope.mode = null;
					$scope.list();
				});
			}

			$scope.add = function() {
				restService.postTopic($scope.topic).then(function(response) {
					$scope.mode = null;
					$scope.list();
				});
			}

			$scope.showAdd = function(id) {
				$scope.topic = null;
				$scope.mode = 'add';
				$scope.showModal();
			}

			$scope.remove = function(id) {
				restService.deleteTopic(id).then(function(response) {
					$scope.list();
				});
			}

			$scope.list();

			$scope.showModal = function() {
				$scope.opts = {
					templateUrl : '/lunchandlearn/html/topicModal.html',
					controller : 'modalController',
					resolve: {
						data: function () {
							return {item: $scope.topic, mode: $scope.mode};
						}
					}
				}
				var modalInstance = $modal.open($scope.opts);

				modalInstance.result.then(function (selectedItem) {
					$scope.topic = selectedItem;
					if($scope.mode == 'add') {
						$scope.add();
					}
					else if($scope.mode == 'edit') {
						$scope.save();
					}
				}, function () {
					$scope.topic = null;
					$scope.mode = 'edit';
				});
			}
		}]);