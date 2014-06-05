define(['../../module'], function (controllers) {
    'use strict';
    controllers.controller('AccionistaController', [ "$scope", "MaestroService", "PersonaNaturalService",
        function($scope, MaestroService, PersonaNaturalService) {

            $scope.control = {"inProcess": false, "submitted" : false};

            $scope.accionista = {
                "idTipoDocumento" : undefined,
                "numeroDocumento" : undefined
            }

            MaestroService.getTipoDocumentoPN().then(function(tipoDocumentos){
                $scope.tipoDocumentos = tipoDocumentos;
            });

            $scope.addAccionista = function() {
                if($scope.formAccionista.$valid){
                    $scope.control.inProcess = true;
                    PersonaNaturalService.findByTipoNumeroDocumento($scope.accionista.idTipoDocumento, $scope.accionista.numeroDocumento).then(
                        function(persona){
                            $scope.control.inProcess = false;
                            var obj = {
                                "porcentajeParticipacion" : 0,
                                "personaNatural" : persona
                            };
                            $scope.accionistas.push(obj);
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

            $scope.removeAccionista = function(index){
                $scope.accionistas.splice(index, 1);
                $scope.resetFocus();
            }

            $scope.clear = function(){
                $scope.accionista = {
                    "idTipoDocumento" : undefined,
                    "numeroDocumento" : ""
                }
                $scope.resetFocus();
            }

            $scope.resetFocus = function(){
                angular.element("#cmbTipoDocumentoAccionista").focus();
            }

        }]);
});