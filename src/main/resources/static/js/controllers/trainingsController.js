angular.module('controllers').controller('trainingsController', [
	'$scope', '$uibModal', 'trainingService', 'pagingService', '$routeParams', 'employeeService', 'topicService', 'utilitiesService',
	function($scope, $uibModal, trainingService, pagingService, $routeParams, employeeService, topicService, utilitiesService) {
		var self = this;

		self.pageSizes = pagingService.pageSizes;
		self.currentPage = 1;
		self.currentPageSize = pagingService.currentPageSize;
		self.maxPages = pagingService.maxPageSize;

		self.checkSearch = function($event) {
			if($event.which === 13 || $event.keyCode === 13) {
				self.list();
			}
		};

		self.listWithFilterBy = function (filterBy) {
			self.filterBy = filterBy;
			self.list();
		};

		self.savePageSize = function() {
			pagingService.savePageSize(self.currentPageSize);
		};

		self.setListResult = function(data) {
			self.totalPages = data.totalPages;
			self.totalCount = data.totalElements;
			self.searchedTrainings = {totalCount: self.totalCount, data: data.content};
			self.fromCount = (data.size * data.number) + 1;
			self.toCount = (self.fromCount - 1) + data.numberOfElements;
		};

		self.list = function() {
			trainingService.listTrainings(pagingService.getConfigObj(self)).then(function(response) {
				self.setListResult(response.data);
			});
		}

		self.showEdit = function(id) {
			trainingService.getTraining(id).then(function(response) {
				self.training = response.data;
				self.training.managers = _.keys(self.training.managers);
				self.mode = 'edit';
				self.showModalDlg();
			});
		}

		self.save = function() {
			self.training.managers = trainingService.getManagersGuidWithName(self.managers, self.training.managers);
			trainingService.updateTraining(self.training).then(function(response) {
				self.mode = null;
				self.list();
			});
		}

		self.add = function() {
			var date = moment(self.training.scheduledOn);
			if(date.isValid()) {
				self.training.scheduledOn = utilitiesService.toISODateString(date);
			}
			if(self.training.topics) {
				self.training.topics = utilitiesService.sortObj(self.training.topics);
			}
			if(self.training.trainers) {
				self.training.trainers = utilitiesService.sortObj(self.training.trainers);
			}
			if(self.training.trainees) {
				self.training.trainees = utilitiesService.sortObj(self.training.trainees);
			}

			trainingService.addTraining(self.training).then(function(response) {
				self.mode = null;
				self.list();
				$scope.$broadcast('trainings.refresh');
			});
		};

		$scope.$on('trainings.refresh', function () {
			self.list();
		})

		self.showAdd = function() {
			employeeService.listEmployees().then(function (response) {
				self.trainers = response.data.content;
				topicService.listTopics().then(function(res) {
					self.training = {scheduledOn: {startDate: moment().second(0).minute(0).hour(12)}};
					self.topics = res.data.content;
					self.mode = 'add';
					self.showModalDlg();
					if(!_.isEmpty($routeParams.topicId)) {
						self.topicId = $routeParams.topicId;
					}
				});
			});

		}

		self.remove = function(id) {
			var training = trainingService.getTrainingByGuid(id, self.trainings);
			self.showConfirmationDlg({msg: 'Training \'' + training.name + '\'', id: id});
		}

		self.doRemove = function(id) {
			trainingService.removeTraining(id).then(function (response) {
				self.list();
			}, function (response) {
				console.log('error removeTraining: '+ response)
			});
		}

		self.showConfirmationDlg = function (data) {
			var opts = {
				templateUrl: '/lunchandlearn/html/main/confirmationDlg.html',
				controller: 'modalController as self',
				backdrop: 'static',
				resolve: {
					data: function () {
						return {msg: data.msg, item: {id: data.id}};
					}
				}
			}

			$uibModal.open(opts).result.then(function (selectedItem) {
				self.doRemove(data.id)
			}, function () {
				console.log('confirmation modal cancelled')
			});
		}

		self.showModalDlg = function () {
			var opts = {
				templateUrl: '/lunchandlearn/html/training/trainingCreate.html',
				backdrop: 'static',
				controller: 'modalController as self',
				resolve: {
					data: function () {
						return {item: self.training, mode: self.mode, options: {trainerId: self.trainerId,
							employees: self.trainers, topics: self.topics}};
					}
				}
			}
			$uibModal.open(opts).result.then(function (selectedItem) {
				self.training = selectedItem;
				if(self.mode === 'add') {
					self.add();
				}
				else if(self.mode === 'edit') {
					self.save();
				}
			}, function () {
				console.log('confirmation modal cancelled')
			});
		}

		self.list();
	}]);