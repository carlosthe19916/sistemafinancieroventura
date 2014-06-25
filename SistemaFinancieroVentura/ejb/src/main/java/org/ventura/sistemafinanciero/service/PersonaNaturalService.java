package org.ventura.sistemafinanciero.service;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Remote;

import org.ventura.sistemafinanciero.entity.PersonaNatural;

@Remote
public interface PersonaNaturalService extends AbstractService<PersonaNatural> {

	/**
	 * Busca una persona
	 *
	 * @param idTipoDocumento
	 *            indica el documento.
	 * @param numeroDocumento
	 *            indica el numero de documento.
	 * @return retorna un objeto PersonaNatural o NULL si no encontró
	 *         resultados.
	 */
	public PersonaNatural find(BigInteger idTipoDocumento, String numeroDocumento);

	/**
	 * Devuelve la lista de todas las personas del sistema
	 *
	 * @return List<PersonaNatural> retorna una lista de todas las personas.
	 */
	public List<PersonaNatural> findAll();

	/**
	 * Devuelve la lista de todas las personas del sistema que esten en el rango
	 * de offset-limit
	 * 
	 * @param offset
	 *            BigInteger indica el inicio del primer resultado. Si offset es
	 *            NULL, entonces offset se pondrá a cero por defecto.
	 * @param limit
	 *            BigInteger indica el numero maximo de resultados. Si limit es
	 *            NULL, entonces no se pondrá limites al resultado.
	 * @return List<PersonaNatural> retorna la lista dentro de los limites
	 *         indicados.
	 */
	public List<PersonaNatural> findAll(BigInteger offset, BigInteger limit);

	/**
	 * Devuelve la lista de todas las personas del sistema que esten concuerden
	 * con la palabra clave enviada.
	 * 
	 * @param filterText
	 *            indica la palabra clave.
	 * @return List<PersonaNatural> retorna la lista de personas.
	 */
	public List<PersonaNatural> findAll(String filterText);

	/**
	 * Devuelve la lista de todas las personas del sistema que esten en el rango
	 * de offset-limit
	 * 
	 * @param filterText
	 *            String para indicar texto a buscar en la persona.
	 * @param offset
	 *            BigInteger indica el inicio del primer resultado. Si offset es
	 *            NULL, entonces offset se pondrá a cero por defecto.
	 * @param limit
	 *            BigInteger indica el numero maximo de resultados. Si limit es
	 *            NULL, entonces no se pondrá limites al resultado.
	 * @return List<PersonaNatural> retorna la lista dentro de los limites
	 *         indicados.
	 */
	public List<PersonaNatural> findAll(String filterText, BigInteger offset, BigInteger limit);

}
