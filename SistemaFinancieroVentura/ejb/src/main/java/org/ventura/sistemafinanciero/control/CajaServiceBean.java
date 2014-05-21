package org.ventura.sistemafinanciero.control;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.Hibernate;
import org.ventura.sistemafinanciero.dao.DAO;
import org.ventura.sistemafinanciero.entity.Boveda;
import org.ventura.sistemafinanciero.entity.BovedaCaja;
import org.ventura.sistemafinanciero.entity.Caja;
import org.ventura.sistemafinanciero.entity.Moneda;
import org.ventura.sistemafinanciero.service.CajaService;

@Named
@Stateless
@Remote(CajaService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CajaServiceBean extends AbstractServiceBean<Caja> implements CajaService {	
	
	@Inject private DAO<Object, Caja> cajaDAO;
	
	@Override
	public Set<Boveda> getBovedasByCaja(BigInteger idCaja) {
		Set<Boveda> result = null;
		Caja caja = cajaDAO.find(idCaja);
		if(caja != null){
			result = new HashSet<Boveda>();
			Set<BovedaCaja> bovedaCajas= caja.getBovedaCajas();
			for (BovedaCaja bovedaCaja : bovedaCajas) {
				Boveda boveda = bovedaCaja.getBoveda();
				Moneda moneda = boveda.getMoneda();				
				Hibernate.initialize(boveda);
				Hibernate.initialize(moneda);
				result.add(boveda);
			}
		}	
		return result;
	}

	@Override
	protected DAO<Object, Caja> getDAO() {
		return this.cajaDAO;
	}
		
	
}
