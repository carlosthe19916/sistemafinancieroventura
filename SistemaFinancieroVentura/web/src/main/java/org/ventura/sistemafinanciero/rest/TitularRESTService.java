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

import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.ventura.sistemafinanciero.entity.Beneficiario;
import org.ventura.sistemafinanciero.entity.Titular;
import org.ventura.sistemafinanciero.exception.NonexistentEntityException;
import org.ventura.sistemafinanciero.exception.PreexistingEntityException;
import org.ventura.sistemafinanciero.service.BeneficiarioService;
import org.ventura.sistemafinanciero.service.TitularService;

@Path("/titular")
public class TitularRESTService {
    
	@EJB
	private TitularService titularService;
	
	@GET
	@Path("/{id}")
	@Produces({ "application/xml", "application/json" })
	public Response findTitular(@PathParam("id")BigInteger id) {				
		if(id != null){
			Titular titular = titularService.findById(id);
			if(titular != null){				
				return Response.status(Response.Status.OK).entity(titular).build();
			} else {
				JsonObject model = Json.createObjectBuilder().add("message", "Titular no encontrado").build();
				return Response.status(Response.Status.NOT_FOUND).entity(model).build();		
			}
		}			
		else {
			JsonObject model = Json.createObjectBuilder().add("message", "solicitud invalida").build();
			return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
		}
	}
     
	
	@POST
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response createTitular() {				
		return null;
	}
	
	@PUT	
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response updateBeneficiario(Titular titular) {
		BigInteger idTitular = titular.getIdTitular();
		if(idTitular != null){
			try {
				Titular titularDB = titularService.findById(idTitular);
				titular.setCuentaBancaria(titularDB.getCuentaBancaria());
				titularService.update(idTitular, titular);
				JsonObject model = Json.createObjectBuilder().add("message", "Titular actualizado").build();
				return Response.status(Response.Status.OK).entity(model).build();
			} catch (NonexistentEntityException e) {
				JsonObject model = Json.createObjectBuilder().add("message", e.getMessage()).build();
				return Response.status(Response.Status.NOT_FOUND).entity(model).build();
			} catch (PreexistingEntityException e) {
				JsonObject model = Json.createObjectBuilder().add("message", e.getMessage()).build();
				return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
			}			
		} else {
			JsonObject model = Json.createObjectBuilder().add("message", "solicitud invalida").build();
			return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
		}		
	}
	
	@DELETE
	@Path("/{id}")
	@Produces({ "application/xml", "application/json" })
	public Response deteleTitular(@PathParam("id") BigInteger id) {
		if(id!= null){
			try {
				titularService.delete(id);
				JsonObject model = Json.createObjectBuilder().add("message", "Titular eliminado").build();
				return Response.status(Response.Status.OK).entity(model).build();
			} catch (NonexistentEntityException e) {
				JsonObject model = Json.createObjectBuilder().add("message", e.getMessage()).build();
				return Response.status(Response.Status.NOT_FOUND).entity(model).build();
			}
		} else {
			JsonObject model = Json.createObjectBuilder().add("message", "solicitud invalida").build();
			return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
		}
		
	}
}
