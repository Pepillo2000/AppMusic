package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class VentanaRegistroPremium extends JFrame {
	
	// Constantes
	private final int ANCHO = 400;
	private final int ALTO = 380;
	private final int ANCHO_LATERALES = 45;
	private final int ANCHO_REGISTRO = ANCHO - 2*ANCHO_LATERALES;
	private final int ALTO_ABAJO = 80;
	private final int ALTO_TOP = 50;
	private final int ALTO_REGISTRO = ALTO - ALTO_TOP - ALTO_ABAJO;
	
	// Paneles
	private JPanel contenedor, abajo, izquierda, derecha, top, datosPersonales;

	// Componentes
	private JDateChooser dateChooser;
	private JPanel panelCampoCVV, panelCamposNumTarjeta, panelCamposFechaCaducidad;
	private JLabel lblCVV, lblNumTarjeta, lblFechaCaducidad, lblPrecio;
	private JTextField txtCVV, txtNumTarjeta;
	private JButton btnPay, btnCancel;
	
	private Controlador controlador;

	public void mostrarVentana() {
		setLocationRelativeTo(null); // Coloca la ventana en el centro de la pantalla
		setVisible(true);
	}

	// Constructor para registro desde la aplicación
	public VentanaRegistroPremium() {
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
	//Inicialización de la ventana
	private void inicializarVentana() {
		controlador = Controlador.getUnicaInstancia();
		this.setSize(ANCHO, ALTO);
		this.setTitle("Payment window");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		Image icon = new ImageIcon("./img/logo.png").getImage();
		this.setIconImage(icon);
	}
	//Creamos el panel principal
	private JPanel crearPanelRegistro() {
		JPanel datos = new JPanel();
		fixSize(datos, ANCHO_REGISTRO, ALTO_REGISTRO);
		JPanel aux = new JPanel();
		fixSize(aux, ANCHO_REGISTRO - 20, ALTO_REGISTRO - 48);
		LineBorder borde = new LineBorder(Color.BLACK, 2);
		TitledBorder tBorde = BorderFactory.createTitledBorder(borde, "Payment data", TitledBorder.LEFT, TitledBorder.TOP, null, Color.BLACK);
		aux.setBorder(tBorde);
		
		double precio = 0.0;
		precio = controlador.calcularPrecioPremium();
		//añadimos la etiqueta donde se muestra el precio a pagar
		lblPrecio = new JLabel("Price to pay: " + precio + "€");
		lblPrecio.setForeground(Color.BLACK);
		aux.add(lblPrecio);
		//añadimos los demás paneles dentro
		aux.add(crearLineaNumTarjeta());
		aux.add(creaLineaCVV());
		aux.add(crearLineaFechaCaducidad());


		datos.add(aux);
		return datos;
	}

	private JPanel creaLineaCVV() {
		JPanel lineaCVV = new JPanel();
		lineaCVV.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		lineaCVV.setLayout(new BorderLayout(0, 0));

		panelCampoCVV = new JPanel();
		lineaCVV.add(panelCampoCVV, BorderLayout.CENTER);
		lblCVV = new JLabel("CVV: ", JLabel.RIGHT);
		fixSize(lblCVV, 80, 30);
		
		txtCVV = new JPasswordField();
		fixSize(txtCVV, 185, 20);
		
		panelCampoCVV.add(lblCVV);
		panelCampoCVV.add(txtCVV);

		return lineaCVV;
	}

	private JPanel crearLineaNumTarjeta() {
		JPanel lineaNumTarjeta = new JPanel();
		lineaNumTarjeta.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		lineaNumTarjeta.setLayout(new BorderLayout(0, 0));

		panelCamposNumTarjeta = new JPanel();
		lineaNumTarjeta.add(panelCamposNumTarjeta, BorderLayout.CENTER);

		lblNumTarjeta = new JLabel("Card number: ", JLabel.RIGHT);
		panelCamposNumTarjeta.add(lblNumTarjeta);
		fixSize(lblNumTarjeta, 80, 30);
		
		txtNumTarjeta = new JTextField();
		panelCamposNumTarjeta.add(txtNumTarjeta);
		fixSize(txtNumTarjeta, 185, 20);

		return lineaNumTarjeta;
	}

	private JPanel crearLineaFechaCaducidad() {
		JPanel lineaFechaCaducidad = new JPanel();
		lineaFechaCaducidad.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		lineaFechaCaducidad.setLayout(new BorderLayout(0, 0));

		panelCamposFechaCaducidad = new JPanel();
		lineaFechaCaducidad.add(panelCamposFechaCaducidad, BorderLayout.CENTER);

		lblFechaCaducidad = new JLabel("Expiration Date: ", JLabel.RIGHT);
		panelCamposFechaCaducidad.add(lblFechaCaducidad);
		fixSize(lblFechaCaducidad, 100, 30);

		dateChooser = new JDateChooser();
		dateChooser.setFont(new Font("Tahoma", Font.PLAIN, 10));
		dateChooser.setDateFormatString("dd/MM/yyyy");
		panelCamposFechaCaducidad.add(dateChooser);

		return lineaFechaCaducidad;
	}

	private JPanel crearPanelBotones() {
		JPanel comp = new JPanel();
		comp.setBackground(Color.BLACK);
		fixSize(comp, ANCHO, ALTO_ABAJO);

		btnPay = new JButton("Pay");
		btnPay.setBackground(Color.GREEN);
		fixSize(btnPay, 100, 30);

		btnCancel = new JButton("Cancel");
		btnCancel.setBackground(Color.YELLOW);
		fixSize(btnCancel, 100, 30);

		comp.add(btnPay);
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
				VentanaPrincipal mainView = new VentanaPrincipal();
				mainView.mostrarVentana();
				dispose();
			}
		});

		btnPay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean OK = true;
				OK = checkFields();
				if (OK) {
					controlador.setPremium();
					VentanaPrincipal ventanaPrincipal = new VentanaPrincipal();
					ventanaPrincipal.mostrarVentana();
					dispose();
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
	//función para comprobar que los campos son correctos
	private boolean checkFields() {
		//comprobamos que no son vacíos
		if ((txtCVV.getText().equals("") || txtNumTarjeta.getText().equals("") || (dateChooser.getDate() == null))) {
			JOptionPane.showMessageDialog(VentanaRegistroPremium.this,
					"Es necesario rellenar todos los campos para registrarse.\n", "Registro",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		//comprobamos que la tarjeta sean 16 dígitos
		if (txtNumTarjeta.getText().length() != 16) {
			JOptionPane.showMessageDialog(VentanaRegistroPremium.this, "La tarjeta debe ser un numero de 16 digitos.\n",
					"Registro", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		//comprobamos que el CVV tiene 3 dígitos
		if (txtCVV.getText().length() != 3) {
			JOptionPane.showMessageDialog(VentanaRegistroPremium.this, "El CVV debe ser un numero de 3 digitos.\n",
					"Registro", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		//comprobamos que la fecha de caducidad es posterior a la actual
		if (dateChooser.getDate().before(new Date())) {
			JOptionPane.showMessageDialog(VentanaRegistroPremium.this, "La tarjeta ha expirado.\n", "Registro",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

}

