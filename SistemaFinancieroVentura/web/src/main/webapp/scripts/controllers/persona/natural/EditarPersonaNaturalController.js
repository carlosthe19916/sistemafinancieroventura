define(['../../module'], function (controllers) {
    'use strict';
    controllers.controller('EditarPersonaNaturalController', ['$scope', '$state','$stateParams','$window', 'MaestroService', 'PersonaNaturalService','RedirectService',
        function($scope,$state,$stateParams,$window,MaestroService,PersonaNaturalService,RedirectService) {

            $scope.control = {
                "success":false,
                "inProcess": false,
                "submitted" : false
            };
            $scope.view = {
                persona: PersonaNaturalService.getModel(),
                dateOptions: {
                    formatYear: 'yyyy',
                    startingDay: 1
                },
                tipoDocumentos: undefined,
                sexos: undefined,
                estadosCiviles: undefined,
                paises: undefined
            };

            $scope.open = function($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.opened = true;
            };

            $scope.$watch("view.persona.numeroDocumento",function (newVal, oldVal) {
                if (newVal !== oldVal) {
                    $scope.validarNumeroDocumento();
                }
            },true);
            $scope.validarNumeroDocumento = function(){
                if(!angular.isUndefined($scope.formEditarPersonanatural.numeroDocumento)){
                    if(!angular.isUndefined($scope.view.persona.numeroDocumento)){
                        if(!angular.isUndefined($scope.view.persona.tipoDocumento)){
                            if($scope.view.persona.numeroDocumento.length == $scope.view.persona.tipoDocumento.numeroCaracteres) {
                                $scope.formEditarPersonanatural.numeroDocumento.$setValidity("sgmaxlength",true);
                            } else {$scope.formEditarPersonanatural.numeroDocumento.$setValidity("sgmaxlength",false);}
                        } else{$scope.formEditarPersonanatural.numeroDocumento.$setValidity("sgmaxlength",false);}
                    } else {$scope.formEditarPersonanatural.numeroDocumento.$setValidity("sgmaxlength",false);}}
            };

            $scope.loadTipoDocumentoPN = function(){
                MaestroService.getTipoDocumentoPN().then(function(data){
                    $scope.view.tipoDocumentos = data;
                });
            };
            $scope.loadSexos = function(){
                MaestroService.getSexos().then(function(data){
                    $scope.view.sexos = data;
                });
            };
            $scope.loadEstadosCiviles = function(){
                MaestroService.getEstadosciviles().then(function(data){
                    $scope.view.estadosCiviles = data;
                });
            };
            $scope.loadPaises = function(){
                MaestroService.getPaises().then(function(data){
                    $scope.view.paises = data;
                });
            };

            $scope.loadTipoDocumentoPN();
            $scope.loadSexos();
            $scope.loadEstadosCiviles();
            $scope.loadPaises();

            $scope.loadPersonaNatural = function(){
                if(!angular.isUndefined($scope.id)){
                    PersonaNaturalService.findById($scope.id).then(
                        function(data){
                            $scope.view.persona = data;
                        }, function error(error){
                            $scope.alerts = [{ type: "danger", msg: "Error: No se pudo cargar la persona."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                };
            };
            $scope.loadPersonaNatural();


            $scope.$watch(
                function () {
                    return {
                        persona: $scope.view.persona,
                        tipoDocumentos: $scope.view.tipoDocumentos
                    };
                },
                function (newVal, oldVal) {
                    if (newVal.persona !== oldVal.persona && newVal != undefined) {
                        if (newVal.tipoDocumentos !== oldVal.tipoDocumentos && newVal != undefined) {
                            if(!angular.isUndefined($scope.view.persona.tipoDocumento) && !angular.isUndefined($scope.view.tipoDocumentos)){
                                for(var i = 0; i<$scope.view.tipoDocumentos.length;i++){
                                    if($scope.view.persona.tipoDocumento.id==$scope.view.tipoDocumentos[i].id){
                                        $scope.view.persona.tipoDocumento = $scope.view.tipoDocumentos[i];
                                    }
                                }
                            }
                        }
                    }
                },true);

            //logic
            $scope.crearTransaccion = function(){
                if ($scope.formEditarPersonanatural.$valid) {
                    $scope.control.inProcess = true;

                    var personaTransaccion = angular.copy($scope.view.persona);
                    personaTransaccion.tipoDocumento = {id:$scope.view.persona.tipoDocumento.id};
                    personaTransaccion.fechaNacimiento = $scope.view.persona.fechaNacimiento;
                    PersonaNaturalService.update(personaTransaccion).then(
                        function(persona){
                            $scope.redireccion();
                        },
                        function error(error){
                            $scope.control.inProcess = false;
                            $scope.control.success = false;
                            $scope.alerts = [{ type: "danger", msg: "Error: No se pudo cargar la persona."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                } else {
                    $scope.control.submitted = true;
                }
            };

            $scope.redireccion = function(){
                if(RedirectService.haveNextState()){
                    var nextState = RedirectService.getNextState();
                    var paramsState = RedirectService.getParamsState();
                    RedirectService.clearNextState();
                    RedirectService.clearParamsState();
                    $state.transitionTo(nextState, paramsState);
                } else {
                    $state.transitionTo('app.administracion.buscarPersonaNatural');
                }
            };

            $scope.cancel = function () {
                $scope.redireccion();
            };

            $scope.buttonDisableState = function(){
                return $scope.control.inProcess;
            };

        }]);
});