package org.ventura.sistemafinanciero.control;

import java.math.BigInteger;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.Hibernate;
import org.ventura.sistemafinanciero.dao.DAO;
import org.ventura.sistemafinanciero.entity.CuentaBancaria;
import org.ventura.sistemafinanciero.entity.PersonaJuridica;
import org.ventura.sistemafinanciero.entity.PersonaNatural;
import org.ventura.sistemafinanciero.entity.Socio;
import org.ventura.sistemafinanciero.entity.TipoDocumento;
import org.ventura.sistemafinanciero.entity.Titular;
import org.ventura.sistemafinanciero.entity.type.EstadoCuentaBancaria;
import org.ventura.sistemafinanciero.exception.NonexistentEntityException;
import org.ventura.sistemafinanciero.exception.RollbackFailureException;
import org.ventura.sistemafinanciero.service.TitularService;

@Named
@Stateless
@Remote(TitularService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class TitularServiceBean extends AbstractServiceBean<Titular> implements TitularService {

	@Inject
	private DAO<Object, Titular> titularDAO;

	@Override
	public Titular findById(BigInteger idTitular){
		Titular titular = titularDAO.find(idTitular);
		if(titular != null){
			PersonaNatural personaNatural =titular.getPersonaNatural();
			TipoDocumento documento = personaNatural.getTipoDocumento();
			Hibernate.initialize(personaNatural);
			Hibernate.initialize(documento);
		}
		return titular;		
	}
	
	@Override
	public void delete(BigInteger idTitular) throws NonexistentEntityException, RollbackFailureException {
		Titular titular = titularDAO.find(idTitular);
		if(titular == null)
			throw new NonexistentEntityException("Titular no existente");
		
		//verificando que no se elimine el titular principal
		PersonaNatural personaTitular = titular.getPersonaNatural();
		
		CuentaBancaria cuentaBancaria = titular.getCuentaBancaria();
		Socio socio = cuentaBancaria.getSocio();
		PersonaNatural personaNatural = socio.getPersonaNatural();
		PersonaJuridica personaJuridica = socio.getPersonaJuridica();
		
		if(cuentaBancaria.getEstado().equals(EstadoCuentaBancaria.INACTIVO))
			throw new RollbackFailureException("Cuenta INACTIVA, no se pueden modificar titulares");
		if(personaNatural != null){
			if(personaTitular.equals(personaNatural))
				throw new NonexistentEntityException("No se puede eliminar el titular principal");
		}
		if(personaJuridica != null)
			if(personaTitular.equals(personaJuridica.getRepresentanteLegal()))
				throw new NonexistentEntityException("No se puede eliminar el titular principal");
		
		titularDAO.delete(titular);
	}
	
	@Override
	protected DAO<Object, Titular> getDAO() {
		return this.titularDAO;
	}

}
