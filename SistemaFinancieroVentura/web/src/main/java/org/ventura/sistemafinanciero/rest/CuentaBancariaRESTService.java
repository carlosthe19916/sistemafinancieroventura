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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.ventura.sistemafinanciero.entity.Agencia;
import org.ventura.sistemafinanciero.entity.Beneficiario;
import org.ventura.sistemafinanciero.entity.CuentaBancaria;
import org.ventura.sistemafinanciero.entity.CuentaBancariaView;
import org.ventura.sistemafinanciero.entity.Moneda;
import org.ventura.sistemafinanciero.entity.PersonaJuridica;
import org.ventura.sistemafinanciero.entity.PersonaNatural;
import org.ventura.sistemafinanciero.entity.Trabajador;
import org.ventura.sistemafinanciero.entity.Usuario;
import org.ventura.sistemafinanciero.entity.type.EstadoCuentaBancaria;
import org.ventura.sistemafinanciero.entity.type.TipoCuentaBancaria;
import org.ventura.sistemafinanciero.entity.type.TipoPersona;
import org.ventura.sistemafinanciero.exception.NonexistentEntityException;
import org.ventura.sistemafinanciero.exception.RollbackFailureException;
import org.ventura.sistemafinanciero.rest.dto.BuscarCuentaViewDTO;
import org.ventura.sistemafinanciero.rest.dto.CuentaAhorroDTO;
import org.ventura.sistemafinanciero.rest.dto.CuentaCorrienteDTO;
import org.ventura.sistemafinanciero.rest.dto.CuentaPlazoFijoDTO;
import org.ventura.sistemafinanciero.service.CuentaBancariaService;
import org.ventura.sistemafinanciero.service.PersonaJuridicaService;
import org.ventura.sistemafinanciero.service.PersonaNaturalService;
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
	public Response findAllView() {
		Set<CuentaBancariaView> list = cuentaBancariaService.findAllView();
		return Response.status(Response.Status.OK).entity(list).build();
	}
	
	@POST
	@Path("/view/tipoCuenta/estadoCuenta")
	@Produces({ "application/xml", "application/json" })
	public Response findAllViewByTipoEstadoCuenta(BuscarCuentaViewDTO dto) {
		List<TipoPersona> tipoPersonaList = dto.getTipoPersonaList();
		List<TipoCuentaBancaria> tipoCuentaList = dto.getTipoCuentaList();		
		List<EstadoCuentaBancaria> estadoCuentaList = dto.getEstadoCuentaList();
		List<Moneda> monedaList = dto.getMonedaList();
		
		List<CuentaBancariaView> list = cuentaBancariaService.findAllView(tipoPersonaList, tipoCuentaList, estadoCuentaList, monedaList);
		return Response.status(Response.Status.OK).entity(list).build();
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
	@Path("/filtertext/{filterText}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findByFilterText(
			@PathParam("filterText") @DefaultValue("") String filterText) {
		Set<CuentaBancaria> list = cuentaBancariaService.findByFilterText(filterText);
		return Response.status(Response.Status.OK).entity(list).build();
	}
	
	@GET
	@Path("/filtertext/{filterText}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findByFilterTextView(
			@PathParam("filterText") @DefaultValue("") String filterText) {
		Set<CuentaBancariaView> list = cuentaBancariaService.findByFilterTextView(filterText);
		return Response.status(Response.Status.OK).entity(list).build();
	}
		
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
			TipoPersona tipoPersona = cuenta.getTipoPersona();
			BigInteger idPersona = cuenta.getIdPersona();
			int cantRetirantes = cuenta.getCantRetirantes();
			List<BigInteger> titulares = cuenta.getTitulares();
			List<Beneficiario> beneficiarios = cuenta.getBeneficiarios();
			
			BigInteger idCuenta = cuentaBancariaService.createCuentaAhorro(agencia.getIdAgencia(), idMoneda, tipoPersona, idPersona, cantRetirantes, titulares, beneficiarios);
			JsonObject model = Json.createObjectBuilder().add("message", "Cuenta creada").add("id", idCuenta).build();
			return Response.status(Response.Status.OK).entity(model).build();
		} catch (NonexistentEntityException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (EJBException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
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
				PersonaNatural personaNatural = personaNaturalService.findByTipoNumeroDocumento(idTipoDocumento, numeroDocumento);
				if(personaNatural == null){
					model = Json.createObjectBuilder().add("message", "Socio no encontrado").build();
					return Response.status(Response.Status.NOT_FOUND).entity(model).build();
				}
				idPersona = personaNatural.getIdPersonaNatural();
				break;
			case JURIDICA:
				PersonaJuridica personaJuridica = personaJuridicaService.findByTipoNumeroDocumento(idTipoDocumento, numeroDocumento);
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
			int cantRetirantes = cuenta.getCantRetirantes();
			List<BigInteger> titulares = cuenta.getTitulares();
			List<Beneficiario> beneficiarios = cuenta.getBeneficiarios();
			
			BigInteger idCuenta = cuentaBancariaService.createCuentaCorriente(agencia.getIdAgencia(), idMoneda, tipoPersona, idPersona, cantRetirantes, titulares, beneficiarios);			
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
				PersonaNatural personaNatural = personaNaturalService.findByTipoNumeroDocumento(idTipoDocumento, numeroDocumento);
				if(personaNatural == null){
					model = Json.createObjectBuilder().add("message", "Socio no encontrado").build();
					return Response.status(Response.Status.NOT_FOUND).entity(model).build();
				}
				idPersona = personaNatural.getIdPersonaNatural();
				break;
			case JURIDICA:
				PersonaJuridica personaJuridica = personaJuridicaService.findByTipoNumeroDocumento(idTipoDocumento, numeroDocumento);
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
			
			BigInteger idCuenta = cuentaBancariaService.createCuentaPlazoFijo(agencia.getIdAgencia(), idMoneda, tipoPersona, idPersona, cantRetirantes, monto, periodo, tasaInteres, titulares, beneficiarios);			
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
}
