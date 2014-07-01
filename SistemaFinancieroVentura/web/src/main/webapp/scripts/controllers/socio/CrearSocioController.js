
define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('CrearSocioController', ['$scope','$state','$window','$timeout','$location','focus', 'MaestroService', 'PersonaNaturalService', 'PersonaJuridicaService', 'SocioService','RedirectService',
        function($scope, $state,$window,$timeout,$location, focus, MaestroService, PersonaNaturalService, PersonaJuridicaService, SocioService,RedirectService) {

            $scope.focusElements = {
                tipoPersona: 'focusTipoPersona'
            };
            $scope.setInitialFocus = function($event){
                if(!angular.isUndefined($event))
                    $event.preventDefault();
                focus($scope.focusElements.tipoPersona);
                $window.scrollTo(0, 0);
            };
            $scope.setInitialFocus();

            $scope.viewState = 'app.socio.crearSocio';

            $scope.control = {
                success: false,
                inProcess: false,
                submitted : false
            };

            $scope.combo = {
                tipoPersonas: MaestroService.getTipoPersonas(),
                tipoDocumentosSocio: [],
                tipoDocumentosApoderado: []
            };

            $scope.view = {
                tipoPersona: undefined,
                idTipoDocumentoSocio: undefined,
                numeroDocumentoSocio: '',
                idTipoDocumentoApoderado: undefined,
                numeroDocumento: ''
            };

            $scope.objetosCargados = {
                socio: undefined,
                apoderado: undefined
            };

            $scope.transaccion = {
                tipoPersona: undefined,
                tipoDocumentoSocio: undefined,
                numeroDocumentoSocio: undefined,
                tipoDocumentoApoderado: undefined,
                numeroDocumentoApoderado: undefined
            };

            $scope.loadTipoDocumentosApoderado = function(){
                MaestroService.getTipoDocumentoPN().then(function(data){
                    $scope.combo.tipoDocumentosApoderado = data;
                });
            };
            $scope.loadTipoDocumentoSocioPN = function(){
                MaestroService.getTipoDocumentoPN().then(function(data){
                    $scope.combo.tipoDocumentosSocio = data;
                });
            };
            $scope.loadTipoDocumentoSocioPJ = function(){
                MaestroService.getTipoDocumentoPJ().then(function(data){
                    $scope.combo.tipoDocumentosSocio = data;
                });
            };

            $scope.loadRedireccion = function(){
                if(RedirectService.haveNext()){
                    var state = RedirectService.getNextState();
                    if(state == $scope.viewState){
                        $scope.view = RedirectService.getNextObject();
                        //var focusElem = RedirectService.getNextFocusElement();
                        RedirectService.clearLast();
                        $timeout(function() {
                            //focusElem.focus();
                        }, 100);
                        $scope.tipoPersonaChange();
                        $scope.buscarPersonaSocio();
                        $scope.buscarPersonaApoderado();
                    }
                }
            };

            $scope.tipoPersonaChange = function(){
                if($scope.view.tipoPersona == "NATURAL"){
                    $scope.loadTipoDocumentoSocioPN();
                }else{if($scope.view.tipoPersona == "JURIDICA"){
                    $scope.loadTipoDocumentoSocioPJ();
                }}
            };

            $scope.buscarPersonaSocio = function($event){
                $scope.control.submitted = true;

                if(angular.isUndefined($scope.view.idTipoDocumentoSocio) || angular.isUndefined($scope.view.numeroDocumentoSocio)){
                    if(!angular.isUndefined($event))
                        $event.preventDefault();
                    return;
                }

                var tipoDoc = $scope.view.idTipoDocumentoSocio;
                var numDoc = $scope.view.numeroDocumentoSocio;
                if($scope.view.tipoPersona == "NATURAL"){
                    PersonaNaturalService.findByTipoNumeroDocumento(tipoDoc,numDoc).then(function(data){
                        $scope.objetosCargados.socio = data;
                        $scope.alerts = [{ type: "success", msg: "Persona(socio) encontrada."}];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);}
                    },function error(error){
                        $scope.objetosCargados.socio = undefined;
                        $scope.alerts = [{ type: "danger", msg: "Persona(socio) no encontrada."}];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                    });
                }else{if($scope.view.tipoPersona == "JURIDICA"){
                    PersonaJuridicaService.findByTipoNumeroDocumento(tipoDoc,numDoc).then(function(persona){
                        $scope.objetosCargados.socio = persona;
                        $scope.alerts = [{ type: "success", msg: "Persona(socio) encontrada."}];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);}
                    },function error(error){
                        $scope.objetosCargados.socio = undefined;
                        $scope.alerts = [{ type: "danger", msg: "Persona(socio) no encontrada."}];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);}
                    });
                }}
                if($event !== undefined)
                    $event.preventDefault();
            };

            $scope.buscarPersonaApoderado = function($event){
                $scope.control.submitted = true;
                if(angular.isUndefined($scope.view.idTipoDocumentoApoderado || angular.isUndefined($scope.view.numeroDocumentoApoderado))){
                    return;
                }
                var tipoDoc = $scope.view.idTipoDocumentoApoderado;
                var numDoc = $scope.view.numeroDocumentoApoderado;
                PersonaNaturalService.findByTipoNumeroDocumento(tipoDoc, numDoc).then(function(data){
                    $scope.objetosCargados.apoderado = data;
                    $scope.alerts = [{ type: "success", msg: "Persona(apoderado) encontrada."}];
                    $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);}
                },function error(error){
                    $scope.objetosCargados.apoderado = undefined;
                    $scope.alerts = [{ type: "danger", msg: "Persona(apoderado) no encontrado."}];
                    $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                });
                if($event !== undefined)
                    $event.preventDefault();
            };

            //transacacion principal
            $scope.crearTransaccion = function(){
                if ($scope.formCrearSocio.$valid) {
                    var tipoPersona = $scope.view.tipoPersona;
                    var idTipoDocumentoSocio = $scope.view.idTipoDocumentoSocio;
                    var numeroDocumentoSocio = $scope.view.numeroDocumentoSocio;
                    var idTipoDocumentoApoderado = $scope.view.idTipoDocumentoApoderado;
                    var numeroDocumentoApoderado = $scope.view.numeroDocumentoApoderado;

                    if(idTipoDocumentoSocio == idTipoDocumentoApoderado && numeroDocumentoSocio == numeroDocumentoApoderado){
                        $scope.alerts = [{ type: "danger", msg: "Error: Socio y apoderado deben de ser diferentes"}];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        return;
                    }

                    $scope.control.inProcess = true;
                    SocioService.crear(tipoPersona,
                        idTipoDocumentoSocio,
                        numeroDocumentoSocio,
                        idTipoDocumentoApoderado,
                        numeroDocumentoApoderado).then(
                        function(data){
                            $scope.control.inProcess = false;
                            $scope.control.success = true;
                            $state.transitionTo("app.socio.panelSocio", { id: data.id });
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

            $scope.crearPersonaSocio = function(){
                if(!angular.isUndefined($scope.view.tipoPersona)){
                    var sendParameters = {
                        tipoDocumento: $scope.view.idTipoDocumentoSocio,
                        numeroDocumento: $scope.view.numeroDocumentoSocio
                    };

                    var nextState = $scope.viewState;
                    var elementFocus = angular.element(document.querySelector('#txtNumeroDocumentoRepresentanteLegal'));
                    RedirectService.addNext(nextState,{},$scope.view, elementFocus);

                    if($scope.view.tipoPersona == 'NATURAL'){
                        $state.transitionTo('app.administracion.crearPersonaNatural', sendParameters);
                    } else if($scope.view.tipoPersona == 'JURIDICA'){
                        $state.transitionTo('app.administracion.crearPersonaJuridica', sendParameters);
                    }
                } else {
                    alert("Seleccione tipo de persona");
                }
            };

            $scope.crearPersonaApoderado = function(){
                var sendParameters = {
                    tipoDocumento: $scope.view.idTipoDocumentoApoderado,
                    numeroDocumento: $scope.view.numeroDocumentoApoderado
                };

                var nextState = $scope.viewState;
                var elementFocus = angular.element(document.querySelector('#txtNumeroDocumentoRepresentanteLegal'));
                RedirectService.addNext(nextState,{},$scope.view, elementFocus);
                $state.transitionTo('app.administracion.crearPersonaNatural', sendParameters);
            };

            $scope.getTipoDocumentoSocio = function(){
                if(!angular.isUndefined($scope.combo.tipoDocumentosSocio)){
                    for(var i = 0; i < $scope.combo.tipoDocumentosSocio.length; i++){
                        if($scope.view.idTipoDocumentoSocio == $scope.combo.tipoDocumentosSocio[i].id)
                            return $scope.combo.tipoDocumentosSocio[i];
                    }
                }
                return undefined;
            };
            $scope.getTipoDocumentoApoderado = function(){
                if(!angular.isUndefined($scope.combo.tipoDocumentosApoderado)){
                    for(var i = 0; i < $scope.combo.tipoDocumentosApoderado.length; i++){
                        if($scope.view.idTipoDocumentoApoderado == $scope.combo.tipoDocumentosApoderado[i].id)
                            return $scope.combo.tipoDocumentosApoderado[i];
                    }
                }
                return undefined;
            };
            $scope.$watch("view.numeroDocumentoSocio",function (newVal, oldVal) {
                if (newVal !== oldVal) {
                    $scope.validarNumeroDocumentoSocio();
                }
            },true);
            $scope.$watch("view.numeroDocumentoApoderado",function (newVal, oldVal) {
                if (newVal !== oldVal) {
                    $scope.validarNumeroDocumentoApoderado();
                }
            },true);
            $scope.validarNumeroDocumentoSocio = function(){
                if(!angular.isUndefined($scope.formCrearSocio.numeroDocumentoSocio)){
                    if(!angular.isUndefined($scope.view.numeroDocumentoSocio)){
                        if(!angular.isUndefined($scope.view.idTipoDocumentoSocio)){
                            var tipoDoc = $scope.getTipoDocumentoSocio();
                            if(!angular.isUndefined(tipoDoc)) {
                                if($scope.view.numeroDocumentoSocio.length == tipoDoc.numeroCaracteres) {
                                    $scope.formCrearSocio.numeroDocumentoSocio.$setValidity("sgmaxlength",true);
                                } else {$scope.formCrearSocio.numeroDocumentoSocio.$setValidity("sgmaxlength",false);}
                            } else {$scope.formCrearSocio.numeroDocumentoSocio.$setValidity("sgmaxlength",false);}
                        } else{$scope.formCrearSocio.numeroDocumentoSocio.$setValidity("sgmaxlength",false);}
                    } else {$scope.formCrearSocio.numeroDocumentoSocio.$setValidity("sgmaxlength",false);}}
            };
            $scope.validarNumeroDocumentoApoderado = function(){
                if(!angular.isUndefined($scope.formCrearSocio.numeroDocumentoApoderado)){
                    if(!angular.isUndefined($scope.view.numeroDocumentoApoderado)){
                        if(!angular.isUndefined($scope.view.idTipoDocumentoApoderado)){
                            var tipoDoc = $scope.getTipoDocumentoApoderado();
                            if(!angular.isUndefined(tipoDoc)) {
                                if($scope.view.numeroDocumentoApoderado.length == tipoDoc.numeroCaracteres) {
                                    $scope.formCrearSocio.numeroDocumentoApoderado.$setValidity("sgmaxlength",true);
                                } else {$scope.formCrearSocio.numeroDocumentoApoderado.$setValidity("sgmaxlength",false);}
                            } else {$scope.formCrearSocio.numeroDocumentoApoderado.$setValidity("sgmaxlength",false);}
                        } else{$scope.formCrearSocio.numeroDocumentoApoderado.$setValidity("sgmaxlength",false);}
                    } else {$scope.formCrearSocio.numeroDocumentoApoderado.$setValidity("sgmaxlength",false);}}
            };

            $scope.redireccion = function(){
                if(RedirectService.haveNext()){
                    var nextState = RedirectService.getNextState();
                    var parametros = RedirectService.getNextParamsState();
                    $state.transitionTo(nextState,parametros);
                } else {
                    $state.transitionTo('app.socio.buscarSocio');
                }
            };

            $scope.cancelar = function(){
                $scope.redireccion();
            };

            $scope.buttonDisableState = function(){
                return $scope.control.inProcess;
            };

            $scope.loadTipoDocumentosApoderado();
            $scope.loadRedireccion();
        }]);
});