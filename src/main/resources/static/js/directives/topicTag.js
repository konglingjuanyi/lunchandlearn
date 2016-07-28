/**
 * Created by DE007RA on 5/15/2016.
 */
angular.module('directives').directive('topicTag', function() {
    return {
        templateUrl : '/lunchandlearn/html/topic/topicTag.html',
        replace : true,
        scope: {
            topic: '=?'
        },
        controller: 'topicTagController',
        controllerAs: 'ttc'
    };
}).controller('topicTagController',
    [ '$scope', 'topicService', function($scope, topicService) {
        var self = this;

        self.likeTopic = function (topic) {
            topicService.likeTopic(topic.id).then(function(response) {
                topic.likesCount = response.data.likesCount;
                $scope.$emit('topics.refresh');
            });
        };
}]);