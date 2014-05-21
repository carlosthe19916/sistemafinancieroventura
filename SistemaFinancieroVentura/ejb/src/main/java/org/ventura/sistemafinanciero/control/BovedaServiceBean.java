package org.ventura.sistemafinanciero.control;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;

import org.ventura.sistemafinanciero.dao.DAO;
import org.ventura.sistemafinanciero.dao.QueryParameter;
import org.ventura.sistemafinanciero.entity.Boveda;
import org.ventura.sistemafinanciero.service.BovedaService;

@Named
@Stateless
@Remote(BovedaService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class BovedaServiceBean extends AbstractServiceBean<Boveda> implements BovedaService {
	
	@Inject
	private DAO<Object, Boveda> bovedaDAO;
	
	@Override
	public Set<Boveda> getBovedasByIdOficina(BigInteger idOficina) {
		Set<Boveda> result = null;
		QueryParameter queryParameter = QueryParameter.with("idoficina",idOficina);
		List<Boveda> list = bovedaDAO.findByNamedQuery("Boveda.findByIdoficina",queryParameter.parameters());
		result = new HashSet<Boveda>(list);
		return result;
	}

	@Override
	protected DAO<Object, Boveda> getDAO() {
		return this.bovedaDAO;
	}
	
}
