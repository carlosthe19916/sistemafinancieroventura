package org.ventura.sistemafinanciero.service;

import java.math.BigInteger;
import java.util.Set;

import javax.ejb.Remote;

import org.ventura.sistemafinanciero.entity.PersonaJuridica;
import org.ventura.sistemafinanciero.exception.PreexistingEntityException;

@Remote
public interface PersonaJuridicaService extends AbstractService<PersonaJuridica>{

	public BigInteger crear(PersonaJuridica personaJuridica) throws PreexistingEntityException;
	
	public PersonaJuridica findByTipoNumeroDocumento(BigInteger idTipodocumento, String numerodocumento);

	public Set<PersonaJuridica> findByFilterText(String filterText);
	
}
