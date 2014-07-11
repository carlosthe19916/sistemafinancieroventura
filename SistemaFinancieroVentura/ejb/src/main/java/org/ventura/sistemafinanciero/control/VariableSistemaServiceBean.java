package org.ventura.sistemafinanciero.control;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;

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
import org.ventura.sistemafinanciero.entity.Moneda;
import org.ventura.sistemafinanciero.entity.VariableSistema;
import org.ventura.sistemafinanciero.entity.type.Variable;
import org.ventura.sistemafinanciero.service.VariableSistemaService;
import org.ventura.sistemafinanciero.util.ProduceObject;

@Named
@Stateless
@Remote(VariableSistemaService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class VariableSistemaServiceBean extends AbstractServiceBean<VariableSistema> implements VariableSistemaService {

	private Logger LOGGER = LoggerFactory.getLogger(VariableSistemaService.class);
	
	@Inject
	private DAO<Object, VariableSistema> variableSistemaDAO;
	@Inject
	private DAO<Object, Moneda> monedaDAO;
	
	@Override
	public VariableSistema find(Variable variable) {
		QueryParameter queryParameter = QueryParameter.with("denominacion", variable.toString());
		List<VariableSistema> list = variableSistemaDAO.findByNamedQuery(VariableSistema.findByDenominacion, queryParameter.parameters());
		for (VariableSistema variableSistema : list) {
			return variableSistema;
		}
		return null;
	}

	@Override
	protected DAO<Object, VariableSistema> getDAO() {
		return variableSistemaDAO;
	}

	@Override
	public BigDecimal getTasaCambio(BigInteger idMonedaRecibida, BigInteger idMonedaEntregada) {
		Moneda monedaRecibida = monedaDAO.find(idMonedaRecibida);
		Moneda monedaEntregada = monedaDAO.find(idMonedaEntregada);
		if(monedaRecibida == null)
			return null;
		if(monedaEntregada == null)
			return null;
		
		String simboloMonedaRecibida = monedaRecibida.getSimbolo().toUpperCase();
		String simboloMonedaEntregada = monedaEntregada.getSimbolo().toUpperCase();
		
		BigDecimal result = null;
		
		switch (simboloMonedaRecibida) {
		case "S/.":
			if(simboloMonedaEntregada.equalsIgnoreCase("$")){
				VariableSistema var = find(Variable.TASA_VENTA_DOLAR);
				result = var.getValor();
			} 
			if(simboloMonedaEntregada.equalsIgnoreCase("€")){
				VariableSistema var = find(Variable.TASA_VENTA_EURO);
				result = var.getValor();
			}
			break;
		case "$":
			if(simboloMonedaEntregada.equalsIgnoreCase("S/.")){
				VariableSistema var = find(Variable.TASA_COMPRA_DOLAR);
				result = var.getValor();								
			} 
			if(simboloMonedaEntregada.equalsIgnoreCase("€")){
				VariableSistema var1 = find(Variable.TASA_COMPRA_DOLAR);
				VariableSistema var2 = find(Variable.TASA_VENTA_EURO);
				result = var2.getValor().divide(var1.getValor(), 3, RoundingMode.FLOOR);
			}
			break;
		case "€":
			if(simboloMonedaEntregada.equalsIgnoreCase("S/.")){
				VariableSistema var = find(Variable.TASA_COMPRA_EURO);
				result = var.getValor();
			} 
			if(simboloMonedaEntregada.equalsIgnoreCase("$")){
				VariableSistema var1 = find(Variable.TASA_COMPRA_EURO);
				VariableSistema var2 = find(Variable.TASA_VENTA_DOLAR);
				result = var2.getValor().divide(var1.getValor(), 3, RoundingMode.FLOOR);
			}
			break;
		default:
			break;
		}		
		return result;
	}
	
}
