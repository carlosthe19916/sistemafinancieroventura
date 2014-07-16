define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('RenovarPlazoFijoController', [ "$scope","$state","$filter","$timeout","focus","CuentaBancariaService","TasaInteresService","RedirectService",
        function($scope,$state,$filter,$timeout,focus,CuentaBancariaService,TasaInteresService,RedirectService) {

            $scope.focusElements = {
                tasaInteres: 'focusTasaInteres'
            };
            $scope.setInitialFocus = function($event){
                if(!angular.isUndefined($event))
                    $event.preventDefault();
                $timeout(function() {
                    focus($scope.focusElements.tasaInteres);
                }, 100);
            };
            $scope.setInitialFocus();

            $scope.control = {
                success: false,
                inProcess: false,
                submitted : false
            };

            $scope.view = {
                monto: undefined,
                tasaInteres: 0,
                periodo: 0,
                total: 0
            };

            $scope.alerts = [];

            $scope.dateOptions = {
                formatYear: 'yyyy',
                startingDay: 1
            };

            $scope.open = function($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.opened = true;
            };

            $scope.loadCuentaBancaria = function(){
                if(!angular.isUndefined($scope.id)){
                    CuentaBancariaService.getCuentasBancariaView($scope.id).then(
                        function(data){
                            $scope.cuentaBancaria = data;
                            $scope.view.tasaInteres = ($scope.cuentaBancaria.tasaInteres*100).toString();

                            var fechaInicioString = $filter('date')($scope.cuentaBancaria.fechaApertura, 'yyyy-MM-dd');
                            var fechaFinString = $filter('date')($scope.cuentaBancaria.fechaCierre, 'yyyy-MM-dd');
                            var fechaInicio = new Date(fechaInicioString);
                            var fechaFin = new Date(fechaFinString);

                            var tasa = $scope.cuentaBancaria.tasaInteres;
                            var periodo = Math.round((fechaFin.getTime() - fechaInicio.getTime())/(24*60*60*1000));
                            $scope.view.periodo = periodo;
                            var monto = $scope.cuentaBancaria.saldo;
                            $scope.view.monto = TasaInteresService.getInteresGenerado(tasa, periodo, monto);
                        }, function error(error){
                            $scope.cuentaBancaria = undefined;
                            $scope.alerts.push({ type: "danger", msg: "Cuenta bancaria no encontrada."});
                        }
                    );
                }
            };
            $scope.loadSocio = function(){
                if(!angular.isUndefined($scope.id)){
                    CuentaBancariaService.getSocio($scope.id).then(
                        function(data){
                            $scope.socio = data;
                        }, function error(error){
                            $scope.socio = undefined;
                            $scope.alerts.push({ type: "danger", msg: "Socio no encontrado."});
                        }
                    );
                };
            };
            $scope.loadCuentaBancaria();
            $scope.loadSocio();

            $scope.redireccion = function(){
                if(RedirectService.haveNext()){
                    var nextState = RedirectService.getNextState();
                    var parametros = RedirectService.getNextParamsState();
                    $state.transitionTo(nextState,parametros);
                } else {
                    $state.transitionTo('app.socio.buscarCuentaBancaria');
                }
            };

            $scope.crearTransaccion = function(){
                if ($scope.formRecalcularCuenta.$valid) {
                    $scope.control.inProcess = true;

                    //poniendo variables
                    var data = {
                        "periodo": $scope.view.periodo,
                        "tasaInteres": $scope.view.tasaInteres/100
                    };

                    CuentaBancariaService.renovarPlazoFijo($scope.id, data).then(
                        function(data){
                            $scope.control.inProcess = false;
                            $scope.control.success = true;
                            var mensaje= data.message;
                            var sendParameters = {
                                id: $scope.id,
                                redirect: true
                            };
                            $state.transitionTo("app.socio.editarCuentaBancaria", sendParameters);
                        }, function error(error){
                            $scope.control.inProcess = false;
                            $scope.control.success = false;
                            $scope.alerts = [{ type: "danger", msg: "Error: " + error.data.message + "."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                } else {
                    $scope.control.submitted = true;
                }
            };

            $scope.$watch('view.tasaInteres', function () {
                $scope.view.total = TasaInteresService.getInteresGenerado($scope.view.tasaInteres/100, $scope.view.periodo, $scope.view.monto);
            });
            $scope.$watch('view.monto', function () {
                $scope.view.total = TasaInteresService.getInteresGenerado($scope.view.tasaInteres/100, $scope.view.periodo, $scope.view.monto);
            });
            $scope.$watch('view.periodo', function () {
                $scope.view.total = TasaInteresService.getInteresGenerado($scope.view.tasaInteres/100, $scope.view.periodo, $scope.view.monto);
            });

            $scope.cancelar = function () {
                $scope.redireccion();
            };

            $scope.buttonDisableState = function(){
                return $scope.control.inProcess;
            };

        }]);
});