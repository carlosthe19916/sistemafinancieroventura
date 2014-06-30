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
            $scope.loadDepartamentos();
            $scope.loadProvincias = function(){
                if(!angular.isUndefined($scope.ubigeo.codigoDepartamento)){
                    MaestroService.getProvinciasByCodigo($scope.ubigeo.codigoDepartamento).then(function(data){
                        $scope.combos.provincias = data;
                    });
                } else {
                    $scope.combos.provincias = [];
                }
            };
            $scope.loadDistritos = function(){
                console.log("cargando distritos de:"+$scope.ubigeo.codigoProvincia);
                if(!angular.isUndefined($scope.ubigeo.codigoProvincia)){
                    MaestroService.getDistritosByCodigo($scope.ubigeo.codigoProvincia).then(function(data){
                        $scope.combos.distritos = data;
                    });
                } else {
                    $scope.combos.distritos = [];
                }
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

            $scope.$parent.$watch('view.ubigeo', function () {
                if (!angular.isUndefined($scope.$parent.view.ubigeo)) {
                    $scope.ubigeo.codigoDepartamento = $scope.$parent.view.ubigeo.substr(0,2);
                    $scope.ubigeo.codigoProvincia = $scope.$parent.view.ubigeo.substr(2,2);
                    $scope.ubigeo.codigoDistrito = $scope.$parent.view.ubigeo.substr(4,2);

                    console.log($scope.ubigeo.codigoDistrito);
                }
            },true);

            $scope.$watch('ubigeo.codigoDepartamento', function(){
                if(!angular.isUndefined($scope.ubigeo.codigoDepartamento))
                    $scope.loadProvincias();
            });
            $scope.$watch('ubigeo.codigoProvincia', function(){
                if(!angular.isUndefined($scope.ubigeo.codigoProvincia))
                    $scope.loadDistritos();
            });
            $scope.$watch('ubigeo.codigoDistrito', function(){
                $scope.initializeUbigeo();
            });

        }]);
});