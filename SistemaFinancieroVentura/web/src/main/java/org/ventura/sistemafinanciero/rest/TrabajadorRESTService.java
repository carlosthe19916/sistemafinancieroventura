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

import java.security.Principal;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.ventura.sistemafinanciero.entity.Trabajador;
import org.ventura.sistemafinanciero.entity.Usuario;
import org.ventura.sistemafinanciero.service.TrabajadorService;
import org.ventura.sistemafinanciero.service.UsuarioService;

@Stateless
@Path("/trabajador")
public class TrabajadorRESTService {

	@Resource
	private Principal principal;

	@EJB
	private UsuarioService usuarioService;
	
	@EJB
	private TrabajadorService trabajadorService;
	
	@GET
	@Path("/current")
	@Produces({ "application/xml", "application/json" })
	public Trabajador getTrabajadorOfAuthenticateSession() {
		Trabajador trabajador = null;
		String username = principal.getName();
		Usuario currentUser = usuarioService.findByUsername(username);
		//trabajador =  trabajadorService.findByIdUsuario(currentUser);	
		return trabajador;
	}

}
