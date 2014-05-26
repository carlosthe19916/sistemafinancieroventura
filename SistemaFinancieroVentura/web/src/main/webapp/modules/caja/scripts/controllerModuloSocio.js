
angular.module('cajaApp.controller')
    .controller('CrearSocioController', [ "$scope","$state","$window", "MaestroService", "PersonaNaturalService", "PersonaJuridicaService", "SocioService",
        function($scope, $state,$window, MaestroService, PersonaNaturalService, PersonaJuridicaService, SocioService) {

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
                            alert("socio creado");
                        }, function error(error){
                            $scope.control.inProcess = true;
                            $scope.control.success = false;
                            $scope.alerts = [{ type: "danger", msg: "Error: " + error.data + "."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                } else {
                    $scope.control.submitted = true;
                }
            }

            $scope.crearPersonaSocio = function(){
                if($scope.tipoPersona !== undefined && $scope.tipoPersona !== null){
                    if($scope.tipoPersona.denominacion == "NATURAL"){
                       $window.open("http://localhost:8080/SistemaFinancieroVentura-web/index.caja.html#/app/socio/personaNatural");
                    } else{if($scope.tipoPersona.denominacion == "JURIDICA"){
                        $window.open("http://localhost:8080/SistemaFinancieroVentura-web/index.caja.html#/app/socio/personaJuridica");
                    }}
                } else{
                    alert("Seleccione tipo de persona");
                }
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
                    {field:'id', displayName:'ID SOCIO'},
                    {field:'tipodocumento', displayName:'TIPO DOC.'},
                    {field:'numerodocumento', displayName:'NUM. DOC.'},
                    {field:'tipopersona', displayName:'TIPO PERSONA'},
                    {field:'socio', displayName:'SOCIO'},
                    {field:"fechaasociado | date:'dd-MM-yyyy'", displayName:'FECHA ASOCIADO'},
                    {displayName: 'Edit', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="editSocio(row.entity)"><span class="glyphicon glyphicon-share"></span>Editar</button></div>'}
                ]
            };

            $scope.editSocio = function(row){
                $state.transitionTo("app.socio.panelSocio", { id: row.id });
            }
        }])
    .controller("PanelSocioController", ["$scope", "$state", "ngProgress", "SocioService",
        function($scope, $state, ngProgress, SocioService) {

            SocioService.getSocio($scope.id).then(
                function(data){
                    $scope.socio = data;
                }, function error(error){

                }
            );
            SocioService.getCuentaAporte($scope.id).then(
                function(data){
                    $scope.cuentaAporte = data;
                }, function error(error){

                }
            );
            SocioService.getPersonaNatural($scope.id).then(
                function(data){
                    $scope.personaNatural = data;
                }, function error(error){

                }
            );
            SocioService.getPersonaJuridica($scope.id).then(
                function(data){
                    $scope.personaJuridica = data;
                }, function error(error){

                }
            );
            SocioService.getApoderado($scope.id).then(
                function(data){
                    $scope.apoderado = data;
                }, function error(error){

                }
            );
            SocioService.getCuentasBancarias($scope.id).then(
                function(data){
                    $scope.cuentasBancarias = data;
                }, function error(error){

                }
            );

        }]);