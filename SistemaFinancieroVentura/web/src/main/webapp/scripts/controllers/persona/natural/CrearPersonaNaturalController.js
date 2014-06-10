define(['../../module'], function (controllers) {
    'use strict';
    controllers.controller('CrearPersonaNaturalController', ["$scope", "$state","$window", "MaestroService", "PersonaNaturalService",
        function($scope, $state,$window, MaestroService, PersonaNaturalService) {
            $scope.control = {"success":false, "inProcess": false, "submitted" : false};
            $scope.persona = PersonaNaturalService.getModel();
            $scope.ubigeo = {"departamento":"", "provincia":"", "distrito":""};
            $scope.persona.ubigeo = $scope.ubigeo.departamento + $scope.ubigeo.provincia + $scope.ubigeo.distrito;

            //recuperando parametros de url
            $scope.persona.tipoDocumento.id = $scope.params.idTipoDocumento;
            $scope.persona.numeroDocumento = $scope.params.numeroDocumento;

            $scope.dateOptions = {
                formatYear: 'yyyy',
                startingDay: 1
            };

            $scope.fechaNacimiento = new Date();
            $scope.persona.fechaNacimiento = $scope.fechaNacimiento.getTime();

            $scope.open = function($event) {
                $event.preventDefault();
                $event.stopPropagation();

                $scope.opened = true;
            };

            MaestroService.getTipoDocumentoPN().then(function(tipodocumentos){
                $scope.tipodocumentos = tipodocumentos;
                $scope.persona.tipoDocumento.id = $scope.params.idTipoDocumento;
                if($scope.persona.tipoDocumento.id !== undefined && $scope.persona.tipoDocumento.id !== null){
                    for(var i = 0; i < $scope.tipodocumentos.length; i++){
                        if($scope.tipodocumentos[i].id == $scope.persona.tipoDocumento.id)
                            $scope.persona.tipoDocumento = $scope.tipodocumentos[i];
                    }
                }
            });
            MaestroService.getSexos().then(function(sexos){
                $scope.sexos = sexos;
            });
            MaestroService.getEstadosciviles().then(function(estadosciviles){
                $scope.estadosciviles = estadosciviles;
            });
            MaestroService.getPaises().then(function(paises){
                $scope.paises = paises;
                $scope.persona.codigoPais = "PE";
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

            //logic
            $scope.crearTransaccion = function(){
                if ($scope.formCrearPersonanatural.$valid) {
                    $scope.buttonDisableState = true;
                    PersonaNaturalService.crear($scope.persona).then(
                        function(persona){
                            $window.close();
                        },
                        function error(error){
                            $scope.control.inProcess = false;
                            $scope.control.success = false;
                            $scope.alerts = [{ type: "danger", msg: "Error: " + error.data + "."}];
                            $scope.alerts.splice(index, 1);
                        }
                    );
                    $scope.buttonDisableState = false;
                } else {
                    $scope.control.submitted = true;
                }
            }

            $scope.cancel = function () {
                //$state.go("app.administracion.personanaturalBuscar");
                alert("cancelar");
            }

            $scope.buttonDisableState = function(){
                return $scope.control.inProcess;
            }

        }]);
});