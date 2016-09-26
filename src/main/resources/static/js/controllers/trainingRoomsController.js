angular.module('controllers').controller('trainingRoomsController', [
	'$scope', '$uibModal', 'trainingRoomService', 'pagingService', 'restService', 'utilitiesService',
	function($scope, $uibModal, trainingRoomService, pagingService, restService, utilitiesService) {
		var self = this;

		self.pageSizes = pagingService.pageSizes;
		self.currentPage = 1;
		self.currentPageSize = pagingService.currentPageSize;
		self.maxPageSize = pagingService.maxPageSize;
		self.locations = ['Noida', 'Pune'];
		self.sort = {};

		restService.getLoggedInUser().then(function(user) {
			if(user) {
				self.userGuid = user.guid;
				self.isAdmin = utilitiesService.isAdminUser(user.roles);
			}
		});

		self.checkSearch = function($event) {
			if($event.which === 13 || $event.keyCode === 13) {
				self.list();
			}
		};

		self.remove = function(id) {
			trainingRoomService.getTrainingRoom(id).then(function(response) {
				self.showConfirmationDlg({msg: 'Training Room \'' + response.name + '\'', id: id});
			});
		}

		self.sortList = function(fieldName) {
			self.sort.name = fieldName;
			self.sort.direction = self.sort.direction === 'asc' ? 'desc' : 'asc';
			self.list();
		};

		self.savePageSize = function() {
			pagingService.savePageSize(self.currentPageSize);
		};

		self.setListResult = function(data) {
			self.totalPages = data.totalPages;
			self.totalCount = data.totalElements;
			self.searchedTrainingRooms = {totalCount: self.totalCount, data: data.content};
			self.fromCount = (data.size * data.number) + 1;
			self.toCount = (self.fromCount - 1) + data.numberOfElements;
		};

		self.list = function() {
			self.searching = true;
			trainingRoomService.listTrainingRooms(pagingService.getConfigObj(self)).then(function(response) {
				if(angular.isDefined(response.data)) {
					self.setListResult(response.data);
				}
			}).finally(function() {
				self.searching = false;
			});
		}

		self.showEdit = function(id) {
			trainingRoomService.getTrainingRoom(id).then(function(response) {
				self.trainingRoom = response.data;
				self.mode = 'edit';
				self.showModalDlg();

			});
		};

		self.save = function() {
			trainingRoomService.updateTrainingRoom(self.trainingRoom).then(function() {
				self.mode = null;
				self.list();
			});
		};

		self.add = function() {
			trainingRoomService.addTrainingRoom(self.trainingRoom).then(function() {
				self.mode = null;
				self.list();
				$scope.$broadcast('trainingRooms.refresh');
				
			});
		};

		$scope.$on('trainingRooms.refresh', function () {
			self.list();
		});

		var getRoomById = function (id) {
			var selRoom = undefined;
			_.each(self.searchedTrainingRooms.data, function (room) {
				if(room.id === id) {
					selRoom = room;
					return false;
				}
			});
			return selRoom;
		};

		self.showAdd = function() {
			self.trainingRoom = {};
			self.mode = 'add';
			self.showModalDlg();
		};

		self.remove = function(id) {
			var trainingRoom = trainingRoomService.getTrainingRoomById(id, self.trainingRooms);
			self.showConfirmationDlg({msg: 'Training Room \'' + trainingRoom.name + '\'', id: id});
		};

		self.doRemove = function(id) {
			trainingRoomService.removeTrainingRoom(id).then(function () {
				self.list();
			}, function (response) {
				console.log('error removeTrainingRoom: '+ response)
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
			}
			$uibModal.open(opts).result.then(function () {
				self.doRemove(data.id)
			}, function () {
				
			});
		};

		self.showModalDlg = function () {
			var opts = {
				templateUrl: '/lunchandlearn/html/trainingRoom/trainingRoomCreateEdit.html',
				backdrop: 'static',
				controller: 'modalController as self',
				resolve: {
					data: function () {
						return {item: self.trainingRoom, mode: self.mode, options: {locations : self.locations}};
					}
				}
			}
			$uibModal.open(opts).result.then(function (selectedItem) {
				self.trainingRoom = selectedItem;
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