package org.ventura.sistemafinanciero.service;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Remote;

import org.ventura.sistemafinanciero.entity.PersonaNatural;

@Remote
public interface PersonaNaturalService extends AbstractService<PersonaNatural>{
	
	public PersonaNatural find(BigInteger idTrabajador);
	
	public PersonaNatural find(BigInteger idTipodocumento, String numerodocumento);	
	
	public List<PersonaNatural> findAll();
	
	public List<PersonaNatural> findAll(BigInteger[] range);
	
	public List<PersonaNatural> findAll(String filterText);
	
	public List<PersonaNatural> findAll(String filterText, BigInteger[] range);
	
}
