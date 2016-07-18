/**
 * Created by DE007RA on 5/9/2016.
 */
angular.module('controllers').controller('loginController',
    ['$rootScope', 'restService', '$location', function ($rootScope, restService, $location) {
        var self = this;

        self.loggedOut = $location.search().logout;
        var authenticate = function () {
            restService.getLoggedInUser();
        }

        self.credentials = {};

        self.logout = function () {
            return restService.post(restService.appUrl + '/logout', self.credentials).then(function () {
                $rootScope.authenticated = false;
                $rootScope.user = undefined;
                $location.path('/login').search({logout: true});
            });
        }

        self.login = function () {
            restService.post(restService.appUrl + '/login', $.param(self.credentials), {
                headers: {
                    "content-type": "application/x-www-form-urlencoded"
                }
            }).success(function (response) {
                if(angular.isUndefined(response)) {
                    self.credentials.error = true;
                    self.loggedOut = false;
                    return;
                }
                authenticate();
            }).error(function (response) {
                $location.path(restService.appUrl + "/login");
                $rootScope.authenticated = false;
                self.credentials.error = true;
            })
        };
        authenticate();
    }]);