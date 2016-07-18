angular.module('controllers').controller('trainingController', ['$scope', '$uibModal', 'trainingService', 'pagingService',
    '$routeParams', 'restService', 'utilitiesService', 'employeeService', 'topicService', 'Upload', '$timeout',
    'trainingStatus', 'trainingLocations',
    function ($scope, $uibModal, trainingService, pagingService, $routeParams, restService, utilitiesService,
              employeeService, topicService, Upload, $timeout, trainingStatus, trainingLocations) {
        var self = this;
        $scope.comments = [];
        $scope.feedbackCount = 0;
        $scope.commentsCount = 0;
        $scope.item = {};
        self.isDatePickerOpen = false;
        self.datePickerOptions = {
            'show-button-bar': false,
            'showWeeks': false,
            // 'minDate': new Date(),
            maxDate: moment().add(3, 'M').toDate()
        };
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

        self.stepperOptions = trainingService.durationStepperOptions;

        $scope.selected = {};

        self.trainingStatus = _.cloneDeep(trainingStatus);
        _.remove(self.trainingStatus, {code: null});
        self.trainingLocations = _.cloneDeep(trainingLocations);

        self.init = function () {
            if (!_.isEmpty($routeParams.trainingId)) {
                self.trainingId = $routeParams.trainingId;
                self.getTraining();
                self.getAttachments();
                trainingService.pushRecentTrainingId(self.trainingId);
            }
            employeeService.listEmployees().then(function (response) {
                if (angular.isDefined(response.data)) {
                    self.employees = response.data.content;
                }
            });
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
        });

        $scope.$watch('selected.duration', function () {
            if ($scope.selected.duration !== $scope.item.duration) {
                $scope.item.duration = $scope.selected.duration;
                self.saveByField('duration');
            }
        });

        $scope.$watch('selected.trainee', function () {
            if ($scope.selected.trainee && (!$scope.item.trainers || !$scope.item.trainers[$scope.selected.trainee.guid])) {
                $scope.item.trainees = utilitiesService.addUnique($scope.item.trainees, $scope.selected.trainee, 'guid', 'name');
                self.saveByField('trainees');
                self.feedBackRequestSent = false;
            }

        });

        var updateTraineesCount = function () {
            self.traineesCount = _.size($scope.item.trainees);
            self.isFeedBackReady = self.traineesCount > 0 && self.isTrainingComplete;
            self.isScheduled = $scope.item.status === 'SCHEDULED';
        }
        $scope.$watch('selected.topic', function () {
            if ($scope.selected.topic && (!$scope.item.prerequisites || !$scope.item.prerequisites[$scope.selected.topic.id])) {
                $scope.item.topics = utilitiesService.addUnique($scope.item.topics, $scope.selected.topic, 'id', 'name');
                self.saveByField('topics');
            }
        });

        $scope.$watch('selected.prerequisite', function () {
            if ($scope.selected.prerequisite && (!$scope.item.topics || !$scope.item.topics[$scope.selected.prerequisite.id])) {
                $scope.item.prerequisites = utilitiesService.addUnique($scope.item.prerequisites, $scope.selected.prerequisite, 'id', 'name');
                self.saveByField('prerequisites');
            }
        });

        $scope.$watch('scheduledOn', function () {
            $scope.scheduledOnView = '';
            if ($scope.scheduledOn) {
                $scope.scheduledOnView = moment($scope.scheduledOn).format('DD-MMM-YY, HH:mm A');
            }
        });

        self.setListResult = function (data) {
            self.totalPages = data.totalPages;
            self.totalCount = data.totalElements;
            self.searchedTrainings = {totalCount: self.totalCount, data: data.content};
            self.fromCount = (data.size * data.number) + 1;
            self.toCount = (self.fromCount - 1) + data.numberOfElements;
        };

        self.getTraining = function () {
            trainingService.getTraining(self.trainingId).then(function (response) {
                if (angular.isDefined(response.data)) {
                    $scope.item = _.defaultsDeep($scope.item, response.data);
                    trainingService.setEditables($scope.item, self);

                    if ($scope.item.scheduledOn) {
                        $scope.scheduledOn = moment($scope.item.scheduledOn).toDate();
                        self.attachmentUploadUrl = trainingService.trainingUrl + '/' + $scope.item.id + '/attachment';
                    }
                    if (angular.isDefined($scope.item.duration)) {
                        $scope.selected.duration = $scope.item.duration;
                    }
                    $scope.selected.agenda = $scope.item.agenda;
                    setCurrentStatus();

                    var user = {};
                    user[$scope.item.createdByGuid] = $scope.item.createdByName;
                    self.createdBy = user;
                    utilitiesService.setLastModifiedBy(self, $scope.item);
                }
            }, function (error) {
                self.error = true;
                self.errorMsg = error.data.message;
            });
        }

        var setCurrentStatus = function () {
            self.isTrainingComplete = $scope.item.status === 'COMPLETED';
            if ($scope.item.status) {
                self.trainingCurrentStatus = _.find(self.trainingStatus, {code: $scope.item.status}).label;
                updateTraineesCount();
            }
            if($scope.item.location) {
                self.trainingCurrentLocation = _.find(self.trainingLocations, {code: $scope.item.location}).label;
            }
        };

        self.getAttachments = function () {
            trainingService.getAttachments(self.trainingId).then(function (response) {
                $scope.item.attachments = response.data;
            });
        }

        self.saveByField = function (fieldName) {
            var data = {name: fieldName, value: _.get($scope.item, fieldName)};
            switch (fieldName) {
                case 'scheduledOn':
                    var date = moment($scope.scheduledOn);
                    var date2 = moment($scope.item.scheduledOn);
                    if (date2.isValid()) {
                        var diff = date.diff(date2, 'minutes');
                        if (!self.isDatePickerOpen || !date.isValid() || isNaN(diff) || diff === 0) {
                            return;
                        }
                    }
                    data.value = utilitiesService.toISODateString(date);
                    break;
                case 'agenda':
                    $scope.item.agenda = $scope.selected.agenda;
                    data.value = $scope.selected.agenda;
                    break;
            }
            trainingService.updateTrainingByField($scope.item.id, data).then(function (response) {
                if (restService.isResponseOk(response)) {
                    utilitiesService.setEditable(self, fieldName, false);
                    self['error' + _.upperFirst(fieldName)] = undefined;
                    if (fieldName == 'status' || fieldName == 'location') {
                        setCurrentStatus();
                    }
                    else if(fieldName === 'trainees') {
                        updateTraineesCount();
                    }
                    utilitiesService.setLastModifiedBy(self);
                }
            }).catch(function(response) {
                self['error' + _.upperFirst(fieldName)] = response.data.message;
                self.prevErrorFieldName = fieldName;
            });
        },

        self.setEditStatus = function(fieldName, status) {
            if(status) {
                if(self.isTrainingComplete || !self.isAdmin && !self.isEditable) {
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
            utilitiesService.setEditable(self, fieldName, status);
        },

        self.showConfirmationDlg = function (data) {
            var opts = {
                templateUrl: '/lunchandlearn/html/main/confirmationDlg.html',
                controller: 'modalController as self',
                backdrop: 'static',
                resolve: {
                    data: function () {
                        return {msg: data.msg, item: {guid: data.guid}};
                    }
                }
            }
            $uibModal.open(opts).result.then(function (selectedItem) {
                self.doRemove(data.guid)
            }, function () {
                console.log('confirmation modal cancelled')
            });
        }

        self.showModalDlg = function () {
            var opts = {
                templateUrl: '/lunchandlearn/html/training/trainingCreate.html',
                backdrop: 'static',
                controller: 'modalController as self',
                resolve: {
                    data: function () {
                        return {item: self.training, mode: self.mode, options: {managers: self.managers}};
                    }
                }
            }
            $uibModal.open(opts).result.then(function (selectedItem) {
                self.training = selectedItem;
                if (self.mode === 'add') {
                    self.add();
                }
                else if (self.mode === 'edit') {
                    self.save();
                }
            }, function () {
                console.log('confirmation modal cancelled')
            });
        };

        self.showTrainingAdd = function () {
            self.training = {};
            self.mode = 'add';
            self.showModalDlg();
        };

        self.showModalDlg = function () {
            var opts = {
                templateUrl: '/lunchandlearn/html/training/trainingCreate.html',
                backdrop: 'static',
                controller: 'modalController as self',
                resolve: {
                    data: function () {
                        return {item: self.training, mode: self.mode, options: {trainingId: self.trainingId}};
                    }
                }
            }
            $uibModal.open(opts).result.then(function (selectedItem) {
                self.training = selectedItem;
                if (self.mode === 'add') {
                    self.add();
                }
                else if (self.mode === 'edit') {
                    self.save();
                }
            }, function () {
                console.log('confirmation modal cancelled')
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
        }

        self.removeAttachment = function (filename) {
            trainingService.removeAttachment(self.trainingId, filename).then(function (response) {
                self.attachmentUploadErrorMsg = undefined;
                self.attachedFiles = undefined;
                self.getAttachments();
            });
        }

        self.sendFeedBackRequest = function () {
            trainingService.sendFeedBackRequest().then(function (response) {
                if (response.data.status) {
                    self.feedBackRequestSent = true;
                }
            })
        }

        self.cancelAgenda = function () {
            $scope.selected.agenda = $scope.item.agenda;
            self.editAgenda = false;
        }

        self.init();
    }]).constant('trainingStatus', [{label: 'All', code: null}, {label: 'Nominated', code: 'NOMINATED'},
    {label: 'Scheduled', code: 'SCHEDULED'}, {label: 'Completed', code: 'COMPLETED'},
    {label: 'Postponed', code: 'POSTPONED'}, {label: 'Cancelled', code: 'CANCELLED'}]).constant('trainingLocations',
    [{label: 'Seine', code: 'Seine'}, {label: 'Ganga', code: 'Ganga'},
    {label: 'Rhine', code: 'Rhine'}, {label: 'Godavari', code: 'Godavari'},
    {label: 'Everest', code: 'Everest'}, {label: 'CEC', code: 'CEC'}]);