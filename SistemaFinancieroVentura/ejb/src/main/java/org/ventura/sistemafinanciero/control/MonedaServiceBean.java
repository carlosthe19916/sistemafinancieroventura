package org.ventura.sistemafinanciero.control;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
import org.ventura.sistemafinanciero.entity.dto.GenericDetalle;
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
	public List<MonedaDenominacion> getDenominaciones(BigInteger idMoneda) {				
		QueryParameter queryParameter = QueryParameter.with("idmoneda", idMoneda);
		List<MonedaDenominacion> denominaciones = monedaDenominacionDAO.findByNamedQuery(MonedaDenominacion.allActive,queryParameter.parameters());
		return denominaciones;		
	}

	@Override
	public Set<GenericDetalle> getGenericDenominaciones(BigInteger idMoneda) {
		Moneda moneda = monedaDAO.find(idMoneda);
		if(moneda != null){
			Set<GenericDetalle> result = new TreeSet<GenericDetalle>();
			Set<MonedaDenominacion> denominaciones = moneda.getMonedaDenominacions();
			for (MonedaDenominacion monedaDenominacion : denominaciones) {
				BigDecimal valor = monedaDenominacion.getValor();				
				GenericDetalle detalle = new GenericDetalle(valor, BigInteger.ZERO);
				result.add(detalle);
			}
			return result;
		} else {
			return null;	
		}		
	}
	
	@Override
	protected DAO<Object, Moneda> getDAO() {
		return monedaDAO;
	}


}
