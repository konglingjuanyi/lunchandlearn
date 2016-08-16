/**
 * Created by DE007RA on 5/15/2016.
 */
angular.module('directives').directive('topicTag', function() {
    return {
        templateUrl : '/lunchandlearn/html/topic/topicTag.html',
        replace : true,
        scope: {
            topic: '=',
            showLikeOnly: '=?'
        },
        controller: 'topicTagController',
        controllerAs: 'ttc'
    };
}).controller('topicTagController',
    [ '$scope', 'topicService', '$rootScope', function($scope, topicService, $rootScope) {
        var self = this;

        if(!angular.isDefined($scope.showName)) {
            $scope.showName = true;
        }
        self.likeTopic = function (topic) {
            $scope.saving = true;
            topicService.likeTopic(topic.id).then(function(response) {
                topic.likesCount = response.data.likesCount;
                $rootScope.$broadcast('topics.refresh');
            }).finally(function () {
                $scope.saving = false;
            });;
        };
}]);