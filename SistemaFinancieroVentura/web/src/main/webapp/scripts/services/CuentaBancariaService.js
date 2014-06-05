define(['./module'], function (services) {
    'use strict';
    services.factory("CuentaBancariaService",["Restangular",
        function(Restangular){
            var _historialCajaService = Restangular.all("cuentaBancaria");
            return {
                getCuentasBancarias: function(){
                    return _historialCajaService.getList().$object;
                },
                getCuentasBancariasView: function(){
                    return Restangular.all("cuentaBancaria/a").getList().$object;
                },
                getCuentasBancaria: function(id){
                    return Restangular.one("cuentaBancaria/"+id).get();
                },
                crearCuentaAhorro: function(transaccion){
                    return Restangular.all("cuentaBancaria/ahorro").post(transaccion);
                },
                crearCuentaCorriente: function(transaccion){
                    return Restangular.all("cuentaBancaria/corriente").post(transaccion);
                },
                crearCuentaPlazoFijo: function(transaccion){
                    return Restangular.all("cuentaBancaria/plazoFijo").post(transaccion);
                },
                findByFilterText: function(text){
                    return Restangular.all("cuentaBancaria/filtertext/"+text).getList();
                },
                findByFilterTextView: function(text){
                    return Restangular.all("cuentaBancaria/filtertext/"+text).getList();
                }
            }
        }])
});
