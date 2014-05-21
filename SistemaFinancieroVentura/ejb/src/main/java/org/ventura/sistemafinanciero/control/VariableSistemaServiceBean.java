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
import org.ventura.sistemafinanciero.entity.VariableSistema;
import org.ventura.sistemafinanciero.entity.type.Variable;
import org.ventura.sistemafinanciero.service.VariableSistemaService;

@Named
@Stateless
@Remote(VariableSistemaService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class VariableSistemaServiceBean extends AbstractServiceBean<VariableSistema> implements VariableSistemaService {

	private Logger LOGGER = LoggerFactory.getLogger(VariableSistemaService.class);
	
	@Inject
	private DAO<Object, VariableSistema> variableSistemaDAO;

	@Override
	public VariableSistema find(Variable variable) {
		QueryParameter queryParameter = QueryParameter.with("denominacion", variable.toString());
		List<VariableSistema> list = variableSistemaDAO.findByNamedQuery(VariableSistema.findByDenominacion, queryParameter.parameters());
		for (VariableSistema variableSistema : list) {
			return variableSistema;
		}
		return null;
	}

	@Override
	protected DAO<Object, VariableSistema> getDAO() {
		return variableSistemaDAO;
	}
	
}
