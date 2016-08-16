/**
 * Created by DE007RA on 5/9/2016.
 */
angular.module('controllers').controller('loginController',
    ['$rootScope', 'restService', '$location', '$localStorage', 'utilitiesService',
        function ($rootScope, restService, $location, $localStorage, utilitiesService) {
        var self = this;

        self.isLoginPage = function() {
            return _.startsWith($location.path(), '/login');
        }

        $rootScope.$on('$locationChangeStart', function(event, next, current) {
            if(self.isLoginPage() && !$rootScope.user
                && !utilitiesService.isLoginUrl(current) && utilitiesService.isAppUrl(current)) {
                $localStorage.lastLnLUrl = utilitiesService.getRelativePath(current);
            }
            else {
                $localStorage.lastLnLUrl = undefined;
            }
        });

        self.loggedOut = $location.search().logout;

        var authenticate = function () {
            restService.getLoggedInUser().then(function(response) {
                if($rootScope.user && self.isLoginPage()) {
                    if(angular.isDefined($localStorage.lastLnLUrl)) {
                        $location.path($localStorage.lastLnLUrl);
                    }
                    else {
                        $location.path('/').search({logout: null});
                    }
                }
            }).finally(function () {
                self.doingLogin = false;
            });
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
            self.doingLogin = true;
            restService.post(restService.appUrl + '/login', $.param(self.credentials), {
                headers: {
                    "content-type": "application/x-www-form-urlencoded"
                }
            }).success(function (response) {
                if(angular.isUndefined(response)) {
                    self.credentials.error = true;
                    self.loggedOut = false;
                    self.doingLogin = false;
                    return;
                }
                authenticate();
            }).error(function (response) {
                self.doingLogin = false;
                $location.path(restService.appUrl + "/login");
                $rootScope.authenticated = false;
                self.credentials.error = true;
            });
        };
        authenticate();
    }]);