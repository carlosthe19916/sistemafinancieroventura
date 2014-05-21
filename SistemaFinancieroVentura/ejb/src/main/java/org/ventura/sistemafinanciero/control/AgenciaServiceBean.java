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
import org.ventura.sistemafinanciero.entity.Agencia;
import org.ventura.sistemafinanciero.entity.Boveda;
import org.ventura.sistemafinanciero.entity.BovedaCaja;
import org.ventura.sistemafinanciero.entity.Caja;
import org.ventura.sistemafinanciero.service.AgenciaService;

@Named
@Stateless
@Remote(AgenciaService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class AgenciaServiceBean extends AbstractServiceBean<Agencia> implements AgenciaService {
	
	@Inject
	private DAO<Object, Agencia> agenciaDAO;

	@Override
	public Set<Caja> getCajas(BigInteger idAgencia) {		
		Agencia agencia = agenciaDAO.find(idAgencia);
		if(agencia == null)
			return null;
		Set<Caja> result = new HashSet<Caja>();
		Set<Boveda> bovedas = agencia.getBovedas();
		for (Boveda boveda : bovedas) {
			Set<BovedaCaja> bovedasCasjas = boveda.getBovedaCajas();
			for (BovedaCaja bovedaCaja : bovedasCasjas) {
				Caja caja = bovedaCaja.getCaja();
				Hibernate.initialize(caja);
				result.add(caja);
			}
		}
		return result;
	}

	@Override
	protected DAO<Object, Agencia> getDAO() {
		return agenciaDAO;
	}

}
