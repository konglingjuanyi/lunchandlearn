/**
 * Created by DE007RA on 6/22/2016.
 */
angular.module('filters').filter('propercase', function() {
    return function(input) {
        if(!_.isEmpty(input)) {
            if(input.length > 1) {
                input = input.substr(0, 1).toUpperCase() + input.substr(1).toLocaleLowerCase();
            }
            else {
                input = input.substr(0, 1).toUpperCase();
            }
        }
        return input;
    };
});