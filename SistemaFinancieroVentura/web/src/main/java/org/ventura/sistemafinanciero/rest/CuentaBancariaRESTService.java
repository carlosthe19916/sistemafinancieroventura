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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.ventura.sistemafinanciero.entity.Agencia;
import org.ventura.sistemafinanciero.entity.Beneficiario;
import org.ventura.sistemafinanciero.entity.CuentaBancaria;
import org.ventura.sistemafinanciero.entity.CuentaBancariaView;
import org.ventura.sistemafinanciero.entity.EstadocuentaBancariaView;
import org.ventura.sistemafinanciero.entity.PersonaJuridica;
import org.ventura.sistemafinanciero.entity.PersonaNatural;
import org.ventura.sistemafinanciero.entity.Socio;
import org.ventura.sistemafinanciero.entity.TipoDocumento;
import org.ventura.sistemafinanciero.entity.Titular;
import org.ventura.sistemafinanciero.entity.Trabajador;
import org.ventura.sistemafinanciero.entity.Usuario;
import org.ventura.sistemafinanciero.entity.dto.VoucherTransaccionBancaria;
import org.ventura.sistemafinanciero.entity.dto.VoucherTransferenciaBancaria;
import org.ventura.sistemafinanciero.entity.type.EstadoCuentaBancaria;
import org.ventura.sistemafinanciero.entity.type.TipoCuentaBancaria;
import org.ventura.sistemafinanciero.entity.type.TipoPersona;
import org.ventura.sistemafinanciero.exception.NonexistentEntityException;
import org.ventura.sistemafinanciero.exception.RollbackFailureException;
import org.ventura.sistemafinanciero.rest.dto.CuentaAhorroDTO;
import org.ventura.sistemafinanciero.rest.dto.CuentaCorrienteDTO;
import org.ventura.sistemafinanciero.rest.dto.CuentaPlazoFijoDTO;
import org.ventura.sistemafinanciero.rest.dto.TitularDTO;
import org.ventura.sistemafinanciero.service.CajaSessionService;
import org.ventura.sistemafinanciero.service.CuentaBancariaService;
import org.ventura.sistemafinanciero.service.PersonaJuridicaService;
import org.ventura.sistemafinanciero.service.PersonaNaturalService;
import org.ventura.sistemafinanciero.service.SocioService;
import org.ventura.sistemafinanciero.service.TrabajadorService;
import org.ventura.sistemafinanciero.service.UsuarioService;
import org.ventura.sistemafinanciero.util.ProduceObject;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

@Path("/cuentaBancaria")
public class CuentaBancariaRESTService {
    
	@EJB
	private CuentaBancariaService cuentaBancariaService;
	@EJB
	private UsuarioService usuarioService;
	@EJB
	private TrabajadorService trabajadorService;
	@EJB
	private PersonaNaturalService personaNaturalService;
	@EJB
	private PersonaJuridicaService personaJuridicaService;
	@EJB
	private SocioService socioService;
	
	@EJB
	private CajaSessionService cajaSessionService;
	
	@GET
	@Path("/")
	@Produces({ "application/xml", "application/json" })
	public Response findAll() {				
		List<CuentaBancaria> list = cuentaBancariaService.findAll();
		return Response.status(Response.Status.OK).entity(list).build();
	}
	
	@GET
	@Path("/view")
	@Produces({ "application/xml", "application/json" })
	public Response findAllViewByTipoEstadoCuenta(
			@QueryParam("filterText") String filterText,
			@QueryParam("offset") BigInteger offset,
			@QueryParam("limit") BigInteger limit,
			@QueryParam("tipoCuenta") TipoCuentaBancaria[] tipoCuenta,
			@QueryParam("tipoPersona") TipoPersona[] tipoPersona,
			@QueryParam("tipoEstadoCuenta") EstadoCuentaBancaria[]  tipoEstadoCuenta) {
		
		if(offset != null && offset.compareTo(BigInteger.ZERO) < 1)
			offset = BigInteger.ZERO;
		if(limit != null && limit.compareTo(BigInteger.ZERO) < 1)
			limit = BigInteger.ZERO;
						
		List<CuentaBancariaView> list = cuentaBancariaService.findAllView(filterText, tipoCuenta, tipoPersona, tipoEstadoCuenta, offset, limit);
		return Response.status(Response.Status.OK).entity(list).build();				
	}
	
	@GET
	@Path("/view/count")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findByFilterText() {								
		int size = cuentaBancariaService.count();
		return Response.status(Response.Status.OK).entity(size).build();						
	}
	
	@GET
	@Path("/view/buscar")
	@Produces({ "application/xml", "application/json" })
	public Response buscarByNumeroCuenta(
			@QueryParam("numeroCuenta") String numeroCuenta) {
		
		if(numeroCuenta == null){
			JsonObject model = Json.createObjectBuilder().add("message", "numero de cuenta no valida").build();
			return Response.status(Response.Status.BAD_REQUEST).entity(model).build();	
		}
		numeroCuenta = numeroCuenta.trim();
		CuentaBancariaView cuentaBancaria = cuentaBancariaService.find(numeroCuenta);
		if(cuentaBancaria != null){
			return Response.status(Response.Status.OK).entity(cuentaBancaria).build();	
		} else {
			JsonObject model = Json.createObjectBuilder().add("message", "cuenta no encontrada").build();
			return Response.status(Response.Status.NOT_FOUND).entity(model).build();	
		}												
	}
	
	@GET
	@Path("/{id}")
	@Produces({ "application/xml", "application/json" })
	public Response findCuentaBancaria(@PathParam("id")BigInteger id) {				
		CuentaBancaria cuentaBancaria = cuentaBancariaService.findById(id);
		if(cuentaBancaria == null)
			return Response.status(Response.Status.NOT_FOUND).entity("No encontrado").build();
		else 
			return Response.status(Response.Status.OK).entity(cuentaBancaria).build();
	}
	
	@GET
	@Path("/{id}/socio")
	@Produces({ "application/xml", "application/json" })
	public Response findSocioFromCuentaBancaria(@PathParam("id")BigInteger id) {				
		CuentaBancaria cuentaBancaria = cuentaBancariaService.findById(id);
		if(cuentaBancaria != null) {
			Socio socio = socioService.find(cuentaBancaria.getIdCuentaBancaria());
			return Response.status(Response.Status.OK).entity(socio).build();	
		}					
		else {
			return Response.status(Response.Status.NOT_FOUND).entity("No encontrado").build();	
		}					
	}
	
	//falta
	@GET
	@Path("/{id}/titulares")
	@Produces({ "application/xml", "application/json" })
	public Response findTitularesActiveFromCuentaBancaria(@PathParam("id")BigInteger id) {				
		CuentaBancaria cuentaBancaria = cuentaBancariaService.findById(id);
		if(cuentaBancaria != null) {
			Set<Titular> titulares = cuentaBancariaService.getTitulares(cuentaBancaria.getIdCuentaBancaria(), true);
			return Response.status(Response.Status.OK).entity(titulares).build();	
		}					
		else {
			JsonObject model = Json.createObjectBuilder().add("message", "Cuenta no encontrada").build();
			return Response.status(Response.Status.NOT_FOUND).entity(model).build();				
		}					
	}
	
	@GET
	@Path("/{id}/titulares/all")
	@Produces({ "application/xml", "application/json" })
	public Response findTitularesAllFromCuentaBancaria(@PathParam("id")BigInteger id) {				
		CuentaBancaria cuentaBancaria = cuentaBancariaService.findById(id);
		if(cuentaBancaria != null) {
			Set<Titular> beneficiarios = cuentaBancariaService.getTitulares(cuentaBancaria.getIdCuentaBancaria(), false);
			return Response.status(Response.Status.OK).entity(beneficiarios).build();	
		}					
		else {
			JsonObject model = Json.createObjectBuilder().add("message", "Cuenta no encontrada").build();
			return Response.status(Response.Status.NOT_FOUND).entity(model).build();				
		}					
	}
	
	@GET
	@Path("/{id}/beneficiarios")
	@Produces({ "application/xml", "application/json" })
	public Response findBeneficiariosFromCuentaBancaria(@PathParam("id")BigInteger id) {				
		CuentaBancaria cuentaBancaria = cuentaBancariaService.findById(id);
		if(cuentaBancaria != null) {
			Set<Beneficiario> beneficiarios = cuentaBancariaService.getBeneficiarios(cuentaBancaria.getIdCuentaBancaria());
			return Response.status(Response.Status.OK).entity(beneficiarios).build();	
		}					
		else {
			JsonObject model = Json.createObjectBuilder().add("message", "Cuenta no encontrada").build();
			return Response.status(Response.Status.NOT_FOUND).entity(model).build();				
		}					
	}
		
	@GET
	@Path("/{id}/estadoCuenta")
	@Produces({ "application/xml", "application/json" })
	public Response getEstadoCuenta(@PathParam("id")BigInteger id, 
			@QueryParam("desde") Long desde, 
			@QueryParam("hasta") Long hasta) {	
		Date dateDesde = (desde != null ? new Date(desde) : null);
		Date dateHasta = (desde != null ? new Date(hasta) : null);
		
		CuentaBancaria cuentaBancaria = cuentaBancariaService.findById(id);
		if(cuentaBancaria != null) {
			List<EstadocuentaBancariaView> list = cuentaBancariaService.getEstadoCuenta(cuentaBancaria.getIdCuentaBancaria(),dateDesde, dateHasta);
			return Response.status(Response.Status.OK).entity(list).build();
		}					
		else {
			return Response.status(Response.Status.NOT_FOUND).entity("Cuenta no encontrado").build();	
		}						
	}
	
	/*
	@GET
	@Path("/filtertext/{filterText}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findByFilterText(@PathParam("filterText") @DefaultValue("") String filterText) {
		List<CuentaBancariaView> list = cuentaBancariaService.findAllView(filterText);
		return Response.status(Response.Status.OK).entity(list).build();
	}*/
	
	
		
	@POST
	@Path("/ahorro")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response createCuentaAhorro(
			CuentaAhorroDTO cuenta, @Context SecurityContext context) {	
		JsonObject model = null;
		try {		
			String username = context.getUserPrincipal().getName();
			Usuario currentUser = usuarioService.findByUsername(username);
			Trabajador trabajador;
			if (currentUser != null)
				trabajador = trabajadorService.findByUsuario(currentUser.getIdUsuario());
			else
				return Response.status(Response.Status.NOT_FOUND).entity("Usuario no encontrado").build();
			Agencia agencia = trabajadorService.getAgencia(trabajador.getIdTrabajador());
			if(agencia == null)
				return Response.status(Response.Status.NOT_FOUND).entity("Usuario no encontrado").build();
			
			TipoPersona tipoPersona = cuenta.getTipoPersona();
			BigInteger idTipoDocumento = cuenta.getIdTipoDocumento();
			String numeroDocumento = cuenta.getNumeroDocumento();	
			BigInteger idPersona = null;
			switch (tipoPersona) {
			case NATURAL:
				PersonaNatural personaNatural = personaNaturalService.find(idTipoDocumento, numeroDocumento);
				if(personaNatural == null){
					model = Json.createObjectBuilder().add("message", "Socio no encontrado").build();
					return Response.status(Response.Status.NOT_FOUND).entity(model).build();
				}
				idPersona = personaNatural.getIdPersonaNatural();
				break;
			case JURIDICA:
				PersonaJuridica personaJuridica = personaJuridicaService.find(idTipoDocumento, numeroDocumento);
				if(personaJuridica == null){
					model = Json.createObjectBuilder().add("message", "Socio no encontrado").build();
					return Response.status(Response.Status.NOT_FOUND).entity(model).build();
				}
				idPersona = personaJuridica.getIdPersonaJuridica();
				break;
			default:
				break;
			}
			
			BigInteger idMoneda = cuenta.getIdMoneda();
			BigDecimal tasaInteres = cuenta.getTasaInteres();			
			int cantRetirantes = cuenta.getCantRetirantes();
			List<BigInteger> titulares = cuenta.getTitulares();
			List<Beneficiario> beneficiarios = cuenta.getBeneficiarios();
			
			BigInteger idCuenta = cuentaBancariaService.crearCuentaBancaria(TipoCuentaBancaria.AHORRO, agencia.getCodigo(), idMoneda, tasaInteres, tipoPersona, idPersona,null, cantRetirantes, titulares, beneficiarios);			
			model = Json.createObjectBuilder().add("message", "Cuenta creada").add("id", idCuenta).build();
			return Response.status(Response.Status.OK).entity(model).build();
		} catch (NonexistentEntityException e) {
			model = Json.createObjectBuilder().add("message", e.getMessage()).build();
			return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
		} catch (RollbackFailureException e) {
			model = Json.createObjectBuilder().add("message", e.getMessage()).build();
			return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
		} catch (EJBException e) {
			model = Json.createObjectBuilder().add("message", e.getMessage()).build();
			return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
		}		
	}
	
	@POST
	@Path("/corriente")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response createCuentaCorriente(
			CuentaCorrienteDTO cuenta, @Context SecurityContext context) {	
		try {		
			JsonObject model = null;
			
			String username = context.getUserPrincipal().getName();
			Usuario currentUser = usuarioService.findByUsername(username);
			Trabajador trabajador;
			if (currentUser != null) {
				trabajador = trabajadorService.findByUsuario(currentUser.getIdUsuario());
			}				
			else {
				model = Json.createObjectBuilder().add("message", "Usuario no encontrado").build();
				return Response.status(Response.Status.NOT_FOUND).entity(model).build();				
			}				
			Agencia agencia = trabajadorService.getAgencia(trabajador.getIdTrabajador());
			if(agencia == null){
				model = Json.createObjectBuilder().add("message", "Usuario no encontrado").build();
				return Response.status(Response.Status.NOT_FOUND).entity(model).build();	
			}
					
			TipoPersona tipoPersona = cuenta.getTipoPersona();
			BigInteger idTipoDocumento = cuenta.getIdTipoDocumento();
			String numeroDocumento = cuenta.getNumeroDocumento();	
			BigInteger idPersona = null;
			switch (tipoPersona) {
			case NATURAL:
				PersonaNatural personaNatural = personaNaturalService.find(idTipoDocumento, numeroDocumento);
				if(personaNatural == null){
					model = Json.createObjectBuilder().add("message", "Socio no encontrado").build();
					return Response.status(Response.Status.NOT_FOUND).entity(model).build();
				}
				idPersona = personaNatural.getIdPersonaNatural();
				break;
			case JURIDICA:
				PersonaJuridica personaJuridica = personaJuridicaService.find(idTipoDocumento, numeroDocumento);
				if(personaJuridica == null){
					model = Json.createObjectBuilder().add("message", "Socio no encontrado").build();
					return Response.status(Response.Status.NOT_FOUND).entity(model).build();
				}
				idPersona = personaJuridica.getIdPersonaJuridica();
				break;
			default:
				break;
			}
														
			BigInteger idMoneda = cuenta.getIdMoneda();		
			BigDecimal tasaInteres = cuenta.getTasaInteres();
			int cantRetirantes = cuenta.getCantRetirantes();
			List<BigInteger> titulares = cuenta.getTitulares();
			List<Beneficiario> beneficiarios = cuenta.getBeneficiarios();
			
			BigInteger idCuenta = cuentaBancariaService.crearCuentaBancaria(TipoCuentaBancaria.CORRIENTE, agencia.getCodigo(), idMoneda, tasaInteres, tipoPersona, idPersona,null, cantRetirantes, titulares, beneficiarios);					
			model = Json.createObjectBuilder().add("message", "Cuenta creada").add("id", idCuenta).build();
			return Response.status(Response.Status.OK).entity(model).build();
		} catch (NonexistentEntityException e) {
			JsonObject model = Json.createObjectBuilder().add("message", e.getMessage()).build();
			return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
		} catch (RollbackFailureException e) {
			JsonObject model = Json.createObjectBuilder().add("message", e.getMessage()).build();
			return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
		} catch (EJBException e) {
			JsonObject model = Json.createObjectBuilder().add("message", e.getMessage()).build();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(model).build();
		}			
	}
	
	@GET
	@Path("{id}/certificado")
	public Response getJson(@PathParam("id")BigInteger idCuentaBancaria) throws IOException, DocumentException {
		OutputStream file;		
					
		String titular = null;
		String documento = null;
		
		CuentaBancaria cuentaBancaria = cuentaBancariaService.findById(idCuentaBancaria);
		Socio socio = socioService.find(cuentaBancaria.getIdCuentaBancaria());
		PersonaNatural socioNatural = socio.getPersonaNatural();
		PersonaJuridica socioJuridico = socio.getPersonaJuridica();
		Agencia agencia = cuentaBancariaService.getAgencia(idCuentaBancaria);
		if(socioNatural == null && socioJuridico == null)
			System.out.println("errorrr");
		if(agencia == null)
			System.out.println("errorrr");
		if(socioNatural != null){
			documento = socioNatural.getTipoDocumento().getAbreviatura()+":"+socioNatural.getNumeroDocumento();
			titular = socioNatural.getApellidoPaterno()+" "+socioNatural.getApellidoMaterno()+","+socioNatural.getNombres();	
		}
		if(socioJuridico != null){
			documento = socioJuridico.getTipoDocumento().getAbreviatura()+":"+socioJuridico.getNumeroDocumento();
			titular = socioJuridico.getRazonSocial();	
		}			
		
		try {
			file = new FileOutputStream(new File("D:\\pdf\\"+idCuentaBancaria+".pdf"));
			Document document = new Document(PageSize.A5.rotate());																																																																																																																																			
			PdfWriter writer = PdfWriter.getInstance(document, file);		    												
		    document.open();
		    
		    Font font = FontFactory.getFont("Times-Roman", 7);	
		    
		    document.add(new Paragraph("\n"));
		    document.add(new Paragraph("\n"));
		    document.add(new Paragraph("\n"));
		    document.add(new Paragraph("\n"));
		    document.add(new Paragraph("\n"));
		    document.add(new Paragraph("\n"));
		    
		    Paragraph paragraph1 = new Paragraph();		
		    paragraph1.setFont(font);
		    Chunk numeroCuenta1 = new Chunk("Nº CUENTA:");
		    Chunk numeroCuenta2 = new Chunk(cuentaBancaria.getNumeroCuenta());
		    paragraph1.add(numeroCuenta1);
		    paragraph1.add(Chunk.SPACETABBING);
		    paragraph1.add(numeroCuenta2);		    
		    document.add(paragraph1);
		    
		    Paragraph paragraph2 = new Paragraph();
		    paragraph2.setFont(font);
		    Chunk agencia1 = new Chunk("AGENCIA:");
		    Chunk agencia2 = new Chunk(agencia.getCodigo()+" - "+agencia.getDenominacion().toUpperCase());
		    paragraph2.add(agencia1);
		    paragraph2.add(Chunk.SPACETABBING);
		    paragraph2.add(Chunk.SPACETABBING);
		    paragraph2.add(agencia2);		    
		    document.add(paragraph2);	
		    
		    Paragraph paragraph3 = new Paragraph();	
		    paragraph3.setFont(font);
		    Chunk monto1 = new Chunk("MONTO:");
		    Chunk monto2 = new Chunk(cuentaBancaria.getMoneda().getSimbolo()+cuentaBancaria.getSaldo().toString()+" - "+ProduceObject.getTextOfNumber(cuentaBancaria.getSaldo().intValue()));
		    paragraph3.add(monto1);
		    paragraph3.add(Chunk.SPACETABBING);
		    paragraph3.add(Chunk.SPACETABBING);
		    paragraph3.add(monto2);		    
		    document.add(paragraph3);
		    
		    Paragraph paragraph4 = new Paragraph();		
		    paragraph4.setFont(font);
		    Chunk socio1 = new Chunk("SOCIO:");
		    Chunk socio2 = new Chunk(titular.toUpperCase());
		    Chunk codigoSocio1 = new Chunk("CODIGO:");
		    Chunk codigoSocio2 = new Chunk(socio.getIdSocio().toString());
		    paragraph4.add(socio1);
		    paragraph4.add(Chunk.SPACETABBING);	
		    paragraph4.add(Chunk.SPACETABBING);	
		    paragraph4.add(socio2);	
		    paragraph4.add(Chunk.SPACETABBING);		    
		    paragraph4.add(codigoSocio1);		    	
		    paragraph4.add(Chunk.SPACETABBING);				    
		    paragraph4.add(codigoSocio2);		    
		    document.add(paragraph4);
		    
		    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");		    
		    String fechaAperturaString = df.format(cuentaBancaria.getFechaApertura());
		    String fechaVencimientoString = df.format(cuentaBancaria.getFechaCierre());

		    Paragraph paragraph6 = new Paragraph();		
		    paragraph6.setFont(font);
		    Chunk fechaApertura1 = new Chunk("F. APERTURA:");
		    Chunk fechaApertura2 = new Chunk(fechaAperturaString);
		    Chunk fechaVencimiento1 = new Chunk("F. VENCIMIENTO:");
		    Chunk fechaVencimiento2 = new Chunk(fechaVencimientoString);
		    paragraph6.add(fechaApertura1);
		    paragraph6.add(Chunk.SPACETABBING);		   
		    paragraph6.add(fechaApertura2);	
		    paragraph6.add(Chunk.SPACETABBING);
		    paragraph6.add(Chunk.SPACETABBING);
		    paragraph6.add(Chunk.SPACETABBING);
		    paragraph6.add(fechaVencimiento1);
		    paragraph6.add(Chunk.SPACETABBING);		   
		    paragraph6.add(fechaVencimiento2);	
		    document.add(paragraph6);
		    		 
		    Paragraph paragraph5 = new Paragraph();		 
		    paragraph5.setFont(font);
		    Chunk tasa1 = new Chunk("TASA INTERES EFECTIVA:");
		    Chunk tasa2 = new Chunk("6.00");
		    Chunk plazo1 = new Chunk("PLAZO:");
		    Chunk plazo2 = new Chunk("90 dias");		   
		    paragraph5.add(tasa1);
		    paragraph5.add(Chunk.SPACETABBING);
		    paragraph5.add(tasa2);
		    paragraph5.add(Chunk.SPACETABBING);
		    paragraph5.add(Chunk.SPACETABBING);
		    paragraph5.add(plazo1);
		    paragraph5.add(Chunk.SPACETABBING);
		    paragraph5.add(plazo2);			 			    		    
		    document.add(paragraph5);						    		    
		    
		    document.close();
		    file.close();		    
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (DocumentException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		
		PdfReader reader = new PdfReader("D:\\pdf\\"+idCuentaBancaria+".pdf");
	    	    
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    PdfStamper pdfStamper = new PdfStamper(reader, out);
	    AcroFields acroFields = pdfStamper.getAcroFields();
	    acroFields.setField("field_title", "test");
	    pdfStamper.close();
	    reader.close();
	    return Response.ok(out.toByteArray()).type("application/pdf").build();
	}
	
	@POST
	@Path("/plazoFijo")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response createCuentaPlazoFijo(CuentaPlazoFijoDTO cuenta, @Context SecurityContext context) {	
		try {		
			JsonObject model = null;
			
			String username = context.getUserPrincipal().getName();
			Usuario currentUser = usuarioService.findByUsername(username);
			Trabajador trabajador;
			if (currentUser != null) {
				trabajador = trabajadorService.findByUsuario(currentUser.getIdUsuario());
			}				
			else {
				model = Json.createObjectBuilder().add("message", "Usuario no encontrado").build();
				return Response.status(Response.Status.NOT_FOUND).entity(model).build();				
			}				
			Agencia agencia = trabajadorService.getAgencia(trabajador.getIdTrabajador());
			if(agencia == null){
				model = Json.createObjectBuilder().add("message", "Usuario no encontrado").build();
				return Response.status(Response.Status.NOT_FOUND).entity(model).build();	
			}
					
			TipoPersona tipoPersona = cuenta.getTipoPersona();
			BigInteger idTipoDocumento = cuenta.getIdTipoDocumento();
			String numeroDocumento = cuenta.getNumeroDocumento();	
			BigInteger idPersona = null;
			switch (tipoPersona) {
			case NATURAL:
				PersonaNatural personaNatural = personaNaturalService.find(idTipoDocumento, numeroDocumento);
				if(personaNatural == null){
					model = Json.createObjectBuilder().add("message", "Socio no encontrado").build();
					return Response.status(Response.Status.NOT_FOUND).entity(model).build();
				}
				idPersona = personaNatural.getIdPersonaNatural();
				break;
			case JURIDICA:
				PersonaJuridica personaJuridica = personaJuridicaService.find(idTipoDocumento, numeroDocumento);
				if(personaJuridica == null){
					model = Json.createObjectBuilder().add("message", "Socio no encontrado").build();
					return Response.status(Response.Status.NOT_FOUND).entity(model).build();
				}
				idPersona = personaJuridica.getIdPersonaJuridica();
				break;
			default:
				break;
			}
														
			BigInteger idMoneda = cuenta.getIdMoneda();
			BigDecimal monto = cuenta.getMonto();
			int periodo = cuenta.getPeriodo();
			BigDecimal tasaInteres = cuenta.getTasaInteres();
			int cantRetirantes = cuenta.getCantRetirantes();
			List<BigInteger> titulares = cuenta.getTitulares();
			List<Beneficiario> beneficiarios = cuenta.getBeneficiarios();
									
			BigInteger[] result = cajaSessionService.crearCuentaBancariaPlazoFijoConDeposito(agencia.getCodigo(), idMoneda, tipoPersona, idPersona, cantRetirantes, monto, periodo, tasaInteres, titulares, beneficiarios);										   
		      
			model = Json.createObjectBuilder().add("message", "Cuenta creada").add("id", result[0]).add("idTransaccion", result[1]).build();
			return Response.status(Response.Status.OK).entity(model).build();
		} catch (NonexistentEntityException e) {
			JsonObject model = Json.createObjectBuilder().add("message", e.getMessage()).build();
			return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
		} catch (RollbackFailureException e) {
			JsonObject model = Json.createObjectBuilder().add("message", e.getMessage()).build();
			return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
		} catch (EJBException e) {
			JsonObject model = Json.createObjectBuilder().add("message", e.getMessage()).build();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(model).build();
		}		
	}
	
	@POST
	@Path("/{id}/titular")
	@Produces({ "application/xml", "application/json" })
	public Response createTitular(@PathParam("id")BigInteger idCuenta, TitularDTO titularDTO) {	
		if(idCuenta != null && titularDTO != null){
			CuentaBancaria cuentaBancaria = cuentaBancariaService.findById(idCuenta);
			if(cuentaBancaria != null) {
				BigInteger idTitular;
				try {
					Titular titular = new Titular();
					
					TipoDocumento tipoDocumento = new TipoDocumento();
					tipoDocumento.setIdTipoDocumento(titularDTO.getIdTipoDocumento());
					
					PersonaNatural personaNatural = new PersonaNatural();
					personaNatural.setNumeroDocumento(titularDTO.getNumeroDocumento());
					personaNatural.setTipoDocumento(tipoDocumento);
					
					titular.setPersonaNatural(personaNatural);
					
					idTitular = cuentaBancariaService.addTitular(idCuenta, titular);
					JsonObject model = Json.createObjectBuilder().add("message", "Titular creado").add("id", idTitular).build();
					return Response.status(Response.Status.OK).entity(model).build();
				} catch (RollbackFailureException e) {
					JsonObject model = Json.createObjectBuilder().add("message", e.getMessage()).build();
					return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
				}					
			}					
			else {
				JsonObject model = Json.createObjectBuilder().add("message", "Cuenta no encontrada").build();
				return Response.status(Response.Status.NOT_FOUND).entity(model).build();				
			}	
		}			
		else {
			JsonObject model = Json.createObjectBuilder().add("message", "solicitud invalida").build();
			return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
		}
								
	}
	
	@POST
	@Path("/{id}/beneficiario")
	@Produces({ "application/xml", "application/json" })
	public Response createBeneficiario(@PathParam("id")BigInteger id, Beneficiario beneficiario) {	
		if(id != null && beneficiario != null){
			CuentaBancaria cuentaBancaria = cuentaBancariaService.findById(id);
			if(cuentaBancaria != null) {
				BigInteger idBeneficiario;
				try {
					idBeneficiario = cuentaBancariaService.addBeneficiario(id, beneficiario);
					JsonObject model = Json.createObjectBuilder().add("message", "beneficicario creado").add("id", idBeneficiario).build();
					return Response.status(Response.Status.OK).entity(model).build();
				} catch (RollbackFailureException e) {
					JsonObject model = Json.createObjectBuilder().add("message", e.getMessage()).build();
					return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(model).build();
				}					
			}					
			else {
				JsonObject model = Json.createObjectBuilder().add("message", "Cuenta no encontrada").build();
				return Response.status(Response.Status.NOT_FOUND).entity(model).build();				
			}	
		}			
		else {
			JsonObject model = Json.createObjectBuilder().add("message", "solicitud invalida").build();
			return Response.status(Response.Status.BAD_REQUEST).entity(model).build();
		}
								
	}
	
	@PUT	
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response updateCuentaBancaria(CuentaBancaria caja) {				
		return null;
	}
	
	@PUT
	@Path("/{id}/congelar")
	@Produces({ "application/xml", "application/json" })
	public Response congelarCuentaBancaria(@PathParam("id")BigInteger id) {				
		Response result = null;
		JsonObject model = null;
		
		if(id == null){
			model = Json.createObjectBuilder().add("message", "id no encontrado").build();
			result = Response.status(Response.Status.BAD_REQUEST).entity(model).build();	
		}			
		try {
			cuentaBancariaService.congelarCuentaBancaria(id);
			model = Json.createObjectBuilder().add("message", "Success").build();
			result = Response.status(Response.Status.OK).entity(model).build();
		} catch (RollbackFailureException e) {
			model = Json.createObjectBuilder().add("message", e.getMessage()).build();
			result = Response.status(Response.Status.BAD_REQUEST).entity(model).build();
		} catch (EJBException e) {
			model = Json.createObjectBuilder().add("message", e.getMessage()).build();
			result = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(model).build();
		}			
		return result;		
	}
	
	@PUT
	@Path("/{id}/descongelar")
	@Produces({ "application/xml", "application/json" })
	public Response descongelarCuentaBancaria(@PathParam("id")BigInteger id) {				
		Response result = null;
		JsonObject model = null;
		
		if(id == null){
			model = Json.createObjectBuilder().add("message", "id no encontrado").build();
			result = Response.status(Response.Status.BAD_REQUEST).entity(model).build();	
		}			
		try {
			cuentaBancariaService.descongelarCuentaBancaria(id);
			model = Json.createObjectBuilder().add("message", "Success").build();
			result = Response.status(Response.Status.OK).entity(model).build();
		} catch (RollbackFailureException e) {
			model = Json.createObjectBuilder().add("message", e.getMessage()).build();
			result = Response.status(Response.Status.BAD_REQUEST).entity(model).build();
		} catch (EJBException e) {
			model = Json.createObjectBuilder().add("message", e.getMessage()).build();
			result = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(model).build();
		}			
		return result;		
	}
	
	@PUT
	@Path("/{id}/recalcularPlazoFijo")
	@Produces({ "application/xml", "application/json" })
	public Response recalcularPlazoFijo(@PathParam("id")BigInteger id, CuentaPlazoFijoDTO cuenta) {				
		Response result = null;
		JsonObject model = null;
		
		if(id == null || cuenta == null){
			model = Json.createObjectBuilder().add("message", "id no encontrado").build();
			result = Response.status(Response.Status.BAD_REQUEST).entity(model).build();	
		}			
		try {			
			BigDecimal tasaInteres = cuenta.getTasaInteres();
			int periodo = cuenta.getPeriodo();
			cuentaBancariaService.recalcularCuentaPlazoFijo(id, periodo, tasaInteres);
			model = Json.createObjectBuilder().add("message", "Success").build();
			result = Response.status(Response.Status.OK).entity(model).build();
		} catch (RollbackFailureException e) {
			model = Json.createObjectBuilder().add("message", e.getMessage()).build();
			result = Response.status(Response.Status.BAD_REQUEST).entity(model).build();
		} catch (EJBException e) {
			model = Json.createObjectBuilder().add("message", e.getMessage()).build();
			result = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(model).build();
		}			
		return result;		
	}
	
	@DELETE
	@Path("/{id}")
	@Produces({ "application/xml", "application/json" })
	public Response deleteCuentaBancaria(@PathParam("id") BigInteger id) {				
		Response result = null;
		JsonObject model = null;		
		if(id == null){
			model = Json.createObjectBuilder().add("message", "Id no valido").build();
			result = Response.status(Response.Status.BAD_REQUEST).entity(model).build();	
		}			
		try {
			cuentaBancariaService.cancelarCuentaBancaria(id);
			model = Json.createObjectBuilder().add("message", "success").build();
			result = Response.status(Response.Status.OK).entity(model).build();
		} catch (RollbackFailureException e) {
			model = Json.createObjectBuilder().add("message", e.getMessage()).build();
			result = Response.status(Response.Status.BAD_REQUEST).entity(model).build();
		} catch (EJBException e) {
			model = Json.createObjectBuilder().add("message", e.getMessage()).build();
			result = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(model).build();
		}			
		return result;	
	}
	
	@GET
    @Path("{id}/voucherCuentaBancaria")  
    @Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
    public Response getVoucherCuentaBancaria(@PathParam("id") BigInteger idTransaccionBancaria){
    	VoucherTransaccionBancaria voucherTransaccionBancaria = cuentaBancariaService.getVoucherCuentaBancaria(idTransaccionBancaria);    	
		return Response.status(Response.Status.OK).entity(voucherTransaccionBancaria).build(); 
    }
	
	@GET
    @Path("{id}/voucherTransferenciaBancaria")  
    @Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
    public Response getVoucherTransferencia(@PathParam("id") BigInteger idTransferencia){
		VoucherTransferenciaBancaria voucherTransferenciaBancaria = cuentaBancariaService.getVoucherTransferenciaBancaria(idTransferencia);    	
		return Response.status(Response.Status.OK).entity(voucherTransferenciaBancaria).build(); 
    }
}
