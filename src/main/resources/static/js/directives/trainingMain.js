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
    ['$scope', 'restService', function ($scope, restService) {
        var self = this;
        self.recentTrainingsUrl = restService.appUrl + '/trainings/recent';

        self.getNewTrainings = function () {
            restService.get(self.recentTrainingsUrl).then(function (response) {
                if(_.isArray(response.data.content)) {
                    self.newTrainings = response.data.content;
                }
            }, function (response) {

            });
        }

        self.getTrendingTrainings = function () {
            restService.get(self.recentTrainingsUrl).then(function (response) {
                if(_.isArray(response.data.content)) {
                    self.trendingTrainings = response.data.content;
                }
            }, function (response) {

            });
        }

        self.init = function() {
            self.getNewTrainings();
            self.getTrendingTrainings();
        };


        $scope.$on('trainings.refresh', function () {
            self.init();
        });

        self.init();
    }]);