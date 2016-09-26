angular.module('controllers').controller('trainingsController', [
	'$scope', '$uibModal', 'trainingService', 'pagingService', '$routeParams', 'employeeService', 'topicService',
	'utilitiesService', '$location',
	function($scope, $uibModal, trainingService, pagingService, $routeParams, employeeService, topicService,
			 utilitiesService, $location) {
		var self = this;

		self.pageSizes = pagingService.pageSizes;
		self.currentPage = 1;
		self.currentPageSize = pagingService.currentPageSize;
		self.maxPageSize = pagingService.maxPageSize;
		self.sort = {};

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

		self.sortList = function(fieldName) {
			self.sort.name = fieldName;
			self.sort.direction = self.sort.direction === 'asc' ? 'desc' : 'asc';
			self.list();
		};

		self.setListResult = function(data) {
			self.totalPages = data.totalPages;
			self.totalCount = data.totalElements;
			if(self.currentPageSize > self.totalCount) {
				self.currentPageSize = self.pageSizes[0];
			}
			self.searchedTrainings = {totalCount: self.totalCount, data: data.content};
			_.each(self.searchedTrainings.data, function(training) {
				training.scheduledOn = moment(training.scheduledOn).format('DD-MMM-YY, h:mm a');
			});
			self.fromCount = (data.size * data.number) + 1;
			self.toCount = (self.fromCount - 1) + data.numberOfElements;
		};

		self.list = function() {
			self.searching = true;
			trainingService.listTrainings(pagingService.getConfigObj(self)).then(function(response) {
				if(angular.isDefined(response.data)) {
					self.setListResult(response.data);
				}
			}).finally(function() {
				self.searching = false;
			});
		};

		self.gotoTraining = function(trainingId) {
			$location.path('/trainings/' + trainingId);
		};

		self.save = function() {
			self.training.managers = trainingService.getManagersGuidWithName(self.managers, self.training.managers);
			trainingService.updateTraining(self.training).then(function(response) {
				self.mode = null;
				self.list();
			});
		};

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
/*
			if(self.training.trainees) {
				self.training.trainees = utilitiesService.sortObj(self.training.trainees);
			}
*/
			trainingService.addTraining(self.training).then(function(response) {
				self.mode = null;
				self.list();
				$scope.$broadcast('trainings.refresh');
			});
		};

		$scope.$on('trainings.refresh', function () {
			self.list();
		});

		self.showAdd = function() {
			employeeService.listEmployeesMinimal().then(function (response) {
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

		};

		self.showTopicModalDlg = function () {
			var opts = {
				templateUrl: '/lunchandlearn/html/topic/topicCreate.html',
				backdrop: 'static',
				controller: 'modalController as self',
				resolve: {
					data: function () {
						return {item: {}, mode: 'add'};
					}
				}
			};
			$uibModal.open(opts).result.then(function (selectedItem) {
				self.topic = selectedItem;
				if(self.mode === 'add') {
					self.addTopic();
				}
			}, function () {
				
			});
		};

		self.showTopicAdd = function() {
			self.showTopicModalDlg();
		};

/*		self.addTopic = function() {
			topicService.addTopic(self.topic).then(function(response) {
				self.list();
				$scope.$broadcast('topics.refresh');
			});
		};*/

		self.remove = function(id) {
			var training = trainingService.getTrainingByGuid(id, self.trainings);
			self.showConfirmationDlg({msg: 'Training \'' + training.name + '\'', id: id});
		};

		self.doRemove = function(id) {
			trainingService.removeTraining(id).then(function () {
				self.list();
			}, function (response) {
				console.log('error removeTraining: '+ response)
			});
		};

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
			};

			$uibModal.open(opts).result.then(function () {
				self.doRemove(data.id)
			}, function () {
				
			});
		};

		self.showModalDlg = function () {
			var opts = {
				templateUrl: '/lunchandlearn/html/training/trainingCreate.html',
				backdrop: 'static',
				controller: 'modalController as self',
				resolve: {
					data: function () {
						return {item: self.training, mode: self.mode, options: {trainerId: self.trainerId,
							employees: self.trainers, topics: self.topics, stepperOptions: trainingService.durationStepperOptions
					}};
					}
				}
			};
			$uibModal.open(opts).result.then(function (selectedItem) {
				self.training = selectedItem;
				if(self.mode === 'add') {
					self.add();
				}
				else if(self.mode === 'edit') {
					self.save();
				}
			}, function () {
				
			});
		};

		self.list();
	}]);