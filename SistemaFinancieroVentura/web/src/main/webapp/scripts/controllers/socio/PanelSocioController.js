define(['../module'], function (controllers) {
    'use strict';
    controllers.controller("PanelSocioController", ['$scope', '$state','$location','$window','$modal','SocioService','MaestroService',
        function($scope, $state,$location,$window,$modal,SocioService,MaestroService) {

            $scope.loadSocio = function(){
                if(!angular.isUndefined($scope.id)){
                    SocioService.getSocio($scope.id).then(
                        function(data){
                            $scope.socio = data;
                        }, function error(error){
                            $scope.alerts = [{ type: "danger", msg: "Socio no encontrado."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                }
            };
            $scope.loadCuentaAporte = function(){
                if(!angular.isUndefined($scope.id)){
                    SocioService.getCuentaAporte($scope.id).then(
                        function(data){
                            $scope.cuentaAporte = data;
                        }, function error(error){
                            $scope.alerts = [{ type: "warning", msg: "Cuenta de aporte no encontrada."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                }
            };
            $scope.loadPersonaNatural = function(){
                if(!angular.isUndefined($scope.id)){
                    SocioService.getPersonaNatural($scope.id).then(function(data){
                        $scope.personaNatural = data;
                        var abreviaturaPais = $scope.personaNatural.codigoPais;
                        MaestroService.getPaisByAbreviatura(abreviaturaPais).then(function(pais){
                            $scope.pais = pais;
                        });
                    });
                }
            };
            $scope.loadPersonaJuridica = function(){
                if(!angular.isUndefined($scope.id)){
                    SocioService.getPersonaJuridica($scope.id).then(function(data){
                        $scope.personaJuridica = data;
                        var abreviaturaPais = $scope.personaJuridica.representanteLegal.codigoPais;
                        MaestroService.getPaisByAbreviatura(abreviaturaPais).then(function(pais){
                            $scope.pais = pais;
                        });
                    });
                }
            };
            $scope.loadApoderado = function(){
                if(!angular.isUndefined($scope.id)){
                    SocioService.getApoderado($scope.id).then(function(data){
                        $scope.apoderado = data;
                    });
                }
            };
            $scope.loadCuentasBancarias = function(){
                if(!angular.isUndefined($scope.id)){
                    SocioService.getCuentasBancarias($scope.id).then(function(data){
                        $scope.cuentasBancarias = data;
                    });
                }
            };

            $scope.loadSocio();
            $scope.loadCuentaAporte();
            $scope.loadPersonaNatural();
            $scope.loadPersonaJuridica();
            $scope.loadApoderado();
            $scope.loadCuentasBancarias();

            $scope.editarSocioPN = function(){
                var baseLen = $location.absUrl().length - $location.url().length;
                var url = $location.absUrl().substring(0, baseLen);
                $window.open(url + "/app/administracion/personaNatural/"+$scope.personaNatural.id);
            };
            $scope.editarSocioPJ = function(){
                var baseLen = $location.absUrl().length - $location.url().length;
                var url = $location.absUrl().substring(0, baseLen);
                $window.open(url + "/app/administracion/personaJuridica/"+$scope.personaJuridica.id);
            };
            $scope.editarRepresentantePJ = function(){
                var baseLen = $location.absUrl().length - $location.url().length;
                var url = $location.absUrl().substring(0, baseLen);
                $window.open(url + "/app/administracion/personaNatural/"+$scope.personaJuridica.representanteLegal.id);
            };
            $scope.editarApoderado = function(){
                if(!angular.isUndefined($scope.apoderado)){
                    var baseLen = $location.absUrl().length - $location.url().length;
                    var url = $location.absUrl().substring(0, baseLen);
                    $window.open(url + "/app/administracion/personaNatural/"+$scope.apoderado.id);
                }
            };
            $scope.cambiarApoderado = function(){
                if(!angular.isUndefined($scope.socio)){
                    var modalInstance = $modal.open({
                        templateUrl: 'views/cajero/socio/apoderadoPopUp.html',
                        controller: "ApoderadoPopUpController"
                    });
                    modalInstance.result.then(function (result) {
                        var apoderado = {
                            "idTipoDocumento" : result.tipoDocumento.id,
                            "numeroDocumento": result.numeroDocumento
                        };
                        SocioService.cambiarApoderado($scope.socio.id, apoderado).then(
                            function(data){
                                $scope.loadApoderado();
                                $scope.alerts = [{ type: "success", msg: "Apoderado cambiado." }];
                                $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                            }, function error(error){
                                $scope.alerts = [{ type: "danger", msg: "Error:" + error.data.message +"." }];
                                $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                                $window.scrollTo(0,0);
                            }
                        );
                    }, function () {
                    });
                }
            };
            $scope.eliminarApoderado = function(){
                if(!angular.isUndefined($scope.socio)){
                    var modalInstance = $modal.open({
                        templateUrl: 'views/cajero/util/confirmPopUp.html',
                        controller: "ConfirmPopUpController"
                    });
                    modalInstance.result.then(function (result) {
                        SocioService.eliminarApoderado($scope.socio.id).then(
                            function(data){
                                $scope.apoderado = undefined;
                                $scope.alerts = [{ type: "success", msg: "Socio inactivado eliminado." }];
                                $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                            }, function error(error){
                                $scope.alerts = [{ type: "danger", msg: "Error:" + error.data.message +"." }];
                                $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                                $window.scrollTo(0,0);
                            }
                        );
                    }, function () {
                    });
                }
            };
            $scope.editarCuentaBancaria = function(index){
                if(!angular.isUndefined($scope.cuentasBancarias)){
                    $state.transitionTo("app.socio.editarCuentaBancaria", { id: $scope.cuentasBancarias[index].id });
                }
            };

            $scope.congelarCuentaAporte = function(){
                if(!angular.isUndefined($scope.socio)){
                    SocioService.congelarCuentaAporte($scope.socio.id).then(
                        function(data){
                            $scope.loadCuentaAporte();
                            $scope.alerts = [{ type: "success", msg: "Cuenta de aportes congelada."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        },
                        function error(error){
                            $scope.alerts = [{ type: "warning", msg: "Error al congelar cuenta, intente nuevamente."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                }
            };
            $scope.descongelarCuentaAporte = function(){
                if(!angular.isUndefined($scope.socio)){
                    SocioService.descongelarCuentaAporte($scope.socio.id).then(
                        function(data){
                            $scope.loadCuentaAporte();
                            $scope.alerts = [{ type: "success", msg: "Cuenta de aportes descongelada."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        },
                        function error(error){
                            $scope.alerts = [{ type: "warning", msg: "Error al descongelar cuenta, intente nuevamente."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                }
            };
            $scope.inactivarSocio = function(){
                if(!angular.isUndefined($scope.socio)){
                    var modalInstance = $modal.open({
                        templateUrl: 'views/cajero/util/confirmPopUp.html',
                        controller: "ConfirmPopUpController"
                    });
                    modalInstance.result.then(function (result) {
                        SocioService.inactivarSocio($scope.socio.id).then(
                            function(data){
                                $scope.loadSocio();
                                $scope.loadCuentaAporte();
                                $scope.alerts = [{ type: "success", msg: "Socio inactivado eliminado." }];
                                $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                            }, function error(error){
                                $scope.alerts = [{ type: "danger", msg: "Error:" + error.data.message +"." }];
                                $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                                $window.scrollTo(0,0);
                            }
                        );
                    }, function () {
                    });
                }
            };

        }]);
});