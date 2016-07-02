angular.module('controllers').controller('trainingController', ['$scope', '$uibModal', 'trainingService', 'pagingService',
	'$routeParams', 'restService', 'utilitiesService', 'employeeService', 'topicService', 'Upload', '$timeout',
	function($scope, $uibModal, trainingService, pagingService, $routeParams, restService, utilitiesService,
			 employeeService, topicService, Upload, $timeout) {
		var self = this;
		$scope.comments = [];
		self.item = {};
		self.isDatePickerOpen = false;
		self.datePickerOptions = {
			'show-button-bar': false,
			'showWeeks': false,
			// 'minDate': new Date(),
			maxDate: moment().add(3, 'M').toDate()
		};
		self.openDatePicker = function (e) {
			e.preventDefault();
			e.stopPropagation();
			self.isDatePickerOpen = true;
		};

		$scope.selected = {};

		self.init = function () {
			if (!_.isEmpty($routeParams.trainingId)) {
				self.trainingId = $routeParams.trainingId;
				self.getTraining();
				self.getAttachments();
				trainingService.pushRecentTrainingId(self.trainingId);
				// self.getTrainings('');
			}
			employeeService.listEmployees().then(function (response) {
				self.trainers = response.data.content;
			});
			topicService.listTopics().then(function (res) {
				self.topics = res.data.content;
			});

		};

		$scope.$watch('selected.trainer', function () {
			if ($scope.selected.trainer) {
				self.item.trainers = utilitiesService.addUnique(self.item.trainers, $scope.selected.trainer, 'guid', 'name');
				self.saveByField('trainers');
			}
		});

		$scope.$watch('selected.topic', function () {
			if ($scope.selected.topic && (!self.item.prerequisites || !self.item.prerequisites[$scope.selected.topic.id])) {
				self.item.topics = utilitiesService.addUnique(self.item.topics, $scope.selected.topic, 'id', 'name');
				self.saveByField('topics');
			}
		});

		$scope.$watch('selected.prerequisite', function () {
			if ($scope.selected.prerequisite && (!self.item.topics || !self.item.topics[$scope.selected.prerequisite.id])) {
				self.item.prerequisites = utilitiesService.addUnique(self.item.prerequisites, $scope.selected.prerequisite, 'id', 'name');
				self.saveByField('prerequisites');
			}
		});

		$scope.$watch('scheduledOn', function () {
			$scope.scheduledOnView = '';
			if ($scope.scheduledOn) {
				$scope.scheduledOnView = moment($scope.scheduledOn).format('DD-MMM-YY, HH:mm A');
			}
		});

/*
		self.getTrainings = function (type) {
			trainingService.listTrainings(type, self.trainingId).then(function (response) {
				if (type === 'upcoming') {
					self.upcomingTrainings = response.data;
				}
				else if (type === 'passed') {
					self.passedTrainings = response.data;
				}
			});
		};

*/
		self.setListResult = function (data) {
			self.totalPages = data.totalPages;
			self.totalCount = data.totalElements;
			self.searchedTrainings = {totalCount: self.totalCount, data: data.content};
			self.fromCount = (data.size * data.number) + 1;
			self.toCount = (self.fromCount - 1) + data.numberOfElements;
		};

		self.getTraining = function () {
			trainingService.getTraining(self.trainingId).then(function (response) {
				self.item = _.defaultsDeep(self.item, response.data);
				if (self.item.scheduledOn) {
					$scope.scheduledOn = moment(self.item.scheduledOn).toDate();
					self.attachmentUploadUrl = trainingService.trainingUrl + '/' + self.item.id + '/attachment';
				}
			}, function (error) {
				self.error = true;
				self.errorMsg = error.data.message;
			});
		}

		self.getAttachments = function () {
			trainingService.getAttachments(self.trainingId).then(function (response) {
				self.item.attachments = response.data;
			});
		}

		self.saveByField = function (fieldName) {
			var data = {name: fieldName, value: _.get(self.item, fieldName)};
			switch (fieldName) {
				case "scheduledOn":
					var date = moment($scope.scheduledOn);
					var date2 = moment(self.item.scheduledOn);
					var diff = date.diff(date2, 'minutes');
					if (!self.isDatePickerOpen || !date.isValid() || isNaN(diff) || diff === 0) {
						return;
					}
					data.value = utilitiesService.toISODateString(date);
					break;
			}
			trainingService.updateTrainingByField(self.item.id, data).then(function (response) {
				if (restService.isResponseOk(response)) {
					utilitiesService.setEditable(self, fieldName, false);
				}
			});
		}

		self.showConfirmationDlg = function (data) {
			var opts = {
				templateUrl: '/lunchandlearn/html/main/confirmationDlg.html',
				controller: 'modalController as self',
				backdrop: 'static',
				resolve: {
					data: function () {
						return {msg: data.msg, item: {guid: data.guid}};
					}
				}
			}
			$uibModal.open(opts).result.then(function (selectedItem) {
				self.doRemove(data.guid)
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
						return {item: self.training, mode: self.mode, options: {managers: self.managers}};
					}
				}
			}
			$uibModal.open(opts).result.then(function (selectedItem) {
				self.training = selectedItem;
				if (self.mode === 'add') {
					self.add();
				}
				else if (self.mode === 'edit') {
					self.save();
				}
			}, function () {
				console.log('confirmation modal cancelled')
			});
		};

		self.showTrainingAdd = function () {
			self.training = {};
			self.mode = 'add';
			self.showModalDlg();
		};

		self.showModalDlg = function () {
			var opts = {
				templateUrl: '/lunchandlearn/html/training/trainingCreate.html',
				backdrop: 'static',
				controller: 'modalController as self',
				resolve: {
					data: function () {
						return {item: self.training, mode: self.mode, options: {trainingId: self.trainingId}};
					}
				}
			}
			$uibModal.open(opts).result.then(function (selectedItem) {
				self.training = selectedItem;
				if (self.mode === 'add') {
					self.add();
				}
				else if (self.mode === 'edit') {
					self.save();
				}
			}, function () {
				console.log('confirmation modal cancelled')
			});
		};

		self.uploadFiles = function(attachedFiles, errFiles) {
			self.attachmentUploadErrorMsg = undefined;
			self.attachedFiles = attachedFiles;
			self.errFiles = errFiles;
			angular.forEach(attachedFiles, function (file) {
				file.upload = Upload.upload({
					url: self.attachmentUploadUrl,
					data: {file: file}
				});

				file.upload.then(function (response) {
					$timeout(function () {
						file.result = response.data;
						self.attachedFiles = undefined;
							self.getAttachments();
					});
				}, function (response) {
					self.attachedFiles = undefined;
					if (response.status > 0)
						self.attachmentUploadErrorMsg = response.data.message;
				}, function (evt) {
					file.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
				});
			});
		}

		self.removeAttachment = function (filename) {
			trainingService.removeAttachment(self.trainingId, filename).then(function (response) {
				self.attachmentUploadErrorMsg = undefined;
				self.attachedFiles = undefined;
				self.getAttachments();
			});
		}

		self.init();
	}]);