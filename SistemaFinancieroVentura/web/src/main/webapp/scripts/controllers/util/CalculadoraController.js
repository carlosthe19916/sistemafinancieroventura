define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('CalculadoraController', function ($scope, $modalInstance, denominaciones, moneda) {

        $scope.denominaciones = denominaciones;
        $scope.moneda = moneda;

        $scope.total = function(){
            var totalCalculadora = 0;
            for(var i = 0; i < $scope.denominaciones.length; i++){
                totalCalculadora = totalCalculadora + ($scope.denominaciones[i].cantidad * $scope.denominaciones[i].valor);
            }
            return totalCalculadora;
        }

        $scope.ok = function () {
            if (($scope.total() != 0 && $scope.total() !== undefined)) {
                $modalInstance.close($scope.total());
            }
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    });
});