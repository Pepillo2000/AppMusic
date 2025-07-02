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
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

public class AdaptadorPlayListTDS implements IAdaptadorPlayListDAO {
	private static ServicioPersistencia servPersistencia;
	private static AdaptadorPlayListTDS unicaInstancia = null;

	public static AdaptadorPlayListTDS getUnicaInstancia() {
		if (unicaInstancia == null)
			return new AdaptadorPlayListTDS();
		else
			return unicaInstancia;
	}
	
	private AdaptadorPlayListTDS() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	@Override	//Función de registro de la playList en la BBDD
	public void registrarPlayList(PlayList playList) {
		Entidad ePlayList = null;

		try {
			ePlayList = servPersistencia.recuperarEntidad(playList.getCodigo());
		} catch (NullPointerException e) {
		}
		if (ePlayList != null)
			return;

		// cojo las canciones que hay en la lista de canciones y las registro
		AdaptadorCancionTDS adaptadorcancion = AdaptadorCancionTDS.getUnicaInstancia();
		for (Cancion c : playList.getCanciones())
			adaptadorcancion.registrarCancion(c);

		// creo la entidad con sus propiedades
		ePlayList = new Entidad();
		ePlayList.setNombre("playlist");
		ePlayList.setPropiedades(new ArrayList<Propiedad>(Arrays.asList(new Propiedad("nombre", playList.getNombre()),
				new Propiedad("canciones", obtenerCodigosCanciones(playList.getCanciones())))));

		// registro la entidad cliente
		ePlayList = servPersistencia.registrarEntidad(ePlayList);
		// recupero el código dado por la BBDD
		playList.setCodigo(ePlayList.getId());
	}

	@Override	//funcón de borrado de la playList
	public void borrarPlayList(PlayList playList) {
		// recupero la entidad
		Entidad ePlaylist = servPersistencia.recuperarEntidad(playList.getCodigo());

		// borro la playlist
		servPersistencia.borrarEntidad(ePlaylist);
	}

	@Override	//función para actualizar la playList en la BBDD
	public void modificarPlayList(PlayList playList) {
		// recupero la entidad
		Entidad ePlaylist = servPersistencia.recuperarEntidad(playList.getCodigo());

		// actualizo todos los valores modificados de la playlist
		for (Propiedad p : ePlaylist.getPropiedades()) {
			if (p.getNombre().equals("codigo")) {
				p.setValor(String.valueOf(playList.getCodigo()));
			} else if (p.getNombre().equals("nombre")) {
				p.setValor(playList.getNombre());
			} else if (p.getNombre().equals("canciones")) {
				String canciones = obtenerCodigosCanciones(playList.getCanciones());
				p.setValor(canciones);
			}
			servPersistencia.modificarPropiedad(p);
		}
	}

	@Override	//Función para recuperar la playList de la BBDD
	public PlayList recuperarPlayList(int codigo) {
		// recupero la entidad
		Entidad ePlaylist = servPersistencia.recuperarEntidad(codigo);

		String nombre;
		List<Cancion> canciones = new LinkedList<Cancion>();

		// recupero los atributos primitivos de la playlist
		nombre = servPersistencia.recuperarPropiedadEntidad(ePlaylist, "nombre");

		// creo la nueva playlist y le asigno el codigo que me da la persistencia
		PlayList playlist = new PlayList(nombre);
		playlist.setCodigo(ePlaylist.getId());

		// recupero las canciones a partir de los codigos en la entidad canciones
		canciones = obtenerCancionesDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(ePlaylist, "canciones"));
		for (Cancion c : canciones)
			playlist.addCancion(c);
		// por ultimo, devuelvo la playlist
		return playlist;
	}

	@Override	//función para recuperar todas las playLists de la BBDD
	public List<PlayList> recuperarTodasPlayLists() {
		List<Entidad> ePlayLists = servPersistencia.recuperarEntidades("playlist");
		List<PlayList> playLists = new LinkedList<PlayList>();

		for (Entidad e : ePlayLists)
			playLists.add(recuperarPlayList(e.getId()));

		return playLists;
	}

	// ------------------Funciones privadas auxiliares-------------------------
	// funcion para obtener los codigos de las canciones de una playlist
	private String obtenerCodigosCanciones(List<Cancion> listaCanciones) {
		String aux = "";
		for (Cancion c : listaCanciones) {
			aux += c.getCodigo() + " ";
		}
		return aux.trim(); // elimina espacios en blanco al principio y al final
	}

	// funcion para obtener las canciones desde los codigos de la persistencia
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
