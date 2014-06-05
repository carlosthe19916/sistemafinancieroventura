define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('CrearCuentaPlazoFijoController', [ "$scope", "$state", "$filter", "$window", "focus", "MaestroService", "MonedaService", "PersonaNaturalService", "PersonaJuridicaService", "SocioService", "TasaInteresService", "CuentaBancariaService",
        function($scope, $state, $filter, $window, focus, MaestroService, MonedaService, PersonaNaturalService, PersonaJuridicaService, SocioService, TasaInteresService, CuentaBancariaService) {

            $scope.$on('$includeContentLoaded', function(){
                focus("firstFocus");
            });

            $scope.control = {"tasaEdited":false,"success":false, "inProcess": false, "submitted" : false};
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
                "idMoneda" : undefined,
                "tasaInteres" : undefined,
                "monto" : undefined,
                "total" : 0.00,
                "tipoPersona" : undefined,
                "idTipoDocumento" : undefined,
                "numeroDocumento" : undefined,
                "idPersona" : undefined,
                "cantRetirantes" : 1,
                "periodo" : undefined,
                "titulares" : {},
                "beneficiarios" : {}
            };

            $scope.$watch('transaccion.tasaInteres', function () {
                $scope.transaccion.total = TasaInteresService.getInteresGenerado($scope.transaccion.tasaInteres, $scope.transaccion.periodo, $scope.transaccion.monto);
            });
            $scope.$watch('transaccion.monto', function () {
                $scope.transaccion.total = TasaInteresService.getInteresGenerado($scope.transaccion.tasaInteres, $scope.transaccion.periodo, $scope.transaccion.monto);
            });
            $scope.$watch('transaccion.periodo', function () {
                $scope.transaccion.total = TasaInteresService.getInteresGenerado($scope.transaccion.tasaInteres, $scope.transaccion.periodo, $scope.transaccion.monto);
            });

            $scope.tabCuentaSelected = function(){
                angular.element("#cmbTipoPersona").focus();
            }
            $scope.tabTitularSelected = function(){
                angular.element("#cmbTipoDocumentoTitular").focus();
            }
            $scope.tabBeneficiarioSelected = function(){
                angular.element("#txtNumeroDocumentoBeneficiario").focus();
            }

            $scope.editarTasaInteres = function(){
                $scope.control.tasaEdited = true;
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

            $scope.actualizarTasaInteres = function($event){
                $scope.control.submitted = true;
                if($scope.transaccion.idMoneda !== undefined && $scope.transaccion.idMoneda) {
                    if($scope.transaccion.periodo !== undefined && $scope.transaccion.periodo !== null){
                        if($scope.transaccion.monto !== undefined && $scope.transaccion.monto !== null){
                            TasaInteresService.getTasaCuentaPlazoFijo($scope.transaccion.idMoneda,$scope.transaccion.periodo,$scope.transaccion.monto).then(function(data){
                                $scope.transaccion.tasaInteres = data.valor;
                            });
                        } else {
                            TasaInteresService.getTasaCuentaPlazoFijo($scope.transaccion.idMoneda,$scope.transaccion.periodo).then(function(data){
                                $scope.transaccion.tasaInteres = data.valor;
                            });
                        }
                    } else {
                        TasaInteresService.getTasaCuentaPlazoFijo($scope.transaccion.idMoneda).then(function(data){
                            $scope.transaccion.tasaInteres = data.valor;
                        });
                    }
                }
                if($event !== undefined)
                    $event.preventDefault();
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
                        "periodo": $scope.transaccion.periodo,
                        "monto": $scope.transaccion.monto,
                        "tasaInteres": $scope.transaccion.tasaInteres,

                        "tipoPersona": $scope.transaccion.tipoPersona,
                        "idTipoDocumento": $scope.transaccion.idTipoDocumento,
                        "numeroDocumento": $scope.transaccion.numeroDocumento,

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

                    CuentaBancariaService.crearCuentaPlazoFijo(cuenta).then(
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