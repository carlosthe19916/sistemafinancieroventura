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

            $scope.changeDepartamento = function(departamento){
                MaestroService.getProvincias(departamento.id).then(function(provincias){
                    $scope.provincias = provincias;
                });
            }
            $scope.changeProvincia = function(provincia){
                MaestroService.getDistritos(provincia.id).then(function(distritos){
                    $scope.distritos = distritos;
                });
            }

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