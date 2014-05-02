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

import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

//import org.softgreen.sistcoop.entity.dto.MonedaCalculadora;
import org.ventura.sistemafinanciero.entity.Caja;
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
	public Caja getCajaOfAuthenticateSession() {	
    	/*Caja caja = null;
		try {
			String username = context.getCallerPrincipal().getName();
			Usuario currentUser = usuarioService.findByUsername(username);

			Trabajador trabajador;
			if (currentUser != null)
				trabajador = trabajadorService.findByUsuario(currentUser.getId());
			else
				throw new NotFoundException();

			caja = cajaService.findByTrabajador(trabajador.getId());

		} catch (NonexistentEntityException e) {
			throw new InternalServerErrorException();
		} 
		if(caja != null)
			return caja;
		else
			throw new NotFoundException();*/ return null;
	}
}
