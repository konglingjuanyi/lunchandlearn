/**
 * Created by DE007RA on 5/9/2016.
 */
var lunchAndLearnControllers = angular.module('controllers', [ 'services', 'ui.bootstrap' ]);

lunchAndLearnControllers.controller('modalController',
    ['$uibModalInstance', 'data', '$scope', 'utilitiesService', function($uibModalInstance, data, $scope,
                                                                         utilitiesService) {
        $scope.mode = data.mode;
        $scope.item = data.item;
        $scope.options = data.options
        $scope.msg = data.msg;

        $scope.$watch('selected.trainer', function () {
            if ($scope.selected.trainer) {
                $scope.item.trainers = utilitiesService.addUnique($scope.item.trainers, $scope.selected.trainer, 'guid', 'name');
            }
        });

        $scope.$watch('selected.topic', function () {
            if ($scope.selected.topic && (!$scope.item.prerequisites || !$scope.item.prerequisites[$scope.selected.topic.id])) {
                $scope.item.topics = utilitiesService.addUnique($scope.item.topics, $scope.selected.topic, 'id', 'name');
            }
        });

        $scope.$watch('selected.prerequisite', function () {
            if ($scope.selected.prerequisite && (!$scope.item.topics || !$scope.item.topics[$scope.selected.prerequisite.id])) {
                $scope.item.prerequisites = utilitiesService.addUnique($scope.item.prerequisites, $scope.selected.prerequisite, 'id', 'name');
            }
        });

        $scope.selected = {};

        $scope.yes = function() {
            $uibModalInstance.close($scope.item);
        };

        $scope.removeManager = function(index, item) {
            $scope.item.managers.splice(index, 1);
            // $scope.setShowStatusMsg(false);
        };

        $scope.cancel = function() {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.isDatePickerOpen = false;
        $scope.datePickerOptions = {'show-button-bar': false, 'showWeeks': false, 'minDate': new Date(), maxDate: moment().add(3, 'M').toDate()};
        $scope.openDatePicker = function(e) {
            e.preventDefault();
            e.stopPropagation();
            $scope.isDatePickerOpen = true;
        };
    }
]).directive('addManager', ['$compile', function ($compile) {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            $("#btn_add_manager").bind('click', function () {
                if(scope.item.managers == null) {
                    scope.item.managers = [];
                }
                scope.item.managers[scope.item.managers.length] = "";
                var select = '<select name="manager" class="dynamic-select" ng-model="item.managers[' + (scope.item.managers.length - 1) + ']" '+
                    'ng-options="managers.guid as managers.name for managers in options.managers"></select>'+
                    '<button class="btn btn-inline btn-default btn-xs dynamic-remove" ng-click="removeManager(' + (scope.item.managers.length - 1) + ')">x</button>'
                $compile(select)(scope);
            });
        }
    }
}]);