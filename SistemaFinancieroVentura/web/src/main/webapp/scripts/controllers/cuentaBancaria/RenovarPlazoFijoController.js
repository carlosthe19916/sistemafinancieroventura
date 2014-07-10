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
                tasaInteres: 0,
                fechaCierre: undefined,
                periodo: 0,
                total: 0
            };

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
                    CuentaBancariaService.getCuentasBancaria($scope.id).then(
                        function(data){
                            $scope.cuentaBancaria = data;
                            $scope.view.fechaCierre = $scope.cuentaBancaria.fechaCierre;
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

            $scope.recalcular = function(){
                if(!angular.isUndefined($scope.cuentaBancaria)){

                    var fechaInicioString = $filter('date')($scope.cuentaBancaria.fechaApertura, 'yyyy-MM-dd');
                    var fechaFinString = $filter('date')($scope.view.fechaCierre, 'yyyy-MM-dd');
                    var fechaInicio = new Date(fechaInicioString);
                    var fechaFin = new Date(fechaFinString);

                    var periodo = Math.round((fechaFin.getTime() - fechaInicio.getTime())/(24*60*60*1000));
                    if(periodo < 0)
                        return;
                    else
                        $scope.view.periodo = periodo;

                    var tasa = $scope.view.tasaInteres;
                    var peri = $scope.view.periodo;
                    var monto = angular.isUndefined($scope.cuentaBancaria) ? 0 : $scope.cuentaBancaria.saldo;

                    if(!angular.isUndefined(tasa) &&  !angular.isUndefined(peri) && !angular.isUndefined(monto))
                        $scope.view.total = TasaInteresService.getInteresGenerado(tasa, peri, monto);
                    else
                        $scope.view.total = 0;
                }
            };

            $scope.crearTransaccion = function(){
                if ($scope.formRecalcularCuenta.$valid) {
                    $scope.control.inProcess = true;

                    //poniendo variables
                    var data = {
                        "periodo": $scope.view.periodo,
                        "tasaInteres": $scope.view.tasaInteres
                    };

                    CuentaBancariaService.recalcularPlazoFijo($scope.id, data).then(
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

            $scope.$watch("view.fechaCierre", function(){
                $scope.recalcular();
            });

            $scope.$watch('view.tasaInteres', function () {
                $scope.recalcular();
            });

            $scope.cancelar = function () {
                $scope.redireccion();
            };

            $scope.buttonDisableState = function(){
                return $scope.control.inProcess;
            };

        }]);
});