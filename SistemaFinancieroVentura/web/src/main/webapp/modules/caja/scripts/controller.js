angular.module('cajaApp.controller', []);

angular.module('cajaApp.controller')
    .controller('cajaNavbarController', ['$scope', "$cookieStore",
        function($scope, $cookieStore) {
            $scope.caja = $cookieStore.get("caja");
            $scope.usuario = $cookieStore.get("usuario");
        }])

    .controller('AbrirCajaController', ['$scope', "$state", "$cookieStore", '$filter', "$dialogs", "CajaService",
        function($scope, $state, $cookieStore, $filter, $dialogs, CajaService) {

            $scope.currentAgencia = $cookieStore.get("agencia");
            $scope.currentCaja = $cookieStore.get("caja");

            $scope.buttonDisableState = function(){
                if($scope.progressTransaction == true)
                    return true;
                if($scope.currentCaja === undefined) {
                    return true;
                }
                else {
                    if($scope.alertMonedasDisableState())
                        return false;
                    else
                        return true;
                }
            }

            $scope.alertMonedasDisableState = function(){
                if($scope.myData.length > 0)
                    return true;
                else
                    return false;
            }


            $scope.abrirCaja = function () {
                $scope.progressTransaction = true;
                CajaService.abrir().then(
                    function(data){
                        $scope.progressTransaction = false;
                        $state.go("app.caja.voucherAbrirCaja")
                    },
                    function error(error){
                        $dialogs.error("Error al abrir caja",JSON.stringify(error.message));
                        $scope.progressTransaction = false;
                    }
                );
            };

            $scope.cancel = function () {

            };

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


        }])
    .controller('CerrarCajaController', ['$scope', "$state", "$cookieStore", '$filter', "$dialogs", "CajaService",
        function($scope, $state, $cookieStore, $filter, $dialogs, CajaService) {

            $scope.cerrarCaja = function () {
                $scope.progressTransaction = true;
                CajaService.cerrar($scope.detalleCajaFinal).then(
                    function(data){
                        $scope.progressTransaction = false;
                        $state.go("app.caja.voucherCerrarCaja")
                    },
                    function error(error){
                        $dialogs.error("Error al cerrar caja",JSON.stringify(error).message);
                        $scope.progressTransaction = false;
                    }
                );
            };

            CajaService.getDetalle().then(function(detalle){
                for(var i = 0; i<detalle.length; i++){
                    angular.forEach(detalle[i].calculadora, function(row){
                        row.subtotal = function(){
                            return this.valor * this.cantidad;
                        }
                    });
                }
                $scope.detalleCajaInicial = angular.copy(detalle);
                $scope.detalleCajaFinal = angular.copy(detalle);
            });

            $scope.currentAgencia = $cookieStore.get("agencia");
            $scope.currentCaja = $cookieStore.get("caja");
            $scope.buttonDisableState = function(){
                if($scope.progressTransaction == true)
                    return true;
                if($scope.currentCaja === undefined) {
                    return true;
                }
                else {
                    if($scope.alertMonedasDisableState())
                        return false;
                    else
                        return true;
                }
            }
            $scope.alertMonedasDisableState = function(){
                if($scope.myDataInicial.length > 0)
                    return true;
                else
                    return false;
            }


            $scope.myDataInicial = [];
            $scope.gridOptionsInicial = [];
            $scope.totalInicial = [];
            $scope.getTemplateInicial = function(index, simbolo){
                $scope.myDataInicial[index] = $scope.detalleCajaInicial[index].calculadora;
                $scope.gridOptionsInicial[index] = {
                    data: 'myDataInicial['+index+']',
                    multiSelect: false,
                    columnDefs: [
                        { field: "denominacion", displayName: "Denominacion", cellTemplate: "<div><div class='ngCellText'>{{simbolo}} {{row.getProperty(col.field)}}</div></div>" },
                        { field: "valor | currency : '' ", displayName: "Valor" },
                        { field: "cantidad", displayName: "Cantidad" },
                        { field: "subtotal() | currency : '' ", displayName: "Subtotal" }
                    ]
                };

                $scope.totalInicial[index] = function(){
                    var total = 0;
                    for(var i = 0; i < $scope.myDataInicial[index].length; i++){
                        total = total + ($scope.myDataInicial[index][i].valor * $scope.myDataInicial[index][i].cantidad);
                    }
                    return $filter('currency')(total," ")
                }

                return $scope.gridOptionsInicial[index];
            }


            $scope.myDataFinal = [];
            $scope.gridOptionsFinal = [];
            $scope.totalFinal = [];
            $scope.getTemplateFinal = function(index, simbolo){
                $scope.myDataFinal[index] = $scope.detalleCajaFinal[index].calculadora;
                $scope.gridOptionsFinal[index] = {
                    data: 'myDataFinal['+index+']',
                    multiSelect: false,
                    enableCellSelection: true,
                    enableRowSelection: false,
                    enableCellEditOnFocus: true,
                    columnDefs: [
                        { field: "denominacion", displayName: "Denominacion", cellTemplate: "<div><div class='ngCellText'>{{simbolo}} {{row.getProperty(col.field)}}</div></div>", enableCellEdit: false },
                        { field: "valor | currency : '' ", displayName: "Valor", enableCellEdit: false },
                        { field: "cantidad", displayName: "Cantidad", enableCellEdit: true },
                        { field: "subtotal() | currency : '' ", displayName: "Subtotal", enableCellEdit: false }
                    ]
                };

                $scope.totalFinal[index] = function(){
                    var total = 0;
                    for(var i = 0; i < $scope.myDataFinal[index].length; i++){
                        total = total + ($scope.myDataFinal[index][i].valor * $scope.myDataFinal[index][i].cantidad);
                    }
                    return $filter('currency')(total," ")
                }

                return $scope.gridOptionsFinal[index];
            }

        }]);