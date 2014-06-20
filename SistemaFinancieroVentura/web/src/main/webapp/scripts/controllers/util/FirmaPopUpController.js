define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('FirmaPopUpController', function ($scope, $modalInstance, idPersonas, nombres) {

        $scope.idPersonas = idPersonas;
        $scope.nombres = nombres;

        $scope.getUrlFirma = function(index) {
            return "http://localhost:8080/SistemaFinancieroVentura-web/services/personanatural/image/"+$scope.idPersonas[index]+"/firma";
        }
        $scope.getNombre = function(index){
            return $scope.nombres[index];
        }

        $scope.ok = function () {
            $modalInstance.close();
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    });
});