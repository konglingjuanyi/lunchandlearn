angular.module('controllers').controller('topicsController', [
	'$scope', '$uibModal', 'topicService', 'pagingService', '$q', '$location',
	function($scope, $uibModal, topicService, pagingService, $q, $location) {
		var self = this;

		self.pageSizes = pagingService.pageSizes;
		self.currentPage = 1;
		self.currentPageSize = pagingService.currentPageSize;
		self.maxPageSize = pagingService.maxPageSize;

		self.checkSearch = function($event) {
			if($event.which === 13 || $event.keyCode === 13) {
				self.list();
			}
		};

		var init = function() {
			self.list();
			if ($location.search().add) {
				self.showAdd();
			}
		};

		self.savePageSize = function() {
			pagingService.savePageSize(self.currentPageSize);
		};

		self.setListResult = function(data) {
			self.totalPages = data.totalPages;
			self.totalCount = data.totalElements;
			self.searchedTopics = {totalCount: self.totalCount, data: data.content};
			self.fromCount = (data.size * data.number) + 1;
			self.toCount = (self.fromCount - 1) + data.numberOfElements;
			if(self.currentPageSize > self.totalCount) {
				self.currentPageSize = self.pageSizes[0];
			}
		};

		self.list = function() {
			self.searching = true;
			topicService.listTopics(pagingService.getConfigObj(self)).then(function(response) {
				if(angular.isDefined(response.data)) {
					self.setListResult(response.data);
				}
			}).finally(function() {
				self.searching = false;
			});
		};

		self.showEdit = function(guid) {
			topicService.getTopic(guid).then(function(response) {
				self.topic = response.data;
				self.topic.managers = _.keys(self.topic.managers);
				self.mode = 'edit';
				self.showModalDlg();
			});
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
				$scope.$broadcast('topics.refresh');
				
			});
		};

		$scope.$on('topics.refresh', function () {
			self.list();
		});

		self.showAdd = function() {
			self.topic = {};
			self.mode = 'add';
			self.showModalDlg();
		};

		self.remove = function(guid) {
			var topic = topicService.getTopicByGuid(guid, self.topics);
			self.showConfirmationDlg({msg: 'Topic \'' + topic.name + '\'', guid: guid});
		};

		self.doRemove = function(guid) {
			topicService.removeTopic(guid).then(function (response) {
				self.list();
			}, function (response) {
				console.log('error removeTopic: '+ response)
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
			};
			$uibModal.open(opts).result.then(function (selectedItem) {
				self.doRemove(data.guid)
			}, function () {
				
			});
		};

		self.getSuggestedNames = function(topicName) {
			var deferred = $q.defer();
			topicService.getSuggestedNames(topicName).then(
				function(response) {
					deferred.resolve(response);
				},
				function (error) {
					deferred.reject(false);
				}
			);
			return deferred.promise;
		};

		self.showModalDlg = function () {
			var opts = {
				templateUrl: '/pbacademy/html/topic/topicCreate.html',
				backdrop: 'static',
				controller: 'modalController as self',
				resolve: {
					data: function () {
						return {item: self.topic, mode: self.mode, options: {managers : self.managers,
							getSuggestedNames: self.getSuggestedNames}};
					}
				}
			};
			$uibModal.open(opts).result.then(function (selectedItem) {
				self.topic = selectedItem;
				if(self.mode === 'add') {
					self.add();
				}
				else if(self.mode === 'edit') {
					self.save();
				}
			}, function () {
				
			});
		};

		init();
	}]);