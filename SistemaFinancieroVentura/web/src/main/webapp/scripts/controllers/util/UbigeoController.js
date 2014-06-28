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

            $scope.getDepartamento = function(){
                if(!angular.isUndefined($scope.ubigeo.codigoDepartamento)){
                    for(var i = 0 ; i < $scope.combos.departamentos.length; i++){
                        if($scope.ubigeo.codigoDepartamento == $scope.combos.departamentos[i].codigo){
                            return $scope.combos.departamentos[i];
                        }
                    }
                }
                return undefined;
            };
            $scope.getProvincia = function(){
                if(!angular.isUndefined($scope.ubigeo.codigoProvincia)){
                    for(var i = 0 ; i < $scope.combos.provincias.length; i++){
                        if($scope.ubigeo.codigoProvincia == $scope.combos.provincias[i].codigo)
                            return $scope.combos.provincias[i];
                    }
                }
                return undefined;
            };
            $scope.getDistrito = function(){
                if(!angular.isUndefined($scope.ubigeo.codigoDistrito)){
                    for(var i = 0 ; i < $scope.combos.distritos.length; i++){
                        if($scope.ubigeo.codigoDistrito == $scope.combos.distritos[i].codigo)
                            return $scope.combos.distritos[i];
                    }
                }
                return undefined;
            };

            //metodos para cargar los combos
            $scope.loadDepartamentos = function(){
                MaestroService.getDepartamentos().then(function(data){
                    $scope.combos.departamentos = data;
                });
            };
            $scope.loadDepartamentos();
            $scope.loadProvincias = function(){
                var departamentoSelected = $scope.getDepartamento();
                if(!angular.isUndefined(departamentoSelected)){
                    MaestroService.getProvincias(departamentoSelected.id).then(function(data){
                        $scope.combos.provincias = data;
                    });
                } else {
                    $scope.combos.provincias = [];
                }
            };
            $scope.loadDistritos = function(){
                var provinciaSelected = $scope.getProvincia();
                if(!angular.isUndefined(provinciaSelected)){
                    MaestroService.getDistritos(provinciaSelected.id).then(function(data){
                        $scope.combos.distritos = data;
                    });
                } else {
                    $scope.combos.distritos = [];
                }
            };

            //metodos de cambio
            $scope.changeDepartamento = function(){
                $scope.loadProvincias();
            };
            $scope.changeProvincia = function(){
                $scope.loadDistritos();
            };
            $scope.changeDistrito = function(){
                $scope.initializeUbigeo();
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
                    setTimeout(function () {
                        $scope.loadProvincias();
                    }, 1000);
                    setTimeout(function () {
                        $scope.loadDistritos();
                    }, 2000);
                }
            },true);

        }]);
});