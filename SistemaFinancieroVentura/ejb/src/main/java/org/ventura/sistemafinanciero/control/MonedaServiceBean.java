package org.ventura.sistemafinanciero.control;

import java.util.Set;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.Hibernate;
import org.ventura.sistemafinanciero.dao.DAO;
import org.ventura.sistemafinanciero.entity.Moneda;
import org.ventura.sistemafinanciero.entity.MonedaDenominacion;
import org.ventura.sistemafinanciero.exception.NonexistentEntityException;
import org.ventura.sistemafinanciero.service.MonedaService;

@Named
@Stateless
@Remote(MonedaService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class MonedaServiceBean extends AbstractServiceBean<Moneda> implements MonedaService {

	//private Logger LOGGER = LoggerFactory.getLogger(MonedaService.class);

	@Inject private DAO<Object, Moneda> monedaDAO;

	@Override
	public Set<MonedaDenominacion> getDenominaciones(int idMoneda) throws NonexistentEntityException {		
		Moneda moneda = monedaDAO.find(idMoneda);
		if (moneda == null)
			throw new NonexistentEntityException("Moneda no encontrada");
		Set<MonedaDenominacion> denominaciones = moneda.getMonedaDenominacions();
		Hibernate.initialize(denominaciones);
		return denominaciones;		
	}

	@Override
	protected DAO<Object, Moneda> getDAO() {
		return monedaDAO;
	}

}
