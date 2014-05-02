package org.ventura.sistemafinanciero.control;

import java.util.Set;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;

import org.ventura.sistemafinanciero.dao.DAO;
import org.ventura.sistemafinanciero.entity.Rol;
import org.ventura.sistemafinanciero.service.RolService;

@Named
@Stateless
@Remote(RolService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class RolServiceBean extends AbstractServiceBean<Rol> implements RolService {

	//private static Logger LOGGER = LoggerFactory.getLogger(RolServiceBean.class);

	@Inject private DAO<Object, Rol> rolDAO;

	@Override
	public Set<Rol> getRolesByUsuario(int idUsuario) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected DAO<Object, Rol> getDAO() {
		// TODO Auto-generated method stub
		return rolDAO;
	}
	
	

}
