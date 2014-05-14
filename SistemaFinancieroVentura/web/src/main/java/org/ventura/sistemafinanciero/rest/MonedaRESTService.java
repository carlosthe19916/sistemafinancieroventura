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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.ventura.sistemafinanciero.entity.Boveda;
import org.ventura.sistemafinanciero.entity.Caja;
import org.ventura.sistemafinanciero.entity.Moneda;
import org.ventura.sistemafinanciero.entity.Trabajador;
import org.ventura.sistemafinanciero.entity.Usuario;
import org.ventura.sistemafinanciero.entity.dto.GenericDetalle;
import org.ventura.sistemafinanciero.entity.dto.GenericMonedaDetalle;
import org.ventura.sistemafinanciero.exception.NonexistentEntityException;
import org.ventura.sistemafinanciero.exception.RollbackFailureException;
import org.ventura.sistemafinanciero.service.CajaService;
import org.ventura.sistemafinanciero.service.MonedaService;
import org.ventura.sistemafinanciero.service.TrabajadorService;
import org.ventura.sistemafinanciero.service.UsuarioService;

@Path("/moneda")
@Stateless
public class MonedaRESTService {
    
    private Logger log;
    
    
    @EJB MonedaService monedaService;
    
    @GET
	@Path("/denominaciones")
	@Produces({ "application/xml", "application/json" })
	public Response getDetalleByMoneda(@QueryParam("moneda") String denominacionMoneda) {
    	if(denominacionMoneda == null)
    		return Response.status(Response.Status.BAD_REQUEST).build();    	
    	Moneda moneda = monedaService.findByDenominacion(denominacionMoneda);
    	if(moneda == null)
    		return Response.status(Response.Status.NOT_FOUND).build();    
		Set<GenericDetalle> result = monedaService.getGenericDenominaciones(moneda.getIdMoneda());
		return Response.status(Response.Status.OK).entity(result).build();
	}
    
  
}
