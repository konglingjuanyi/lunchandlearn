/**
 * Created by DE007RA on 6/23/2016.
 */
angular.module('services').service('utilitiesService', ['restService', '$uibModal', function(restService, $uibModal) {
    var self = this;
    self.addUnique = function (map, obj, objMapKey, objValueKey, sort) {
        if(angular.isDefined(obj)) {
            if (!map) {
                map = {};
            }
            var keyName = _.get(obj, objMapKey).toString();
            var value = _.get(obj, objValueKey);
            if (angular.isUndefined(map[keyName])) {
                map[keyName] = value;
                
            }
        }
        return sort ? self.sortObj(map) : map;
    };

    self.isAdminUser = function (roles) {
        if(!_.isEmpty(roles)) {
            if (_.indexOf(roles, 'ADMIN') > -1) {
                return true;
            }
        }
        return false;
    };

    self.isClericalUser = function (roles) {
        if(!_.isEmpty(roles)) {
            if (_.indexOf(roles, 'CLERICAL') > -1) {
                return true;
            }
        }
        return false;
    };

    self.getKeyByMatchingVal = function(value, jsonObj) {
        value = _.toLower(value).trim();
        var key = {};
        _.each(jsonObj, function (v, k) {
            if(_.toLower(v) === value) {
                key = k;
                return false;
            }
        });
        return key;
    };

    self.isManagerUser = function (roles) {
        if(!_.isEmpty(roles)) {
            if (_.indexOf(roles, 'MANAGER') > -1) {
                return true;
            }
        }
        return false;
    };

    self.sortObj = function (obj) {
        var values = [];
        _.each(obj, function (v, k) {
            values.push(v);
        });
        if(values.length > 1) {
            values = self.toUpper(values).sort();
            var sortedObj = {};
            _.each(values, function (val) {
                _.each(obj, function (v, k) {
                    if(v.toLowerCase() === val) {
                        sortedObj[k] = val;
                        delete obj[k];
                        return false;
                    }
                });
            });
            return sortedObj;
        }
        else {
            return obj;
        }
    };

    self.toUpper = function (array) {
        _.each(array, function (item, index) {
            array[index] = _.toLower(item);
        });
        return array;
    };

    self.toISODateString = function (momentObj) {
        if(angular.isDefined(momentObj)) {
            return momentObj.toISOString();
        }
    },

    self.setEditable = function (obj, fieldName, status) {
        var propName = 'edit' + _.upperFirst(fieldName);
        obj[propName] = status;
    };

    self.filterByLikeCount = function (array) {
        var data = _.filter(array, function (item) {
            return item.likesCount > 0;
        });
        return data;
    };

    self.isAppUrl = function (fullUrl) {
        if(_.isEmpty(fullUrl)) {
            return false;
        }
        var names = fullUrl.split('/');
        if(names.length < 4) {
            return false;
        }
         return names[3] === 'pbacademy';
    };


    self.isLoginUrl = function (fullUrl) {
        if(_.isEmpty(fullUrl)) {
            return false;
        }
        var names = fullUrl.split('/');
        if(names.length < 5) {
            return false;
        }
        var lastUrl = (names[names.length - 1]).split('?')[0];

        return lastUrl === 'login';
    };

    self.getRelativePath = function (fullUrl) {
        if(_.isEmpty(fullUrl)) {
            return '';
        }
        return fullUrl.substr(fullUrl.indexOf('#') + 1);
    };

    self.setLastModifiedBy = function(parent, obj) {
        if(!obj) {
            restService.getLoggedInUser().then(function(user) {
                var entry = {};
                entry[user.guid] = user.name;
                parent.lastModifiedBy = entry;
            });
        }
        else if(obj.lastModifiedByGuid) {
            var entry = {};
            entry[obj.lastModifiedByGuid] = obj.lastModifiedByName;
            parent.lastModifiedBy = entry;
        }
    };

    self.showMsgDlg = function (data) {
        var opts = {
            templateUrl: '/pbacademy/html/main/msgDlg.html',
            controller: 'modalController as self',
            backdrop: 'static',
            resolve: {
                data: function () {
                    return data;
                }
            }
        };
        $uibModal.open(opts);
    };

    self.resetErrorProps = function(obj) {
        obj.error = false;
        obj.errorMsg = null;
    };
}]);