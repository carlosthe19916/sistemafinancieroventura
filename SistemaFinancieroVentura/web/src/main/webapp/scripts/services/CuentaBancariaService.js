define(['./module'], function (services) {
    'use strict';
    services.factory("CuentaBancariaService",["Restangular",
        function(Restangular){
            var _historialCajaService = Restangular.all("cuentaBancaria");

            return {
                getCuentasBancarias: function(){
                    return _historialCajaService.getList();
                },
                getCuentasBancariasView: function(tipoCuentaList,tipoPersonaList,estadoCuentaList,offset,limit){
                    if(arguments.length == 0){
                        return Restangular.all("cuentaBancaria/view").getList();
                    }else if (arguments.length == 1) {
                        return Restangular.all("cuentaBancaria/view").getList({"tipoCuenta":tipoCuentaList},{});
                    } else if (arguments.length == 2) {
                        return Restangular.all("cuentaBancaria/view").getList({"tipoCuenta":tipoCuentaList,"tipoPersona":tipoPersonaList},{});
                    } else if (arguments.length == 3) {
                        return Restangular.all("cuentaBancaria/view").getList({"tipoCuenta":tipoCuentaList,"tipoPersona":tipoPersonaList,"tipoEstadoCuenta":estadoCuentaList},{});
                    } else if (arguments.length == 4) {
                        return Restangular.all("cuentaBancaria/view").getList({"tipoCuenta":tipoCuentaList,"tipoPersona":tipoPersonaList,"tipoEstadoCuenta":estadoCuentaList,"offset":offset},{});
                    } else if (arguments.length == 5) {
                        return Restangular.all("cuentaBancaria/view").getList({"tipoCuenta":tipoCuentaList,"tipoPersona":tipoPersonaList,"tipoEstadoCuenta":estadoCuentaList,"offset":offset,"limit":limit},{});
                    }
                },
                findByFilterTextView: function(filterText,tipoCuentaList,tipoPersonaList,estadoCuentaList,offset,limit){
                    if(arguments.length == 0){
                        return Restangular.all("cuentaBancaria/view").getList();
                    }else if (arguments.length == 1) {
                        return Restangular.all("cuentaBancaria/view").getList({"filterText":filterText},{});
                    } else if (arguments.length == 2) {
                        return Restangular.all("cuentaBancaria/view").getList({"filterText":filterText,"tipoCuenta":tipoCuentaList},{});
                    } else if (arguments.length == 3) {
                        return Restangular.all("cuentaBancaria/view").getList({"filterText":filterText,"tipoCuenta":tipoCuentaList,"tipoPersona":tipoPersonaList},{});
                    } else if (arguments.length == 4) {
                        return Restangular.all("cuentaBancaria/view").getList({"filterText":filterText,"tipoCuenta":tipoCuentaList,"tipoPersona":tipoPersonaList,"tipoEstadoCuenta":estadoCuentaList},{});
                    } else if (arguments.length == 5) {
                        return Restangular.all("cuentaBancaria/view").getList({"filterText":filterText,"tipoCuenta":tipoCuentaList,"tipoPersona":tipoPersonaList,"tipoEstadoCuenta":estadoCuentaList,"offset":offset},{});
                    } else if (arguments.length == 6) {
                        return Restangular.all("cuentaBancaria/view").getList({"filterText":filterText,"tipoCuenta":tipoCuentaList,"tipoPersona":tipoPersonaList,"tipoEstadoCuenta":estadoCuentaList,"offset":offset,"limit":limit},{});
                    }
                },
                count: function(){
                    return Restangular.one("cuentaBancaria/view/count").get();
                },
                getCuentasBancaria: function(id){
                    return Restangular.one("cuentaBancaria/"+id).get();
                },
                getCuentasBancariaView: function(id){
                    return Restangular.one("cuentaBancaria/view/"+id).get();
                },
                findCuentaByNumeroCuenta: function(numeroCuenta){
                    return Restangular.one("cuentaBancaria/view/buscar").get({numeroCuenta:numeroCuenta},{});
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
                getSocio: function(idCuenta){
                    return Restangular.one("cuentaBancaria/"+idCuenta+"/socio").get();
                },
                getTitulares: function(idCuenta){
                    return Restangular.all("cuentaBancaria/"+idCuenta+"/titulares").getList();
                },
                getBeneficiarios: function(idCuenta){
                    return Restangular.all("cuentaBancaria/"+idCuenta+"/beneficiarios").getList();
                },
                getVoucherCuentaBancaria: function(id) {
                    return Restangular.one("cuentaBancaria/"+id+"/voucherCuentaBancaria").get();
                },
                getVoucherTransferenciaBancaria: function(idTransferencia){
                    return Restangular.one("cuentaBancaria/"+idTransferencia+"/voucherTransferenciaBancaria").get();
                },
                getEstadoCuenta: function(id, desde, hasta) {
                    return Restangular.all("cuentaBancaria/"+id+"/estadoCuenta").getList({"desde":desde,"hasta":hasta},{});
                },
                congelarCuentaBancaria: function(id){
                    return Restangular.one("cuentaBancaria/"+id+"/congelar").customPUT({},'',{},{});
                },
                descongelarCuentaBancaria: function(id){
                    return Restangular.one("cuentaBancaria/"+id+"/descongelar").customPUT({},'',{},{});
                },
                cancelarCuenta: function(id){
                    return Restangular.one("cuentaBancaria/"+id).remove();
                },
                recalcularPlazoFijo: function(idCuenta, data){
                    return Restangular.one("cuentaBancaria/"+idCuenta+"/recalcularPlazoFijo").customPUT(data,'',{},{});
                }
            }
        }])
});
