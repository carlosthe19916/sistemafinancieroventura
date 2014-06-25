package org.ventura.sistemafinanciero.service;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Remote;

import org.ventura.sistemafinanciero.entity.PersonaNatural;

@Remote
public interface PersonaNaturalService extends AbstractService<PersonaNatural>{
		
	/**
     * Devuelve la persona para los parametros indicados
     *
     * @param  idTipoDocumento BigInteger para indicar el Tipo de Documento.
     * @param  numeroDocumento String para indicar el Numero de Documento.
     * @return PersonaNatural persona para los parametros o NULL si no encontró resultados.
     */
	public PersonaNatural find(BigInteger idTipoDocumento, String numeroDocumento);	
	
	/**
     * Devuelve la persona para los parametros indicados
     *
     * @return List<PersonaNatural> todas las personas del sistema.
     */
	public List<PersonaNatural> findAll();
	
	/**
     * Devuelve la persona para los parametros indicados
     * @param  offset BigInteger para indicar el inicio de resultados.
     * @param  limit BigInteger para indicar la cantidad de resultados.
     * @return List<PersonaNatural> todas las personas del sistema.
     */
	public List<PersonaNatural> findAll(BigInteger offset, BigInteger limit);
	
	/**
     * Devuelve la persona para los parametros indicados
     * @param  filterText String para indicar texto a buscar en la persona.
     * @return List<PersonaNatural> todas las personas para los parametros indicados.
     */
	public List<PersonaNatural> findAll(String filterText);
	
	/**
     * Devuelve la persona para los parametros indicados
     * @param  filterText String para indicar texto a buscar en la persona.
     * @param  offset BigInteger para indicar el inicio de resultados.
     * @param  limit BigInteger para indicar la cantidad de resultados.
     * @return List<PersonaNatural> todas las personas para los parametros indicados.
     */
	public List<PersonaNatural> findAll(String filterText, BigInteger offset, BigInteger limit);
	
}
