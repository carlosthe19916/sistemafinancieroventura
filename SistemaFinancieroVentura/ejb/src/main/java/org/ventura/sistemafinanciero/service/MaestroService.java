package org.ventura.sistemafinanciero.service;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Remote;

import org.ventura.sistemafinanciero.entity.Departamento;
import org.ventura.sistemafinanciero.entity.Distrito;
import org.ventura.sistemafinanciero.entity.Pais;
import org.ventura.sistemafinanciero.entity.Provincia;
import org.ventura.sistemafinanciero.entity.TipoDocumento;
import org.ventura.sistemafinanciero.entity.type.TipoPersona;

@Remote
public interface MaestroService {

	public List<TipoDocumento> getAll(TipoPersona tipopersona);
	
	public List<Pais> getPaises();
	
	public List<Departamento> getDepartamentos();
	public List<Provincia> getProvincias(BigInteger idDepartamento);
	public List<Distrito> getDistritos(BigInteger idProvincia);
	
}
