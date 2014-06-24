package org.ventura.sistemafinanciero.service;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Remote;

import org.ventura.sistemafinanciero.entity.PersonaJuridica;

@Remote
public interface PersonaJuridicaService extends AbstractService<PersonaJuridica> {

	public PersonaJuridica find(BigInteger idTipodocumento, String numerodocumento);
	
	public List<PersonaJuridica> findAll();

	public List<PersonaJuridica> findAll(BigInteger offset, BigInteger limit);

	public List<PersonaJuridica> findAll(String filterText);

	public List<PersonaJuridica> findAll(String filterText, BigInteger offset, BigInteger limit);
}
