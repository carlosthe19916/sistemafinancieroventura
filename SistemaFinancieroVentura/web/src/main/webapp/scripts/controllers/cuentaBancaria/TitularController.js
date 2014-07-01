define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('TitularController', [ "$scope", "$state", "$location","$filter", "$window","focus", "MaestroService", "PersonaNaturalService", "RedirectService",
        function($scope, $state, $location, $filter, $window,focus, MaestroService, PersonaNaturalService, RedirectService) {

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
                    PersonaNaturalService.findByTipoNumeroDocumento($scope.$parent.view.idTipoDocumentoTitular, $scope.$parent.view.numeroDocumentoTitular).then(
                        function(data){
                            $scope.$parent.view.titulares.push(data);
                            $scope.$parent.view.titulares = $filter('unique')($scope.$parent.view.titulares);
                            $scope.resetFocus();
                            $scope.alertsTitulares = [];
                        }, function error(error){
                            $scope.alertsTitulares = [{ type: 'danger', msg: 'Error: persona no encontrada' }];
                            $scope.closeAlert = function(index) {$scope.alertsTitulares.splice(index, 1);};
                        }
                    );
                }
            };

            $scope.removePersona = function(index){
                $scope.$parent.view.titulares.splice(index, 1);
                $scope.resetFocus();
            };

            $scope.nuevaPersona = function(){
                $scope.$parent.setTabTitularesActive();
                var savedParameters = 'AHORRO';
                var sendParameters = {
                    tipoDocumento: $scope.$parent.view.idTipoDocumentoTitular,
                    numeroDocumento: $scope.$parent.view.numeroDocumentoTitular
                };
                var nextState = $scope.$parent.viewState;
                var elementFocus = $scope.$parent.focusElements.numeroDocumentoTitular;
                RedirectService.addNext(nextState,savedParameters,$scope.$parent.view, elementFocus);
                $state.transitionTo('app.administracion.crearPersonaNatural', sendParameters);
            };

            $scope.resetFocus = function(){
                $scope.$parent.view.idTipoDocumentoTitular = undefined;
                $scope.$parent.view.numeroDocumentoTitular = undefined;
                $scope.formTitular.$setPristine();
                focus($scope.focusElements.tipoDocumentoTitular);
            };

            $scope.getTipoDocumento = function(){
                if(!angular.isUndefined($scope.combo.tipoDocumentos)){
                    for(var i = 0; i < $scope.combo.tipoDocumentos.length; i++){
                        if($scope.$parent.view.idTipoDocumentoTitular == $scope.combo.tipoDocumentos[i].id)
                            return $scope.combo.tipoDocumentos[i];
                    }
                }
                return undefined;
            };
            $scope.$watch("view.numeroDocumentoTitular",function (newVal, oldVal) {
                if (newVal !== oldVal) {
                    $scope.validarNumeroDocumento();
                }
            },true);
            $scope.validarNumeroDocumento = function(){
                if(!angular.isUndefined($scope.formTitular.numeroDocumento)){
                    if(!angular.isUndefined($scope.view.numeroDocumentoTitular)){
                        if(!angular.isUndefined($scope.view.idTipoDocumentoTitular)){
                            var tipoDoc = $scope.getTipoDocumentoSocio();
                            if(!angular.isUndefined(tipoDoc)) {
                                if($scope.view.numeroDocumentoTitular.length == tipoDoc.numeroCaracteres) {
                                    $scope.formTitular.numeroDocumento.$setValidity("sgmaxlength",true);
                                } else {$scope.formTitular.numeroDocumento.$setValidity("sgmaxlength",false);}
                            } else {$scope.formTitular.numeroDocumento.$setValidity("sgmaxlength",false);}
                        } else{$scope.formTitular.numeroDocumento.$setValidity("sgmaxlength",false);}
                    } else {$scope.formTitular.numeroDocumento.$setValidity("sgmaxlength",false);}}
            };

            $scope.loadTipoDocumentoPN();

        }]);
});