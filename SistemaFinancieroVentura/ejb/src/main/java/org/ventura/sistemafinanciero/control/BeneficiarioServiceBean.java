package org.ventura.sistemafinanciero.control;

import java.math.BigInteger;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;

import org.ventura.sistemafinanciero.dao.DAO;
import org.ventura.sistemafinanciero.entity.Beneficiario;
import org.ventura.sistemafinanciero.entity.CuentaBancaria;
import org.ventura.sistemafinanciero.entity.type.EstadoCuentaBancaria;
import org.ventura.sistemafinanciero.exception.NonexistentEntityException;
import org.ventura.sistemafinanciero.exception.PreexistingEntityException;
import org.ventura.sistemafinanciero.exception.RollbackFailureException;
import org.ventura.sistemafinanciero.service.BeneficiarioService;

@Named
@Stateless
@Remote(BeneficiarioService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class BeneficiarioServiceBean extends AbstractServiceBean<Beneficiario>
		implements BeneficiarioService {

	@Inject
	private DAO<Object, Beneficiario> beneficiarioDAO;

	@Override
	public void update(BigInteger idBeneficiario, Beneficiario beneficiario) throws NonexistentEntityException, PreexistingEntityException{
		Beneficiario beneficiarioDB = beneficiarioDAO.find(idBeneficiario);
		if(beneficiarioDB == null)
			throw new NonexistentEntityException("Beneficiario no encontrado");
		CuentaBancaria cuentaBancaria = beneficiarioDB.getCuentaBancaria();
		if(cuentaBancaria.getEstado().equals(EstadoCuentaBancaria.INACTIVO))
			throw new NonexistentEntityException("Cuenta INACTIVA, no se puede modificar los beneficiarios");
		super.update(idBeneficiario, beneficiario);
	}
	
	@Override
	public void delete(BigInteger idBeneficiario) throws NonexistentEntityException, RollbackFailureException{
		Beneficiario beneficiario = beneficiarioDAO.find(idBeneficiario);
		if(beneficiario == null)
			throw new NonexistentEntityException("Beneficiario no encontrado");
		CuentaBancaria cuentaBancaria = beneficiario.getCuentaBancaria();
		if(cuentaBancaria.getEstado().equals(EstadoCuentaBancaria.INACTIVO))
			throw new RollbackFailureException("Cuenta INACTIVA, no se puede modificar los beneficiarios");
		beneficiarioDAO.delete(beneficiario);
	}
	
	@Override
	protected DAO<Object, Beneficiario> getDAO() {
		return this.beneficiarioDAO;
	}

}
