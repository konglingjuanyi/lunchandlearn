/**
 * Created by DE007RA on 8/3/2016.
 */
angular.module('directives').directive('focusMe', function ($timeout) {
    return {
        scope: {trigger: '=ngShow'},
        link: function (scope, element) {
            scope.$watch('trigger', function (value) {
                $timeout(function() {
                    if (value === true) {
                        element[0].focus();
                    }
                });
            });
        }
    };
});