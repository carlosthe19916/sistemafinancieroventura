define(['../../module'], function (controllers) {
    'use strict';
    controllers.controller('EditarPersonaNaturalController', ['$scope', '$state','$stateParams','$window', 'MaestroService', 'PersonaNaturalService','RedirectService',
        function($scope,$state,$stateParams,$window,MaestroService,PersonaNaturalService,RedirectService) {

            $scope.control = {
                "success":false,
                "inProcess": false,
                "submitted" : false
            };

            $scope.combo = {
                paises: undefined,
                tipoDocumentos: undefined,
                sexos: undefined,
                estadosCiviles: undefined
            };

            $scope.view = {
                "id":undefined,
                "idTipoDocumento":undefined,
                "numeroDocumento":undefined,
                "apellidoPaterno":undefined,
                "apellidoMaterno":undefined,
                "nombres":undefined,
                "fechaNacimiento":undefined,
                "sexo":undefined,
                "estadoCivil":undefined,
                "ocupacion":undefined,
                "direccion":undefined,
                "referencia":undefined,
                "telefono":undefined,
                "celular":undefined,
                "email":undefined,
                "ubigeo":undefined,
                "codigoPais":undefined
            };

            $scope.dateOptions = {
                formatYear: 'yyyy',
                startingDay: 1
            };

            $scope.open = function($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.opened = true;
            };

            $scope.$watch("view.numeroDocumento",function (newVal, oldVal) {
                if (newVal !== oldVal) {
                    $scope.validarNumeroDocumento();
                }
            },true);
            $scope.validarNumeroDocumento = function(){
                if(!angular.isUndefined($scope.formEditarPersonanatural.numeroDocumento)){
                    if(!angular.isUndefined($scope.view.numeroDocumento)){
                        if(!angular.isUndefined($scope.view.idTipoDocumento)){
                            var tipoDoc = $scope.getTipoDocumento();
                            if(!angular.isUndefined(tipoDoc)) {
                                if($scope.view.numeroDocumento.length == tipoDoc.numeroCaracteres) {
                                    $scope.formEditarPersonanatural.numeroDocumento.$setValidity("sgmaxlength",true);
                                } else {$scope.formEditarPersonanatural.numeroDocumento.$setValidity("sgmaxlength",false);}
                            } else {$scope.formEditarPersonanatural.numeroDocumento.$setValidity("sgmaxlength",false);}
                        } else{$scope.formEditarPersonanatural.numeroDocumento.$setValidity("sgmaxlength",false);}
                    } else {$scope.formEditarPersonanatural.numeroDocumento.$setValidity("sgmaxlength",false);}}
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

            $scope.loadTipoDocumentoPN = function(){
                MaestroService.getTipoDocumentoPN().then(function(data){
                    $scope.combo.tipoDocumentos = data;
                });
            };
            $scope.loadSexos = function(){
                MaestroService.getSexos().then(function(data){
                    $scope.combo.sexos = data;
                });
            };
            $scope.loadEstadosCiviles = function(){
                MaestroService.getEstadosciviles().then(function(data){
                    $scope.combo.estadosCiviles = data;
                });
            };
            $scope.loadPaises = function(){
                MaestroService.getPaises().then(function(data){
                    $scope.combo.paises = data;
                });
            };

            $scope.loadTipoDocumentoPN();
            $scope.loadSexos();
            $scope.loadEstadosCiviles();
            $scope.loadPaises();

            $scope.loadTipoDocumentoPN();
            $scope.loadSexos();
            $scope.loadEstadosCiviles();
            $scope.loadPaises();

            $scope.loadPersonaNatural = function(){
                if(!angular.isUndefined($scope.id)){
                    PersonaNaturalService.findById($scope.id).then(
                        function(data){
                            $scope.view.id = data.id;
                            $scope.view.idTipoDocumento = data.tipoDocumento.id;
                            $scope.view.numeroDocumento = data.numeroDocumento;
                            $scope.view.apellidoPaterno = data.apellidoPaterno;
                            $scope.view.apellidoMaterno = data.apellidoMaterno;
                            $scope.view.nombres = data.nombres;
                            $scope.view.fechaNacimiento = data.fechaNacimiento;
                            $scope.view.sexo = data.sexo;
                            $scope.view.estadoCivil = data.estadoCivil;
                            $scope.view.ocupacion = data.ocupacion;
                            $scope.view.direccion = data.direccion;
                            $scope.view.referencia = data.referencia;
                            $scope.view.telefono = data.telefono;
                            $scope.view.celular = data.celular;
                            $scope.view.email = data.email;
                            $scope.view.ubigeo = data.ubigeo;
                            $scope.view.codigoPais = data.codigoPais;
                        }, function error(error){
                            $scope.alerts = [{ type: "danger", msg: "Error: No se pudo cargar la persona."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                };
            };
            $scope.loadPersonaNatural();

            //logic
            $scope.crearTransaccion = function(){
                if ($scope.formEditarPersonanatural.$valid) {
                    $scope.control.inProcess = true;

                    var personaTransaccion = PersonaNaturalService.getModel();
                    personaTransaccion.id = $scope.view.id;
                    personaTransaccion.tipoDocumento = {
                        id: $scope.view.idTipoDocumento
                    };
                    personaTransaccion.numeroDocumento = $scope.view.numeroDocumento;
                    personaTransaccion.apellidoPaterno = $scope.view.apellidoPaterno;
                    personaTransaccion.apellidoMaterno = $scope.view.apellidoMaterno;
                    personaTransaccion.nombres = $scope.view.nombres;
                    personaTransaccion.fechaNacimiento = $scope.view.fechaNacimiento;
                    personaTransaccion.sexo = $scope.view.sexo;
                    personaTransaccion.estadoCivil = $scope.view.estadoCivil;
                    personaTransaccion.ocupacion = $scope.view.ocupacion;
                    personaTransaccion.direccion = $scope.view.direccion;
                    personaTransaccion.referencia = $scope.view.referencia;
                    personaTransaccion.telefono = $scope.view.telefono;
                    personaTransaccion.celular = $scope.view.celular;
                    personaTransaccion.email = $scope.view.email;
                    personaTransaccion.ubigeo = $scope.view.ubigeo;
                    personaTransaccion.codigoPais = $scope.view.codigoPais;

                    PersonaNaturalService.update(personaTransaccion).then(
                        function(persona){
                            $scope.redireccion();
                        },
                        function error(error){
                            $scope.control.inProcess = false;
                            $scope.control.success = false;
                            $scope.alerts = [{ type: "danger", msg: "Error: No se pudo cargar la persona."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                } else {
                    $scope.control.submitted = true;
                }
            };

            $scope.redireccion = function(){
                if(RedirectService.haveNext()){
                    var nextState = RedirectService.getNextState();
                    $state.transitionTo(nextState);
                } else {
                    $state.transitionTo('app.administracion.buscarPersonaNatural');
                }
            };

            $scope.cancel = function () {
                $scope.redireccion();
            };

            $scope.buttonDisableState = function(){
                return $scope.control.inProcess;
            };

        }]);
});