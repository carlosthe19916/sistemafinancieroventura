define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('TitularPopUpController', [ "$scope", "MaestroService", "PersonaNaturalService", "$modalInstance",
        function($scope, MaestroService, PersonaNaturalService, $modalInstance) {

            $scope.control = {"submitted" : false};

            $scope.titular = {
                "id" : undefined,
                "numeroDocumento" : undefined,
                "tipoDocumento" : undefined,
                "persona": undefined
            }

            //cargar tipos documento
            MaestroService.getTipoDocumentoPN().then(function(data){
                $scope.tipoDocumentos = data;
            });

            $scope.formTitular = {};
            $scope.addTitular = function() {
                if($scope.formTitular.$valid){
                    $scope.ok();
                } else {
                    $scope.control.submitted = true;
                }
            }

            $scope.buscarPersonaTitular = function($event){
                if($scope.formTitular.$valid){
                    if(!angular.isUndefined($scope.titular.titular.persona)){
                        PersonaNaturalService.findByTipoNumeroDocumento($scope.titular.tipoDocumento.id, $scope.titular.numeroDocumento).then(
                            function(persona){
                                $scope.titular.persona = persona;
                            }, function error(error){
                                $scope.control.inProcess = false;
                                $scope.alertsTitular = [{ type: 'danger', msg: 'Error: persona no encontrada' }];
                                $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                            }
                        );
                    } else {
                        $scope.alertsTitular = [{ type: 'warning', msg: 'Error: persona no cargada' }];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                    }
                } else {
                    $scope.control.submitted = true;
                }

                if(!angular.isUndefined($event))
                    $event.preventDefault();
            }
            $scope.$watch("titular.numeroDocumento", function(){$scope.validarNumeroDocumentoTitular();});
            $scope.$watch("titular.tipoDocumento", function(){$scope.validarNumeroDocumentoTitular();});
            $scope.validarNumeroDocumentoTitular = function(){
                if(!angular.isUndefined($scope.formTitular.numeroDocumento)){
                    if(!angular.isUndefined($scope.titular.numeroDocumento)){
                        if(!angular.isUndefined($scope.titular.tipoDocumento)){
                            if($scope.titular.numeroDocumento.length == $scope.titular.tipoDocumento.numeroCaracteres) {
                                $scope.formTitular.numeroDocumento.$setValidity("sgmaxlength",true);
                            } else {$scope.formTitular.numeroDocumento.$setValidity("sgmaxlength",false);}
                        } else{$scope.formTitular.numeroDocumento.$setValidity("sgmaxlength",false);}
                    } else {$scope.formTitular.numeroDocumento.$setValidity("sgmaxlength",false);}}
            }

            $scope.ok = function () {
                $modalInstance.close($scope.titular.persona);
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

        }]);
});