package org.ventura.sistemafinanciero.control;

import java.math.BigDecimal;
import java.math.BigInteger;
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
import org.ventura.sistemafinanciero.entity.TasaInteres;
import org.ventura.sistemafinanciero.entity.ValorTasaInteres;
import org.ventura.sistemafinanciero.entity.type.TasaInteresType;
import org.ventura.sistemafinanciero.service.TasaInteresService;

@Named
@Stateless
@Remote(TasaInteresService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class TasaInteresServiceBean extends AbstractServiceBean<TasaInteres> implements TasaInteresService {

	@Inject
	private DAO<Object, Moneda> monedaDAO;
	@Inject
	private DAO<Object, ValorTasaInteres> valorTasaInteresDAO;
	@Inject
	private DAO<Object, TasaInteres> tasaInteresDAO;
	
	@Override
	public BigDecimal getTasaInteresCuentaPlazoFijo(BigInteger idMoneda,
			int periodo, BigDecimal monto) {
		return null;
	}

	@Override
	public BigDecimal getTasaInteresCuentaAhorro(BigInteger idMoneda) {
		Moneda moneda = monedaDAO.find(idMoneda);
		ValorTasaInteres valorTasaInteres = null;
		if(moneda == null)
			return null;
		QueryParameter queryParameter = QueryParameter
				.with("tasaInteresDenominacion", TasaInteresType.TASA_CUENTA_AHORRO.toString())
				.and("idMoneda", idMoneda);
		List<ValorTasaInteres> list = valorTasaInteresDAO.findByNamedQuery(ValorTasaInteres.finByDenominacionTasaAndIdMoneda, queryParameter.parameters());
		if(list.size() == 1)
			valorTasaInteres = list.get(0);
		
		if(valorTasaInteres != null)
			return valorTasaInteres.getValor();
		else
			return null;
	}

	@Override
	public BigDecimal getTasaInteresCuentaCorriente(BigInteger idMoneda) {
		Moneda moneda = monedaDAO.find(idMoneda);
		ValorTasaInteres valorTasaInteres = null;
		if(moneda == null)
			return null;
		QueryParameter queryParameter = QueryParameter
				.with("tasaInteresDenominacion", TasaInteresType.TASA_CUENTA_CORRIENTE.toString())
				.and("idMoneda", idMoneda);
		List<ValorTasaInteres> list = valorTasaInteresDAO.findByNamedQuery(ValorTasaInteres.finByDenominacionTasaAndIdMoneda, queryParameter.parameters());
		if(list.size() == 1)
			valorTasaInteres = list.get(0);
		
		if(valorTasaInteres != null)
			return valorTasaInteres.getValor();
		else
			return null;
	}

	@Override
	public BigDecimal getInteresGenerado(BigDecimal monto, int periodo,
			BigDecimal tasaInteres) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected DAO<Object, TasaInteres> getDAO() {
		return tasaInteresDAO;
	}
	
}
