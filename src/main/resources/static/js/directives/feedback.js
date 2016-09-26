angular.module('directives').directive('feedback', function() {
    return {
        templateUrl : '/lunchandlearn/html/training/trainingFeedbackList.html',
        replace : true,
        scope: {
            parentId: '=',
            feedbackCount: '=',
            training: '=',
            feedbackStatus: '=',
            isTrainingClosed: '='
        },
        controller: 'trainingsFeedbackController as self'
    };
}).controller('trainingsFeedbackController', [
    '$scope', '$uibModal', 'trainingService', 'restService', 'utilitiesService', 'd3Service',
    function($scope, $uibModal, trainingService, restService, utilitiesService, d3Service) {
        var self = this;
        self.ratingsRows = trainingService.ratingsRows;
        self.parentId = $scope.parentId;
        self.range = [1, 2, 3, 4, 5];

        restService.getLoggedInUser().then(function(user) {
            if(user) {
                self.userGuid = user.guid;
                self.isAdmin = utilitiesService.isAdminUser(user.roles);
            }
        });

        $scope.$watch('training', function() {
            if($scope.training) {
                list();
            }
        });

        var list = function() {
            trainingService.getFeedbacks(self.parentId).then(function(response) {
                $scope.feedbackStatus.pendingFeedBack = $scope.training.trainees && $scope.training.trainees[self.userGuid]
                    && ($scope.training.status === 'COMPLETED' && $scope.training.status !== 'CLOSED');
                if(!_.isEmpty(response.data)) {
                    $scope.feedbackStatus.pendingFeedBack = $scope.feedbackStatus.pendingFeedBack && isFeedbackPending(response.data);
                    self.feedbacks = response.data.content;
                    self.ratings = _.omit(response.data, ['content']);
                    $scope.feedbackCount = response.data.feedbackCount;
                    self.feedbackComments = response.data.comments;
                    showRatingsGraph();
                }
            });
        }

        var isFeedbackPending = function(feedbacks) {
            var status = true;
            _.forEach(feedbacks.content, function(feedback) {
                if(feedback.respondentGuid === self.userGuid) {
                    status = false;
                    return false;
                }
            });
            return status;
        };

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
                list();
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
                
            });
        };

        var showRatingsGraph = function () {
            var ratingsData = [];
            _.each(self.ratingsRows, function (ratingRow) {
                var rating = self.ratings[ratingRow.code];
                if (rating) {
                    ratingsData.push({
                        rating: rating,
                        color: ratingRow.color, label: ratingRow.label
                    });
                }
            });
            if (!_.isEmpty(ratingsData)) {
                var barWidth = 25, barPadding = 20, rectPadding = 5;

                // scaling data
                var totalHeight = 420;
                var totalWidth = 665;
                var width = 640;
                var mainGWidth = 330;

                var max = 5;
                // scale factor
                var scale = width / max;
                var scaled_data = ratingsData.map(function (d) {
                    return {
                        rating: d.rating, scaledRating: d.rating * scale,
                        color: d.color, label: d.label
                    };
                });

                function yloc(d, i) {
                    return i * (barWidth + barPadding);
                }

                function translator(d, i) {
                    return 'translate(0,' + yloc(d, i) + ')';
                }

                var margin = {top: 10, right: 10, bottom: 30, left: 10};
                var textTranslator = 'translate(2, 22)';

                var svg = d3Service.select('.rating-bar svg').attr('width', totalWidth).attr('height', totalHeight);
                var axisGroup = svg.append('g').attr('transform', 'translate(' + margin.left + ',' + (mainGWidth + margin.top - 5) + ')');
                var scale = d3Service.scaleLinear().domain([0, max]).range([0, width]);
                var axis = d3Service.axisTop(scale);
                var axisNodes = axisGroup.call(axis);

                var domain = axisNodes.selectAll('.domain');
                domain.attr('fill', 'none').attr('stroke-width', 1).attr('stroke', 'black');
                var ticks = axisNodes.selectAll('.tick line');
                ticks.attr('fill', 'none').attr('stroke-width', 1).attr('stroke', 'black');

                var barGroup = svg.append('g').attr('transform', 'translate(' + margin.left + ',' + margin.top + ')');
                barGroup.append('rect').attr('fill', 'rgba(0,0,0,0.1)').attr('width', width).attr('height', mainGWidth);

                barGroup = barGroup.selectAll('g').data(scaled_data).enter().append('g').attr('transform', translator);
                barGroup.append('rect').attr('transform', 'translate(0, ' + rectPadding + ')')
                    .attr('fill', '#3e53a4').attr('width', function (d) {
                    return d.scaledRating;
                }).attr('height', barWidth);

                barGroup.append('text').text(function (d) {
                    return d.label + ': ' + d.rating;
                }).attr('fill', 'white')
                    .attr('transform', textTranslator).style('font-size', '13px')
                    .style('font-family', '"Helvetica Neue", Helvetica, Arial, sans-serif;');
            }
        }
    }]);