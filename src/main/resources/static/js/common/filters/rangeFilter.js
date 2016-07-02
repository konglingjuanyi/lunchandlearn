/**
 * Created by DE007RA on 6/22/2016.
 */
angular.module('filters', []).filter('range', function() {
    return function(input, total) {
        total = parseInt(total);

        for (var i = 1; i <= total; i++) {
            input.push(i);
        }

        return input;
    };
});