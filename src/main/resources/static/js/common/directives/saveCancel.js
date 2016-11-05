/**
 * Created by DE007RA on 8/3/2016.
 */
angular.module('directives').directive('saveCancel', function () {
    return {
        templateUrl: '/pbacademy/html/main/saveCancel.html',
        replace: true,
        scope: {
            onSave: '=',
            field: '@',
            onCancel: '='
        },
        controller: 'saveCancelController'
    };
}).controller('saveCancelController', ['$scope',
    function ($scope) {
        $scope.save = function () {
            $scope.saving = true;
            $scope.onSave($scope.field).then(function (status) {
                $scope.saving = false;
            }, function () {
                $scope.saving = false;
            });
        };

        $scope.cancel = function () {
            $scope.onCancel(_.upperFirst($scope.field), false);
        };
    }]);