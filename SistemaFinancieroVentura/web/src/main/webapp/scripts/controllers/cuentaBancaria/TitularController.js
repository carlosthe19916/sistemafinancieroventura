define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('TitularController', [ "$scope", "$state", "$location", "$window", "MaestroService", "PersonaNaturalService", "RedirectService",
        function($scope, $state, $location, $window, MaestroService, PersonaNaturalService, RedirectService) {

            $scope.control = {
                "inProcess": false,
                "submitted" : false
            };

            $scope.view = {
              idTipoDocumento: undefined,
              numeroDocumento: undefined
            };

            $scope.combo = {
              tipoDocumentos: undefined
            };

            $scope.loadTipoDocumentoPN = function(){
                MaestroService.getTipoDocumentoPN().then(function(data){
                    $scope.combo.tipoDocumentos = data;
                });
            };

            $scope.addPersona = function() {
                if($scope.formTitular.$valid){
                    $scope.control.inProcess = true;
                    PersonaNaturalService.findByTipoNumeroDocumento($scope.view.idTipoDocumento, $scope.view.numeroDocumento).then(
                        function(data){
                            $scope.control.inProcess = false;
                            $scope.$parent.view.titulares.push(data);
                            $scope.resetFocus();
                            $scope.alertsTitulares = [];
                        }, function error(error){
                            $scope.control.inProcess = false;
                            $scope.alertsTitulares = [{ type: 'danger', msg: 'Error: persona no encontrada' }];
                            $scope.closeAlert = function(index) {$scope.alertsTitulares.splice(index, 1);};
                        }
                    );
                } else {
                    $scope.control.submitted = true;
                }
            };

            $scope.removePersona = function(index){
                $scope.$parent.view.titulares.splice(index, 1);
                $scope.resetFocus();
            };

            $scope.nuevaPersona = function(){
                var savedParameters = 'AHORRO';
                var sendParameters = {
                    tipoDocumento: $scope.view.idTipoDocumento,
                    numeroDocumento: $scope.view.numeroDocumento
                };
                var nextState = $scope.$parent.viewState;
                var elementFocus = angular.element(document.querySelector('#txtNumeroDocumentoRepresentanteLegal'));
                RedirectService.addNext(nextState,savedParameters,$scope.$parent.view, elementFocus);
                $state.transitionTo('app.administracion.crearPersonaNatural', sendParameters);
            };

            $scope.resetFocus = function(){
                angular.element("#cmbTipoDocumentoTitular").focus();
                $scope.view = {
                    idTipoDocumento: undefined,
                    numeroDocumento: ''
                };
                $scope.control.submitted = false;
                $scope.formTitular.$setPristine();
            };

            $scope.getTipoDocumento = function(){
                if(!angular.isUndefined($scope.combo.tipoDocumentos)){
                    for(var i = 0; i < $scope.combo.tipoDocumentos.length; i++){
                        if($scope.view.idTipoDocumento == $scope.combo.tipoDocumentos[i].id)
                            return $scope.combo.tipoDocumentos[i];
                    }
                }
                return undefined;
            };
            $scope.$watch("view.numeroDocumento",function (newVal, oldVal) {
                if (newVal !== oldVal) {
                    $scope.validarNumeroDocumento();
                }
            },true);
            $scope.validarNumeroDocumento = function(){
                if(!angular.isUndefined($scope.formTitular.numeroDocumento)){
                    if(!angular.isUndefined($scope.view.numeroDocumento)){
                        if(!angular.isUndefined($scope.view.idTipoDocumento)){
                            var tipoDoc = $scope.getTipoDocumentoSocio();
                            if(!angular.isUndefined(tipoDoc)) {
                                if($scope.view.numeroDocumento.length == tipoDoc.numeroCaracteres) {
                                    $scope.formTitular.numeroDocumento.$setValidity("sgmaxlength",true);
                                } else {$scope.formTitular.numeroDocumento.$setValidity("sgmaxlength",false);}
                            } else {$scope.formTitular.numeroDocumento.$setValidity("sgmaxlength",false);}
                        } else{$scope.formTitular.numeroDocumento.$setValidity("sgmaxlength",false);}
                    } else {$scope.formTitular.numeroDocumento.$setValidity("sgmaxlength",false);}}
            };

            $scope.loadTipoDocumentoPN();

        }]);
});