
package org.ventura.sistemafinanciero.rest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.ventura.sistemafinanciero.entity.PersonaNatural;
import org.ventura.sistemafinanciero.entity.TipoDocumento;
import org.ventura.sistemafinanciero.entity.Trabajador;
import org.ventura.sistemafinanciero.entity.Usuario;
import org.ventura.sistemafinanciero.exception.NonexistentEntityException;
import org.ventura.sistemafinanciero.exception.PreexistingEntityException;
import org.ventura.sistemafinanciero.service.PersonanaturalService;
import org.ventura.sistemafinanciero.service.TrabajadorService;
import org.ventura.sistemafinanciero.service.UsuarioService;

@Stateless
@Path("/personanatural")
public class PersonanaturalRESTService {

	private Logger log;

	@Resource 
	private SessionContext principal;
	
	@Inject
	private Validator validator;

	@EJB
	private PersonanaturalService personanaturalService;
	@EJB
	private UsuarioService usuarioService;
	@EJB
	private TrabajadorService trabajadorService;
	
	@POST
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response create(PersonaNatural personanatural) {
		Response.ResponseBuilder builder = null;
		try {
			validatePersonaNatural(personanatural);						
			TipoDocumento tipoDocumento = personanatural.getTipoDocumento();
			String numerodocumento = personanatural.getNumeroDocumento();
			Object obj = personanaturalService.findByTipoNumeroDocumento(tipoDocumento.getIdTipoDocumento(), numerodocumento);
			if(obj == null) {
				personanaturalService.create(personanatural);
				builder = Response.ok();
			}				
			else {
				throw new ValidationException("Unique Tipo documento, Numero documento Violation");
			}		
		} catch (ConstraintViolationException ce) {
			builder = createViolationResponse(ce.getConstraintViolations());
		} catch (ValidationException e) {
			Map<String, String> responseObj = new HashMap<String, String>();
			responseObj.put("Tipo documento", "documento existente");
			responseObj.put("Numero documento", "documento existente");
			builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
		} catch (PreexistingEntityException e) {
			Map<String, String> responseObj = new HashMap<String, String>();
			responseObj.put("Persona existente ", e.getMessage());
			builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
		} catch (Exception e) {
			Map<String, String> responseObj = new HashMap<String, String>();
			responseObj.put("error", e.getMessage());
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseObj);
		}
		return builder.build();
	}
	
	@PUT
	@Path("/{id}")	
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") @DefaultValue("-1") int id, PersonaNatural personanatural) {
		Response.ResponseBuilder builder = null;
		try {
			if(id == -1)
				throw new BadRequestException();
			personanatural.setIdPersonaNatural(id);;
			validatePersonaNatural(personanatural);
			personanatural.setIdPersonaNatural(id);
			personanaturalService.update(id, personanatural);
			builder = Response.ok();						
		} catch (ConstraintViolationException ce) {
			builder = createViolationResponse(ce.getConstraintViolations());
		} catch (NonexistentEntityException e) {
			Map<String, String> responseObj = new HashMap<String, String>();
			responseObj.put("Persona no existente ", e.getMessage());
			builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
		} catch (PreexistingEntityException e) {
			Map<String, String> responseObj = new HashMap<String, String>();
			responseObj.put("Persona existente ", e.getMessage());
			builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
		} catch (Exception e) {
			Map<String, String> responseObj = new HashMap<String, String>();
			responseObj.put("error", e.getMessage());
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseObj);
		}
		return builder.build();
	}
	
	@DELETE
	@Path("/{id}")	
	public void delete(@PathParam("id") @DefaultValue("-1") int id) {		
		try {
			if(id == -1)
				throw new NonexistentEntityException("Persona no encontrada");
			personanaturalService.delete(id);								
		} catch (NonexistentEntityException e) {
			Map<String, String> responseObj = new HashMap<String, String>();
			responseObj.put("Persona no existente ", e.getMessage());
			throw new NotFoundException();
		} catch (Exception e) {
			Map<String, String> responseObj = new HashMap<String, String>();
			responseObj.put("error", e.getMessage());
			throw new InternalServerErrorException();
		}	
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public PersonaNatural findById(@PathParam("id") @DefaultValue("-1") int id) {
		if (id == -1)
			throw new NotFoundException();
		PersonaNatural personanatural = personanaturalService.findById(id);
		if (personanatural == null) {
			throw new NotFoundException();
		}
		return personanatural;
	}
	
	@GET
	@Path("/{idtipodocumento}/{numerodocumento}")
	@Produces({ "application/xml", "application/json" })
	public PersonaNatural findByTipoNumeroDocumento(@PathParam("idtipodocumento") @DefaultValue("-1") int idtipodocumento ,@PathParam("numerodocumento") @DefaultValue("") String numerodocumento) {
		TipoDocumento tipoDocumento = new TipoDocumento();
		tipoDocumento.setIdTipoDocumento(idtipodocumento);
		PersonaNatural personanatural = null;
		try {
			personanatural = personanaturalService.findByTipoNumeroDocumento(tipoDocumento.getIdTipoDocumento(), numerodocumento);
			if(personanatural == null)
				throw new NotFoundException();
		} catch (IllegalArgumentException e) {
			throw new javax.ws.rs.BadRequestException();
		}
		return personanatural;
	}
	
	@GET
	@Path("/filtertext/{filterText}")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<PersonaNatural> findByFilterText(@PathParam("filterText") @DefaultValue("") String filterText) {		
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
		PersonaNatural personaNatural = null;
		try {
			String username = principal.getCallerPrincipal().getName();
			Usuario currentUser = usuarioService.findByUsername(username);

			Trabajador trabajador;
			if (currentUser != null)
				trabajador = trabajadorService.findByUsuario(currentUser.getIdUsuario());
			else
				throw new NotFoundException();
			if(trabajador != null)
				personaNatural = personanaturalService.findByTrabajador(trabajador.getIdTrabajador());
			else
				personaNatural = null;
		} catch (IllegalArgumentException e) {
			throw new InternalServerErrorException();
		} 
		return personaNatural;
	}

	private void validatePersonaNatural(PersonaNatural personanatural) throws ConstraintViolationException {		
		Set<ConstraintViolation<PersonaNatural>> violations = validator.validate(personanatural);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}
	}

	private Response.ResponseBuilder createViolationResponse(Set<ConstraintViolation<?>> violations) {
		log.fine("Validation completed. violations found: " + violations.size());
		Map<String, String> responseObj = new HashMap<String, String>();
		for (ConstraintViolation<?> violation : violations) {
			responseObj.put(violation.getPropertyPath().toString(),violation.getMessage());
		}
		return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
	}

}
