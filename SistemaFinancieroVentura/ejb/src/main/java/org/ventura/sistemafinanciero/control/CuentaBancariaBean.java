package org.ventura.sistemafinanciero.control;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ventura.sistemafinanciero.dao.DAO;
import org.ventura.sistemafinanciero.entity.Agencia;
import org.ventura.sistemafinanciero.entity.Beneficiario;
import org.ventura.sistemafinanciero.entity.CuentaBancaria;
import org.ventura.sistemafinanciero.entity.CuentaBancariaTasa;
import org.ventura.sistemafinanciero.entity.Moneda;
import org.ventura.sistemafinanciero.entity.PersonaJuridica;
import org.ventura.sistemafinanciero.entity.PersonaNatural;
import org.ventura.sistemafinanciero.entity.Socio;
import org.ventura.sistemafinanciero.entity.Titular;
import org.ventura.sistemafinanciero.entity.type.EstadoCuentaBancaria;
import org.ventura.sistemafinanciero.entity.type.TipoCuentaBancaria;
import org.ventura.sistemafinanciero.entity.type.TipoPersona;
import org.ventura.sistemafinanciero.exception.RollbackFailureException;
import org.ventura.sistemafinanciero.service.CuentaBancariaService;
import org.ventura.sistemafinanciero.service.TasaInteresService;
import org.ventura.sistemafinanciero.util.ProduceObject;

@Named
@Stateless
@Remote(CuentaBancariaService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CuentaBancariaBean extends AbstractServiceBean<CuentaBancaria> implements CuentaBancariaService {

	private static Logger LOGGER = LoggerFactory.getLogger(CuentaBancariaBean.class);

	@Inject
	private DAO<Object, CuentaBancaria> cuentaBancariaDAO;
	@Inject
	private DAO<Object, PersonaNatural> personaNaturalDAO;
	@Inject
	private DAO<Object, PersonaJuridica> personaJuridicaDAO;
	@Inject
	private DAO<Object, Socio> socioDAO;
	@Inject
	private DAO<Object, Moneda> monedaDAO;
	@Inject
	private DAO<Object, Titular> titularDAO;
	@Inject
	private DAO<Object, Beneficiario> beneficiarioDAO;
	@Inject
	private DAO<Object, CuentaBancariaTasa> cuentaBancariaTasaDAO;
	@Inject
	private DAO<Object, Agencia> agenciaDAO;
	
	@EJB
	private TasaInteresService tasaInteresService;
	
	@Override
	public Set<CuentaBancaria> findByFilterText(String filterText) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public BigInteger createCuentaAhorro(BigInteger idAgencia, BigInteger idMoneda,
			TipoPersona tipoPersona, BigInteger idPersona, int cantRetirantes,
			List<BigInteger> titulares, List<Beneficiario> beneficiarios)
			throws RollbackFailureException {
		
		PersonaNatural personaNatural = null;
		PersonaJuridica personaJuridica = null;
		
		switch (tipoPersona) {				
		case NATURAL:
			personaNatural = personaNaturalDAO.find(idPersona);
			break;
		case JURIDICA:
			personaJuridica = personaJuridicaDAO.find(idPersona);
			break;
		default:
			break;
		}
		
		Calendar calendar = Calendar.getInstance();
		Moneda moneda = monedaDAO.find(idMoneda);
		Agencia agencia = agenciaDAO.find(idAgencia);
		
		if(agencia == null)
			throw new RollbackFailureException("Agencia no encontrada");
		
		//verificar si existe el socio
		Set<Socio> socios = null;
		Socio socio = null;
		if(personaNatural != null)
			socios = personaNatural.getSocios();
		if(personaJuridica != null)
			socios = personaJuridica.getSocios();
		for (Socio s : socios) {
			if(s.getEstado())
				socio = s;
		}				
		
		//verificar titulares				
		Set<Titular> listTitulres = new HashSet<>();
		for (BigInteger id : titulares) {
			PersonaNatural persona = personaNaturalDAO.find(id);
			if(persona == null){
				throw new RollbackFailureException("Titular no encontrado");
			} else {
				Titular titular = new Titular();
				titular.setCuentaBancaria(null);
				titular.setEstado(true);
				titular.setPersonaNatural(persona);
				titular.setFechaInicio(calendar.getTime());
				titular.setFechaFin(null);	
				listTitulres.add(titular);
			}							
		}
		
		//crear socio sin cuenta de aportes si no existe
		if(socio == null){
			socio = new Socio();
			socio.setApoderado(null);
			socio.setCuentaAporte(null);
			socio.setCuentaBancarias(null);
			socio.setEstado(true);
			socio.setFechaInicio(calendar.getTime());
			socio.setFechaFin(null);
			socio.setPersonaJuridica(personaJuridica);
			socio.setPersonaNatural(personaNatural);
			socioDAO.create(socio);
		}		
		
		//crear cuenta bancaria
		CuentaBancaria cuentaBancaria = new CuentaBancaria();
		cuentaBancaria.setNumeroCuenta(agencia.getCodigo());
		cuentaBancaria.setBeneficiarios(null);
		cuentaBancaria.setCantidadRetirantes(cantRetirantes);
		cuentaBancaria.setEstado(EstadoCuentaBancaria.ACTIVO);
		cuentaBancaria.setFechaApertura(calendar.getTime());
		cuentaBancaria.setFechaCierre(null);
		cuentaBancaria.setMoneda(moneda);
		cuentaBancaria.setSaldo(BigDecimal.ZERO);
		cuentaBancaria.setSocio(socio);
		cuentaBancaria.setTipoCuentaBancaria(TipoCuentaBancaria.AHORRO);
		cuentaBancaria.setTitulars(null);
		cuentaBancariaDAO.create(cuentaBancaria);				
		//generar el numero de cuenta de cuenta
		String numeroCuenta = ProduceObject.getNumeroCuenta(cuentaBancaria);
		cuentaBancaria.setNumeroCuenta(numeroCuenta);
		cuentaBancariaDAO.update(cuentaBancaria);
		
		//crear titulares
		for (Titular titular : listTitulres) {
			titular.setCuentaBancaria(cuentaBancaria);
			titularDAO.create(titular);
		}
		for (Beneficiario beneficiario : beneficiarios) {
			beneficiario.setCuentaBancaria(cuentaBancaria);
			beneficiarioDAO.create(beneficiario);
		}
		
		//crear intereses
		BigDecimal tasaInteres = tasaInteresService.getTasaInteresCuentaAhorro(idMoneda);
		CuentaBancariaTasa cuentaBancariaTasa = new CuentaBancariaTasa();
		cuentaBancariaTasa.setCuentaBancaria(cuentaBancaria);
		cuentaBancariaTasa.setValor(tasaInteres);
		cuentaBancariaTasaDAO.create(cuentaBancariaTasa);
		
		return cuentaBancaria.getIdCuentaBancaria();
	}
	
	@Override
	public BigInteger createCuentaCorriente(BigInteger idMoneda, TipoPersona tipoPersona,
			BigInteger idPersona, int cantRetirantes,
			List<BigInteger> titulares, List<Beneficiario> beneficiarios)
			throws RollbackFailureException {
		
		PersonaNatural personaNatural = null;
		PersonaJuridica personaJuridica = null;
		
		switch (tipoPersona) {				
		case NATURAL:
			personaNatural = personaNaturalDAO.find(idPersona);
			break;
		case JURIDICA:
			personaJuridica = personaJuridicaDAO.find(idPersona);
			break;
		default:
			break;
		}
		
		Calendar calendar = Calendar.getInstance();
		Moneda moneda = monedaDAO.find(idMoneda);
		
		//verificar si existe el socio
		Set<Socio> socios = null;
		Socio socio = null;
		if(personaNatural != null)
			socios = personaNatural.getSocios();
		if(personaJuridica != null)
			socios = personaNatural.getSocios();
		for (Socio s : socios) {
			if(s.getEstado())
				socio = s;
		}				
		
		//verificar titulares				
		Set<Titular> listTitulres = new HashSet<>();
		for (BigInteger id : titulares) {
			PersonaNatural persona = personaNaturalDAO.find(id);
			if(persona == null){
				throw new RollbackFailureException("Titular no encontrado");
			} else {
				Titular titular = new Titular();
				titular.setCuentaBancaria(null);
				titular.setEstado(true);
				titular.setPersonaNatural(persona);
				titular.setFechaInicio(calendar.getTime());
				titular.setFechaFin(null);				
			}							
		}
		
		//crear socio sin cuenta de aportes si no existe
		if(socio == null){
			socio = new Socio();
			socio.setApoderado(null);
			socio.setCuentaAporte(null);
			socio.setCuentaBancarias(null);
			socio.setEstado(true);
			socio.setFechaInicio(calendar.getTime());
			socio.setFechaFin(null);
			socio.setPersonaJuridica(personaJuridica);
			socio.setPersonaNatural(personaNatural);
			socioDAO.create(socio);
		}		
		
		//crear cuenta bancaria
		CuentaBancaria cuentaBancaria = new CuentaBancaria();
		cuentaBancaria.setBeneficiarios(null);
		cuentaBancaria.setCantidadRetirantes(cantRetirantes);
		cuentaBancaria.setEstado(EstadoCuentaBancaria.ACTIVO);
		cuentaBancaria.setFechaApertura(calendar.getTime());
		cuentaBancaria.setFechaCierre(null);
		cuentaBancaria.setMoneda(moneda);
		cuentaBancaria.setSaldo(BigDecimal.ZERO);
		cuentaBancaria.setSocio(socio);
		cuentaBancaria.setTipoCuentaBancaria(TipoCuentaBancaria.CORRIENTE);
		cuentaBancaria.setTitulars(null);
		cuentaBancariaDAO.create(cuentaBancaria);
				
		for (Titular titular : listTitulres) {
			titular.setCuentaBancaria(cuentaBancaria);
			titularDAO.create(titular);
		}
		for (Beneficiario beneficiario : beneficiarios) {
			beneficiario.setCuentaBancaria(cuentaBancaria);
			beneficiarioDAO.create(beneficiario);
		}
		
		//crear intereses
		BigDecimal tasaInteres = tasaInteresService.getTasaInteresCuentaCorriente(idMoneda);
		CuentaBancariaTasa cuentaBancariaTasa = new CuentaBancariaTasa();
		cuentaBancariaTasa.setCuentaBancaria(cuentaBancaria);
		cuentaBancariaTasa.setValor(tasaInteres);
		cuentaBancariaTasaDAO.create(cuentaBancariaTasa);		
				
		return cuentaBancaria.getIdCuentaBancaria();
	}
	
	@Override
	public BigInteger createCuentaPlazoFijo(BigInteger idMoneda, TipoPersona tipoPersona,
			BigInteger idPersona, int cantRetirantes, BigDecimal monto, int periodo, BigDecimal tasaInteres,
			List<BigInteger> titulares, List<Beneficiario> beneficiarios)
			throws RollbackFailureException {
		
		PersonaNatural personaNatural = null;
		PersonaJuridica personaJuridica = null;
		
		switch (tipoPersona) {				
		case NATURAL:
			personaNatural = personaNaturalDAO.find(idPersona);
			break;
		case JURIDICA:
			personaJuridica = personaJuridicaDAO.find(idPersona);
			break;
		default:
			break;
		}
		
		Calendar calendar = Calendar.getInstance();
		Moneda moneda = monedaDAO.find(idMoneda);
		
		//verificar si existe el socio
		Set<Socio> socios = null;
		Socio socio = null;
		if(personaNatural != null)
			socios = personaNatural.getSocios();
		if(personaJuridica != null)
			socios = personaNatural.getSocios();
		for (Socio s : socios) {
			if(s.getEstado())
				socio = s;
		}				
		
		//verificar titulares				
		Set<Titular> listTitulres = new HashSet<>();
		for (BigInteger id : titulares) {
			PersonaNatural persona = personaNaturalDAO.find(id);
			if(persona == null){
				throw new RollbackFailureException("Titular no encontrado");
			} else {
				Titular titular = new Titular();
				titular.setCuentaBancaria(null);
				titular.setEstado(true);
				titular.setPersonaNatural(persona);
				titular.setFechaInicio(calendar.getTime());
				titular.setFechaFin(null);				
			}							
		}
		
		//crear socio sin cuenta de aportes si no existe
		if(socio == null){
			socio = new Socio();
			socio.setApoderado(null);
			socio.setCuentaAporte(null);
			socio.setCuentaBancarias(null);
			socio.setEstado(true);
			socio.setFechaInicio(calendar.getTime());
			socio.setFechaFin(null);
			socio.setPersonaJuridica(personaJuridica);
			socio.setPersonaNatural(personaNatural);
			socioDAO.create(socio);
		}		
		
		//crear cuenta bancaria
		CuentaBancaria cuentaBancaria = new CuentaBancaria();
		cuentaBancaria.setBeneficiarios(null);
		cuentaBancaria.setCantidadRetirantes(cantRetirantes);
		cuentaBancaria.setEstado(EstadoCuentaBancaria.ACTIVO);
		cuentaBancaria.setFechaApertura(calendar.getTime());
		cuentaBancaria.setFechaCierre(null);
		cuentaBancaria.setMoneda(moneda);
		cuentaBancaria.setSaldo(monto);
		cuentaBancaria.setSocio(socio);
		cuentaBancaria.setTipoCuentaBancaria(TipoCuentaBancaria.PLAZO_FIJO);
		cuentaBancaria.setTitulars(null);
		cuentaBancariaDAO.create(cuentaBancaria);
				
		for (Titular titular : listTitulres) {
			titular.setCuentaBancaria(cuentaBancaria);
			titularDAO.create(titular);
		}
		for (Beneficiario beneficiario : beneficiarios) {
			beneficiario.setCuentaBancaria(cuentaBancaria);
			beneficiarioDAO.create(beneficiario);
		}
		
		//crear las tasas de interes
		
		
		//crear intereses
	
		
		//crear transaccion del deposito 
		/*Transaccioncuentabancaria transaccioncuentabancaria = new Transaccioncuentabancaria();
		transaccioncuentabancaria.setCuentabancaria(cuentabancaria);
		transaccioncuentabancaria.setEstado(true);
		transaccioncuentabancaria.setMonto(new Moneda(monto));
		transaccioncuentabancaria.setSaldodisponible(new Moneda(monto));
		transaccioncuentabancaria.setTipomoneda(cuentabancaria.getTipomoneda());
		transaccioncuentabancaria.setTipotransaccion(ProduceObject.getTipotransaccion(TipoTransaccionType.DEPOSITO));
		transaccionCajaServiceLocal.deposito(caja, cuentabancaria, transaccioncuentabancaria,null, usuario, transaccionmayorcuantia);
		*/
		return cuentaBancaria.getIdCuentaBancaria();
	}
	
	@Override
	protected DAO<Object, CuentaBancaria> getDAO() {
		// TODO Auto-generated method stub
		return null;
	}
	

	

}
