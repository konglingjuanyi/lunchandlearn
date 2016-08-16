/**
 * Created by DE007RA on 8/5/2016.
 */
angular.module('directives').directive('loading', function () {
    return {
        restrict: 'EA',
        replace: true,
        scope: {smaller: '@'},
        template: '<span class="text-center" ng-class="{\'loading-lg\': !smaller}">' +
        '<i class="nc-icon-mini loader_circle-04 spin"></i></span>'
    }
});