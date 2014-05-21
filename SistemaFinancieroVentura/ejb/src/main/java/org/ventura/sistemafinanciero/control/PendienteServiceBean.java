package org.ventura.sistemafinanciero.control;

import java.math.BigInteger;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.Hibernate;
import org.ventura.sistemafinanciero.dao.DAO;
import org.ventura.sistemafinanciero.entity.Moneda;
import org.ventura.sistemafinanciero.entity.PendienteCaja;
import org.ventura.sistemafinanciero.service.PendienteService;

@Named
@Stateless
@Remote(PendienteService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PendienteServiceBean extends AbstractServiceBean<PendienteCaja> implements PendienteService {

	@Inject
	private DAO<Object, PendienteCaja> pendienteCajaDAO;
	
	@Override
	public PendienteCaja getPendiente(BigInteger id) {
		PendienteCaja pendiente = pendienteCajaDAO.find(id);
		Moneda moneda = pendiente.getMoneda();
		Hibernate.initialize(moneda);
		return pendiente;
	}	
	
	@Override
	protected DAO<Object, PendienteCaja> getDAO() {
		return this.pendienteCajaDAO;
	}
		
	
}
