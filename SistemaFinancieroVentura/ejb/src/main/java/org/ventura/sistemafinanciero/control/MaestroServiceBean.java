package org.ventura.sistemafinanciero.control;

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

	@Override
	public List<TipoDocumento> getAll(TipoPersona tipopersona) {
		List<TipoDocumento> list = null;
		QueryParameter queryParameter = QueryParameter.with("tipopersona", tipopersona.toString());
		list = tipodocumentoDAO.findByNamedQuery(TipoDocumento.findByTipopersona, queryParameter.parameters());		
		return list;
	}

}
