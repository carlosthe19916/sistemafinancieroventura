package org.ventura.sistemafinanciero.rest;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.ventura.sistemafinanciero.entity.Accionista;
import org.ventura.sistemafinanciero.entity.PersonaJuridica;
import org.ventura.sistemafinanciero.exception.PreexistingEntityException;
import org.ventura.sistemafinanciero.exception.RollbackFailureException;
import org.ventura.sistemafinanciero.service.PersonaJuridicaService;

@Path("/personaJuridica")
public class PersonaJuridicaRESTService {

	private Logger log;

	@Inject
	private Validator validator;
	
	@EJB
	private PersonaJuridicaService personaJuridicaService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<PersonaJuridica> listAll() {
		List<PersonaJuridica> list = personaJuridicaService.findAll();
		return list;
	}

	
	@POST
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response create(PersonaJuridica personaJuridica) {
		try {
			Set<Accionista> accionistas = personaJuridica.getAccionistas();
			for (Accionista accionista : accionistas) {
				accionista.toString();
			}
			BigInteger idPersona = personaJuridicaService.crear(personaJuridica);
			JsonObject model = Json.createObjectBuilder().add("message", "persona creada").add("id", idPersona).build();
			return Response.status(Response.Status.OK).entity(model).build();
		} catch (PreexistingEntityException e) {
			JsonObject model = Json.createObjectBuilder().add("message", "Persona ya existente").build();
			return Response.status(Response.Status.CONFLICT).entity(model).build();
		} catch (RollbackFailureException e) {
			JsonObject model = Json.createObjectBuilder().add("message", e.getMessage()).build();
			return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
		} catch (EJBException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		} 
	}

	@GET
	@Path("/{idtipodocumento}/{numerodocumento}")
	@Produces({ "application/xml", "application/json" })
	public Response findByTipoNumeroDocumento(
			@PathParam("idtipodocumento") @DefaultValue("-1") BigInteger idtipodocumento,
			@PathParam("numerodocumento") @DefaultValue("") String numerodocumento) {
		try {
			PersonaJuridica personaJuridica = personaJuridicaService.findByTipoNumeroDocumento(idtipodocumento, numerodocumento);
			 if(personaJuridica == null){
				 JsonObject model = Json.createObjectBuilder().add("message", "Persona no encontrada").build();
				 return Response.status(Response.Status.NOT_FOUND).entity(model).build();
			 }				
			 else {
				 return Response.status(Response.Status.OK).entity(personaJuridica).build(); 
			 }				 
		} catch (EJBException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}		 	
	}

}
