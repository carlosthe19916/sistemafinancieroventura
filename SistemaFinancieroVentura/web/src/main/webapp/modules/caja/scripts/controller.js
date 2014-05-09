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

            $scope.detalleCaja = [];
            $scope.alertMonedasDisableState = function(){
                return $scope.detalleCaja.length != 0;
            }

            if($scope.currentCaja.abierto == false){

                CajaService.getDetalle().then(function(detalleCaja){
                    for(var i = 0; i<detalleCaja.length; i++){
                        angular.forEach(detalleCaja[i].detalle, function(row){
                            row.subtotal = function(){
                                return this.valor * this.cantidad;
                            }
                        });
                    }
                    $scope.detalleCaja = angular.copy(detalleCaja);
                });

                $scope.myData = [];
                $scope.gridOptions = [];
                $scope.total = [];
                $scope.getTemplate = function(index, simbolo){
                    $scope.myData[index] = $scope.detalleCaja[index].detalle;
                    $scope.gridOptions[index] = {
                        data: 'myData['+index+']',
                        multiSelect: false,
                        columnDefs: [
                            //{ field: "valor", displayName: "Denominacion", cellTemplate: "<div><div class='ngCellText'>simbolo {{row.getProperty(col.field)}}</div></div>" },
                            { field: "valor | currency : '"+simbolo+ " '", displayName: "Valor" },
                            { field: "cantidad", displayName: "Cantidad" },
                            { field: "subtotal() | currency : '' ", displayName: "Subtotal" }
                        ]
                    };
                    $scope.total[index] = function(){
                        var total = 0;
                        for(var i = 0; i < $scope.myData[index].length; i++){
                            total = total + ($scope.myData[index][i].valor * $scope.myData[index][i].cantidad);
                        }
                        return $filter('currency')(total," ")
                    }
                    return $scope.gridOptions[index];
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
            }

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
        }])
    .controller('CerrarCajaController', ['$scope', "$state", "$cookieStore", '$filter', "$dialogs", "CajaService",
        function($scope, $state, $cookieStore, $filter, $dialogs, CajaService) {

            $scope.currentAgencia = $cookieStore.get("agencia");
            $scope.currentCaja = $cookieStore.get("caja");

            $scope.detalleCajaInicial = [];

            if($scope.currentCaja.abierto == true){
                //cargar los datos del web service
                CajaService.getDetalle().then(function(detalleCaja){
                    for(var i = 0; i<detalleCaja.length; i++){
                        angular.forEach(detalleCaja[i].detalle, function(row){
                            row.subtotal = function(){
                                return this.valor * this.cantidad;
                            }
                        });
                    }
                    $scope.detalleCajaInicial = angular.copy(detalleCaja);
                    $scope.detalleCajaFinal = angular.copy(detalleCaja);
                });

                //configurar las tablas
                $scope.myDataInicial = [];
                $scope.gridOptionsInicial = [];
                $scope.totalInicial = [];
                $scope.getTemplateInicial = function(index, simbolo){
                    $scope.myDataInicial[index] = $scope.detalleCajaInicial[index].detalle;
                    $scope.gridOptionsInicial[index] = {
                        data: 'myDataInicial['+index+']',
                        multiSelect: false,
                        columnDefs: [
                            //{ field: "denominacion", displayName: "Denominacion", cellTemplate: "<div><div class='ngCellText'>{{simbolo}} {{row.getProperty(col.field)}}</div></div>" },
                            { field: "valor | currency : '"+simbolo+" '", displayName: "Valor" },
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
                    $scope.myDataFinal[index] = $scope.detalleCajaFinal[index].detalle;
                    $scope.gridOptionsFinal[index] = {
                        data: 'myDataFinal['+index+']',
                        multiSelect: false,
                        enableCellSelection: true,
                        enableRowSelection: false,
                        enableCellEditOnFocus: true,
                        columnDefs: [
                            //{ field: "denominacion", displayName: "Denominacion", cellTemplate: "<div><div class='ngCellText'>{{simbolo}} {{row.getProperty(col.field)}}</div></div>", enableCellEdit: false },
                            { field: "valor | currency : '"+simbolo+" '", displayName: "Valor", enableCellEdit: false },
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

                //cerrar caja
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

                //ocultar ceros
                $scope.hideZeroRows = function(){
                    for(var i = 0; i < $scope.gridOptionsFinal.length; i++){
                        $scope.gridOptionsFinal[i].$gridScope.filterText = Math.ceil(24 * Math.random());
                    }

                }
            }

            $scope.buttonDisableState = function(){
                if($scope.progressTransaction == true)
                    return true;
                if($scope.currentCaja === undefined) {
                    return true;
                }
                else {
                    return false;
                }
            }

        }]);