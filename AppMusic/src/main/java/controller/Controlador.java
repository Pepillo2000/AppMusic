package controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dao.DAOException;
import dao.FactoriaDAO;
import dao.IAdaptadorCancionDAO;
import dao.IAdaptadorPlayListDAO;
import dao.IAdaptadorUsuarioDAO;
import domain.Cancion;
import domain.PlayList;
import domain.RepositorioCanciones;
import domain.RepositorioUsuarios;
import domain.Usuario;

import umu.tds.componente.CancionesEvent;
import umu.tds.componente.CargadorCanciones;
import umu.tds.componente.ICancionesListener;

public class Controlador implements ICancionesListener {
	
	private static Controlador unicaInstancia;
	
	private IAdaptadorUsuarioDAO adaptadorUsuario;
	private IAdaptadorCancionDAO adaptadorCancion;
	private IAdaptadorPlayListDAO adaptadorPlayList;
	
	private RepositorioUsuarios repositorioUsuarios;
	private RepositorioCanciones repositorioCanciones;
	
	private Usuario usuarioActual;
	
	private Player player;
	private GeneradorPDF generador;	
	
	//CONSTRUCTOR
	private Controlador() {
		inicializarAdaptadores(); // debe ser la primera linea para evitar error
								  // de sincronización
		inicializarRepositorios();
		
		player = Player.getUnicaInstancia();
		generador = new GeneradorPDF();
	}
	
	//FUNCIÓN PARA RECUPERAR LA ÚNICA INSTANCIA
	public static Controlador getUnicaInstancia() {
		if (unicaInstancia == null)
			unicaInstancia = new Controlador();
		return unicaInstancia;
	}
	
	//INCIALIZAMOS LOS ADAPTADORES
	private void inicializarAdaptadores() {
		FactoriaDAO factoria = null;
		try {
			factoria = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		adaptadorUsuario = factoria.getUsuarioDAO();
		adaptadorCancion = factoria.getCancionDAO();
		adaptadorPlayList = factoria.getPlayListDAO();
	}
	
	//INCIALIZAMOS LOS REPOSITORIOS
	private void inicializarRepositorios() {
		repositorioUsuarios = RepositorioUsuarios.getUnicaInstancia();
		repositorioCanciones = RepositorioCanciones.getUnicaInstancia();
	}
	
	// ###########################################
	// ########### FUNCIONES USUARIO #############
	// ###########################################
	
	public Usuario getUsuarioActual() {
		return usuarioActual;
	}
	
	public boolean esUsuarioRegistrado(String nickname) {
		// si se encuentra al usuario, está ya registrado y devuelve false
		// si no se encuentra al usuario, en null y devuelve true
		return repositorioUsuarios.findUsuario(nickname) != null;
	}
	
	public boolean loginUsuario(String nickname, String password) {
		Usuario usuario = repositorioUsuarios.findUsuario(nickname);
		if (usuario != null && usuario.getPassword().equals(password)) {
			usuarioActual = usuario;
			return true;
		}
		return false;
	}
	
	public boolean registrarUsuario(String nickname, boolean isPremium, String email,
			String password, String fechaNacimiento) {
		
		if (esUsuarioRegistrado(nickname))
			return false;
		Usuario usuario = new Usuario(nickname, isPremium, email, password, fechaNacimiento);
		adaptadorUsuario.registrarUsuario(usuario);
		repositorioUsuarios.addUsuario(usuario); 
		return true;
	}
	
	
	// ###########################################
    // ########### FUNCIONES CANCION #############
	// ###########################################
	
	public void existenNuevasCanciones(CancionesEvent e) {
		for(umu.tds.componente.Cancion c : e.getCanciones().getCancion()) {
			Cancion cancion = new Cancion(c.getTitulo(), c.getURL(), c.getInterprete(), c.getEstilo()); 
			if (!repositorioCanciones.existeCancionRepo(cancion)) {
				adaptadorCancion.registrarCancion(cancion);	
				repositorioCanciones.addCancion(cancion);	
			}
		}
	}
	
	
	public void cargarCanciones(File file) {
		CargadorCanciones cargador = new CargadorCanciones();
		cargador.addListener(Controlador.getUnicaInstancia());
		cargador.setArchivoCanciones(file.getAbsolutePath());
	}
	
	public List<Cancion> getCancionesFiltradas(String titulo, String interprete, String estilo){
		return repositorioCanciones.filtrarCanciones(titulo, interprete, estilo);			
	}
	
	public List<Cancion> getCancionesFavoritas(){
		return usuarioActual.getFavoritas();
	}
	
	public Cancion getCancionActual() {
		return player.getCancionActual();
	}
	
	public void setCancionActual(Cancion c) {
		player.setCancionActual(c);
	}
	
	// ###########################################
	// ########### FUNCIÓN RECIENTES #############
	// ###########################################
	
	public List<Cancion> getCancionesRecientes(){
		return usuarioActual.getRecientes();
	}
	
	// ###########################################
	// ########### FUNCIONES PLAYER ##############
	// ###########################################
	
	public void reproducirCancion() {
		player.reproducirCancion();
		//añade la canción reproducida a recientes del usuario
		usuarioActual.addCancionReciente(player.getCancionActual());
	}

	public void stopCancion() {
		player.stopCancion();
	}
	
	public void pausarCancion() {
		player.pauseCancion();
	}
	
	// ###########################################
	// ########### FUNCIONES PLAYLIST ############
	// ###########################################
	
	public List<PlayList> getPlayLists(){
		return usuarioActual.getPlaylists();
	}
	
	public List<Cancion> getCancionesPlayList(String nombre){
		return usuarioActual.getCancionesPlayList(nombre);
	}
	
	public boolean existePlayList(String nombre) {
		return usuarioActual.existePlayList(nombre);
	}
	
	public void registrarPlayList(List<Cancion> canciones, String titulo) {
		boolean existe = existePlayList(titulo);
		PlayList p;
		if (existe) {
			p = usuarioActual.getPlayList(titulo);
			usuarioActual.modificarPlayList(p, canciones);
			adaptadorPlayList.modificarPlayList(p);
		}
		else {
		   p = usuarioActual.crearPlayList(titulo, canciones);
		   adaptadorPlayList.registrarPlayList(p);
		}
		adaptadorUsuario.modificarUsuario(usuarioActual);	
	} 
	
	public void borrarPlayList(String nombre) {
		PlayList p = usuarioActual.getPlayList(nombre);
		usuarioActual.borrarPlayList(p);
		adaptadorPlayList.borrarPlayList(p);
		adaptadorUsuario.modificarUsuario(usuarioActual);
	}
	
	// ###########################################
	// ######## FUNCIONES USUARIO PREMIUM ########
	// ###########################################
	
	public boolean isPremium() {
		return usuarioActual.isPremium();
	}
	
	public void setPremium() {
		usuarioActual.setPremium(true);
		adaptadorUsuario.modificarUsuario(usuarioActual);
	}
	
	public void generarPDF() throws Exception{
		generador.generarPDF(usuarioActual.getNickname(), usuarioActual.getPlaylists());
	}
	
	public List<Cancion> getCancionesMasEscuchadas(){
		return repositorioCanciones.getCancionesMasEscuchadas();
	}
	
	public double calcularPrecioPremium() {
		return usuarioActual.calcularPago();
	}
	
	// ###########################################
	// ######### FUNCIÓN OBTENER ESTILOS #########
	// ###########################################
	
	public ArrayList<String> getEstilosRepo(){
		return repositorioCanciones.getEstilos();
	}
	
	// ###########################################
	// ############ FUNCIÓN DE LOGOUT ############
	// ###########################################
	
	public void logout() {
		//actualizo en la BBDD 
		adaptadorUsuario.modificarUsuario(usuarioActual);
		usuarioActual = null;
	}
	
}
