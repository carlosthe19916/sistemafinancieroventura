package org.ventura.sistemafinanciero.rest;

import java.math.BigInteger;
import java.util.HashSet;
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
import org.ventura.sistemafinanciero.entity.PersonaNatural;
import org.ventura.sistemafinanciero.exception.PreexistingEntityException;
import org.ventura.sistemafinanciero.exception.RollbackFailureException;
import org.ventura.sistemafinanciero.rest.dto.PersonaJuridicaDTO;
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
	public Response create(PersonaJuridicaDTO persona) {
		try {

			
			PersonaJuridica personaJuridica = new PersonaJuridica();
			personaJuridica.setIdPersonaJuridica(null);
			personaJuridica.setActividadPrincipal(persona.getActividadPrincipal());
			personaJuridica.setCelular(persona.getCelular());
			personaJuridica.setTelefono(persona.getTelefono());
			personaJuridica.setDireccion(persona.getDireccion());
			personaJuridica.setReferencia(persona.getReferencia());
			personaJuridica.setEmail(persona.getEmail());
			personaJuridica.setFechaConstitucion(persona.getFechaConstitucion());
			personaJuridica.setFinLucro(persona.isFinLucro());			
			personaJuridica.setNombreComercial(persona.getNombreComercial());
			personaJuridica.setNumeroDocumento(persona.getNumeroDocumento());
			personaJuridica.setRazonSocial(persona.getRazonSocial());			
			personaJuridica.setTipoDocumento(persona.getTipoDocumento());
			personaJuridica.setTipoEmpresa(persona.getTipoEmpresa());
			personaJuridica.setUbigeo(persona.getUbigeo());
			
			PersonaNatural representante = new PersonaNatural();
			representante.setIdPersonaNatural(persona.getIdRepresentanteLegal());			
			personaJuridica.setRepresentanteLegal(representante);
			
			Set<Accionista> accionistasFinal = new HashSet<Accionista>();
			Set<org.ventura.sistemafinanciero.rest.dto.PersonaJuridicaDTO.Accionista> accionistas = persona.getAccionistas();
			for (org.ventura.sistemafinanciero.rest.dto.PersonaJuridicaDTO.Accionista accionista : accionistas) {
				Accionista accionistaFinal = new Accionista();
				accionistaFinal.setIdAccionista(null);
				PersonaNatural person = new PersonaNatural();
				person.setIdPersonaNatural(accionista.getIdPersona());
				accionistaFinal.setPersonaNatural(person);
				accionistaFinal.setPorcentajeParticipacion(accionista.getPorcentaje());
				
				accionistasFinal.add(accionistaFinal);
			}
			personaJuridica.setAccionistas(accionistasFinal);
			
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
			JsonObject model = Json.createObjectBuilder().add("message", "Error interno").build();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(model).build();
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
