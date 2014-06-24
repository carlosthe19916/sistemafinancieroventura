define(['../../module'], function (controllers) {
    'use strict';
    controllers.controller('CrearPersonaNaturalController', ['$scope','$state','$window','MaestroService','PersonaNaturalService','TransitionService',
        function($scope,$state,$window,MaestroService,PersonaNaturalService,TransitionService) {

            $scope.control = {
                "success":false,
                "inProcess": false,
                "submitted" : false,
                "errorForm" : {"numeroDocumento" : false}
            };

            $scope.persona = PersonaNaturalService.getModel();
            $scope.ubigeo = {"departamento": {"codigo":""}, "provincia": {"codigo":""}, "distrito": {"codigo":""}};
            $scope.persona.ubigeo = $scope.ubigeo.departamento.codigo + $scope.ubigeo.provincia.codigo + $scope.ubigeo.distrito.codigo;
            $scope.$watch("ubigeo.departamento", function(){
                $scope.persona.ubigeo = $scope.getDepartamentoCode() + $scope.getProvinciaCode() + $scope.getDistritoCode();
            });
            $scope.$watch("ubigeo.provincia", function(){
                $scope.persona.ubigeo = $scope.getDepartamentoCode() + $scope.getProvinciaCode() + $scope.getDistritoCode();
            });
            $scope.$watch("ubigeo.distrito", function(){
                $scope.persona.ubigeo = $scope.getDepartamentoCode() + $scope.getProvinciaCode() + $scope.getDistritoCode();
            });
            $scope.getDepartamentoCode = function(){
                if(!angular.isUndefined($scope.ubigeo.departamento))
                    return $scope.ubigeo.departamento.codigo;
                else return "";
            };
            $scope.getProvinciaCode = function(){
                if(!angular.isUndefined($scope.ubigeo.provincia))
                    return $scope.ubigeo.provincia.codigo;
                else return "";
            };
            $scope.getDistritoCode = function(){
                if(!angular.isUndefined($scope.ubigeo.distrito))
                    return $scope.ubigeo.distrito.codigo;
                else return "";
            };

            //recuperando parametros de url
            //$scope.persona.tipoDocumento.id = $scope.params.idTipoDocumento;
            $scope.persona.numeroDocumento = $scope.params.numeroDocumento;

            $scope.$watch("persona.numeroDocumento", function(){
                if(!angular.isUndefined($scope.persona.tipoDocumento) && $scope.persona.tipoDocumento !== null){
                    if(angular.isUndefined($scope.persona.numeroDocumento) || $scope.persona.numeroDocumento === null){
                        $scope.control.errorForm.numeroDocumento = true;
                    } else if($scope.persona.tipoDocumento.numeroCaracteres != $scope.persona.numeroDocumento.length){
                        $scope.control.errorForm.numeroDocumento = true;
                    } else {
                        $scope.control.errorForm.numeroDocumento = false;
                    }
                }
                if(angular.isUndefined($scope.persona.numeroDocumento) || $scope.persona.numeroDocumento === "")
                    $scope.control.errorForm.numeroDocumento = true;
                else
                    $scope.control.errorForm.numeroDocumento = false;
            });

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
                if(!angular.isUndefined($scope.params.idTipoDocumento)){
                    for(var i = 0; i < $scope.tipodocumentos.length; i++){
                        if($scope.tipodocumentos[i].id == $scope.params.idTipoDocumento)
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

            $scope.changeDepartamento = function(){
                MaestroService.getProvincias($scope.ubigeo.departamento.id).then(function(provincias){
                    $scope.provincias = provincias;
                });
            };
            $scope.changeProvincia = function(){
                MaestroService.getDistritos($scope.ubigeo.provincia.id).then(function(distritos){
                    $scope.distritos = distritos;
                });
            };

            //logic
            $scope.crearTransaccion = function(){
                console.log($scope.persona.ubigeo);
                if ($scope.formCrearPersonanatural.$valid) {
                    if($scope.control.errorForm.numeroDocumento == true){
                        return;
                    }
                    $scope.buttonDisableState = true;
                    PersonaNaturalService.crear($scope.persona).then(
                        function(persona){
                            if(TransitionService.isModeRedirect()){
                                var url = TransitionService.getUrl();
                                $state.transitionTo(url);
                            } else if(TransitionService.isModeClose()){
                                $window.close();
                            } else {
                                $window.close();
                            }
                        },
                        function error(error){
                            $scope.control.inProcess = false;
                            $scope.control.success = false;
                            $scope.alerts = [{ type: "danger", msg: "Error: " + error.data + "."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                    $scope.buttonDisableState = false;
                } else {
                    $scope.control.submitted = true;
                }
            };

            $scope.cancel = function () {
                if(TransitionService.isModeRedirect()){
                    var url = TransitionService.getUrl();
                    $state.transitionTo(url);
                } else if(TransitionService.isModeClose()){
                    $window.close();
                } else {
                    $window.close();
                }
            };

            $scope.buttonDisableState = function(){
                return $scope.control.inProcess;
            };

        }]);
});