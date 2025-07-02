package controller;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import dao.AdaptadorCancionTDS;
import domain.Cancion;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Player {
	
	private static Player unicaInstancia;
	private MediaPlayer mediaPlayer;
	private String tempPath;
	private Cancion cancionActual;
	
	//CONSTRUCTOR
	public Player() {
		// existen otras formas de lanzar JavaFX desde Swing
		try {
			com.sun.javafx.application.PlatformImpl.startup(() -> {
			});
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Exception: " + ex.getMessage());
		}
		tempPath = "./temp"; //ruta del fichero temporal
	}

	public static Player getUnicaInstancia() {
		if (unicaInstancia == null)
			unicaInstancia = new Player();
		return unicaInstancia;
	}
	
	
	public void reproducirCancion() {
		if (cancionActual != null) {	//nos aseguramos de que la canción no es null
			playCancion();
			//actualizamos atributos de la canción
			cancionActual.setNumReproducciones(cancionActual.getNumReproducciones()+1);
			AdaptadorCancionTDS.getUnicaInstancia().modificarCancion(cancionActual);
		}
	}
	
	public void playCancion() {
		//Comprobamos primero el estado de media player
		if (mediaPlayer == null || (mediaPlayer.getStatus().equals(MediaPlayer.Status.STOPPED))) {
			try {
				setCancionActual();	//si es correcto, llamamos a la función
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		mediaPlayer.play();	//reproducimos una vez está descargada y lista
	}
	//función para pausar la canción
	public void pauseCancion() {
		if (mediaPlayer != null)
			mediaPlayer.pause();
	}
	//función para parar la canción
	public void stopCancion() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();

			File directorio = new File(tempPath);
			String[] files = directorio.list();
			//borramos todos los ficheros del directorio
			for (String archivo : files) {
				File fichero = new File(tempPath + File.separator + archivo);
				fichero.delete();
			}
		}
	}

	private void setCancionActual() throws Exception {
		//obtenemos la URL
		URL uri = new URL(cancionActual.getRutaFichero());
		//creamos la ruta
		System.setProperty("java.io.tmpdir", tempPath);
		Path mp3 = Files.createTempFile("now-playing", ".mp3");
		//descargamos la canción en la ruta creada
		InputStream stream = uri.openStream();
		Files.copy(stream, mp3, StandardCopyOption.REPLACE_EXISTING);
		//creamos un nuevo mediaPlayer con la canción a reproducir
		Media media = new Media(uri.toExternalForm());
		mediaPlayer = new MediaPlayer(media);
	}
	
	public Cancion getCancionActual() {
		return cancionActual;
	}
	
	public void setCancionActual(Cancion c) {
		cancionActual = c;
	}

}