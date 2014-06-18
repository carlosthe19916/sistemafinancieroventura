define(['../../module'], function (controllers) {
    'use strict';
    controllers.controller('EditarPersonaNaturalController', ["$scope", "$state","$window", "MaestroService", "PersonaNaturalService",
        function($scope, $state,$window, MaestroService, PersonaNaturalService) {
            $scope.control = {
                "success":false,
                "inProcess": false,
                "submitted" : false
            };

            if(!angular.isUndefined($scope.id)){
                PersonaNaturalService.findById($scope.id).then(
                    function(data){
                        $scope.persona = data;
                        $scope.$watch("persona.tipoDocumento", function(){
                            if(!angular.isUndefined($scope.tipodocumentos)){
                                for(var i = 0; i < $scope.tipodocumentos.length; i++){
                                    if($scope.tipodocumentos[i].id == $scope.persona.tipoDocumento.id)
                                        $scope.persona.tipoDocumento = $scope.tipodocumentos[i];
                                }
                            }
                        });
                        $scope.$watch("persona.numeroDocumento", function(){$scope.validarNumeroDocumento();});
                        $scope.$watch("persona.tipoDocumento", function(){$scope.validarNumeroDocumento();});
                        $scope.validarNumeroDocumento = function(){
                            if(!angular.isUndefined($scope.formEditarPersonanatural.numerodocumento)){
                                if(!angular.isUndefined($scope.persona.numeroDocumento)){
                                    if(!angular.isUndefined($scope.persona.tipoDocumento)){
                                        if($scope.persona.numeroDocumento.length == $scope.persona.tipoDocumento.numeroCaracteres) {
                                            $scope.formEditarPersonanatural.numerodocumento.$setValidity("sgmaxlength",true);
                                        } else {$scope.formEditarPersonanatural.numerodocumento.$setValidity("sgmaxlength",false);}
                                    } else{$scope.formEditarPersonanatural.numerodocumento.$setValidity("sgmaxlength",false);}
                                } else {$scope.formEditarPersonanatural.numerodocumento.$setValidity("sgmaxlength",false);}}
                        }
                    }, function error(error){
                        $scope.alerts = [{ type: "danger", msg: "Error: No se pudo cargar la persona."}];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                    }
                );
            }

            $scope.ubigeo = {"departamento": {"codigo":""}, "provincia": {"codigo":""}, "distrito": {"codigo":""}};

            $scope.$watch("ubigeo.departamento", function(){
                if(!angular.isUndefined($scope.persona))
                    $scope.persona.ubigeo = $scope.getDepartamentoCode() + $scope.getProvinciaCode() + $scope.getDistritoCode();
            });
            $scope.$watch("ubigeo.provincia", function(){
                if(!angular.isUndefined($scope.persona))
                    $scope.persona.ubigeo = $scope.getDepartamentoCode() + $scope.getProvinciaCode() + $scope.getDistritoCode();
            });
            $scope.$watch("ubigeo.distrito", function(){
                if(!angular.isUndefined($scope.persona))
                    $scope.persona.ubigeo = $scope.getDepartamentoCode() + $scope.getProvinciaCode() + $scope.getDistritoCode();
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

            $scope.dateOptions = {formatYear: 'yyyy', startingDay: 1};

            $scope.open = function($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.opened = true;
            };

            MaestroService.getTipoDocumentoPN().then(function(tipodocumentos){
                $scope.tipodocumentos = tipodocumentos;
            });
            MaestroService.getSexos().then(function(sexos){
                $scope.sexos = sexos;
            });
            MaestroService.getEstadosciviles().then(function(estadosciviles){
                $scope.estadosciviles = estadosciviles;
            });
            MaestroService.getPaises().then(function(paises){
                $scope.paises = paises;
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

            //logic
            $scope.crearTransaccion = function(){
                if ($scope.formEditarPersonanatural.$valid) {
                    $scope.control.inProcess = true;
                    PersonaNaturalService.update($scope.persona).then(
                        function(persona){
                            $window.close();
                            $scope.control.inProcess = false;
                        },
                        function error(error){
                            $scope.control.inProcess = false;
                            $scope.control.success = false;
                            $scope.alerts = [{ type: "danger", msg: "Error: No se pudo cargar la persona."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
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