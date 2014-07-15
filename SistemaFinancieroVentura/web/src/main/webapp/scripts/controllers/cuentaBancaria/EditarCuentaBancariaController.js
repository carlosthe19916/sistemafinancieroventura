define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('EditarCuentaBancariaController', [ "$scope", "$state", "$location", "$filter", "$window", "focus","$modal",
        "MaestroService", "PersonaNaturalService", "PersonaJuridicaService", "SocioService", "CuentaBancariaService", "BeneficiarioService","TitularService","RedirectService",
        function($scope, $state, $location, $filter, $window, focus,$modal,
                 MaestroService, PersonaNaturalService, PersonaJuridicaService, SocioService, CuentaBancariaService, BeneficiarioService,TitularService,RedirectService) {

            $scope.viewState = "app.socio.editarCuentaBancaria";

            $scope.alerts = [];
            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};

            $scope.control = {
              "beneficiario": {"success": false, "message": undefined},
              "titular": {"success": false, "message": undefined}
            };

            $scope.login = {
                result: false
            };

            $scope.loadRedireccion = function(){
                if(RedirectService.haveNext()){
                    var state = RedirectService.getNextState();
                    if(state == $scope.viewState){
                        $scope.view = RedirectService.getNextObject();
                        RedirectService.clearLast();
                    }
                }
            };

            //cargar datos
            $scope.loadCuentaBancaria = function(){
                if(!angular.isUndefined($scope.id)){
                    CuentaBancariaService.getCuentasBancaria($scope.id).then(
                        function(data){
                            $scope.cuentaBancaria = data;
                        }, function error(error){
                            $scope.cuentaBancaria = undefined;
                            $scope.alerts.push({ type: "danger", msg: "Cuenta bancaria no encontrada."});
                        }
                    );
                }
            };
            $scope.loadSocio = function(){
                if(!angular.isUndefined($scope.id)){
                    CuentaBancariaService.getSocio($scope.id).then(
                        function(data){
                            $scope.socio = data;
                        }, function error(error){
                            $scope.socio = undefined;
                            $scope.alerts.push({ type: "danger", msg: "Socio no encontrado."});
                        }
                    );
                };
            };
            $scope.loadBeneficiarios = function(){
                if(!angular.isUndefined($scope.id)){
                    CuentaBancariaService.getBeneficiarios($scope.id).then(
                        function(data){
                            $scope.beneficiarios = data;
                        }, function error(error){
                            $scope.beneficiarios = undefined;
                            $scope.alerts.push({ type: "danger", msg: "Beneficiarios no encontrados."});
                        }
                    );
                };
            };
            $scope.loadTitulares = function(){
                if(!angular.isUndefined($scope.id)){
                    CuentaBancariaService.getTitulares($scope.id).then(
                        function(data){
                            $scope.titulares = data;
                        }, function error(error){
                            $scope.titulares = undefined;
                            $scope.alerts.push({ type: "danger", msg: "Titulares no encontrados."});
                        }
                    );
                };
            };
            $scope.loadEstadoCuenta = function(){
                if(!angular.isUndefined($scope.id)){
                    CuentaBancariaService.getEstadoCuenta($scope.id).then(
                        function(data){
                            $scope.transacciones = data;
                        }, function error(error){
                            $scope.transacciones = undefined;
                            $scope.alerts.push({ type: "danger", msg: "Estado de cuenta no encontrado."});
                        }
                    );
                };
            };

            $scope.loadSocio();
            $scope.loadCuentaBancaria();
            $scope.loadBeneficiarios();
            $scope.loadTitulares();
            $scope.loadEstadoCuenta();

            $scope.estadoCuentaSearcher = false;
            $scope.today = function() { $scope.desde = new Date(); $scope.hasta = new Date(); };
            $scope.today();
            $scope.openDesde = function($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.openedDesde = true;
            };
            $scope.openHasta = function($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.openedHasta = true;
            };
            $scope.dateOptions = {formatYear: 'yy',startingDay: 1};
            $scope.formats = ['dd/MM/yyyy','dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
            $scope.format = $scope.formats[0];

            $scope.changeEstadoCuentaSearcher = function(){
                $scope.estadoCuentaSearcher = !$scope.estadoCuentaSearcher;
            };
            $scope.buscarEstadoCuenta = function(){
                CuentaBancariaService.getEstadoCuenta($scope.id, $scope.desde.getTime(),$scope.hasta.getTime()).then(
                    function(data){
                        $scope.transacciones = data;
                    }, function error(error){
                        $scope.transacciones = undefined;
                        $scope.alerts.push({ type: "danger", msg: "Estado de cuenta no encontrado."});
                    }
                );
            };

            //editar persona socio
            $scope.editarSocioPersonaNatural = function(){
                var savedParameters = {
                    id: $scope.id
                };
                var sendParameters = {
                    id: $scope.socio.personaNatural.id
                };
                var nextState = $scope.viewState;
                RedirectService.addNext(nextState, savedParameters);
                $state.transitionTo('app.administracion.editarPersonaNatural', sendParameters);
            };

            $scope.editarSocioPersonaJuridica = function(){
                var savedParameters = {
                    id: $scope.id
                };
                var sendParameters = {
                    id: $scope.socio.personaJuridica.id
                };
                var nextState = $scope.viewState;
                RedirectService.addNext(nextState, savedParameters);
                $state.transitionTo('app.administracion.editarPersonaJuridica', sendParameters);
            };

            //cuenta bancaria
            $scope.congelarCuentaBancaria = function(){
                if(!angular.isUndefined($scope.cuentaBancaria)){
                    CuentaBancariaService.congelarCuentaBancaria($scope.cuentaBancaria.id).then(
                        function(data){
                            $scope.loadCuentaBancaria();
                            $scope.alerts = [{ type: "success", msg: "Cuenta bancaria congelada."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        },
                        function error(error){
                            $scope.alerts = [{ type: "warning", msg: "Error:"+error.data.message+"."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                }
            };
            $scope.descongelarCuentaBancaria = function(){
                if(!angular.isUndefined($scope.cuentaBancaria)){
                    CuentaBancariaService.descongelarCuentaBancaria($scope.cuentaBancaria.id).then(
                        function(data){
                            $scope.loadCuentaBancaria();
                            $scope.alerts = [{ type: "success", msg: "Cuenta bancaria descongelada."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        },
                        function error(error){
                            $scope.alerts = [{ type: "warning", msg: "Error:"+error.data.message+"."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                }
            };
            //cuenta a plazo fijo carlos
            $scope.recalcularPlazoFijo = function(){
                $scope.openLoginPopUp('app.socio.recalcularPlazoFijo');
            };

            $scope.renovarPlazoFijo = function(){
                $scope.openLoginPopUp('app.socio.renovarPlazoFijo');
            };

            $scope.openLoginPopUp = function (paginaSiguiente) {
                var modalInstance = $modal.open({
                    templateUrl: 'views/cajero/util/loginPopUp.html',
                    controller: "LoginPopUpController"
                });
                modalInstance.result.then(function (result) {
                    $scope.login.result = result;

                    //redireccionar
                    var savedParameters = {
                        id: $scope.id
                    };
                    var sendParameters = {
                        id: $scope.id
                    };
                    var nextState = $scope.viewState;
                    RedirectService.addNext(nextState, savedParameters);
                    $state.transitionTo(paginaSiguiente, sendParameters);
                }, function () {
                });
            };

            $scope.cancelarCuentaBancaria = function(){
                if(!angular.isUndefined($scope.cuentaBancaria)){
                    if($scope.cuentaBancaria.estado != 'ACTIVO'){
                        $scope.alerts = [{ type: "warning", msg: "Error: La cuenta debe de estar activa."}];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        return;
                    }
                    $state.transitionTo("app.socio.cancelarCuentaBancaria", { id: $scope.id });
                }
            };

            //titulares
            $scope.goToFirmas = function(){
                $state.transitionTo("app.socio.firmasCuentaBancaria", { id: $scope.id });
            };

            $scope.editTitular = function(index){
                var savedParameters = {
                    id: $scope.id
                };
                var sendParameters = {
                    id: $scope.titulares[index].personaNatural.id
                };
                var nextState = $scope.viewState;
                RedirectService.addNext(nextState, savedParameters);
                $state.transitionTo('app.administracion.editarPersonaNatural', sendParameters);
            };

            $scope.addTitular = function(){
                var modalInstance = $modal.open({
                    templateUrl: 'views/cajero/cuentaBancaria/titularPopUp.html',
                    controller: "TitularPopUpController"
                });
                modalInstance.result.then(function (result) {
                    var titular = {"idTipoDocumento" : result.tipoDocumento.id,"numeroDocumento": result.numeroDocumento};

                    TitularService.crearTitular($scope.id, titular).then(
                        function(data){
                            TitularService.getTitular(data.id).then(function(titular){
                                $scope.titulares.push(titular);
                            });
                            $scope.alerts = [{ type: "success", msg: "Titular creado." }];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                            $scope.control.titular.success = true;
                            $scope.control.titular.message = '<span class="label label-success">Creado</span>';
                        }, function error(error){
                            $scope.alerts = [{ type: "danger", msg: "Error:" + error.data.message +"." }];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                            $scope.control.titular.success = false;
                            $scope.control.titular.message = '';
                            $window.scrollTo(0,0);
                        }
                    );
                }, function () {
                });
            };
            $scope.deleteTitular = function(index){
                var modalInstance = $modal.open({
                    templateUrl: 'views/cajero/util/confirmPopUp.html',
                    controller: "ConfirmPopUpController"
                });
                modalInstance.result.then(function (result) {
                    TitularService.eliminarTitular($scope.titulares[index].id).then(
                        function(data){
                            $scope.alerts = [{ type: "success", msg: "Titular eliminado." }];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                            $scope.control.titular.success = true;
                            $scope.titulares.splice(index, 1);
                            $scope.control.titular.message = '<span class="label label-success">Eliminado</span>';
                        }, function error(error){
                            $scope.alerts = [{ type: "danger", msg: "Error:" + error.data.message +"." }];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                            $scope.control.titular.success = false;
                            $scope.control.titular.message = '';
                            $window.scrollTo(0,0);
                        }
                    );
                }, function () {
                });
            };

            //beneficiarios
            $scope.addBeneficiario = function(){
                var modalInstance = $modal.open({
                    templateUrl: 'views/cajero/cuentaBancaria/beneficiarioPopUp.html',
                    controller: "BeneficiarioPopUpController",
                    resolve: {
                        total: function () {
                            var tot = 0;
                            if(!angular.isUndefined($scope.beneficiarios))
                                for(var i = 0; i < $scope.beneficiarios.length; i++)
                                    tot = tot + $scope.beneficiarios[i].porcentajeBeneficio;
                            return tot;
                        },
                        obj: function(){
                            return undefined;
                        }
                    }
                });
                modalInstance.result.then(function (result) {
                    BeneficiarioService.crearBeneficiario($scope.id, result).then(
                        function(data){
                            BeneficiarioService.getBeneficiario(data.id).then(function(beneficiario){
                                $scope.beneficiarios.push(beneficiario);
                            });
                            $scope.alerts = [{ type: "success", msg: "Beneficiario creado." }];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                            $scope.control.beneficiario.success = true;
                            $scope.control.beneficiario.message = '<span class="label label-success">Creado</span>';
                        }, function error(error){
                            $scope.alerts = [{ type: "danger", msg: "Error:" + error.data.message +"." }];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                            $scope.control.beneficiario.success = false;
                            $scope.control.beneficiario.message = '';
                            $window.scrollTo(0,0);
                        }
                    );
                }, function () {
                });
            };
            $scope.deleteBeneficiario = function(index){
                var modalInstance = $modal.open({
                    templateUrl: 'views/cajero/util/confirmPopUp.html',
                    controller: "ConfirmPopUpController"
                });
                modalInstance.result.then(function (result) {
                    BeneficiarioService.eliminarBeneficiario($scope.beneficiarios[index].id).then(
                        function(data){
                            $scope.alerts = [{ type: "success", msg: "Beneficiario eliminado." }];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                            $scope.control.beneficiario.success = true;
                            $scope.beneficiarios.splice(index, 1);
                            $scope.control.beneficiario.message = '<span class="label label-success">Eliminado</span>';
                        }, function error(error){
                            $scope.alerts = [{ type: "danger", msg: "Error:" + error.data.message +"." }];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                            $scope.control.beneficiario.success = false;
                            $scope.control.beneficiario.message = '';
                            $window.scrollTo(0,0);
                        }
                    );
                }, function () {
                });
            };
            $scope.editBeneficiario = function(index){
                var modalInstance = $modal.open({
                    templateUrl: 'views/cajero/cuentaBancaria/beneficiarioPopUp.html',
                    controller: "BeneficiarioPopUpController",
                    resolve: {
                        total: function () {
                            var tot = 0;
                            if(!angular.isUndefined($scope.beneficiarios))
                                for(var i = 0; i < $scope.beneficiarios.length; i++)
                                    tot = tot + $scope.beneficiarios[i].porcentajeBeneficio;
                            return tot;
                        },
                        obj: function(){
                            return $scope.beneficiarios[index];
                        }
                    }
                });
                modalInstance.result.then(function (result) {
                    BeneficiarioService.actualizarBeneficiario(result).then(
                        function(data){
                            $scope.beneficiarios.splice(index, 1);
                            $scope.beneficiarios.push(result);
                            $scope.alerts = [{ type: "success", msg: "Beneficiario creado." }];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                            $scope.control.beneficiario.success = true;
                            $scope.control.beneficiario.message = '<span class="label label-success">Actualizado</span>';
                        }, function error(error){
                            $scope.alerts = [{ type: "danger", msg: "Error:" + error.data.message +"." }];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                            $scope.control.beneficiario.success = false;
                            $scope.control.beneficiario.message = '';
                            $window.scrollTo(0,0);
                        }
                    );
                }, function () {
                });
            };


            $scope.salir = function(){
                $scope.redireccion();
            };

            $scope.redireccion = function(){
                if(RedirectService.haveNext()){
                    var nextState = RedirectService.getNextState();
                    var parametros = RedirectService.getNextParamsState();
                    $state.transitionTo(nextState,parametros);
                } else {
                    $state.transitionTo('app.socio.buscarCuentaBancaria');
                }
            };

            $scope.imprimirCertificado = function(){
              $window.open('http://localhost:8080/SistemaFinancieroVentura-web/services/cuentaBancaria/'+
                  $scope.id+'/certificado');
            };

        }]);
});