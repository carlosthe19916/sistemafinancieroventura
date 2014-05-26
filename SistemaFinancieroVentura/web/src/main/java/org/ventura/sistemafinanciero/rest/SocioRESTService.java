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

import java.math.BigInteger;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.ventura.sistemafinanciero.entity.CuentaAporte;
import org.ventura.sistemafinanciero.entity.CuentaBancaria;
import org.ventura.sistemafinanciero.entity.PersonaJuridica;
import org.ventura.sistemafinanciero.entity.PersonaNatural;
import org.ventura.sistemafinanciero.entity.Socio;
import org.ventura.sistemafinanciero.entity.SocioView;
import org.ventura.sistemafinanciero.entity.type.TipoPersona;
import org.ventura.sistemafinanciero.exception.RollbackFailureException;
import org.ventura.sistemafinanciero.service.SocioService;

@Path("/socio")
@Stateless
public class SocioRESTService {
    
	@EJB
	private SocioService socioService;
	
	@GET
	@Path("/{id}")
	@Produces({ "application/xml", "application/json" })
	public Response getSocio(@PathParam("id") @DefaultValue("null") BigInteger id) {				
		if(id == null)
			return Response.status(Response.Status.BAD_REQUEST).entity("id no valido").build();
		Socio socio = socioService.findById(id);
		if(socio == null)
			return Response.status(Response.Status.NOT_FOUND).entity("Socio no encontrado").build();
		else 
			return Response.status(Response.Status.OK).entity(socio).build();
	}
	
	@GET
	@Path("/{id}/cuentaAporte")
	@Produces({ "application/xml", "application/json" })
	public Response getCuentaAporte(@PathParam("id")BigInteger id) {				
		if(id == null)
			return Response.status(Response.Status.BAD_REQUEST).entity("id no valido").build();
		Socio socio = socioService.findById(id);
		if(socio == null){
			return Response.status(Response.Status.NOT_FOUND).entity("Socio no encontrado").build();	
		}			
		else {
			CuentaAporte ctaAporte= socioService.getCuentaAporte(socio.getIdSocio());	
			if(ctaAporte == null)
				return Response.status(Response.Status.NOT_FOUND).entity(ctaAporte).build();
			else 
				return Response.status(Response.Status.OK).entity(ctaAporte).build();
		}			
	}
	
	@GET
	@Path("/{id}/personaNatural")
	@Produces({ "application/xml", "application/json" })
	public Response getPersonaNatural(@PathParam("id")BigInteger id) {				
		if(id == null)
			return Response.status(Response.Status.BAD_REQUEST).entity("id no valido").build();
		Socio socio = socioService.findById(id);
		if(socio == null){
			return Response.status(Response.Status.NOT_FOUND).entity("Socio no encontrado").build();	
		}			
		else {
			PersonaNatural persona= socioService.getPersonaNatural(socio.getIdSocio());	
			if(persona == null)
				return Response.status(Response.Status.NOT_FOUND).entity("No encontrado").build();
			else 
				return Response.status(Response.Status.OK).entity(persona).build();
		}	
	}
	
	@GET
	@Path("/{id}/personaJuridica")
	@Produces({ "application/xml", "application/json" })
	public Response getPersonaJuridica(@PathParam("id")BigInteger id) {				
		if(id == null)
			return Response.status(Response.Status.BAD_REQUEST).entity("id no valido").build();
		Socio socio = socioService.findById(id);
		if(socio == null){
			return Response.status(Response.Status.NOT_FOUND).entity("Socio no encontrado").build();	
		}			
		else {
			PersonaJuridica persona= socioService.getPersonaJuridica(socio.getIdSocio());	
			if(persona == null)
				return Response.status(Response.Status.NOT_FOUND).entity("No encontrado").build();
			else
				return Response.status(Response.Status.OK).entity(persona).build();
		}	
	}
	
	@GET
	@Path("/{id}/apoderado")
	@Produces({ "application/xml", "application/json" })
	public Response getApoderado(@PathParam("id")BigInteger id) {				
		if(id == null)
			return Response.status(Response.Status.BAD_REQUEST).entity("id no valido").build();
		Socio socio = socioService.findById(id);
		if(socio == null){
			return Response.status(Response.Status.NOT_FOUND).entity("Socio no encontrado").build();	
		}			
		else {
			PersonaNatural persona= socioService.getApoderado(socio.getIdSocio());	
			if(persona == null)
				return Response.status(Response.Status.NOT_FOUND).entity("No encontrado").build();
			else
				return Response.status(Response.Status.OK).entity(persona).build();
		}	
	}
	
	@GET
	@Path("/{id}/cuentasBancarias")
	@Produces({ "application/xml", "application/json" })
	public Response getCuentasBancarias(@PathParam("id")BigInteger id) {				
		if(id == null)
			return Response.status(Response.Status.BAD_REQUEST).entity("id no valido").build();
		Socio socio = socioService.findById(id);
		if(socio == null){
			return Response.status(Response.Status.NOT_FOUND).entity("Socio no encontrado").build();	
		}			
		else {
			Set<CuentaBancaria> cuentas= socioService.getCuentasBancarias(socio.getIdSocio());	
			if(cuentas == null)
				return Response.status(Response.Status.NOT_FOUND).entity("No encontrado").build();
			else
				return Response.status(Response.Status.OK).entity(cuentas).build();
		}	
	}
     
	@GET
	@Path("/filtertext/{filterText}")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<SocioView> findByFilterText(@PathParam("filterText") @DefaultValue("") String filterText) {		
		Set<SocioView> list = socioService.findByFilterText(filterText);	
		return list;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Set<SocioView> listAll() {
		Set<SocioView> list = socioService.findAllView();		
		return list;
	}
	
	@POST
	@Produces({ "application/xml", "application/json" })
	public Response createSocio(@FormParam("tipoPersona") @DefaultValue("null") TipoPersona tipoPersona,
			@FormParam("idTipoDocumentoSocio") @DefaultValue("null") BigInteger tipoDocumentoSocio,
			@FormParam("numeroDocumentoSocio") @DefaultValue("null") String numeroDocumentoSocio,
			@FormParam("idTipoDocumentoApoderado") @DefaultValue("null") BigInteger tipoDocumentoApoderado,
			@FormParam("numeroDocumentoApoderado") @DefaultValue("null") String numeroDocumentoApoderado) {
		try {			
			BigInteger idSocio = socioService.create(tipoPersona, tipoDocumentoSocio, numeroDocumentoSocio, tipoDocumentoApoderado, numeroDocumentoApoderado);
			JsonObject model = Json.createObjectBuilder().add("message", "Socio creado").add("id", idSocio).build();
			return Response.status(Response.Status.OK).entity(model).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (EJBException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		} 
	}				
	
	@DELETE
	@Path("/{id}")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response deleteSocio(@PathParam("id") int id) {				
		return null;
	}
}
