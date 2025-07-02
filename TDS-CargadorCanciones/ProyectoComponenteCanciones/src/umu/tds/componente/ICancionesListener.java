package umu.tds.componente;

import java.util.EventListener;

 
/**
 * Interfaz para notificar a los oyentes de las nuevas canciones
 */
public interface ICancionesListener extends EventListener{
	public void existenNuevasCanciones(CancionesEvent e);
}
