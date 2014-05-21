package org.ventura.sistemafinanciero.rest.session;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.ventura.sistemafinanciero.entity.PendienteCaja;
import org.ventura.sistemafinanciero.exception.RollbackFailureException;
import org.ventura.sistemafinanciero.service.CajaSessionService;
import org.ventura.sistemafinanciero.service.PendienteService;

@RolesAllowed("CAJERO")
@Path("/caja/session/pendiente")
public class PendienteSessionRESTService {

	@EJB
	private CajaSessionService cajaSessionService;

	@EJB
	private PendienteService pendienteService;

	@RolesAllowed("CAJERO")
	@GET
	@Produces({ "application/xml", "application/json" })
	public Response getPendientes(@Context SecurityContext context) {
		Set<PendienteCaja> result = cajaSessionService.getPendientesCaja();
		return Response.status(Response.Status.OK).entity(result).build();
	}
	
	
	@RolesAllowed("CAJERO")
	@POST
	@Produces({ "application/xml", "application/json" })
	public Response crearPendiente(@Context SecurityContext context, @FormParam("idboveda") BigInteger idboveda,
			@FormParam("monto") BigDecimal monto,
			@FormParam("observacion") String observacion) {
		try {
			BigInteger idPendiente = cajaSessionService.crearPendiente(idboveda, monto,observacion);
			JsonObject model = Json.createObjectBuilder().add("message", "Pendiente cread").add("id", idPendiente).build();			
			return Response.status(Response.Status.OK).entity(model).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (EJBException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
}
