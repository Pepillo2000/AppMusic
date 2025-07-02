package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import controller.Controlador;

public class VentanaLogin extends JFrame {
	
	// Constantes para modificar los paneles
	private final int ANCHO = 350;
	private final int ALTO = 300;
	private final int ANCHO_CENTRO = 275;
	private final int ALTO_CENTRO = 175;
	private final int ANCHO_LATERALES = (ANCHO - ANCHO_CENTRO)/2;
	private final int ALTO_LATERALES = ALTO_CENTRO;
	private final int ANCHO_ARRIBA = ANCHO;
	private final int ALTO_ARRIBA = 80;
	private final int ANCHO_ABAJO = ANCHO;
	private final int ALTO_ABAJO = ALTO - ALTO_ARRIBA - ALTO_CENTRO;

	// Paneles 
	private JPanel contenedor, arriba, centro, abajo, izquierda, derecha, cajaLogin, cajaOpciones, cajaUser, cajaPassword;
	
	// Botones
	private JButton btnLog, btnGitHub, btnReg;
	
	// Textos y etiquetas
	private JTextField txtUser, txtPassword;
	private JLabel lApp;
	
	private Controlador controlador;
	
	public void mostrarVentana() {
		setLocationRelativeTo(null);	// Coloca la ventana en el centro de la pantalla
		setVisible(true);
	}
	//Constructor de la ventana
	public VentanaLogin() {
		inicializarVentana();
		contenedor = (JPanel) this.getContentPane();
		crearPanelesExteriores();
		crearPanelCentro();
	}
	//Inicialización de la ventana
	private void inicializarVentana() {
		controlador = Controlador.getUnicaInstancia();
		this.setSize(ANCHO, ALTO);
		this.setTitle("AppMusic");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		Image icon = new ImageIcon("./img/logo.png").getImage();
		this.setIconImage(icon);
	}
	//Creamos el panel del centro de la ventana
	private void crearPanelCentro() {
		centro = new JPanel();
		fixSize(centro, ANCHO_CENTRO, ALTO_CENTRO);
		contenedor.add(centro, BorderLayout.CENTER);
		
		cajaLogin = crearCajaLogin();
		centro.add(cajaLogin);
	
		cajaOpciones = crearCajaOpciones();		
		centro.add(cajaOpciones);		
	}
	//creamos los paneles exteriores
	private void crearPanelesExteriores() {
		arriba = new JPanel();
		fixSize(arriba, ANCHO_ARRIBA, ALTO_ARRIBA);
		arriba.setBackground(Color.BLACK);
		lApp = new JLabel("AppMusic", JLabel.CENTER);
		fixSize(lApp, 200, 75);
		Font font = new Font("Arial", Font.BOLD, 24);
		lApp.setFont(font);
		lApp.setForeground(Color.GREEN);
		arriba.add(lApp);
		
		abajo = new JPanel();
		abajo.setBackground(Color.BLACK);
		fixSize(abajo, ANCHO_ABAJO, ALTO_ABAJO);
		
		izquierda = new JPanel();
		fixSize(izquierda, ANCHO_LATERALES, ALTO_LATERALES);
		izquierda.setBackground(Color.BLACK);
		
		derecha = new JPanel();
		fixSize(derecha, ANCHO_LATERALES, ALTO_LATERALES);
		derecha.setBackground(Color.BLACK);
		
		contenedor.add(izquierda, BorderLayout.WEST);
		contenedor.add(derecha, BorderLayout.EAST);
		contenedor.add(abajo, BorderLayout.SOUTH);
		contenedor.add(arriba, BorderLayout.NORTH);
	}
	//creamos la caja del interior del panel central
	private JPanel crearCajaLogin() {
		JPanel log = new JPanel();
		fixSize(log, 220, 85);
		LineBorder borde = new LineBorder(Color.BLACK, 2);
		TitledBorder tBorde = BorderFactory.createTitledBorder(borde, "Login", TitledBorder.LEFT, TitledBorder.TOP, null, Color.BLACK);
		log.setBorder(tBorde);
		log.setLayout(new BoxLayout(log, BoxLayout.Y_AXIS));
		
		//añadimos la caja del username
		cajaUser = new JPanel();
		cajaUser.setOpaque(false);
		fixSize(cajaUser, 200, 30);
		JLabel lUser = new JLabel("User:", JLabel.RIGHT);
		fixSize(lUser, 65, 20);
		txtUser = new JTextField();
		fixSize(txtUser, 120, 20);
		cajaUser.add(lUser);
		cajaUser.add(txtUser);
		
		//añadimos la caja del password
		cajaPassword = new JPanel();
		cajaPassword.setOpaque(false);
		fixSize(cajaPassword, 200, 30);
		JLabel lPass = new JLabel("Password:", JLabel.RIGHT);
		fixSize(lPass, 65, 20);
		txtPassword = new JPasswordField();
		fixSize(txtPassword, 120, 20);
		cajaPassword.add(lPass);
		cajaPassword.add(txtPassword);
		
		log.add(cajaUser);
		log.add(cajaPassword);
		return log;
	}
	//añadimos el panel con los botones
	private JPanel crearCajaOpciones() {
		JPanel opc = new JPanel();
		opc.setLayout(new BoxLayout(opc, BoxLayout.X_AXIS)); 
		opc.setBackground(Color.BLACK);
		fixSize(opc, ANCHO_CENTRO, 45);
		
		// Creamos y añadimos los 3 botones
		btnLog = new JButton("Login");
		btnLog.setBackground(Color.GREEN);
		btnGitHub = new JButton("Login GitHub");
		btnGitHub.setBackground(Color.GREEN);
		btnReg = new JButton("Register");
		btnReg.setBackground(Color.YELLOW);
		
		opc.add(Box.createHorizontalStrut(8));	
		opc.add(btnLog);
		opc.add(Box.createHorizontalStrut(2));	
		opc.add(btnGitHub);
		opc.add(Box.createHorizontalStrut(2));	
		opc.add(btnReg);
		
		manejadorBotones();
		
		return opc;
	}
	//creamos los manejadores de los 3 botones
	private void manejadorBotones() {
		btnReg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//creamos una nueva VentanaRegistro
				VentanaRegistro registroView = new VentanaRegistro();
				registroView.mostrarVentana();
				dispose();
			}
		});
		btnLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Vemos si ya existe un usuario en la BBDD
				boolean login = controlador.loginUsuario(txtUser.getText(),
						new String(txtPassword.getText()));
				//si existe, se abre la ventana principal
				if (login) {
					VentanaPrincipal principal = new VentanaPrincipal();
					principal.mostrarVentana();
					dispose();
				} else //si no existe, se informa del error
					JOptionPane.showMessageDialog(VentanaLogin.this, "Nombre de usuario o contraseña no valido", "Error",
							JOptionPane.ERROR_MESSAGE);
			}
		});
		btnGitHub.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GitHub github;
				try {
					github = new GitHubBuilder().withPassword(txtUser.getText(), new String(txtPassword.getText())).build();
					// Si se ha podido registrar desde Github
					if (github.isCredentialValid()) {
						boolean OK = true;	
						OK = checkFields();
						if (OK) {
							// Vemos si ya existe un usuario en la BBDD
							boolean login = controlador.loginUsuario(txtUser.getText(),
									new String(txtPassword.getText()));
							if (login) {
								VentanaPrincipal principal = new VentanaPrincipal();
								principal.mostrarVentana();
								dispose();
							} 
							// Si no existe ==> Lo registramos en la BBDD apareciendo primero la ventana de registro para que rellene los datos que faltan
							else {
								VentanaRegistro registro = new VentanaRegistro(txtUser.getText(), new String(txtPassword.getText()));
								registro.mostrarVentana();
								dispose();
							}
						} else {
							JOptionPane.showMessageDialog(VentanaLogin.this, "Es necesario rellenar todos los campos para registrarse.\n",
									"Registro", JOptionPane.ERROR_MESSAGE);
						}
					}
					else {
						JOptionPane.showMessageDialog(VentanaLogin.this, "Nombre de usuario o contraseña no valido", "Error",
								JOptionPane.ERROR_MESSAGE);	
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
	}
	//comprobamos si hay algún campo vacío
	private boolean checkFields() {
		return (!txtUser.getText().equals("") && !txtPassword.getText().equals(""));
	}
	//función auxiliar para estableces dimensiones de los componentes
	private void fixSize(JComponent c, int x, int y) {
		c.setMaximumSize(new Dimension(x, y));
		c.setMinimumSize(new Dimension(x, y));
		c.setPreferredSize(new Dimension(x, y));
	}
}
