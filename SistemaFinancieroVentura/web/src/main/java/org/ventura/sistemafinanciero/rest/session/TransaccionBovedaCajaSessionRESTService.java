package org.ventura.sistemafinanciero.rest.session;

import java.math.BigInteger;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.json.Json;
import javax.json.JsonObject;
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
import org.ventura.sistemafinanciero.exception.RollbackFailureException;
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
	private CajaSessionService cajaSessionService;


	@RolesAllowed("CAJERO")
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
	
	@RolesAllowed("CAJERO")
	@POST
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response createTransaccionBovedaCaja(@Context SecurityContext context,
			Set<GenericDetalle> detalleTransaccion,
			@QueryParam("boveda") BigInteger idboveda) {
		try {			
			BigInteger idTransaccion = cajaSessionService.crearTransaccionBovedaCaja(idboveda, detalleTransaccion);
			JsonObject model = Json.createObjectBuilder().add("message", "Transaccion creada").add("id", idTransaccion).build();	
			return Response.status(Response.Status.OK).entity(model).build();
		}  catch (RollbackFailureException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (EJBException e) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
		}
	}

}
