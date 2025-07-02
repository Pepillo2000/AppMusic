package domain;

import java.util.HashMap;
import java.util.List;

import dao.FactoriaDAO;
import dao.IAdaptadorUsuarioDAO;
import dao.DAOException;

public class RepositorioUsuarios {
	private static RepositorioUsuarios unicaInstancia;
	private FactoriaDAO factoria;
	private IAdaptadorUsuarioDAO adaptadorUsuario;

	private HashMap<String, Usuario> usuarios;

	public static RepositorioUsuarios getUnicaInstancia() {
		if (unicaInstancia == null) unicaInstancia = new RepositorioUsuarios();
		return unicaInstancia;
	}
	//constructor
	private RepositorioUsuarios() {
		usuarios = new HashMap<String, Usuario>();
		try {
  			factoria = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS);
  			adaptadorUsuario = factoria.getUsuarioDAO();
  			List<Usuario> listausuarios = adaptadorUsuario.recuperarTodosUsuarios();
			for (Usuario usuario : listausuarios) {
				usuarios.put(usuario.getNickname(), usuario);
			}
  		} catch (DAOException eDAO) {
  			eDAO.printStackTrace();
  		}
	}
	// ###############################################
	// ######### FUNCIONALIDAD DEL REPOSITORIO #######
	// ###############################################
	public Usuario findUsuario(String nickname) {
		return usuarios.get(nickname);
	}
	
	public void addUsuario(Usuario usuario) {
		usuarios.put(usuario.getNickname(), usuario);
	}
	
	public void removeUsuario(Usuario usuario) {
		usuarios.remove(usuario.getNickname());
	}
}
