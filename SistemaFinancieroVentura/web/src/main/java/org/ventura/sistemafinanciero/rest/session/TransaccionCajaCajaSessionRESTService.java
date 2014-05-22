package org.ventura.sistemafinanciero.rest.session;

import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.ventura.sistemafinanciero.entity.TransaccionCajaCaja;
import org.ventura.sistemafinanciero.service.BovedaService;
import org.ventura.sistemafinanciero.service.CajaService;
import org.ventura.sistemafinanciero.service.CajaSessionService;
import org.ventura.sistemafinanciero.service.PendienteService;
import org.ventura.sistemafinanciero.service.TrabajadorService;
import org.ventura.sistemafinanciero.service.UsuarioService;

@RolesAllowed("CAJERO")
@Path("/caja/session/transaccioncajacaja")
public class TransaccionCajaCajaSessionRESTService {

	@EJB
	private BovedaService bovedaService;
	@EJB
	private CajaService cajaService;
	@EJB
	private CajaSessionService cajaSessionService;
	@EJB
	private UsuarioService usuarioService;
	@EJB
	private TrabajadorService trabajadorService;
	@EJB
	private PendienteService pendienteService;


	@GET
	@Path("/enviados")
	@Produces({ "application/xml", "application/json" })
	public Response getTransaccionesCajaCajaOfCajaEnviados(@Context SecurityContext context) {
		Set<TransaccionCajaCaja> result = cajaSessionService.getTransaccionesEnviadasCajaCaja();
		return Response.status(Response.Status.OK).entity(result).build();
	}

	@GET
	@Path("/recibidos")
	@Produces({ "application/xml", "application/json" })
	public Response getTransaccionesCajaCajaOfCajaRecibidos(@Context SecurityContext context) {
		Set<TransaccionCajaCaja> result = cajaSessionService.getTransaccionesRecibidasCajaCaja();
		return Response.status(Response.Status.OK).entity(result).build();
	}
	
	@POST
	@Produces({ "application/xml", "application/json" })
	public Response createTransaccionCajaCaja(@Context SecurityContext context) {
		return null;
	}
}
