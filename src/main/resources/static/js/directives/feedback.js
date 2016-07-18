angular.module('directives').directive('feedback', function() {
    return {
        templateUrl : '/lunchandlearn/html/training/trainingFeedbackList.html',
        replace : true,
        scope: {
            parentId: '=',
            feedbackCount: '=?',
            training: '='
        },
        controller: 'trainingsFeedbackController as self'
    };
}).controller('trainingsFeedbackController', [
    '$scope', '$uibModal', 'trainingService', 'restService', 'utilitiesService',
    function($scope, $uibModal, trainingService, restService, utilitiesService) {
        var self = this;
        self.ratingsRows = [{label: 'Over All Expectation', code: 'overallExpectation'}, {label: 'Relevancy', code: 'relevancy'},
            {label: 'Applicability', code: 'applicability'}, {label: 'Appropriateness', code: 'appropriateness'},
            {label: 'Facilitation', code: 'facilitation'}, {label: 'Responsiveness', code: 'answerAllQueries'},
            {label: 'Preparedness', code: 'preparedness'}];
        self.parentId = $scope.parentId;
        self.range = [1, 2, 3, 4, 5];

        restService.getLoggedInUser().then(function(user) {
            if(user) {
                self.userGuid = user.guid;
                self.isAdmin = utilitiesService.isAdminUser(user.roles);
            }
        });

        $scope.$watch('training', function () {
            self.pendingFeedBack = $scope.training && $scope.training.trainees &&
                $scope.training.trainees[self.userGuid] && $scope.training.status === 'COMPLETED';
        });

        self.list = function() {
            trainingService.getTrainingMinimal(self.parentId).then(function(response) {
                if(response.data) {
                    self.training = response.data;
                }
            });
            trainingService.getFeedbacks(self.parentId).then(function(response) {
                if(angular.isDefined(response.data)) {
                    self.feedbacks = response.data;
                    $scope.feedbackCount = self.feedbacks.length;
                }
            });
            getRatings();
        }

        var getRatings = function() {
            trainingService.getFeedbackRatings(self.parentId).then(function(response) {
                if(angular.isDefined(response.data)) {
                    self.ratings = response.data;
                    $scope.feedbackCount = self.feedbacks.length;
                }
            });
        }

        self.show = function(id) {
            trainingService.getFeedback(self.parentId, id).then(function(response) {
                self.mode = 'view';
                self.feedback = response.data;
                self.showModalDlg();
            });
        }

        self.add = function() {
            trainingService.addFeedback(self.feedback).then(function(response) {
                self.mode = null;
                self.pendingFeedBack = false;
                self.list();
            });
        };

        self.showAdd = function() {
            self.mode = 'add';
            self.feedback = {parentId: self.parentId};
            self.showModalDlg();
        };

        self.showModalDlg = function () {
            var opts = {
                templateUrl: '/lunchandlearn/html/training/trainingFeedbackForm.html',
                backdrop: 'static',
                controller: 'modalController as self',
                resolve: {
                    data: function () {
                        return {item: self.feedback, mode: self.mode,
                            options: {training: $scope.training, feedbackRange: self.feedbackRange,
                                range: self.range}};
                    }
                }
            }
            $uibModal.open(opts).result.then(function (selectedItem) {
                self.feedback = selectedItem;
                self.add();
            }, function () {
                console.log('confirmation modal cancelled')
            });
        };

        self.list();
    }]);