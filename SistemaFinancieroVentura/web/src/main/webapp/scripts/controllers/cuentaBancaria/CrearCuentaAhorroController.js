define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('CrearCuentaAhorroController', [ "$scope", "$state", "$filter", "$window", "focus", "MaestroService", "MonedaService", "PersonaNaturalService", "PersonaJuridicaService", "SocioService", "TasaInteresService", "CuentaBancariaService",
        function($scope, $state, $filter, $window, focus, MaestroService, MonedaService, PersonaNaturalService, PersonaJuridicaService, SocioService, TasaInteresService, CuentaBancariaService) {

            $scope.$on('$includeContentLoaded', function(){
                focus("firstFocus");
            });


            $scope.control = {"success":false, "inProcess": false, "submitted" : false};
            $scope.tipoPersonas = [{"denominacion":"NATURAL"},{"denominacion":"JURIDICA"}];

            $scope.tipoDocumentos = [];
            $scope.titulares = [];
            $scope.titularesFinal = [];
            $scope.beneficiarios = [];

            $scope.$watchCollection('titulares', function() {
                $scope.titularesFinal = angular.copy($scope.titulares);
                if($scope.socioNatural !== undefined)
                    $scope.titularesFinal.push($scope.socioNatural);
                if($scope.socioJuridico !== undefined)
                    $scope.titularesFinal.push($scope.socioJuridico.representanteLegal);
            });
            $scope.$watch('socioNatural', function() {
                $scope.titularesFinal = angular.copy($scope.titulares);
                if($scope.socioNatural !== undefined){
                    $scope.titularesFinal.push($scope.socioNatural);
                    $scope.transaccion.idPersona = $scope.socioNatural.id;
                }
            });
            $scope.$watch('socioJuridico', function() {
                $scope.titularesFinal = angular.copy($scope.titulares);
                if($scope.socioJuridico !== undefined){
                    $scope.titularesFinal.push($scope.socioJuridico.representanteLegal);
                    $scope.transaccion.idPersona = $scope.socioJuridico.id;
                }
            });

            $scope.transaccion = {
                "idMoneda": undefined,
                "tasaInteres": undefined,
                "tipoPersona": undefined,
                "idTipoDocumento" : undefined,
                "numeroDocumento" : undefined,
                "idPersona" : undefined,
                "cantRetirantes": 1,
                "titulares" : {},
                "beneficiarios" : {}
            };

            $scope.tabCuentaSelected = function(){
                angular.element("#cmbTipoPersona").focus();
            }
            $scope.tabTitularSelected = function(){
                angular.element("#cmbTipoDocumentoTitular").focus();
            }
            $scope.tabBeneficiarioSelected = function(){
                angular.element("#txtNumeroDocumentoBeneficiario").focus();
            }

            MonedaService.getMonedas().then(function(monedas){
                $scope.monedas = monedas;
            });

            $scope.tipoPersonaChange = function(){
                $scope.socioNatural = undefined;
                $scope.socioJuridico = undefined;
                if($scope.transaccion.tipoPersona == "NATURAL"){
                    MaestroService.getTipoDocumentoPN().then(function(data){
                        $scope.tipoDocumentos = data;
                        $scope.transaccion.numeroDocumento = "";
                    });
                }else{if($scope.transaccion.tipoPersona == "JURIDICA"){
                    MaestroService.getTipoDocumentoPJ().then(function(data){
                        $scope.tipoDocumentos = data;
                        $scope.transaccion.numeroDocumento = "";
                    });
                }}
            }

            $scope.monedaChange = function(){
                TasaInteresService.getTasaCuentaAhorro($scope.transaccion.idMoneda).then(
                    function(data){
                        $scope.transaccion.tasaInteres = data.valor;
                    }
                );
            }

            $scope.buscarPersonaSocio = function($event){
                var tipoDoc = $scope.transaccion.idTipoDocumento;
                var numDoc = $scope.transaccion.numeroDocumento;
                if(tipoDoc === null || tipoDoc === undefined){
                    alert("Tipo documento no definido");
                    return;
                }
                if(numDoc === null || numDoc === undefined){
                    alert("Numero documento no definido");
                    return;
                }
                if($scope.transaccion.tipoPersona == "NATURAL"){
                    $scope.socioJuridico = undefined;
                    PersonaNaturalService.findByTipoNumeroDocumento(tipoDoc,numDoc).then(function(persona){
                        $scope.socioNatural = persona;
                    },function error(error){
                        $scope.socioNatural = undefined;
                        $scope.alerts = [{ type: "danger", msg: "Socio no encontrado."}];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                    });
                }else{if($scope.transaccion.tipoPersona == "JURIDICA"){
                    $scope.socioNatural = undefined;
                    PersonaJuridicaService.findByTipoNumeroDocumento(tipoDoc,numDoc).then(function(persona){
                        $scope.socioJuridico = persona;
                    },function error(error){
                        $scope.socioJuridico = undefined;
                        $scope.alerts = [{ type: "danger", msg: "Socio no encontrado."}];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                    });
                }}
                if($event !== undefined)
                    $event.preventDefault();
            }

            //transacacion principal
            $scope.formCrearCuenta = {};
            $scope.crearCuenta = function(){
                if ($scope.formCrearCuenta.$valid) {
                    $scope.control.inProcess = true;

                    //poniendo variables
                    var cuenta = {
                        "idMoneda": $scope.transaccion.idMoneda,
                        "tipoPersona": $scope.transaccion.tipoPersona,
                        "idPersona": $scope.transaccion.idPersona,
                        "cantRetirantes":$scope.transaccion.cantRetirantes,
                        "titulares":[],
                        "beneficiarios": ($filter('unique')($scope.beneficiarios))
                    }

                    for(var i = 0; i < $scope.titularesFinal.length ; i++){
                        var idTitular = $scope.titularesFinal[i].id;
                        cuenta.titulares.push(idTitular);
                    }
                    if($scope.transaccion.idPersona === undefined || $scope.transaccion.idPersona === null)
                        $scope.buscarPersonaSocio();

                    CuentaBancariaService.crearCuentaAhorro(cuenta).then(
                        function(data){
                            $scope.control.inProcess = false;
                            $scope.control.success = true;
                            var mensaje= data.message;
                            $state.transitionTo("app.socio.editarCuentaBancaria", { id: data.id });
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

            $scope.crearPersona = function(){
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
        }]);
});