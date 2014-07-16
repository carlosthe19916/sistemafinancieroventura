define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('ConfirmPopUpController', function ($scope,$timeout,focus,$modalInstance) {

        $scope.focusElements = {
            si: 'focusSi',
            no: 'focusNo'
        };
        $scope.setInitialFocus = function($event){
            if(!angular.isUndefined($event))
                $event.preventDefault();
            $timeout(function() {
                focus($scope.focusElements.si);
            }, 100);
        };
        $scope.setInitialFocus();

        $scope.ok = function () {
            $modalInstance.close();
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    });
});