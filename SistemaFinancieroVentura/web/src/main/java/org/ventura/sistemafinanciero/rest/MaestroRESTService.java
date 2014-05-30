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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.ventura.sistemafinanciero.entity.Departamento;
import org.ventura.sistemafinanciero.entity.Distrito;
import org.ventura.sistemafinanciero.entity.Pais;
import org.ventura.sistemafinanciero.entity.Provincia;
import org.ventura.sistemafinanciero.entity.TipoDocumento;
import org.ventura.sistemafinanciero.entity.type.Estadocivil;
import org.ventura.sistemafinanciero.entity.type.Sexo;
import org.ventura.sistemafinanciero.entity.type.TipoEmpresa;
import org.ventura.sistemafinanciero.entity.type.TipoPersona;
import org.ventura.sistemafinanciero.service.MaestroService;

@Path("/")
public class MaestroRESTService {


	@EJB
	private MaestroService maestroService;

	@GET
	@Path("/tipopersona")
	@Produces(MediaType.APPLICATION_JSON)
	public List<TipoPersona> getTipoPersonas() {
		TipoPersona[] s = TipoPersona.values();
		List<TipoPersona> list = new ArrayList<TipoPersona>();
		for (int i = 0; i < s.length; i++) {
			list.add(s[i]);
		}
		return list;
	}
	
	@GET
	@Path("/estadocivil")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Estadocivil> getEstadoCiviles() {
		Estadocivil[] e = Estadocivil.values();
		List<Estadocivil> list = new ArrayList<Estadocivil>();
		for (int i = 0; i < e.length; i++) {
			list.add(e[i]);
		}
		return list;
	}
	
	@GET
	@Path("/sexo")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Sexo> getSexos() {
		Sexo[] s = Sexo.values();
		List<Sexo> list = new ArrayList<Sexo>();
		for (int i = 0; i < s.length; i++) {
			list.add(s[i]);
		}
		return list;
	}
	
	@GET
	@Path("/tipodocumento/personanatural")
	@Produces(MediaType.APPLICATION_JSON)
	public List<TipoDocumento> listAllFromPersonanatural() {
		List<TipoDocumento> list = maestroService.getAll(TipoPersona.NATURAL);
		return list;
	}

	@GET
	@Path("/tipodocumento/personajuridica")
	@Produces(MediaType.APPLICATION_JSON)
	public List<TipoDocumento> listAllFromPersonajuridica() {
		List<TipoDocumento> list = maestroService.getAll(TipoPersona.JURIDICA);
		return list;
	}
	
	@GET
	@Path("/tipoEmpresa")
	@Produces(MediaType.APPLICATION_JSON)
	public List<TipoEmpresa> listTipoEmpresa() {
		TipoEmpresa[] s = TipoEmpresa.values();
		List<TipoEmpresa> list = new ArrayList<TipoEmpresa>();
		for (int i = 0; i < s.length; i++) {
			list.add(s[i]);
		}
		return list;
	}
	
	@GET
	@Path("/pais")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Pais> getPaises() {
		List<Pais> list = maestroService.getPaises();
		return list;
	}
	
	@GET
	@Path("/departamento")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Departamento> getDepartamentos() {
		List<Departamento> list = maestroService.getDepartamentos();
		return list;
	}
	
	@GET
	@Path("/departamento/{id}/provincias")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Provincia> getProvincias(@PathParam("id") BigInteger idDepartamento) {
		List<Provincia> list = maestroService.getProvincias(idDepartamento);
		return list;
	}
	
	@GET
	@Path("/provincia/{id}/distritos")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Distrito> getDistritos(@PathParam("id") BigInteger idProvincia) {
		List<Distrito> list = maestroService.getDistritos(idProvincia);
		return list;
	}

}
