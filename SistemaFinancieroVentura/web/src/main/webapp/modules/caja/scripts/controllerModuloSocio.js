angular.module('cajaApp.controller')
    .controller('CrearSocioController', [ "$scope","$state","$window", "MaestroService", "PersonaNaturalService", "PersonaJuridicaService", "SocioService",
        function($scope, $state,$window, MaestroService, PersonaNaturalService, PersonaJuridicaService, SocioService) {

            angular.element(document).ready(function() {
                $('select[autofocus]:visible:first').focus();
            });

            $scope.control = {"success":false, "inProcess": false, "submitted" : false};
            $scope.tipoPersonas = [{"denominacion":"NATURAL"},{"denominacion":"JURIDICA"}];
            $scope.tipoDocumentosSocio = [];
            $scope.tipoDocumentosApoderado = [];

            $scope.transaccion = {
                "tipoPersona": undefined,
                "idTipoDocumentoSocio": undefined,
                "numeroDocumentoSocio": undefined,
                "idTipoDocumentoApoderado": undefined,
                "numeroDocumentoApoderado": undefined
            };

            MaestroService.getTipoDocumentoPN().then(function(data){
                    $scope.tipoDocumentosApoderado = data;
            });

            $scope.tipoPersonaChange = function(){
                $scope.transaccion.numeroDocumentoSocio = "";
                if($scope.transaccion.tipoPersona == "NATURAL"){
                    MaestroService.getTipoDocumentoPN().then(function(data){
                            $scope.tipoDocumentosSocio = data;
                    });
                }else{if($scope.transaccion.tipoPersona == "JURIDICA"){
                    MaestroService.getTipoDocumentoPJ().then(function(data){
                            $scope.tipoDocumentosSocio = data;
                    });
                }}
            }

            $scope.buscarPersonaSocio = function($event){
                var tipoDoc = $scope.transaccion.idTipoDocumentoSocio;
                var numDoc = $scope.transaccion.numeroDocumentoSocio;
                if(tipoDoc === null || tipoDoc === undefined){
                    alert("Tipo documento no definido");
                    return;
                }
                if(numDoc === null || numDoc === undefined){
                    alert("Numero documento no definido");
                    return;
                }
                if($scope.transaccion.tipoPersona == "NATURAL"){
                    PersonaNaturalService.findByTipoNumeroDocumento(tipoDoc,numDoc).then(function(persona){
                        $scope.socio = persona;
                    },function error(error){
                        $scope.socio = undefined;
                        $scope.alerts = [{ type: "warning", msg: "Socio no encontrado."}];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                    });
                }else{if($scope.transaccion.tipoPersona == "JURIDICA"){
                    PersonaJuridicaService.findByTipoNumeroDocumento(tipoDoc,numDoc).then(function(persona){
                        $scope.socio = persona;
                    },function error(error){
                        $scope.socio = undefined;
                        $scope.alerts = [{ type: "warning", msg: "Socio no encontrado."}];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                    });
                }}
                $event.preventDefault();
            }
            $scope.buscarPersonaApoderado = function($event){
                var tipoDoc = $scope.transaccion.idTipoDocumentoApoderado;
                var numDoc = $scope.transaccion.numeroDocumentoApoderado;
                if(tipoDoc === null || tipoDoc === undefined){
                    alert("Tipo documento no definido");
                    return;
                }
                if(numDoc === null || numDoc === undefined){
                    alert("Numero documento no definido");
                    return;
                }
                PersonaNaturalService.findByTipoNumeroDocumento(tipoDoc, numDoc).then(function(persona){
                    $scope.apoderado = persona;
                },function error(error){
                    $scope.apoderado = undefined;
                    $scope.alerts = [{ type: "info", msg: "Apoderado no encontrado."}];
                    $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                });
                $event.preventDefault();
            }

            //transacacion principal
            $scope.crearSocio = function(){
                if ($scope.formCrearSocio.$valid) {
                    $scope.control.inProcess = true;
                    var tipoPersona = $scope.transaccion.tipoPersona;
                    var idTipoDocumentoSocio = $scope.transaccion.idTipoDocumentoSocio;
                    var numeroDocumentoSocio = $scope.transaccion.numeroDocumentoSocio;
                    var idTipoDocumentoApoderado = $scope.transaccion.idTipoDocumentoApoderado;
                    var numeroDocumentoApoderado = $scope.transaccion.numeroDocumentoApoderado;

                    SocioService.crear(tipoPersona,
                        idTipoDocumentoSocio,
                        numeroDocumentoSocio,
                        idTipoDocumentoApoderado,
                        numeroDocumentoApoderado).then(
                        function(data){
                            $scope.control.inProcess = false;
                            $scope.control.success = true;
                            $state.transitionTo("app.socio.panelSocio", { id: data.id });
                        }, function error(error){
                            $scope.control.inProcess = false;
                            $scope.control.success = false;
                            $scope.alerts = [{ type: "danger", msg: "Error: " + error.data.message + "."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                } else {
                    $scope.control.submitted = true;
                }
            }

            $scope.crearPersonaSocio = function(){
                if($scope.transaccion.tipoPersona !== undefined && $scope.transaccion.tipoPersona !== null){
                    if($scope.transaccion.tipoPersona == "NATURAL"){
                       $window.open("http://localhost:8080/SistemaFinancieroVentura-web/index.caja.html#/app/socio/personaNatural");
                    } else{if($scope.transaccion.tipoPersona == "JURIDICA"){
                        $window.open("http://localhost:8080/SistemaFinancieroVentura-web/index.caja.html#/app/socio/personaJuridica");
                    }}
                } else{
                    alert("Seleccione tipo de persona");
                }
            }

            $scope.crearPersonaApoderado = function(){
                $window.open("http://localhost:8080/SistemaFinancieroVentura-web/index.caja.html#/app/socio/personaNatural");
            }

            $scope.buttonDisableState = function(){
                return $scope.control.inProcess;
            }
        }])
    .controller("BuscarSocioController", ["$scope", "$state", "ngProgress", "SocioService",
        function($scope, $state, ngProgress, SocioService) {
            ngProgress.color("#2d6ca2");

            $scope.nuevo = function(){
                $state.transitionTo("app.socio.crearSocio");
            }

            $scope.sociosList = [];

            $scope.filterOptions = {
                filterText: "",
                useExternalFilter: true
            };
            $scope.totalServerItems = 0;
            $scope.pagingOptions = {
                pageSizes: [10, 20, 40],
                pageSize: 10,
                currentPage: 1
            };
            $scope.setPagingData = function(data, page, pageSize){
                var pagedData = data.slice((page - 1) * pageSize, page * pageSize);
                $scope.sociosList = pagedData;
                $scope.totalServerItems = data.length;
                if (!$scope.$$phase) {
                    $scope.$apply();
                }
            };

            $scope.getPagedDataAsync = function (pageSize, page, searchText) {
                setTimeout(function () {
                    var data;
                    if (searchText) {
                        var ft = searchText.toUpperCase();
                        data = $scope.sociosList.filter(function(item) {
                            return JSON.stringify(item).toUpperCase().indexOf(ft) != -1;
                        });
                        $scope.setPagingData(data,page,pageSize);

                    } else {
                        if( angular.isUndefined($scope.filterOptions.filterText) || $scope.filterOptions.filterText === null) {
                            var result = $scope.sociosList = SocioService.getSocios();
                            $scope.setPagingData(result,page,pageSize);
                        } else {
                            $scope.setPagingData($scope.sociosList,page,pageSize);
                        }
                    }
                }, 100);
            };

            $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage);

            $scope.getPagedDataSearched = function () {
                setTimeout(function () {
                    if ($scope.filterOptions.filterText) {
                        ngProgress.start();
                        var ft = $scope.filterOptions.filterText.toUpperCase();
                        SocioService.findByFilterText(ft).then(function (socios){
                            $scope.sociosList = socios;
                        });
                        ngProgress.complete();
                    } else {
                        ngProgress.start();
                        $scope.sociosList = SocioService.getSocios();
                        ngProgress.complete();
                    }
                }, 100);
            };

            $scope.getPagedDataSearched();

            $scope.$watch('pagingOptions', function (newVal, oldVal) {
                if (newVal !== oldVal && newVal.currentPage !== oldVal.currentPage) {
                    $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.filterOptions.filterText);
                }
            }, true);

            $scope.$watch('filterOptions', function (newVal, oldVal) {
                if (newVal !== oldVal) {
                    $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.filterOptions.filterText);
                }
            }, true);

            $scope.gridOptions = {
                data: 'sociosList',
                multiSelect: false,
                enablePaging: true,
                showFooter: true,
                totalServerItems: 'totalServerItems',
                pagingOptions: $scope.pagingOptions,
                filterOptions: $scope.filterOptions,
                columnDefs: [
                    {field:'id', displayName:'ID SOCIO', width:"10%"},
                    {field:'tipodocumento', displayName:'TIPO DOC.',width:"15%"},
                    {field:'numerodocumento', displayName:'NUM. DOC.',width:"12%"},
                    {field:'tipopersona', displayName:'TIPO PERSONA',width:"12%"},
                    {field:'socio', displayName:'SOCIO',width:"28%"},
                    {field:"fechaasociado | date:'dd-MM-yyyy'", displayName:'F. ASOCIADO',width:"14%"},
                    {displayName: 'Edit', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="editSocio(row.entity)"><span class="glyphicon glyphicon-share"></span>Editar</button></div>'}
                ]
            };

            $scope.editSocio = function(row){
                $state.transitionTo("app.socio.panelSocio", { id: row.id });
            }
        }])
    .controller("PanelSocioController", ["$scope", "$state","$window", "SocioService", "MaestroService",
        function($scope, $state,$window, SocioService, MaestroService) {

            SocioService.getSocio($scope.id).then(
                function(data){
                    $scope.socio = data;
                }, function error(error){
                    $scope.alerts = [{ type: "danger", msg: "Socio no encontrado."}];
                    $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                }
            );
            SocioService.getCuentaAporte($scope.id).then(
                function(data){
                    $scope.cuentaAporte = data;
                }, function error(error){
                    $scope.alerts = [{ type: "warning", msg: "Cuenta de aporte no encontrada."}];
                    $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                }
            );
            SocioService.getPersonaNatural($scope.id).then(function(data){
                $scope.personaNatural = data;
                var abreviaturaPais = $scope.personaNatural.codigoPais;
                MaestroService.getPaisByAbreviatura(abreviaturaPais).then(function(pais){
                    $scope.pais = pais;
                });
            });
            SocioService.getPersonaJuridica($scope.id).then(function(data){
                $scope.personaJuridica = data;
                var abreviaturaPais = $scope.personaJuridica.representanteLegal.codigoPais;
                MaestroService.getPaisByAbreviatura(abreviaturaPais).then(function(pais){
                    $scope.pais = pais;
                });
            });
            SocioService.getApoderado($scope.id).then(function(data){
                $scope.apoderado = data;
            });

            SocioService.getCuentasBancarias($scope.id).then(function(data){
                $scope.cuentasBancarias = data;
            });


            $scope.editarSocioPN = function(){
                $window.open("http://localhost:8080/SistemaFinancieroVentura-web/index.caja.html#/app/socio/personaNatural/"+ $scope.personaNatural.id);
            }
            $scope.editarSocioPJ = function(){

            }

        }])


    .controller("BuscarCuentaBancariaController", ["$scope", "$state", "ngProgress", "CuentaBancariaService",
        function($scope, $state, ngProgress, CuentaBancariaService) {
            ngProgress.color("#2d6ca2");

            $scope.nuevo = function(){
                $state.transitionTo("app.socio.crearCuentaBancaria");
            }

            $scope.cuentasList = [];

            $scope.filterOptions = {
                filterText: "",
                useExternalFilter: true
            };
            $scope.totalServerItems = 0;
            $scope.pagingOptions = {
                pageSizes: [10, 20, 40],
                pageSize: 10,
                currentPage: 1
            };
            $scope.setPagingData = function(data, page, pageSize){
                var pagedData = data.slice((page - 1) * pageSize, page * pageSize);
                $scope.cuentasList = pagedData;
                $scope.totalServerItems = data.length;
                if (!$scope.$$phase) {
                    $scope.$apply();
                }
            };

            $scope.getPagedDataAsync = function (pageSize, page, searchText) {
                setTimeout(function () {
                    var data;
                    if (searchText) {
                        var ft = searchText.toUpperCase();
                        data = $scope.cuentasList.filter(function(item) {
                            return JSON.stringify(item).toUpperCase().indexOf(ft) != -1;
                        });
                        $scope.setPagingData(data,page,pageSize);

                    } else {
                        if( angular.isUndefined($scope.filterOptions.filterText) || $scope.filterOptions.filterText === null) {
                            var result = $scope.cuentasList = SocioService.getSocios();
                            $scope.setPagingData(result,page,pageSize);
                        } else {
                            $scope.setPagingData($scope.cuentasList,page,pageSize);
                        }
                    }
                }, 100);
            };

            $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage);

            $scope.getPagedDataSearched = function () {
                setTimeout(function () {
                    if ($scope.filterOptions.filterText) {
                        ngProgress.start();
                        var ft = $scope.filterOptions.filterText.toUpperCase();
                        CuentaBancariaService.findByFilterText(ft).then(function (socios){
                            $scope.cuentasList = socios;
                        });
                        ngProgress.complete();
                    } else {
                        ngProgress.start();
                        $scope.cuentasList = CuentaBancariaService.getCuentasBancarias();
                        ngProgress.complete();
                    }
                }, 100);
            };

            $scope.getPagedDataSearched();

            $scope.$watch('pagingOptions', function (newVal, oldVal) {
                if (newVal !== oldVal && newVal.currentPage !== oldVal.currentPage) {
                    $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.filterOptions.filterText);
                }
            }, true);

            $scope.$watch('filterOptions', function (newVal, oldVal) {
                if (newVal !== oldVal) {
                    $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.filterOptions.filterText);
                }
            }, true);

            $scope.gridOptions = {
                data: 'cuentasList',
                multiSelect: false,
                enablePaging: true,
                showFooter: true,
                totalServerItems: 'totalServerItems',
                pagingOptions: $scope.pagingOptions,
                filterOptions: $scope.filterOptions,
                columnDefs: [
                    {field:"tipoCuentaBancaria", displayName:'Tipo cta.'},
                    {field:"numeroCuenta", displayName:'Num. cuenta'},
                    {field:"moneda.denominacion", displayName:'Moneda'},
                    {field:"fechaApertura | date : 'dd/MM/yyyy'", displayName:'F. apertura'},
                    {field:"fechaCierre | date : 'dd/MM/yyyy'", displayName:'F. cierre'},
                    {field:"cantidadRetirantes", displayName:'Cant. retirantes'},
                    {field:"estado", displayName:'Estado'},
                    {displayName: 'Edit', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="editCuenta(row.entity)"><span class="glyphicon glyphicon-share"></span>Editar</button></div>'}
                ]
            };

            $scope.editCuenta = function(row){
                $state.transitionTo("app.socio.editarCuentaBancaria", { id: row.id });
            }
        }])
    .controller("CrearCuentaBancariaController", ["$scope", "$state",
        function($scope, $state) {
            $scope.cuentaSelected;
            $scope.cuentaAhorro = "modules/caja/views/cuenta/crearCuentaAhorro.html";
            $scope.cuentaCorriente = "modules/caja/views/cuenta/crearCuentaCorriente.html";
            $scope.cuentaPlazoFijo = "modules/caja/views/cuenta/crearCuentaPlazoFijo.html";
        }])
    .controller('CrearCuentaAhorroController', [ "$scope", "$state", "$filter", "$window", "MaestroService", "MonedaService", "PersonaNaturalService", "PersonaJuridicaService", "SocioService", "TasaInteresService", "CuentaBancariaService",
        function($scope, $state, $filter, $window, MaestroService, MonedaService, PersonaNaturalService, PersonaJuridicaService, SocioService, TasaInteresService, CuentaBancariaService) {

            $scope.control = {"success":false, "inProcess": false, "submitted" : false};
            $scope.tipoPersonas = [{"denominacion":"NATURAL"},{"denominacion":"JURIDICA"}];

            $scope.tipoDocumentos = [];
            $scope.titulares = [];
            $scope.titularesFinal = [];
            $scope.beneficiarios = [];

            $scope.$watchCollection('titulares', function() {
                $scope.titularesFinal = angular.copy($scope.titulares);
                if($scope.socioNatural !== undefined)
                    $scope.titularesFinal.push($scope.socioNatural);
                if($scope.socioJuridico !== undefined)
                    $scope.titularesFinal.push($scope.socioJuridico.representanteLegal);
            });
            $scope.$watch('socioNatural', function() {
                $scope.titularesFinal = angular.copy($scope.titulares);
                if($scope.socioNatural !== undefined){
                    $scope.titularesFinal.push($scope.socioNatural);
                    $scope.transaccion.idPersona = $scope.socioNatural.id;
                }
            });
            $scope.$watch('socioJuridico', function() {
                $scope.titularesFinal = angular.copy($scope.titulares);
                if($scope.socioJuridico !== undefined){
                    $scope.titularesFinal.push($scope.socioJuridico.representanteLegal);
                    $scope.transaccion.idPersona = $scope.socioJuridico.id;
                }
            });

            $scope.transaccion = {
                "idMoneda": undefined,
                "tasaInteres": undefined,
                "tipoPersona": undefined,
                "idTipoDocumento" : undefined,
                "numeroDocumento" : undefined,
                "idPersona" : undefined,
                "cantRetirantes": undefined,
                "titulares" : {},
                "beneficiarios" : {}
            };

            $scope.tabCuentaSelected = function(){
                angular.element("#cmbTipoPersona").focus();
            }
            $scope.tabTitularSelected = function(){
                angular.element("#cmbTipoDocumentoTitular").focus();
            }
            $scope.tabBeneficiarioSelected = function(){
                angular.element("#txtNumeroDocumentoBeneficiario").focus();
            }

            MonedaService.getMonedas().then(function(monedas){
                $scope.monedas = monedas;
            });

            $scope.tipoPersonaChange = function(){
                $scope.socioNatural = undefined;
                $scope.socioJuridico = undefined;
                if($scope.transaccion.tipoPersona == "NATURAL"){
                    MaestroService.getTipoDocumentoPN().then(function(data){
                        $scope.tipoDocumentos = data;
                        $scope.transaccion.numeroDocumento = "";
                    });
                }else{if($scope.transaccion.tipoPersona == "JURIDICA"){
                    MaestroService.getTipoDocumentoPJ().then(function(data){
                        $scope.tipoDocumentos = data;
                        $scope.transaccion.numeroDocumento = "";
                    });
                }}
            }

            $scope.monedaChange = function(){
                TasaInteresService.getTasaCuentaAhorro($scope.transaccion.idMoneda).then(
                    function(data){
                        $scope.transaccion.tasaInteres = data.valor;
                    }
                );
            }

            $scope.buscarPersonaSocio = function($event){
                var tipoDoc = $scope.transaccion.idTipoDocumento;
                var numDoc = $scope.transaccion.numeroDocumento;
                if(tipoDoc === null || tipoDoc === undefined){
                    alert("Tipo documento no definido");
                    return;
                }
                if(numDoc === null || numDoc === undefined){
                    alert("Numero documento no definido");
                    return;
                }
                if($scope.transaccion.tipoPersona == "NATURAL"){
                    $scope.socioJuridico = undefined;
                    PersonaNaturalService.findByTipoNumeroDocumento(tipoDoc,numDoc).then(function(persona){
                        $scope.socioNatural = persona;
                    },function error(error){
                        $scope.socioNatural = undefined;
                        $scope.alerts = [{ type: "danger", msg: "Socio no encontrado."}];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                    });
                }else{if($scope.transaccion.tipoPersona == "JURIDICA"){
                    $scope.socioNatural = undefined;
                    PersonaJuridicaService.findByTipoNumeroDocumento(tipoDoc,numDoc).then(function(persona){
                        $scope.socioJuridico = persona;
                    },function error(error){
                        $scope.socioJuridico = undefined;
                        $scope.alerts = [{ type: "danger", msg: "Socio no encontrado."}];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                    });
                }}
                $event.preventDefault();
            }

            //transacacion principal
            $scope.formCrearCuenta = {};
            $scope.crearCuenta = function(){
                if ($scope.formCrearCuenta.$valid) {
                    $scope.control.inProcess = true;

                    //poniendo variables
                    var cuenta = {
                        "idMoneda": $scope.transaccion.idMoneda,
                        "tipoPersona": $scope.transaccion.tipoPersona,
                        "idPersona": $scope.transaccion.idPersona,
                        "cantRetirantes":$scope.transaccion.cantRetirantes,
                        "titulares":[],
                        "beneficiarios": ($filter('unique')($scope.beneficiarios))
                    }

                    for(var i = 0; i < $scope.titularesFinal.length ; i++){
                        var idTitular = $scope.titularesFinal[i].id;
                        cuenta.titulares.push(idTitular);
                    }
                    if($scope.transaccion.idPersona === undefined || $scope.transaccion.idPersona === null)
                        $scope.buscarPersonaSocio();

                    console.log(JSON.stringify(cuenta));

                    CuentaBancariaService.crearCuentaAhorro(cuenta).then(
                        function(data){
                            $scope.control.inProcess = false;
                            $scope.control.success = true;
                            alert("creado");
                        }, function error(error){
                            $scope.control.inProcess = false;
                            $scope.control.success = false;
                        }
                    );
                } else {
                    $scope.control.submitted = true;
                }
            }

            $scope.crearPersona = function(){
                if($scope.transaccion.tipoPersona !== undefined && $scope.transaccion.tipoPersona !== null){
                    if($scope.transaccion.tipoPersona == "NATURAL"){
                        $window.open("http://localhost:8080/SistemaFinancieroVentura-web/index.caja.html#/app/socio/personaNatural");
                    } else{if($scope.transaccion.tipoPersona == "JURIDICA"){
                        $window.open("http://localhost:8080/SistemaFinancieroVentura-web/index.caja.html#/app/socio/personaJuridica");
                    }}
                } else{
                    alert("Seleccione tipo de persona");
                }
            }
        }])
    .controller('BeneficiarioController', [ "$scope",
        function($scope) {

            $scope.control = {"submitted" : false};

            $scope.beneficiario = {
                "numeroDocumento" : undefined,
                "apellidoPaterno" : undefined,
                "apellidoMaterno" : undefined,
                "nombres" : undefined,
                "porcentajeBeneficio" : undefined
            }

            //$scope.beneficiarios = [];

            $scope.addBeneficiario = function() {
                if($scope.formBeneficiario.$valid){
                    var totalActual = $scope.totalPorcentaje();
                    var totalFinal = totalActual + $scope.beneficiario.porcentajeBeneficio;
                    if(totalFinal <= 100){
                        $scope.beneficiarios.push({
                            "numeroDocumento" : $scope.beneficiario.numeroDocumento,
                            "apellidoPaterno" : $scope.beneficiario.apellidoPaterno,
                            "apellidoMaterno" : $scope.beneficiario.apellidoMaterno,
                            "nombres" : $scope.beneficiario.nombres,
                            "porcentajeBeneficio" : $scope.beneficiario.porcentajeBeneficio
                        });
                        $scope.clear();
                        $scope.resetFocus();
                    } else {
                        $scope.alerts = [
                            { type: 'warning', msg: 'Error: Porcentaje supera el 100%' }
                        ];
                        $scope.closeAlert = function(index) {
                            $scope.alerts.splice(index, 1);
                        };
                    }
                } else {
                    $scope.control.submitted = true;
                }
            }

            $scope.removeBeneficiario = function(index){
                $scope.beneficiarios.splice(index, 1);
                $scope.resetFocus();
            }

            $scope.clear = function(){
                $scope.beneficiario = {
                    "numeroDocumento" : "",
                    "apellidoPaterno" : "",
                    "apellidoMaterno" : "",
                    "nombres" : "",
                    "porcentajeBeneficio" : undefined
                }
                $scope.resetFocus();
            }

            $scope.resetFocus = function(){
                angular.element("#txtNumeroDocumentoBeneficiario").focus();
            }

            $scope.totalPorcentaje = function(){
                var total = 0;
                for(var i = 0; i < $scope.beneficiarios.length; i++)
                    total = total + $scope.beneficiarios[i].porcentajeBeneficio;
                return total;
            }
        }])
    .controller('TitularController', [ "$scope", "MaestroService", "PersonaNaturalService",
        function($scope, MaestroService, PersonaNaturalService) {

            $scope.control = {"inProcess": false, "submitted" : false};

            $scope.titular = {
                "idTipoDocumento" : undefined,
                "numeroDocumento" : undefined
            }

            //$scope.titulares = [];

            MaestroService.getTipoDocumentoPN().then(function(tipoDocumentos){
                $scope.tipoDocumentos = tipoDocumentos;
            });

            $scope.addPersona = function() {
                if($scope.formTitular.$valid){
                    $scope.control.inProcess = true;
                    PersonaNaturalService.findByTipoNumeroDocumento($scope.titular.idTipoDocumento, $scope.titular.numeroDocumento).then(
                        function(persona){
                            $scope.control.inProcess = false;
                            $scope.titulares.push(persona);
                            $scope.clear();
                            $scope.resetFocus();
                        }, function error(error){
                            $scope.control.inProcess = false;
                            $scope.alerts = [{ type: 'danger', msg: 'Error: persona no encontrada' }];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                } else {
                    $scope.control.submitted = true;
                }
            }

            $scope.removePersona = function(index){
                $scope.titulares.splice(index, 1);
                $scope.resetFocus();
            }

            $scope.clear = function(){
                $scope.titular = {
                    "idTipoDocumento" : undefined,
                    "numeroDocumento" : ""
                }
                $scope.resetFocus();
            }

            $scope.resetFocus = function(){
                angular.element("#cmbTipoDocumentoTitular").focus();
            }

        }]);