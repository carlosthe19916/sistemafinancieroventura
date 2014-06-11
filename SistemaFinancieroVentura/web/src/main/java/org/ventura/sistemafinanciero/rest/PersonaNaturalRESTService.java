package org.ventura.sistemafinanciero.rest;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.ventura.sistemafinanciero.entity.PersonaNatural;
import org.ventura.sistemafinanciero.exception.PreexistingEntityException;
import org.ventura.sistemafinanciero.service.PersonaNaturalService;
import org.ventura.sistemafinanciero.service.TrabajadorService;
import org.ventura.sistemafinanciero.service.UsuarioService;

@Path("/personanatural")
public class PersonaNaturalRESTService {

	private Logger log;

	@Inject
	private Validator validator;

	@EJB
	private PersonaNaturalService personanaturalService;
	@EJB
	private UsuarioService usuarioService;
	@EJB
	private TrabajadorService trabajadorService;

	private final String UPLOADED_FILE_PATH = "d:\\";
	
	@POST
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response create(PersonaNatural personaNatural) {
		try {
			BigInteger idPersona = personanaturalService.crear(personaNatural);
			JsonObject model = Json.createObjectBuilder()
					.add("message", "persona creada")
					.add("id", idPersona).build();
			return Response.status(Response.Status.OK).entity(model).build();
		} catch (PreexistingEntityException e) {
			JsonObject model = Json.createObjectBuilder()
					.add("message", "Persona ya existente").build();
			return Response.status(Response.Status.CONFLICT).entity(model).build();
		} catch (EJBException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") @DefaultValue("-1") int id,
			PersonaNatural personanatural) {
		return null;
	}

	@DELETE
	@Path("/{id}")
	public void delete(@PathParam("id") @DefaultValue("-1") int id) {

	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public PersonaNatural findById(
			@PathParam("id") @DefaultValue("-1") BigInteger id) {
		return null;
	}

	@GET
	@Path("/{idtipodocumento}/{numerodocumento}")
	@Produces({ "application/xml", "application/json" })
	public Response findByTipoNumeroDocumento(
			@PathParam("idtipodocumento") @DefaultValue("-1") BigInteger idtipodocumento,
			@PathParam("numerodocumento") @DefaultValue("") String numerodocumento) {
		try {		
			if(idtipodocumento == null || numerodocumento == null || numerodocumento.isEmpty() || numerodocumento.trim().isEmpty()){
				 JsonObject model = Json.createObjectBuilder().add("message", "Datos no validos").build();
				 return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
			}
			PersonaNatural personanatural = personanaturalService.findByTipoNumeroDocumento(idtipodocumento, numerodocumento);
			if(personanatural == null){
				 JsonObject model = Json.createObjectBuilder().add("message", "Persona no encontrada").build();
				 return Response.status(Response.Status.NOT_FOUND).entity(model).build();
			 }				
			 else {
				 return Response.status(Response.Status.OK).entity(personanatural).build(); 
			 }				 
		} catch (EJBException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}		 	
	}

	@GET
	@Path("/filtertext/{filterText}")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<PersonaNatural> findByFilterText(
			@PathParam("filterText") @DefaultValue("") String filterText) {
		Set<PersonaNatural> list = personanaturalService.findByFilterText(filterText);
		return list;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<PersonaNatural> listAll() {
		List<PersonaNatural> list = personanaturalService.findAll();
		return list;
	}

	@GET
	@Path("/currentSession")
	@Produces({ "application/xml", "application/json" })
	public PersonaNatural getPersonaOfAuthenticateSession() {
		/*PersonaNatural personaNatural = null;
		try {
			String username = principal.getCallerPrincipal().getName();
			Usuario currentUser = usuarioService.findByUsername(username);

			Trabajador trabajador;
			if (currentUser != null)
				trabajador = trabajadorService.findByUsuario(currentUser.getIdUsuario());
			else
				throw new NotFoundException();
			if (trabajador != null)
				personaNatural = personanaturalService
						.findByTrabajador(trabajador.getIdTrabajador());
			else
				personaNatural = null;
		} catch (IllegalArgumentException e) {
			throw new InternalServerErrorException();
		}
		return personaNatural;*/
		return null;
	}

	private void validatePersonaNatural(PersonaNatural personanatural)
			throws ConstraintViolationException {
		Set<ConstraintViolation<PersonaNatural>> violations = validator
				.validate(personanatural);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(
					new HashSet<ConstraintViolation<?>>(violations));
		}
	}

	private Response.ResponseBuilder createViolationResponse(
			Set<ConstraintViolation<?>> violations) {
		log.fine("Validation completed. violations found: " + violations.size());
		Map<String, String> responseObj = new HashMap<String, String>();
		for (ConstraintViolation<?> violation : violations) {
			responseObj.put(violation.getPropertyPath().toString(),
					violation.getMessage());
		}
		return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
	}
}
