define(['../../module'], function (controllers) {
    'use strict';
    controllers.controller('CrearPersonaJuridicaController', ["$scope", "$state","$window", "MaestroService", "PersonaNaturalService", "PersonaJuridicaService",
        function($scope, $state,$window, MaestroService, PersonaNaturalService, PersonaJuridicaService) {
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
                if(!angular.isUndefined($scope.ubigeo.departamento))
                    return $scope.ubigeo.departamento.codigo;
                else return "";
            }
            $scope.getProvinciaCode = function(){
                if(!angular.isUndefined($scope.ubigeo.provincia))
                    return $scope.ubigeo.provincia.codigo;
                else return "";
            }
            $scope.getDistritoCode = function(){
                if(!angular.isUndefined($scope.ubigeo.distrito))
                    return $scope.ubigeo.distrito.codigo;
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
                $scope.personaJuridica.tipoDocumento.id = $scope.params.idTipoDocumento;
                if($scope.personaJuridica.tipoDocumento.id !== undefined && $scope.personaJuridica.tipoDocumento.id !== null){
                    for(var i = 0; i < $scope.tipoDocumentosPJ.length; i++){
                        if($scope.tipoDocumentosPJ[i].id == $scope.personaJuridica.tipoDocumento.id)
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
            $scope.personaJuridica.tipoDocumento.id = $scope.params.idTipoDocumento;
            $scope.personaJuridica.numeroDocumento = $scope.params.numeroDocumento;

            $scope.buscarRepresentanteLegal = function($event){
                var tipoDoc = $scope.representanteLegal.tipoDocumento.id;
                var numDoc = $scope.representanteLegal.numeroDocumento;
                if(tipoDoc === null || tipoDoc === undefined){
                    alert("Tipo documento no definido");
                    return;
                }
                if(numDoc === null || numDoc === undefined){
                    alert("Numero documento no definido");
                    return;
                }
                PersonaNaturalService.findByTipoNumeroDocumento(tipoDoc,numDoc).then(function(persona){
                    $scope.representanteLegal = persona;
                },function error(error){
                    $scope.alerts = [{ type: "warning", msg: "Representante legal no encontrado."}];
                    $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                });
                $event.preventDefault();
            }

            $scope.formCrearPersonaJuridica = {};
            $scope.crear = function(){
                if ($scope.formCrearPersonaJuridica.$valid) {
                    $scope.control.inProcess = true;
                    PersonaJuridicaService.crear($scope.personaJuridica, $scope.representanteLegal, $scope.accionistas).then(
                        function(persona){
                            $scope.control.inProcess = false;
                            $scope.control.success = true;
                            $window.close();
                        },
                        function error(error){
                            $scope.control.inProcess = false;
                            $scope.control.success = false;
                            $scope.alerts = [{ type: "danger", msg: "Error: " + error.data + "."}];
                            $scope.alerts.splice(index, 1);
                        }
                    );
                } else {
                    $scope.control.submitted = true;
                }
            }

            $scope.cancel = function () {
                $window.close();
            }

            $scope.buttonDisableState = function(){
                return $scope.control.inProcess;
            }

        }]);
});