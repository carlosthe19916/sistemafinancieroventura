define(['../../module'], function (controllers) {
    'use strict';

    controllers.controller('AccionistaController', [ "$scope","$state","$location", "$window", "$timeout", "$filter", "MaestroService", "PersonaNaturalService","RedirectService",
        function($scope,$state,$location, $window,$timeout, $filter, MaestroService, PersonaNaturalService,RedirectService) {

            $scope.control = {
                inProcess: false,
                submitted : false
            };

            $scope.view = {
              idTipoDocumento: undefined,
              numeroDocumento: undefined
            };

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
                    PersonaNaturalService.findByTipoNumeroDocumento($scope.view.idTipoDocumento, $scope.view.numeroDocumento).then(
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
                var savedParameters = {
                    id: $scope.view.id
                };
                var sendParameters = {
                    tipoDocumento: $scope.view.idTipoDocumento,
                    numeroDocumento: $scope.view.numeroDocumento
                };
                var nextState = "app.administracion.crearPersonaJuridica";
                var elementFocus = angular.element(document.querySelector('#txtNumeroDocumentoRepresentanteLegal'));
                RedirectService.addNext(nextState,savedParameters,$scope.$parent.view, elementFocus);
                $state.transitionTo('app.administracion.crearPersonaNatural', sendParameters);
            };

            $scope.removeAccionista = function(index){
                $scope.$parent.view.accionistas.splice(index, 1);
                $scope.resetFocus();
            };

            $scope.$watch("view.numeroDocumento", function(){
                $scope.validarNumeroDocumentoAccionista();
            });
            $scope.$watch("view.idTipoDocumento", function(){
                $scope.validarNumeroDocumentoAccionista();
            });
            $scope.validarNumeroDocumentoAccionista = function(){
                if(!angular.isUndefined($scope.formAccionista.numeroDocumento)){
                    if(!angular.isUndefined($scope.view.numeroDocumento)){
                        if(!angular.isUndefined($scope.view.idTipoDocumento)){
                            var tipoDoc = $scope.getTipoDocumento();
                            if(!angular.isUndefined(tipoDoc)) {
                                if($scope.view.numeroDocumento.length == tipoDoc.numeroCaracteres) {
                                    $scope.formAccionista.numeroDocumento.$setValidity("sgmaxlength",true);
                                } else {$scope.formAccionista.numeroDocumento.$setValidity("sgmaxlength",false);}
                            } else {$scope.formAccionista.numeroDocumento.$setValidity("sgmaxlength",false);}
                        } else{$scope.formAccionista.numeroDocumento.$setValidity("sgmaxlength",false);}
                    } else {$scope.formAccionista.numeroDocumento.$setValidity("sgmaxlength",false);}}
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

            $scope.resetFocus = function(){
                $scope.formAccionista.$setPristine(false);
                $scope.control.submited = false;
                $scope.view = {
                    idTipoDocumento : undefined,
                    numeroDocumento : ''
                };
                angular.element(document.querySelector('#cmbTipoDocumentoAccionista')).focus();
            };

        }]);
});