/**
 * Created by DE007RA on 5/15/2016.
 */
angular.module('directives').directive('comments', function() {
    return {
        templateUrl : '/lunchandlearn/html/main/comments.html',
        replace : true,
        scope: {
            parentId: '=?',
            sectionName: '@',
            commentsCount: '=?'
        },
        controller: 'commentController',
        controllerAs: 'self'
    };
}).controller('commentController',
    [ '$scope', 'trainingService', function($scope, trainingService) {
        var self = this;
        self.parentId = $scope.parentId;
        self.sectionName = $scope.sectionName;
        self.getComments = function () {
            if(self.sectionName === 'trainings') {
                trainingService.getComments(self.parentId).then(function (response) {
                    self.comments = response.data;
                    $scope.commentsCount = self.comments.length;
                });
            }
        };

        self.addReply = function (comment) {
            $scope.parentCommentId = comment.id;
            self.newReply = '';
        };

        self.cancelReply = function (comment) {
            $scope.parentCommentId = undefined;
            self.newReply = undefined;
        };

        self.saveReply = function () {
            if(!_.isEmpty(self.newReply)) {
                trainingService.addCommentReply(self.parentId, $scope.parentCommentId, self.newReply).then(function() {
                    self.getComments();
                    self.newReply = undefined;
                    $scope.parentCommentId = undefined;
                });
            }
        }

        self.saveComment = function () {
            if(!_.isEmpty(self.newComment)) {
                trainingService.addComment(self.parentId, self.newComment).then(function() {
                    self.getComments();
                    self.newComment = undefined;
                });
            }
        }

        self.removeComment = function (commendId) {
            trainingService.removeComment(self.parentId, commendId).then(function() {
                self.getComments();
            });
        }

        self.removeCommentReply = function (commendId, commentReplyId) {
            trainingService.removeCommentReply(self.parentId, commendId, commentReplyId).then(function() {
                self.getComments();
            });
        }
        self.getComments();
    }]);