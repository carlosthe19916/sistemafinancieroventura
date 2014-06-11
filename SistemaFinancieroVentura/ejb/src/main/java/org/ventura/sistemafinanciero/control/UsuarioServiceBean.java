package org.ventura.sistemafinanciero.control;

import java.util.List;

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
import org.ventura.sistemafinanciero.entity.Usuario;
import org.ventura.sistemafinanciero.entity.type.RolType;
import org.ventura.sistemafinanciero.exception.IllegalResultException;
import org.ventura.sistemafinanciero.service.UsuarioService;

@Named
@Stateless
@Remote(UsuarioService.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UsuarioServiceBean extends AbstractServiceBean<Usuario> implements UsuarioService {

	private static Logger LOGGER = LoggerFactory.getLogger(UsuarioServiceBean.class);

	@Inject private DAO<Object, Usuario> usuarioDAO;

	@Override
	public Usuario findByUsername(String username) {
		Usuario usuario = null;
		try {
			QueryParameter queryParameter = QueryParameter.with("username", username);
			List<Usuario> result = usuarioDAO.findByNamedQuery(Usuario.findByUsername, queryParameter.parameters());
			if(result.size() > 1)
				throw new IllegalResultException("Mas de un usuario encontrado");
			for (Usuario u : result) {
				usuario = u;
			}			
		} catch (IllegalResultException e) {
			LOGGER.error(e.getMessage(), e.getCause(), e.getLocalizedMessage());
		}
		return usuario;
	}
	
	@Override
	public boolean authenticateAsAdministrator(String username, String password) {
		QueryParameter queryParameter = QueryParameter.with("username", username).and("password", password).and("rol", RolType.ADMINISTRADOR.toString());
		List<Usuario> list = usuarioDAO.findByNamedQuery(Usuario.findByUsernameAndPasswordAndRol, queryParameter.parameters());
		if(list.size() == 1)
			return true;
		else if(list.size() == 0)
			return false;
		else
			try {
				throw new IllegalResultException("Mas de un usuario encontrado");
			} catch (IllegalResultException e) {
				LOGGER.error(e.getMessage(), e.getCause(), e.getLocalizedMessage());
				return false;
			}	
	}

	@Override
	protected DAO<Object, Usuario> getDAO() {
		return usuarioDAO;
	}

}
