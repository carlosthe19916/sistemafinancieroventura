define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('FirmaPopUpController', function ($scope, $modalInstance, idPersona, descripcion) {

        $scope.descripcion = descripcion;

        $scope.getUrlFirma = function(){
            return "http://localhost:8080/SistemaFinancieroVentura-web/services/personanatural/image/"+idPersona+"/firma";
        }

        $scope.ok = function () {
            $modalInstance.close();
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    });
});