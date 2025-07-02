package dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import beans.Entidad;
import beans.Propiedad;
import domain.Cancion;
import domain.PlayList;
import domain.Usuario;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

public class AdaptadorUsuarioTDS implements IAdaptadorUsuarioDAO {
	private static ServicioPersistencia servPersistencia;
	private static AdaptadorUsuarioTDS unicaInstancia = null;

	public static AdaptadorUsuarioTDS getUnicaInstancia() { 
		if (unicaInstancia == null)
			return new AdaptadorUsuarioTDS();
		else
			return unicaInstancia;
	}
	
	private AdaptadorUsuarioTDS() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	@Override	//Función de registro del usuario en la BBDD
	public void registrarUsuario(Usuario usuario) {
		Entidad eUsuario = null;
		// si ya existe, lo encuentra y lo devuelve; si no, hace todo lo de esta función
		try {
			eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
		} catch (NullPointerException e) {
		}
		if (eUsuario != null)
			return;

		// registro las playlist que hay en la lista de playlists
		AdaptadorPlayListTDS adaptadorPlayList = AdaptadorPlayListTDS.getUnicaInstancia();
		if (usuario.getPlaylists() != null) {
			for (PlayList p : usuario.getPlaylists())
				adaptadorPlayList.registrarPlayList(p);
		}
		
		// registro las canciones de la lista de recientes
		AdaptadorCancionTDS adaptadorCancion = AdaptadorCancionTDS.getUnicaInstancia();
		if (usuario.getRecientes() != null) {
			for (Cancion c : usuario.getRecientes())
				adaptadorCancion.registrarCancion(c);
		}

		// creo la entidad con su nombre y propiedades
		eUsuario = new Entidad();
		eUsuario.setNombre("usuario");
		eUsuario.setPropiedades(new ArrayList<Propiedad>(Arrays.asList(new Propiedad("nickname", usuario.getNickname()), 
				new Propiedad("ispremium", Boolean.toString(usuario.isPremium())),
				new Propiedad("email", usuario.getEmail()), new Propiedad("password", usuario.getPassword()),
				new Propiedad("fechanacimiento", usuario.getFechaNacimiento()),
				new Propiedad("playlists", obtenerCodigosPlaylists(usuario.getPlaylists())),
				new Propiedad("recientes", obtenerCodigosCanciones(usuario.getRecientes())))));

		// registro la entidad cliente
		eUsuario = servPersistencia.registrarEntidad(eUsuario);
		// recupero el código dado por la BBDD
		usuario.setCodigo(eUsuario.getId());
	}

	@Override	//funcón de borrado del usuario en la BBDD
	public void borrarUsuario(Usuario usuario) {
		// recupero la entidad
		Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());

		// borro primero las playlists del usuario
		for (PlayList p : usuario.getPlaylists())
			AdaptadorPlayListTDS.getUnicaInstancia().borrarPlayList(p);

		// borro la playlist
		servPersistencia.borrarEntidad(eUsuario);
	}

	@Override	//Función para actualizar el usuario en la BBDD
	public void modificarUsuario(Usuario usuario) {
		Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());

		// actualizo todos los valores modificados de la playlist
		for (Propiedad p : eUsuario.getPropiedades()) {
			if (p.getNombre().equals("codigo")) {
				p.setValor(String.valueOf(usuario.getCodigo()));
			} else if (p.getNombre().equals("nickname")) {
				p.setValor(usuario.getNickname());
			} else if (p.getNombre().equals("ispremium")) {
				p.setValor(Boolean.toString(usuario.isPremium()));
			} else if (p.getNombre().equals("email")) {
				p.setValor(usuario.getEmail());
			} else if (p.getNombre().equals("password")) {
				p.setValor(usuario.getPassword());
			} else if (p.getNombre().equals("fechanacimiento")) {
				p.setValor(usuario.getFechaNacimiento());
			} else if (p.getNombre().equals("playlists")) {
				String playLists = obtenerCodigosPlaylists(usuario.getPlaylists());
				p.setValor(playLists);
			} else if (p.getNombre().equals("recientes")) {
				String recientes = obtenerCodigosCanciones(usuario.getRecientes());
				p.setValor(recientes);
			}
			servPersistencia.modificarPropiedad(p);
		}
	}

	@Override	//Función para recuperar el usuario de la BBDD
	public Usuario recuperarUsuario(int codigo) {
		// recupero la entidad
		Entidad eUsuario = servPersistencia.recuperarEntidad(codigo);

		String nickname;
		boolean isPremium;
		String email;
		String password;
		String fechaNacimiento;
		List<PlayList> playlists = new LinkedList<PlayList>();
		List<Cancion> recientes = new LinkedList<Cancion>();

		// recupero los atributos primitivos de la playlist
		nickname = servPersistencia.recuperarPropiedadEntidad(eUsuario, "nickname");
		email = servPersistencia.recuperarPropiedadEntidad(eUsuario, "email");
		password = servPersistencia.recuperarPropiedadEntidad(eUsuario, "password");
		fechaNacimiento = servPersistencia.recuperarPropiedadEntidad(eUsuario, "fechanacimiento");
		isPremium = Boolean.parseBoolean(servPersistencia.recuperarPropiedadEntidad(eUsuario, "ispremium"));

		// creo el nuevo usuario y le asigno el codigo que me da la persistencia
		Usuario usuario = new Usuario(nickname, isPremium, email, password, fechaNacimiento);
		usuario.setCodigo(eUsuario.getId());

		// recupero las canciones a partir de los codigos en la entidad canciones
		playlists = obtenerPlayListsDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eUsuario, "playlists"));
		for (PlayList p : playlists)
			usuario.addPlaylist(p);

		// recupero las canciones a partir de los codigos en la entidad canciones
		recientes = obtenerCancionesDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eUsuario, "recientes"));
		for (Cancion c : recientes)
			usuario.addCancionReciente(c);

		// por ultimo, devuelvo el usuario
		return usuario;
	}

	@Override		//función para recuperar todos los usuarios de la BBDD
	public List<Usuario> recuperarTodosUsuarios() {
		List<Entidad> eUsuarios = servPersistencia.recuperarEntidades("usuario");
		List<Usuario> usuarios = new LinkedList<Usuario>();

		for (Entidad e : eUsuarios)
			usuarios.add(recuperarUsuario(e.getId()));

		return usuarios;
	}
	

	// ------------------Funciones privadas auxiliares-------------------------
	// funcion para obtener los codigos de las playlists de un usuario
	private String obtenerCodigosPlaylists(List<PlayList> listaPlayLists) {
		if (listaPlayLists != null) {
			String aux = "";
			for (PlayList p : listaPlayLists) {
				aux += p.getCodigo() + " ";
			}
			return aux.trim(); // elimina espacios en blanco al principio y al final
		} else
			return "";
	}

	// funcion para obtener las playlists desde los codigos de la persistencia
	private List<PlayList> obtenerPlayListsDesdeCodigos(String playLists) {

		List<PlayList> listaPlayLists = new LinkedList<PlayList>();
		StringTokenizer strTok = new StringTokenizer(playLists, " ");
		AdaptadorPlayListTDS adaptadorV = AdaptadorPlayListTDS.getUnicaInstancia();
		while (strTok.hasMoreTokens()) {
			listaPlayLists.add(adaptadorV.recuperarPlayList(Integer.valueOf((String) strTok.nextElement())));
		}
		return listaPlayLists;
	}

	// funcion para obtener los codigos de las canciones (recientes) de un usuario
	private String obtenerCodigosCanciones(List<Cancion> listaCanciones) {
		if (listaCanciones != null) {
			String aux = "";
			for (Cancion c : listaCanciones) {
				aux += c.getCodigo() + " ";
			}
			return aux.trim(); // elimina espacios en blanco al principio y al final
		} else
			return "";
	}

	// funcion para obtener las canciones recientes desde los codigos de la
	// persistencia
	private List<Cancion> obtenerCancionesDesdeCodigos(String canciones) {

		List<Cancion> listaCanciones = new LinkedList<Cancion>();
		StringTokenizer strTok = new StringTokenizer(canciones, " ");
		AdaptadorCancionTDS adaptadorV = AdaptadorCancionTDS.getUnicaInstancia();
		while (strTok.hasMoreTokens()) {
			listaCanciones.add(adaptadorV.recuperarCancion(Integer.valueOf((String) strTok.nextElement())));
		}
		return listaCanciones;
	}

}
