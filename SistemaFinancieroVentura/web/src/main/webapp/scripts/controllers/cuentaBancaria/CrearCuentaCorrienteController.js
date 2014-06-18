define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('CrearCuentaCorrienteController', [ "$scope", "$state","$location", "$filter", "$window","$timeout", "focus","$modal", "MaestroService", "MonedaService", "PersonaNaturalService", "PersonaJuridicaService", "SocioService", "TasaInteresService", "CuentaBancariaService",
        function($scope, $state,$location, $filter, $window,$timeout, focus,$modal, MaestroService, MonedaService, PersonaNaturalService, PersonaJuridicaService, SocioService, TasaInteresService, CuentaBancariaService) {

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
                "moneda" : undefined,
                "tasaInteres" : undefined,
                "monto" : undefined,
                "tipoPersona" : undefined,
                "tipoDocumento" : undefined,
                "numeroDocumento" : undefined,
                "persona" : undefined,
                "cantRetirantes" : 1,
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
                if($scope.transaccion.moneda !== undefined && $scope.transaccion.moneda) {
                    TasaInteresService.getTasaCuentaCorriente($scope.transaccion.moneda.id).then(function(data){
                        $scope.transaccion.tasaInteres = data.valor;
                    });
                }
                if($event !== undefined)
                    $event.preventDefault();
            }

            $scope.buscarPersonaSocio = function($event){
                if($scope.formCrearCuenta.tipoDocumento.$valid
                    && $scope.formCrearCuenta.numeroDocumento.$valid){
                    var tipoDoc = $scope.transaccion.tipoDocumento.id;
                    var numDoc = $scope.transaccion.numeroDocumento;
                    if($scope.transaccion.tipoPersona == "NATURAL"){
                        $scope.socioJuridico = undefined;
                        PersonaNaturalService.findByTipoNumeroDocumento(tipoDoc,numDoc).then(function(persona){
                            $scope.socioNatural = persona;
                            $scope.alerts = [{ type: "success", msg: "Persona encontrada."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        },function error(error){
                            $scope.socioNatural = undefined;
                            $scope.alerts = [{ type: "danger", msg: "Persona no encontrada."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        });
                    }else{if($scope.transaccion.tipoPersona == "JURIDICA"){
                        $scope.socioNatural = undefined;
                        PersonaJuridicaService.findByTipoNumeroDocumento(tipoDoc,numDoc).then(function(persona){
                            $scope.socioJuridico = persona;
                            $scope.alerts = [{ type: "success", msg: "Persona encontrada."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        },function error(error){
                            $scope.socioJuridico = undefined;
                            $scope.alerts = [{ type: "danger", msg: "Persona no encontrada."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        });
                    }}
                    if($event !== undefined)
                        $event.preventDefault();
                }
            }

            //transacacion principal
            $scope.formCrearCuenta = {};
            $scope.crearCuenta = function(){
                if ($scope.formCrearCuenta.$valid) {
                    $scope.control.inProcess = true;

                    //poniendo variables
                    var cuenta = {
                        "idMoneda": $scope.transaccion.moneda.id,
                        "tasaInteres" : $scope.transaccion.tasaInteres,
                        "tipoPersona": $scope.transaccion.tipoPersona,
                        "idTipoDocumento": $scope.transaccion.tipoDocumento.id,
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

                    CuentaBancariaService.crearCuentaCorriente(cuenta).then(
                        function(data){
                            $scope.control.inProcess = false;
                            $scope.control.success = true;
                            var mensaje= data.message;
                            $state.transitionTo("app.socio.firmasCuentaBancaria", { id: data.id });
                        }, function error(error){
                            $scope.control.inProcess = false;
                            $scope.control.success = false;
                            $scope.alerts = [{ type: "danger", msg: "Error: " + error.data.message + "."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                            $window.scrollTo(0,0);
                        }
                    );
                } else {
                    $scope.control.submitted = true;
                }
            }

            $scope.crearPersona = function(){
                if($scope.transaccion.tipoPersona !== undefined && $scope.transaccion.tipoPersona !== null){
                    if($scope.transaccion.tipoPersona == "NATURAL"){
                        var idTipoDoc = undefined;
                        if(!angular.isUndefined($scope.transaccion.tipoDocumento))
                            idTipoDoc = $scope.transaccion.tipoDocumento.id;
                        var baseLen = $location.absUrl().length - $location.url().length;
                        var url = $location.absUrl().substring(0, baseLen);
                        $window.open(url + "/app/socio/personaNatural" + "?tipoDocumento=" + idTipoDoc + "&numeroDocumento=" + $scope.transaccion.numeroDocumento);
                        $timeout(function() {angular.element("#txtNumeroDocumentoSocio").focus();}, 100);
                    } else{if($scope.transaccion.tipoPersona == "JURIDICA"){
                        var idTipoDoc = undefined;
                        if(!angular.isUndefined($scope.transaccion.tipoDocumento))
                            idTipoDoc = $scope.transaccion.tipoDocumento.id;
                        var baseLen = $location.absUrl().length - $location.url().length;
                        var url = $location.absUrl().substring(0, baseLen);
                        $window.open(url + "/app/socio/personaJuridica" + "?tipoDocumento=" + idTipoDoc + "&numeroDocumento=" + $scope.transaccion.numeroDocumento);
                        $timeout(function() {angular.element("#txtNumeroDocumentoSocio").focus();}, 100);
                    }}
                } else{
                    alert("Seleccione tipo de persona");
                }
            }


            $scope.login = {"result":false , "tasaInteres": undefined};
            $scope.openLoginPopUp = function () {
                var modalInstance = $modal.open({
                    templateUrl: 'views/cajero/util/loginPopUp.html',
                    controller: "LoginPopUpController"
                });
                modalInstance.result.then(function (result) {
                    $scope.login.result = result;
                    $timeout(function() {
                        angular.element("#txtTasaInteresEdited").focus();
                    }, 100);
                }, function () {
                    console.log('Modal dismissed at: ' + new Date());
                });
            };

            $scope.setTasaInteres = function($event){
                if(!angular.isUndefined($scope.login.tasaInteres)){
                    var final = parseFloat($scope.login.tasaInteres.replace(',','.').replace(' ',''));
                    if(final >= 0 && final <= 100) {
                        $scope.transaccion.tasaInteres = final / 100;
                        $scope.login.result = false;
                        angular.element("#btnGuardar").focus();
                        if(!angular.isUndefined($event))
                            $event.preventDefault();
                    } else {
                        if(!angular.isUndefined($event))
                            $event.preventDefault();
                    }
                }else {
                    if(!angular.isUndefined($event))
                        $event.preventDefault();
                }
            }

            $scope.$watch("transaccion.numeroDocumento", function(){$scope.validarNumeroDocumentoSocio();});
            $scope.$watch("transaccion.tipoDocumento", function(){$scope.validarNumeroDocumentoSocio();});
            $scope.$watch("transaccion.cantRetirantes", function(){$scope.validarCantidadRetirantes();});
            $scope.validarNumeroDocumentoSocio = function(){
                if(!angular.isUndefined($scope.formCrearCuenta.numeroDocumento)){
                    if(!angular.isUndefined($scope.transaccion.numeroDocumento)){
                        if(!angular.isUndefined($scope.transaccion.tipoDocumento)){
                            if($scope.transaccion.numeroDocumento.length == $scope.transaccion.tipoDocumento.numeroCaracteres) {
                                $scope.formCrearCuenta.numeroDocumento.$setValidity("sgmaxlength",true);
                            } else {$scope.formCrearCuenta.numeroDocumento.$setValidity("sgmaxlength",false);}
                        } else{$scope.formCrearCuenta.numeroDocumento.$setValidity("sgmaxlength",false);}
                    } else {$scope.formCrearCuenta.numeroDocumento.$setValidity("sgmaxlength",false);}}
            }
            $scope.validarCantidadRetirantes = function(){
                if(!angular.isUndefined($scope.formCrearCuenta.cantRetirantes)){
                    if($scope.titularesFinal.length < $scope.transaccion.cantRetirantes){
                        $scope.formCrearCuenta.cantRetirantes.$setValidity("sgmaxlength",false);
                    } else {$scope.formCrearCuenta.cantRetirantes.$setValidity("sgmaxlength",true);}
                }
            }
        }]);
});