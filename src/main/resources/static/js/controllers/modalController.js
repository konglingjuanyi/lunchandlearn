/**
 * Created by DE007RA on 5/9/2016.
 */
var pbAcademyControllers = angular.module('controllers', [ 'services', 'ui.bootstrap' ]);

pbAcademyControllers.controller('modalController',
    ['$uibModalInstance', 'data', '$scope', 'utilitiesService', function($uibModalInstance, data, $scope,
                                                                         utilitiesService) {
        $scope.mode = data.mode;
        $scope.item = data.item;
        $scope.options = data.options
        $scope.msg = data.msg;

        $scope.getSuggestedNames = function() {
            $scope.suggestedNames = null;
            $scope.duplicateItemId = null;
            if($scope.item.name && $scope.options.getSuggestedNames) {
                $scope.suggesting = true;
                $scope.options.getSuggestedNames($scope.item.name).then(function (response) {
                    $scope.suggesting = false;
                    if(_.isObject(response.data)) {
                        $scope.duplicateItemId = utilitiesService.getKeyByMatchingVal($scope.item.name, response.data);
                        if(!$scope.duplicateItemId) {
                            $scope.suggestedNames = response.data;
                        }
                    }
                }, function () {
                    $scope.suggesting = false;
                });
            }
        };

        $scope.$watch('selected.trainer', function () {
            if ($scope.selected.trainer) {
                $scope.item.trainers = utilitiesService.addUnique($scope.item.trainers, $scope.selected.trainer, 'guid', 'name');
            }
        });

        $scope.$watch('selected.topic', function () {
            if ($scope.selected.topic && (!$scope.item.prerequisites || !$scope.item.prerequisites[$scope.selected.topic.id])) {
                $scope.item.topics = utilitiesService.addUnique($scope.item.topics, $scope.selected.topic, 'id', 'name');
                $scope.errorTopics = null;
            }
            else if($scope.selected.topic) {
                $scope.errorTopics = '\'' + $scope.selected.topic.name + '\' already added in Prerequisites';
            }
        });

        $scope.$watch('selected.prerequisite', function () {
            if ($scope.selected.prerequisite && (!$scope.item.topics || !$scope.item.topics[$scope.selected.prerequisite.id])) {
                $scope.item.prerequisites = utilitiesService.addUnique($scope.item.prerequisites, $scope.selected.prerequisite, 'id', 'name');
                $scope.errorPrerequisites = null;
            }
            else if($scope.selected.prerequisite) {
                $scope.errorPrerequisites = '\'' + $scope.selected.prerequisite.name + '\' already added in Topics';
            }
        });

        $scope.$watch('selected.manager', function () {
            if ($scope.selected.manager) {
                $scope.item.managers = utilitiesService.addUnique($scope.item.managers, $scope.selected.manager, 'guid', 'name');
                $scope.editManagers = false;
            }
        });

        $scope.$watch('selected.role', function () {
            if ($scope.selected.role) {
                $scope.item.roles = utilitiesService.addUnique($scope.item.roles, $scope.selected.role, 'code', 'label');
                $scope.editRoles = false;
            }
        });

        $scope.selected = {};

        $scope.yes = function() {
            $scope.form.$setSubmitted();
            $scope.submit = true;
            if($scope.form.$valid) {
                $uibModalInstance.close($scope.item);
            }
        };

        $scope.removeManager = function(index) {
            $scope.item.managers.splice(index, 1);
        };

        $scope.removeRole = function(index) {
            $scope.item.roles.splice(index, 1);
        };

        $scope.cancel = function() {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.reset = function() {
            $scope.item = {};
        };

        $scope.isDatePickerOpen = false;
        $scope.datePickerOptions = {'show-button-bar': false, 'showWeeks': false, 'minDate': new Date(), maxDate: moment().add(3, 'M').toDate()};
        $scope.openDatePicker = function(e) {
            e.preventDefault();
            e.stopPropagation();
            $scope.isDatePickerOpen = true;
        };
    }
]);