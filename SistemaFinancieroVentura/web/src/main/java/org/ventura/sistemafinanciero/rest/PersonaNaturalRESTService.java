package org.ventura.sistemafinanciero.rest;

import java.math.BigInteger;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.ventura.sistemafinanciero.entity.PersonaNatural;
import org.ventura.sistemafinanciero.exception.NonexistentEntityException;
import org.ventura.sistemafinanciero.exception.PreexistingEntityException;
import org.ventura.sistemafinanciero.service.PersonaNaturalService;
import org.ventura.sistemafinanciero.service.TrabajadorService;
import org.ventura.sistemafinanciero.service.UsuarioService;

@Path("/personaNatural")
public class PersonaNaturalRESTService {

	private Logger log;

	@EJB
	private PersonaNaturalService personaNaturalService;
	
	@EJB
	private UsuarioService usuarioService;
	
	@EJB
	private TrabajadorService trabajadorService;
	
	
	//cuerpo de la respuesta
	private final String ID_RESPONSE = "id";
	private final String MESSAGE_RESPONSE = "message";
	
	//mensajes
	private final String SUCCESS_MESSAGE = "Success";
	private final String NOT_FOUND_MESSAGE = "Persona no encontrada";
	private final String BAD_REQUEST_MESSAGE = "Datos invalidos";
	private final String CONFLICT_MESSAGE = "Persona ya existente";
	
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findById(@PathParam("id") @DefaultValue("-1") BigInteger id) {				
		Response result = null;
		JsonObject model = null;		
		if(id != null){
			PersonaNatural persona = personaNaturalService.findById(id);
			if(persona != null){				
				result = Response.status(Response.Status.OK).entity(persona).build();	
			} else {
				model = Json.createObjectBuilder().add(MESSAGE_RESPONSE, NOT_FOUND_MESSAGE).build();
				result = Response.status(Response.Status.NOT_FOUND).entity(model).build();	
			}
		} else {
			model = Json.createObjectBuilder().add(MESSAGE_RESPONSE, BAD_REQUEST_MESSAGE).build();
			result = Response.status(Response.Status.BAD_REQUEST).entity(model).build();
		}	
		return result;
	}

	@GET
	@Path("/{idtipodocumento}/{numerodocumento}")
	@Produces({ "application/xml", "application/json" })
	public Response findByTipoNumeroDocumento(
			@PathParam("idtipodocumento") @DefaultValue("-1") BigInteger idtipodocumento,
			@PathParam("numerodocumento") @DefaultValue("") String numerodocumento) {
		Response result = null;
		JsonObject model = null;
		if(idtipodocumento == null || numerodocumento == null || numerodocumento.isEmpty() || numerodocumento.trim().isEmpty()){
			 model = Json.createObjectBuilder().add(MESSAGE_RESPONSE, BAD_REQUEST_MESSAGE).build();
			 result = Response.status(Response.Status.BAD_REQUEST).entity(model).build();
		}
		try {							
			PersonaNatural persona = personaNaturalService.find(idtipodocumento, numerodocumento);
			if(persona != null) {				
				result = Response.status(Response.Status.OK).entity(persona).build(); 				
			}				
			else {
				model = Json.createObjectBuilder().add(MESSAGE_RESPONSE, NOT_FOUND_MESSAGE).build();
				result = Response.status(Response.Status.NOT_FOUND).entity(model).build();
			}				 
		} catch (EJBException e) {
			log.log(Level.SEVERE, e.getMessage());
			model = Json.createObjectBuilder().add(MESSAGE_RESPONSE, e.getMessage()).build();
			result = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(model).build();
		}	
		return result;
	}

	@GET
	@Path("/filtertext/{filterText}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findByFilterText(
			@PathParam("filterText") @DefaultValue("") String filterText,
			@QueryParam("desde") BigInteger desde,
			@QueryParam("hasta") BigInteger hasta) {
		if(desde == null || hasta == null){
			desde = null;
			hasta = null;
		}	
		if(desde != null && desde.compareTo(BigInteger.ZERO) < 1)
			desde = BigInteger.ZERO;
		if(hasta != null && hasta.compareTo(BigInteger.ZERO) < 1)
			hasta = BigInteger.ZERO;
		BigInteger[] range = null;
		if(desde != null && hasta != null){
			range = new BigInteger[]{desde, hasta};
		}
		
		List<PersonaNatural> list = personaNaturalService.findAll(filterText, range);
		Response result = null;
		JsonObject model = null;
		if(list != null){
			result = Response.status(Response.Status.OK).entity(list).build();
		} else {
			model = Json.createObjectBuilder().add(MESSAGE_RESPONSE, NOT_FOUND_MESSAGE).build();
			result = Response.status(Response.Status.NOT_FOUND).entity(model).build();	
		}
		return result;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response listAll(
			@QueryParam("desde") BigInteger desde,
			@QueryParam("hasta") BigInteger hasta) {
		if(desde == null || hasta == null){
			desde = null;
			hasta = null;
		}	
		if(desde != null && desde.compareTo(BigInteger.ZERO) < 1)
			desde = BigInteger.ZERO;
		if(hasta != null && hasta.compareTo(BigInteger.ZERO) < 1)
			hasta = BigInteger.ZERO;
		BigInteger[] range = null;
		if(desde != null && hasta != null){
			range = new BigInteger[]{desde, hasta};
		}
		
		List<PersonaNatural> list = personaNaturalService.findAll(range);		
		Response result = null;
		JsonObject model = null;
		if(list != null){						
			result = Response.status(Response.Status.OK).entity(list).build();
		} else {
			model = Json.createObjectBuilder().add(MESSAGE_RESPONSE, NOT_FOUND_MESSAGE).build();
			result = Response.status(Response.Status.NOT_FOUND).entity(model).build();	
		}
		return result;
	}
	
	@GET
	@Path("/count")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listAll(
			@QueryParam("filterText") String filterText) {				
		int size = personaNaturalService.count();		
		Response result = Response.status(Response.Status.OK).entity(size).build();
		return result;
	}
	
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") @DefaultValue("-1") BigInteger id, PersonaNatural personanatural) {
		Response result = null;
		JsonObject model = null;
		try {
			personaNaturalService.update(id, personanatural);
			model = Json.createObjectBuilder().add(MESSAGE_RESPONSE, SUCCESS_MESSAGE).build();
			result = Response.status(Response.Status.OK).entity(model).build();
		} catch (NonexistentEntityException e) {
			model = Json.createObjectBuilder().add(MESSAGE_RESPONSE, NOT_FOUND_MESSAGE).build();
			result = Response.status(Response.Status.NOT_FOUND).entity(model).build();
		} catch (PreexistingEntityException e) {
			model = Json.createObjectBuilder().add(MESSAGE_RESPONSE, CONFLICT_MESSAGE).build();
			result = Response.status(Response.Status.CONFLICT).entity(model).build();
		} catch (EJBException e) {
			log.log(Level.SEVERE, e.getMessage());
			model = Json.createObjectBuilder().add(MESSAGE_RESPONSE, e.getMessage()).build();
			result = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(model).build();
		}
		return result;
	}
	
	@POST
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response create(PersonaNatural personaNatural) {
		Response result = null;
		JsonObject model = null;
		try {
			PersonaNatural persona = personaNaturalService.create(personaNatural);
			BigInteger idPersona = persona.getIdPersonaNatural();
			model = Json.createObjectBuilder().add(MESSAGE_RESPONSE, SUCCESS_MESSAGE).add(ID_RESPONSE, idPersona).build();
			result = Response.status(Response.Status.OK).entity(model).build();
		} catch (PreexistingEntityException e) {
			model = Json.createObjectBuilder().add(MESSAGE_RESPONSE, CONFLICT_MESSAGE).build();
			result = Response.status(Response.Status.CONFLICT).entity(model).build();
		} catch (EJBException e) {
			log.log(Level.SEVERE, e.getMessage());
			model = Json.createObjectBuilder().add(MESSAGE_RESPONSE, e.getMessage()).build();
			result = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(model).build();
		}
		return result;
	}
	
	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") @DefaultValue("-1") BigInteger id) {
		Response result = null;
		JsonObject model = null;
		try {
			personaNaturalService.delete(id);
			model = Json.createObjectBuilder().add(MESSAGE_RESPONSE, SUCCESS_MESSAGE).build();
			result = Response.status(Response.Status.OK).entity(model).build();
		} catch (NonexistentEntityException e) {
			model = Json.createObjectBuilder().add(MESSAGE_RESPONSE, NOT_FOUND_MESSAGE).build();
			result = Response.status(Response.Status.NOT_FOUND).entity(model).build();
		} catch (EJBException e) {
			log.log(Level.SEVERE, e.getMessage());
			model = Json.createObjectBuilder().add(MESSAGE_RESPONSE, e.getMessage()).build();
			result = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(model).build();
		}
		return result;
	}
		
}
