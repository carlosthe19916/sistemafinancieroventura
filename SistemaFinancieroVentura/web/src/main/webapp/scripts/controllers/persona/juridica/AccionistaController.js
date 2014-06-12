define(['../../module'], function (controllers) {
    'use strict';

    controllers.controller('AccionistaController', [ "$scope", "$location", "$window", "$timeout", "$filter", "MaestroService", "PersonaNaturalService",
        function($scope, $location, $window,$timeout, $filter, MaestroService, PersonaNaturalService) {

            $scope.control = {"inProcess": false, "submitted" : false};

            $scope.accionista = {
                "tipoDocumento" : undefined,
                "numeroDocumento" : undefined
            }

            MaestroService.getTipoDocumentoPN().then(function(tipoDocumentos){
                $scope.tipoDocumentos = tipoDocumentos;
            });

            $scope.addAccionista = function() {
                if($scope.formAccionista.$valid){
                    $scope.control.inProcess = true;
                    PersonaNaturalService.findByTipoNumeroDocumento($scope.accionista.tipoDocumento.id, $scope.accionista.numeroDocumento).then(
                        function(persona){
                            $scope.control.inProcess = false;
                            var obj = {
                                "porcentajeParticipacion" : "0",
                                "personaNatural" : persona
                            };
                            $scope.accionistas.push(obj);
                            $scope.accionistas = $filter('unique')($scope.accionistas);
                            $scope.resetFocus();
                            $scope.alertsAccionistas = [];
                        }, function error(error){
                            $scope.control.inProcess = false;
                            $scope.alertsAccionistas = [{ type: 'danger', msg: 'Error: persona no encontrada' }];
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

            $scope.resetFocus = function(){
                $scope.formAccionista.$setPristine(false);
                $scope.control.submited = false;
                $scope.accionista = {"tipoDocumento" : undefined,"numeroDocumento" : ""}
                angular.element("#cmbTipoDocumentoAccionista").focus();
            }

            $scope.nuevaPersona = function(){
                var idTipoDoc = undefined;
                if(!angular.isUndefined($scope.accionista.tipoDocumento))
                    idTipoDoc = $scope.accionista.tipoDocumento.id;
                var baseLen = $location.absUrl().length - $location.url().length;
                var url = $location.absUrl().substring(0, baseLen);
                $window.open(url + "/app/socio/personaNatural" + "?tipoDocumento=" + idTipoDoc + "&numeroDocumento=" + $scope.accionista.numeroDocumento);
                $timeout(function() {angular.element("#txtNumeroDocumentoAccionista").focus();}, 100);
            }

            $scope.$watch("accionista.numeroDocumento", function(){
                $scope.validarNumeroDocumentoAccionista();
            });
            $scope.$watch("accionista.tipoDocumento", function(){
                $scope.validarNumeroDocumentoAccionista();
            });
            $scope.validarNumeroDocumentoAccionista = function(){
                if(!angular.isUndefined($scope.formAccionista.numeroDocumento)){
                    if(!angular.isUndefined($scope.accionista.numeroDocumento)){
                        if(!angular.isUndefined($scope.accionista.tipoDocumento)){
                            if($scope.accionista.numeroDocumento.length == $scope.accionista.tipoDocumento.numeroCaracteres) {
                                $scope.formAccionista.numeroDocumento.$setValidity("sgmaxlength",true);
                            } else {$scope.formAccionista.numeroDocumento.$setValidity("sgmaxlength",false);}
                        } else{$scope.formAccionista.numeroDocumento.$setValidity("sgmaxlength",false);}
                    } else {$scope.formAccionista.numeroDocumento.$setValidity("sgmaxlength",false);}}
            }

        }]);
});