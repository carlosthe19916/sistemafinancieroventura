package org.ventura.sistemafinanciero.rest.session;

import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.ventura.sistemafinanciero.entity.Agencia;
import org.ventura.sistemafinanciero.entity.Caja;
import org.ventura.sistemafinanciero.entity.Trabajador;
import org.ventura.sistemafinanciero.entity.Usuario;
import org.ventura.sistemafinanciero.exception.NonexistentEntityException;
import org.ventura.sistemafinanciero.service.AgenciaService;
import org.ventura.sistemafinanciero.service.TrabajadorService;
import org.ventura.sistemafinanciero.service.UsuarioService;

@RolesAllowed("CAJERO")
@Path("/agencia/session")
public class AgenciaSessionRESTService {

	@EJB
	private UsuarioService usuarioService;
	@EJB
	private TrabajadorService trabajadorService;
	@EJB
	private AgenciaService agenciaService;
	
	@GET
	@Produces({ "application/xml", "application/json" })
	public Response getAgenciaOfSession(@Context SecurityContext context) {
		Agencia agencia = null;
		try {
			String username = context.getUserPrincipal().getName();
			Usuario currentUser = usuarioService.findByUsername(username);

			Trabajador trabajador;
			if (currentUser != null)
				trabajador = trabajadorService.findByUsuario(currentUser.getIdUsuario());
			else
				return Response.status(Response.Status.NOT_FOUND).entity("Usuario no encontrado").build();
			agencia = trabajadorService.getAgencia(trabajador.getIdTrabajador());
		} catch (NonexistentEntityException e) {
			throw new InternalServerErrorException();
		}
		return Response.status(Response.Status.OK).entity(agencia).build();
	}
	
	@GET
	@Path("/cajas")
	@Produces({ "application/xml", "application/json" })
	public Response getCajasOfSession(@Context SecurityContext context) {
		Agencia agencia = null;
		try {
			String username = context.getUserPrincipal().getName();
			Usuario currentUser = usuarioService.findByUsername(username);

			Trabajador trabajador;
			if (currentUser != null)
				trabajador = trabajadorService.findByUsuario(currentUser.getIdUsuario());
			else
				return Response.status(Response.Status.NOT_FOUND).entity("Usuario no encontrado").build();
			agencia = trabajadorService.getAgencia(trabajador.getIdTrabajador());
			Set<Caja> agencias = agenciaService.getCajas(agencia.getIdAgencia());
			return Response.status(Response.Status.OK).entity(agencias).build();
		} catch (NonexistentEntityException e) {			
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}		
	}

}
