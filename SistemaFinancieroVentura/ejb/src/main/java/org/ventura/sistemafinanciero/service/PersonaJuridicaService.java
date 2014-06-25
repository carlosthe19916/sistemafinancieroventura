package org.ventura.sistemafinanciero.service;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Remote;

import org.ventura.sistemafinanciero.entity.PersonaJuridica;

@Remote
public interface PersonaJuridicaService extends AbstractService<PersonaJuridica> {

	/**
     * Devuelve la persona para los parametros indicados
     *
     * @param  idTipoDocumento BigInteger para indicar el Tipo de Documento.
     * @param  numeroDocumento String para indicar el Numero de Documento.
     * @return PersonaJuridica persona para los parametros o NULL si no encontró resultados.
     */
	public PersonaJuridica find(BigInteger idTipodocumento, String numerodocumento);
	
	/**
     * Devuelve la persona para los parametros indicados
     *
     * @return List<PersonaJuridica> todas las personas del sistema.
     */
	public List<PersonaJuridica> findAll();

	/**
     * Devuelve la persona para los parametros indicados
     * @param  offset BigInteger para indicar el inicio de resultados.
     * @param  limit BigInteger para indicar la cantidad de resultados.
     * @return List<PersonaJuridica> todas las personas del sistema.
     */
	public List<PersonaJuridica> findAll(BigInteger offset, BigInteger limit);

	/**
     * Devuelve la persona para los parametros indicados
     * @param  filterText String para indicar texto a buscar en la persona.
     * @return List<PersonaJuridica> todas las personas para los parametros indicados.
     */
	public List<PersonaJuridica> findAll(String filterText);

	/**
     * Devuelve la persona para los parametros indicados
     * @param  filterText String para indicar texto a buscar en la persona.
     * @param  offset BigInteger para indicar el inicio de resultados.
     * @param  limit BigInteger para indicar la cantidad de resultados.
     * @return List<PersonaJuridica> todas las personas para los parametros indicados.
     */
	public List<PersonaJuridica> findAll(String filterText, BigInteger offset, BigInteger limit);
}
