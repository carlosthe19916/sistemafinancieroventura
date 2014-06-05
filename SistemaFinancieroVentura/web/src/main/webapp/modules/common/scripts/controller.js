var commonAppController = angular.module('commonApp.controller', []);

angular.module('commonApp.controller')
    .controller('CalculadoraController', function ($scope, $modalInstance, denominaciones, moneda) {

        $scope.denominaciones = denominaciones;
        $scope.moneda = moneda;

        $scope.total = function(){
            var totalCalculadora = 0;
            for(var i = 0; i < $scope.denominaciones.length; i++){
                totalCalculadora = totalCalculadora + ($scope.denominaciones[i].cantidad * $scope.denominaciones[i].valor);
            }
            return totalCalculadora;
        }

        $scope.ok = function () {
            if (($scope.total() != 0 && $scope.total() !== undefined)) {
                $modalInstance.close($scope.total());
            }
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    })
    .controller('BuscarCuentaBancariaController', function ($scope, $modalInstance) {
       
        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    })
    .controller('pruebaController', function ($scope) {

        $scope.$on('flow::fileAdded', function (event, $flow, flowFile) {
            alert("entroo");
            event.preventDefault();//prevent file from uploading
        });
    })
    .controller('PersonanaturalBuscarController', ['$scope','$state','$dialogs', 'ngProgress','Restangular', "PersonanaturalService",
        function($scope, $state, $dialogs, ngProgress, Restangular, PersonanaturalService) {

            $scope.personasList = [];

            $scope.create = function() {
                $state.go("app.administracion.personanaturalCreate");
            };

            $scope.edit = function(persona) {
                $state.transitionTo('app.administracion.personanaturalUpdate', { id: persona.id });
            };

            $scope.delete = function(persona) {
                var dlg = $dialogs.confirm('Confirmacion','¿Está seguro de eliminar la persona? "Si" or "No"');
                dlg.result.then(function(btn){
                    PersonanaturalService.remove(persona.id);
                    var index = $scope.personasList.indexOf(persona);
                    if (index > -1) {
                        $scope.personasList.splice(index, 1);
                    }
                },function(btn){});
            };

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
                $scope.personasList = pagedData;
                $scope.totalServerItems = data.length;
                if (!$scope.$$phase) {
                    $scope.$apply();
                }
            };

            $scope.getPagedDataAsync = function (pageSize, page, searchText) {
                setTimeout(function () {
                    var data;
                    if (searchText) {
                        var ft = searchText.toLowerCase();
                        data = $scope.personasList.filter(function(item) {
                            return JSON.stringify(item).toLowerCase().indexOf(ft) != -1;
                        });
                        $scope.setPagingData(data,page,pageSize);
                    } else {
                        if( angular.isUndefined($scope.filterOptions.filterText) || $scope.filterOptions.filterText === null) {
                            var result = $scope.personasList = PersonanaturalService.getPersonas();
                            $scope.setPagingData(result,page,pageSize);
                        } else {
                            $scope.setPagingData($scope.personasList,page,pageSize);
                        }
                    }
                }, 100);
            };

            $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage);

            ngProgress.color("#2d6ca2");

            $scope.getPagedDataSearched = function () {
                setTimeout(function () {
                    if ($scope.filterOptions.filterText) {
                        ngProgress.start();
                        var ft = $scope.filterOptions.filterText.toLowerCase();
                        PersonanaturalService.findByFilterText(ft).then(function (personas){
                            $scope.personasList = personas;
                        });
                        ngProgress.complete();
                    } else {
                        ngProgress.start();
                        $scope.personasList = PersonanaturalService.getPersonas();
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
                data: 'personasList',
                multiSelect: false,
                enablePaging: true,
                showFooter: true,
                totalServerItems: 'totalServerItems',
                pagingOptions: $scope.pagingOptions,
                filterOptions: $scope.filterOptions,
                columnDefs: [
                    {field:'tipodocumento.abreviatura', displayName:'T.doc.'},
                    {field:'numerodocumento', displayName:'Num.doc.'},
                    {field:'apellidopaterno', displayName:'Ap.paterno'},
                    {field:'apellidomaterno', displayName:'Ap.materno'},
                    {field:'nombres', displayName:'Nombres'},
                    {field:'sexo', displayName:'Sexo'},
                    {field:"fechanacimiento | date:'dd-MM-yyyy'", displayName:'F.nacimiento'},
                    {displayName: 'Edit', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="edit(row.entity)"><span class="glyphicon glyphicon-share"></span>Edit</button>&nbsp;<button type="button" class="btn btn-danger btn-xs" ng-click="delete(row.entity)"><span class="glyphicon glyphicon-remove"></span>Del</button></div>'}]
            };
        }])
    .controller('CrearPersonaNaturalController', ["$scope", "$state","$window", "MaestroService", "PersonaNaturalService",
        function($scope, $state,$window, MaestroService, PersonaNaturalService) {
            $scope.control = {"success":false, "inProcess": false, "submitted" : false};
            $scope.persona = PersonaNaturalService.getModel();
            $scope.ubigeo = {"departamento":"", "provincia":"", "distrito":""};
            $scope.persona.ubigeo = $scope.ubigeo.departamento + $scope.ubigeo.provincia + $scope.ubigeo.distrito;

            $scope.dateOptions = {
                formatYear: 'yyyy',
                startingDay: 1
            };

            $scope.fechaNacimiento = new Date();
            $scope.persona.fechaNacimiento = $scope.fechaNacimiento.getTime();

            $scope.open = function($event) {
                $event.preventDefault();
                $event.stopPropagation();

                $scope.opened = true;
            };

            MaestroService.getTipoDocumentoPN().then(function(tipodocumentos){
                $scope.tipodocumentos = tipodocumentos;
            });
            MaestroService.getSexos().then(function(sexos){
                $scope.sexos = sexos;
            });
            MaestroService.getEstadosciviles().then(function(estadosciviles){
                $scope.estadosciviles = estadosciviles;
            });
            MaestroService.getPaises().then(function(paises){
                $scope.paises = paises;
            });
            MaestroService.getDepartamentos().then(function(departamentos){
                $scope.departamentos = departamentos;
            });

            $scope.changeDepartamento = function(departamento){
                MaestroService.getProvincias(departamento.id).then(function(provincias){
                    $scope.provincias = provincias;
                });
            }
            $scope.changeProvincia = function(provincia){
                MaestroService.getDistritos(provincia.id).then(function(distritos){
                    $scope.distritos = distritos;
                });
            }

            //logic
            $scope.create = function(){
                if ($scope.formCrearPersonanatural.$valid) {
                    $scope.buttonDisableState = true;
                    PersonaNaturalService.crear($scope.persona).then(
                        function(persona){
                           //PersonaNaturalService.guardarPersonaResponse(data.id);
                           //alert("dato enviado "+PersonaNaturalService.getPersonaResponse());
                            $window.close();
                        },
                        function error(error){
                            $scope.control.inProcess = false;
                            $scope.control.success = false;
                            $scope.alerts = [{ type: "danger", msg: "Error: " + error.data + "."}];
                            $scope.alerts.splice(index, 1);
                        }
                    );
                    $scope.buttonDisableState = false;
                } else {
                    $scope.control.submitted = true;
                }
            }

            $scope.cancel = function () {
                //$state.go("app.administracion.personanaturalBuscar");
                alert("cancelar");
            }

            $scope.buttonDisableState = function(){
                return $scope.control.inProcess;
            }

        }])
    .controller('CrearPersonaJuridicaController', ["$scope", "$state","$window", "MaestroService", "PersonaNaturalService", "PersonaJuridicaService",
        function($scope, $state,$window, MaestroService, PersonaNaturalService, PersonaJuridicaService) {
            $scope.control = {"success":false, "inProcess": false, "submitted" : false};
            $scope.personaJuridica = PersonaJuridicaService.getModel();
            $scope.representanteLegal = PersonaNaturalService.getModel();
            $scope.accionistas = [];
            $scope.ubigeo = {"departamento":"", "provincia":"", "distrito":""};
            $scope.personaJuridica.ubigeo = $scope.ubigeo.departamento + $scope.ubigeo.provincia + $scope.ubigeo.distrito;

            $scope.dateOptions = {formatYear: 'yyyy',startingDay: 1};
            $scope.fechaConstitucion = new Date();
            $scope.personaJuridica.fechaConstitucion = $scope.fechaConstitucion.getTime();

            $scope.open = function($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.opened = true;
            };

            MaestroService.getTipoDocumentoPJ().then(function(tipodocumentos){
                $scope.tipoDocumentosPJ = tipodocumentos;
            });
            MaestroService.getTipoDocumentoPN().then(function(tipodocumentos){
                $scope.tipoDocumentosPN = tipodocumentos;
            });
            MaestroService.getTiposEmpresa().then(function(tiposEmpresa){
                $scope.tiposEmpresa = tiposEmpresa;
            });

            MaestroService.getDepartamentos().then(function(departamentos){
                $scope.departamentos = departamentos;
            });

            $scope.changeDepartamento = function(departamento){
                MaestroService.getProvincias(departamento.id).then(function(provincias){
                    $scope.provincias = provincias;
                });
            }
            $scope.changeProvincia = function(provincia){
                MaestroService.getDistritos(provincia.id).then(function(distritos){
                    $scope.distritos = distritos;
                });
            }

            $scope.buscarRepresentanteLegal = function($event){
                var tipoDoc = $scope.representanteLegal.tipoDocumento.id;
                var numDoc = $scope.representanteLegal.numeroDocumento;
                if(tipoDoc === null || tipoDoc === undefined){
                    alert("Tipo documento no definido");
                    return;
                }
                if(numDoc === null || numDoc === undefined){
                    alert("Numero documento no definido");
                    return;
                }
                PersonaNaturalService.findByTipoNumeroDocumento(tipoDoc,numDoc).then(function(persona){
                    $scope.representanteLegal = persona;
                },function error(error){
                    $scope.alerts = [{ type: "warning", msg: "Representante legal no encontrado."}];
                    $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                });
                $event.preventDefault();
            }

            $scope.formCrearPersonaJuridica = {};
            $scope.crear = function(){
                if ($scope.formCrearPersonaJuridica.$valid) {
                    $scope.control.inProcess = true;
                    PersonaJuridicaService.crear($scope.personaJuridica, $scope.representanteLegal, $scope.accionistas).then(
                        function(persona){
                            $scope.control.inProcess = false;
                            $scope.control.success = true;
                            $window.close();
                        },
                        function error(error){
                            $scope.control.inProcess = false;
                            $scope.control.success = false;
                            $scope.alerts = [{ type: "danger", msg: "Error: " + error.data + "."}];
                            $scope.alerts.splice(index, 1);
                        }
                    );
                } else {
                    $scope.control.submitted = true;
                }
            }

            $scope.cancel = function () {
                $window.close();
            }

            $scope.buttonDisableState = function(){
                return $scope.control.inProcess;
            }

        }])
    .controller('AccionistaController', [ "$scope", "MaestroService", "PersonaNaturalService",
        function($scope, MaestroService, PersonaNaturalService) {

            $scope.control = {"inProcess": false, "submitted" : false};

            $scope.accionista = {
                "idTipoDocumento" : undefined,
                "numeroDocumento" : undefined
            }

            MaestroService.getTipoDocumentoPN().then(function(tipoDocumentos){
                $scope.tipoDocumentos = tipoDocumentos;
            });

            $scope.addAccionista = function() {
                if($scope.formAccionista.$valid){
                    $scope.control.inProcess = true;
                    PersonaNaturalService.findByTipoNumeroDocumento($scope.accionista.idTipoDocumento, $scope.accionista.numeroDocumento).then(
                        function(persona){
                            $scope.control.inProcess = false;
                            var obj = {
                                "porcentajeParticipacion" : 0,
                                "personaNatural" : persona
                            };
                            $scope.accionistas.push(obj);
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

            $scope.removeAccionista = function(index){
                $scope.accionistas.splice(index, 1);
                $scope.resetFocus();
            }

            $scope.clear = function(){
                $scope.accionista = {
                    "idTipoDocumento" : undefined,
                    "numeroDocumento" : ""
                }
                $scope.resetFocus();
            }

            $scope.resetFocus = function(){
                angular.element("#cmbTipoDocumentoAccionista").focus();
            }

        }]);