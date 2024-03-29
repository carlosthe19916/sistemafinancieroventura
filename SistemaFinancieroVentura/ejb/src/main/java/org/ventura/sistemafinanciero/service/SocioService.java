package org.ventura.sistemafinanciero.service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Remote;

import org.ventura.sistemafinanciero.entity.CuentaAporte;
import org.ventura.sistemafinanciero.entity.CuentaBancaria;
import org.ventura.sistemafinanciero.entity.HistorialAportesSP;
import org.ventura.sistemafinanciero.entity.PersonaJuridica;
import org.ventura.sistemafinanciero.entity.PersonaNatural;
import org.ventura.sistemafinanciero.entity.Socio;
import org.ventura.sistemafinanciero.entity.SocioView;
import org.ventura.sistemafinanciero.entity.dto.VoucherTransaccionCuentaAporte;
import org.ventura.sistemafinanciero.entity.type.TipoPersona;
import org.ventura.sistemafinanciero.exception.RollbackFailureException;

@Remote
public interface SocioService extends AbstractService<Socio> {

	public List<SocioView> findAllView();		
	
	public List<SocioView> findAllView(String filterText);
	
	public List<SocioView> findAllView(Boolean estadoCuentaAporte);	
	
	public List<SocioView> findAllView(String filterText, Boolean cuentaAporte);
	
	public List<SocioView> findAllView(Boolean estadoCuentaAporte, Boolean estadoSocio);
		
	public List<SocioView> findAllView(String filterText, Boolean estadoCuentaAporte, Boolean estadoSocio);
		
	public List<SocioView> findAllView(BigInteger offset, BigInteger limit);
	
	public List<SocioView> findAllView(Boolean cuentaAporte, BigInteger offset, BigInteger limit);
	
	public List<SocioView> findAllView(Boolean estadoCuentaAporte, Boolean estadoSocio, BigInteger offset, BigInteger limit);

	public List<SocioView> findAllView(String filterText, BigInteger offset, BigInteger limit);

	public List<SocioView> findAllView(String filterText, Boolean cuentaAporte, BigInteger offset, BigInteger limit);

	public List<SocioView> findAllView(String filterText, Boolean estadoCuentaAporte, Boolean estadoSocio, BigInteger offset, BigInteger limit);
	
	
	public Socio find(TipoPersona tipoPersona, BigInteger idTipoDocumento, String numeroDocumento);	

	public Socio find(BigInteger idCuentaBancaria);
	
	public PersonaNatural getPersonaNatural(BigInteger idSocio);

	public PersonaJuridica getPersonaJuridica(BigInteger idSocio);

	public PersonaNatural getApoderado(BigInteger idSocio);

	public CuentaAporte getCuentaAporte(BigInteger idSocio);

	public Set<CuentaBancaria> getCuentasBancarias(BigInteger idSocio);

	public List<HistorialAportesSP> getHistorialAportes(BigInteger idSocio, Date desde, Date hasta, BigInteger offset, BigInteger limit);
	// transaccional
	public BigInteger create(BigInteger idAgencia, TipoPersona tipoPersona, 
			BigInteger idDocSocio, String numDocSocio, BigInteger 
			idDocApoderado, String numDocApoderado) throws RollbackFailureException;
	
	public void congelarCuentaAporte(BigInteger idSocio) throws RollbackFailureException;
	
	public void descongelarCuentaAporte(BigInteger idSocio) throws RollbackFailureException;
	
	public void inactivarSocio(BigInteger idSocio) throws RollbackFailureException;

	public void cambiarApoderado(BigInteger idSocio, BigInteger idPersonaNatural)throws RollbackFailureException;

	public void eliminarApoderado(BigInteger idSocio)throws RollbackFailureException;

	public VoucherTransaccionCuentaAporte getVoucherCancelacion(BigInteger idTransaccion);
	
	public VoucherTransaccionCuentaAporte getVoucherCuentaAporte(BigInteger idTransaccion);

}
