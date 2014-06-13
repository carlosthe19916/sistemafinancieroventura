package org.ventura.sistemafinanciero.control;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;

import org.ventura.sistemafinanciero.dao.DAO;
import org.ventura.sistemafinanciero.entity.Beneficiario;
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
	protected DAO<Object, Beneficiario> getDAO() {
		return this.beneficiarioDAO;
	}

}
