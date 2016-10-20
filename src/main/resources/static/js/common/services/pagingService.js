/**
 * Created by DE007RA on 5/14/2016.
 */
angular.module('services').service('pagingService', ['$localStorage', function($localStorage) {
    var self = this;
    self.maxPageSize = 10;

    self.pageSizes = [8, 15, 30, 60];

    self.currentPageSize = angular.isDefined($localStorage.lnlPageSize) ? $localStorage.lnlPageSize : self.pageSizes[0];

    self.savePageSize = function (pageSize) {
        pageSize = _.toNumber(pageSize);
        if(_.isNumber(pageSize) && pageSize > 0) {
            $localStorage.pageSize = pageSize;
        }
    };

    self.getConfigObj = function (obj) {
        var params = {
            page: obj.currentPage - 1,
            size: obj.currentPageSize
        }
        if(angular.isDefined(obj.searchTerm)) {
            obj.searchTerm = _.trim(obj.searchTerm);
            if (!_.isEmpty(obj.searchTerm)) {
                params.search = obj.searchTerm;
            }
        }
        if(angular.isDefined(obj.filterBy)) {
            if (!_.isEmpty(obj.filterBy)) {
                params.filterBy = obj.filterBy;
            }
        }
        if(angular.isDefined(obj.sort) && angular.isDefined(obj.sort.name) && !_.isEmpty(obj.sort.name.trim())) {
            params.sort = obj.sort.name + ',' + obj.sort.direction;
        }
        return {params: params};
    };
}]);