/**
 * Created by DE007RA on 5/15/2016.
 */
angular.module('directives').directive('topicTag', function() {
    return {
        templateUrl : '/lunchandlearn/html/topic/topicTag.html',
        replace : true,
        scope: true
    };
});