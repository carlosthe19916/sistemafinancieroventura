/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ventura.sistemafinanciero.rest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.ventura.sistemafinanciero.entity.Caja;
import org.ventura.sistemafinanciero.entity.Moneda;
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
				caja = cajaService.findByTrabajador(trabajador.getIdTrabajador());
			else
				return Response.status(Response.Status.NOT_FOUND).entity("El usuario no tiene cajas asignadas").build();		
		} catch (NonexistentEntityException e) {
			throw new InternalServerErrorException();
		} 	
		return Response.status(Response.Status.OK).entity(caja).build();
	}
    
    @GET
    @Path("/detalle")
    @Produces({ "application/xml", "application/json" })
    public Response getCajaHistorialDetalle() {
    	Set<GenericMonedaDetalle> result = null;    	    	   
    	try {
    		String username = context.getCallerPrincipal().getName();
        	Usuario usuario = usuarioService.findByUsername(username);
        	Trabajador trabajador = trabajadorService.findByUsuario(usuario.getIdUsuario());
        	Caja caja = cajaService.findByTrabajador(trabajador.getIdTrabajador());
        	
        	result =  cajaService.getDetalleCaja(caja.getIdCaja());
        	return Response.status(Response.Status.OK).entity(result).build();        	
		} catch (NullPointerException e) {
			log.log(Level.SEVERE, e.getMessage());
		} catch (NonexistentEntityException e) {
			log.log(Level.SEVERE, e.getMessage());
			throw new BadRequestException();
		}
    	return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();   
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
				caja = cajaService.findByTrabajador(trabajador.getIdTrabajador());
			else
				caja = null;	
			if(caja != null) {
				cajaService.abrirCaja(caja.getIdCaja());
				builder = Response.status(Response.Status.OK).entity("Caja abierta correctamente"); 
			} else {
				throw new NotFoundException("Caja no encontrada");
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
				caja = cajaService.findByTrabajador(trabajador.getIdTrabajador());
			else
				caja = null;	
			if(caja != null) {
				cajaService.cerrarCaja(caja.getIdCaja(), new HashSet<GenericMonedaDetalle>(detalleCaja));
				builder = Response.ok();
			} else {
				throw new NotFoundException("Caja no encontrada");
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
}
