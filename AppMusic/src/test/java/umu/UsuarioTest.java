package umu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import controller.Controlador;
import domain.Usuario;

/**
 * TEST UNITARIOS SOBRE LA CLASE USUARIO: Registro y login de usuario
 */
public class UsuarioTest {
	private Usuario usuario;
	
	@Before
	public void crearUsuarioTest() {
		usuario = new Usuario("pepa", false, "pepa@um.es", "pepa1234", "30/12/2000");
	}

	@Test
	public void testRegistro() {
		Controlador controlador = Controlador.getUnicaInstancia();

		controlador.registrarUsuario(usuario.getNickname(), usuario.isPremium(), usuario.getEmail(),
				usuario.getPassword(), usuario.getFechaNacimiento());

		assertTrue(controlador.esUsuarioRegistrado(usuario.getNickname()));
	}

	@Test
	public void testLogin() {
		Controlador controlador = Controlador.getUnicaInstancia();

		controlador.registrarUsuario(usuario.getNickname(), usuario.isPremium(), usuario.getEmail(),
				usuario.getPassword(), usuario.getFechaNacimiento());

		controlador.loginUsuario(usuario.getNickname(), usuario.getPassword());

		assertEquals(controlador.getUsuarioActual().getNickname(), usuario.getNickname());
	} 
	
	
}
