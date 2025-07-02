package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import controller.Controlador;
import domain.Cancion;
import domain.PlayList;

public class VentanaGestionPlayLists extends JFrame {

	// Paneles
	private JPanel contenedor, cajaBuscador, cajaTabla1, cajaTabla2, cajaCrear, cajaTitulo, cajaInterprete, cajaEstilos;

	// Botones
	private JButton btnSearch, btnAdd, btnRemove, btnCreate, btnCancel, btnView, btnClean;

	// Constantes
	private final int ANCHO = 440;
	private final int ALTO = 430;
	private final int TAM_BORDE = 10;
	private final int ANCHO_PANEL = ANCHO - 2 * TAM_BORDE;
	private final int ALTO_BUT = 25;
	private final int ANCHO_TABLA = 300;
	private final int ALTO_TABLA = 120;
	private final int ALTO_BUSCADOR = 100;
	private final int ALTO_CREAR = 30;

	// AtriBUTos para buscar canciones
	private JLabel lName, lInterprete, lTitulo;
	private JTextField tName, tTitulo, tInterprete;
	private JComboBox<String> cEstilos, cMyPlayLists;
	private String opciones[] = { "Style", "Folk", "Cabaret", "Rock-sinfonico", "Jazz", "Ballard", "Tango", "Otaku" };

	// AtriBUTos para las tabla
	private JTable tabla1, tabla2;
	private JScrollPane scroll1, scroll2;
	private DefaultTableModel dtm1, dtm2;

	private List<Cancion> cancionesFiltradas;
	private List<Cancion> cancionesPlayList;

	private Controlador controlador;

	public void mostrarVentana() {
		setLocationRelativeTo(null); // Coloca la ventana en el centro de la pantalla
		setVisible(true);
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
	//constructor
	public VentanaGestionPlayLists() {
		inicializarVentana();
		contenedor = (JPanel) this.getContentPane();
		Border border = new LineBorder(Color.BLACK, TAM_BORDE);
		contenedor.setBorder(border);
		contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
		contenedor.setBackground(Color.BLACK);
		//añadimos las cajas
		cajaBuscador = new JPanel();
		fixSize(cajaBuscador, ANCHO_PANEL, ALTO_BUSCADOR);
		cajaBuscador.setOpaque(false);
		crearBusqueda();

		cajaTabla1 = new JPanel();
		fixSize(cajaTabla1, ANCHO_PANEL, ALTO_TABLA);
		cajaTabla1.setOpaque(false);
		crearCaja1();

		cajaTabla2 = new JPanel();
		fixSize(cajaTabla2, ANCHO_PANEL, ALTO_TABLA);
		cajaTabla2.setOpaque(false);
		crearCaja2();

		cajaCrear = new JPanel();
		fixSize(cajaCrear, ANCHO_PANEL, ALTO_CREAR);
		cajaCrear.setOpaque(false);
		crearCrearPL();

		contenedor.add(cajaBuscador);
		contenedor.add(cajaTabla1);
		contenedor.add(cajaTabla2);
		contenedor.add(cajaCrear);

		manejadorBotones();

		cancionesPlayList = new LinkedList<Cancion>();
	}
	//función para crear la caja de usaremos para buscar canciones, y playlists de un usuario
	private void crearBusqueda() {
		cajaTitulo = new JPanel();
		fixSize(cajaTitulo, ANCHO_PANEL, 25);
		cajaTitulo.setOpaque(false);

		lTitulo = new JLabel("Title: ", JLabel.RIGHT);
		fixSize(lTitulo, 80, 20);
		lTitulo.setForeground(Color.GREEN);
		tTitulo = new JTextField();
		fixSize(tTitulo, 180, 20);
		cajaTitulo.add(lTitulo);
		cajaTitulo.add(tTitulo);

		cajaInterprete = new JPanel();
		fixSize(cajaInterprete, ANCHO_PANEL, 25);
		cajaInterprete.setOpaque(false);

		lInterprete = new JLabel("Performer: ", JLabel.RIGHT);
		fixSize(lInterprete, 80, 20);
		lInterprete.setForeground(Color.GREEN);
		tInterprete = new JTextField();
		fixSize(tInterprete, 180, 20);
		cajaInterprete.add(lInterprete);
		cajaInterprete.add(tInterprete);

		cajaEstilos = new JPanel();
		fixSize(cajaEstilos, ANCHO_PANEL, 30);
		cajaEstilos.setOpaque(false);

		cMyPlayLists = new JComboBox<String>();
		fixSize(cMyPlayLists, 120, 20);
		cMyPlayLists.removeAllItems();
		cMyPlayLists.addItem("My playLists");
		for (PlayList p : controlador.getPlayLists()) {
			cMyPlayLists.addItem(p.getNombre());
		}

		btnView = crearBoton("View", 70);

		cEstilos = new JComboBox<String>();
		fixSize(cEstilos, 120, 20);
		cEstilos.removeAllItems();
		for (String item : opciones) {
			cEstilos.addItem(item);
		}

		btnSearch = crearBoton("Search", 80);

		cajaEstilos.add(cMyPlayLists);
		cajaEstilos.add(btnView);
		cajaEstilos.add(cEstilos);
		cajaEstilos.add(btnSearch);

		cajaBuscador.add(cajaTitulo);
		cajaBuscador.add(cajaInterprete);
		cajaBuscador.add(cajaEstilos);
	}
	//creamos la primera tabla, con las canciones buscadas
	private void crearCaja1() {
		tabla1 = new JTable();
		String[] columnas = { "Song", "Performer", "Style" };
		dtm1 = new DefaultTableModel(null, columnas) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tabla1.setModel(dtm1);

		tabla1.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		scroll1 = new JScrollPane(tabla1);
		fixSize(scroll1, ANCHO_TABLA, ALTO_TABLA);

		cajaTabla1.add(scroll1, BorderLayout.WEST);

		JPanel aux = new JPanel();
		fixSize(aux, 80, ALTO_TABLA);
		aux.setOpaque(false);

		btnAdd = crearBoton("Add", 80);
		aux.add(btnAdd);

		cajaTabla1.add(aux, BorderLayout.EAST);
	}
	//creamos la segunda tabla, con las canciones de una playList concreta 
	//para gestionar sus canciones
	private void crearCaja2() {
		tabla2 = new JTable();
		String[] columnas = { "Song", "Performer", "Style" };
		dtm2 = new DefaultTableModel(null, columnas) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tabla2.setModel(dtm2);

		tabla2.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		scroll2 = new JScrollPane(tabla2);
		fixSize(scroll2, ANCHO_TABLA, ALTO_TABLA);

		cajaTabla2.add(scroll2, BorderLayout.WEST);

		JPanel aux = new JPanel();
		fixSize(aux, 80, ALTO_TABLA);
		aux.setOpaque(false);

		btnRemove = crearBoton("Remove", 80);
		btnClean = crearBoton("Clean", 80);
		aux.add(btnRemove);
		aux.add(btnClean);

		cajaTabla2.add(aux, BorderLayout.EAST);
	}
	//caja de abajo, para confirmar e introducir el nombre de la playList nueva
	private void crearCrearPL() {
		cajaCrear.setLayout(new BoxLayout(cajaCrear, BoxLayout.X_AXIS));

		lName = new JLabel("Playlist's name: ", JLabel.RIGHT);
		fixSize(lName, 100, 20);
		lName.setForeground(Color.GREEN);
		tName = new JTextField();
		fixSize(tName, 140, 20);
		cajaCrear.add(lName);
		cajaCrear.add(tName);

		btnCreate = crearBoton("Create", 80);
		btnCancel = crearBoton("Finish", 70);
		cajaCrear.add(Box.createVerticalStrut(1));
		cajaCrear.add(btnCreate);
		cajaCrear.add(Box.createVerticalStrut(1));
		cajaCrear.add(btnCancel);
	}
	//función auxiliar para crear un botón
	private JButton crearBoton(String msg, int ancho) {
		JButton boton = new JButton(msg);
		boton.setBackground(Color.YELLOW);
		fixSize(boton, ancho, ALTO_BUT);
		return boton;
	}
	// ###########################################
	// ##############  MANEJADORES  ##############
	// ###########################################
	private void manejadorBotones() {
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dtm1.setRowCount(0); // Para que se vacíe la tabla cada vez que le das al boton search

				String titulo = tTitulo.getText();
				String interprete = tInterprete.getText();
				String estilo = (String) cEstilos.getSelectedItem();

				if (titulo.isEmpty() && interprete.isEmpty() && estilo.equals("Style"))
					JOptionPane.showMessageDialog(VentanaGestionPlayLists.this,
							"No ha introducido ningun criterio de busqueda", "Error", JOptionPane.ERROR_MESSAGE);
				else {
					cancionesFiltradas = controlador.getCancionesFiltradas(titulo, interprete, estilo);
					if (cancionesFiltradas.isEmpty())
						JOptionPane.showMessageDialog(VentanaGestionPlayLists.this, "No se han encontrado canciones",
								"Error", JOptionPane.ERROR_MESSAGE);
					else {
						for (Cancion c : cancionesFiltradas) {
							Object[] fila = { c.getTitulo(), c.getInterprete(), c.getEstilo() };
							dtm1.addRow(fila);
						}
					}
				}
			}
		});
		//botón para añadir una canción a la playList seleccionada
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = tabla1.getSelectedRow();
				if (row == -1) {
					JOptionPane.showMessageDialog(VentanaGestionPlayLists.this, "No se ha seleccionado cancion",
							"Error", JOptionPane.ERROR_MESSAGE);
				} else {
					Cancion c = cancionesFiltradas.get(row);
					if (cancionesPlayList.contains(c)) {
						JOptionPane.showMessageDialog(VentanaGestionPlayLists.this,
								"Esta cancion ya forma parte de la PlayList", "Error", JOptionPane.ERROR_MESSAGE);
					} else {
						Object[] fila = { c.getTitulo(), c.getInterprete(), c.getEstilo() };
						dtm2.addRow(fila);
						cancionesPlayList.add(c);
					}
				}
			}
		});
		//botón para borrar una canción de la playList seleccionada
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = tabla2.getSelectedRow();
				if (row == -1) {
					JOptionPane.showMessageDialog(VentanaGestionPlayLists.this, "No se ha seleccionado cancion",
							"Error", JOptionPane.ERROR_MESSAGE);
				} else {
					dtm2.removeRow(row);
					cancionesPlayList.remove(row);
				}
			}
		});
		//botón para dejar la segunda tabla vacía
		btnClean.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dtm2.setRowCount(0);
				cancionesPlayList = new LinkedList<>();
			}
		});
		//botón para crear una playList
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nombre = tName.getText();
				if (nombre.isEmpty()) {	//comprobación de que se ha introducido un nombre
					JOptionPane.showMessageDialog(VentanaGestionPlayLists.this, "La playList debe tener un nombre",
							"Error", JOptionPane.ERROR_MESSAGE);
				} else {
					boolean nuevaPL = false;
					int respuesta;
					//comprueba si se ha añadido alguna canción a la playlist
					if (cancionesPlayList.isEmpty()) {
						JOptionPane.showMessageDialog(VentanaGestionPlayLists.this, "Va a crear una playList vacía",
								"Lista Vacía", JOptionPane.WARNING_MESSAGE);
					}
					// Primero vemos si existe una playList con el mismo título
					// Si sí ==> Avisamos de que la playList se va a modificar
					if (controlador.existePlayList(nombre)) {
						JOptionPane.showMessageDialog(VentanaGestionPlayLists.this,
								"Ya existe una playList con ese nombre", "Lista Existente",
								JOptionPane.WARNING_MESSAGE);
						respuesta = JOptionPane.showConfirmDialog(VentanaGestionPlayLists.this,
								"¿Esta seguro de que desea modificar la playList?", "Modificacion", JOptionPane.YES_NO_OPTION);
					}
					// Si no ==> Preguntamos si se quiere crear
					else {
						respuesta = JOptionPane.showConfirmDialog(VentanaGestionPlayLists.this,
								"¿Esta seguro de que desea crear la playList?", "Creacion", JOptionPane.YES_NO_OPTION);
						nuevaPL = true;
					}
					// Si la respuesta es afirmativa (creacion o modificacion, se delega al controlador el resto)
					if (respuesta == JOptionPane.YES_OPTION) {
						controlador.registrarPlayList(cancionesPlayList, nombre);
					}
					// Además si es una nueva PL, se añade al ComboBox de My PlayLists
					if (nuevaPL) {
						cMyPlayLists.addItem(nombre);
					}
				}
			}
		});
		//botón para visualizar en la segunda tabla las canciones de la playList seleccionada
		btnView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String playList = (String) cMyPlayLists.getSelectedItem();
				if (playList.equals("My playLists")) {
					JOptionPane.showMessageDialog(VentanaGestionPlayLists.this, "No se ha seleccionado playList",
							"Error", JOptionPane.ERROR_MESSAGE);
				} else { 
					tName.setText(playList);
					dtm1.setRowCount(0);
					dtm2.setRowCount(0);
					cancionesPlayList = controlador.getCancionesPlayList(playList);
					for (Cancion c : cancionesPlayList) {
						Object[] fila = { c.getTitulo(), c.getInterprete(), c.getEstilo() };
						dtm2.addRow(fila);
					}
				}
			}
		});
		//botón para cancelar
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VentanaPrincipal principalView = new VentanaPrincipal();
				principalView.mostrarVentana();
				dispose();
			}
		});
	}
	//función para asignar dimensiones a un componente
	private void fixSize(JComponent c, int x, int y) {
		c.setMinimumSize(new Dimension(x, y));
		c.setMaximumSize(new Dimension(x, y));
		c.setPreferredSize(new Dimension(x, y));
	}

}
