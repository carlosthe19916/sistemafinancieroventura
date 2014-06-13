define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('ConfirmPopUpController', function ($scope, $modalInstance) {

        $scope.ok = function () {
            $modalInstance.close();
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    });
});