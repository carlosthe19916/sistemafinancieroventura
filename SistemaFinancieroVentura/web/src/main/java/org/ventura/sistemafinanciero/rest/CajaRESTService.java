package org.ventura.sistemafinanciero.rest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.ventura.sistemafinanciero.entity.Boveda;
import org.ventura.sistemafinanciero.entity.Caja;
import org.ventura.sistemafinanciero.entity.Trabajador;
import org.ventura.sistemafinanciero.entity.Usuario;
import org.ventura.sistemafinanciero.entity.dto.GenericMonedaDetalle;
import org.ventura.sistemafinanciero.exception.NonexistentEntityException;
import org.ventura.sistemafinanciero.exception.RollbackFailureException;
import org.ventura.sistemafinanciero.service.CajaService;
import org.ventura.sistemafinanciero.service.TrabajadorService;
import org.ventura.sistemafinanciero.service.UsuarioService;

@Path("/caja")
@Stateless
public class CajaRESTService {
    
    private Logger log;
    
    @Resource
    private SessionContext context;
    
    @EJB CajaService cajaService;
    @EJB UsuarioService usuarioService;
    @EJB TrabajadorService trabajadorService;
    
    @GET
	@Path("/currentSession")
	@Produces({ "application/xml", "application/json" })
	public Response getCajaOfAuthenticateSession() {	
    	Caja caja = null;
		try {
			String username = context.getCallerPrincipal().getName();
			Usuario currentUser = usuarioService.findByUsername(username);

			Trabajador trabajador;
			if (currentUser != null)
				trabajador = trabajadorService.findByUsuario(currentUser.getIdUsuario());
			else
				return Response.status(Response.Status.NOT_FOUND).entity("Usuario no encontrado").build();
			if(trabajador != null)
				caja = trabajadorService.findByTrabajador(trabajador.getIdTrabajador());
			else
				return Response.status(Response.Status.NOT_FOUND).entity("El usuario no tiene cajas asignadas").build();		
		} catch (NonexistentEntityException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		} 	
		return Response.status(Response.Status.OK).entity(caja).build();
	}
    
    @GET
	@Path("/currentSession/bovedas")
	@Produces({ "application/xml", "application/json" })
	public Response getBovedasAuthenticateSession() {	
    	Caja caja = null;
		try {
			String username = context.getCallerPrincipal().getName();
			Usuario currentUser = usuarioService.findByUsername(username);

			Trabajador trabajador;
			if (currentUser != null)
				trabajador = trabajadorService.findByUsuario(currentUser.getIdUsuario());
			else
				return Response.status(Response.Status.NOT_FOUND).entity("Usuario no encontrado").build();
			if(trabajador != null)
				caja = trabajadorService.findByTrabajador(trabajador.getIdTrabajador());
			else
				return Response.status(Response.Status.NOT_FOUND).entity("El usuario no tiene cajas asignadas").build();	
			Set<Boveda> bovedas = cajaService.getBovedasByCaja(caja.getIdCaja());
			return Response.status(Response.Status.OK).entity(bovedas).build();
		} catch (NonexistentEntityException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		} 			
	}
    
    @GET
	@Path("/currentSession/detalle")
	@Produces({ "application/xml", "application/json" })
	public Response getDetalleByMonedaAuthenticateSession() {	
    	Caja caja = null;
		try {
			String username = context.getCallerPrincipal().getName();
			Usuario currentUser = usuarioService.findByUsername(username);

			Trabajador trabajador;
			if (currentUser != null)
				trabajador = trabajadorService.findByUsuario(currentUser.getIdUsuario());
			else
				return Response.status(Response.Status.NOT_FOUND).entity("Usuario no encontrado").build();
			if(trabajador != null)
				caja = trabajadorService.findByTrabajador(trabajador.getIdTrabajador());
			else
				return Response.status(Response.Status.NOT_FOUND).entity("El usuario no tiene cajas asignadas").build();	
			Set<Boveda> bovedas = cajaService.getBovedasByCaja(caja.getIdCaja());
			return Response.status(Response.Status.OK).entity(bovedas).build();
		} catch (NonexistentEntityException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		} 			
	}
    
    @GET
    @Path("/detalle")
    @Produces({ "application/xml", "application/json" })
    public Response getCajaHistorialDetalle() {   	    	   
    	try {
    		String username = context.getCallerPrincipal().getName();
        	Usuario usuario = usuarioService.findByUsername(username);
        	Trabajador trabajador = trabajadorService.findByUsuario(usuario.getIdUsuario());
        	Caja caja = trabajadorService.findByTrabajador(trabajador.getIdTrabajador());
        	
        	Set<GenericMonedaDetalle> result =  cajaService.getDetalleCaja(caja.getIdCaja());
        	return Response.status(Response.Status.OK).entity(result).build();        	
		} catch (NullPointerException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		} catch (NonexistentEntityException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}    	
    }
    
    @PUT
    @Path("/abrir")    
    public Response abrirCaja() {
    	Response.ResponseBuilder builder = null;
    	Caja caja = null;    	
		try {
			String username = context.getCallerPrincipal().getName();
			Usuario currentUser = usuarioService.findByUsername(username);
			Trabajador trabajador;
			if (currentUser != null)
				trabajador = trabajadorService.findByUsuario(currentUser.getIdUsuario());
			else
				throw new NotFoundException();
			if(trabajador != null)
				caja = trabajadorService.findByTrabajador(trabajador.getIdTrabajador());
			else
				caja = null;	
			if(caja != null) {
				cajaService.abrirCaja(caja.getIdCaja());
				builder = Response.status(Response.Status.OK).entity("Caja abierta correctamente"); 
			} else {
				return Response.status(Response.Status.NOT_FOUND).entity("Caja no encontrada").build();
			}			
		} catch (NonexistentEntityException e) {
			builder = Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage());					
		} catch (RollbackFailureException e) {
			builder = Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage());	
		} catch (EJBException e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e);	
		} 	
		return builder.build();
    }
            
    
    @POST
    @Path("/cerrar") 
    @Consumes({ "application/xml", "application/json" })
    public Response cerrarCaja(List<GenericMonedaDetalle> detalleCaja) {    	 
    	Caja caja = null;    	
		try {
			String username = context.getCallerPrincipal().getName();
			Usuario currentUser = usuarioService.findByUsername(username);
			Trabajador trabajador;
			if (currentUser != null)
				trabajador = trabajadorService.findByUsuario(currentUser.getIdUsuario());
			else
				throw new NotFoundException();
			if(trabajador != null)
				caja = trabajadorService.findByTrabajador(trabajador.getIdTrabajador());
			else
				caja = null;	
			if(caja != null) {
				cajaService.cerrarCaja(caja.getIdCaja(), new HashSet<GenericMonedaDetalle>(detalleCaja));
				return Response.status(Response.Status.OK).entity("Caja abierta correctamente").build();
			} else {
				return Response.status(Response.Status.NOT_FOUND).entity("Caja no encontrada").build();
			}			
		} catch (NonexistentEntityException e) {			
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();					
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();	
		} catch (EJBException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();	
		} 	
    }
}
