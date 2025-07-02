package umu;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import dao.DAOException;
import domain.Cancion;
import domain.RepositorioCanciones;

/**
 * TEST SOBRE CLASE REPOSITORIO CANCIONES: Más escuchadas y filtros de búsqueda
 *
 */
public class RepositorioCancionesTest {
	
	private static RepositorioCanciones repositorioCanciones;
	private static Cancion cancion1, cancion2, cancion3;
	private static List<Cancion> lista;
	
	@BeforeClass
	public static void inicializacion() throws DAOException {
		repositorioCanciones = RepositorioCanciones.getUnicaInstancia();
		lista = new LinkedList<Cancion>();
		
		cancion1 = new Cancion("Ave María", "David Bisbal", "ruta1", "Pop");
		cancion1.setNumReproducciones(7);
		cancion1.setCodigo(1);
		
		cancion2 = new Cancion("La raja de tu falda", "Estopa", "ruta2", "Flamenco");
		cancion2.setNumReproducciones(28);
		cancion2.setCodigo(2);
				
		cancion3 = new Cancion("Yellow submarine", "The Beatles", "ruta3", "Pop"); 
		cancion3.setNumReproducciones(15);
		cancion3.setCodigo(3);
		
		lista.add(cancion1);
		lista.add(cancion2);
		lista.add(cancion3);
		
		// Vaciamos el repositorio, dado que no queremos que un elemento de este interfiera en nuestros tests
		List<Cancion> canciones = repositorioCanciones.getCanciones();
    	for (Cancion c : canciones) {
			repositorioCanciones.removeCancion(c);
    	}
	}
    
	// Añadimos las canciones al repositorio antes de empezar el test
    @Before
    public void inicializarRepositorio() {
    	for (Cancion cancion : lista) {
			repositorioCanciones.addCancion(cancion);
		}
    }
    
    // Borramos las canciones del repositorio al terminar el test pues no queremos que interfieran en la aplicación
    @After
    public void vaciarRepositorio() throws DAOException {
    	List<Cancion> canciones = repositorioCanciones.getCanciones();
    	for (Cancion c : canciones) {
			repositorioCanciones.removeCancion(c);
    	}
    }
    
	@Test
	public void testCancionMasReproducida() {
		Cancion cancionMasEscuchadas = repositorioCanciones.getCancionesMasEscuchadas().get(0);
		assertEquals(cancionMasEscuchadas.getTitulo(), cancion2.getTitulo());
	}
	
	@Test
    public void testFiltroTitulo() {
		List<Cancion> cancionesFiltradas = new LinkedList<Cancion>();
		cancionesFiltradas.add(cancion1);
		assertEquals(repositorioCanciones.filtrarCanciones("Ave", "" ,"Style"), cancionesFiltradas);
	}
	
	@Test
	public void testFiltroInterprete() {
		List<Cancion> cancionesFiltradas = new LinkedList<Cancion>();
		cancionesFiltradas.add(cancion1);
		cancionesFiltradas.add(cancion2);
		cancionesFiltradas.add(cancion3);
		assertEquals(repositorioCanciones.filtrarCanciones("", "a" ,"Style"), cancionesFiltradas);
	}
	
	@Test
	public void testFiltroEstilo() {
		List<Cancion> cancionesFiltradas = new LinkedList<Cancion>();	
		cancionesFiltradas.add(cancion1);
		cancionesFiltradas.add(cancion3);
		assertEquals(repositorioCanciones.filtrarCanciones("", "" ,"Pop"), cancionesFiltradas);
	}
	
	@Test
	public void testTodosFiltros() {
		List<Cancion> cancionesFiltradas = new LinkedList<Cancion>();	
		assertEquals(repositorioCanciones.filtrarCanciones("Alejandro", "Lady Gaga" ,"Style"), cancionesFiltradas);
	}

}
