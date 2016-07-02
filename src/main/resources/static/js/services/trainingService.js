/**
 * Created by de007ra on 5/8/2016.
 */
angular.module('services').factory('trainingService', [ 'restService', '$localStorage',
	function(restService, $localStorage) {
	return {
		trainingsUrl: restService.appUrl + '/trainings',
		trainingUrl: restService.appUrl + '/trainings/training',
		maxRecentTrainingsCount: 4,
		//training APIs

		listTrainings: function(config) {
			var url = this.trainingsUrl;
			if(!_.isEmpty(config.url)) {
				url += config.url;
			}
			return restService.get(url, config);
		},

		likeTraining: function(trainingId) {
			if(_.isUndefined(trainingId)) {
				return;
			}
			return restService.post(this.trainingsUrl + '/' + trainingId + '/likes');
		},

		getTrainingsName: function() {
			return restService.get(this.trainingsUrl + '/names')
		},

		addTraining: function(item) {
			return restService.post(this.trainingUrl, item);
		},

		updateTraining: function(item) {
			return restService.put(this.trainingUrl  + "/" +  item.trainingId, item);
		},

		updateTrainingByField: function(trainingId, item) {
			return restService.put(this.trainingUrl  + "/" +  trainingId + '/field', item);
		},

		removeTraining: function(trainingId) {
			return restService.delete(this.trainingUrl + "/" + trainingId);
		},

		getTraining: function(trainingId) {
			return restService.get(this.trainingUrl  + "/" + trainingId);
		},

		getComments: function (trainingId) {
			return restService.get(this.trainingUrl + '/' + trainingId + '/comments');
		},

		removeComment: function(trainingId, commentId) {
			return restService.delete(this.trainingUrl + "/" + trainingId + '/comments/' + commentId);
		},

		removeAttachment: function(trainingId, filename) {
			var config = {params: {name: filename}}
			return restService.delete(this.trainingUrl + "/" + trainingId + '/attachments/file', config);
		},

		removeCommentReply: function(trainingId, commentId, commentReplyId) {
			return restService.delete(this.trainingUrl + "/" + trainingId + '/comments/' + commentId + '/replies/' + commentReplyId);
		},

		addCommentReply: function(trainingId, commentId, newReply) {
			var comment = {name: 'comment', value: newReply};
			return restService.post(this.trainingUrl + "/" + trainingId + '/comments/' + commentId + '/reply', comment);
		},

		getComments: function (trainingId) {
			return restService.get(this.trainingUrl + '/' + trainingId + '/comments');
		},

		getAttachments: function(id) {
			return restService.get(this.trainingUrl + '/' + id + '/attachments');
		},

		addComment: function(id, newComment) {
			var comment = {name: 'comment', value: newComment};
			return restService.post(this.trainingUrl + '/' + id + '/comments', comment);
		},

		getRecentTrainingsId: function() {
			return $localStorage.recentTrainingsId;
		},

		pushRecentTrainingId: function (trainingId) {
			trainingId = parseInt(trainingId);
			if(_.isUndefined(trainingId) || isNaN(trainingId)) {
				return;
			}
			var recentTrainingsId = $localStorage.recentTrainingsId;
			if(_.isEmpty(recentTrainingsId)) {
				recentTrainingsId = [trainingId];
			}
			else {
				var index = recentTrainingsId.indexOf(trainingId);
				if(index != -1) {
					recentTrainingsId.splice(index, 1);
				}
				if(this.maxRecentTrainingsCount < recentTrainingsId.length) {
					recentTrainingsId.splice(recentTrainingsId.length - 1, recentTrainingsId.length - this.maxRecentTrainingsCount);
				}
				for(var count = this.maxRecentTrainingsCount - 1;count >= 1;--count) {
					if(!_.isUndefined(recentTrainingsId[count - 1]) && trainingId != recentTrainingsId[count - 1]) {
						recentTrainingsId[count] = recentTrainingsId[count - 1];
					}
				}
				recentTrainingsId[0] = trainingId;
			}
			$localStorage.recentTrainingsId = recentTrainingsId;

		}
	};
}]);