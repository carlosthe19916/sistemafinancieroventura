define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('TitularController', [ "$scope", "$location", "$window", "MaestroService", "PersonaNaturalService",
        function($scope, $location, $window, MaestroService, PersonaNaturalService) {

            $scope.control = {
                "inProcess": false,
                "submitted" : false,
                "errorForm" : {"numeroDocumento" : false}
            };

            $scope.titular = {
                "tipoDocumento" : undefined,
                "numeroDocumento" : undefined
            }

            $scope.$watch("titular.numeroDocumento", function(){
                if(!angular.isUndefined($scope.titular.tipoDocumento) && $scope.titular.tipoDocumento !== null){
                    if(angular.isUndefined($scope.titular.numeroDocumento) || $scope.titular.numeroDocumento === null){
                        $scope.control.errorForm.numeroDocumento = true;
                    } else if($scope.titular.tipoDocumento.numeroCaracteres != $scope.titular.numeroDocumento.length){
                        $scope.control.errorForm.numeroDocumento = true;
                    } else {
                        $scope.control.errorForm.numeroDocumento = false;
                    }
                } else {
                    $scope.control.errorForm.numeroDocumento = true;
                }
                if(angular.isUndefined($scope.titular.numeroDocumento) || $scope.titular.numeroDocumento === null || $scope.titular.numeroDocumento === "")
                    $scope.control.errorForm.numeroDocumento = true;
            });

            //$scope.titulares = [];

            MaestroService.getTipoDocumentoPN().then(function(tipoDocumentos){
                $scope.tipoDocumentos = tipoDocumentos;
            });

            $scope.addPersona = function() {
                if($scope.formTitular.$valid){
                    if($scope.control.errorForm.numeroDocumento)
                        return;
                    $scope.control.inProcess = true;
                    PersonaNaturalService.findByTipoNumeroDocumento($scope.titular.tipoDocumento.id, $scope.titular.numeroDocumento).then(
                        function(persona){
                            $scope.control.inProcess = false;
                            $scope.titulares.push(persona);
                            $scope.resetFocus();
                        }, function error(error){
                            $scope.control.inProcess = false;
                            $scope.alertsTitulares = [{ type: 'danger', msg: 'Error: persona no encontrada' }];
                            $scope.closeAlert = function(index) {$scope.alertsTitulares.splice(index, 1);};
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

            $scope.nuevaPersona = function(){
                var idTipoDoc = undefined;
                if(!angular.isUndefined($scope.titular.tipoDocumento))
                    idTipoDoc = $scope.titular.tipoDocumento.id;
                var baseLen = $location.absUrl().length - $location.url().length;
                var url = $location.absUrl().substring(0, baseLen);
                $window.open(url + "/app/socio/personaNatural" + "?tipoDocumento=" + idTipoDoc + "&numeroDocumento=" + $scope.titular.numeroDocumento);
            }

            $scope.resetFocus = function(){
                angular.element("#cmbTipoDocumentoTitular").focus();
                $scope.titular = {
                    "tipoDocumento" : undefined,
                    "numeroDocumento" : ""
                }
                $scope.control.errorForm.numeroDocumento = false;
                $scope.control.submitted = false;
                $scope.formTitular.$setPristine();
            }


        }]);
});