package org.ventura.sistemafinanciero.control;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
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
import org.ventura.sistemafinanciero.entity.Boveda;
import org.ventura.sistemafinanciero.service.BovedaService;

@DeclareRoles({ "ADMIN", "JEFECAJA" })
@Named
@Stateless
@Remote(BovedaService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class BovedaServiceBean extends AbstractServiceBean<Boveda> implements BovedaService {

	private static Logger LOGGER = LoggerFactory.getLogger(BovedaService.class);

	@Inject
	private DAO<Object, Boveda> bovedaDAO;

	@RolesAllowed("JEFECAJA")
	@Override
	public Set<Boveda> getBovedasByIdOficina(int idOficina) {
		Set<Boveda> result = null;
		try {
			QueryParameter queryParameter = QueryParameter.with("idoficina",idOficina);
			List<Boveda> list = bovedaDAO.findByNamedQuery("Boveda.findByIdoficina",queryParameter.parameters());
			result = new HashSet<Boveda>(list);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			
		}
		return result;
	}

	@Override
	protected DAO<Object, Boveda> getDAO() {
		return this.bovedaDAO;
	}
	
}
