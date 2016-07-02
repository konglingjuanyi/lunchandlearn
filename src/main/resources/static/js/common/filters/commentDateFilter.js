/**
 * Created by DE007RA on 6/22/2016.
 */
angular.module('filters').filter('commentDate', function() {
    return function(date) {
        var date = moment(date);
        return date.format('Do MMM, YYYY');
    };
});