package org.ventura.sistemafinanciero.service;

import java.util.Set;

import javax.ejb.Remote;

import org.ventura.sistemafinanciero.entity.SocioView;

@Remote
public interface SocioService extends AbstractService<SocioView>{

	public Set<SocioView> findByFilterText(String filterText);
	
}
