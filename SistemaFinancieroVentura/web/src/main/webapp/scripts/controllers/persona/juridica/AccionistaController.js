define(['../../module'], function (controllers) {
    'use strict';

    controllers.controller('AccionistaController', [ "$scope","$state","$location", "$window", "$timeout", "$filter","focus","MaestroService", "PersonaNaturalService","RedirectService",
        function($scope,$state,$location,$window,$timeout,$filter,focus,MaestroService, PersonaNaturalService,RedirectService) {

            $scope.combo = {
                tipoDocumentos: undefined
            };

            $scope.loadTipoDocumento = function(){
                MaestroService.getTipoDocumentoPN().then(function(data){
                    $scope.combo.tipoDocumentos = data;
                });
            };
            $scope.loadTipoDocumento();

            $scope.addAccionista = function() {
                if($scope.formAccionista.$valid){
                    $scope.control.inProcess = true;
                    PersonaNaturalService.findByTipoNumeroDocumento($scope.$parent.view.idTipoDocumentoAccionista, $scope.$parent.view.numeroDocumentoAccionista).then(
                        function(data){
                            $scope.control.inProcess = false;
                            var obj = {
                                "porcentajeParticipacion" : "0",
                                "personaNatural" : data
                            };
                            $scope.$parent.view.accionistas.push(obj);
                            $scope.$parent.view.accionistas = $filter('unique')($scope.$parent.view.accionistas);
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
            };

            $scope.nuevaPersona = function(){
                $scope.$parent.setTabAcctionistasActive();

                var savedParameters = {
                    id: $scope.view.id
                };
                var sendParameters = {
                    tipoDocumento: $scope.$parent.view.idTipoDocumentoAccionista,
                    numeroDocumento: $scope.$parent.view.numeroDocumentoAccionista
                };
                var nextState = "app.administracion.crearPersonaJuridica";
                var elementFocus = $scope.$parent.focusElements.numeroDocumentoAccionista;
                RedirectService.addNext(nextState,savedParameters,$scope.$parent.view, elementFocus);
                $state.transitionTo('app.administracion.crearPersonaNatural', sendParameters);
            };

            $scope.removeAccionista = function(index){
                $scope.$parent.view.accionistas.splice(index, 1);
                $scope.resetFocus();
            };

            $scope.$watch("view.numeroDocumentoAccionista", function(){
                $scope.validarNumeroDocumentoAccionista();
            });
            $scope.$watch("view.idTipoDocumentoAccionista", function(){
                $scope.validarNumeroDocumentoAccionista();
            });
            $scope.validarNumeroDocumentoAccionista = function(){
                if(!angular.isUndefined($scope.formAccionista.numeroDocumento)){
                    if(!angular.isUndefined($scope.view.numeroDocumentoAccionista)){
                        if(!angular.isUndefined($scope.view.idTipoDocumentoAccionista)){
                            var tipoDoc = $scope.getTipoDocumento();
                            if(!angular.isUndefined(tipoDoc)) {
                                if($scope.view.numeroDocumentoAccionista.length == tipoDoc.numeroCaracteres) {
                                    $scope.formAccionista.numeroDocumento.$setValidity("sgmaxlength",true);
                                } else {$scope.formAccionista.numeroDocumento.$setValidity("sgmaxlength",false);}
                            } else {$scope.formAccionista.numeroDocumento.$setValidity("sgmaxlength",false);}
                        } else{$scope.formAccionista.numeroDocumento.$setValidity("sgmaxlength",false);}
                    } else {$scope.formAccionista.numeroDocumento.$setValidity("sgmaxlength",false);}}
            };
            $scope.getTipoDocumento = function(){
                if(!angular.isUndefined($scope.combo.tipoDocumentos)){
                    for(var i = 0; i < $scope.combo.tipoDocumentos.length; i++){
                        if($scope.$parent.view.idTipoDocumentoAccionista == $scope.combo.tipoDocumentos[i].id)
                            return $scope.combo.tipoDocumentos[i];
                    }
                }
                return undefined;
            };

            $scope.resetFocus = function(){
                $scope.formAccionista.$setPristine(false);

                $scope.$parent.view.idTipoDocumentoAccionista = undefined;
                $scope.$parent.view.numeroDocumentoAccionista = '';

                focus($scope.focusElements.tipoDocumentoAccionista);
            };

        }]);
});