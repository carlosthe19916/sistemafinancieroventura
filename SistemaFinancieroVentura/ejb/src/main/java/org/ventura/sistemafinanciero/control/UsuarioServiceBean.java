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
		/*Usuario usuario = null;
		try {
			QueryParameter queryParameter = QueryParameter.with("username", username);
			List<Usuario> result = usuarioDAO.findByNamedQuery(Usuario.findByUsername, queryParameter.parameters());
			int count = 0;
			for (Usuario u : result) {
				if(u.getEstado() == true)
					count++;
				if(count > 1)
					break;
			}
			if(count == 1)
				usuario = result.get(0);
			else
				throw new Exception("Mas de un usuario activo encontrado");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return usuario;*/return null;
	}

	@Override
	protected DAO<Object, Usuario> getDAO() {
		return usuarioDAO;
	}

}
