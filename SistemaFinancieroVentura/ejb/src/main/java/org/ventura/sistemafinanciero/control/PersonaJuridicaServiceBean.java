package org.ventura.sistemafinanciero.control;

import java.math.BigInteger;
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
import org.ventura.sistemafinanciero.entity.PersonaJuridica;
import org.ventura.sistemafinanciero.entity.PersonaNatural;
import org.ventura.sistemafinanciero.entity.TipoDocumento;
import org.ventura.sistemafinanciero.exception.IllegalResultException;
import org.ventura.sistemafinanciero.exception.PreexistingEntityException;
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
    private Validator validator;

	@Override
	public BigInteger crear(PersonaJuridica personaJuridica) throws PreexistingEntityException {	
		Set<ConstraintViolation<PersonaJuridica>> violations = validator.validate(personaJuridica);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}

		TipoDocumento tipoDocumento = personaJuridica.getTipoDocumento();
		String numeroDocumento = personaJuridica.getNumeroDocumento();

		Object obj = findByTipoNumeroDocumento(tipoDocumento.getIdTipoDocumento(), numeroDocumento);
		if (obj == null)
			personaJuridicaDAO.create(personaJuridica);
		else
			throw new PreexistingEntityException("La persona con el Tipo y Numero de documento ya existe");
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
			List<PersonaJuridica> list = personaJuridicaDAO.findByNamedQuery(PersonaJuridica.FindByTipoAndNumeroDocumento, queryParameter.parameters());
			if (list.size() > 1)
				throw new IllegalResultException("Se encontró mas de una persona con idDocumento:" + idTipodocumento + " y numero de documento:" + numerodocumento);
			else 
				for (PersonaJuridica personaJuridica : list) {
					result = personaJuridica;
					TipoDocumento tipoDocumento = result.getTipoDocumento();
					Hibernate.initialize(tipoDocumento);
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
