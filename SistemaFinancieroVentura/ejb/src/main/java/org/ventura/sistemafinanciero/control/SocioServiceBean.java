package org.ventura.sistemafinanciero.control;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.ventura.sistemafinanciero.entity.SocioView;
import org.ventura.sistemafinanciero.service.SocioService;

@Named
@Stateless
@Remote(SocioService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SocioServiceBean extends AbstractServiceBean<SocioView> implements SocioService {

	private static Logger LOGGER = LoggerFactory.getLogger(SocioServiceBean.class);

	@Inject
	private DAO<Object, SocioView> socioViewDAO;

	@Override
	public Set<SocioView> findByFilterText(String filterText) {		
		if(filterText == null)
			return new HashSet<SocioView>();
		if(filterText.isEmpty() || filterText.trim().isEmpty()){
			return new HashSet<SocioView>();
		}
		List<SocioView> list = null;
		QueryParameter queryParameter = QueryParameter.with("filtertext",'%' + filterText.toUpperCase() + '%');
		list = socioViewDAO.findByNamedQuery(SocioView.FindByFilterTextSocioView, queryParameter.parameters(), 1000);												
		return new HashSet<SocioView>(list);
	}

	@Override
	protected DAO<Object, SocioView> getDAO() {
		return socioViewDAO;
	}

}
