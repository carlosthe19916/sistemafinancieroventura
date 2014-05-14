package org.ventura.sistemafinanciero.control;

import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;

import org.ventura.sistemafinanciero.dao.DAO;
import org.ventura.sistemafinanciero.dao.QueryParameter;
import org.ventura.sistemafinanciero.entity.Moneda;
import org.ventura.sistemafinanciero.entity.MonedaDenominacion;
import org.ventura.sistemafinanciero.service.MonedaService;

@Named
@Stateless
@Remote(MonedaService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class MonedaServiceBean extends AbstractServiceBean<Moneda> implements MonedaService {

	//private Logger LOGGER = LoggerFactory.getLogger(MonedaService.class);

	@Inject 
	private DAO<Object, Moneda> monedaDAO;
	@Inject
	private DAO<Object, MonedaDenominacion> monedaDenominacionDAO;

	@Override
	public List<MonedaDenominacion> getDenominaciones(int idMoneda) {				
		QueryParameter queryParameter = QueryParameter.with("idmoneda", idMoneda);
		List<MonedaDenominacion> denominaciones = monedaDenominacionDAO.findByNamedQuery(MonedaDenominacion.allActive,queryParameter.parameters());
		return denominaciones;		
	}

	@Override
	protected DAO<Object, Moneda> getDAO() {
		return monedaDAO;
	}

}
