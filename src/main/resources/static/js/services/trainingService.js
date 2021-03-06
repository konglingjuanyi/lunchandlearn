/**
 * Created by de007ra on 5/8/2016.
 */
angular.module('services').factory('trainingService', [ 'restService', '$localStorage', 'utilitiesService', '$rootScope',
	function(restService, $localStorage, utilitiesService, $rootScope) {
	var trainingService = {
		trainingsUrl: restService.appUrl + '/trainings',
		trainingUrl: restService.appUrl + '/trainings/training',
		maxRecentTrainingsCount: 4,
		//training APIs

		ratingsRows: [{label: 'Over All Expectation', code: 'overallExpectation', color: 'blue'},
		{label: 'Relevancy', code: 'relevancy', color: 'crimson'},
		{label: 'Applicability', code: 'applicability', color: 'yellowgreen'},
		{label: 'Appropriateness', code: 'appropriateness', color: 'blueviolet'},
		{label: 'Facilitation', code: 'facilitation', color: 'cadetblue'},
		{label: 'Responsiveness', code: 'answerAllQueries', color: 'darkturquoise'},
		{label: 'Preparedness', code: 'preparedness', color: 'tomato'}],

		trainingTypes: [{id: 'INTERNAL', label: 'Internal'}, {id: 'EXTERNAL', label: 'External'}],

		listTrainings: function(config) {
			var url = this.trainingsUrl;
			if(!_.isEmpty(config.url)) {
				url += config.url;
			}
			return restService.get(url, config);
		},

		isEditable: function (training) {
			return restService.getLoggedInUser().then(function(user) {
				if(utilitiesService.isAdminUser(user.roles)) {
					return true;
				}
				var currentUserGuid = user.guid.toUpperCase();
				return training && (currentUserGuid === training.createdByGuid
					|| (training.trainers && angular.isDefined(training.trainers[currentUserGuid])));
			});
		},

		likeTraining: function(trainingId) {
			if(_.isUndefined(trainingId)) {
				return;
			}
			return restService.post(this.trainingsUrl + '/' + trainingId + '/likes');
		},

		getLikesCount: function(trainingId) {
			return restService.get(this.trainingsUrl + '/' + trainingId + '/likes');
		},

		getTrainingsName: function() {
			return restService.get(this.trainingsUrl + '/names')
		},

		getTrainingLocations: function() {
			return restService.get(this.trainingsUrl + '/locations')
		},

		addTraining: function(item) {
			return restService.post(this.trainingUrl, item);
		},

		updateTraining: function(item) {
			return restService.put(this.trainingUrl  + '/' +  item.trainingId, item);
		},

		updateTrainingByField: function(trainingId, item) {
			return restService.put(this.trainingUrl  + '/' +  trainingId + '/field', item);
		},

		removeTraining: function(trainingId) {
			return restService.delete(this.trainingUrl + '/' + trainingId);
		},

		getTraining: function(trainingId) {
			return restService.get(this.trainingUrl  + '/' + trainingId);
		},

		getTrainingMinimal: function(trainingId) {
			return restService.get(this.trainingUrl  + '/' + trainingId + '/minimal');
		},

		getFeedback: function(trainingId, id) {
			return restService.get(this.trainingUrl  + '/' + trainingId + '/feedbacks/' + id);
		},

		getComments: function (trainingId) {
			return restService.get(this.trainingUrl + '/' + trainingId + '/comments');
		},

		removeComment: function(trainingId, commentId) {
			return restService.delete(this.trainingUrl + '/' + trainingId + '/comments/' + commentId);
		},

		removeAttachment: function(trainingId, filename) {
			var config = {params: {name: filename}}
			return restService.delete(this.trainingUrl + '/' + trainingId + '/attachments/file', config);
		},

		removeCommentReply: function(trainingId, commentId, commentReplyId) {
			return restService.delete(this.trainingUrl + '/' + trainingId + '/comments/' + commentId + '/replies/' + commentReplyId);
		},

		addCommentReply: function(trainingId, commentId, newReply) {
			var comment = {name: 'comment', value: newReply};
			return restService.post(this.trainingUrl + '/' + trainingId + '/comments/' + commentId + '/reply', comment);
		},

		listTrainees: function(trainingId) {
			return restService.get(this.trainingUrl + '/' + trainingId + '/trainees');
		},

		durationStepperOptions: {
			arrowStep: 0.5,
			wheelStep: 0.5,
			limit: [0.5, 8],
			type: 'float',
			floatPrecission: 2// decimal precission
		},

		getComments: function (trainingId) {
			return restService.get(this.trainingUrl + '/' + trainingId + '/comments');
		},

		getFeedbacks: function (trainingId) {
			return restService.get(this.trainingUrl + '/' + trainingId + '/feedbacks');
		},

		getFeedbackRatings: function (trainingId) {
			return restService.get(this.trainingUrl + '/' + trainingId + '/feedbacks/rating');
		},

		sendFeedBackRequest: function (trainingId) {
			return restService.get(this.trainingUrl + '/' + trainingId + '/feedbacks/request');
		},

		sendTrainingRequest: function (trainingId) {
			return restService.get(this.trainingUrl + '/' + trainingId + '/mail/scheduled');
		},

		getAttachments: function(id) {
			return restService.get(this.trainingUrl + '/' + id + '/attachments');
		},

		addComment: function(id, newComment) {
			var comment = {name: 'comment', value: newComment};
			return restService.post(this.trainingUrl + '/' + id + '/comments', comment);
		},

		addFeedback: function(feedback) {
			return restService.post(this.trainingUrl + '/' + feedback.parentId + '/feedbacks', feedback);
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
		},

		setEditables: function (training, obj) {
			obj.isAdmin = utilitiesService.isAdminUser($rootScope.user.roles);
			obj.isClerical = utilitiesService.isClericalUser($rootScope.user.roles);
			obj.userGuid = $rootScope.user.guid;
			if(obj.isAdmin) {
				obj.isEditable = true;
			}
			else {
				trainingService.isEditable(training).then(function (status) {
					obj.isEditable = status;
				});
			}
		},

		getMaxMinScheduledOn: function () {
			return restService.get(this.trainingsUrl + '/maxMinScheduledOn');
		}
	};
	return trainingService;
}]);