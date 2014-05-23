package org.ventura.sistemafinanciero.service;

import java.math.BigInteger;
import java.util.Set;

import javax.ejb.Remote;

import org.ventura.sistemafinanciero.entity.Socio;
import org.ventura.sistemafinanciero.entity.SocioView;
import org.ventura.sistemafinanciero.entity.type.TipoPersona;
import org.ventura.sistemafinanciero.exception.RollbackFailureException;

@Remote
public interface SocioService extends AbstractService<SocioView>{

	//no transaccional
	public Set<SocioView> findByFilterText(String filterText);

	public Socio findSocio(TipoPersona tipoPersona, BigInteger idTipoDoc, String numDoc);
	
	//transaccional
	public BigInteger create(TipoPersona tipoPersona,BigInteger idDocSocio, String numDocSocio,BigInteger idDocApoderado, String numDocApoderado) throws RollbackFailureException;
	
}
