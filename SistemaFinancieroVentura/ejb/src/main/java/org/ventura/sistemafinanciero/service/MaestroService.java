package org.ventura.sistemafinanciero.service;

import java.util.List;

import javax.ejb.Remote;

import org.ventura.sistemafinanciero.entity.TipoDocumento;
import org.ventura.sistemafinanciero.entity.type.Tipopersona;

@Remote
public interface MaestroService {

	public List<TipoDocumento> getAll(Tipopersona tipopersona);
	
}
