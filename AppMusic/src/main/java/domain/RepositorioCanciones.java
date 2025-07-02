package domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import dao.DAOException;
import dao.FactoriaDAO;
import dao.IAdaptadorCancionDAO;

public class RepositorioCanciones {
	private static RepositorioCanciones unicaInstancia;
	private FactoriaDAO factoria;
	private IAdaptadorCancionDAO adaptadorCancion;
	//constantes necesarias
	private final int MAS_ESCUCHADAS = 10;
	
	private List<Cancion> canciones;

	public static RepositorioCanciones getUnicaInstancia() {
		if (unicaInstancia == null)
			unicaInstancia = new RepositorioCanciones();
		return unicaInstancia;
	}
	//constructor 
	private RepositorioCanciones() {
		canciones = new LinkedList<Cancion>();
		try {
			factoria = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS);
			adaptadorCancion = factoria.getCancionDAO();
			List<Cancion> listaCanciones = adaptadorCancion.recuperarTodasCanciones();
			for (Cancion cancion : listaCanciones) {
				canciones.add(cancion);
			}
		} catch (DAOException eDAO) {
			eDAO.printStackTrace();
		}
	}
	//función para añadir una canción al repositorio
	public void addCancion(Cancion cancion) {
		canciones.add(cancion);
	}
	//función para comprobar si existe una canción en el repositorio
	public boolean existeCancionRepo(Cancion c) {
		return canciones.stream().anyMatch(canc -> canc.getTitulo().equals(c.getTitulo()));
	}
	//función que filtra las canciones
	public List<Cancion> filtrarCanciones(String titulo, String interprete, String estilo) {
		List<Cancion> lista = new LinkedList<Cancion>();

		// Filtrar solo por titulo
		if (!titulo.isEmpty() && interprete.isEmpty() && estilo.equals("Style"))
			lista = canciones.stream().
				filter(c -> c.getTitulo().contains(titulo)).
				collect(Collectors.toList());
		// Filtrar solo por interprete
		else if (!interprete.isEmpty() && titulo.isEmpty() && estilo.equals("Style"))
			lista = canciones.stream().	
				filter(c -> c.getInterprete().contains(interprete)).
				collect(Collectors.toList());
		// Filtrar solo por estilo
		else if (interprete.isEmpty() && titulo.isEmpty() && !estilo.equals("Style"))
			lista = canciones.stream().
				filter(c -> c.getEstilo().equals(estilo)).
				collect(Collectors.toList());
		// Filtrar por titulo e interprete
		else if (!titulo.isEmpty() && !interprete.isEmpty() && estilo.equals("Style"))
			lista = canciones.stream().
				filter(c -> c.getTitulo().contains(titulo))
				.filter(c -> c.getInterprete().contains(interprete))
				.collect(Collectors.toList());
		// Filtrar por titulo y estilo
		else if (!titulo.isEmpty() && interprete.isEmpty() && !estilo.equals("Style"))
			lista = canciones.stream()
				.filter(c -> c.getTitulo().contains(titulo))
				.filter(c -> c.getEstilo().equals(estilo)).
				collect(Collectors.toList());
		// Filtrar por interprete y estilo
		else if (titulo.isEmpty() && !interprete.isEmpty() && !estilo.equals("Style"))
			lista = canciones.stream()
				.filter(c -> c.getInterprete().contains(interprete))
				.filter(c -> c.getEstilo().equals(estilo))
				.collect(Collectors.toList());
		// Filtrar por titulo, interprete y estilo
		else if (!titulo.isEmpty() && !interprete.isEmpty() && !estilo.equals("Style"))
			lista = canciones.stream()
				.filter(c -> c.getTitulo().contains(titulo))
				.filter(c -> c.getInterprete().contains(interprete))
				.filter(c -> c.getEstilo().equals(estilo))
				.collect(Collectors.toList());
		return lista;
	}
	
	public List<Cancion> getCanciones() throws DAOException {
		return new LinkedList<Cancion>(canciones);
	}
	//función para borrar una canción del repositorio
	public void removeCancion(Cancion c) {
		canciones.remove(c);
	}
	// ###############################################
	// ####### FUNCIONALIDAD MÁS ESCUCHADAS ##########
	// ###############################################
	public List<Cancion> getCancionesMasEscuchadas() {
		return canciones.stream()
				.sorted(Comparator.comparingInt(Cancion::getNumReproducciones).reversed())
				.limit(MAS_ESCUCHADAS)
				.collect(Collectors.toList());
	}
	//Función para obtener todos los diferentes estilos existentes en las canciones
	public ArrayList<String> getEstilos(){
		ArrayList<String> estilos = new ArrayList<String>();
		for(Cancion c: canciones) {
			if (!estilos.contains(c.getEstilo())) estilos.add(c.getEstilo());
		}
		return estilos;
	}
}
