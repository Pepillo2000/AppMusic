package domain;

import java.util.LinkedList;
import java.util.List;

public class PlayList {
	//atributos
	private int codigo;
	private String nombre;
	private List<Cancion> canciones;
	//constructores
	public PlayList(String nombre) {
		codigo = 0;
		this.nombre = nombre;
		this.canciones = new LinkedList<Cancion>();
	}
	
	public PlayList(String nombre, List<Cancion> canciones) {
		codigo = 0;
		this.nombre = nombre;
		this.canciones = canciones;
	}
	//getters y setters
	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Cancion> getCanciones() {
		return canciones;
	}

	public void setCanciones(List<Cancion> canciones) {
		this.canciones = canciones;
	}
	
	public void addCancion(Cancion c) {
		this.canciones.add(c);
	}
	
}
