package org.ventura.sistemafinanciero.rest.session;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.ventura.sistemafinanciero.entity.Caja;
import org.ventura.sistemafinanciero.exception.RollbackFailureException;
import org.ventura.sistemafinanciero.rest.dto.TransaccionBancariaDTO;
import org.ventura.sistemafinanciero.rest.dto.TransaccionCuentaAporteDTO;
import org.ventura.sistemafinanciero.service.CajaSessionService;

@RolesAllowed("CAJERO")
@Path("/caja/session/transaccionCuentaAporte")
public class TransaccionCuentaAporteSessionRESTService {
	
	@EJB
	private CajaSessionService cajaSessionService;


	@RolesAllowed("CAJERO")
	@POST
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response crearAporte(TransaccionCuentaAporteDTO transaccion) {
		Response.ResponseBuilder builder = null;		
		try {			
			BigInteger idSocio = transaccion.getIdSocio();
			BigDecimal monto = transaccion.getMonto();
			int mes = transaccion.getMes();
			int anio = transaccion.getAnio();
			String referencia = transaccion.getReferencia();
			
			BigInteger idTransaccion = null;
			if(monto.compareTo(BigDecimal.ZERO) >= 0){
				idTransaccion = cajaSessionService.crearAporte(idSocio, monto, mes, anio, referencia);	
			} else {
				JsonObject model = Json.createObjectBuilder().add("message", "Monto no valido").build();
				builder = Response.status(Response.Status.BAD_REQUEST).entity(model);
			}
			
			JsonObject model = Json.createObjectBuilder().add("message", "Transaccion creada").add("id", idTransaccion).build();
			builder = Response.status(Response.Status.OK).entity(model);			
		} catch (RollbackFailureException e) {
			JsonObject model = Json.createObjectBuilder().add("message", e.getMessage()).build();
			builder = Response.status(Response.Status.BAD_REQUEST).entity(model);
		} catch (EJBException e) {
			JsonObject model = Json.createObjectBuilder().add("message", e.getMessage()).build();
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(model);
		}
		return builder.build();
	}	

}
