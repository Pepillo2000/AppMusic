package domain;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Usuario {
	//atributos
	private int codigo;
	private boolean isPremium;
	private String nickname;
	private String email;
	private String password;
	private String fechaNacimiento;
	private List<PlayList> playlists;
	private List<Cancion> recientes;
	private List<IDescuento> descuentos;
	private int edad;
	//constantes necesarias
	private final int MAX_CANCIONES = 10;
	private final double PRECIO_POR_DEFECTO = 8.99;
	//constructor
	public Usuario(String nickname, boolean isPremium, String email, String password, String fechaNacimiento) {
		codigo = 0;
		this.nickname = nickname;
		this.email = email;
		this.password = password;
		this.fechaNacimiento = fechaNacimiento;
		this.playlists = new LinkedList<PlayList>();
		this.recientes = new LinkedList<Cancion>();
		this.isPremium = isPremium;
		this.descuentos = new LinkedList<IDescuento>();
		this.edad = calcularEdad();
		descuentos.add(new DescuentoNavidad());
		descuentos.add(new DescuentoMayores(edad));
	}
	//getters y setters
	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	public boolean isPremium() {
		return isPremium;
	}

	public void setPremium(boolean isPremium) {
		this.isPremium = isPremium;
	}	

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public List<PlayList> getPlaylists() {
		return playlists;
	}
	
	public void setPlaylists(List<PlayList> playlists) {
		this.playlists = playlists;
	}
	
	public List<Cancion> getRecientes() {
		return recientes;
	}

	public void setRecientes(List<Cancion> recientes) {
		this.recientes = recientes;
	}
	
	public int getEdad() {
		return edad;
	}
	
	public void setEdad(int edad) {
		this.edad = edad;
	}
	
	//función para calcular la edad a partir de la fecha de nacimiento
	public int calcularEdad() {
		LocalDate now = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate nacimientoLocal = LocalDate.parse(fechaNacimiento, formatter);
		return Period.between(nacimientoLocal, now).getYears();
	}
	
	// ###############################################
	// ### FUNCIONALIDAD PARA GESTION DE PLAYLISTS ###
	// ###############################################

	public List<Cancion> getCancionesPlayList(String nombre){
		return getPlayList(nombre).getCanciones();
	}

	public PlayList getPlayList(String nombre) {
		for (PlayList p : playlists) {
			if (p.getNombre().equals(nombre)) return p;
		}
		return null;
	}
	
	public PlayList crearPlayList(String nombre, List<Cancion> canciones) {
		PlayList p = new PlayList(nombre, canciones);
		playlists.add(p);
		return p;
	}
	
	public void modificarPlayList(PlayList p, List<Cancion> canciones) {
		int pos = playlists.indexOf(p);
		playlists.remove(pos);
		p.setCanciones(canciones);
		playlists.add(p);
	}
	
	public void addPlaylist(PlayList p) {
		playlists.add(p);
	}
	
	public void borrarPlayList(PlayList p) {
		int pos = playlists.indexOf(p);
		playlists.remove(pos);
	}
	
	public boolean existePlayList(String nombre) {
		return playlists.stream()
				.anyMatch(p -> p.getNombre().equals(nombre));
	}

	// ###############################################
	// ########### FUNCIONALIDAD FAVORITAS ###########
	// ###############################################
		
	public List<Cancion> getFavoritas() {
		return playlists.stream()
                .flatMap(p -> p.getCanciones().stream())
                .distinct()
                .collect(Collectors.toList());				
	}
	
	// ###############################################
	// ########### FUNCIONALIDAD RECIENTES ###########
	// ###############################################

	public void addCancionReciente(Cancion nuevaCancionReciente) {
		if (recientes.contains(nuevaCancionReciente)) {
			recientes.remove(nuevaCancionReciente);
		} else if (recientes.size() == MAX_CANCIONES){
			recientes.remove(MAX_CANCIONES - 1);
		}
		recientes.add(0, nuevaCancionReciente);
	}
	
	// ###############################################
	// ########### FUNCIONALIDAD DESCUENTOS ##########
	// ###############################################
	
	public double calcularPago() {
		return descuentos.stream()
				.map(d -> d.calcDescuento(PRECIO_POR_DEFECTO))
				.min(Double::compare)
				.get();
	}
	
}
