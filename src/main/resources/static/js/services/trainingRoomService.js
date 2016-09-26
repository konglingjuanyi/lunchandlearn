/**
 * Created by de007ra on 5/8/2016.
 */
angular.module('services').factory('trainingRoomService', [ 'restService', 'utilitiesService',
	function(restService, utilitiesService) {
	var trainingRoomService = {
		getTrainingRoomById: function(id, trainingRoomArray) {
			return _.find(trainingRoomArray, function (room) {
				return room.id === id;
			});
		},

		trainingRoomsUrl: restService.appUrl + '/trainingrooms',
		trainingRoomUrl: restService.appUrl + '/trainingrooms/trainingroom',
		//TrainingRoom APIs
		listTrainingRooms: function(config) {
			return restService.get(this.trainingRoomsUrl, config);
		},

		listTrainingRoomsBrief: function() {
			return restService.get(this.trainingRoomsUrl + '/brief');
		},

		addTrainingRoom: function(item) {
			return restService.post(this.trainingRoomUrl, item);
		},

		updateTrainingRoom: function(item) {
			return restService.put(this.trainingRoomUrl, item);
		},

		removeTrainingRoom: function(id) {
			return restService.delete(this.trainingRoomUrl + "/" + id);
		},

		getTrainingRoom: function(id) {
			return restService.get(this.trainingRoomUrl  + "/" + id);
		}
	};
	return trainingRoomService;
}]);