define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('RecalcularPlazoFijoPopUpController', [ "$scope","focus","$modalInstance","TasaInteresService","cuentaBancaria",
        function($scope,focus,$modalInstance,TasaInteresService, cuentaBancaria) {

            $scope.formRecalcularPopUp = {};

            $scope.view = {
                moneda: cuentaBancaria.moneda,
                saldo: parseFloat(cuentaBancaria.saldo.toString()),
                fechaApertura: cuentaBancaria.fechaApertura,
                fechaCierre: cuentaBancaria.fechaCierre,
                tasaInteres: cuentaBancaria.tasaInteres,
                periodo: 0,
                total: 0
            };

            $scope.dateOptions = {
                formatYear: 'yyyy',
                startingDay: 1
            };

            $scope.openApertura = function($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.openedApertura = true;
            };

            $scope.openCierre = function($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.openedCierre = true;
            };

            $scope.recalcular = function(){
                $scope.view.total = TasaInteresService.getInteresGenerado($scope.view.tasaInteres, $scope.view.periodo, $scope.view.saldo);
            };

            $scope.$watch("view.fechaCierre", function(){
                if(!angular.isUndefined($scope.view.fechaApertura) && !angular.isUndefined($scope.view.fechaCierre)){
                    $scope.view.periodo = Math.round(Math.abs(($scope.view.fechaApertura.getTime() - $scope.view.fechaCierre.getTime())/(24*60*60*1000)))
                }
            });

            $scope.$watch('view.tasaInteres', function () {
                $scope.recalcular();
            });
            $scope.$watch('view.periodo', function () {
                $scope.recalcular();
            });

            $scope.ok = function () {
                var resultado = {
                    periodo: $scope.view.periodo,
                    tasaInteres: $scope.view.tasaInteres
                };
                $modalInstance.close(resultado);
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

        }]);
});