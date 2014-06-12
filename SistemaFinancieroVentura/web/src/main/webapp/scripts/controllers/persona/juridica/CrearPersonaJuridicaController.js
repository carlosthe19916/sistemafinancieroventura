define(['../../module'], function (controllers) {
    'use strict';
    controllers.controller('CrearPersonaJuridicaController', ["$scope", "$state","$window","$timeout", "MaestroService", "PersonaNaturalService", "PersonaJuridicaService",
        function($scope, $state,$window,$timeout, MaestroService, PersonaNaturalService, PersonaJuridicaService) {
            $scope.control = {"success":false, "inProcess": false, "submitted" : false};
            $scope.personaJuridica = PersonaJuridicaService.getModel();
            $scope.representanteLegal = PersonaNaturalService.getModel();
            $scope.accionistas = [];
            $scope.ubigeo = {"departamento":"", "provincia":"", "distrito":""};
            $scope.personaJuridica.ubigeo = $scope.ubigeo.departamento + $scope.ubigeo.provincia + $scope.ubigeo.distrito;

            $scope.$watch("ubigeo.departamento", function(){
                $scope.personaJuridica.ubigeo = $scope.getDepartamentoCode() + $scope.getProvinciaCode() + $scope.getDistritoCode();
            });
            $scope.$watch("ubigeo.provincia", function(){
                $scope.personaJuridica.ubigeo = $scope.getDepartamentoCode() + $scope.getProvinciaCode() + $scope.getDistritoCode();
            });
            $scope.$watch("ubigeo.distrito", function(){
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
            $scope.fechaConstitucion = new Date();
            $scope.personaJuridica.fechaConstitucion = $scope.fechaConstitucion.getTime();

            $scope.open = function($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.opened = true;
            };

            MaestroService.getTipoDocumentoPJ().then(function(tipodocumentos){
                $scope.tipoDocumentosPJ = tipodocumentos;
                if(!angular.isUndefined($scope.params.idTipoDocumento)){
                    for(var i = 0; i < $scope.tipoDocumentosPJ.length; i++){
                        if($scope.tipoDocumentosPJ[i].id == $scope.params.idTipoDocumento)
                            $scope.personaJuridica.tipoDocumento = $scope.tipoDocumentosPJ[i];
                    }
                }
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

            //recuperando parametros de url
            //$scope.personaJuridica.tipoDocumento.id = $scope.params.idTipoDocumento;
            $scope.personaJuridica.numeroDocumento = $scope.params.numeroDocumento;

            $scope.buscarRepresentanteLegal = function($event){
                if($scope.formCrearPersonaJuridica.tipoDocumentoRepresentante.$valid
                    && $scope.formCrearPersonaJuridica.numeroDocumentoRepresentante.$valid){
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

            $scope.formCrearPersonaJuridica = {};
            $scope.crear = function(){
                if ($scope.formCrearPersonaJuridica.$valid) {
                    if(angular.isUndefined($scope.representanteLegal.id)){
                        $scope.alerts = [{ type: 'danger', msg: "Representante no cargado." }];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        $window.scrollTo(0,0);
                        return;
                    }

                    var personaJurica = angular.copy($scope.personaJuridica);
                    personaJurica.tipoDocumento = {"id":personaJurica.tipoDocumento.id};
                    personaJurica.idRepresentanteLegal = $scope.representanteLegal.id;
                    personaJurica.accionistas = [];
                    for(var i = 0; i < $scope.accionistas.length; i++){
                        personaJurica.accionistas[i] = {"idPersona": $scope.accionistas[i].personaNatural.id , "porcentaje": $scope.accionistas[i].porcentajeParticipacion};
                    }
                    $scope.control.inProcess = true;
                    PersonaJuridicaService.crear(personaJurica).then(
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

            $scope.$watch("personaJuridica.tipoDocumento", function(){
                $scope.validarNumeroDocumentoPJ();
            });
            $scope.$watch("personaJuridica.numeroDocumento", function(){
                $scope.validarNumeroDocumentoPJ();
            });
            $scope.$watch("representanteLegal.tipoDocumento", function(){
                $scope.validarNumeroDocumentoRepresentanteLegal();
            });
            $scope.$watch("representanteLegal.numeroDocumento", function(){
                $scope.validarNumeroDocumentoRepresentanteLegal();
            });
            $scope.validarNumeroDocumentoPJ = function(){
                if(!angular.isUndefined($scope.formCrearPersonaJuridica.numerodocumento)){
                    if(!angular.isUndefined($scope.personaJuridica.numeroDocumento)){
                        if(!angular.isUndefined($scope.personaJuridica.tipoDocumento)){
                            if($scope.personaJuridica.numeroDocumento.length == $scope.personaJuridica.tipoDocumento.numeroCaracteres) {
                                $scope.formCrearPersonaJuridica.numerodocumento.$setValidity("sgmaxlength",true);
                            } else {$scope.formCrearPersonaJuridica.numerodocumento.$setValidity("sgmaxlength",false);}
                        } else{$scope.formCrearPersonaJuridica.numerodocumento.$setValidity("sgmaxlength",false);}
                    } else {$scope.formCrearPersonaJuridica.numerodocumento.$setValidity("sgmaxlength",false);}}
            }
            $scope.validarNumeroDocumentoRepresentanteLegal = function(){
                if(!angular.isUndefined($scope.formCrearPersonaJuridica.numeroDocumentoRepresentante)){
                    if(!angular.isUndefined($scope.representanteLegal.numeroDocumento)){
                        if(!angular.isUndefined($scope.representanteLegal.tipoDocumento)){
                            if($scope.representanteLegal.numeroDocumento.length == $scope.representanteLegal.tipoDocumento.numeroCaracteres) {
                                $scope.formCrearPersonaJuridica.numeroDocumentoRepresentante.$setValidity("sgmaxlength",true);
                            } else {$scope.formCrearPersonaJuridica.numeroDocumentoRepresentante.$setValidity("sgmaxlength",false);}
                        } else{$scope.formCrearPersonaJuridica.numeroDocumentoRepresentante.$setValidity("sgmaxlength",false);}
                    } else {$scope.formCrearPersonaJuridica.numeroDocumentoRepresentante.$setValidity("sgmaxlength",false);}}
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