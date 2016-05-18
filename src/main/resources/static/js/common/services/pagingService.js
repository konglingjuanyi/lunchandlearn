/**
 * Created by DE007RA on 5/14/2016.
 */
angular.module('services').service('pagingService', ['$localStorage', function($localStorage) {
    var self = this;
    self.maxPageSize = 10;

    self.pageSizes = [2, 5, 20, 25, 30];

    self.currentPageSize = angular.isDefined($localStorage.pageSize) ? $localStorage.pageSize : self.pageSizes[0];

    self.savePageSize = function (pageSize) {
        pageSize = _.toNumber(pageSize);
        if(_.isNumber(pageSize) && pageSize > 0) {
            $localStorage.pageSize = pageSize;
        }
    }
}]);