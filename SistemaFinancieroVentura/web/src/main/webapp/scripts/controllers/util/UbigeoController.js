define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('UbigeoController', ['$scope','MaestroService',
        function($scope,MaestroService) {

            $scope.ubigeo = {
              codigoDepartamento: undefined,
              codigoProvincia: undefined,
              codigoDistrito: undefined,
              codigo:''
            };

            $scope.combos = {
              departamentos: undefined,
              provincias: undefined,
              distritos: undefined
            };

            //metodos para cargar los combos
            $scope.loadDepartamentos = function(){
                MaestroService.getDepartamentos().then(function(data){
                    $scope.combos.departamentos = data;
                });
            };
            $scope.loadProvincias = function(){
                if(!angular.isUndefined($scope.ubigeo.codigoDepartamento)){
                    MaestroService.getProvinciasByCodigo($scope.ubigeo.codigoDepartamento).then(function(data){
                        $scope.combos.provincias = data;
                    });
                } else {
                    $scope.clearProvincias();
                }
            };
            $scope.loadDistritos = function(){
                if(!angular.isUndefined($scope.ubigeo.codigoProvincia)){
                    MaestroService.getDistritosByCodigo($scope.ubigeo.codigoDepartamento, $scope.ubigeo.codigoProvincia).then(function(data){
                        $scope.combos.distritos = data;
                    });
                } else {
                    $scope.clearDistritos();
                }
            };

            $scope.clearProvincias = function(){
                $scope.combos.provincias = [];
            };
            $scope.clearDistritos = function(){
                $scope.combos.distritos = [];
            };

            $scope.initializeUbigeo = function(){
                var ubigeo = '';
                if(!angular.isUndefined($scope.ubigeo.codigoDepartamento)){
                    ubigeo = $scope.ubigeo.codigoDepartamento;
                    if(!angular.isUndefined($scope.ubigeo.codigoProvincia)){
                        ubigeo = ubigeo + $scope.ubigeo.codigoProvincia;
                        if(!angular.isUndefined($scope.ubigeo.codigoDistrito)){
                            ubigeo = ubigeo + $scope.ubigeo.codigoDistrito;

                            //poner ubigeo al objeto padre
                            $scope.$parent.view.ubigeo = ubigeo;
                        }
                    }
                }
                //objeto local
                $scope.ubigeo.codigo = ubigeo;
            };

            $scope.loadDepartamentos();

            $scope.$parent.$watch('view.ubigeo', function () {
                if (!angular.isUndefined($scope.$parent.view.ubigeo)) {
                    $scope.ubigeo.codigoDepartamento = $scope.$parent.view.ubigeo.substr(0,2);
                    $scope.ubigeo.codigoProvincia = $scope.$parent.view.ubigeo.substr(2,2);
                    $scope.ubigeo.codigoDistrito = $scope.$parent.view.ubigeo.substr(4,2);

                    $scope.loadProvincias();
                    $scope.loadDistritos();
                }
            },true);

            $scope.changeDepartamento = function(){
                $scope.loadProvincias();
                $scope.ubigeo.codigoProvincia = undefined;
                $scope.ubigeo.codigoDistrito = undefined;
            };
            $scope.changeProvincia = function(){
                $scope.loadDistritos();
                $scope.ubigeo.codigoDistrito = undefined;
            };
            $scope.changeDistrito = function(){
                $scope.initializeUbigeo();
            };

        }]);
});