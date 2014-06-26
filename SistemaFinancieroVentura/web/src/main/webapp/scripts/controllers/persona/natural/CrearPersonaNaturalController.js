define(['../../module'], function (controllers) {
    'use strict';
    controllers.controller('CrearPersonaNaturalController', ['$scope','$state','$stateParams','$window','MaestroService','PersonaNaturalService','RedirectService',
        function($scope,$state,$stateParams,$window,MaestroService,PersonaNaturalService,RedirectService) {

            $scope.limpiarRedirectService = function(){
              if($stateParams.redirect){
                  RedirectService.clearAll();
              }
            };
            $scope.limpiarRedirectService();

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
                if(!angular.isUndefined($scope.formCrearPersonanatural.numeroDocumento)){
                    if(!angular.isUndefined($scope.view.persona.numeroDocumento)){
                        if(!angular.isUndefined($scope.view.persona.tipoDocumento)){
                            if($scope.view.persona.numeroDocumento.length == $scope.view.persona.tipoDocumento.numeroCaracteres) {
                                $scope.formCrearPersonanatural.numeroDocumento.$setValidity("sgmaxlength",true);
                            } else {$scope.formCrearPersonanatural.numeroDocumento.$setValidity("sgmaxlength",false);}
                        } else{$scope.formCrearPersonanatural.numeroDocumento.$setValidity("sgmaxlength",false);}
                    } else {$scope.formCrearPersonanatural.numeroDocumento.$setValidity("sgmaxlength",false);}}
            };

            $scope.loadParametros = function(){
                if(!angular.isUndefined($scope.view.persona)){
                    $scope.view.persona.numeroDocumento = $scope.params.numeroDocumento;
                    if(!angular.isUndefined($scope.params.idTipoDocumento)){
                        for(var i = 0; i < $scope.view.tipoDocumentos.length; i++){
                            if($scope.view.tipoDocumentos[i].id == $scope.params.idTipoDocumento)
                                $scope.view.persona.tipoDocumento = $scope.view.tipoDocumentos[i];
                        }
                    }
                }
            };

            $scope.loadTipoDocumentoPN = function(){
                MaestroService.getTipoDocumentoPN().then(function(data){
                    $scope.view.tipoDocumentos = data;
                    $scope.loadParametros();
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

            //logic
            $scope.crearTransaccion = function(){
                if ($scope.formCrearPersonanatural.$valid) {
                    var personaTransaccion = angular.copy($scope.view.persona);
                    personaTransaccion.tipoDocumento = {
                        id: personaTransaccion.tipoDocumento.id
                    };
                    personaTransaccion.fechaNacimiento = $scope.view.persona.fechaNacimiento.getTime();

                    $scope.buttonDisableState = true;
                    PersonaNaturalService.crear(personaTransaccion).then(
                        function(persona){
                            $scope.redireccion();
                        },
                        function error(error){
                            $scope.control.inProcess = false;
                            $scope.control.success = false;
                            $scope.alerts = [{ type: "danger", msg: "Error: " + error.data.message + "."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                            $window.scrollTo(0,0);
                        }
                    );
                    $scope.buttonDisableState = false;
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