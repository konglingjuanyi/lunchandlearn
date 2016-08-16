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
        self.nominatedTrainingsUrl = restService.appUrl + '/trainings/nominated';
        self.upcomingTrainingsUrl = restService.appUrl + '/trainings/scheduled';
        self.recentTrainingsUrl = restService.appUrl + '/trainings/completed';

        self.getNominatedTrainings = function () {
            restService.getLoggedInUser().then(function(user) {
                if(user) {
                    self.userGuid = user.guid;
                    self.isAdmin = utilitiesService.isAdminUser(user.roles);
                    restService.get(self.nominatedTrainingsUrl).then(function (response) {
                        if(angular.isDefined(response.data)) {
                            if (_.isArray(response.data.content)) {
                                self.nominatedTrainings = response.data.content;
                            }
                        }
                    }, function (response) {});
                }
            });
        }

        self.getUpcomingTrainings = function () {
            restService.get(self.upcomingTrainingsUrl).then(function (response) {
                if(angular.isDefined(response.data)) {
                    if (_.isArray(response.data.content)) {
                        self.upcomingTrainings = response.data.content;
                    }
                }
            }, function (response) {});
        }


        self.getRecentTrainings = function () {
            restService.get(self.recentTrainingsUrl).then(function (response) {
                if(angular.isDefined(response.data)) {
                    if (_.isArray(response.data.content)) {
                        self.recentTrainings = response.data.content;
                    }
                }
            }, function (response) {});
        }

        self.init = function() {
            self.getNominatedTrainings();
            self.getUpcomingTrainings();
            self.getRecentTrainings();
        };

        $scope.$on('trainings.refresh', function () {
            self.init();
        });

        self.init();
    }]);