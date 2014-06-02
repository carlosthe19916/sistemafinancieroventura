package org.ventura.sistemafinanciero.control;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ventura.sistemafinanciero.dao.DAO;
import org.ventura.sistemafinanciero.dao.QueryParameter;
import org.ventura.sistemafinanciero.entity.Departamento;
import org.ventura.sistemafinanciero.entity.Distrito;
import org.ventura.sistemafinanciero.entity.Pais;
import org.ventura.sistemafinanciero.entity.Provincia;
import org.ventura.sistemafinanciero.entity.TipoDocumento;
import org.ventura.sistemafinanciero.entity.type.TipoPersona;
import org.ventura.sistemafinanciero.service.MaestroService;

@Named
@Stateless
@Remote(MaestroService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class MaestroServiceBean implements MaestroService {

	private static Logger LOGGER = LoggerFactory.getLogger(MaestroService.class);

	@Inject
	private DAO<Object, TipoDocumento> tipodocumentoDAO;

	@Inject
	private DAO<Object, Pais> paisDAO;
	
	@Inject
	private DAO<Object, Departamento> departamentoDAO;	
	@Inject
	private DAO<Object, Provincia> provinciaDAO;
	@Inject
	private DAO<Object, Distrito> distritoDAO;
	
	@Override
	public List<TipoDocumento> getAll(TipoPersona tipopersona) {
		List<TipoDocumento> list = null;
		QueryParameter queryParameter = QueryParameter.with("tipopersona", tipopersona.toString());
		list = tipodocumentoDAO.findByNamedQuery(TipoDocumento.findByTipopersona, queryParameter.parameters());		
		return list;
	}

	@Override
	public List<Pais> getPaises() {		
		return paisDAO.findAll();
	}
	
	@Override
	public Pais findPaisByAbreviatura(String abrevitura) {
		if(abrevitura == null || abrevitura.isEmpty())
			return null;
		QueryParameter namedQueryName = QueryParameter.with("abreviatura", abrevitura);
		List<Pais> list = paisDAO.findByNamedQuery(Pais.findByAbreviatura, namedQueryName.parameters());
		if(list.size() == 1)
			return list.get(0);
		else 
			return null;
	}

	@Override
	public Pais findPaisByCodigo(String codigo) {
		if(codigo == null || codigo.isEmpty())
			return null;
		QueryParameter namedQueryName = QueryParameter.with("abreviatura", codigo);
		List<Pais> list = paisDAO.findByNamedQuery(Pais.findByCodigo, namedQueryName.parameters());
		if(list.size() == 1)
			return list.get(0);
		else 
			return null;
	}
	
	@Override
	public List<Departamento> getDepartamentos() {
		return departamentoDAO.findAll();
	}

	@Override
	public List<Provincia> getProvincias(BigInteger idDepartamento) {
		QueryParameter queryParameter = QueryParameter.with("iddepartamento", idDepartamento);
		List<Provincia> provincias = provinciaDAO.findByNamedQuery(Provincia.findByIdDepartamento, queryParameter.parameters());
		return provincias;
	}

	@Override
	public List<Distrito> getDistritos(BigInteger idProvincia) {
		QueryParameter queryParameter = QueryParameter.with("idprovincia", idProvincia);
		List<Distrito> provincias = distritoDAO.findByNamedQuery(Distrito.findByIdProvincia, queryParameter.parameters());
		return provincias;
	}

}
