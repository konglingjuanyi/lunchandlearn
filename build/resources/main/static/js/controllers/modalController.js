/**
 * Created by DE007RA on 5/9/2016.
 */
var lunchAndLearnControllers = angular.module('controllers', [ 'services', 'ui.bootstrap' ]);

lunchAndLearnControllers.controller('modalController',
    ['$uibModalInstance', 'data', '$scope', function($uibModalInstance, data, $scope) {
        $scope.mode = data.mode;
        $scope.item = data.item;
        $scope.options = data.options
        $scope.msg = data.msg;

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