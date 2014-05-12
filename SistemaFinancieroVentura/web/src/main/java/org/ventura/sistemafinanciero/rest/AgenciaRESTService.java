package org.ventura.sistemafinanciero.rest;

import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.ventura.sistemafinanciero.entity.Agencia;
import org.ventura.sistemafinanciero.entity.Trabajador;
import org.ventura.sistemafinanciero.entity.Usuario;
import org.ventura.sistemafinanciero.exception.NonexistentEntityException;
import org.ventura.sistemafinanciero.service.TrabajadorService;
import org.ventura.sistemafinanciero.service.UsuarioService;

@Path("/agencia")
@Stateless
public class AgenciaRESTService {

	private Logger log;

	@Resource
	private SessionContext context;

	@EJB
	private UsuarioService usuarioService;
	@EJB
	private TrabajadorService trabajadorService;

	@GET
	@Path("/currentSession")
	@Produces({ "application/xml", "application/json" })
	public Response getAgenciaOfAuthenticateSession() {
		Agencia agencia = null;
		try {
			String username = context.getCallerPrincipal().getName();
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

	
}
