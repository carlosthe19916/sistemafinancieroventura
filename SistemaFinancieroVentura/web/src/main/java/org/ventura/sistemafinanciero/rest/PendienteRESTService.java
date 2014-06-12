package org.ventura.sistemafinanciero.rest;

import java.math.BigInteger;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.ventura.sistemafinanciero.entity.PendienteCaja;
import org.ventura.sistemafinanciero.entity.dto.VoucherPendienteCaja;
import org.ventura.sistemafinanciero.service.CajaSessionService;
import org.ventura.sistemafinanciero.service.PendienteService;

@Path("/pendiente")
public class PendienteRESTService {

	@EJB
	private CajaSessionService cajaSessionService;

	@EJB
	private PendienteService pendienteService;

	@GET
	@Path("/{id}")
	@Produces({ "application/xml", "application/json" })
	public Response findPendiente(@PathParam("id")BigInteger id) {				
		PendienteCaja pendiente = pendienteService.getPendiente(id);
		if(pendiente != null)
			return Response.status(Response.Status.OK).entity(pendiente).build();
		else
			return Response.status(Response.Status.NOT_FOUND).build();
	}
	
	@GET
    @Path("{id}/voucherPendienteCaja")  
    @Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
    public Response getVoucherPendienteCaja(@PathParam("id") BigInteger idPendienteCaja){
    	VoucherPendienteCaja voucherPendienteCaja = pendienteService.getVoucherPendienteCaja(idPendienteCaja);    	
		return Response.status(Response.Status.OK).entity(voucherPendienteCaja).build(); 
    }
}
