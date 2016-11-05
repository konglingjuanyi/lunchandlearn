angular.module('controllers').controller('trainingController', ['$scope', '$uibModal', 'trainingService', 'pagingService',
    '$routeParams', 'restService', 'utilitiesService', 'employeeService', 'topicService', 'Upload', '$timeout',
    'trainingStatus', '$q', 'trainingRoomService',
    function ($scope, $uibModal, trainingService, pagingService, $routeParams, restService, utilitiesService,
              employeeService, topicService, Upload, $timeout, trainingStatus, $q, trainingRoomService) {
        var self = this;
        var prevItem = null;
        $scope.feedbackCount = 0;
        $scope.commentsCount = 0;
        $scope.item = {};
        $scope.feedbackStatus = {};
        self.isDatePickerOpen = false;
        self.datePickerOptions = {
            'show-button-bar': false,
            'showWeeks': false,
            // 'minDate': new Date(),
            maxDate: moment().add(3, 'M').toDate()
        };
        self.trainingTypes = trainingService.trainingTypes;

        self.openDatePicker = function (e) {
            e.preventDefault();
            e.stopPropagation();
            self.isDatePickerOpen = true;
        };

        self.openStepper = function (e) {
            if(self.setEditStatus('Duration', true)) {
                e.preventDefault();
                e.stopPropagation();
            }
        };

        $scope.selected = {};

        self.trainingStatus = _.cloneDeep(trainingStatus);
        _.remove(self.trainingStatus, {code: null});

        self.init = function () {
            employeeService.listEmployeesMinimal().then(function (response) {
                if (angular.isDefined(response.data)) {
                    self.employees = response.data.content;
                }
            });
            if (!_.isEmpty($routeParams.trainingId)) {
                self.trainingId = $routeParams.trainingId;
                self.getTraining();
                self.getAttachments();
                trainingService.pushRecentTrainingId(self.trainingId);
                getTrainingLocations();
            }

            topicService.listTopics().then(function (response) {
                if (angular.isDefined(response.data)) {
                    self.topics = response.data.content;
                }
            });
            trainingService.listTrainees(self.trainingId).then(function (response) {
                if (angular.isDefined(response.data)) {
                    $scope.item.trainees = response.data;
                    updateTraineesCount();
                }
            });
        };

        $scope.$watch('selected.trainer', function () {
            if ($scope.selected.trainer && (!$scope.item.trainees || !$scope.item.trainees[$scope.selected.trainer.guid])) {
                $scope.item.trainers = utilitiesService.addUnique($scope.item.trainers, $scope.selected.trainer, 'guid', 'name');
                self.saveByField('trainers');
            }
            else if($scope.selected.trainer) {
                self.errorTrainers = $scope.selected.trainer.name + ' already added as trainee';
            }
        });

        $scope.$watch('selected.location', function () {
            if ($scope.selected.location && (!$scope.item.locations || !$scope.item.locations[$scope.selected.location.id])) {
                $scope.item.locations = utilitiesService.addUnique($scope.item.locations, $scope.selected.location, 'id', 'name');
                self.saveByField('locations');
            }
            else if($scope.selected.location) {
                self.errorLocations = $scope.selected.location.name + ' already added as location';
            }
        });

        $scope.$watch('selected.trainee', function () {
            if ($scope.selected.trainee && (!$scope.item.trainers || !$scope.item.trainers[$scope.selected.trainee.guid])) {
                $scope.item.trainees = utilitiesService.addUnique($scope.item.trainees, $scope.selected.trainee, 'guid', 'name');
                self.saveByField('trainees');
            }
            else if($scope.selected.trainee) {
                self.errorTrainees = $scope.selected.trainee.name + ' already added as trainer';
            }
        });

        var updateTraineesCount = function () {
            self.traineesCount = _.size($scope.item.trainees);
            self.isFeedBackReady = self.traineesCount > 0 && self.isTrainingComplete;
            self.isScheduled = $scope.item.status === 'SCHEDULED';
        };

        $scope.$watch('selected.topic', function () {
            if ($scope.selected.topic && (!$scope.item.prerequisites || !$scope.item.prerequisites[$scope.selected.topic.id])) {
                $scope.item.topics = utilitiesService.addUnique($scope.item.topics, $scope.selected.topic, 'id', 'name');
                self.saveByField('topics');
            }
            else if($scope.selected.topic) {
                self.errorTopics = $scope.selected.topic.name + ' already added as prerequisite';
            }
        });

        $scope.$watch('selected.prerequisite', function () {
            if ($scope.selected.prerequisite && (!$scope.item.topics || !$scope.item.topics[$scope.selected.prerequisite.id])) {
                $scope.item.prerequisites = utilitiesService.addUnique($scope.item.prerequisites, $scope.selected.prerequisite, 'id', 'name');
                self.saveByField('prerequisites');
            }
            else if($scope.selected.prerequisite) {
                self.errorTopics = $scope.selected.prerequisite.name + ' already added as topic';
            }
        });

        $scope.$watch('item.scheduledOn', function () {
            $scope.scheduledOnView = '';
            if ($scope.item.scheduledOn) {
                $scope.scheduledOnView = moment($scope.item.scheduledOn).format('DD-MMM-YY, HH:mm A');
            }
        });

        self.getTraining = function () {
            trainingService.getTraining(self.trainingId).then(function (response) {
                if(_.isEmpty(response.data)) {
                    self.errorMsg = 'Training detail not found!';
                    self.error = true;
                    return;
                }
                if (angular.isDefined(response.data)) {
                    $scope.item = _.defaultsDeep($scope.item, response.data);
                    trainingService.setEditables($scope.item, self);

                    if ($scope.item.scheduledOn) {
                        $scope.item.scheduledOn = moment($scope.item.scheduledOn).toDate();
                    }
                    self.attachmentUploadUrl = trainingService.trainingUrl + '/' + $scope.item.id + '/attachment';
                    if (angular.isDefined($scope.item.duration)) {
                        $scope.selected.duration = $scope.item.duration;
                    }
                    setAgendaTabFields();
                    setCurrentStatus();

                    var user = {};
                    user[$scope.item.createdByGuid] = $scope.item.createdByName;
                    self.createdBy = user;
                    utilitiesService.setLastModifiedBy(self, $scope.item);
                    prevItem = _.cloneDeep($scope.item);

                }
            }, function (error) {
                self.error = true;
                self.errorMsg = error.data.message;
            });
        };

        self.saveSelectedField = function(fieldName) {
            $scope.item[fieldName] = $scope.selected[fieldName];
            return self.saveByField(fieldName);
        };

        var setAgendaTabFields = function () {
            $scope.selected.agenda = $scope.item.agenda;
            $scope.selected.whatsForOrg = $scope.item.whatsForOrg;
            $scope.selected.whatsForTrainees = $scope.item.whatsForTrainees;
        };

        var setCurrentStatus = function () {
            self.isTrainingClosed = $scope.item.status === 'CLOSED';
            self.isTrainingComplete = self.isTrainingClosed || $scope.item.status === 'COMPLETED';
            if ($scope.item.status) {
                self.trainingCurrentStatus = _.find(self.trainingStatus, {code: $scope.item.status}).label;
                updateTraineesCount();
            }
        };

        self.getAttachments = function () {
            trainingService.getAttachments(self.trainingId).then(function (response) {
                $scope.item.attachments = response.data;
            });
        };

        var afterSave = function(fieldName) {
            utilitiesService.setLastModifiedBy(self);
            setPrevItemField(fieldName);
            utilitiesService.setEditable(self, fieldName, false);
            self['error' + _.upperFirst(fieldName)] = undefined;
        };

        var resetFields = function () {
            self.feedBackRequestSent = false;
            self.trainingRequestSent = false;
        };

        self.saveByField = function (fieldName) {
            resetFields();
            var deferred = $q.defer();

            var data = {name: fieldName, value: _.get($scope.item, fieldName)};
            if(_.isEqual(data.value, prevItem[fieldName])) {
                afterSave(fieldName);
                deferred.resolve(true);
            }
            switch (fieldName) {
                case 'scheduledOn':
                    var date = moment($scope.item.scheduledOn);
                    var date2 = moment(prevItem.scheduledOn);
                    if (date2.isValid()) {
                        var diff = date.diff(date2, 'minutes');
                        if (!date.isValid() || isNaN(diff) || diff === 0) {
                            $scope.item.scheduledOn = _.cloneDeep(prevItem.scheduledOn);
                            utilitiesService.setEditable(self, fieldName, false);
                            deferred.resolve(false);
                        }
                    }
                    data.value = utilitiesService.toISODateString(date);
                    break;
                case 'status':
                    if(data.value === 'SCHEDULED') {
                        self.errorStatus = getIncompleteMsg();
                        if(!_.isEmpty(self.errorStatus)) {
                            $scope.item.status = prevItem.status;
                            deferred.resolve(false);
                        }
                    }
                    break;
                case 'name':
                    if(_.isEmpty(data.value)) {
                        self.errorName = 'Name can\'t be empty';
                        deferred.resolve(false);
                    }
                    break;
            }
            if(deferred.promise.$$state.status === 1) {
                return deferred.promise;
            }
            return trainingService.updateTrainingByField($scope.item.id, data).then(function (response) {
                if (restService.isResponseOk(response)) {
                    afterSave(fieldName);
                    switch (fieldName) {
                        case 'agenda':
                        case 'whatsForTrainees':
                        case 'whatsForOrg':
                            setAgendaTabFields();
                            break;
                        case 'status':
                        case 'locations':
                            setCurrentStatus();
                            break;
                        case 'trainees':
                            updateTraineesCount();
                            break;
                    }
                    return $q.resolve(true);
                }
            }).catch(function(response) {
                self['error' + _.upperFirst(fieldName)] = response.data.message;
                self.prevErrorFieldName = fieldName;
                $q.resolve(response);
            });
        };

        var setPrevItemField = function(fieldName) {
            var fldName = _.lowerFirst(fieldName);
            prevItem[fldName] = _.cloneDeep($scope.item[fldName]);
        };

        var resetPrevItemField = function(fieldName) {
            var fldName = _.lowerFirst(fieldName);
            $scope.item[fldName] = prevItem[fldName];
        };

        self.setEditStatus = function(fieldName, status) {
            if(status) {
                if(self.isTrainingClosed) {
                    return false;
                }
                if(fieldName !== 'Status' && self.isTrainingComplete) {
                    return false;
                }
                switch (fieldName) {
                    case 'ScheduledOn':
                        if(!self.isAdmin && self.isEditable && $scope.item.status !== 'NOMINATED') {
                            return false;
                        }
                        break;
                    case 'Status':
                        if(!self.isAdmin) {
                            return false;
                        }
                        break;
                }
            }
            else {
                resetPrevItemField(fieldName);
            }
            self['error' + fieldName] = undefined;
            utilitiesService.setEditable(self, fieldName, status);
        };

        self.showConfirmationDlg = function (data) {
            var opts = {
                templateUrl: '/pbacademy/html/main/confirmationDlg.html',
                controller: 'modalController as self',
                backdrop: 'static',
                resolve: {
                    data: function () {
                        return {msg: data.msg, item: {guid: data.guid}};
                    }
                }
            };
            $uibModal.open(opts).result.then(function (selectedItem) {
                self.doRemove(data.guid)
            }, function () {
                
            });
        };

        self.showModalDlg = function () {
            var opts = {
                templateUrl: '/pbacademy/html/training/trainingCreate.html',
                backdrop: 'static',
                controller: 'modalController as self',
                resolve: {
                    data: function () {
                        return {item: self.training, mode: self.mode, options: {managers: self.managers}};
                    }
                }
            };
            $uibModal.open(opts).result.then(function (selectedItem) {
                self.training = selectedItem;
                if (self.mode === 'add') {
                    self.add();
                }
                else if (self.mode === 'edit') {
                    self.save();
                }
            }, function () {
                
            });
        };

        self.showTrainingAdd = function () {
            self.training = {};
            self.mode = 'add';
            self.showModalDlg();
        };

        self.showModalDlg = function () {
            var opts = {
                templateUrl: '/pbacademy/html/training/trainingCreate.html',
                backdrop: 'static',
                controller: 'modalController as self',
                resolve: {
                    data: function () {
                        return {item: self.training, mode: self.mode, options: {trainingId: self.trainingId}};
                    }
                }
            };
            $uibModal.open(opts).result.then(function (selectedItem) {
                self.training = selectedItem;
                if (self.mode === 'add') {
                    self.add();
                }
                else if (self.mode === 'edit') {
                    self.save();
                }
            }, function () {
                
            });
        };

        self.uploadFiles = function (attachedFiles, errFiles) {
            self.attachmentUploadErrorMsg = undefined;
            self.attachedFiles = attachedFiles;
            self.errFiles = errFiles;
            angular.forEach(attachedFiles, function (file) {
                file.upload = Upload.upload({
                    url: self.attachmentUploadUrl,
                    data: {file: file}
                });

                file.upload.then(function (response) {
                    $timeout(function () {
                        file.result = response.data;
                        self.attachedFiles = undefined;
                        self.getAttachments();
                    });
                }, function (response) {
                    self.attachedFiles = undefined;
                    if (response.status > 0)
                        self.attachmentUploadErrorMsg = response.data.message;
                }, function (evt) {
                    file.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
                });
            });
        };

        self.removeAttachment = function (filename) {
            trainingService.removeAttachment(self.trainingId, filename).then(function (response) {
                self.attachmentUploadErrorMsg = undefined;
                self.attachedFiles = undefined;
                self.getAttachments();
            });
        };

        self.sendFeedBackRequest = function () {
            self.errorStatus = getIncompleteMsg();
            if(!_.isEmpty(self.errorStatus)) {
                return;
            }
            self.sendingTrainingFeedbackRequest = true;
            trainingService.sendFeedBackRequest($scope.item.id).then(function (response) {
                self.feedBackRequestSent = false;
                if (response.data) {
                    self.feedBackRequestSent = true;
                    self.sendingTrainingFeedbackRequest = false;
                }
            });
        };

        self.sendTrainingRequest = function () {
            self.errorStatus = getIncompleteMsg();
            if(!_.isEmpty(self.errorStatus)) {
                return;
            }
            self.sendingTrainingRequest = true;
            trainingService.sendTrainingRequest($scope.item.id).then(function (response) {
                self.trainingRequestSent = false;
                if (response.data) {
                    self.trainingRequestSent = true;
                    self.sendingTrainingRequest = false;
                }
            });
        };

        self.removeLocation = function (locationId) {
            self.item.locations = _.remove(self.item.locations, {id: locationId});
            self.saveByField('locations');
        };

        self.cancelTextEdit = function (field) {
            $scope.selected[field] = $scope.item[field];
            self['edit' + _.upperFirst(field)] = false;
        };

        var getTrainingLocations = function() {
            trainingRoomService.listTrainingRoomsBrief().then(function (response) {
                if (angular.isDefined(response.data)) {
                    self.trainingLocations = [];
                    _.each(response.data, function(room) {
                        self.trainingLocations.push({id: room.id, name: room.name + ", " + room.location});
                    });
                }
            });
        };

        $scope.$on('training.refresh', function () {
            trainingService.getLikesCount(self.item.id).then(function(result) {
                if(angular.isDefined(result.data)) {
                    self.item.likesCount = result.data;
                }
            });
        });

        var getIncompleteMsg = function () {
            var msg = '';
            if(_.isEmpty($scope.item.whatsForTrainees)) {
                msg = 'Whats for Trainees, ';
                self.errorWhatsForTrainees = 'Whats for Trainees can\'t be empty';
            }
            if(_.isEmpty($scope.item.whatsForOrg)) {
                msg += 'Whats for Trainees, ';
                self.errorWhatsForOrg = 'What\'s For Organisation can\'t be empty';
            }
            if(_.isEmpty($scope.item.agenda)) {
                msg += 'Agenda, ';
                self.errorAgenda = 'Agenda can\'t be empty';
            }
            if(_.isEmpty($scope.item.name)) {
                msg += "Name, ";
                self.errorName = 'Name can\'t be empty';
            }
            if(_.isEmpty($scope.item.locations)) {
                msg += "Locations, ";
                self.errorLocation = 'Locations can\'t be empty';
            }
            if(!_.isNumber($scope.item.duration)) {
                msg += "Duration, ";
                self.errorDuration = 'Duration can\'t be empty';
            }
            if(!(_.isNumber($scope.item.scheduledOn) || _.isDate($scope.item.scheduledOn))) {
                msg += "Scheduled On, ";
                self.errorScheduledOn = 'ScheduledOn can\'t be empty';
            }
            if(angular.isUndefined($scope.item.topics)) {
                msg += "Topics, ";
                self.errorTopics = 'Topics can\'t be empty';
            }
            if(!_.isEmpty(msg)) {
                msg = msg.substr(0, msg.lastIndexOf(',')) + ' field(s) can\'t be empty';
            }
            return msg;
        };

        self.saveOnEnterKey = function($event, fieldName) {
            if($event.which === 13) {
                $event.stopPropagation();
                $event.preventDefault();
                self.saveByField(fieldName);
            }
            else {
                self['error' + _.upperFirst(fieldName)] = undefined;
            }
        };

        self.cancelOnEnterKey = function($event, fieldName) {
            if($event.keyCode === 27) {
                $event.stopPropagation();
                $event.preventDefault();
                self.setEditStatus(fieldName);
            }
        };

        self.init();
    }]).constant('trainingStatus', [{label: 'All', code: null}, {label: 'Nominated', code: 'NOMINATED'},
    {label: 'Scheduled', code: 'SCHEDULED'}, {label: 'Completed', code: 'COMPLETED'},
    {label: 'Postponed', code: 'POSTPONED'}, {label: 'Cancelled', code: 'CANCELLED'}, {label: 'Closed', code: 'CLOSED'}]);