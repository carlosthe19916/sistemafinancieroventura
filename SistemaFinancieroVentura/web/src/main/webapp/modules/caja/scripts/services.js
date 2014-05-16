angular.module('cajaApp.service', []);

angular.module("cajaApp.service", ["restangular"])
    .factory("CajaService",["Restangular",
        function(Restangular){
            var _cajaService = Restangular.all("caja/session");
            return {
                create: function(caja){
                    return _cajaService.post(caja);
                },
                update: function(caja){
                    var copy = Restangular.copy(caja);
                    return copy.put();
                },
                getCurrentCaja: function(){
                    return Restangular.one("caja/session").get();
                },
                getDetalle: function(){
                    return Restangular.all("caja/session/detalle").getList();
                },
                abrir: function(){
                    return Restangular.one("caja/session/abrir").put();
                },
                cerrar: function(detalle){
                    var copy = Restangular.copy(detalle);
                    return Restangular.all("caja/session/cerrar").post(copy);
                },
                getBovedasOfCurrentCaja: function(){
                    return Restangular.all("caja/session/bovedas").getList();
                },
                crearTransaccionBovedaCaja: function(boveda, detalle){
                    var copy = Restangular.copy(detalle);
                    return Restangular.all("caja/session/transaccionbovedacaja").post(copy ,{"boveda":boveda});
                },
                getTransaccionBovedaCajaEnviadas: function(){
                    return Restangular.all("caja/session/transaccionbovedacaja/enviados").getList();
                },
                getTransaccionBovedaCajaRecibidas: function(){
                    return Restangular.all("caja/session/transaccionbovedacaja/recibidos").getList();
                },
                getPendientes: function(){
                    return Restangular.all("caja/session/pendiente").getList();
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