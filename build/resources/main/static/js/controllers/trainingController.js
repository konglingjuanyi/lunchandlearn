var lunchAndLearnControllers = angular.module('controllers');

lunchAndLearnControllers.controller('trainingController', ['$modal', 'restService', function($modal, restService) {
			var self = this;

			self.list = function() {
				restService.listTopics().then(function(response) {
					self.trainings = response.data;
				});
			}

			self.showEdit = function(id) {
				restService.getTraining(id).then(function(response) {
					self.training = response.data;
					self.mode = 'edit';
					self.showModal();
				});
			}

			self.save = function() {
				restService.updateTraining(self.training).then(function(response) {
					self.mode = null;
					self.list();
				});
			}

			self.add = function() {
				restService.postTraining(self.training).then(function(response) {
					self.mode = null;
					self.list();
				});
			}

			self.showAdd = function(id) {
				self.training = null;
				self.mode = 'add';
				self.showModal();
			}

			self.remove = function(id) {
				restService.deleteTraining(id).then(function(response) {
					self.list();
				});
			}

			self.list();

			self.showModal = function() {
				self.opts = {
					templateUrl : '/lunchandlearn/html/trainingModal.html',
					controller : 'modalController',
					resolve: {
						data: function () {
							return {item: self.training, mode: self.mode};
						}
					}
				}
				var modalInstance = $modal.open(self.opts);

				modalInstance.result.then(function (selectedItem) {
					self.training = selectedItem;
					if(self.mode == 'add') {
						self.add();
					}
					else if(self.mode == 'edit') {
						self.save();
					}
				}, function () {
					self.training = null;
					self.mode = 'edit';
				});
			}
		}]);