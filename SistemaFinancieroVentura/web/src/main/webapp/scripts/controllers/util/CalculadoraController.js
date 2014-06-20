define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('CalculadoraController', function ($scope, $modalInstance, denominaciones, moneda) {

        $scope.control = {"mode": true};

        $scope.denominaciones = denominaciones;
        $scope.moneda = moneda;

        for(var i = 0; i < $scope.denominaciones.length; i++){
            $scope.denominaciones[i].subtotal = $scope.denominaciones[i].valor * $scope.denominaciones[i].cantidad;
        }

        $scope.$watch("denominaciones",function (newVal, oldVal) {
            if(newVal != oldVal){
                if($scope.control.mode){
                    for(var i = 0; i < $scope.denominaciones.length; i++){
                        $scope.denominaciones[i].subtotal = $scope.denominaciones[i].valor * $scope.denominaciones[i].cantidad;
                    }
                } else {
                    for(var i = 0; i < $scope.denominaciones.length; i++){
                        var cant = $scope.denominaciones[i].subtotal / $scope.denominaciones[i].valor;
                        $scope.denominaciones[i].cantidad = Math.floor(cant);
                    }
                }
            }
        },true);

        $scope.total = function(){
            var totalCalculadora = 0;
            for(var i = 0; i < $scope.denominaciones.length; i++){
                totalCalculadora = totalCalculadora + ($scope.denominaciones[i].cantidad * $scope.denominaciones[i].valor);
            }
            return totalCalculadora;
        }

        $scope.ok = function () {
            if (($scope.total() != 0 && $scope.total() !== undefined && !isNaN($scope.total()))) {
                $modalInstance.close($scope.total());
            }
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    });
});