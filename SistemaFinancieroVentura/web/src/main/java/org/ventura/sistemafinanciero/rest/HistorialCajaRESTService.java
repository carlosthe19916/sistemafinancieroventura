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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.ventura.sistemafinanciero.entity.Caja;
import org.ventura.sistemafinanciero.entity.HistorialCaja;
import org.ventura.sistemafinanciero.entity.Trabajador;
import org.ventura.sistemafinanciero.entity.Usuario;
import org.ventura.sistemafinanciero.entity.dto.CajaCierreMoneda;
import org.ventura.sistemafinanciero.exception.IllegalResultException;
import org.ventura.sistemafinanciero.exception.NonexistentEntityException;
import org.ventura.sistemafinanciero.service.CajaService;
import org.ventura.sistemafinanciero.service.TrabajadorService;
import org.ventura.sistemafinanciero.service.UsuarioService;

@Path("/historialcaja")
@Stateless
public class HistorialCajaRESTService {
    
    
    @Resource
    private SessionContext context;
    
    @EJB CajaService cajaService;
    @EJB UsuarioService usuarioService;
    @EJB TrabajadorService trabajadorService;
     
    @GET
    @Path("/currentSession")  
    @Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
    public List<HistorialCaja> getHistorialCajaCurrentSession(@QueryParam("desde") String desde, @QueryParam("hasta") String hasta){
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
				throw new NotFoundException("Usuario:"+username+" no tiene una caja asignada");	
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date dateDesde = (desde != null ? formatter.parse(desde) : null);
			Date dateHasta = (hasta != null ? formatter.parse(hasta) : null);
			
			return cajaService.getHistorialCaja(caja.getIdCaja(), dateDesde, dateHasta);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NonexistentEntityException e) {
			throw new InternalServerErrorException();
		}
		return null; 	
    }
    
    @GET
    @Path("/voucherCierreCaja")  
    @Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
    public Set<CajaCierreMoneda> getVoucherCierreCaja(@QueryParam("fechaApertura") String fecha){
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
				throw new NotFoundException("Usuario:"+username+" no tiene una caja asignada");	
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date fechaApertura = (fecha != null ? formatter.parse(fecha) : null);
			return cajaService.getVoucherCierreCaja(caja.getIdCaja(), fechaApertura);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NonexistentEntityException e) {
			throw new InternalServerErrorException();
		} catch (IllegalResultException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   		
    	return null;
    }
}
