angular.module('cajaApp.controller', []);

angular.module('cajaApp.controller')
    .controller('cajaStatusController', ['$scope', "CajaService",
        function($scope, CajaService) {

            $scope.caja = CajaService.getCurrentCaja();

        }])

    .controller('AbrirCajaController', ['$scope', '$filter', "CajaService",
        function($scope, $filter, CajaService) {

            CajaService.getDetalle().then(function(detalle){
                $scope.detalleCaja = detalle;
                for(var i = 0; i<$scope.detalleCaja.length; i++){
                    angular.forEach($scope.detalleCaja[i].calculadora, function(row){
                        row.subtotal = function(){
                            return this.valor * this.cantidad;
                        }
                    });
                }
            });

            $scope.myData = [];
            $scope.total = [];

            $scope.getTemplate = function(moneda, detalle, index){
                $scope.myData[index] = detalle;

                $scope.totalData = 0;
                for(var i = 0; i < detalle.length; i++){
                    $scope.totalData = $scope.totalData + (detalle[i].valor * detalle[i].cantidad);
                }
                $scope.total[index] = $filter('currency')($scope.totalData, moneda.simbolo+" ") ;

                $scope.gridOptions = {
                    data: 'myData['+index+']',
                    multiSelect: false,
                    columnDefs: [
                        { field: "denominacion", displayName: "Denominacion", cellTemplate: "<div><div class='ngCellText'>{{detalle.moneda.simbolo}} {{row.getProperty(col.field)}}</div></div>" },
                        { field: "valor | currency : '' ", displayName: "Valor" },
                        { field: "cantidad", displayName: "Cantidad" },
                        { field: "subtotal() | currency : '' ", displayName: "Subtotal" }
                    ]
                };
                return $scope.gridOptions;
            }


        }]);