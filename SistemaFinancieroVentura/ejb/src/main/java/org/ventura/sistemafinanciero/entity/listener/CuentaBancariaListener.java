package org.ventura.sistemafinanciero.entity.listener;

import java.math.BigInteger;

import javax.inject.Inject;
import javax.persistence.PostPersist;
import javax.persistence.PrePersist;

import org.ventura.sistemafinanciero.dao.DAO;
import org.ventura.sistemafinanciero.entity.CuentaBancaria;
import org.ventura.sistemafinanciero.entity.type.TipoCuentaBancaria;
import org.ventura.sistemafinanciero.util.EntityManagerProducer;
import org.ventura.sistemafinanciero.util.ProduceObject;

public class CuentaBancariaListener {
	
	@PostPersist
	public void setNumeroCuenta(Object obj) {		
		CuentaBancaria cuentaBancaria = (CuentaBancaria) obj;
		String numeroCuenta = cuentaBancaria.getNumeroCuenta();
		
		BigInteger idCuenta = cuentaBancaria.getIdCuentaBancaria();
		BigInteger idMoneda = cuentaBancaria.getMoneda().getIdMoneda();
		TipoCuentaBancaria tipoCuenta = cuentaBancaria.getTipoCuentaBancaria();		
		
		numeroCuenta = numeroCuenta +  completar(idCuenta.toString(), 8);
		numeroCuenta = numeroCuenta + idMoneda;
		
		switch (tipoCuenta) {
		case AHORRO:
			numeroCuenta = numeroCuenta + ProduceObject.CODIGO_CUENTA_AHORRO;
			break;
		case CORRIENTE:
			numeroCuenta = numeroCuenta + ProduceObject.CODIGO_CUENTA_CORRIENTE;
			break;
		case PLAZO_FIJO:
			numeroCuenta = numeroCuenta + ProduceObject.CODIGO_CUENTA_PLAZO_FIJO;
			break;
		default:
			break;
		}
		
		cuentaBancaria.setNumeroCuenta(numeroCuenta);
		//cuentaBancariaDAO.update(cuentaBancaria);
	}
	
	public static String completar(String cad, int caracteres){
		StringBuffer sb = new StringBuffer(cad);
		for (int i = cad.length(); i < caracteres; i++) {
			sb.append("0");
		}
		return sb.toString();
	}

}
