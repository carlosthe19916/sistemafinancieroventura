package org.ventura.sistemafinanciero.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

import javax.ejb.Remote;

import org.ventura.sistemafinanciero.entity.CuentaAporte;
import org.ventura.sistemafinanciero.entity.CuentaBancaria;
import org.ventura.sistemafinanciero.entity.PersonaJuridica;
import org.ventura.sistemafinanciero.entity.PersonaNatural;
import org.ventura.sistemafinanciero.entity.Socio;
import org.ventura.sistemafinanciero.entity.SocioView;
import org.ventura.sistemafinanciero.entity.type.TipoPersona;
import org.ventura.sistemafinanciero.exception.RollbackFailureException;

@Remote
public interface SocioService extends AbstractService<Socio>{

	/**
     * Devuelve la lista de socios buscados por la cadena FILTERTEXT del sistema,
     * incluyendo socios de Personas naturales y juridicas.
     *
     * @param  filterText String cadena a buscar en el sistema.
     * @param  range BigInteger[] para indicar el rango de resultados.
     * @param  modeSocio Boolean: TRUE todos los socios, FALSE socios con cuenta aporte, NULL todos los socios
     * @param  modeEstado Boolean: TRUE socios activos, FALSE socios inactivos, NULL todos los socios
     * @return List<SocioView> lista de socios.
     */
	public List<SocioView> findByFilterText(String filterText,
			BigInteger[] range, Boolean modeSocio, Boolean modeEstado);	
	
	/**
     * Devuelve la lista de socios del sistema,
     * incluyendo socios de Personas naturales y juridicas.
     *
     * @param  range BigInteger[] para indicar el rango de resultados.
     * @param  modeSocio Boolean: TRUE todos los socios, FALSE socios con cuenta aporte, NULL todos los socios
     * @param  modeEstado Boolean: TRUE socios activos, FALSE socios inactivos, NULL todos los socios
     * @return List<SocioView> lista de socios.
     */
	public List<SocioView> findAllView(BigInteger[] range, Boolean modeSocio, Boolean modeEstado);
	
	public Socio findSocio(TipoPersona tipoPersona, BigInteger idTipoDoc, String numDoc);
	
	public Socio findSocioByCuenta(BigInteger idCuentaBancaria);
	
	public PersonaNatural getPersonaNatural(BigInteger idSocio);

	public PersonaJuridica getPersonaJuridica(BigInteger idSocio);
	
	public PersonaNatural getApoderado(BigInteger idSocio);
	
	public CuentaAporte getCuentaAporte(BigInteger idSocio);

	public Set<CuentaBancaria> getCuentasBancarias(BigInteger idSocio);
	
	//transaccional
	public BigInteger create(BigInteger idAgencia, TipoPersona tipoPersona,BigInteger idDocSocio, String numDocSocio,BigInteger idDocApoderado, String numDocApoderado) throws RollbackFailureException;
	
	
}
