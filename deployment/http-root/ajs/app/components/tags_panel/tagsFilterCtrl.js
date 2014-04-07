'use strict';
angular.module('codeine').controller('tagsFilterCtrl',['$scope','$rootScope', '$log','Constants','$location',
    function($scope,$rootScope,$log,Constants,$location) {
        $log.debug('tagsFilterCtrl: created');
        $scope.maxTags = 10;

        $scope.initTagsFromQueryString = function() {
            var queryStringObject = $location.search();
            var shouldRefresh = false;
            for (var j=0; j < $scope.projectStatus.tag_info.length ; j++) {
                $scope.projectStatus.tag_info[j].state = 0;
            }
            if (angular.isDefined(queryStringObject.tagsOn)) {
                shouldRefresh = true;
                $log.debug('projectStatusCtrl: Tags on init from query string - ' + queryStringObject.tagsOn);
                var array = queryStringObject.tagsOn.split(',');
                for (var i=0; i < array.length; i++) {
                    for (var k=0; k < $scope.projectStatus.tag_info.length ; k++) {
                        if ($scope.projectStatus.tag_info[k].name === array[i]) {
                            $scope.projectStatus.tag_info[k].state = 1;
                        }
                    }
                }
            }
            if (angular.isDefined(queryStringObject.tagsOff)) {
                shouldRefresh = true;
                $log.debug('tagsFilterCtrl: Tags on init from query string - ' + queryStringObject.tagsOff);
                var array2 = queryStringObject.tagsOff.split(',');
                for (var i1=0; i1 < array2.length; i1++) {
                    for (var f=0; f < $scope.projectStatus.tag_info.length ; f++) {
                        if ($scope.projectStatus.tag_info[f].name === array2[i1]) {
                            $scope.projectStatus.tag_info[f].state = 2;
                        }
                    }
                }
            }
            return shouldRefresh;
        };

        $scope.initTagsFromQueryString();

        $scope.updateTags = function() {
            var on = [], off = [];
            for (var i=0; i < $scope.projectStatus.tag_info.length ; i++) {
                if ($scope.projectStatus.tag_info[i].state === 1) {
                    on.push($scope.projectStatus.tag_info[i].name);
                } else if ($scope.projectStatus.tag_info[i].state === 2) {
                    off.push($scope.projectStatus.tag_info[i].name);
                }
            }
            $scope.$apply(function() {
                $location.search('tagsOn',on.join(','));
                $location.search('tagsOff',off.join(','));
            });
            $rootScope.$emit(Constants.EVENTS.TAGS_CHANGED);
        };
    }]);