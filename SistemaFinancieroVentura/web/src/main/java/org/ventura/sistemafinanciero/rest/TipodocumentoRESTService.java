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

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.ventura.sistemafinanciero.entity.TipoDocumento;
import org.ventura.sistemafinanciero.entity.type.TipoPersona;
import org.ventura.sistemafinanciero.service.MaestroService;

@Stateless
@Path("/tipodocumento")
public class TipodocumentoRESTService {

	private Logger log;

	@EJB
	private MaestroService maestroService;

	@GET
	@Path("/personanatural")
	@Produces(MediaType.APPLICATION_JSON)
	public List<TipoDocumento> listAllFromPersonanatural() {
		List<TipoDocumento> list = maestroService.getAll(TipoPersona.NATURAL);
		return list;
	}

	@GET
	@Path("/personajuridica")
	@Produces(MediaType.APPLICATION_JSON)
	public List<TipoDocumento> listAllFromPersonajuridica() {
		List<TipoDocumento> list = maestroService.getAll(TipoPersona.JURIDICA);
		return list;
	}

}
