
define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('CrearSocioController', [ "$scope","$state","$window","focus", "MaestroService", "PersonaNaturalService", "PersonaJuridicaService", "SocioService",
        function($scope, $state, $window, focus, MaestroService, PersonaNaturalService, PersonaJuridicaService, SocioService) {

            focus("firstFocus");

            $scope.control = {"success":false, "inProcess": false, "submitted" : false};
            $scope.tipoPersonas = [{"denominacion":"NATURAL"},{"denominacion":"JURIDICA"}];
            $scope.tipoDocumentosSocio = [];
            $scope.tipoDocumentosApoderado = [];

            $scope.transaccion = {
                "tipoPersona": undefined,
                "tipoDocumentoSocio": undefined,
                "numeroDocumentoSocio": "",
                "tipoDocumentoApoderado": undefined,
                "numeroDocumentoApoderado": ""
            };

            $scope.errorNumeroDocumento = {"socio":false,"apoderado":false}
            $scope.$watch("transaccion.numeroDocumentoSocio", function(){
                if($scope.transaccion.tipoDocumentoSocio !== undefined && $scope.transaccion.tipoDocumentoSocio !== null){
                    if($scope.transaccion.numeroDocumentoSocio === undefined || $scope.transaccion.numeroDocumentoSocio === null){
                        $scope.errorNumeroDocumento.socio = true;
                    } else if($scope.transaccion.tipoDocumentoSocio.numeroCaracteres != $scope.transaccion.numeroDocumentoSocio.length){
                        $scope.errorNumeroDocumento.socio = true;
                    } else {
                        $scope.errorNumeroDocumento.socio = false;
                    }
                }
                if($scope.transaccion.numeroDocumentoSocio === undefined || $scope.transaccion.numeroDocumentoSocio === null)
                    $scope.errorNumeroDocumento.socio = true;
            });
            $scope.$watch("transaccion.numeroDocumentoApoderado", function(){
                if($scope.transaccion.tipoDocumentoApoderado !== undefined && $scope.transaccion.tipoDocumentoApoderado !== null){
                    if($scope.transaccion.numeroDocumentoApoderado === undefined || $scope.transaccion.numeroDocumentoApoderado === null){
                        $scope.errorNumeroDocumento.apoderado = true;
                    } else if($scope.transaccion.tipoDocumentoApoderado.numeroCaracteres != $scope.transaccion.tipoDocumentoApoderado.length){
                        $scope.errorNumeroDocumento.apoderado = true;
                    } else {
                        $scope.errorNumeroDocumento.apoderado = false;
                    }
                }
                if($scope.transaccion.numeroDocumentoApoderado === undefined || $scope.transaccion.numeroDocumentoApoderado === null)
                    $scope.errorNumeroDocumento.socio = true;
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
                var tipoDoc = $scope.transaccion.tipoDocumentoSocio.id;
                var numDoc = $scope.transaccion.numeroDocumentoSocio;
                if(tipoDoc === null || tipoDoc === undefined){
                    return;
                }
                if($scope.errorNumeroDocumento.socio == true){
                    return;
                }

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
                var tipoDoc = $scope.transaccion.tipoDocumentoApoderado.id;
                var numDoc = $scope.transaccion.numeroDocumentoApoderado;
                if(tipoDoc === null || tipoDoc === undefined){
                    alert("Tipo documento no definido");
                    return;
                }
                if(numDoc === null || numDoc === undefined){
                    alert("Numero documento no definido");
                    return;
                }

                $scope.control.submitted = true;
                if($scope.errorNumeroDocumento.apoderado == true){
                    return;
                }

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
            $scope.crearSocio = function(){
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
                    if($scope.errorNumeroDocumento.socio == true || $scope.errorNumeroDocumento.apoderado == true){
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
                        $window.open("http://localhost:8080/SistemaFinancieroVentura-web/index.caja.html#/app/socio/personaNatural");
                    } else{if($scope.transaccion.tipoPersona == "JURIDICA"){
                        $window.open("http://localhost:8080/SistemaFinancieroVentura-web/index.caja.html#/app/socio/personaJuridica");
                    }}
                } else{
                    alert("Seleccione tipo de persona");
                }
            }

            $scope.crearPersonaApoderado = function(){
                $window.open("http://localhost:8080/SistemaFinancieroVentura-web/index.caja.html#/app/socio/personaNatural");
            }

            $scope.buttonDisableState = function(){
                return $scope.control.inProcess;
            }
        }]);
});