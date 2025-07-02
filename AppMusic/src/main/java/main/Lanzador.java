package main;

import java.awt.EventQueue;

import gui.VentanaLogin;

public class Lanzador {
	public static void main(final String[] args){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaLogin loginView = new VentanaLogin();
					loginView.mostrarVentana();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
