angular.module('cajaApp.service', []);

angular.module("cajaApp.service", ["restangular"])
    .factory("CajaService",["Restangular",
        function(Restangular){
            var _cajaService = Restangular.all("caja");
            return {
                create: function(caja){
                    return _cajaService.post(caja);
                },
                update: function(caja){
                    var copy = Restangular.copy(caja);
                    return copy.put();
                },
                abrir: function(){
                    return Restangular.one("caja/abrir").put();
                },
                cerrar: function(detalle){
                    var copy = Restangular.copy(detalle);
                    return Restangular.all("caja/cerrar").post(copy);
                },
                getDetalle: function(){
                    return Restangular.all("caja/detalle").getList();
                },
                getCurrentCaja: function(){
                    return Restangular.one("caja/currentSession").get();
                },
                getBovedasOfCurrentCaja: function(){
                    return Restangular.all("caja/currentSession/bovedas").getList();
                }
            }
        }])
    .factory("HistorialCajaService",["Restangular",
        function(Restangular){
            var _historialCajaService = Restangular.all("historialcaja");
            return {
                buscar: function(desde, hasta){
                    return Restangular.all("historialcaja/currentSession").getList({"desde":desde,"hasta":hasta},{});
                },
                getVoucherCierreCaja: function(fechaAperturaCaja){
                    return Restangular.all("historialcaja/voucherCierreCaja").getList({"fechaApertura":fechaAperturaCaja},{});
                },
                getResumenCierreCaja: function(fechaAperturaCaja){
                    return Restangular.one("historialcaja/resumenCierreCaja").get({"fechaApertura":fechaAperturaCaja},{});
                }
            }
        }]);