package umu.tds.componente;

import java.util.EventObject;

/**
 * Evento que contiene las canciones
 */
public class CancionesEvent extends EventObject{
	
	private Canciones canciones;
	
	public CancionesEvent(Object source, Canciones c) {
		super(source);
		canciones = c;
	}
	
	public Canciones getCanciones() {
		return canciones;
	}

}
