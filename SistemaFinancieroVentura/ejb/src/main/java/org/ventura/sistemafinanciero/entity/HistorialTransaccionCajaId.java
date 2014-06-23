package org.ventura.sistemafinanciero.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class HistorialTransaccionCajaId implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(unique = false, nullable = false)
	private BigInteger idHistorialCaja;

	@Column(unique = false, nullable = false)
	private BigInteger numeroOperacion;

	public BigInteger getIdHistorialCaja() {
		return idHistorialCaja;
	}

	public void setIdHistorialCaja(BigInteger idHistorialCaja) {
		this.idHistorialCaja = idHistorialCaja;
	}

	public BigInteger getNumeroOperacion() {
		return numeroOperacion;
	}

	public void setNumeroOperacion(BigInteger numeroOperacion) {
		this.numeroOperacion = numeroOperacion;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof HistorialTransaccionCajaId)) {
			return false;
		}
		HistorialTransaccionCajaId castOther = (HistorialTransaccionCajaId) other;
		return this.idHistorialCaja.equals(castOther.idHistorialCaja)
				&& this.numeroOperacion.equals(castOther.numeroOperacion);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idHistorialCaja.hashCode();
		hash = hash * prime + this.numeroOperacion.hashCode();
		return hash;
	}
}
