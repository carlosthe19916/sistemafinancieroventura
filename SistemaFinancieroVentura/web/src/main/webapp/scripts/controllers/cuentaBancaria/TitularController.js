define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('TitularController', [ "$scope", "MaestroService", "PersonaNaturalService",
        function($scope, MaestroService, PersonaNaturalService) {

            $scope.control = {"inProcess": false, "submitted" : false};

            $scope.titular = {
                "idTipoDocumento" : undefined,
                "numeroDocumento" : undefined
            }

            //$scope.titulares = [];

            MaestroService.getTipoDocumentoPN().then(function(tipoDocumentos){
                $scope.tipoDocumentos = tipoDocumentos;
            });

            $scope.addPersona = function() {
                if($scope.formTitular.$valid){
                    $scope.control.inProcess = true;
                    PersonaNaturalService.findByTipoNumeroDocumento($scope.titular.idTipoDocumento, $scope.titular.numeroDocumento).then(
                        function(persona){
                            $scope.control.inProcess = false;
                            $scope.titulares.push(persona);
                            $scope.clear();
                            $scope.resetFocus();
                        }, function error(error){
                            $scope.control.inProcess = false;
                            $scope.alerts = [{ type: 'danger', msg: 'Error: persona no encontrada' }];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                } else {
                    $scope.control.submitted = true;
                }
            }

            $scope.removePersona = function(index){
                $scope.titulares.splice(index, 1);
                $scope.resetFocus();
            }

            $scope.clear = function(){
                $scope.titular = {
                    "idTipoDocumento" : undefined,
                    "numeroDocumento" : ""
                }
                $scope.resetFocus();
            }

            $scope.resetFocus = function(){
                angular.element("#cmbTipoDocumentoTitular").focus();
            }

        }]);
});