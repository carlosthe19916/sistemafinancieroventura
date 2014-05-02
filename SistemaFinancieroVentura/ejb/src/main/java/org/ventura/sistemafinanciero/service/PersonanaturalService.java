package org.ventura.sistemafinanciero.service;

import java.util.Set;

import javax.ejb.Remote;

import org.ventura.sistemafinanciero.entity.PersonaNatural;

@Remote
public interface PersonanaturalService extends AbstractService<PersonaNatural>{

	public PersonaNatural findByTipoNumeroDocumento(int idTipodocumento, String numerodocumento);

	public Set<PersonaNatural> findByFilterText(String filterText);

	public PersonaNatural findByTrabajador(int idTrabajador);
	
}
