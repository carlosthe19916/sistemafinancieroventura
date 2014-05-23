package org.ventura.sistemafinanciero.service;

import java.math.BigInteger;
import java.util.Set;

import javax.ejb.Remote;

import org.ventura.sistemafinanciero.entity.PersonaNatural;
import org.ventura.sistemafinanciero.exception.PreexistingEntityException;

@Remote
public interface PersonaNaturalService extends AbstractService<PersonaNatural>{

	public BigInteger crear(PersonaNatural personaNatural) throws PreexistingEntityException;
	
	public PersonaNatural findByTipoNumeroDocumento(BigInteger idTipodocumento, String numerodocumento);

	public Set<PersonaNatural> findByFilterText(String filterText);

	public PersonaNatural findByTrabajador(BigInteger idTrabajador);
	
}
