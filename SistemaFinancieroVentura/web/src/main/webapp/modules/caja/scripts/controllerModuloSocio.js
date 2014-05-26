
angular.module('cajaApp.controller')
    .controller('CrearSocioController', [ "$scope","$state","$window", "MaestroService", "PersonaNaturalService", "PersonaJuridicaService", "SocioService",
        function($scope, $state,$window, MaestroService, PersonaNaturalService, PersonaJuridicaService, SocioService) {

            $scope.control = {"success":false, "inProcess": false, "submitted" : false};
            $scope.tipoPersonas = [{"denominacion":"NATURAL"},{"denominacion":"JURIDICA"}];
            $scope.tipoDocumentosSocio = [];
            $scope.tipoDocumentosApoderado = [];

            $scope.transaccion = {
                "tipoPersona": undefined,
                "idTipoDocumentoSocio": undefined,
                "numeroDocumentoSocio": undefined,
                "idTipoDocumentoApoderado": undefined,
                "numeroDocumentoApoderado": undefined
            };

            MaestroService.getTipoDocumentoPN().then(function(data){
                    $scope.tipoDocumentosApoderado = data;
            });

            $scope.tipoPersonaChange = function(){
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
                var tipoDoc = $scope.transaccion.idTipoDocumentoSocio;
                var numDoc = $scope.transaccion.numeroDocumentoSocio;
                if(tipoDoc === null || tipoDoc === undefined){
                    alert("Tipo documento no definido");
                    return;
                }
                if(numDoc === null || numDoc === undefined){
                    alert("Numero documento no definido");
                    return;
                }
                if($scope.transaccion.tipoPersona == "NATURAL"){
                    PersonaNaturalService.findByTipoNumeroDocumento(tipoDoc,numDoc).then(function(persona){
                        $scope.socio = persona;
                    },function error(error){
                        $scope.socio = undefined;
                        $scope.alerts = [{ type: "warning", msg: "Socio no encontrado."}];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                    });
                }else{if($scope.transaccion.tipoPersona == "JURIDICA"){
                    PersonaJuridicaService.findByTipoNumeroDocumento(tipoDoc,numDoc).then(function(persona){
                        $scope.socio = persona;
                    },function error(error){
                        $scope.socio = undefined;
                        $scope.alerts = [{ type: "warning", msg: "Socio no encontrado."}];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                    });
                }}
                $event.preventDefault();
            }
            $scope.buscarPersonaApoderado = function($event){
                var tipoDoc = $scope.transaccion.idTipoDocumentoApoderado;
                var numDoc = $scope.transaccion.numeroDocumentoApoderado;
                if(tipoDoc === null || tipoDoc === undefined){
                    alert("Tipo documento no definido");
                    return;
                }
                if(numDoc === null || numDoc === undefined){
                    alert("Numero documento no definido");
                    return;
                }
                PersonaNaturalService.findByTipoNumeroDocumento(tipoDoc, numDoc).then(function(persona){
                    $scope.apoderado = persona;
                },function error(error){
                    $scope.apoderado = undefined;
                    $scope.alerts = [{ type: "info", msg: "Apoderado no encontrado."}];
                    $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                });
                $event.preventDefault();
            }

            //transacacion principal
            $scope.crearSocio = function(){
                if ($scope.formCrearSocio.$valid) {
                    $scope.control.inProcess = true;
                    var tipoPersona = $scope.transaccion.tipoPersona;
                    var idTipoDocumentoSocio = $scope.transaccion.idTipoDocumentoSocio;
                    var numeroDocumentoSocio = $scope.transaccion.numeroDocumentoSocio;
                    var idTipoDocumentoApoderado = $scope.transaccion.idTipoDocumentoApoderado;
                    var numeroDocumentoApoderado = $scope.transaccion.numeroDocumentoApoderado;

                    SocioService.crear(tipoPersona,
                        idTipoDocumentoSocio,
                        numeroDocumentoSocio,
                        idTipoDocumentoApoderado,
                        numeroDocumentoApoderado).then(
                        function(data){
                            $scope.control.inProcess = false;
                            $scope.control.success = true;
                            alert("socio creado");
                        }, function error(error){
                            $scope.control.inProcess = true;
                            $scope.control.success = false;
                            $scope.alerts = [{ type: "danger", msg: "Error: " + error.data + "."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                } else {
                    $scope.control.submitted = true;
                }
            }

            $scope.crearPersonaSocio = function(){
                if($scope.tipoPersona !== undefined && $scope.tipoPersona !== null){
                    if($scope.tipoPersona.denominacion == "NATURAL"){
                       $window.open("http://localhost:8080/SistemaFinancieroVentura-web/index.caja.html#/app/socio/personaNatural");
                    } else{if($scope.tipoPersona.denominacion == "JURIDICA"){
                        $window.open("http://localhost:8080/SistemaFinancieroVentura-web/index.caja.html#/app/socio/personaJuridica");
                    }}
                } else{
                    alert("Seleccione tipo de persona");
                }
            }
        }])
    ;