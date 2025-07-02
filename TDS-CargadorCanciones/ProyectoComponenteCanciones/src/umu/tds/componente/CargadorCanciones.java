package umu.tds.componente;

import java.util.ArrayList;

public class CargadorCanciones {
	private String archivoCanciones;	//ruta del fichero xml
	private ArrayList<ICancionesListener> listeners; 
	
	public CargadorCanciones(){
		archivoCanciones = "";
		listeners = new ArrayList<ICancionesListener>();
	}
	
	public synchronized void addListener(ICancionesListener listener) {
		listeners.add(listener);
	}
	
	public void setArchivoCanciones(String file) {
		archivoCanciones = file;
		if (file != null) {
			Canciones canciones = MapperCancionesXMLtoJava.cargarCanciones(archivoCanciones);
			CancionesEvent event = new CancionesEvent(this, canciones);
			notificarCambio(event);
		}
	}
	
	private void notificarCambio(CancionesEvent e) {
		ArrayList<ICancionesListener> lista;
		synchronized(this){
			lista = (ArrayList<ICancionesListener>) listeners.clone();
		}
		for(int i=0; i<lista.size(); i++){
			ICancionesListener listener=(ICancionesListener)lista.get(i);
			listener.existenNuevasCanciones(e);
		}
	}
}
