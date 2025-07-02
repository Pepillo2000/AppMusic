package domain;

import java.util.Objects;

public class Cancion {
	//atributos
	private int codigo;
	private String titulo;
	private String rutaFichero;
	private int numReproducciones;
	private String interprete;
	private String estilo;
	//constructor
	public Cancion(String titulo, String rutaFichero, String interprete, String estilo) {
		codigo = 0;
		this.titulo = titulo;
		this.rutaFichero = rutaFichero;
		this.interprete = interprete;
		this.estilo = estilo;
		this.numReproducciones = 0;
	}
	//getters y setters
	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getRutaFichero() {
		return rutaFichero;
	}

	public void setRutaFichero(String rutaFichero) {
		this.rutaFichero = rutaFichero;
	}

	public int getNumReproducciones() {
		return numReproducciones;
	}

	public void setNumReproducciones(int numReproducciones) {
		this.numReproducciones = numReproducciones;
	}

	public String getInterprete() {
		return interprete;
	}

	public void setInterprete(String interprete) {
		this.interprete = interprete;
	}

	public String getEstilo() {
		return estilo;
	}

	public void setEstilo(String estilo) {
		this.estilo = estilo;
	}
	
	// Dos canciones son distintas si tienen distinto ID 
	/// Funcionalidad necesaria para usar distinct en los streams
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Cancion cancion = (Cancion) obj;
		return codigo == cancion.getCodigo();
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigo);
	}

}
