/**
 * Created by DE007RA on 5/15/2016.
 */
angular.module('directives').directive('trainingTag', function() {
    return {
        templateUrl : '/lunchandlearn/html/training/trainingTag.html',
        replace : true,
        scope: {
        training: '=?'
        },
        controller: 'trainingTagController',
        controllerAs: 'ttc'
    };
}).controller('trainingTagController',
    ['$scope', 'trainingService', function($scope, trainingService) {
        var self = this;

        self.likeTraining = function (training) {
            trainingService.likeTraining(training.id).then(function(response) {
                training.likesCount = response.data.likesCount;
                $scope.$emit('trainings.refresh');
            });
        };
    }]);