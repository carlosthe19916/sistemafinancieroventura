
package org.ventura.sistemafinanciero.rest;

import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.ventura.sistemafinanciero.entity.SocioView;
import org.ventura.sistemafinanciero.service.SocioService;

@Stateless
@Path("/socio")
public class SocioRESTService {

	@EJB
	private SocioService socioService;
	
	@GET
	@Path("/filtertext/{filterText}")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<SocioView> findByFilterText(@PathParam("filterText") @DefaultValue("") String filterText) {		
		Set<SocioView> list = socioService.findByFilterText(filterText);	
		return list;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<SocioView> listAll() {
		List<SocioView> list = socioService.findAll();		
		return list;
	}
}
