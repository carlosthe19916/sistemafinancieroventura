define(['../../module'], function (controllers) {
    'use strict';
    controllers.controller('EditarPersonaJuridicaController', ["$scope", "$state","$location", "$window","$timeout","focus","MaestroService", "PersonaNaturalService", "PersonaJuridicaService","RedirectService",
        function($scope, $state,$location, $window,$timeout,focus,MaestroService, PersonaNaturalService, PersonaJuridicaService,RedirectService) {

            $scope.viewState = "app.administracion.editarPersonaJuridica";

            $scope.focusElements = {
                tipoDocumento: 'focusTipoDocumento',
                representanteLegal: 'focusNumeroDocumentoRepresentante',
                tipoDocumentoAccionista: 'focusTipoDocumentoAccionista',
                numeroDocumentoAccionista: 'focusNumeroDocumentoAccionista'
            };
            $scope.setInitialFocus = function($event){
                if(!angular.isUndefined($event))
                    $event.preventDefault();
                focus($scope.focusElements.tipoDocumento);
                $window.scrollTo(0, 0);
            };
            $scope.setInitialFocus();

            $scope.control = {
                success:false,
                inProcess: false,
                submitted : false
            };

            $scope.combo = {
                tipoDocumentosPJ: undefined,
                tiposEmpresa: undefined,
                tipoDocumentoPN: undefined
            };

            $scope.view = {
                "id":undefined,
                "idTipoDocumentoPJ": undefined,
                "numeroDocumentoPJ":undefined,
                "razonSocial":undefined,
                "nombreComercial":undefined,
                "fechaConstitucion":undefined,
                "actividadPrincipal":undefined,
                "tipoEmpresa":undefined,
                "finLucro":undefined,
                "direccion":undefined,
                "referencia":undefined,
                "telefono":undefined,
                "celular":undefined,
                "email":undefined,
                "ubigeo":undefined,

                "idTipoDocumentoPN": undefined,
                "numeroDocumentoPN": undefined,
                "accionistas": [],

                idTipoDocumentoAccionista: undefined,
                numeroDocumentoAccionista: undefined,

                tabSelectedPersonaJuridica: true,
                tabSelectedAccionista: false
            };

            $scope.objetosCargados = {
                representanteLegal: undefined
            };

            $scope.dateOptions = {formatYear: 'yyyy',startingDay: 1};
            $scope.open = function($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.opened = true;
            };

            $scope.loadPersonaJuridica = function(){
                if(!angular.isUndefined($scope.id)){
                    PersonaJuridicaService.findById($scope.id).then(
                        function(data){
                            $scope.view.id = data.id;
                            $scope.view.idTipoDocumentoPJ = data.tipoDocumento.id;
                            $scope.view.numeroDocumentoPJ = data.numeroDocumento;
                            $scope.view.razonSocial = data.razonSocial;
                            $scope.view.nombreComercial = data.nombreComercial;
                            $scope.view.fechaConstitucion = data.fechaConstitucion;
                            $scope.view.actividadPrincipal = data.actividadPrincipal;
                            $scope.view.tipoEmpresa = data.tipoEmpresa;
                            $scope.view.finLucro = data.finLucro;
                            $scope.view.direccion = data.direccion;
                            $scope.view.referencia = data.referencia;
                            $scope.view.telefono = data.telefono;
                            $scope.view.celular = data.celular;
                            $scope.view.email = data.email;
                            $scope.view.ubigeo = data.ubigeo;
                            $scope.view.idTipoDocumentoPN = data.representanteLegal.tipoDocumento.id;
                            $scope.view.numeroDocumentoPN = data.representanteLegal.numeroDocumento;
                            $scope.view.accionistas = data.accionistas;

                            $scope.objetosCargados.representanteLegal = data.representanteLegal;
                        }, function error(error){
                            $scope.alerts = [{ type: "danger", msg: "Error: No se pudo cargar la persona."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                }
            };

            $scope.loadTipoDocumentoPJ = function(){
                MaestroService.getTipoDocumentoPJ().then(function(data){
                    $scope.combo.tipoDocumentosPJ = data;
                });
            };
            $scope.loadTipoDocumentoPN = function(){
                MaestroService.getTipoDocumentoPN().then(function(tipodocumentos){
                    $scope.combo.tipoDocumentosPN = tipodocumentos;
                });
            };
            $scope.loadTipoEmpresas = function(){
                MaestroService.getTiposEmpresa().then(function(tiposEmpresa){
                    $scope.combo.tiposEmpresa = tiposEmpresa;
                });
            };

            $scope.loadRedireccion = function(){
                if(RedirectService.haveNext()){
                    var state = RedirectService.getNextState();
                    if(state == $scope.viewState){
                        $scope.view = RedirectService.getNextObject();
                        var focusElem = RedirectService.getNextFocusElement();
                        var windowsPosition = RedirectService.getNextWindowsPosition();
                        RedirectService.clearLast();
                        $timeout(function() {
                            focus(focusElem);
                        }, 500);
                        if(!angular.isUndefined(windowsPosition))
                            $window.scrollTo(windowsPosition.x, windowsPosition.y);
                        $scope.buscarRepresentanteLegal();
                    }
                }
            };

            $scope.buscarRepresentanteLegal = function($event){
                if(!angular.isUndefined($scope.view.idTipoDocumentoPN) &&
                    !angular.isUndefined($scope.view.numeroDocumentoPN)){

                    var tipoDoc = $scope.view.idTipoDocumentoPN;
                    var numDoc = $scope.view.numeroDocumentoPN;
                    PersonaNaturalService.findByTipoNumeroDocumento(tipoDoc,numDoc).then(function(data){
                        $scope.objetosCargados.representanteLegal = data;
                    },function error(error){
                        $scope.alerts = [{ type: "danger", msg: "Representante legal no encontrado."}];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        $window.scrollTo(0,0);
                    });
                    if(!angular.isUndefined($event)) $event.preventDefault();
                }
            };
            $scope.nuevaPersonaRepresentanteLegal = function(){
                $scope.setTabPersonaJuridicaActive();
                var savedParameters = {
                    id: $scope.view.id
                };
                var sendParameters = {
                    tipoDocumento: $scope.view.idTipoDocumentoPN,
                    numeroDocumento: $scope.view.numeroDocumentoPN
                };
                var nextState = $scope.viewState;
                var elementFocus = $scope.focusElements.representanteLegal;
                var windowsPosition = {x: $window.screenX, y: $window.screenY};
                RedirectService.addNext(nextState, savedParameters, $scope.view, elementFocus, windowsPosition);

                $scope.view.tabSelectedPersonaJuridica = true;
                $scope.view.tabSelectedAccionista = false;

                $state.transitionTo('app.administracion.crearPersonaNatural', sendParameters);
            };

            $scope.getTipoDocumentoPJ = function(){
                if(!angular.isUndefined($scope.combo.tipoDocumentosPJ)){
                    for(var i = 0; i < $scope.combo.tipoDocumentosPJ.length; i++){
                        if($scope.view.idTipoDocumentoPJ == $scope.combo.tipoDocumentosPJ[i].id)
                            return $scope.combo.tipoDocumentosPJ[i];
                    }
                }
                return undefined;
            };
            $scope.getTipoDocumentoPN = function(){
                if(!angular.isUndefined($scope.combo.tipoDocumentosPN)){
                    for(var i = 0; i < $scope.combo.tipoDocumentosPN.length; i++){
                        if($scope.view.idTipoDocumentoPN == $scope.combo.tipoDocumentosPN[i].id)
                            return $scope.combo.tipoDocumentosPN[i];
                    }
                }
                return undefined;
            };
            $scope.validarNumeroDocumentoPJ = function(){
                if(!angular.isUndefined($scope.formEditarPersonaJuridica.numeroDocumentoPJ)){
                    if(!angular.isUndefined($scope.view.numeroDocumentoPJ)){
                        if(!angular.isUndefined($scope.view.idTipoDocumentoPJ)){
                            var tipoDoc = $scope.getTipoDocumentoPJ();
                            if(!angular.isUndefined(tipoDoc)) {
                                if($scope.view.numeroDocumentoPJ.length == tipoDoc.numeroCaracteres) {
                                    $scope.formEditarPersonaJuridica.numeroDocumentoPJ.$setValidity("sgmaxlength",true);
                                } else {$scope.formEditarPersonaJuridica.numeroDocumentoPJ.$setValidity("sgmaxlength",false);}
                            } else {$scope.formEditarPersonaJuridica.numeroDocumentoPJ.$setValidity("sgmaxlength",false);}
                        } else{$scope.formEditarPersonaJuridica.numeroDocumentoPJ.$setValidity("sgmaxlength",false);}
                    } else {$scope.formEditarPersonaJuridica.numeroDocumentoPJ.$setValidity("sgmaxlength",false);}}
            };
            $scope.validarNumeroDocumentoPN = function(){
                if(!angular.isUndefined($scope.formEditarPersonaJuridica.numeroDocumentoRepresentante)){
                    if(!angular.isUndefined($scope.view.numeroDocumentoPN)){
                        if(!angular.isUndefined($scope.view.idTipoDocumentoPN)){
                            var tipoDoc = $scope.getTipoDocumentoPN();
                            if(!angular.isUndefined(tipoDoc)) {
                                if($scope.view.numeroDocumentoPN.length == tipoDoc.numeroCaracteres) {
                                    $scope.formEditarPersonaJuridica.numeroDocumentoRepresentante.$setValidity("sgmaxlength",true);
                                } else {$scope.formEditarPersonaJuridica.numeroDocumentoRepresentante.$setValidity("sgmaxlength",false);}
                            } else {$scope.formEditarPersonaJuridica.numeroDocumentoRepresentante.$setValidity("sgmaxlength",false);}
                        } else{$scope.formEditarPersonaJuridica.numeroDocumentoRepresentante.$setValidity("sgmaxlength",false);}
                    } else {$scope.formEditarPersonaJuridica.numeroDocumentoRepresentante.$setValidity("sgmaxlength",false);}}
            };
            $scope.$watch("view.numeroDocumentoPJ",function (newVal, oldVal) {
                if (newVal !== oldVal) {
                    $scope.validarNumeroDocumentoPJ();
                }
            },true);
            $scope.$watch("view.numeroDocumentoPN",function (newVal, oldVal) {
                if (newVal !== oldVal) {
                    $scope.validarNumeroDocumentoPN();
                }
            },true);

            $scope.formEditarPersonaJuridica = {};
            $scope.crearTransaccion = function(){
                if ($scope.formEditarPersonaJuridica.$valid) {
                    if(angular.isUndefined($scope.objetosCargados.representanteLegal.id)){
                        $scope.alerts = [{ type: 'danger', msg: "Representante no cargado." }];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        $window.scrollTo(0,0);
                        return;
                    }

                    //copiando datos
                    var personaJurica = {};
                    personaJurica.id = $scope.view.id;
                    personaJurica.tipoDocumento = {
                        id: $scope.view.idTipoDocumentoPJ
                    };
                    personaJurica.numeroDocumento = $scope.view.numeroDocumentoPJ;
                    personaJurica.razonSocial = $scope.view.razonSocial;
                    personaJurica.nombreComercial = $scope.view.nombreComercial;
                    personaJurica.fechaConstitucion = $scope.view.fechaConstitucion;
                    personaJurica.actividadPrincipal = $scope.view.actividadPrincipal;
                    personaJurica.direccion = $scope.view.direccion;
                    personaJurica.referencia = $scope.view.referencia;
                    personaJurica.telefono = $scope.view.telefono;
                    personaJurica.celular = $scope.view.celular;
                    personaJurica.email = $scope.view.email;
                    personaJurica.tipoEmpresa = $scope.view.tipoEmpresa;
                    personaJurica.finLucro = $scope.view.finLucro;
                    personaJurica.ubigeo = $scope.view.ubigeo;

                    personaJurica.idRepresentanteLegal = $scope.objetosCargados.representanteLegal.id;
                    personaJurica.accionistas = [];
                    for(var i = 0; i < $scope.view.accionistas.length; i++){
                        personaJurica.accionistas[i] = {
                            idPersona: $scope.view.accionistas[i].personaNatural.id ,
                            porcentaje: $scope.view.accionistas[i].porcentajeParticipacion
                        };
                    }

                    $scope.control.inProcess = true;
                    PersonaJuridicaService.update(personaJurica).then(
                        function(persona){
                            $scope.control.inProcess = false;
                            $scope.control.success = true;
                            $scope.redireccion();
                        },
                        function error(error){
                            $scope.control.inProcess = false;
                            $scope.control.success = false;
                            $scope.alerts = [{ type: 'danger', msg: "Error: " + error.data.message + "." }];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                            $window.scrollTo(0,0);
                        }
                    );
                } else {
                    $scope.control.submitted = true;
                }
            };

            $scope.redireccion = function(){
                if(RedirectService.haveNext()){
                    var nextState = RedirectService.getNextState();
                    var parametros = RedirectService.getNextParamsState();
                    $state.transitionTo(nextState,parametros);
                } else {
                    $state.transitionTo('app.administracion.buscarPersonaJuridica');
                }
            };
            $scope.cancelar = function () {
                $scope.redireccion();
            };
            $scope.buttonDisableState = function(){
                return $scope.control.inProcess;
            };

            //tabs
            $scope.tabPersonaJuridicaSelected = function(){
                focus($scope.focusElements.tipoDocumento);
            };
            $scope.tabAccionistaSelected = function(){
                focus($scope.focusElements.tipoDocumentoAccionista);
            };
            $scope.setTabPersonaJuridicaActive = function(){
                $scope.view.tabSelectedPersonaJuridica = true;
                $scope.view.tabSelectedAccionista = false;
            };
            $scope.setTabAcctionistasActive = function(){
                $scope.view.tabSelectedPersonaJuridica = false;
                $scope.view.tabSelectedAccionista = true;
            };

            //llamar a los metodos
            $scope.loadPersonaJuridica();

            $scope.loadTipoDocumentoPJ();
            $scope.loadTipoDocumentoPN();
            $scope.loadTipoEmpresas();
            $scope.loadRedireccion();
        }]);
});