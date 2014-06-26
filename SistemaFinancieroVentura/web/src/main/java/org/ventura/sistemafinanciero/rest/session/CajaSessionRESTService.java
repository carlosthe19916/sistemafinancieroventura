package org.ventura.sistemafinanciero.rest.session;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.hibernate.Hibernate;
import org.ventura.sistemafinanciero.entity.Boveda;
import org.ventura.sistemafinanciero.entity.Caja;
import org.ventura.sistemafinanciero.entity.HistorialCaja;
import org.ventura.sistemafinanciero.entity.HistorialTransaccionCaja;
import org.ventura.sistemafinanciero.entity.Moneda;
import org.ventura.sistemafinanciero.entity.Trabajador;
import org.ventura.sistemafinanciero.entity.Usuario;
import org.ventura.sistemafinanciero.entity.dto.GenericMonedaDetalle;
import org.ventura.sistemafinanciero.exception.NonexistentEntityException;
import org.ventura.sistemafinanciero.exception.RollbackFailureException;
import org.ventura.sistemafinanciero.service.BovedaService;
import org.ventura.sistemafinanciero.service.CajaService;
import org.ventura.sistemafinanciero.service.CajaSessionService;
import org.ventura.sistemafinanciero.service.TrabajadorService;
import org.ventura.sistemafinanciero.service.UsuarioService;

@RolesAllowed("CAJERO")
@Path("/caja/session")
public class CajaSessionRESTService {

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

	@RolesAllowed("CAJERO")
	@GET
	@Produces({ "application/xml", "application/json" })
	public Response getCaja(@Context SecurityContext context) {
		Caja caja = null;
		try {
			String username = context.getUserPrincipal().getName();
			Usuario currentUser = usuarioService.findByUsername(username);

			Trabajador trabajador;
			if (currentUser != null)
				trabajador = trabajadorService.findByUsuario(currentUser.getIdUsuario());
			else
				return Response.status(Response.Status.NOT_FOUND).entity("Usuario no encontrado").build();
			if (trabajador != null)
				caja = trabajadorService.findByTrabajador(trabajador.getIdTrabajador());
			else
				return Response.status(Response.Status.NOT_FOUND).entity("El usuario no tiene cajas asignadas").build();
		} catch (NonexistentEntityException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.status(Response.Status.OK).entity(caja).build();
	}

	@RolesAllowed("CAJERO")
	@GET
	@Path("/bovedas")
	@Produces({ "application/xml", "application/json" })
	public Response getBovedasOfCaja(@Context SecurityContext context) {
		Caja caja = null;
		try {
			String username = context.getUserPrincipal().getName();
			Usuario currentUser = usuarioService.findByUsername(username);

			Trabajador trabajador;
			if (currentUser != null)
				trabajador = trabajadorService.findByUsuario(currentUser.getIdUsuario());
			else
				return Response.status(Response.Status.NOT_FOUND).entity("Usuario no encontrado").build();
			if (trabajador != null)
				caja = trabajadorService.findByTrabajador(trabajador.getIdTrabajador());
			else
				return Response.status(Response.Status.NOT_FOUND).entity("El usuario no tiene cajas asignadas").build();
			Set<Boveda> bovedas = cajaService.getBovedasByCaja(caja.getIdCaja());
			Hibernate.initialize(bovedas);
			return Response.status(Response.Status.OK).entity(bovedas).build();
		} catch (NonexistentEntityException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
	}

	@RolesAllowed("CAJERO")
	@GET
	@Path("/monedas")
	@Produces({ "application/xml", "application/json" })
	public Response getMonedasOfCaja(@Context SecurityContext context) {
		Set<Moneda> monedas = cajaSessionService.getMonedas();
		return Response.status(Response.Status.OK).entity(monedas).build();
	}

	@RolesAllowed("CAJERO")
	@GET
	@Path("/detalle")
	@Produces({ "application/xml", "application/json" })
	public Response getDetalleOfCaja() {
		Set<GenericMonedaDetalle> result = cajaSessionService.getDetalleCaja();
		return Response.status(Response.Status.OK).entity(result).build();			
	}	

	@RolesAllowed("CAJERO")
	@GET
	@Path("/historial")
	@Produces({ "application/xml", "application/json" })
	public Response getHistorialOfCaja(@QueryParam("desde") Long desde, @QueryParam("hasta") Long hasta) {
		Date dateDesde = (desde != null ? new Date(desde) : null);
		Date dateHasta = (desde != null ? new Date(hasta) : null);
		if(dateDesde == null || dateHasta == null)
			return Response.status(Response.Status.BAD_REQUEST).build();
		Set<HistorialCaja> list = cajaSessionService.getHistorialCaja(dateDesde, dateHasta);
		return Response.status(Response.Status.OK).entity(list).build();
	}

	@RolesAllowed("CAJERO")
	@GET
	@Path("/historialTransaccion")
	@Produces({ "application/xml", "application/json" })
	public Response getHistorialTransaccionCaja(){		
		List<HistorialTransaccionCaja> list = cajaSessionService.getHistorialTransaccion();
		return Response.status(Response.Status.OK).entity(list).build();
	}
	
	@RolesAllowed("CAJERO")
	@POST
	@Path("/abrir")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response abrirCaja(@Context SecurityContext context) {
		Response.ResponseBuilder builder = null;		
		try {			
			BigInteger idHistorialCaja = cajaSessionService.abrirCaja();
			JsonObject model = Json.createObjectBuilder().add("message", "Caja abierta").add("id", idHistorialCaja).build();
			builder = Response.status(Response.Status.OK).entity(model);			
		} catch (RollbackFailureException e) {
			builder = Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage());
		} catch (EJBException e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage());
		}
		return builder.build();
	}

	@RolesAllowed("CAJERO")
	@POST
	@Path("/cerrar")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response cerrarCaja(@Context SecurityContext context, Set<GenericMonedaDetalle> detalleCaja) {
		try {			
			Map<Boveda, BigDecimal> diferencia = cajaSessionService.getDiferenciaSaldoCaja(detalleCaja);
			if(diferencia.size()>0){
				JsonArrayBuilder result = Json.createArrayBuilder();
				for (Boveda boveda : diferencia.keySet()) {
					BigDecimal dif = diferencia.get(boveda);
					JsonObject obj = Json.createObjectBuilder()
					.add("idboveda", boveda.getIdBoveda())
					.add("boveda", boveda.getDenominacion())
					.add("monto", dif).build();		
					result.add(obj);
				}
				return Response.status(Response.Status.BAD_REQUEST).entity(result.build()).build();
			}
			BigInteger idHistorialCaja = cajaSessionService.cerrarCaja(new HashSet<GenericMonedaDetalle>(detalleCaja));
			JsonObject model = Json.createObjectBuilder().add("message", "Caja cerrada").add("id", idHistorialCaja).build();
			return Response.status(Response.Status.OK).entity(model).build();			
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		} catch (EJBException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
	
}
