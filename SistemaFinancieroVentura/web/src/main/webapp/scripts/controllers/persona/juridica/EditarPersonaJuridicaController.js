define(['../../module'], function (controllers) {
    'use strict';
    controllers.controller('EditarPersonaJuridicaController', ["$scope", "$state","$location", "$window","$timeout", "MaestroService", "PersonaNaturalService", "PersonaJuridicaService",
        function($scope, $state,$location, $window,$timeout, MaestroService, PersonaNaturalService, PersonaJuridicaService) {

            $scope.control = {"success":false, "inProcess": false, "submitted" : false};
            $scope.formEditarPersonaJuridica = {};
            $scope.accionistas = [];
            $scope.ubigeo = {"departamento":"", "provincia":"", "distrito":""};

            if(!angular.isUndefined($scope.id)){
                PersonaJuridicaService.findById($scope.id).then(
                    function(data){
                        $scope.personaJuridica = data;
                        $scope.representanteLegal = $scope.personaJuridica.representanteLegal;
                        $scope.accionistas = $scope.personaJuridica.accionistas;
                        $scope.$watch("personaJuridica.tipoDocumento", function(){
                            if(!angular.isUndefined($scope.tipoDocumentosPJ)){
                                for(var i = 0; i < $scope.tipoDocumentosPJ.length; i++){
                                    if($scope.tipoDocumentosPJ[i].id == $scope.personaJuridica.tipoDocumento.id)
                                        $scope.personaJuridica.tipoDocumento = $scope.tipoDocumentosPJ[i];
                                }
                            }
                        });
                        $scope.$watch("representanteLegal.tipoDocumento", function(){
                            if(!angular.isUndefined($scope.tipoDocumentosPN)){
                                for(var i = 0; i < $scope.tipoDocumentosPN.length; i++){
                                    if($scope.tipoDocumentosPN[i].id == $scope.representanteLegal.tipoDocumento.id)
                                        $scope.representanteLegal.tipoDocumento = $scope.tipoDocumentosPN[i];
                                }
                            }
                        });
                        $scope.$watch("personaJuridica.numeroDocumento", function(){$scope.validarNumeroDocumentoPJ();});
                        $scope.$watch("personaJuridica.tipoDocumento", function(){$scope.validarNumeroDocumentoPJ();});
                        $scope.$watch("representanteLegal.numeroDocumento", function(){$scope.validarNumeroDocumentoPJ();});
                        $scope.$watch("representanteLegal.tipoDocumento", function(){$scope.validarNumeroDocumentoPJ();});
                        $scope.validarNumeroDocumentoPJ = function(){
                            if(!angular.isUndefined($scope.formEditarPersonaJuridica.numerodocumento)){
                                if(!angular.isUndefined($scope.personaJuridica.numeroDocumento)){
                                    if(!angular.isUndefined($scope.personaJuridica.tipoDocumento)){
                                        if($scope.personaJuridica.numeroDocumento.length == $scope.personaJuridica.tipoDocumento.numeroCaracteres) {
                                            $scope.formEditarPersonaJuridica.numerodocumento.$setValidity("sgmaxlength",true);
                                        } else {$scope.formEditarPersonaJuridica.numerodocumento.$setValidity("sgmaxlength",false);}
                                    } else{$scope.formEditarPersonaJuridica.numerodocumento.$setValidity("sgmaxlength",false);}
                                } else {$scope.formEditarPersonaJuridica.numerodocumento.$setValidity("sgmaxlength",false);}}
                        }
                        $scope.validarNumeroDocumentoRepresentanteLegal = function(){
                            if(!angular.isUndefined($scope.formEditarPersonaJuridica.numeroDocumentoRepresentante)){
                                if(!angular.isUndefined($scope.representanteLegal.numeroDocumento)){
                                    if(!angular.isUndefined($scope.representanteLegal.tipoDocumento)){
                                        if($scope.representanteLegal.numeroDocumento.length == $scope.representanteLegal.tipoDocumento.numeroCaracteres) {
                                            $scope.formEditarPersonaJuridica.numeroDocumentoRepresentante.$setValidity("sgmaxlength",true);
                                        } else {$scope.formEditarPersonaJuridica.numeroDocumentoRepresentante.$setValidity("sgmaxlength",false);}
                                    } else{$scope.formEditarPersonaJuridica.numeroDocumentoRepresentante.$setValidity("sgmaxlength",false);}
                                } else {$scope.formEditarPersonaJuridica.numeroDocumentoRepresentante.$setValidity("sgmaxlength",false);}}
                        }
                    }, function error(error){
                        $scope.alerts = [{ type: "danger", msg: "Error: No se pudo cargar la persona."}];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                    }
                );
            }

            $scope.$watch("ubigeo.departamento", function(){
                if(!angular.isUndefined($scope.personaJuridica))
                    $scope.personaJuridica.ubigeo = $scope.getDepartamentoCode() + $scope.getProvinciaCode() + $scope.getDistritoCode();
            });
            $scope.$watch("ubigeo.provincia", function(){
                if(!angular.isUndefined($scope.personaJuridica))
                    $scope.personaJuridica.ubigeo = $scope.getDepartamentoCode() + $scope.getProvinciaCode() + $scope.getDistritoCode();
            });
            $scope.$watch("ubigeo.distrito", function(){
                if(!angular.isUndefined($scope.personaJuridica))
                    $scope.personaJuridica.ubigeo = $scope.getDepartamentoCode() + $scope.getProvinciaCode() + $scope.getDistritoCode();
            });
            $scope.getDepartamentoCode = function(){
                if(!angular.isUndefined($scope.ubigeo.departamento)) return $scope.ubigeo.departamento.codigo;
                else return "";
            }
            $scope.getProvinciaCode = function(){
                if(!angular.isUndefined($scope.ubigeo.provincia)) return $scope.ubigeo.provincia.codigo;
                else return "";
            }
            $scope.getDistritoCode = function(){
                if(!angular.isUndefined($scope.ubigeo.distrito)) return $scope.ubigeo.distrito.codigo;
                else return "";
            }

            $scope.dateOptions = {formatYear: 'yyyy',startingDay: 1};
            $scope.open = function($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.opened = true;
            };

            MaestroService.getTipoDocumentoPJ().then(function(tipodocumentos){
                $scope.tipoDocumentosPJ = tipodocumentos;
            });
            MaestroService.getTipoDocumentoPN().then(function(tipodocumentos){
                $scope.tipoDocumentosPN = tipodocumentos;
            });
            MaestroService.getTiposEmpresa().then(function(tiposEmpresa){
                $scope.tiposEmpresa = tiposEmpresa;
            });

            MaestroService.getDepartamentos().then(function(departamentos){
                $scope.departamentos = departamentos;
            });

            $scope.changeDepartamento = function(){
                MaestroService.getProvincias($scope.ubigeo.departamento.id).then(function(provincias){
                    $scope.provincias = provincias;
                });
            }
            $scope.changeProvincia = function(){
                MaestroService.getDistritos($scope.ubigeo.provincia.id).then(function(distritos){
                    $scope.distritos = distritos;
                });
            }

            $scope.buscarRepresentanteLegal = function($event){
                if($scope.formEditarPersonaJuridica.tipoDocumentoRepresentante.$valid
                    && $scope.formEditarPersonaJuridica.numeroDocumentoRepresentante.$valid){
                    var tipoDoc = $scope.representanteLegal.tipoDocumento.id;
                    var numDoc = $scope.representanteLegal.numeroDocumento;
                    PersonaNaturalService.findByTipoNumeroDocumento(tipoDoc,numDoc).then(function(persona){
                        $scope.representanteLegal = persona;
                        for(var i = 0; i < $scope.tipoDocumentosPN.length; i++){
                            if($scope.tipoDocumentosPN[i].id == $scope.representanteLegal.tipoDocumento.id)
                                $scope.representanteLegal.tipoDocumento = $scope.tipoDocumentosPN[i];
                        }
                    },function error(error){
                        $scope.alerts = [{ type: "danger", msg: "Representante legal no encontrado."}];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        $window.scrollTo(0,0);
                    });
                    if(!angular.isUndefined($event)) $event.preventDefault();
                }
            }

            $scope.crearTransaccion = function(){
                if ($scope.formEditarPersonaJuridica.$valid) {
                    if(angular.isUndefined($scope.representanteLegal.id)){
                        $scope.alerts = [{ type: 'danger', msg: "Representante no cargado." }];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        $window.scrollTo(0,0);
                        return;
                    }

                    var personaJurica = angular.copy($scope.personaJuridica);
                    delete personaJurica.representanteLegal;
                    personaJurica.tipoDocumento = {"id":personaJurica.tipoDocumento.id};
                    personaJurica.idRepresentanteLegal = $scope.representanteLegal.id;
                    personaJurica.accionistas = [];
                    for(var i = 0; i < $scope.accionistas.length; i++){
                        personaJurica.accionistas[i] = {"idPersona": $scope.accionistas[i].personaNatural.id , "porcentaje": $scope.accionistas[i].porcentajeParticipacion};
                    }
                    $scope.control.inProcess = true;
                    PersonaJuridicaService.update(personaJurica).then(
                        function(persona){
                            $scope.control.inProcess = false;
                            $scope.control.success = true;
                            $window.close();
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
            }

            $scope.nuevaPersonaRepresentanteLegal = function(){
                var idTipoDoc = undefined;
                if(!angular.isUndefined($scope.representanteLegal.tipoDocumento))
                    idTipoDoc = $scope.representanteLegal.tipoDocumento.id;
                var baseLen = $location.absUrl().length - $location.url().length;
                var url = $location.absUrl().substring(0, baseLen);
                $window.open(url + "/app/socio/personaNatural" + "?tipoDocumento=" + idTipoDoc + "&numeroDocumento=" + $scope.representanteLegal.numeroDocumento);
                $timeout(function() {angular.element("#txtNumeroDocumentoRepresentanteLegal").focus();}, 100);
            }

            $scope.cancel = function () {
                $window.close();
            }

            $scope.buttonDisableState = function(){
                return $scope.control.inProcess;
            }


            $scope.$on('$includeContentLoaded', function(){
                $scope.tabPersonaJuridicaSelected();
            });
            $scope.tabPersonaJuridicaSelected = function(){
                $timeout(function() {
                    angular.element("#cmbTipoDocumentoPersonaJuridica").focus();
                }, 100);
            }
            $scope.tabAccionistaSelected = function(){
                $timeout(function() {
                    angular.element("#cmbTipoDocumentoAccionista").focus();
                }, 100);
            }

        }]);
});