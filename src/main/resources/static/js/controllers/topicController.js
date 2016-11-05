angular.module('controllers').controller('topicController', [
	'$uibModal', 'topicService', 'pagingService', '$routeParams', 'restService', 'utilitiesService',
	'$scope', '$q', function($uibModal, topicService, pagingService, $routeParams,
					   restService, utilitiesService, $scope, $q) {
		var self = this;
		var prevItem = null;

		self.init = function () {
			if(!_.isEmpty($routeParams.topicId)) {
				self.getTopic($routeParams.topicId);
				topicService.pushRecentTopicId($routeParams.topicId);
				self.getTrainings('', $routeParams.topicId);
			}
			self.list();
		};

		self.getTrainings = function (type, topicId) {
			topicService.listTrainings(type, topicId).then(function(response) {
				if(angular.isDefined(response.data)) {
					if (type === 'upcoming') {
						self.upcomingTrainings = response.data;
					}
					else if (type === 'passed') {
						self.passedTrainings = response.data;
					}
				}
			});
		};

		self.setListResult = function(data) {
			if(angular.isDefined(data)) {
				self.totalPages = data.totalPages;
				self.totalCount = data.totalElements;
				self.topics = {totalCount: self.totalCount, data: data.content};
				self.fromCount = (data.size * data.number) + 1;
				self.toCount = (self.fromCount - 1) + data.numberOfElements;
			}
		};

		self.list = function() {
			if(!_.isEmpty(self.searchTerm)) {
				return self.search();
			}
			topicService.listTopics({params: {page: self.currentPage - 1, size: self.currentPageSize}}).then(function(response) {
				self.setListResult(response.data);
			});
		}

		self.getTopic = function(topicId) {
			topicService.getTopic(topicId).then(function(response) {
				if(_.isEmpty(response.data)) {
					self.error = true;
					self.errorMsg = 'Topic detail not found!';
					return;
				}
				if(angular.isDefined(response.data)) {
					self.item = response.data;

					var obj = {};
					obj[self.item.createdByGuid] = self.item.createdByName;
					self.createdBy = obj;

					self.employeesKnowAboutEmpty = _.isEmpty(self.item.employeesKnowAbout);
					updateTrainingsList();
					utilitiesService.setLastModifiedBy(self, self.item);
					prevItem = _.cloneDeep(self.item);
				}
			}, function (error) {
				self.error = true;
				self.errorMsg = error.data.message;
			});
		}

		var updateTrainingsList = function () {
			self.otherTrainings = {};
			self.upcomingTrainings = {};
			self.closedTrainings = {};
			self.unscheduledTrainings = {};
			self.unscheduledTrainingsCount = 0;
			self.closedTrainingsCount = 0;
			self.upcomingTrainingsCount = 0;
			self.otherTrainingsCount = 0;
			if(self.item) {
				_.each(self.item.trainings, function (training) {
					switch(training.status) {
						case 'NOMINATED':
							self.unscheduledTrainings[training.id] = training.name;
							++self.unscheduledTrainingsCount;
							break;
						case 'SCHEDULED':
							self.upcomingTrainings[training.id] = training.name;
							++self.upcomingTrainingsCount;
							break;
						case 'COMPLETED':
						case 'CLOSED':
							self.closedTrainings[training.id] = training.name;
							++self.closedTrainingsCount;
							break;
						default:
							self.otherTrainings[training.id] = training.name;
							++self.otherTrainingsCount;
					}
				})
			}
		}

		var afterSave = function(fieldName) {
			utilitiesService.setEditable(self, fieldName, false);
			utilitiesService.setLastModifiedBy(self);
			setPrevItemField(fieldName);
			self['error' + _.upperFirst(fieldName)] = undefined;
		};

		self.saveByField = function(fieldName) {
			var data = {name: fieldName, value: _.get(self.item, fieldName)};
			var deferred = $q.defer();
			if(fieldName === 'name' && _.isEmpty(data.value)) {
				self.errorName = 'Name can\'t be empty';
				deferred.resolve(false);
			}
			else if(_.isEqual(data.value, prevItem[fieldName])) {
				afterSave(fieldName);
				deferred.resolve(true);
			}
			if(deferred.promise.$$state.status === 1) {
				return deferred.promise;
			}
			return topicService.updateTopicByField(self.item.id, data).then(function(response) {
				if(restService.isResponseOk(response)) {
					afterSave(fieldName);
					return $q.resolve(true);
				}
				return $q.resolve(false);
			});
		};

		self.setEditStatus = function(fieldName, status) {
			if(!status) {
				resetPrevItemField(fieldName);
			}
			self['error' + fieldName] = undefined;
			utilitiesService.setEditable(self, fieldName, status);
		};

		self.save = function() {
			self.topic.managers = topicService.getManagersGuidWithName(self.managers, self.topic.managers);
			topicService.updateTopic(self.topic).then(function(response) {
				self.mode = null;
				self.list();
			});
		};

		self.add = function() {
			topicService.addTopic(self.topic).then(function(response) {
				self.mode = null;
				self.list();
			});
		};

		self.showConfirmationDlg = function (data) {
			var opts = {
				templateUrl: '/pbacademy/html/main/confirmationDlg.html',
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
				
			});
		};

		self.showTrainingAdd = function() {
			self.training = {};
			self.mode = 'add';
			self.showModalDlg();
		};

		self.showModalDlg = function () {
			var opts = {
				templateUrl: '/pbacademy/html/training/trainingCreate.html',
				backdrop: 'static',
				controller: 'modalController as self',
				resolve: {
					data: function () {
						return {item: self.training, mode: self.mode, options: {topicId: self.topicId}};
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
				
			});
		};

		var setPrevItemField = function(fieldName) {
			var fldName = _.lowerFirst(fieldName);
			prevItem[fldName] = _.cloneDeep(self.item[fldName]);
		};

		var resetPrevItemField = function(fieldName) {
			var fldName = _.lowerFirst(fieldName);
			self.item[fldName] = prevItem[fldName];
		};

		$scope.$on('topics.refresh', function () {
			topicService.getLikesCount(self.item.id).then(function(result) {
				if(angular.isDefined(result.data)) {
					self.item.likesCount = result.data;
				}
			});
		});

		self.saveOnEnterKey = function($event, fieldName) {
			if($event.which === 13) {
				$event.stopPropagation();
				$event.preventDefault();
				self.saveByField(fieldName);
			}
			else {
				self['error' + _.upperFirst(fieldName)] = undefined;
			}
		};

		self.cancelOnEnterKey = function($event, fieldName) {
			if($event.keyCode === 27) {
				$event.stopPropagation();
				$event.preventDefault();
				self.setEditStatus(fieldName);
			}
		};

		self.init();
	}]);