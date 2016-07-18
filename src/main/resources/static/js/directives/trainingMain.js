/**
 * Created by de007ra on 5/7/2016.
 */
var lunchAndLearnDirectives = angular.module('directives');

lunchAndLearnDirectives.directive('trainingMain', function () {
    return {
        templateUrl: '/lunchandlearn/html/main/trainingMain.html',
        replace: true,
        scope: true,
        controller: 'trainingMainController as self'
    };
}).controller('trainingMainController',
    ['$scope', 'restService', 'utilitiesService', function ($scope, restService, utilitiesService) {
        var self = this;
        self.newTrainingsUrl = restService.appUrl + '/trainings/nominated';
        self.upcomingTrainingsUrl = restService.appUrl + '/trainings/scheduled';

        self.getNewTrainings = function () {
            restService.get(self.newTrainingsUrl).then(function (response) {
                if(angular.isDefined(response.data)) {
                    if (_.isArray(response.data.content)) {
                        self.newTrainings = response.data.content;
                    }
                }
            }, function (response) {

            });
        }

        self.getUpcomingTrainings = function () {
            restService.get(self.upcomingTrainingsUrl).then(function (response) {
                if(angular.isDefined(response.data)) {
                    if (_.isArray(response.data.content)) {
                        self.upcomingTrainings = utilitiesService.filterByLikeCount(response.data.content);
                    }
                }
            }, function (response) {

            });
        }

        self.init = function() {
            self.getNewTrainings();
            self.getUpcomingTrainings();
        };

        $scope.$on('trainings.refresh', function () {
            self.init();
        });

        self.init();
    }]);