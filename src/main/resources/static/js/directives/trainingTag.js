/**
 * Created by DE007RA on 5/15/2016.
 */
angular.module('directives').directive('trainingTag', function() {
    return {
        templateUrl : '/lunchandlearn/html/training/trainingTag.html',
        replace : true,
        scope: {
            training: '=',
            target: '@',
            showLikeOnly: '=?'
        },
        controller: 'trainingTagController',
        controllerAs: 'ttc'
    };
}).controller('trainingTagController',
    ['$scope', 'trainingService', '$rootScope', function($scope, trainingService, $rootScope) {
        var self = this;
        $scope.target = $scope.target ? $scope.target : '_self';
        self.likeTraining = function (training) {
            $scope.saving = true;
            trainingService.likeTraining(training.id).then(function(response) {
                training.likesCount = response.data.likesCount;
                $rootScope.$broadcast('trainings.refresh');
            }).finally(function () {
                $scope.saving = false;
            });
        };
    }]);