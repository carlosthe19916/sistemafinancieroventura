package org.ventura.sistemafinanciero.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.json.Json;
import javax.json.JsonObject;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.ventura.sistemafinanciero.entity.PersonaNatural;
import org.ventura.sistemafinanciero.exception.NonexistentEntityException;
import org.ventura.sistemafinanciero.exception.PreexistingEntityException;
import org.ventura.sistemafinanciero.exception.RollbackFailureException;
import org.ventura.sistemafinanciero.service.PersonaNaturalService;
import org.ventura.sistemafinanciero.service.TrabajadorService;
import org.ventura.sistemafinanciero.service.UsuarioService;

@Path("/personaNatural")
public class PersonaNaturalRESTService {

	private Logger log;

	@EJB
	private PersonaNaturalService personaNaturalService;
	
	@EJB
	private UsuarioService usuarioService;
	
	@EJB
	private TrabajadorService trabajadorService;
	
	private final String UPLOADED_FIRMA_PATH = "d:\\firmas\\";
	private final String UPLOADED_FOTO_PATH = "d:\\fotos\\";
	
	//cuerpo de la respuesta
	private final String ID_RESPONSE = "id";
	private final String MESSAGE_RESPONSE = "message";
	
	//mensajes
	private final String SUCCESS_MESSAGE = "Success";
	private final String NOT_FOUND_MESSAGE = "Persona no encontrada";
	private final String BAD_REQUEST_MESSAGE = "Datos invalidos";
	private final String CONFLICT_MESSAGE = "Persona ya existente";
		
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findById(@PathParam("id") @DefaultValue("-1") BigInteger id) {				
		Response result = null;
		JsonObject model = null;		
		if(id != null){
			PersonaNatural persona = personaNaturalService.findById(id);
			if(persona != null){				
				result = Response.status(Response.Status.OK).entity(persona).build();	
			} else {
				model = Json.createObjectBuilder().add(MESSAGE_RESPONSE, NOT_FOUND_MESSAGE).build();
				result = Response.status(Response.Status.NOT_FOUND).entity(model).build();	
			}
		} else {
			model = Json.createObjectBuilder().add(MESSAGE_RESPONSE, BAD_REQUEST_MESSAGE).build();
			result = Response.status(Response.Status.BAD_REQUEST).entity(model).build();
		}	
		return result;
	}

	@GET
	@Path("/buscar")
	@Produces({ "application/xml", "application/json" })
	public Response findByTipoNumeroDocumento(
			@QueryParam("idTipoDocumento") @DefaultValue("-1") BigInteger idtipodocumento,
			@QueryParam("numeroDocumento") @DefaultValue("") String numerodocumento) {
		Response result = null;
		JsonObject model = null;
		if(idtipodocumento == null || numerodocumento == null || numerodocumento.isEmpty() || numerodocumento.trim().isEmpty()){
			 model = Json.createObjectBuilder().add(MESSAGE_RESPONSE, BAD_REQUEST_MESSAGE).build();
			 result = Response.status(Response.Status.BAD_REQUEST).entity(model).build();
		}
		try {							
			PersonaNatural persona = personaNaturalService.find(idtipodocumento, numerodocumento);
			if(persona != null) {				
				result = Response.status(Response.Status.OK).entity(persona).build(); 				
			}				
			else {
				model = Json.createObjectBuilder().add(MESSAGE_RESPONSE, NOT_FOUND_MESSAGE).build();
				result = Response.status(Response.Status.NOT_FOUND).entity(model).build();
			}				 
		} catch (EJBException e) {
			log.log(Level.SEVERE, e.getMessage());
			model = Json.createObjectBuilder().add(MESSAGE_RESPONSE, e.getMessage()).build();
			result = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(model).build();
		}	
		return result;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response listAll(
			@QueryParam("filterText") String filterText,
			@QueryParam("offset") BigInteger offset,
			@QueryParam("limit") BigInteger limit) {
		if(offset != null && offset.compareTo(BigInteger.ZERO) < 1)
			offset = BigInteger.ZERO;
		if(limit != null && limit.compareTo(BigInteger.ZERO) < 1)
			limit = BigInteger.ZERO;
		
		List<PersonaNatural> list = personaNaturalService.findAll(filterText, offset, limit);		
		Response result = null;
		JsonObject model = null;
		if(list != null){						
			result = Response.status(Response.Status.OK).entity(list).build();
		} else {
			model = Json.createObjectBuilder().add(MESSAGE_RESPONSE, NOT_FOUND_MESSAGE).build();
			result = Response.status(Response.Status.NOT_FOUND).entity(model).build();	
		}
		return result;
	}
	
	@GET
	@Path("/count")
	@Produces(MediaType.APPLICATION_JSON)
	public Response countAll(
			@QueryParam("filterText") String filterText) {				
		int size = personaNaturalService.count();		
		Response result = Response.status(Response.Status.OK).entity(size).build();
		return result;
	}
	
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") @DefaultValue("-1") BigInteger id, PersonaNatural personanatural) {
		Response result = null;
		JsonObject model = null;
		try {
			personaNaturalService.update(id, personanatural);
			model = Json.createObjectBuilder().add(MESSAGE_RESPONSE, SUCCESS_MESSAGE).build();
			result = Response.status(Response.Status.OK).entity(model).build();
		} catch (NonexistentEntityException e) {
			model = Json.createObjectBuilder().add(MESSAGE_RESPONSE, NOT_FOUND_MESSAGE).build();
			result = Response.status(Response.Status.NOT_FOUND).entity(model).build();
		} catch (PreexistingEntityException e) {
			model = Json.createObjectBuilder().add(MESSAGE_RESPONSE, CONFLICT_MESSAGE).build();
			result = Response.status(Response.Status.CONFLICT).entity(model).build();
		} catch (EJBException e) {
			log.log(Level.SEVERE, e.getMessage());
			model = Json.createObjectBuilder().add(MESSAGE_RESPONSE, e.getMessage()).build();
			result = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(model).build();
		}
		return result;
	}
	
	@POST
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response create(PersonaNatural personaNatural) {
		Response result = null;
		JsonObject model = null;
		try {
			PersonaNatural persona = personaNaturalService.create(personaNatural);
			BigInteger idPersona = persona.getIdPersonaNatural();
			model = Json.createObjectBuilder().add(MESSAGE_RESPONSE, SUCCESS_MESSAGE).add(ID_RESPONSE, idPersona).build();
			result = Response.status(Response.Status.OK).entity(model).build();
		} catch (PreexistingEntityException e) {
			model = Json.createObjectBuilder().add(MESSAGE_RESPONSE, CONFLICT_MESSAGE).build();
			result = Response.status(Response.Status.CONFLICT).entity(model).build();
		} catch (javax.validation.ConstraintViolationException e) {
			model = Json.createObjectBuilder().add(MESSAGE_RESPONSE, BAD_REQUEST_MESSAGE).build();
			result = Response.status(Response.Status.BAD_REQUEST).entity(model).build();
		} catch (EJBException e) {
			log.log(Level.SEVERE, e.getMessage());
			model = Json.createObjectBuilder().add(MESSAGE_RESPONSE, e.getMessage()).build();
			result = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(model).build();
		}
		return result;
	}
	
	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") @DefaultValue("-1") BigInteger id) {
		Response result = null;
		JsonObject model = null;
		try {
			personaNaturalService.delete(id);
			model = Json.createObjectBuilder().add(MESSAGE_RESPONSE, SUCCESS_MESSAGE).build();
			result = Response.status(Response.Status.OK).entity(model).build();
		} catch (NonexistentEntityException e) {
			model = Json.createObjectBuilder().add(MESSAGE_RESPONSE, NOT_FOUND_MESSAGE).build();
			result = Response.status(Response.Status.NOT_FOUND).entity(model).build();
		} catch (RollbackFailureException e) {
			model = Json.createObjectBuilder().add(MESSAGE_RESPONSE, e.getMessage()).build();
			result = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(model).build();
		} catch (EJBException e) {
			log.log(Level.SEVERE, e.getMessage());
			model = Json.createObjectBuilder().add(MESSAGE_RESPONSE, e.getMessage()).build();
			result = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(model).build();
		}
		return result;
	}
	
	@GET
	@Path("{id}/firma")
	@Produces("image/png")
	public Response getFirma(@PathParam("id") String id,
			@QueryParam("flowChunkNumber") int flowChunkNumber,
			@QueryParam("flowChunkSize") int flowChunkSize,
			@QueryParam("flowCurrentChunkSize") int flowCurrentChunkSize,
			@QueryParam("flowFilename") String flowFilename,
			@QueryParam("flowIdentifier") String flowIdentifier,
			@QueryParam("flowRelativePath") String flowRelativePath,
			@QueryParam("flowTotalChunks") int flowTotalChunks,
			@QueryParam("flowTotalSize") int flowTotalSize) {
 
		if(flowFilename != null)
			return Response.status(Response.Status.NOT_FOUND).build();
		
		File file = new File(this.UPLOADED_FIRMA_PATH + id);

		if(!file.exists())
			file = new File(this.UPLOADED_FIRMA_PATH + "default.gif");
		
		ResponseBuilder response = Response.status(Response.Status.OK).entity((Object) file);		
		
		response.header("Content-Disposition", "attachment; filename=image"+ id + ".png");
		return response.build();
 
	}
	
	@GET
	@Path("{id}/foto")
	@Produces("image/png")
	public Response getFoto(@PathParam("id") String id,
			@QueryParam("flowChunkNumber") int flowChunkNumber,
			@QueryParam("flowChunkSize") int flowChunkSize,
			@QueryParam("flowCurrentChunkSize") int flowCurrentChunkSize,
			@QueryParam("flowFilename") String flowFilename,
			@QueryParam("flowIdentifier") String flowIdentifier,
			@QueryParam("flowRelativePath") String flowRelativePath,
			@QueryParam("flowTotalChunks") int flowTotalChunks,
			@QueryParam("flowTotalSize") int flowTotalSize) {
 
		if(flowFilename != null)
			return Response.status(Response.Status.NOT_FOUND).build();
		
		
		File file = new File(this.UPLOADED_FOTO_PATH + id);

		if(!file.exists())
			file = new File(this.UPLOADED_FOTO_PATH + "default.gif");
		
		ResponseBuilder response = Response.status(Response.Status.OK).entity((Object) file);		
		
		response.header("Content-Disposition", "attachment; filename=image"+ id + ".png");
		return response.build();
 
	}
	
	@GET
	@Path("/firma/upload")
	public Response uploadFirma(
			@QueryParam("flowChunkNumber") int flowChunkNumber,
			@QueryParam("flowChunkSize") int flowChunkSize,
			@QueryParam("flowCurrentChunkSize") int flowCurrentChunkSize,
			@QueryParam("flowFilename") String flowFilename,
			@QueryParam("flowIdentifier") String flowIdentifier,
			@QueryParam("flowRelativePath") String flowRelativePath,
			@QueryParam("flowTotalChunks") int flowTotalChunks,
			@QueryParam("flowTotalSize") int flowTotalSize,
			@QueryParam("id") int id) {
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@GET
	@Path("/foto/upload")
	public Response uploadFoto(
			@QueryParam("flowChunkNumber") int flowChunkNumber,
			@QueryParam("flowChunkSize") int flowChunkSize,
			@QueryParam("flowCurrentChunkSize") int flowCurrentChunkSize,
			@QueryParam("flowFilename") String flowFilename,
			@QueryParam("flowIdentifier") String flowIdentifier,
			@QueryParam("flowRelativePath") String flowRelativePath,
			@QueryParam("flowTotalChunks") int flowTotalChunks,
			@QueryParam("flowTotalSize") int flowTotalSize,
			@QueryParam("id") int id) {
		return Response.status(Response.Status.NOT_FOUND).build();
	}
	
	@POST
	@Path("/{id}/firma")
	@Consumes("multipart/form-data")
	public Response uploadFirma(@PathParam("id") BigInteger id,			
			MultipartFormDataInput input) {		
		
		String fileName = "";

		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("file");

		//try {
		//	fileName = input.getFormDataPart("id", new GenericType<String>() { });
			fileName = id.toString();
		//} catch (IOException e) {			
		//	return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal error").build();
		//}
		
		for (InputPart inputPart : inputParts) {

			try {

				//MultivaluedMap<String, String> header = inputPart.getHeaders();
				//fileName = getFileName(header);

				// convert the uploaded file to inputstream
				InputStream inputStream = inputPart.getBody(InputStream.class,null);

				byte[] bytes = IOUtils.toByteArray(inputStream);

				// constructs upload file path
				fileName = UPLOADED_FIRMA_PATH + fileName;

				writeFile(bytes, fileName);				

			} catch (IOException e) {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal error").build();
			}
		}
		return Response.status(200).entity("uploadFile is called, Uploaded file name : " + fileName).build();

	}
	@POST
	@Path("/{id}/foto")
	@Consumes("multipart/form-data")
	public Response uploadFoto(@PathParam("id") BigInteger id,MultipartFormDataInput input) {

		String fileName = "";

		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("file");

		//try {
			//fileName = input.getFormDataPart("id", new GenericType<String>() { });	
			fileName = id.toString();
		//} catch (IOException e) {			
		//	return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal error").build();
		//}
		
		for (InputPart inputPart : inputParts) {

			try {

				//MultivaluedMap<String, String> header = inputPart.getHeaders();
				//fileName = getFileName(header);

				// convert the uploaded file to inputstream
				InputStream inputStream = inputPart.getBody(InputStream.class,null);

				byte[] bytes = IOUtils.toByteArray(inputStream);

				// constructs upload file path
				fileName = UPLOADED_FOTO_PATH + fileName;

				writeFile(bytes, fileName);				

			} catch (IOException e) {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal error").build();
			}
		}
		return Response.status(200).entity("uploadFile is called, Uploaded file name : " + fileName).build();

	}
	
	/**
	 * header sample { Content-Type=[image/png], Content-Disposition=[form-data;
	 * name="file"; filename="filename.extension"] }
	 **/
	// get uploaded filename, is there a easy way in RESTEasy?
	@SuppressWarnings("unused")
	private String getFileName(MultivaluedMap<String, String> header) {

		String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

		for (String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {

				String[] name = filename.split("=");

				String finalFileName = name[1].trim().replaceAll("\"", "");
				return finalFileName;
			}
		}
		return "unknown";
	}

	// save to somewhere
	private void writeFile(byte[] content, String filename) throws IOException {
		File file = new File(filename);

		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream fop = new FileOutputStream(file);
		fop.write(content);
		fop.flush();
		fop.close();
	}
		
}
