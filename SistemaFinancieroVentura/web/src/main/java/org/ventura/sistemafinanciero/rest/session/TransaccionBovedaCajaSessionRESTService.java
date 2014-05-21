package org.ventura.sistemafinanciero.rest.session;

import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.ventura.sistemafinanciero.entity.TransaccionBovedaCaja;
import org.ventura.sistemafinanciero.entity.dto.GenericDetalle;
import org.ventura.sistemafinanciero.service.BovedaService;
import org.ventura.sistemafinanciero.service.CajaService;
import org.ventura.sistemafinanciero.service.CajaSessionService;
import org.ventura.sistemafinanciero.service.PendienteService;
import org.ventura.sistemafinanciero.service.TrabajadorService;
import org.ventura.sistemafinanciero.service.UsuarioService;

@RolesAllowed("CAJERO")
@Path("/caja/session/transaccionbovedacaja")
public class TransaccionBovedaCajaSessionRESTService {

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
	public Response getTransaccionesBovedaCajaOfCajaEnviados(@Context SecurityContext context) {
		Set<TransaccionBovedaCaja> result = cajaSessionService.getTransaccionesEnviadasBovedaCaja();
		return Response.status(Response.Status.OK).entity(result).build();				
	}
	
	@GET
	@Path("/recibidos")
	@Produces({ "application/xml", "application/json" })
	public Response getTransaccionesBovedaCajaOfCajaRecibidos(@Context SecurityContext context) {
		Set<TransaccionBovedaCaja> result = cajaSessionService.getTransaccionesRecibidasBovedaCaja();
		return Response.status(Response.Status.OK).entity(result).build();	
	}
	
	@POST
	@Path("/transaccionbovedacaja")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response createTransaccionBovedaCaja(@Context SecurityContext context,
			Set<GenericDetalle> detalleTransaccion,
			@QueryParam("boveda") String bovedaDenominacion) {
		/*try {
			if (bovedaDenominacion.isEmpty() || bovedaDenominacion == null) {
				return Response.status(Response.Status.BAD_REQUEST).entity("Boveda no encontrada").build();
			}
			Caja caja = null;
			Boveda boveda = null;
			Agencia agencia = null;
			String username = context.getUserPrincipal().getName();
			Usuario currentUser = usuarioService.findByUsername(username);

			Trabajador trabajador;
			if (currentUser != null)
				trabajador = trabajadorService.findByUsuario(currentUser.getIdUsuario());
			else
				return Response.status(Response.Status.NOT_FOUND).entity("Usuario no encontrado").build();
			if (trabajador != null)
				caja = trabajadorService.findByTrabajador(trabajador.getIdTrabajador());

			agencia = trabajadorService.getAgencia(trabajador.getIdTrabajador());
			boveda = bovedaService.findByAgenciaAndBoveda(agencia.getIdAgencia(), bovedaDenominacion);

			cajaSessionService.crearTransaccionBovedaCaja(boveda.getIdBoveda(), detalleTransaccion);
			return Response.status(Response.Status.OK).entity("Transaccion creada").build();
		} catch (NonexistentEntityException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}*/
		return null;
	}

}
