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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jboss.resteasy.util.GenericType;

@Path("/personanatural/image")
public class ImageRESTService {

	private final String UPLOADED_FIRMA_PATH = "d:\\firmas\\";
	private final String UPLOADED_FOTO_PATH = "d:\\fotos\\";

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
	@Path("/firma/upload")
	@Consumes("multipart/form-data")
	public Response uploadFirma(MultipartFormDataInput input) {

		String fileName = "";

		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("file");

		try {
			fileName = input.getFormDataPart("id", new GenericType<String>() { });			
		} catch (IOException e) {			
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal error").build();
		}
		
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
	@Path("/foto/upload")
	@Consumes("multipart/form-data")
	public Response uploadFoto(MultipartFormDataInput input) {

		String fileName = "";

		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("file");

		try {
			fileName = input.getFormDataPart("id", new GenericType<String>() { });			
		} catch (IOException e) {			
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal error").build();
		}
		
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
	
	@GET
	@Path("{id}/firma")
	@Produces("image/png")
	public Response getFirma(@PathParam("id") String id) {
 
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
	public Response getFoto(@PathParam("id") String id) {
 
		File file = new File(this.UPLOADED_FOTO_PATH + id);

		if(!file.exists())
			file = new File(this.UPLOADED_FOTO_PATH + "default.gif");
		
		ResponseBuilder response = Response.status(Response.Status.OK).entity((Object) file);		
		
		response.header("Content-Disposition", "attachment; filename=image"+ id + ".png");
		return response.build();
 
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
