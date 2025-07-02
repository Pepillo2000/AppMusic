package dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import beans.Entidad;
import beans.Propiedad;
import domain.Cancion;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

public class AdaptadorCancionTDS implements IAdaptadorCancionDAO {
	private static ServicioPersistencia servPersistencia;
	private static AdaptadorCancionTDS unicaInstancia = null;

	public static AdaptadorCancionTDS getUnicaInstancia() {
		if (unicaInstancia == null)
			return new AdaptadorCancionTDS();
		else
			return unicaInstancia;
	}

	private AdaptadorCancionTDS() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}
	//Función de registro de la canción en la BBDD
	@Override
	public void registrarCancion(Cancion cancion) {
		Entidad eCancion = null;

		try {
			eCancion = servPersistencia.recuperarEntidad(cancion.getCodigo());
		} catch (NullPointerException e) {
		}
		if (eCancion != null)
			return;

		eCancion = new Entidad();
		eCancion.setNombre("cancion");
		eCancion.setPropiedades(new ArrayList<Propiedad>(Arrays.asList(new Propiedad("titulo", cancion.getTitulo()),
				new Propiedad("rutaFichero", cancion.getRutaFichero()),
				new Propiedad("numReproducciones", Integer.toString(cancion.getNumReproducciones())),
				new Propiedad("interprete", cancion.getInterprete()), new Propiedad("estilo", cancion.getEstilo()))));

		eCancion = servPersistencia.registrarEntidad(eCancion);
		cancion.setCodigo(eCancion.getId());
	}

	@Override // si tiene lista de objetos, borro esos objetos y luego borro este
	public void borrarCancion(Cancion cancion) {
		Entidad eCancion = servPersistencia.recuperarEntidad(cancion.getCodigo());
		servPersistencia.borrarEntidad(eCancion);
	}

	@Override // Función para actualizar la canción en la BBDD
	public void modificarCancion(Cancion cancion) {
		Entidad eCancion = servPersistencia.recuperarEntidad(cancion.getCodigo());

		for (Propiedad prop : eCancion.getPropiedades()) {
			if (prop.getNombre().equals("titulo")) {
				prop.setValor(String.valueOf(cancion.getTitulo()));
			} else if (prop.getNombre().equals("codigo")) { // esta no sé si ponerla
				prop.setValor(String.valueOf(cancion.getCodigo()));
			} else if (prop.getNombre().equals("rutaFichero")) {
				prop.setValor(cancion.getRutaFichero());
			} else if (prop.getNombre().equals("numReproducciones")) {
				prop.setValor(Integer.toString(cancion.getNumReproducciones()));
			} else if (prop.getNombre().equals("interprete")) {
				prop.setValor(cancion.getInterprete());
			} else if (prop.getNombre().equals("estilo")) {
				prop.setValor(cancion.getEstilo());
			}
			servPersistencia.modificarPropiedad(prop);
		}
	}

	@Override	//función para recuperar la canción de la BBDD
	public Cancion recuperarCancion(int codigo) {
		Entidad eCancion;
		String titulo;
		String rutaFichero;
		String numReproducciones;
		String interprete;
		String estilo;

		eCancion = servPersistencia.recuperarEntidad(codigo);
		// igual hay que cambiar el segundo argumento y ponerlo sin comillas
		titulo = servPersistencia.recuperarPropiedadEntidad(eCancion, "titulo");
		rutaFichero = servPersistencia.recuperarPropiedadEntidad(eCancion, "rutaFichero");
		numReproducciones = servPersistencia.recuperarPropiedadEntidad(eCancion, "numReproducciones");
		interprete = servPersistencia.recuperarPropiedadEntidad(eCancion, "interprete");
		estilo = servPersistencia.recuperarPropiedadEntidad(eCancion, "estilo");

		Cancion cancion = new Cancion(titulo, rutaFichero, interprete, estilo);
		cancion.setCodigo(codigo);
		cancion.setNumReproducciones(Integer.parseInt(numReproducciones));

		return cancion;
	}

	
	@Override	//Función para recuperar todas las canciones de la BBDD
	public List<Cancion> recuperarTodasCanciones() {
		List<Entidad> eCanciones = servPersistencia.recuperarEntidades("cancion");
		List<Cancion> canciones = new LinkedList<Cancion>();

		for (Entidad eCancion : eCanciones) {
			canciones.add(recuperarCancion(eCancion.getId()));
		}
		return canciones;
	}

}
