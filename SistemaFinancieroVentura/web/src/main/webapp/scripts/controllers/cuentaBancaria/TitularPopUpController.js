define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('TitularPopUpController', [ "$scope","$timeout","MaestroService","PersonaNaturalService", "$modalInstance","focus",
        function($scope,$timeout, MaestroService, PersonaNaturalService, $modalInstance,focus) {

            $scope.focusElements = {
                tipoDocumentoTitular: 'focusTipoDocumentoTitular'
            };
            $scope.setInitialFocus = function($event){
                if(!angular.isUndefined($event))
                    $event.preventDefault();
                $timeout(function() {
                    focus($scope.focusElements.tipoDocumentoTitular);
                }, 100);
            };
            $scope.setInitialFocus();

            $scope.control = {"submitted" : false};

            $scope.titular = {
                "id" : undefined,
                "numeroDocumento" : undefined,
                "tipoDocumento" : undefined,
                "persona": undefined
            };

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
            };

            $scope.buscarPersonaTitular = function($event){
                if($scope.formTitular.$valid){
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
                    $scope.control.submitted = true;
                }

                if(!angular.isUndefined($event))
                    $event.preventDefault();
            };

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
            };

            $scope.ok = function () {
                if(!angular.isUndefined($scope.titular.persona)){
                    $modalInstance.close($scope.titular.persona);
                }
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

        }]);
});