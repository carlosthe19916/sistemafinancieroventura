define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('EditarCuentaBancariaController', [ "$scope", "$state", "$location", "$filter", "$window", "focus","$modal", "MaestroService", "PersonaNaturalService", "PersonaJuridicaService", "SocioService", "CuentaBancariaService", "BeneficiarioService",
        function($scope, $state, $location, $filter, $window, focus,$modal, MaestroService, PersonaNaturalService, PersonaJuridicaService, SocioService, CuentaBancariaService, BeneficiarioService) {

            $scope.alerts = [];
            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};

            $scope.control = {
              "beneficiario": {"success": false, "message": undefined}
            };

            //cargar datos
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
            if(!angular.isUndefined($scope.id)){
                CuentaBancariaService.getSocio($scope.id).then(
                    function(data){
                        $scope.socio = data;
                    }, function error(error){
                        $scope.socio = undefined;
                        $scope.alerts.push({ type: "danger", msg: "Socio no encontrado."});
                    }
                );
            }
            if(!angular.isUndefined($scope.id)){
                CuentaBancariaService.getBeneficiarios($scope.id).then(
                    function(data){
                        $scope.beneficiarios = data;
                    }, function error(error){
                        $scope.beneficiarios = undefined;
                        $scope.alerts.push({ type: "danger", msg: "Beneficiarios no encontrados."});
                    }
                );
            }
            if(!angular.isUndefined($scope.id)){
                CuentaBancariaService.getTitulares($scope.id).then(
                    function(data){
                        $scope.titulares = data;
                    }, function error(error){
                        $scope.titulares = undefined;
                        $scope.alerts.push({ type: "danger", msg: "Titulares no encontrados."});
                    }
                );
            }

            //editar persona socio
            $scope.editarSocioPersonaNatural = function(){
                var baseLen = $location.absUrl().length - $location.url().length;
                var url = $location.absUrl().substring(0, baseLen);
                $window.open(url + "/app/socio/personaNatural/" + $scope.id);
            }

            $scope.editarSocioPersonaJuridica = function(){
                var baseLen = $location.absUrl().length - $location.url().length;
                var url = $location.absUrl().substring(0, baseLen);
                $window.open(url + "/app/socio/personaJuridica/" + $scope.id);
            }

            //cuenta bancaria



            //titulares
            $scope.addTitular = function(){
                var modalInstance = $modal.open({
                    templateUrl: 'views/cajero/cuentaBancaria/titularPopUp.html',
                    controller: "TitularPopUpController"
                });
                modalInstance.result.then(function (result) {
                    
                }, function () {

                });
            }


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
            }
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
            }
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
            }

        }]);
});