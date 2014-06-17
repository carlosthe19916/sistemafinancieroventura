package org.ventura.sistemafinanciero.control;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.hibernate.Hibernate;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ventura.sistemafinanciero.dao.DAO;
import org.ventura.sistemafinanciero.dao.QueryParameter;
import org.ventura.sistemafinanciero.entity.Agencia;
import org.ventura.sistemafinanciero.entity.Beneficiario;
import org.ventura.sistemafinanciero.entity.BovedaCaja;
import org.ventura.sistemafinanciero.entity.Caja;
import org.ventura.sistemafinanciero.entity.CuentaBancaria;
import org.ventura.sistemafinanciero.entity.CuentaBancariaTasa;
import org.ventura.sistemafinanciero.entity.CuentaBancariaView;
import org.ventura.sistemafinanciero.entity.EstadocuentaBancariaView;
import org.ventura.sistemafinanciero.entity.Moneda;
import org.ventura.sistemafinanciero.entity.PersonaJuridica;
import org.ventura.sistemafinanciero.entity.PersonaNatural;
import org.ventura.sistemafinanciero.entity.Socio;
import org.ventura.sistemafinanciero.entity.TipoDocumento;
import org.ventura.sistemafinanciero.entity.Titular;
import org.ventura.sistemafinanciero.entity.TransaccionBancaria;
import org.ventura.sistemafinanciero.entity.dto.VoucherTransaccionBancaria;
import org.ventura.sistemafinanciero.entity.type.EstadoCuentaBancaria;
import org.ventura.sistemafinanciero.entity.type.TipoCuentaBancaria;
import org.ventura.sistemafinanciero.entity.type.TipoPersona;
import org.ventura.sistemafinanciero.exception.RollbackFailureException;
import org.ventura.sistemafinanciero.service.CajaSessionService;
import org.ventura.sistemafinanciero.service.CuentaBancariaService;
import org.ventura.sistemafinanciero.service.PersonaNaturalService;
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
	private DAO<Object, CuentaBancariaView> cuentaBancariaViewDAO;
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
	private DAO<Object, TransaccionBancaria> transaccionBancariaDAO;
	@Inject
	private DAO<Object, Agencia> agenciaDAO;
	@Inject
	private DAO<Object, EstadocuentaBancariaView> estadocuentaBancariaViewDAO;
	
	@EJB
	private TasaInteresService tasaInteresService;
	@EJB
	private CajaSessionService cajaSessionService;
	@EJB
	private PersonaNaturalService personaNaturalService;
	
	@Inject
    private Validator validator;
	
	@Override
	public CuentaBancaria findById(BigInteger idCuenta){
		CuentaBancaria cuentaBancaria = cuentaBancariaDAO.find(idCuenta);
		if(cuentaBancaria != null){
			Moneda moneda = cuentaBancaria.getMoneda();
			Hibernate.initialize(moneda);
		}
		return cuentaBancaria;
	}
	
	@Override
	public Set<CuentaBancaria> findByFilterText(String filterText) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<CuentaBancaria> findAll() {
		List<CuentaBancaria> list = this.cuentaBancariaDAO.findAll();
		for (CuentaBancaria cuentaBancaria : list) {
			Moneda moneda = cuentaBancaria.getMoneda();
			Hibernate.initialize(moneda);
		}
		return list;
	}
	
	@Override
	public Set<Titular> getTitulares(BigInteger idCuentaBancaria, boolean mode) {
		CuentaBancaria cuenta = cuentaBancariaDAO.find(idCuentaBancaria);
		if(cuenta == null)
			return null;
		Set<Titular> result = cuenta.getTitulars();
		for (Titular titular : result) {
			PersonaNatural persona = titular.getPersonaNatural();
			TipoDocumento documento = persona.getTipoDocumento();
			Hibernate.initialize(titular);	
			Hibernate.initialize(persona);	
			Hibernate.initialize(documento);	
		}
		return result;
	}

	@Override
	public Set<Beneficiario> getBeneficiarios(BigInteger idCuentaBancaria) {
		CuentaBancaria cuenta = cuentaBancariaDAO.find(idCuentaBancaria);
		if(cuenta == null)
			return null;
		Set<Beneficiario> result = cuenta.getBeneficiarios();
		Hibernate.initialize(result);
		return result;
	}	
	
	@Override
	public BigInteger createCuentaAhorro(BigInteger idAgencia, BigInteger idMoneda, BigDecimal tasaInteres,
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
		if(tasaInteres == null)
			tasaInteres = tasaInteresService.getTasaInteresCuentaAhorro(idMoneda);
		CuentaBancariaTasa cuentaBancariaTasa = new CuentaBancariaTasa();
		cuentaBancariaTasa.setCuentaBancaria(cuentaBancaria);
		cuentaBancariaTasa.setValor(tasaInteres);
		cuentaBancariaTasaDAO.create(cuentaBancariaTasa);
		
		return cuentaBancaria.getIdCuentaBancaria();
	}
	
	@Override
	public BigInteger createCuentaCorriente(BigInteger idAgencia, BigInteger idMoneda, BigDecimal tasaInteres, TipoPersona tipoPersona,
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
		cuentaBancaria.setTipoCuentaBancaria(TipoCuentaBancaria.CORRIENTE);
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
		if(tasaInteres == null)
			tasaInteres = tasaInteresService.getTasaInteresCuentaCorriente(idMoneda);
		CuentaBancariaTasa cuentaBancariaTasa = new CuentaBancariaTasa();
		cuentaBancariaTasa.setCuentaBancaria(cuentaBancaria);
		cuentaBancariaTasa.setValor(tasaInteres);
		cuentaBancariaTasaDAO.create(cuentaBancariaTasa);
		
		return cuentaBancaria.getIdCuentaBancaria();
	}
	
	@Override
	public BigInteger createCuentaPlazoFijo(BigInteger idAgencia, BigInteger idMoneda, TipoPersona tipoPersona,
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
		Set<Titular> listTitulares = new HashSet<>();
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
				listTitulares.add(titular);
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
		Date date = calendar.getTime();
		LocalDate inicio = new LocalDate(date.getTime());
		LocalDate fin = inicio.plusDays(periodo);
		
		CuentaBancaria cuentaBancaria = new CuentaBancaria();
		cuentaBancaria.setNumeroCuenta(agencia.getCodigo());
		cuentaBancaria.setBeneficiarios(null);
		cuentaBancaria.setCantidadRetirantes(cantRetirantes);
		cuentaBancaria.setEstado(EstadoCuentaBancaria.ACTIVO);
		cuentaBancaria.setFechaApertura(inicio.toDateTimeAtStartOfDay().toDate());
		cuentaBancaria.setFechaCierre(fin.toDateTimeAtStartOfDay().toDate());
		cuentaBancaria.setMoneda(moneda);
		cuentaBancaria.setSaldo(BigDecimal.ZERO);
		cuentaBancaria.setSocio(socio);
		cuentaBancaria.setTipoCuentaBancaria(TipoCuentaBancaria.PLAZO_FIJO);
		cuentaBancaria.setTitulars(null);
		cuentaBancariaDAO.create(cuentaBancaria);
		//generar el numero de cuenta de cuenta
		String numeroCuenta = ProduceObject.getNumeroCuenta(cuentaBancaria);
		cuentaBancaria.setNumeroCuenta(numeroCuenta);
		cuentaBancariaDAO.update(cuentaBancaria);
		
		for (Titular titular : listTitulares) {
			titular.setCuentaBancaria(cuentaBancaria);
			titularDAO.create(titular);
		}
		for (Beneficiario beneficiario : beneficiarios) {
			beneficiario.setCuentaBancaria(cuentaBancaria);
			beneficiarioDAO.create(beneficiario);
		}
		
		//crear las tasas de interes
		//crear intereses		
		CuentaBancariaTasa cuentaBancariaTasa = new CuentaBancariaTasa();
		cuentaBancariaTasa.setCuentaBancaria(cuentaBancaria);
		cuentaBancariaTasa.setValor(tasaInteres);
		cuentaBancariaTasaDAO.create(cuentaBancariaTasa);
		
		//crear transaccion bancaria
		cajaSessionService.crearDepositoBancario(cuentaBancaria.getNumeroCuenta(), monto, "APERTURA DE CUENTA A PLAZO FIJO");
		
		return cuentaBancaria.getIdCuentaBancaria();
						
	}
	
	@Override
	protected DAO<Object, CuentaBancaria> getDAO() {
		return this.cuentaBancariaDAO;
	}
	
	@Override
	public Set<CuentaBancariaView> findAllView() {
		List<CuentaBancariaView> list = cuentaBancariaViewDAO.findAll();
		for (CuentaBancariaView cuentaBancariaView : list) {
			Moneda moneda = cuentaBancariaView.getMoneda();
			Hibernate.initialize(moneda);
		}
		return new HashSet<>(list);
	}
	
	@Override
	public List<CuentaBancariaView> findAllView(
			List<TipoPersona> tipoPersonaList,
			List<TipoCuentaBancaria> tipoCuentaList,
			List<EstadoCuentaBancaria> estadoCuentaList, List<Moneda> monedaList) {
		if(tipoPersonaList == null) {
			tipoPersonaList = new ArrayList<>();
		}
			
		if(tipoCuentaList == null){
			tipoCuentaList = new ArrayList<>();
		}
		if(estadoCuentaList == null) {
			estadoCuentaList = new ArrayList<>();
		}			
		if(monedaList == null) {
			monedaList = new ArrayList<>();
		}
						
						
		QueryParameter queryParameter = QueryParameter.with("listTipoPersona", tipoPersonaList).
				and("listTipoCuenta", tipoCuentaList).
				and("listEstadoCuenta", estadoCuentaList);//.and("listMoneda", monedaList);
		
		List<CuentaBancariaView> list = cuentaBancariaViewDAO.findByNamedQuery(CuentaBancariaView.FindByLists, queryParameter.parameters(), 200);
		for (CuentaBancariaView cuentaBancariaView : list) {
			Moneda moneda = cuentaBancariaView.getMoneda();
			Hibernate.initialize(moneda);
		}
		return list;
	}

	@Override
	public Set<CuentaBancariaView> findByFilterTextView(String filterText) {
		if (filterText == null)
			return new HashSet<CuentaBancariaView>();
		if (filterText.isEmpty() || filterText.trim().isEmpty()) {
			return new HashSet<CuentaBancariaView>();
		}
		List<CuentaBancariaView> list = null;
		QueryParameter queryParameter = QueryParameter.with("filtertext", '%' + filterText.toUpperCase() + '%');
		list = cuentaBancariaViewDAO.findByNamedQuery(CuentaBancariaView.FindByFilterTextCuentaBancariaView, queryParameter.parameters());
		for (CuentaBancariaView cuentaBancariaView : list) {
			Moneda moneda = cuentaBancariaView.getMoneda();
			Hibernate.initialize(moneda);
		}
		return new HashSet<CuentaBancariaView>(list);
	}

	@Override
	public BigInteger addBeneficiario(BigInteger id, Beneficiario beneficiario)
			throws RollbackFailureException {
		CuentaBancaria cuentaBancaria = cuentaBancariaDAO.find(id);
		if(cuentaBancaria == null)
			throw new RollbackFailureException("Cuenta bancaria no encotrada");
		beneficiario.setIdBeneficiario(null);
		beneficiario.setCuentaBancaria(cuentaBancaria);
		
		//validar beneficiario
		Set<ConstraintViolation<Beneficiario>> violations = validator.validate(beneficiario);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}
		
		beneficiarioDAO.create(beneficiario);
		return beneficiario.getIdBeneficiario();
	}

	@Override
	public BigInteger addTitular(BigInteger idCuenta, Titular titular)
			throws RollbackFailureException {
		CuentaBancaria cuentaBancaria = cuentaBancariaDAO.find(idCuenta);
		if(cuentaBancaria == null)
			throw new RollbackFailureException("Cuenta bancaria no encotrada");		
		
		PersonaNatural personaNatural = titular.getPersonaNatural();
		personaNatural = personaNaturalService.findByTipoNumeroDocumento(personaNatural.getTipoDocumento().getIdTipoDocumento(), personaNatural.getNumeroDocumento());
		
		Set<Titular> titulresDB = cuentaBancaria.getTitulars();
		for (Titular titDB : titulresDB) {
			if(titDB.getPersonaNatural().equals(personaNatural))
				if(titDB.getEstado())
					throw new RollbackFailureException("Titular ya existente");
		}
				
		if(personaNatural == null)
			throw new RollbackFailureException("Persona para titular no encontrado");
		
		titular.setPersonaNatural(personaNatural);
		titular.setIdTitular(null);
		titular.setCuentaBancaria(cuentaBancaria);
		titular.setEstado(true);
		titular.setFechaFin(null);
		titular.setFechaInicio(Calendar.getInstance().getTime());
		
		//validar beneficiario
		Set<ConstraintViolation<Titular>> violations = validator.validate(titular);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}

		titularDAO.create(titular);
		return titular.getIdTitular();
	}
	
	@Override
	public VoucherTransaccionBancaria getVoucherCuentaBancaria(BigInteger idTransaccionBancaria) {
		VoucherTransaccionBancaria voucherTransaccion = new VoucherTransaccionBancaria();
		
		// recuperando transaccion
		TransaccionBancaria transaccionBancaria = transaccionBancariaDAO.find(idTransaccionBancaria);
		CuentaBancaria cuentaBancaria = transaccionBancaria.getCuentaBancaria();
		Socio socio = cuentaBancaria.getSocio();
		Caja caja = transaccionBancaria.getHistorialCaja().getCaja();
		Set<BovedaCaja> list = caja.getBovedaCajas();
		Agencia agencia = null;
		for (BovedaCaja bovedaCaja : list) {
			agencia = bovedaCaja.getBoveda().getAgencia();
			break;
		}
		
		//Poniendo datos de transaccion
		voucherTransaccion.setIdTransaccionBancaria(transaccionBancaria.getIdTransaccionBancaria());
		Moneda moneda = transaccionBancaria.getMoneda();
		Hibernate.initialize(moneda);
		voucherTransaccion.setMoneda(moneda);
		
		voucherTransaccion.setFecha(transaccionBancaria.getFecha());
		voucherTransaccion.setHora(transaccionBancaria.getHora());
		voucherTransaccion.setNumeroOperacion(transaccionBancaria.getNumeroOperacion());
		voucherTransaccion.setMonto(transaccionBancaria.getMonto());
		voucherTransaccion.setReferencia(transaccionBancaria.getReferencia());
		voucherTransaccion.setTipoTransaccion(transaccionBancaria.getTipoTransaccion());				
		voucherTransaccion.setObservacion(transaccionBancaria.getObservacion());
		
		//Poniendo datos de cuenta bancaria
		voucherTransaccion.setTipoCuentaBancaria(cuentaBancaria.getTipoCuentaBancaria());				
		voucherTransaccion.setNumeroCuenta(cuentaBancaria.getNumeroCuenta());
		voucherTransaccion.setSaldoDisponible(cuentaBancaria.getSaldo());
		
		//Poniendo datos de agencia
		voucherTransaccion.setAgenciaDenominacion(agencia.getDenominacion());
		voucherTransaccion.setAgenciaAbreviatura(agencia.getAbreviatura());
		
		//Poniendo datos de caja
		voucherTransaccion.setCajaDenominacion(caja.getDenominacion());
		voucherTransaccion.setCajaAbreviatura(caja.getAbreviatura());
		
		//Poniendo datos del socio
		PersonaNatural personaNatural = socio.getPersonaNatural();
		PersonaJuridica personaJuridica = socio.getPersonaJuridica();
		if (personaJuridica == null) {
			voucherTransaccion.setIdSocio(socio.getIdSocio());
			voucherTransaccion.setTipoDocumento(socio.getPersonaNatural().getTipoDocumento());			//
			voucherTransaccion.setNumeroDocumento(socio.getPersonaNatural().getNumeroDocumento());
			voucherTransaccion.setSocio(personaNatural.getApellidoPaterno() + " " + personaNatural.getApellidoMaterno() + ", " + personaNatural.getNombres());
		}
		if (personaNatural == null) {
			voucherTransaccion.setIdSocio(socio.getIdSocio());
			voucherTransaccion.setTipoDocumento(socio.getPersonaJuridica().getTipoDocumento());			//
			voucherTransaccion.setNumeroDocumento(socio.getPersonaJuridica().getNumeroDocumento());
			voucherTransaccion.setSocio(personaJuridica.getRazonSocial());
		}
		return voucherTransaccion;
	}

	@Override
	public List<EstadocuentaBancariaView> getEstadoCuenta(BigInteger idCuenta, Date dateDesde,
			Date dateHasta) {
		CuentaBancaria cuenta = cuentaBancariaDAO.find(idCuenta);
		if(cuenta == null)
			return null;
		
		Date desdeQuery = null;
		Date hastaQuery = null;
		
		if(dateDesde == null || dateHasta == null){
			Calendar calendar = Calendar.getInstance();
			LocalDate localDateHasta = new LocalDate(calendar.getTime());			
			LocalDate localDateDesde = localDateHasta.minusDays(30);
			
			desdeQuery = localDateDesde.toDateTimeAtStartOfDay().toDate();
			hastaQuery = localDateHasta.toDateTimeAtStartOfDay().toDate();	
		} else {
			desdeQuery = dateDesde;
			hastaQuery = dateHasta;
		}
		
		QueryParameter queryParameter = QueryParameter.with("numeroCuenta", cuenta.getNumeroCuenta())
				.and("desde", desdeQuery)
				.and("hasta", hastaQuery);
		List<EstadocuentaBancariaView> list = estadocuentaBancariaViewDAO.findByNamedQuery(EstadocuentaBancariaView.findByNumeroCuentaAndDesdeHasta, 
				queryParameter.parameters());
		
		return list;
	}


}
