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
import org.ventura.sistemafinanciero.dao.QueryParameter;
import org.ventura.sistemafinanciero.entity.CuentaAporte;
import org.ventura.sistemafinanciero.entity.PersonaJuridica;
import org.ventura.sistemafinanciero.entity.PersonaNatural;
import org.ventura.sistemafinanciero.entity.Socio;
import org.ventura.sistemafinanciero.entity.SocioView;
import org.ventura.sistemafinanciero.entity.type.EstadoCuentaAporte;
import org.ventura.sistemafinanciero.entity.type.TipoPersona;
import org.ventura.sistemafinanciero.exception.RollbackFailureException;
import org.ventura.sistemafinanciero.service.PersonaJuridicaService;
import org.ventura.sistemafinanciero.service.PersonaNaturalService;
import org.ventura.sistemafinanciero.service.SocioService;
import org.ventura.sistemafinanciero.util.ProduceObject;

@Named
@Stateless
@Remote(SocioService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SocioServiceBean extends AbstractServiceBean<SocioView> implements SocioService {

	private static Logger LOGGER = LoggerFactory.getLogger(SocioServiceBean.class);

	@Inject
	private DAO<Object, Socio> socioDAO;
	@Inject
	private DAO<Object, SocioView> socioViewDAO;
	@Inject
	private DAO<Object, CuentaAporte> cuentaAporteDAO;
	
	@EJB
	private PersonaNaturalService personaNaturalService;
	@EJB
	private PersonaJuridicaService personaJuridicaService;

	@Override
	public BigInteger create(TipoPersona tipoPersona, BigInteger idDocSocio,
			String numDocSocio, BigInteger idDocApoderado,
			String numDocApoderado) throws RollbackFailureException {

		PersonaNatural personaNatural = null;
		PersonaJuridica personaJuridica = null;
		PersonaNatural apoderado = personaNaturalService.findByTipoNumeroDocumento(idDocApoderado, numDocApoderado);
		Calendar calendar = Calendar.getInstance();
		switch (tipoPersona) {
		case NATURAL:
			personaNatural = personaNaturalService.findByTipoNumeroDocumento(idDocSocio, numDocSocio);
			if(personaNatural == null)
				throw new RollbackFailureException("Persona para socio no encontrado");
			break;
		case JURIDICA:
			personaJuridica = personaJuridicaService.findByTipoNumeroDocumento(idDocSocio, numDocSocio);
			break;
		default:
			throw new RollbackFailureException("Tipo de persona no valido");
		}
						
		//verificar si el socio ya existe
		Socio socio = findSocio(tipoPersona, idDocSocio,numDocSocio);
		if(socio != null){
			if(!socio.getEstado()){
				CuentaAporte aporte = socio.getCuentaAporte();
				if(aporte != null){
					CuentaAporte cuentaAporte = new CuentaAporte();
					cuentaAporte.setEstadoCuenta(EstadoCuentaAporte.ACTIVO);
					cuentaAporte.setMoneda(ProduceObject.getMonedaPrincipal());
					cuentaAporte.setSaldo(BigDecimal.ZERO);
					cuentaAporte.setSocios(null);
					cuentaAporteDAO.create(cuentaAporte);
					
					socio.setCuentaAporte(cuentaAporte);
					socio.setApoderado(apoderado);
					socioDAO.update(socio);
				} else {
					throw new RollbackFailureException("Socio ya existente, y tiene cuenta aportes activa");
				}
			} else {
				throw new RollbackFailureException("Socio ya existente, no se puede registrar nuevamente");
			}
		} else {			
			CuentaAporte cuentaAporte = new CuentaAporte();
			cuentaAporte.setEstadoCuenta(EstadoCuentaAporte.ACTIVO);
			cuentaAporte.setMoneda(ProduceObject.getMonedaPrincipal());
			cuentaAporte.setSaldo(BigDecimal.ZERO);
			cuentaAporte.setSocios(null);
			cuentaAporteDAO.create(cuentaAporte);
			
			socio = new Socio();
			socio.setPersonaNatural(personaNatural);
			socio.setPersonaJuridica(personaJuridica);
			socio.setApoderado(apoderado);
			socio.setCuentaAporte(cuentaAporte);
			socio.setEstado(true);
			socio.setFechaInicio(calendar.getTime());
			socio.setFechaFin(null);
			socioDAO.create(socio);
		}				
		
		return socio.getIdSocio();
	}

	@Override
	public Socio findSocio(TipoPersona tipoPersona, BigInteger idTipoDoc, String numDoc) {
		switch (tipoPersona) {
		case NATURAL:
			PersonaNatural personaNatural = personaNaturalService.findByTipoNumeroDocumento(idTipoDoc, numDoc);
			if(personaNatural == null)
				return null;
			Set<Socio> socios = personaNatural.getSocios();
			if(socios.size() == 1){
				for (Socio socio : socios) {
					return socio;
				}
			} else {
				return null;
			}
			break;
		case JURIDICA:
			PersonaJuridica personaJuridica = personaJuridicaService.findByTipoNumeroDocumento(idTipoDoc, numDoc);
			if(personaJuridica == null)
				return null;
			Set<Socio> socios2 = personaJuridica.getSocios();
			if(socios2.size() == 1){
				for (Socio socio : socios2) {
					return socio;
				}
			} else {
				return null;
			}
			break;
		default:
			return null;
		}
		return null;
	}
	
	@Override
	public Set<SocioView> findByFilterText(String filterText) {
		if (filterText == null)
			return new HashSet<SocioView>();
		if (filterText.isEmpty() || filterText.trim().isEmpty()) {
			return new HashSet<SocioView>();
		}
		List<SocioView> list = null;
		QueryParameter queryParameter = QueryParameter.with("filtertext",
				'%' + filterText.toUpperCase() + '%');
		list = socioViewDAO.findByNamedQuery(
				SocioView.FindByFilterTextSocioView,
				queryParameter.parameters(), 1000);
		return new HashSet<SocioView>(list);
	}

	@Override
	protected DAO<Object, SocioView> getDAO() {
		return socioViewDAO;
	}	

}
