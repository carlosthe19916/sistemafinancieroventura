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
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.ventura.sistemafinanciero.entity.Beneficiario;
import org.ventura.sistemafinanciero.entity.CuentaBancaria;
import org.ventura.sistemafinanciero.entity.type.TipoPersona;
import org.ventura.sistemafinanciero.exception.RollbackFailureException;
import org.ventura.sistemafinanciero.rest.dto.CrearCuentaAhorro;
import org.ventura.sistemafinanciero.service.CuentaBancariaService;

@Path("/cuentaBancaria")
public class CuentaBancariaRESTService {
    
	@EJB
	private CuentaBancariaService cuentaBancariaService;
	
	@GET
	@Path("/")
	@Produces({ "application/xml", "application/json" })
	public Response findAll() {				
		List<CuentaBancaria> list = cuentaBancariaService.findAll();
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
	
	@POST
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response createCuentaBancaria() {				
		return null;
	}
	
	@POST
	@Path("/ahorro")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response createCuentaAhorro(
			CrearCuentaAhorro cuenta) {	
		try {		
			BigInteger idMoneda = cuenta.getIdMoneda();
			TipoPersona tipoPersona = cuenta.getTipoPersona();
			BigInteger idPersona = cuenta.getIdPersona();
			int cantRetirantes = cuenta.getCantRetirantes();
			List<BigInteger> titulares = cuenta.getTitulares();
			List<Beneficiario> beneficiarios = cuenta.getBeneficiarios();
			
			BigInteger idCuenta = cuentaBancariaService.createCuentaAhorro(idMoneda, tipoPersona, idPersona, cantRetirantes, titulares, beneficiarios);
			JsonObject model = Json.createObjectBuilder().add("message", "Cuenta creada").add("id", idCuenta).build();
			return Response.status(Response.Status.OK).entity(model).build();
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
			@FormParam("tipoPersona") TipoPersona tipoPersona,
			@FormParam("idPersona") BigInteger idPersona,
			@FormParam("titulares") List<BigInteger> titulares) {	
		try {
			/*BigInteger idCuenta = cuentaBancariaService.createCuentaCorriente(tipoPersona, idPersona, titulares);
			JsonObject model = Json.createObjectBuilder().add("message", "Cuenta creada").add("id", null).build();
			return Response.status(Response.Status.OK).entity(model).build();*/
			return null;
		} /*catch (RollbackFailureException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		} */catch (EJBException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}		
	}
	
	@POST
	@Path("/plazoFijo")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response createCuentaPlazoFijo(
			@FormParam("tipoPersona") TipoPersona tipoPersona,
			@FormParam("idPersona") BigInteger idPersona,
			@FormParam("monto") BigDecimal monto,
			@FormParam("periodo") int periodo,
			@FormParam("interes") BigDecimal interes,
			@FormParam("titulares") List<BigInteger> titulares) {	
		try {
			//BigInteger idCuenta = cuentaBancariaService.createCuentaPlazoFijo(tipoPersona, idPersona, monto, periodo, interes, titulares);
			JsonObject model = Json.createObjectBuilder().add("message", "Cuenta creada").add("id", 1).build();
			return Response.status(Response.Status.OK).entity(model).build();
		}/* catch (RollbackFailureException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}*/ catch (EJBException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
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
