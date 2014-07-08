package org.ventura.sistemafinanciero.control;

import java.math.BigDecimal;
import java.math.BigInteger;
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
import org.ventura.sistemafinanciero.entity.CuentaBancaria;
import org.ventura.sistemafinanciero.entity.Moneda;
import org.ventura.sistemafinanciero.entity.MonedaDenominacion;
import org.ventura.sistemafinanciero.entity.Socio;
import org.ventura.sistemafinanciero.entity.TransaccionBancaria;
import org.ventura.sistemafinanciero.entity.TransaccionBovedaCaja;
import org.ventura.sistemafinanciero.entity.TransaccionBovedaCajaDetalle;
import org.ventura.sistemafinanciero.entity.TransaccionCajaCaja;
import org.ventura.sistemafinanciero.entity.dto.VoucherTransaccionBovedaCaja;
import org.ventura.sistemafinanciero.entity.dto.VoucherTransaccionCajaCaja;
import org.ventura.sistemafinanciero.service.TransaccionInternaService;

@Named
@Stateless
@Remote(TransaccionInternaService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class TransaccionInternaServiceBean implements TransaccionInternaService {

	@Inject
	private DAO<Object, TransaccionBovedaCaja> transaccionBovedaCajaDAO;
	
	@Inject
	private DAO<Object, TransaccionCajaCaja> transaccioCajaCajaDAO;

	@Override
	public VoucherTransaccionBovedaCaja getVoucherTransaccionBovedaCaja(BigInteger idTransaccionBovedaCaja) {
		TransaccionBovedaCaja transaccion = transaccionBovedaCajaDAO.find(idTransaccionBovedaCaja);
		if(transaccion == null)
			return null;
		
		// recuperando transaccion								
		Caja caja = transaccion.getHistorialCaja().getCaja();
		Boveda boveda = transaccion.getHistorialBoveda().getBoveda();
		Moneda moneda = boveda.getMoneda();
		
		Set<TransaccionBovedaCajaDetalle> detalleTransaccion = transaccion.getTransaccionBovedaCajaDetalls();
		BigDecimal totalTransaccion = BigDecimal.ZERO;
		Set<BovedaCaja> list = caja.getBovedaCajas();
		Agencia agencia = null;
		for (BovedaCaja bovedaCaja : list) {
			agencia = bovedaCaja.getBoveda().getAgencia();
			break;
		}
		for (TransaccionBovedaCajaDetalle det : detalleTransaccion) {
			MonedaDenominacion denominacion = det.getMonedaDenominacion();
			BigDecimal valor = denominacion.getValor();
			BigInteger cantidad = det.getCantidad();
			BigDecimal subtotal = valor.multiply(new BigDecimal(cantidad));
			totalTransaccion = totalTransaccion.add(subtotal);
		}
				
		
		Hibernate.initialize(moneda);
		VoucherTransaccionBovedaCaja voucher = new VoucherTransaccionBovedaCaja();
		
		voucher.setId(transaccion.getIdTransaccionBovedaCaja());
		voucher.setAgenciaAbreviatura(agencia.getAbreviatura());
		voucher.setAgenciaDenominacion(agencia.getDenominacion());
		voucher.setEstadoConfirmacion(transaccion.getEstadoConfirmacion());
		voucher.setEstadoSolicitud(transaccion.getEstadoSolicitud());
		voucher.setFecha(transaccion.getFecha());
		voucher.setHora(transaccion.getHora());
		voucher.setMoneda(moneda);
		voucher.setMonto(totalTransaccion);
		voucher.setObservacion(transaccion.getObservacion());
		voucher.setOrigen(transaccion.getOrigen());
		voucher.setCajaDenominacion(caja.getDenominacion());
		voucher.setCajaAbreviatura(caja.getAbreviatura());
		//voucher.setTrabajador(transaccion.get);
		
		return voucher;
	}

	@Override
	public VoucherTransaccionCajaCaja getVoucherTransaccionCajaCaja(
			BigInteger idTransaccionCajaCaja) {
		// TODO Auto-generated method stub
		return null;
	}

}
