/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ventura.sistemafinanciero.rest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.ventura.sistemafinanciero.entity.Agencia;
import org.ventura.sistemafinanciero.entity.Beneficiario;
import org.ventura.sistemafinanciero.entity.CuentaBancaria;
import org.ventura.sistemafinanciero.entity.CuentaBancariaView;
import org.ventura.sistemafinanciero.entity.EstadocuentaBancariaView;
import org.ventura.sistemafinanciero.entity.PersonaJuridica;
import org.ventura.sistemafinanciero.entity.PersonaNatural;
import org.ventura.sistemafinanciero.entity.Socio;
import org.ventura.sistemafinanciero.entity.TipoDocumento;
import org.ventura.sistemafinanciero.entity.Titular;
import org.ventura.sistemafinanciero.entity.Trabajador;
import org.ventura.sistemafinanciero.entity.Usuario;
import org.ventura.sistemafinanciero.entity.dto.VoucherTransaccionBancaria;
import org.ventura.sistemafinanciero.entity.type.EstadoCuentaBancaria;
import org.ventura.sistemafinanciero.entity.type.TipoCuentaBancaria;
import org.ventura.sistemafinanciero.entity.type.TipoPersona;
import org.ventura.sistemafinanciero.exception.NonexistentEntityException;
import org.ventura.sistemafinanciero.exception.RollbackFailureException;
import org.ventura.sistemafinanciero.rest.dto.CuentaAhorroDTO;
import org.ventura.sistemafinanciero.rest.dto.CuentaCorrienteDTO;
import org.ventura.sistemafinanciero.rest.dto.CuentaPlazoFijoDTO;
import org.ventura.sistemafinanciero.rest.dto.TitularDTO;
import org.ventura.sistemafinanciero.service.CuentaBancariaService;
import org.ventura.sistemafinanciero.service.PersonaJuridicaService;
import org.ventura.sistemafinanciero.service.PersonaNaturalService;
import org.ventura.sistemafinanciero.service.SocioService;
import org.ventura.sistemafinanciero.service.TrabajadorService;
import org.ventura.sistemafinanciero.service.UsuarioService;

@Path("/cuentaBancaria")
public class CuentaBancariaRESTService {
    
	@EJB
	private CuentaBancariaService cuentaBancariaService;
	@EJB
	private UsuarioService usuarioService;
	@EJB
	private TrabajadorService trabajadorService;
	@EJB
	private PersonaNaturalService personaNaturalService;
	@EJB
	private PersonaJuridicaService personaJuridicaService;
	@EJB
	private SocioService socioService;
	
	@GET
	@Path("/")
	@Produces({ "application/xml", "application/json" })
	public Response findAll() {				
		List<CuentaBancaria> list = cuentaBancariaService.findAll();
		return Response.status(Response.Status.OK).entity(list).build();
	}
	
	@GET
	@Path("/view")
	@Produces({ "application/xml", "application/json" })
	public Response findAllViewByTipoEstadoCuenta(
			@QueryParam("desde") BigInteger desde,
			@QueryParam("hasta") BigInteger hasta,
			@QueryParam("tipoCuenta") TipoCuentaBancaria[] tipoCuenta,
			@QueryParam("tipoPersona") TipoPersona[] tipoPersona,
			@QueryParam("tipoEstadoCuenta") EstadoCuentaBancaria[]  tipoEstadoCuenta) {
				
		if(desde != null && desde.compareTo(BigInteger.ZERO) < 1)
			desde = BigInteger.ZERO;
		if(hasta != null && hasta.compareTo(BigInteger.ZERO) < 1)
			hasta = BigInteger.ZERO;
						
		List<CuentaBancariaView> list = cuentaBancariaService.findAllView(tipoCuenta, tipoPersona, tipoEstadoCuenta, desde, hasta);
		return Response.status(Response.Status.OK).entity(list).build();				
	}
	
	@GET
	@Path("/view/filtertext/{filterText}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findByFilterTextView(
			@PathParam("filterText") @DefaultValue("") String filterText,
			@QueryParam("desde") BigInteger desde,
			@QueryParam("hasta") BigInteger hasta,
			@QueryParam("tipoCuenta") TipoCuentaBancaria[] tipoCuenta,
			@QueryParam("tipoPersona") TipoPersona[] tipoPersona,
			@QueryParam("tipoEstadoCuenta") EstadoCuentaBancaria[] tipoEstadoCuenta) {
		if(desde != null && desde.compareTo(BigInteger.ZERO) < 1)
			desde = BigInteger.ZERO;
		if(hasta != null && hasta.compareTo(BigInteger.ZERO) < 1)
			hasta = BigInteger.ZERO;
		
		List<CuentaBancariaView> list = cuentaBancariaService.findAllView(filterText, tipoCuenta, tipoPersona, tipoEstadoCuenta, desde, hasta);
		return Response.status(Response.Status.OK).entity(list).build();				
	}
	
	@GET
	@Path("/view/count")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findByFilterText() {								
		int size = cuentaBancariaService.count();
		return Response.status(Response.Status.OK).entity(size).build();						
	}
	
	@GET
	@Path("/{id}")
	@Produces({ "application/xml", "application/json" })
	public Response findCuentaBancaria(@PathParam("id")BigInteger id) {				
		CuentaBancaria cuentaBancaria = cuentaBancariaService.findById(id);
		if(cuentaBancaria == null)
			return Response.status(Response.Status.NOT_FOUND).entity("No encontrado").build();
		else 
			return Response.status(Response.Status.OK).entity(cuentaBancaria).build();
	}
	
	@GET
	@Path("/{id}/socio")
	@Produces({ "application/xml", "application/json" })
	public Response findSocioFromCuentaBancaria(@PathParam("id")BigInteger id) {				
		CuentaBancaria cuentaBancaria = cuentaBancariaService.findById(id);
		if(cuentaBancaria != null) {
			Socio socio = socioService.find(cuentaBancaria.getIdCuentaBancaria());
			return Response.status(Response.Status.OK).entity(socio).build();	
		}					
		else {
			return Response.status(Response.Status.NOT_FOUND).entity("No encontrado").build();	
		}					
	}
	
	//falta
	@GET
	@Path("/{id}/titulares")
	@Produces({ "application/xml", "application/json" })
	public Response findTitularesActiveFromCuentaBancaria(@PathParam("id")BigInteger id) {				
		CuentaBancaria cuentaBancaria = cuentaBancariaService.findById(id);
		if(cuentaBancaria != null) {
			Set<Titular> titulares = cuentaBancariaService.getTitulares(cuentaBancaria.getIdCuentaBancaria(), true);
			return Response.status(Response.Status.OK).entity(titulares).build();	
		}					
		else {
			JsonObject model = Json.createObjectBuilder().add("message", "Cuenta no encontrada").build();
			return Response.status(Response.Status.NOT_FOUND).entity(model).build();				
		}					
	}
	
	@GET
	@Path("/{id}/titulares/all")
	@Produces({ "application/xml", "application/json" })
	public Response findTitularesAllFromCuentaBancaria(@PathParam("id")BigInteger id) {				
		CuentaBancaria cuentaBancaria = cuentaBancariaService.findById(id);
		if(cuentaBancaria != null) {
			Set<Titular> beneficiarios = cuentaBancariaService.getTitulares(cuentaBancaria.getIdCuentaBancaria(), false);
			return Response.status(Response.Status.OK).entity(beneficiarios).build();	
		}					
		else {
			JsonObject model = Json.createObjectBuilder().add("message", "Cuenta no encontrada").build();
			return Response.status(Response.Status.NOT_FOUND).entity(model).build();				
		}					
	}
	
	@GET
	@Path("/{id}/beneficiarios")
	@Produces({ "application/xml", "application/json" })
	public Response findBeneficiariosFromCuentaBancaria(@PathParam("id")BigInteger id) {				
		CuentaBancaria cuentaBancaria = cuentaBancariaService.findById(id);
		if(cuentaBancaria != null) {
			Set<Beneficiario> beneficiarios = cuentaBancariaService.getBeneficiarios(cuentaBancaria.getIdCuentaBancaria());
			return Response.status(Response.Status.OK).entity(beneficiarios).build();	
		}					
		else {
			JsonObject model = Json.createObjectBuilder().add("message", "Cuenta no encontrada").build();
			return Response.status(Response.Status.NOT_FOUND).entity(model).build();				
		}					
	}
		
	@GET
	@Path("/{id}/estadoCuenta")
	@Produces({ "application/xml", "application/json" })
	public Response getEstadoCuenta(@PathParam("id")BigInteger id, 
			@QueryParam("desde") Long desde, 
			@QueryParam("hasta") Long hasta) {	
		Date dateDesde = (desde != null ? new Date(desde) : null);
		Date dateHasta = (desde != null ? new Date(hasta) : null);
		
		CuentaBancaria cuentaBancaria = cuentaBancariaService.findById(id);
		if(cuentaBancaria != null) {
			List<EstadocuentaBancariaView> list = cuentaBancariaService.getEstadoCuenta(cuentaBancaria.getIdCuentaBancaria(),dateDesde, dateHasta);
			return Response.status(Response.Status.OK).entity(list).build();
		}					
		else {
			return Response.status(Response.Status.NOT_FOUND).entity("Cuenta no encontrado").build();	
		}						
	}
	
	/*
	@GET
	@Path("/filtertext/{filterText}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findByFilterText(@PathParam("filterText") @DefaultValue("") String filterText) {
		List<CuentaBancariaView> list = cuentaBancariaService.findAllView(filterText);
		return Response.status(Response.Status.OK).entity(list).build();
	}*/
	
	
		
	@POST
	@Path("/ahorro")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response createCuentaAhorro(
			CuentaAhorroDTO cuenta, @Context SecurityContext context) {	
		try {		
			String username = context.getUserPrincipal().getName();
			Usuario currentUser = usuarioService.findByUsername(username);
			Trabajador trabajador;
			if (currentUser != null)
				trabajador = trabajadorService.findByUsuario(currentUser.getIdUsuario());
			else
				return Response.status(Response.Status.NOT_FOUND).entity("Usuario no encontrado").build();
			Agencia agencia = trabajadorService.getAgencia(trabajador.getIdTrabajador());
			if(agencia == null)
				return Response.status(Response.Status.NOT_FOUND).entity("Usuario no encontrado").build();
			
			BigInteger idMoneda = cuenta.getIdMoneda();
			BigDecimal tasaInteres = cuenta.getTasaInteres();
			TipoPersona tipoPersona = cuenta.getTipoPersona();
			BigInteger idPersona = cuenta.getIdPersona();
			int cantRetirantes = cuenta.getCantRetirantes();
			List<BigInteger> titulares = cuenta.getTitulares();
			List<Beneficiario> beneficiarios = cuenta.getBeneficiarios();
			
			BigInteger idCuenta = cuentaBancariaService.createCuentaAhorro(agencia.getIdAgencia(), idMoneda, tasaInteres, tipoPersona, idPersona, cantRetirantes, titulares, beneficiarios);
			JsonObject model = Json.createObjectBuilder().add("message", "Cuenta creada").add("id", idCuenta).build();
			return Response.status(Response.Status.OK).entity(model).build();
		} catch (NonexistentEntityException e) {
			JsonObject model = Json.createObjectBuilder().add("message", e.getMessage()).build();
			return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
		} catch (RollbackFailureException e) {
			JsonObject model = Json.createObjectBuilder().add("message", e.getMessage()).build();
			return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
		} catch (EJBException e) {
			JsonObject model = Json.createObjectBuilder().add("message", e.getMessage()).build();
			return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
		}		
	}
	
	@POST
	@Path("/corriente")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response createCuentaCorriente(
			CuentaCorrienteDTO cuenta, @Context SecurityContext context) {	
		try {		
			JsonObject model = null;
			
			String username = context.getUserPrincipal().getName();
			Usuario currentUser = usuarioService.findByUsername(username);
			Trabajador trabajador;
			if (currentUser != null) {
				trabajador = trabajadorService.findByUsuario(currentUser.getIdUsuario());
			}				
			else {
				model = Json.createObjectBuilder().add("message", "Usuario no encontrado").build();
				return Response.status(Response.Status.NOT_FOUND).entity(model).build();				
			}				
			Agencia agencia = trabajadorService.getAgencia(trabajador.getIdTrabajador());
			if(agencia == null){
				model = Json.createObjectBuilder().add("message", "Usuario no encontrado").build();
				return Response.status(Response.Status.NOT_FOUND).entity(model).build();	
			}
					
			TipoPersona tipoPersona = cuenta.getTipoPersona();
			BigInteger idTipoDocumento = cuenta.getIdTipoDocumento();
			String numeroDocumento = cuenta.getNumeroDocumento();	
			BigInteger idPersona = null;
			switch (tipoPersona) {
			case NATURAL:
				PersonaNatural personaNatural = personaNaturalService.find(idTipoDocumento, numeroDocumento);
				if(personaNatural == null){
					model = Json.createObjectBuilder().add("message", "Socio no encontrado").build();
					return Response.status(Response.Status.NOT_FOUND).entity(model).build();
				}
				idPersona = personaNatural.getIdPersonaNatural();
				break;
			case JURIDICA:
				PersonaJuridica personaJuridica = personaJuridicaService.find(idTipoDocumento, numeroDocumento);
				if(personaJuridica == null){
					model = Json.createObjectBuilder().add("message", "Socio no encontrado").build();
					return Response.status(Response.Status.NOT_FOUND).entity(model).build();
				}
				idPersona = personaJuridica.getIdPersonaJuridica();
				break;
			default:
				break;
			}
														
			BigInteger idMoneda = cuenta.getIdMoneda();		
			BigDecimal tasaInteres = cuenta.getTasaInteres();
			int cantRetirantes = cuenta.getCantRetirantes();
			List<BigInteger> titulares = cuenta.getTitulares();
			List<Beneficiario> beneficiarios = cuenta.getBeneficiarios();
			
			BigInteger idCuenta = cuentaBancariaService.createCuentaCorriente(agencia.getIdAgencia(), idMoneda, tasaInteres, tipoPersona, idPersona, cantRetirantes, titulares, beneficiarios);			
			model = Json.createObjectBuilder().add("message", "Cuenta creada").add("id", idCuenta).build();
			return Response.status(Response.Status.OK).entity(model).build();
		} catch (NonexistentEntityException e) {
			JsonObject model = Json.createObjectBuilder().add("message", e.getMessage()).build();
			return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
		} catch (RollbackFailureException e) {
			JsonObject model = Json.createObjectBuilder().add("message", e.getMessage()).build();
			return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
		} catch (EJBException e) {
			JsonObject model = Json.createObjectBuilder().add("message", e.getMessage()).build();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(model).build();
		}			
	}
	
	@POST
	@Path("/plazoFijo")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response createCuentaPlazoFijo(CuentaPlazoFijoDTO cuenta, @Context SecurityContext context) {	
		try {		
			JsonObject model = null;
			
			String username = context.getUserPrincipal().getName();
			Usuario currentUser = usuarioService.findByUsername(username);
			Trabajador trabajador;
			if (currentUser != null) {
				trabajador = trabajadorService.findByUsuario(currentUser.getIdUsuario());
			}				
			else {
				model = Json.createObjectBuilder().add("message", "Usuario no encontrado").build();
				return Response.status(Response.Status.NOT_FOUND).entity(model).build();				
			}				
			Agencia agencia = trabajadorService.getAgencia(trabajador.getIdTrabajador());
			if(agencia == null){
				model = Json.createObjectBuilder().add("message", "Usuario no encontrado").build();
				return Response.status(Response.Status.NOT_FOUND).entity(model).build();	
			}
					
			TipoPersona tipoPersona = cuenta.getTipoPersona();
			BigInteger idTipoDocumento = cuenta.getIdTipoDocumento();
			String numeroDocumento = cuenta.getNumeroDocumento();	
			BigInteger idPersona = null;
			switch (tipoPersona) {
			case NATURAL:
				PersonaNatural personaNatural = personaNaturalService.find(idTipoDocumento, numeroDocumento);
				if(personaNatural == null){
					model = Json.createObjectBuilder().add("message", "Socio no encontrado").build();
					return Response.status(Response.Status.NOT_FOUND).entity(model).build();
				}
				idPersona = personaNatural.getIdPersonaNatural();
				break;
			case JURIDICA:
				PersonaJuridica personaJuridica = personaJuridicaService.find(idTipoDocumento, numeroDocumento);
				if(personaJuridica == null){
					model = Json.createObjectBuilder().add("message", "Socio no encontrado").build();
					return Response.status(Response.Status.NOT_FOUND).entity(model).build();
				}
				idPersona = personaJuridica.getIdPersonaJuridica();
				break;
			default:
				break;
			}
														
			BigInteger idMoneda = cuenta.getIdMoneda();
			BigDecimal monto = cuenta.getMonto();
			int periodo = cuenta.getPeriodo();
			BigDecimal tasaInteres = cuenta.getTasaInteres();
			int cantRetirantes = cuenta.getCantRetirantes();
			List<BigInteger> titulares = cuenta.getTitulares();
			List<Beneficiario> beneficiarios = cuenta.getBeneficiarios();
			
			BigInteger[] result = cuentaBancariaService.createCuentaPlazoFijo(agencia.getIdAgencia(), idMoneda, tipoPersona, idPersona, cantRetirantes, monto, periodo, tasaInteres, titulares, beneficiarios);			
			model = Json.createObjectBuilder().add("message", "Cuenta creada").add("id", result[0]).add("idTransaccion", result[1]).build();
			return Response.status(Response.Status.OK).entity(model).build();
		} catch (NonexistentEntityException e) {
			JsonObject model = Json.createObjectBuilder().add("message", e.getMessage()).build();
			return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
		} catch (RollbackFailureException e) {
			JsonObject model = Json.createObjectBuilder().add("message", e.getMessage()).build();
			return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
		} catch (EJBException e) {
			JsonObject model = Json.createObjectBuilder().add("message", e.getMessage()).build();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(model).build();
		}		
	}
	
	@POST
	@Path("/{id}/titular")
	@Produces({ "application/xml", "application/json" })
	public Response createTitular(@PathParam("id")BigInteger idCuenta, TitularDTO titularDTO) {	
		if(idCuenta != null && titularDTO != null){
			CuentaBancaria cuentaBancaria = cuentaBancariaService.findById(idCuenta);
			if(cuentaBancaria != null) {
				BigInteger idTitular;
				try {
					Titular titular = new Titular();
					
					TipoDocumento tipoDocumento = new TipoDocumento();
					tipoDocumento.setIdTipoDocumento(titularDTO.getIdTipoDocumento());
					
					PersonaNatural personaNatural = new PersonaNatural();
					personaNatural.setNumeroDocumento(titularDTO.getNumeroDocumento());
					personaNatural.setTipoDocumento(tipoDocumento);
					
					titular.setPersonaNatural(personaNatural);
					
					idTitular = cuentaBancariaService.addTitular(idCuenta, titular);
					JsonObject model = Json.createObjectBuilder().add("message", "Titular creado").add("id", idTitular).build();
					return Response.status(Response.Status.OK).entity(model).build();
				} catch (RollbackFailureException e) {
					JsonObject model = Json.createObjectBuilder().add("message", e.getMessage()).build();
					return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
				}					
			}					
			else {
				JsonObject model = Json.createObjectBuilder().add("message", "Cuenta no encontrada").build();
				return Response.status(Response.Status.NOT_FOUND).entity(model).build();				
			}	
		}			
		else {
			JsonObject model = Json.createObjectBuilder().add("message", "solicitud invalida").build();
			return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
		}
								
	}
	
	@POST
	@Path("/{id}/beneficiario")
	@Produces({ "application/xml", "application/json" })
	public Response createBeneficiario(@PathParam("id")BigInteger id, Beneficiario beneficiario) {	
		if(id != null && beneficiario != null){
			CuentaBancaria cuentaBancaria = cuentaBancariaService.findById(id);
			if(cuentaBancaria != null) {
				BigInteger idBeneficiario;
				try {
					idBeneficiario = cuentaBancariaService.addBeneficiario(id, beneficiario);
					JsonObject model = Json.createObjectBuilder().add("message", "beneficicario creado").add("id", idBeneficiario).build();
					return Response.status(Response.Status.OK).entity(model).build();
				} catch (RollbackFailureException e) {
					JsonObject model = Json.createObjectBuilder().add("message", e.getMessage()).build();
					return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(model).build();
				}					
			}					
			else {
				JsonObject model = Json.createObjectBuilder().add("message", "Cuenta no encontrada").build();
				return Response.status(Response.Status.NOT_FOUND).entity(model).build();				
			}	
		}			
		else {
			JsonObject model = Json.createObjectBuilder().add("message", "solicitud invalida").build();
			return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
		}
								
	}
	
	@PUT	
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response updateCuentaBancaria(CuentaBancaria caja) {				
		return null;
	}
	
	@DELETE
	@Path("/{id}")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response updateCuentaBancaria(@PathParam("id") int id) {				
		return null;
	}
	
	@GET
    @Path("{id}/voucherCuentaBancaria")  
    @Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
    public Response getVoucherCuentaBancaria(@PathParam("id") BigInteger idTransaccionBancaria){
    	VoucherTransaccionBancaria voucherTransaccionBancaria = cuentaBancariaService.getVoucherCuentaBancaria(idTransaccionBancaria);    	
		return Response.status(Response.Status.OK).entity(voucherTransaccionBancaria).build(); 
    }
}
