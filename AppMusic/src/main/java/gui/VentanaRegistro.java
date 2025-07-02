package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import javax.swing.BorderFactory;
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

import com.toedter.calendar.JDateChooser;

import controller.Controlador;

public class VentanaRegistro extends JFrame {

	// Constantes
	private final int ANCHO = 400;
	private final int ALTO = 380;
	private final int ANCHO_LATERALES = 45;
	private final int ANCHO_REGISTRO = ANCHO - 2*ANCHO_LATERALES;
	private final int ALTO_TOP = 50;
	private final int ALTO_ABAJO = 80;
	private final int ALTO_REGISTRO = ALTO - ALTO_TOP - ALTO_ABAJO;
	private final int ANCHO_BOTON = 100;
	private final int ALTO_BOTON =  30;

	// Paneles
	private JPanel contenedor, abajo, izquierda, derecha, top, datosPersonales;

	// Componentes
	private JDateChooser dateChooser;
	private JPanel panelCampoNombre, panelCamposEmail, panelPassword, panelCamposFechaNacimiento;
	private JLabel lblNombre, lblEmail, lblPassword, lblFechaNacimiento;
	private JTextField txtNombre, txtEmail, txtPassword;
	private JButton btnReg, btnCancel;
	
	private Controlador controlador;

	public void mostrarVentana() {
		setLocationRelativeTo(null); // Coloca la ventana en el centro de la pantalla
		setVisible(true);
	}

	// Constructor para registro desde la aplicación
	public VentanaRegistro() {
		inicializarVentana();
		contenedor = (JPanel) this.getContentPane();
		datosPersonales = crearPanelRegistro();
		abajo = crearPanelBotones();

		izquierda = new JPanel();
		izquierda.setBackground(Color.BLACK);
		fixSize(izquierda, ANCHO_LATERALES, ALTO);

		derecha = new JPanel();
		derecha.setBackground(Color.BLACK);
		fixSize(derecha, ANCHO_LATERALES, ALTO);

		top = new JPanel();
		top.setBackground(Color.BLACK);
		fixSize(top, ANCHO, ALTO_TOP);

		contenedor.add(top, BorderLayout.NORTH);
		contenedor.add(derecha, BorderLayout.EAST);
		contenedor.add(izquierda, BorderLayout.WEST);
		contenedor.add(abajo, BorderLayout.SOUTH);
		contenedor.add(datosPersonales, BorderLayout.CENTER);
	}

	// Constructor para registro desde GitHub
	public VentanaRegistro(String nickname, String password) {
		this();
		txtNombre.setText(nickname);
		txtPassword.setText(password);
	}

	private void inicializarVentana() {
		controlador = Controlador.getUnicaInstancia();
		this.setSize(ANCHO, ALTO);
		this.setTitle("AppMusic");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		Image icon = new ImageIcon("./img/logo.png").getImage();
		this.setIconImage(icon);
	}

	private JPanel crearPanelRegistro() {
		JPanel datos = new JPanel();
		fixSize(datos, ANCHO_REGISTRO, ALTO_REGISTRO);
		JPanel aux = new JPanel();
		fixSize(aux, ANCHO_REGISTRO - 20, ALTO_REGISTRO - 48);
		LineBorder borde = new LineBorder(Color.BLACK, 2);
		TitledBorder tBorde = BorderFactory.createTitledBorder(borde, "Registration data", TitledBorder.LEFT,
				TitledBorder.TOP, null, Color.BLACK);
		aux.setBorder(tBorde);

		aux.add(crearLineaEmail());
		aux.add(creaLineaNombre());
		aux.add(crearLineaPassword());
		aux.add(crearLineaFechaNacimiento());

		datos.add(aux);
		return datos;
	}

	private JPanel creaLineaNombre() {
		JPanel lineaNombre = new JPanel();
		lineaNombre.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		lineaNombre.setLayout(new BorderLayout(0, 0));

		panelCampoNombre = new JPanel();
		lineaNombre.add(panelCampoNombre, BorderLayout.CENTER);

		lblNombre = new JLabel("Username: ", JLabel.RIGHT);
		panelCampoNombre.add(lblNombre);
		fixSize(lblNombre, 80, 30);
		txtNombre = new JTextField();
		panelCampoNombre.add(txtNombre);
		fixSize(txtNombre, 185, 20);

		return lineaNombre;
	}

	private JPanel crearLineaEmail() {
		JPanel lineaEmail = new JPanel();
		lineaEmail.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		lineaEmail.setLayout(new BorderLayout(0, 0));

		panelCamposEmail = new JPanel();
		lineaEmail.add(panelCamposEmail, BorderLayout.CENTER);

		lblEmail = new JLabel("Email: ", JLabel.RIGHT);
		panelCamposEmail.add(lblEmail);
		fixSize(lblEmail, 80, 30);
		txtEmail = new JTextField();
		panelCamposEmail.add(txtEmail);
		fixSize(txtEmail, 185, 20);

		return lineaEmail;
	}

	private JPanel crearLineaPassword() {
		JPanel lineaPassword = new JPanel();
		lineaPassword.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		lineaPassword.setLayout(new BorderLayout(0, 0));

		panelPassword = new JPanel();
		lineaPassword.add(panelPassword, BorderLayout.CENTER);

		lblPassword = new JLabel("Password: ", JLabel.RIGHT);
		panelPassword.add(lblPassword);
		fixSize(lblPassword, 80, 30);
		txtPassword = new JPasswordField();
		panelPassword.add(txtPassword);
		fixSize(txtPassword, 185, 20);

		return lineaPassword;
	}

	private JPanel crearLineaFechaNacimiento() {
		JPanel lineaFechaNacimiento = new JPanel();
		lineaFechaNacimiento.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		lineaFechaNacimiento.setLayout(new BorderLayout(0, 0));

		panelCamposFechaNacimiento = new JPanel();
		lineaFechaNacimiento.add(panelCamposFechaNacimiento, BorderLayout.CENTER);

		lblFechaNacimiento = new JLabel("Birth date: ", JLabel.RIGHT);
		panelCamposFechaNacimiento.add(lblFechaNacimiento);
		fixSize(lblFechaNacimiento, 80, 30);

		dateChooser = new JDateChooser();
		dateChooser.setFont(new Font("Tahoma", Font.PLAIN, 10));
		dateChooser.setDateFormatString("dd/MM/yyyy");
		panelCamposFechaNacimiento.add(dateChooser);

		return lineaFechaNacimiento;
	}

	private JPanel crearPanelBotones() {
		JPanel comp = new JPanel();
		comp.setBackground(Color.BLACK);
		fixSize(comp, ANCHO, ALTO_ABAJO);

		btnReg = new JButton("Register");
		btnReg.setBackground(Color.GREEN);
		fixSize(btnReg, ANCHO_BOTON, ALTO_BOTON);

		btnCancel = new JButton("Cancel");
		btnCancel.setBackground(Color.YELLOW);
		fixSize(btnCancel, ANCHO_BOTON, ALTO_BOTON);

		comp.add(btnReg);
		comp.add(btnCancel);

		manejadorBotones();

		return comp;
	}
	// ###########################################
	// ##############  MANEJADORES  ##############
	// ###########################################
	public void manejadorBotones() {
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VentanaLogin loginView = new VentanaLogin();
				loginView.mostrarVentana();
				dispose();
			}
		});

		btnReg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean OK = true;
				OK = checkFields();
				//comprobamos que los campos introducidos son correctos
				if (OK) {
					boolean registrado = false;
					DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					//comprobamos que la fecha de nacimiento es anterior a la actual
					if (dateChooser.getDate().before(new Date())){
						registrado = controlador.registrarUsuario(txtNombre.getText(), false, txtEmail.getText(),
								txtPassword.getText(), dateFormat.format(dateChooser.getDate()));
						//comprobamos que no está registrado
						if (registrado) {
							JOptionPane.showMessageDialog(VentanaRegistro.this, "Usuario registrado correctamente.",
									"Registro", JOptionPane.INFORMATION_MESSAGE);
	
							VentanaLogin login = new VentanaLogin();
							login.mostrarVentana();
							VentanaRegistro.this.dispose();
						} else {
							JOptionPane.showMessageDialog(VentanaRegistro.this,
									"No se ha podido llevar a cabo el registro.\n", "Registro", JOptionPane.ERROR_MESSAGE);
						}
					}else {
						JOptionPane.showMessageDialog(VentanaRegistro.this,
								"La fecha debe ser anterior a la actual.\n", "Registro", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(VentanaRegistro.this,
							"Es necesario rellenar todos los campos para registrarse.\n", "Registro",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	//función para asignar dimensiones a un componente
	private void fixSize(JComponent c, int x, int y) {
		c.setMinimumSize(new Dimension(x, y));
		c.setMaximumSize(new Dimension(x, y));
		c.setPreferredSize(new Dimension(x, y));
	}
	//función para comprobar que no haya campos nulos
	private boolean checkFields() {
		return (!txtNombre.getText().equals("") && !txtEmail.getText().equals("") && !txtPassword.getText().equals("")
				&& !(dateChooser.getDate() == null));
	}

}
