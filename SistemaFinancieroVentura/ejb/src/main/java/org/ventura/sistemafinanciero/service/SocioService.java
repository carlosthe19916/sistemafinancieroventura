package org.ventura.sistemafinanciero.service;

import java.math.BigInteger;
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

	//no transaccional
	public Set<SocioView> findByFilterText(String filterText);

	public Set<SocioView> findAllView();
	
	public Socio findSocio(TipoPersona tipoPersona, BigInteger idTipoDoc, String numDoc);
	
	public PersonaNatural getPersonaNatural(BigInteger idSocio);

	public PersonaJuridica getPersonaJuridica(BigInteger idSocio);
	
	public PersonaNatural getApoderado(BigInteger idSocio);
	
	public CuentaAporte getCuentaAporte(BigInteger idSocio);

	public Set<CuentaBancaria> getCuentasBancarias(BigInteger idSocio);
	
	//transaccional
	public BigInteger create(TipoPersona tipoPersona,BigInteger idDocSocio, String numDocSocio,BigInteger idDocApoderado, String numDocApoderado) throws RollbackFailureException;	
	
}
