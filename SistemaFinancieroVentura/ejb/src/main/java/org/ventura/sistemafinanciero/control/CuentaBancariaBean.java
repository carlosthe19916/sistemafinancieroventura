package org.ventura.sistemafinanciero.control;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.EJBException;
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
import org.joda.time.Days;
import org.joda.time.LocalDate;
//import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ventura.sistemafinanciero.dao.DAO;
import org.ventura.sistemafinanciero.dao.QueryParameter;
import org.ventura.sistemafinanciero.entity.Agencia;
import org.ventura.sistemafinanciero.entity.Beneficiario;
import org.ventura.sistemafinanciero.entity.BovedaCaja;
import org.ventura.sistemafinanciero.entity.Caja;
import org.ventura.sistemafinanciero.entity.CuentaBancaria;
import org.ventura.sistemafinanciero.entity.CuentaBancariaInteresGenera;
import org.ventura.sistemafinanciero.entity.CuentaBancariaTasa;
import org.ventura.sistemafinanciero.entity.CuentaBancariaView;
import org.ventura.sistemafinanciero.entity.EstadocuentaBancariaView;
import org.ventura.sistemafinanciero.entity.Moneda;
import org.ventura.sistemafinanciero.entity.PersonaJuridica;
import org.ventura.sistemafinanciero.entity.PersonaNatural;
import org.ventura.sistemafinanciero.entity.Socio;
import org.ventura.sistemafinanciero.entity.TasaInteres;
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
	
	@Inject
	private DAO<Object, CuentaBancariaInteresGenera> cuentaBancariaInteresGeneraDAO;
	
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
	public List<CuentaBancaria> findAll() {
		List<CuentaBancaria> list = this.cuentaBancariaDAO.findAll();
		for (CuentaBancaria cuentaBancaria : list) {
			Moneda moneda = cuentaBancaria.getMoneda();
			Hibernate.initialize(moneda);
		}
		return list;
	}
	
	@Override
	public List<CuentaBancariaView> findAllView() {
		TipoCuentaBancaria[] tipoCuenta = EnumSet.allOf(TipoCuentaBancaria.class).toArray(new TipoCuentaBancaria[0]);
		TipoPersona[] persona = EnumSet.allOf(TipoPersona.class).toArray(new TipoPersona[0]);
		EstadoCuentaBancaria[] estadoCuenta = EnumSet.allOf(EstadoCuentaBancaria.class).toArray(new EstadoCuentaBancaria[0]);
		return findAllView(tipoCuenta, persona, estadoCuenta, null, null);
	}

	@Override
	public List<CuentaBancariaView> findAllView(TipoCuentaBancaria[] tipoCuenta) {
		TipoPersona[] persona = EnumSet.allOf(TipoPersona.class).toArray(new TipoPersona[0]);
		EstadoCuentaBancaria[] estadoCuenta = EnumSet.allOf(EstadoCuentaBancaria.class).toArray(new EstadoCuentaBancaria[0]);
		return findAllView(tipoCuenta, persona, estadoCuenta, null, null);
	}

	@Override
	public List<CuentaBancariaView> findAllView(TipoCuentaBancaria[] tipoCuenta, TipoPersona[] persona) {
		EstadoCuentaBancaria[] estadoCuenta = EnumSet.allOf(EstadoCuentaBancaria.class).toArray(new EstadoCuentaBancaria[0]);
		return findAllView(tipoCuenta, persona, estadoCuenta, null, null);
	}

	@Override
	public List<CuentaBancariaView> findAllView(TipoCuentaBancaria[] tipoCuenta, TipoPersona[] persona, EstadoCuentaBancaria[] estadoCuenta) {
		return findAllView(tipoCuenta, persona, estadoCuenta, null, null);
	}

	@Override
	public List<CuentaBancariaView> findAllView(TipoCuentaBancaria[] tipoCuenta, TipoPersona[] persona,EstadoCuentaBancaria[] estadoCuenta, BigInteger offset, BigInteger limit) {
		return findAllView(null, tipoCuenta, persona, estadoCuenta, offset, limit);
	}

	@Override
	public List<CuentaBancariaView> findAllView(String filterText) {
		TipoCuentaBancaria[] tipoCuenta = EnumSet.allOf(TipoCuentaBancaria.class).toArray(new TipoCuentaBancaria[0]);
		TipoPersona[] persona = EnumSet.allOf(TipoPersona.class).toArray(new TipoPersona[0]);
		EstadoCuentaBancaria[] estadoCuenta = EnumSet.allOf(EstadoCuentaBancaria.class).toArray(new EstadoCuentaBancaria[0]);
		return findAllView(filterText, tipoCuenta, persona, estadoCuenta, null, null);
	}

	@Override
	public List<CuentaBancariaView> findAllView(String filterText, TipoCuentaBancaria[] tipoCuenta) {
		TipoPersona[] persona = EnumSet.allOf(TipoPersona.class).toArray(new TipoPersona[0]);
		EstadoCuentaBancaria[] estadoCuenta = EnumSet.allOf(EstadoCuentaBancaria.class).toArray(new EstadoCuentaBancaria[0]);
		return findAllView(filterText, tipoCuenta, persona, estadoCuenta, null, null);
	}

	@Override
	public List<CuentaBancariaView> findAllView(String filterText,TipoCuentaBancaria[] tipoCuenta, TipoPersona[] persona) {		
		EstadoCuentaBancaria[] estadoCuenta = EnumSet.allOf(EstadoCuentaBancaria.class).toArray(new EstadoCuentaBancaria[0]);
		return findAllView(filterText, tipoCuenta, persona, estadoCuenta, null, null);
	}

	@Override
	public List<CuentaBancariaView> findAllView(String filterText,TipoCuentaBancaria[] tipoCuenta, TipoPersona[] persona,EstadoCuentaBancaria[] estadoCuenta) {
		return findAllView(filterText, tipoCuenta, persona, estadoCuenta, null, null);
	}

	@Override
	public List<CuentaBancariaView> findAllView(String filterText,TipoCuentaBancaria[] tipoCuenta, TipoPersona[] persona,EstadoCuentaBancaria[] estadoCuenta, BigInteger offset, BigInteger limit) {
		List<CuentaBancariaView> result = null;

		if(filterText == null)
			filterText = "";
		if (tipoCuenta == null)
			return new ArrayList<>();
		if (tipoCuenta.length == 0)
			return new ArrayList<>();
		if (persona == null)
			return new ArrayList<>();
		if (persona.length == 0)
			return new ArrayList<>();
		if (estadoCuenta == null)
			return new ArrayList<>();
		if (estadoCuenta.length == 0)
			return new ArrayList<>();

			
		ArrayList<TipoCuentaBancaria> uno = new ArrayList<>(Arrays.asList(tipoCuenta));
		ArrayList<TipoPersona> dos = new ArrayList<>(Arrays.asList(persona));
		ArrayList<EstadoCuentaBancaria> tres = new ArrayList<>(Arrays.asList(estadoCuenta));
		QueryParameter queryParameter = QueryParameter.with("filtertext", '%' + filterText.toUpperCase() + '%')
				.and("tipoCuenta", uno)
				.and("tipoPersona", dos)
				.and("tipoEstadoCuenta", tres);

		if(filterText == null)
			filterText = "";
		if(offset == null) {			
			offset = BigInteger.ZERO;			
		}
		offset = offset.abs();
		if(limit != null){
			limit = limit.abs();			
		}
		
		Integer offSetInteger = offset.intValue();
		Integer limitInteger = (limit != null ? limit.intValue() : null);
				
		result = cuentaBancariaViewDAO.findByNamedQuery(CuentaBancariaView.FindByFilterTextCuentaBancariaView,queryParameter.parameters(), offSetInteger, limitInteger);
		if (result != null)
			for (CuentaBancariaView cuentaBancariaView : result) {
				Moneda moneda = cuentaBancariaView.getMoneda();
				Hibernate.initialize(moneda);
			}
		return result;
	}

	@Override
	public BigInteger crearCuentaBancaria(TipoCuentaBancaria tipoCuentaBancaria, String codigoAgencia,
			BigInteger idMoneda, BigDecimal tasaInteres,
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
				
		QueryParameter queryParameter = QueryParameter.with("codigo", codigoAgencia);
		List<Agencia> listAgencias = agenciaDAO.findByNamedQuery(Agencia.findByCodigo, queryParameter.parameters());
		if(listAgencias.size() != 1)
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
		cuentaBancaria.setNumeroCuenta(codigoAgencia);
		cuentaBancaria.setBeneficiarios(null);
		cuentaBancaria.setCantidadRetirantes(cantRetirantes);
		cuentaBancaria.setEstado(EstadoCuentaBancaria.ACTIVO);
		cuentaBancaria.setFechaApertura(calendar.getTime());
		cuentaBancaria.setFechaCierre(null);
		cuentaBancaria.setMoneda(moneda);
		cuentaBancaria.setSaldo(BigDecimal.ZERO);
		cuentaBancaria.setSocio(socio);
		cuentaBancaria.setTipoCuentaBancaria(tipoCuentaBancaria);
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
		if(beneficiarios != null)
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
	public BigInteger[] crearCuentaBancariaPlazoFijoConDeposito(String codigo,
			BigInteger idMoneda, TipoPersona tipoPersona, BigInteger idPersona,
			int cantRetirantes, BigDecimal monto, int periodo,
			BigDecimal tasaInteres, List<BigInteger> titulares,
			List<Beneficiario> beneficiarios) throws RollbackFailureException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void congelarCuentaBancaria(BigInteger idCuentaBancaria)
			throws RollbackFailureException {
		CuentaBancaria cuentaBancaria = cuentaBancariaDAO.find(idCuentaBancaria);
		if(cuentaBancaria == null)
			throw new RollbackFailureException("Cuenta bancaria no encontrada");
		if(!cuentaBancaria.getEstado().equals(EstadoCuentaBancaria.ACTIVO))
			throw new RollbackFailureException("La cuenta no esta activa, no se puede congelar");
		cuentaBancaria.setEstado(EstadoCuentaBancaria.CONGELADO);
		cuentaBancariaDAO.update(cuentaBancaria);		
	}

	@Override
	public void descongelarCuentaBancaria(BigInteger idCuentaBancaria)
			throws RollbackFailureException {
		CuentaBancaria cuentaBancaria = cuentaBancariaDAO.find(idCuentaBancaria);
		if(cuentaBancaria == null)
			throw new RollbackFailureException("Cuenta bancaria no encontrada");
		if(!cuentaBancaria.getEstado().equals(EstadoCuentaBancaria.CONGELADO))
			throw new RollbackFailureException("La cuenta no esta congelada, no se puede descongelar");
		if(cuentaBancaria.getTipoCuentaBancaria().equals(TipoCuentaBancaria.PLAZO_FIJO)){
			Date fechaActual = Calendar.getInstance().getTime();
			Date fechaCierre = cuentaBancaria.getFechaCierre();
			if(fechaActual.compareTo(fechaCierre) != 1)
				throw new RollbackFailureException("Cuenta PLAZO_FIJO, no vencio aun, no se puede descongelar");
		}
		cuentaBancaria.setEstado(EstadoCuentaBancaria.ACTIVO);
		cuentaBancariaDAO.update(cuentaBancaria);
	}

	@Override
	public void recalcularCuentaPlazoFijo(BigInteger idCuenta, int periodo,
			BigDecimal tasaInteres) throws RollbackFailureException {
		CuentaBancaria cuentaBancaria = cuentaBancariaDAO.find(idCuenta);
		if(cuentaBancaria == null)
			throw new RollbackFailureException("Cuenta no encontrada");
		if(!cuentaBancaria.getTipoCuentaBancaria().equals(TipoCuentaBancaria.PLAZO_FIJO))
			throw new RollbackFailureException("Solo las cuentas PLAZO_FIJO pueden ser recalculadas.");
		if(cuentaBancaria.getEstado().equals(EstadoCuentaBancaria.INACTIVO))
			throw new RollbackFailureException("Cuenta inactiva, no se puede recalcular");
		
		LocalDate inicio = new LocalDate(cuentaBancaria.getFechaApertura());
		LocalDate fin = inicio.plusDays(periodo);
		
		Date fechaCierre = fin.toDateTimeAtStartOfDay().toDate();
		
		//actualizando cuenta
		cuentaBancaria.setFechaCierre(fechaCierre);		
		
		Set<CuentaBancariaTasa> tasas = cuentaBancaria.getCuentaBancariaTasas();
		CuentaBancariaTasa tasaInteresCuentaBancaria = null;
		for (CuentaBancariaTasa cuentaBancariaTasa : tasas) {
			tasaInteresCuentaBancaria = cuentaBancariaTasa;
		}
		if(tasaInteresCuentaBancaria == null)
			throw new RollbackFailureException("Tasa de interes no encontrada");
		tasaInteresCuentaBancaria.setValor(tasaInteres);
		
		
		cuentaBancariaDAO.update(cuentaBancaria);
		cuentaBancariaTasaDAO.update(tasaInteresCuentaBancaria);
		
		if(fechaCierre.compareTo(Calendar.getInstance().getTime()) == -1){
			descongelarCuentaBancaria(idCuenta);
		} else {
			congelarCuentaBancaria(idCuenta);
		}
	}

	@Override
	public BigInteger[] renovarCuentaPlazoFijo(BigInteger idCuenta,
			int periodo, BigDecimal tasaInteres)
			throws RollbackFailureException {
		
		CuentaBancaria cuentaBancariaOld = cuentaBancariaDAO.find(idCuenta);
		if(cuentaBancariaOld == null)
			throw new RollbackFailureException("Cuenta bancaria no encontrada");
		if(!cuentaBancariaOld.getTipoCuentaBancaria().equals(TipoCuentaBancaria.PLAZO_FIJO))
			throw new RollbackFailureException("Solo las cuentas a plazo fijo pueden ser renovadas");
		if(!cuentaBancariaOld.getEstado().equals(EstadoCuentaBancaria.ACTIVO))
			throw new RollbackFailureException("Cuenta bancaria no activa, no se puede renovar.");
		
		Calendar calendar = Calendar.getInstance();
		Date fechaActual = calendar.getTime();
		
		if(fechaActual.compareTo(cuentaBancariaOld.getFechaCierre()) != 1)
			throw new RollbackFailureException("Cuenta aun no vence, no se puede renovar");
				
		//obtener datos		
		Moneda moneda = cuentaBancariaOld.getMoneda();					
		Set<Titular> titularesOld = cuentaBancariaOld.getTitulars();
		Set<Beneficiario> beneficiariosOld = cuentaBancariaOld.getBeneficiarios();		
		Socio socio = cuentaBancariaOld.getSocio();
		PersonaNatural personaNaturalSocio = socio.getPersonaNatural();
		PersonaJuridica personaJuridicaSocio = socio.getPersonaJuridica();
		if(personaNaturalSocio == null && personaJuridicaSocio == null)
			throw new RollbackFailureException("Socio no tiene una persona asociada");
		if(personaNaturalSocio != null && personaJuridicaSocio != null)
			throw new RollbackFailureException("Socio tiene persona natural y juridica asociada");
		
		List<BigInteger> listaTitulares = new ArrayList<>();
		List<Beneficiario> listaBeneficiarios = new ArrayList<>();
		
		for (Titular titularOld : titularesOld) {
			PersonaNatural personaOld = titularOld.getPersonaNatural();
			BigInteger idPersonaOld = personaOld.getIdPersonaNatural();
			listaTitulares.add(idPersonaOld);
		}
		for (Beneficiario beneficiarioOld : beneficiariosOld) {
			Beneficiario beneficiarioNew = new Beneficiario();
			beneficiarioNew.setIdBeneficiario(null);
			beneficiarioNew.setApellidoPaterno(beneficiarioOld.getApellidoPaterno());
			beneficiarioNew.setApellidoMaterno(beneficiarioOld.getApellidoMaterno());
			beneficiarioNew.setNombres(beneficiarioOld.getNombres());
			beneficiarioNew.setNumeroDocumento(beneficiarioOld.getNumeroDocumento());
			beneficiarioNew.setPorcentajeBeneficio(beneficiarioOld.getPorcentajeBeneficio());
			
			listaBeneficiarios.add(beneficiarioNew);
		}
		
		//recuperar datos
		String codigoAgencia = ProduceObject.getCodigoAgenciaFromNumeroCuenta(cuentaBancariaOld.getNumeroCuenta());
		BigInteger idMoneda = moneda.getIdMoneda();
		TipoPersona tipoPersona = (personaNaturalSocio != null ? TipoPersona.NATURAL : TipoPersona.JURIDICA);
		BigInteger idPersona = (personaNaturalSocio != null ? personaNaturalSocio.getIdPersonaNatural() : personaJuridicaSocio.getIdPersonaJuridica());
		int cantRetirantes = cuentaBancariaOld.getCantidadRetirantes();
						
		//capitalizar cuenta
		capitalizarCuenta(cuentaBancariaOld.getIdCuentaBancaria());
		
		//crear cuenta nueva
		BigInteger idCuentaBancariaNew = crearCuentaBancaria(TipoCuentaBancaria.PLAZO_FIJO, codigoAgencia, idMoneda, tasaInteres, tipoPersona, idPersona, cantRetirantes, listaTitulares, listaBeneficiarios);
		CuentaBancaria cuentaBancariaNew = cuentaBancariaDAO.find(idCuentaBancariaNew);
		
		//crear transferencia		
		String numeroCuentaOld = cuentaBancariaOld.getNumeroCuenta();
		String numeroCuentaNew = cuentaBancariaNew.getNumeroCuenta(); 
		BigDecimal montoTransferencia = cuentaBancariaOld.getSaldo();
		String referencia = "TRANSFERENCIA POR RENOVACION DE CUENTA";		
		BigInteger idTransferencia = cajaSessionService.crearTransferenciaBancaria(numeroCuentaOld, numeroCuentaNew, montoTransferencia, referencia);
		
		//cancelar cuenta antigua
		BigInteger idCuentaBancariaOld = cuentaBancariaOld.getIdCuentaBancaria();
		cancelarCuentaBancaria(idCuentaBancariaOld);													
		
		return new BigInteger[]{idCuentaBancariaNew, idTransferencia};
	}

	
	private void capitalizarCuenta(BigInteger idCuentaBancaria) throws RollbackFailureException {
		CuentaBancaria cuentaBancaria = cuentaBancariaDAO.find(idCuentaBancaria);
		if(cuentaBancaria == null)
			throw new RollbackFailureException("Cuenta bancaria no encontrada");
		if(!cuentaBancaria.getEstado().equals(EstadoCuentaBancaria.ACTIVO))
			throw new RollbackFailureException("La cuenta no esta activa, no se puede cancelar");

		TipoCuentaBancaria tipoCuentaBancaria = cuentaBancaria.getTipoCuentaBancaria();
		BigDecimal capital = cuentaBancaria.getSaldo();
		BigDecimal tasaInteres = null;		
				
		Set<CuentaBancariaTasa> tasasList = cuentaBancaria.getCuentaBancariaTasas();
		if(tasasList.size() != 1)
			throw new RollbackFailureException("Tasa de interes no encontrada, no se puede capitalizar");
		for (CuentaBancariaTasa tasa : tasasList) {			
			tasaInteres = tasa.getValor();
		}
		
		switch (tipoCuentaBancaria) {
		case AHORRO:
				CuentaBancariaTasa cuentaBancariaTasa = null;
				Set<CuentaBancariaTasa> tasasCuentaBancaria = cuentaBancaria.getCuentaBancariaTasas();
				if(tasasCuentaBancaria.size() != 1)
					throw new RollbackFailureException("No se encontró tasas de interes para la cuenta bancaria");
				for (CuentaBancariaTasa tasa : tasasCuentaBancaria) {
					cuentaBancariaTasa = tasa;
				}
				
				throw new RollbackFailureException("No se implemento el metodo de capitalizacion para cuenta ahorro");
			//break;
		case CORRIENTE:
				throw new RollbackFailureException("No se implemento el metodo de capitalizacion para cuenta corriente");
			//break;
		case PLAZO_FIJO:	
			Date fechaApertura = cuentaBancaria.getFechaApertura();
			Date fechaCierre = cuentaBancaria.getFechaCierre();			
			LocalDate localDateApertura = new LocalDate(fechaApertura);
			LocalDate localDateCierre = new LocalDate(fechaCierre);
			Days days = Days.daysBetween(localDateApertura, localDateCierre);
			
			//calcular nuevo saldo
			int periodo = days.getDays();						
			BigDecimal interesGenerado = ProduceObject.getInteresPlazoFijo(capital, tasaInteres, periodo);
			cuentaBancaria.setSaldo(interesGenerado.add(capital));
			
			//crear tupla en interes generado
			CuentaBancariaInteresGenera bancariaInteresGenera = new CuentaBancariaInteresGenera();
			bancariaInteresGenera.setIdCuentaBancariaInteresGen(null);
			bancariaInteresGenera.setCuentaBancaria(cuentaBancaria);			
			bancariaInteresGenera.setFecha(Calendar.getInstance().getTime());
			bancariaInteresGenera.setCapital(capital);
			bancariaInteresGenera.setInteresGenerado(interesGenerado);
			
			cuentaBancariaDAO.update(cuentaBancaria);
			cuentaBancariaInteresGeneraDAO.create(bancariaInteresGenera);
			break;
		default:
			throw new RollbackFailureException("Tipo de cuenta bancaria no identificado");
		}
	}

	@Override
	public void cancelarCuentaBancaria(BigInteger id)
			throws RollbackFailureException {
		CuentaBancaria cuentaBancaria = cuentaBancariaDAO.find(id);
		if(cuentaBancaria == null)
			throw new RollbackFailureException("Cuenta bancaria no encontrada");
		if(!cuentaBancaria.getEstado().equals(EstadoCuentaBancaria.ACTIVO))
			throw new RollbackFailureException("La cuenta no esta activa, no se puede cancelar");
		if(cuentaBancaria.getSaldo().compareTo(BigDecimal.ZERO) != 0)
			throw new RollbackFailureException("Cuenta tiene saldo diferente de cero, no se puede cancelar");
		
		if(cuentaBancaria.getTipoCuentaBancaria().equals(TipoCuentaBancaria.PLAZO_FIJO)){
			if(cuentaBancaria.getFechaCierre().compareTo(Calendar.getInstance().getTime()) == 1)
				throw new RollbackFailureException("La cuenta PLAZO_FIJO tiene fecha de cierre aun no vencida");
		} else {			
			cuentaBancaria.setFechaCierre(Calendar.getInstance().getTime());	
		}		
		cuentaBancaria.setEstado(EstadoCuentaBancaria.INACTIVO);
		cuentaBancariaDAO.update(cuentaBancaria);
	}

	@Override
	public BigInteger cancelarCuentaBancariaConRetiro(BigInteger idCuentaBancaria)
			throws RollbackFailureException {
		CuentaBancaria cuentaBancaria = cuentaBancariaDAO.find(idCuentaBancaria);
		if(cuentaBancaria == null)
			throw new RollbackFailureException("Cuenta bancaria no encontrada");
		if(!cuentaBancaria.getEstado().equals(EstadoCuentaBancaria.ACTIVO))
			throw new RollbackFailureException("La cuenta no esta activa, no se puede cancelar");
		
		capitalizarCuenta(idCuentaBancaria);
		
		String numeroCuenta = cuentaBancaria.getNumeroCuenta();
		BigDecimal monto = cuentaBancaria.getSaldo();
		String referencia = "RETIRO POR CANCELACION DE CUENTA";
		
		BigInteger idTransaccion = cajaSessionService.crearRetiroBancario(numeroCuenta, monto, referencia);		
		cancelarCuentaBancaria(idCuentaBancaria);
		return idTransaccion;
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
		personaNatural = personaNaturalService.find(personaNatural.getTipoDocumento().getIdTipoDocumento(), personaNatural.getNumeroDocumento());
		
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
	public VoucherTransaccionBancaria getVoucherCuentaBancaria(
			BigInteger idTransaccionBancaria) {
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
	public List<EstadocuentaBancariaView> getEstadoCuenta(BigInteger idCuenta,
			Date dateDesde, Date dateHasta) {
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

	@Override
	public CuentaBancariaView find(String numeroCuenta) {
		QueryParameter queryParameter = QueryParameter.with("numeroCuenta", numeroCuenta);
		List<CuentaBancariaView> list = cuentaBancariaViewDAO.findByNamedQuery(CuentaBancariaView.findByNumeroCuenta, queryParameter.parameters());
		if(list.size()>1)
			throw new EJBException("Mas de una cuenta con el numero de cuenta");
		else
			for (CuentaBancariaView cuentaBancaria : list) {
				Moneda moneda = cuentaBancaria.getMoneda();
				Hibernate.initialize(moneda);
				return cuentaBancaria;
			}
		return null;
	}

	@Override
	protected DAO<Object, CuentaBancaria> getDAO() {
		return this.cuentaBancariaDAO;
	}

	

}
