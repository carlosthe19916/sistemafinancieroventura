
define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('CrearSocioController', [ "$scope","$state","$window","$location", "focus", "MaestroService", "PersonaNaturalService", "PersonaJuridicaService", "SocioService",
        function($scope, $state, $window,$location, focus, MaestroService, PersonaNaturalService, PersonaJuridicaService, SocioService) {

            focus("firstFocus");

            $scope.control = {
                "success":false,
                "inProcess": false,
                "submitted" : false,
                "errorForm" : {"socio" : false, "apoderado" : false}
            };

            $scope.tipoPersonas = MaestroService.getTipoPersonas();
            $scope.tipoDocumentosSocio = [];
            $scope.tipoDocumentosApoderado = [];

            $scope.transaccion = {
                "tipoPersona": undefined,
                "tipoDocumentoSocio": undefined,
                "numeroDocumentoSocio": "",
                "tipoDocumentoApoderado": undefined,
                "numeroDocumentoApoderado": ""
            };

            $scope.$watch("transaccion.numeroDocumentoSocio", function(){
                if($scope.transaccion.tipoDocumentoSocio !== undefined && $scope.transaccion.tipoDocumentoSocio !== null){
                    if($scope.transaccion.numeroDocumentoSocio === undefined || $scope.transaccion.numeroDocumentoSocio === null){
                        $scope.control.errorForm.socio = true;
                    } else if($scope.transaccion.tipoDocumentoSocio.numeroCaracteres != $scope.transaccion.numeroDocumentoSocio.length){
                        $scope.control.errorForm.socio = true;
                    } else {
                        $scope.control.errorForm.socio = false;
                    }
                }
                if($scope.transaccion.numeroDocumentoSocio === undefined || $scope.transaccion.numeroDocumentoSocio === null || $scope.transaccion.numeroDocumentoSocio === "")
                    $scope.control.errorForm.socio = true;
            });
            $scope.$watch("transaccion.numeroDocumentoApoderado", function(){
                if($scope.transaccion.tipoDocumentoApoderado !== undefined && $scope.transaccion.tipoDocumentoApoderado !== null){
                    if($scope.transaccion.numeroDocumentoApoderado === undefined || $scope.transaccion.numeroDocumentoApoderado === null){
                        $scope.control.errorForm.apoderado = true;
                    } else if($scope.transaccion.tipoDocumentoApoderado.numeroCaracteres != $scope.transaccion.numeroDocumentoApoderado.length){
                        $scope.control.errorForm.apoderado = true;
                    } else {
                        $scope.control.errorForm.apoderado = false;
                    }
                } else {
                    $scope.control.errorForm.apoderado = false;
                }
            });

            MaestroService.getTipoDocumentoPN().then(function(data){
                $scope.tipoDocumentosApoderado = data;
            });
            $scope.tipoPersonaChange = function(){
                $scope.transaccion.numeroDocumentoSocio = "";
                if($scope.transaccion.tipoPersona == "NATURAL"){
                    MaestroService.getTipoDocumentoPN().then(function(data){
                        $scope.tipoDocumentosSocio = data;
                    });
                }else{if($scope.transaccion.tipoPersona == "JURIDICA"){
                    MaestroService.getTipoDocumentoPJ().then(function(data){
                        $scope.tipoDocumentosSocio = data;
                    });
                }}
            }

            $scope.buscarPersonaSocio = function($event){
                $scope.control.submitted = true;

                if(angular.isUndefined($scope.transaccion.tipoDocumentoSocio || $scope.control.errorForm.socio == true)){
                    if($event !== undefined)
                        $event.preventDefault();
                    return;
                }

                var tipoDoc = $scope.transaccion.tipoDocumentoSocio.id;
                var numDoc = $scope.transaccion.numeroDocumentoSocio;
                if($scope.transaccion.tipoPersona == "NATURAL"){
                    PersonaNaturalService.findByTipoNumeroDocumento(tipoDoc,numDoc).then(function(persona){
                        $scope.socio = persona;
                        $scope.alerts = [{ type: "success", msg: "Persona(socio) encontrada."}];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);}
                    },function error(error){
                        $scope.socio = undefined;
                        $scope.alerts = [{ type: "danger", msg: "Persona(socio) no encontrada."}];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                    });
                }else{if($scope.transaccion.tipoPersona == "JURIDICA"){
                    PersonaJuridicaService.findByTipoNumeroDocumento(tipoDoc,numDoc).then(function(persona){
                        $scope.socio = persona;
                        $scope.alerts = [{ type: "success", msg: "Persona(socio) encontrada."}];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);}
                    },function error(error){
                        $scope.socio = undefined;
                        $scope.alerts = [{ type: "danger", msg: "Persona(socio) no encontrada."}];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);}
                    });
                }}
                if($event !== undefined)
                    $event.preventDefault();
            }
            $scope.buscarPersonaApoderado = function($event){
                $scope.control.submitted = true;
                if(angular.isUndefined($scope.transaccion.tipoDocumentoApoderado
                    || angular.isUndefined($scope.transaccion.numeroDocumentoApoderado)
                    || $scope.transaccion.numeroDocumentoApoderado === ""
                    || $scope.control.errorForm.apoderado == true)){
                    return;
                }
                var tipoDoc = $scope.transaccion.tipoDocumentoApoderado.id;
                var numDoc = $scope.transaccion.numeroDocumentoApoderado;

                PersonaNaturalService.findByTipoNumeroDocumento(tipoDoc, numDoc).then(function(persona){
                    $scope.apoderado = persona;
                    $scope.alerts = [{ type: "success", msg: "Persona(apoderado) encontrada."}];
                    $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);}
                },function error(error){
                    $scope.apoderado = undefined;
                    $scope.alerts = [{ type: "danger", msg: "Persona(apoderado) no encontrado."}];
                    $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                });
                if($event !== undefined)
                    $event.preventDefault();
            }

            //transacacion principal
            $scope.crearTransaccion = function(){
                if ($scope.formCrearSocio.$valid) {
                    var tipoPersona = $scope.transaccion.tipoPersona;
                    var idTipoDocumentoSocio = $scope.transaccion.tipoDocumentoSocio.id;
                    var numeroDocumentoSocio = $scope.transaccion.numeroDocumentoSocio;
                    var idTipoDocumentoApoderado;
                    var numeroDocumentoApoderado = $scope.transaccion.numeroDocumentoApoderado;
                    if($scope.transaccion.tipoDocumentoApoderado === undefined)
                        idTipoDocumentoApoderado = undefined;
                    else
                        idTipoDocumentoApoderado = $scope.transaccion.tipoDocumentoApoderado.id;

                    if(idTipoDocumentoSocio == idTipoDocumentoApoderado && numeroDocumentoSocio == numeroDocumentoApoderado){
                        $scope.alerts = [{ type: "danger", msg: "Error: Socio y apoderado deben de ser diferentes"}];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        return;
                    }
                    if($scope.control.errorForm.socio == true || $scope.control.errorForm.apoderado == true){
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
            }

            $scope.crearPersonaSocio = function(){
                if($scope.transaccion.tipoPersona !== undefined && $scope.transaccion.tipoPersona !== null){
                    if($scope.transaccion.tipoPersona == "NATURAL"){
                        var idTipoDoc = undefined;
                        if($scope.transaccion.tipoDocumentoSocio !== undefined && $scope.transaccion.tipoDocumentoSocio !== null)
                            idTipoDoc = $scope.transaccion.tipoDocumentoSocio.id;
                        var baseLen = $location.absUrl().length - $location.url().length;
                        var url = $location.absUrl().substring(0, baseLen);
                        $window.open(url + "/app/socio/personaNatural" + "?tipoDocumento=" + idTipoDoc + "&numeroDocumento=" + $scope.transaccion.numeroDocumentoSocio);
                    } else{if($scope.transaccion.tipoPersona == "JURIDICA"){
                        if($scope.transaccion.tipoDocumentoSocio !== undefined && $scope.transaccion.tipoDocumentoSocio !== null)
                            idTipoDoc = $scope.transaccion.tipoDocumentoSocio.id;
                        var baseLen = $location.absUrl().length - $location.url().length;
                        var url = $location.absUrl().substring(0, baseLen);
                        $window.open(url + "/app/socio/personaJuridica" + "?tipoDocumento=" + idTipoDoc + "&numeroDocumento=" + $scope.transaccion.numeroDocumentoSocio);
                    }}
                } else{
                    alert("Seleccione tipo de persona");
                }
            }

            $scope.crearPersonaApoderado = function(){
                var idTipoDoc = undefined;
                if($scope.transaccion.tipoDocumentoApoderado !== undefined && $scope.transaccion.tipoDocumentoApoderado !== null)
                    idTipoDoc = $scope.transaccion.tipoDocumentoApoderado.id;
                var baseLen = $location.absUrl().length - $location.url().length;
                var url = $location.absUrl().substring(0, baseLen);
                $window.open(url + "/app/socio/personaNatural" + "?tipoDocumento=" + idTipoDoc + "&numeroDocumento=" + $scope.transaccion.numeroDocumentoApoderado);
            }

            $scope.buttonDisableState = function(){
                return $scope.control.inProcess;
            }

            $scope.cancelar = function(){
                $state.transitionTo("app.socio.buscarSocio");
            }
        }]);
});