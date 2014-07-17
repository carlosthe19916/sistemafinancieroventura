package org.ventura.sistemafinanciero.rest.session;

import java.math.BigInteger;
import java.util.List;
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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.ventura.sistemafinanciero.entity.TransaccionBovedaCajaView;
import org.ventura.sistemafinanciero.entity.dto.GenericDetalle;
import org.ventura.sistemafinanciero.exception.RollbackFailureException;
import org.ventura.sistemafinanciero.service.CajaSessionService;

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
		List<TransaccionBovedaCajaView> result = cajaSessionService.getTransaccionesEnviadasBovedaCaja();
		return Response.status(Response.Status.OK).entity(result).build();				
	}
	
	@GET
	@Path("/recibidos")
	@Produces({ "application/xml", "application/json" })
	public Response getTransaccionesBovedaCajaOfCajaRecibidos(@Context SecurityContext context) {
		List<TransaccionBovedaCajaView> result = cajaSessionService.getTransaccionesRecibidasBovedaCaja();
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
	
	@RolesAllowed("CAJERO")
	@POST
	@Path("{id}/confirmar")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response confirmarTransaccion(@PathParam("id") BigInteger id) {
		try {			
			cajaSessionService.confirmarTransaccionBovedaCaja(id);
			JsonObject model = Json.createObjectBuilder().add("message", "Transaccion confirmada").build();	
			return Response.status(Response.Status.OK).entity(model).build();
		}  catch (RollbackFailureException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (EJBException e) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
		}
	}
	
	@RolesAllowed("CAJERO")
	@POST
	@Path("{id}/cancelar")	
	@Produces({ "application/xml", "application/json" })
	public Response cancelarTransaccion(@PathParam("id") BigInteger id) {
		try {			
			cajaSessionService.cancelarTransaccionBovedaCaja(id);
			JsonObject model = Json.createObjectBuilder().add("message", "Transaccion cancelada").build();	
			return Response.status(Response.Status.OK).entity(model).build();
		}  catch (RollbackFailureException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (EJBException e) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
		}
	}

}
