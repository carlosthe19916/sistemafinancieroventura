package org.ventura.sistemafinanciero.control;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ventura.sistemafinanciero.dao.DAO;
import org.ventura.sistemafinanciero.dao.QueryParameter;
import org.ventura.sistemafinanciero.entity.Accionista;
import org.ventura.sistemafinanciero.entity.PersonaJuridica;
import org.ventura.sistemafinanciero.entity.PersonaNatural;
import org.ventura.sistemafinanciero.entity.TipoDocumento;
import org.ventura.sistemafinanciero.exception.IllegalResultException;
import org.ventura.sistemafinanciero.exception.PreexistingEntityException;
import org.ventura.sistemafinanciero.exception.RollbackFailureException;
import org.ventura.sistemafinanciero.service.PersonaJuridicaService;

@Named
@Stateless
@Remote(PersonaJuridicaService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PersonaJuridicaServiceBean extends AbstractServiceBean<PersonaJuridica> implements PersonaJuridicaService {

	private static Logger LOGGER = LoggerFactory.getLogger(PersonaJuridicaServiceBean.class);

	@Inject
	private DAO<Object, PersonaJuridica> personaJuridicaDAO;
	@Inject
	private DAO<Object, PersonaNatural> personaNaturalDAO;
	@Inject
	private DAO<Object, Accionista> accionistaDAO;
	
	@Inject
    private Validator validator;

	@Override
	public List<PersonaJuridica> findAll(){
		List<PersonaJuridica> list = personaJuridicaDAO.findAll();
		for (PersonaJuridica personaJuridica : list) {
			Set<Accionista> accionistas = personaJuridica.getAccionistas();
			PersonaNatural personaNatural = personaJuridica.getRepresentanteLegal();
			TipoDocumento tipoDocumento = personaJuridica.getTipoDocumento();
			Hibernate.initialize(accionistas);
			Hibernate.initialize(personaNatural);
			Hibernate.initialize(tipoDocumento);
			
		}
		return list;
	}
	
	
	@Override
	public BigInteger crear(PersonaJuridica personaJuridica) throws PreexistingEntityException, RollbackFailureException {	
		Set<ConstraintViolation<PersonaJuridica>> violations = validator.validate(personaJuridica);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}

		TipoDocumento tipoDocumento = personaJuridica.getTipoDocumento();
		String numeroDocumento = personaJuridica.getNumeroDocumento();
		Set<Accionista> accionistas = personaJuridica.getAccionistas();
		
		Object obj = findByTipoNumeroDocumento(tipoDocumento.getIdTipoDocumento(), numeroDocumento);
		if (obj == null)
			personaJuridicaDAO.create(personaJuridica);
		else
			throw new PreexistingEntityException("La persona con el Tipo y Numero de documento ya existe");
		
		//crear accionistas		
		for (Accionista accionista : accionistas) {
			PersonaNatural personaNatural = accionista.getPersonaNatural();
			if(personaNatural != null){
				BigInteger idPersona = personaNatural.getIdPersonaNatural();
				if(idPersona != null){
					PersonaNatural persona = personaNaturalDAO.find(idPersona);
					if(persona != null){
						accionista.setPersonaJuridica(personaJuridica);			
						accionistaDAO.create(accionista);	
					} else {
						throw new RollbackFailureException("Accionista no encontrado");
					}
				}				
			}			
		}
		
		
		return personaJuridica.getIdPersonaJuridica();		
	}
	
	@Override
	public PersonaJuridica findByTipoNumeroDocumento(BigInteger idTipodocumento, String numerodocumento) {		
		if(idTipodocumento == null || numerodocumento == null)
			return null;
		if(numerodocumento.isEmpty() || numerodocumento.trim().isEmpty())
			return null;
		PersonaJuridica result = null;
		try {
			QueryParameter queryParameter = QueryParameter.with("idtipodocumento",idTipodocumento).and("numerodocumento", numerodocumento);
			List<PersonaJuridica> list = personaJuridicaDAO.findByNamedQuery(PersonaJuridica.FindByTipoAndNumeroDocumento,queryParameter.parameters());
			if (list.size() > 1)
				throw new IllegalResultException("Se encontró mas de una persona con idDocumento:" + idTipodocumento + " y numero de documento:" + numerodocumento);
			else 
				for (PersonaJuridica personaJuridica : list) {
					result = personaJuridica;
					TipoDocumento tipoDocumento = result.getTipoDocumento();
					PersonaNatural representante = result.getRepresentanteLegal();
					TipoDocumento documentoRepre = representante.getTipoDocumento();
					Set<Accionista> accionistas = result.getAccionistas();
					Hibernate.initialize(representante);
					Hibernate.initialize(accionistas);
					Hibernate.initialize(tipoDocumento);
					Hibernate.initialize(documentoRepre);
				}
		} catch (IllegalResultException e) {
			LOGGER.error(e.getMessage(), e.getLocalizedMessage(), e.getCause());
		}
		return result;
	}

	@Override
	public Set<PersonaJuridica> findByFilterText(String filterText) {		
		if(filterText == null)
			return new HashSet<PersonaJuridica>();
		if(filterText.isEmpty() || filterText.trim().isEmpty()){
			return new HashSet<PersonaJuridica>();
		}
		List<PersonaJuridica> list = null;
		QueryParameter queryParameter = QueryParameter.with("filtertext", filterText.toLowerCase());
		list = personaJuridicaDAO.findByNamedQuery(PersonaJuridica.FindByFilterText, queryParameter.parameters(), 1000);												
		return new HashSet<PersonaJuridica>(list);
	}

	@Override
	protected DAO<Object, PersonaJuridica> getDAO() {
		return personaJuridicaDAO;
	}

}
