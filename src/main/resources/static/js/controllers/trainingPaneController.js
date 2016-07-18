angular.module('controllers').controller('trainingPaneController', [
	'$scope', 'trainingService', 'utilitiesService', function($scope, trainingService, utilitiesService) {
		var self = this;

		self.init = function () {
			self.getRecentTrainings();
			self.getTopLikesTrainings();
			self.getViewedTrainings();
		};

		self.getRecentTrainings = function() {
			trainingService.listTrainings({url: '/nominated'}).then(function(response) {
				if(angular.isDefined(response.data)) {
					self.recentTrainings = {data: response.data.content};
				}
			});
		};

		self.getViewedTrainings = function() {
			var ids = trainingService.getRecentTrainingsId();
			if(!_.isEmpty(ids)) {
				trainingService.listTrainings({url: '/ids', params: {ids: ids}}).then(function(response) {
					if(angular.isDefined(response.data)) {
						self.viewedTrainings = {data: []};
						var trainings = response.data.content;
						_.forEach(ids, function (id) {
							var training = _.find(trainings, {'id': id});
							if (!_.isUndefined(training)) {
								self.viewedTrainings.data.push(training);
							}
						});
					}
				});
			}
		};

		self.getTopLikesTrainings = function() {
			trainingService.listTrainings({url: '/likes'}).then(function(response) {
				if(angular.isDefined(response.data)) {
					self.trendingTrainings = {data: utilitiesService.filterByLikeCount(response.data.content)};
				}
			});
		}
		self.init();

		$scope.$on('trainings.refresh', function () {
			self.init();
		})
	}]);