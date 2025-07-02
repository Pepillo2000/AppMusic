package gui;

import java.io.File;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.ResourceBundle.Control;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import pulsador.Luz;
import pulsador.IEncendidoListener;

import controller.Controlador;
import controller.Player;
import domain.Cancion;
import domain.PlayList;
import dao.DAOException;
import domain.Cancion;
import domain.RepositorioCanciones;

public class VentanaPrincipal extends JFrame {
	private JPanel contenedor, izquierda, derecha, cajaBienvenida, cajaBusqueda, cajaTitulo, cajaInterprete, cajaSearch,
			cajaTabla, cajaPlayer, cajaPlayLists;

	// Constantes para modificar los paneles
	private final int ANCHO = 550;
	private final int ALTO = 450;
	private final int TAM_BORDE = 10;
	private final int ANCHO_I = 140;
	private final int ANCHO_D = ANCHO - ANCHO_I - 2 * TAM_BORDE;
	private final int ALTO_PANELES = ALTO - 2 * TAM_BORDE;
	private final int ANCHO_BUT_I = 130;
	private final int ALTO_BUT_I = 50;
	private final int ALTO_CAJA_1 = 40;
	private final int ALTO_CAJA_2 = 120;
	private final int ANCHO_BUT_PLAYER = (ANCHO_D - 80) / 4;
	private final int ALTO_CAJA_3 = 180;
	private final int ALTO_CAJA_4 = ALTO_PANELES - ALTO_CAJA_1 - ALTO_CAJA_2 - ALTO_CAJA_3;
	private final String ESTILO_POR_DEFECTO = "Style";

	// Botones
	private JButton btnBusq, btnMas, btnRecientes, btnMyPlayLists, btnPDF, btnPremium, btnLogOut, btnSearch, btnPlay,
			btnPrev, btnNext, btnRemove, btnStop, btnMasEscuchadas;

	// Botón Luz
	private Luz pulsador;

	// Atributos para buscar canciones
	private JLabel lSaludo, lInterprete, lTitulo;
	private JTextField tTitulo, tInterprete;
	private JComboBox<String> cEstilos;
	private ArrayList<String> opciones;
	private JCheckBox popular;

	// Atributos para la tabla y la lista de playLists
	private JTable tabla;
	private JScrollPane scroll, scroll2;
	private DefaultTableModel dtm;
	private JList<String> lista;
	private DefaultListModel dlm;

	// Atributos para la reproduccion de canciones
	private boolean isPlaying = false;
	private boolean firstPlay = true;
	private String pauseURL = "/pause.png";
	private String playURL = "/play.png";

	private Controlador controlador;

	public void mostrarVentana() {
		setLocationRelativeTo(null); // Coloca la ventana en el centro de la pantalla
		setVisible(true);
	}
	
	//constructor
	public VentanaPrincipal() {
		controlador = Controlador.getUnicaInstancia();
		inicializarVentana();
		contenedor = (JPanel) this.getContentPane();
		Border border = new LineBorder(Color.BLACK, TAM_BORDE);
		contenedor.setBorder(border);

		opciones = controlador.getEstilosRepo();
		//al principio, todo será invisible
		izquierda = crearPanelIzquierda();
		derecha = crearPanelDerecha();
		contenedor.add(izquierda, BorderLayout.WEST);
		contenedor.add(derecha, BorderLayout.CENTER);
		
		manejadorBotones();
		
 	}
	
	//inicializamos la imagen que tendrá la ventana principal
	private void inicializarVentana() {
		this.setSize(ANCHO, ALTO);
		this.setTitle("AppMusic");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		Image icon = new ImageIcon("./img/logo.png").getImage();
		this.setIconImage(icon);
	}
	
	//creamos el panel izquierdo
	private JPanel crearPanelIzquierda() {
		JPanel izq = new JPanel();
		fixSize(izq, ANCHO_I, ALTO_PANELES);
		izq.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		izq.setBackground(Color.BLACK);
		//asignamos imágenes y texto a los botones
		btnBusq = createButton("/lupa.png", "Search", ANCHO_BUT_I, ALTO_BUT_I, 14);
		btnMas = createButton("/mas.png", "<html>Management <br>PlayLists</html>", ANCHO_BUT_I, ALTO_BUT_I, 14);
		btnRecientes = createButton("/recientes.png", "Recent", ANCHO_BUT_I, ALTO_BUT_I, 14);
		btnMyPlayLists = createButton("/altavoz.png", "<html>My <br>PlayLists</html>", ANCHO_BUT_I, ALTO_BUT_I, 14);
		btnPDF = createButton("/pdf.png", "Generate", ANCHO_BUT_I, ALTO_BUT_I / 2, 14);
		btnMasEscuchadas = createButton("/masEscuchadas.png", "Top Songs", ANCHO_BUT_I, ALTO_BUT_I / 2, 14);

		cajaPlayLists = new JPanel();
		fixSize(cajaPlayLists, ANCHO_BUT_I, 110);
		cajaPlayLists.setVisible(false);
		//añadimos los botones
		izq.add(btnBusq);
		izq.add(btnMas);
		izq.add(btnRecientes);
		izq.add(btnMyPlayLists);
		if (controlador.isPremium()) {	//estos botones aparecen solo si el usuario es premium
			izq.add(btnPDF);
			izq.add(btnMasEscuchadas);
		}
		izq.add(cajaPlayLists);

		return izq;
	}
	
	//creamos el panel de la derecha
	private JPanel crearPanelDerecha() {
		JPanel dcha = new JPanel();
		dcha.setBackground(Color.BLACK);
		dcha.setLayout(new BoxLayout(dcha, BoxLayout.Y_AXIS));
		fixSize(dcha, ANCHO_D, ALTO_PANELES);

		cajaBienvenida = new JPanel();
		fixSize(cajaBienvenida, ANCHO_D, ALTO_CAJA_1);
		cajaBienvenida.setBackground(Color.BLACK);
		lSaludo = new JLabel("Welcome user " + controlador.getUsuarioActual().getNickname());
		lSaludo.setForeground(Color.GREEN);
		btnPremium = new JButton("Premium");
		btnPremium.setBackground(Color.YELLOW);
		btnLogOut = new JButton("LogOut");
		btnLogOut.setBackground(Color.YELLOW);

		// Crear botón pulsador
		pulsador = new Luz();
		pulsador.setColor(Color.YELLOW);

		cajaBienvenida.add(lSaludo);
		//incluimos el botón premium solo si el usuario no es premium
		if (!controlador.isPremium()) {
			cajaBienvenida.add(btnPremium);
		}
		cajaBienvenida.add(btnLogOut);
		cajaBienvenida.add(pulsador);
		
		cajaBusqueda = new JPanel();
		fixSize(cajaBusqueda, ANCHO_D, ALTO_CAJA_2);
		cajaBusqueda.setVisible(false);
		cajaBusqueda.setOpaque(false);
		cajaBusqueda.setLayout(new BoxLayout(cajaBusqueda, BoxLayout.Y_AXIS));
		manejadorBusqueda();

		cajaTabla = new JPanel();
		fixSize(cajaTabla, ANCHO_D, ALTO_CAJA_3);
		cajaTabla.setVisible(false);
		cajaTabla.setOpaque(false);
		crearCajaTabla();

		cajaPlayer = new JPanel();
		fixSize(cajaPlayer, ANCHO_D, ALTO_CAJA_4);
		cajaPlayer.setVisible(false);
		cajaPlayer.setOpaque(false);
		crearCajaPlayer();

		dcha.add(cajaBienvenida);
		dcha.add(cajaBusqueda);
		dcha.add(cajaTabla);
		dcha.add(cajaPlayer);
		return dcha;
	}
	
	//código auxiliar para crear un botón de unas características concretas
	private JButton createButton(String imageURL, String text, int width, int height, int font_size) {
		JButton newboton;
		if (text.equals(""))
			newboton = new JButton();
		else
			newboton = new JButton(text);
		fixSize(newboton, width, height);
		newboton.setIcon(new ImageIcon(getClass().getResource(imageURL)));
		if (!text.equals(""))
			newboton.setFont(new Font("Arial", Font.PLAIN, font_size));
		newboton.setBackground(Color.GREEN);
		return newboton;
	}
	
	//código para realizar la búsqueda de canciones
	private void manejadorBusqueda() {
		LineBorder borde = new LineBorder(Color.GREEN, 2);
		TitledBorder tBorde = BorderFactory.createTitledBorder(borde, "Search", TitledBorder.LEFT, TitledBorder.TOP,
				null, Color.GREEN);
		cajaBusqueda.setBorder(tBorde);

		cajaTitulo = new JPanel();
		fixSize(cajaTitulo, ANCHO_D, 30);
		cajaTitulo.setOpaque(false);

		lTitulo = new JLabel("Title: ", JLabel.RIGHT); // Alinear a la derecha EL TEXTO (etiqueta sigue teniendo el
														// mismo tamaño)
		fixSize(lTitulo, 80, 20);
		lTitulo.setForeground(Color.GREEN);
		tTitulo = new JTextField();
		fixSize(tTitulo, 180, 20);
		cajaTitulo.add(lTitulo);
		cajaTitulo.add(tTitulo);

		cajaInterprete = new JPanel();
		fixSize(cajaInterprete, ANCHO_D, 30);
		cajaInterprete.setOpaque(false);

		lInterprete = new JLabel("Performer: ", JLabel.RIGHT); // Alinear a la derecha EL TEXTO (etiqueta sigue
																// teniendo el mismo tamaño)
		fixSize(lInterprete, 80, 20);
		lInterprete.setForeground(Color.GREEN);
		tInterprete = new JTextField();
		fixSize(tInterprete, 180, 20);
		cajaInterprete.add(lInterprete);
		cajaInterprete.add(tInterprete);

		cajaSearch = new JPanel();
		fixSize(cajaSearch, ANCHO_D, 30);
		cajaSearch.setOpaque(false);

		cEstilos = new JComboBox<String>();
		fixSize(cEstilos, 120, 20);
		//INTRODUCIMOS LOS ESTILOS, OBTENIENDOLOS DEL REPOSITORIO, EN LA COMBOBOX
		getAllEstilos();
		popular = new JCheckBox("Popular");
		popular.setOpaque(false);
		popular.setForeground(Color.GREEN);
		fixSize(popular, 90, 20);

		btnSearch = new JButton("Search");
		btnSearch.setBackground(Color.YELLOW);
		fixSize(btnSearch, 80, 25);

		cajaSearch.add(popular, BorderLayout.WEST);
		cajaSearch.add(cEstilos, BorderLayout.CENTER);
		cajaSearch.add(btnSearch, BorderLayout.EAST);

		cajaBusqueda.add(cajaTitulo);
		cajaBusqueda.add(cajaInterprete);
		cajaBusqueda.add(cajaSearch);

	}
	
	//código para crear la tabla, con las columnas correspondientes
	private void crearCajaTabla() {
		// Tabla
		tabla = new JTable();
		String[] columnas = { "Song", "Performer", "Style"};
		dtm = new DefaultTableModel(null, columnas) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tabla.setModel(dtm);

		tabla.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // No seleccion multiple
		tabla.addMouseListener(new MouseAdapter() {
			@Override	//si se clicka 2 veces sobre una canción, este se selecciona
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int row = tabla.getSelectedRow();
					getSelectedSong(row);
				}
			}
		});

		scroll = new JScrollPane(tabla);
		fixSize(scroll, ANCHO_D - 10, ALTO_CAJA_3 - 10);
		cajaTabla.add(scroll);
	}
	
	//caja donde podemos reproducir canciones
	private void crearCajaPlayer() {
		JPanel botones = new JPanel();
		fixSize(botones, ANCHO_D, ALTO_CAJA_4 - 40);
		botones.setOpaque(false);
		botones.setLayout(new BoxLayout(botones, BoxLayout.X_AXIS));
		//creamos los botones
		btnPrev = createButton("/anterior.png", "", ANCHO_BUT_PLAYER, 35, 0);
		btnPlay = createButton("/play.png", "", ANCHO_BUT_PLAYER, 35, 0);
		btnNext = createButton("/siguiente.png", "", ANCHO_BUT_PLAYER, 35, 0);
		btnStop = createButton("/stop.png", "", ANCHO_BUT_PLAYER, 35, 0);
		
		botones.add(btnPrev);
		botones.add(btnStop);
		botones.add(btnPlay);
		botones.add(btnNext);
		botones.add(Box.createVerticalStrut(1));
		btnRemove = new JButton("<html>Remove <br>playlist</html>");
		fixSize(btnRemove, 78, 35);
		btnRemove.setBackground(Color.YELLOW);
		btnRemove.setVisible(false);
		botones.add(btnRemove);
		cajaPlayer.add(botones);
	}
	
	//código para crear la lista de playLists del usuario
	private void crearListaPlaylists() {
		List<PlayList> playlists = controlador.getPlayLists();

		dlm = new DefaultListModel();

		for (PlayList p : playlists)
			dlm.addElement(p.getNombre());

		lista = new JList<>(dlm);

		lista.setLayoutOrientation(JList.VERTICAL);

		scroll2 = new JScrollPane(lista);

		scroll2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		fixSize(scroll2, ANCHO_BUT_I, 110);

		cajaPlayLists.add(scroll2);
	}
	
	// ###########################################
	// ##############  MANEJADORES  ##############
	// ###########################################
	private void manejadorBotones() {
		btnPremium.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				VentanaRegistroPremium regPremium = new VentanaRegistroPremium();
				regPremium.mostrarVentana();
				dispose();
			}
		});
		btnLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controlador.logout();
				VentanaLogin loginView = new VentanaLogin();
				loginView.mostrarVentana();
				dispose();
			}
		});
		btnBusq.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cajaBusqueda.setVisible(true);
				cajaTabla.setVisible(true);
				cajaPlayer.setVisible(true);
			}
		});
		//botón para añadir un xml con canciones
		pulsador.addEncendidoListener(new IEncendidoListener() {
			@Override
			public void enteradoCambioEncendido(EventObject e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setDialogTitle("Choose a xml file");
				int res = chooser.showOpenDialog(pulsador);
				// Comprobamos si se ha seleccionado un fichero
				if (res == JFileChooser.APPROVE_OPTION) {
					File ficheroXML = chooser.getSelectedFile();
					if (ficheroXML.getAbsolutePath().contains(".xml")) {
						try {
							//cargamos las canciones 
							controlador.cargarCanciones(ficheroXML);
							//obtenemos todos los estilos diferentes presentes en ellas
							opciones = controlador.getEstilosRepo();
							getAllEstilos();
						} catch (Exception e2) {
							JOptionPane.showMessageDialog(VentanaPrincipal.this, "Error al cargar las canciones",
									"Error", JOptionPane.ERROR_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(VentanaPrincipal.this, "El fichero elegido no es un xml", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}

			}
		});
		//botón que obtiene las canciones favoritas
		popular.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (popular.isSelected()) {
					dtm.setRowCount(0);
					cajaTabla.setVisible(true);
					cajaPlayer.setVisible(true);
					List<Cancion> populares = controlador.getCancionesFavoritas();
					for (Cancion c : populares) {
						Object[] fila = { c.getTitulo(), c.getInterprete(), c.getEstilo() };
						dtm.addRow(fila);
					}
				}
			}
		});
		//botón para abrir la ventana de gestión de playLists
		btnMas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VentanaGestionPlayLists loginGPL = new VentanaGestionPlayLists();
				loginGPL.mostrarVentana();
				dispose();
			}
		});
		//botón para mostrar las canciones recientes del usuario
		btnRecientes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dtm.setRowCount(0);		//vacía la tabla
				cajaTabla.setVisible(true);
				cajaPlayer.setVisible(true);
				//controlador obtiene recientes del usuario actual
				List<Cancion> recientes = controlador.getCancionesRecientes();
				for (Cancion c : recientes) {
					Object[] fila = { c.getTitulo(), c.getInterprete(), c.getEstilo() };
					dtm.addRow(fila);
				}
			}
		});
		//botón para obtener las playLists de un usuario
		btnMyPlayLists.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cajaPlayLists.setVisible(true);
				cajaTabla.setVisible(true);
				cajaPlayer.setVisible(true);
				btnRemove.setVisible(true);
				crearListaPlaylists();
				lista.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (lista.getSelectedIndex() != -1) {
							if (e.getClickCount() == 2) {
								dtm.setRowCount(0);
								//obtiene las canciones de la playList seleccionada y las muestra en la tabla
								List<Cancion> cancionesPlayList = controlador
										.getCancionesPlayList(lista.getSelectedValue());
								for (Cancion c : cancionesPlayList) {
									Object[] fila = { c.getTitulo(), c.getInterprete(), c.getEstilo() };
									dtm.addRow(fila);
								}
							}
						}
					}
				});

			}
		});
		//botón para buscar las canciones
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dtm.setRowCount(0); // Para que se vacíe la tabla cada vez que le das al boton search

				String titulo = tTitulo.getText();
				String interprete = tInterprete.getText();
				String estilo = (String) cEstilos.getSelectedItem();
			
				//comprueba si titulo e intérprete están vacíos
				if (titulo.isEmpty() && interprete.isEmpty() && estilo.equals("Style"))
					//si lo están, muestra un error
					JOptionPane.showMessageDialog(VentanaPrincipal.this,	
							"No ha introducido ningun criterio de busqueda", "Error", JOptionPane.ERROR_MESSAGE);
				else {
					//busca las canciones que cumplan los criterios
					//delegando en el controlador la tarea
					List<Cancion> cancionesFiltradas = controlador.getCancionesFiltradas(titulo, interprete, estilo);
					//si la lista de canciones devuelta está vacía, se informa
					if (cancionesFiltradas.isEmpty())
						JOptionPane.showMessageDialog(VentanaPrincipal.this, "No se han encontrado canciones", "Error",
								JOptionPane.ERROR_MESSAGE);
					else {	//si no, se muestran las canciones
						for (Cancion c : cancionesFiltradas) {
							Object[] fila = { c.getTitulo(), c.getInterprete(), c.getEstilo() };
							dtm.addRow(fila);
						}
					}
				}
			}
		});
		//botón para reproducir y pausar la canción actual
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//si no está reproduciéndose, comienza
				if (!isPlaying) {
					btnPlay.setIcon(new ImageIcon(getClass().getResource(pauseURL)));
					controlador.reproducirCancion();
					isPlaying = true;
				} else {	//si lo está, se para
					setDefaultSearch();
					controlador.pausarCancion();
				}
			}
		});
		//botón para parar una canción
		btnStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controlador.stopCancion();
				setDefaultSearch();
			}
		});
		//botón para pasar a la siguiente canción a la seleccionada en una tabla
		btnNext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = tabla.getSelectedRow();
				if (row != -1) {	//comprueba que haya alguna seleccionada
					//busca la canción siguiente y la establece como actual
					if (row < tabla.getRowCount() - 1) {
						getSelectedSong(row + 1);
						tabla.setRowSelectionInterval(row + 1, row + 1);
					} else { // si estoy en la última de la lista, vuelvo a la primera
						getSelectedSong(0);
						tabla.setRowSelectionInterval(0, 0);
					}
					//establece el play en el icono del botón play
					setDefaultSearch();
				}
			}
		});
		//botón para pasar a la anterior canción a la seleccionada en una tabla
		btnPrev.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = tabla.getSelectedRow();
				if (row != -1) {	//comprueba que haya alguna seleccionada
					//busca la canción anterior y la establece como actual
					if (row > 0) {
						getSelectedSong(row - 1);
						tabla.setRowSelectionInterval(row - 1, row - 1);
					} else { // si estoy en la primera, paso a la última
						getSelectedSong(tabla.getRowCount()-1);
						tabla.setRowSelectionInterval(tabla.getRowCount()-1, tabla.getRowCount()-1);
					}
					//establece el play en el icono del botón play
					setDefaultSearch();
				}
			}
		});
		//botón para borrar una playList seleccionada
		btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = lista.getSelectedIndex();
				if (row != -1) {
					int respuesta = JOptionPane.showConfirmDialog(VentanaPrincipal.this,
							"¿Esta seguro de que desea borrar la playList?", "Confirmacion", JOptionPane.YES_NO_OPTION);
					if (respuesta == JOptionPane.YES_OPTION) {
						controlador.borrarPlayList(lista.getSelectedValue());
						dlm.remove(row);
						dtm.setRowCount(0);
					}
				}
			}
		});
		//botón para generar un pdf con la información de las playLists
		btnPDF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					controlador.generarPDF();
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(VentanaPrincipal.this, "No se ha podido generar el PDF", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		//botón para obtener las canciones más escuchadas por todos los usuarios
		btnMasEscuchadas.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//delega en controlador la tarea 
				List<Cancion> masEscuchadas = controlador.getCancionesMasEscuchadas();
				String[] columnas = { "Song", "Performer", "Style", "Views" };
				dtm.setRowCount(0);
				dtm = new DefaultTableModel(null, columnas) {
					@Override
					public boolean isCellEditable(int row, int column) {
						return false;
					}
				};
				tabla.setModel(dtm);	//la tabla ahora tiene cuatro columnas
				cajaTabla.setVisible(true);
				cajaPlayer.setVisible(true);
				for (Cancion c : masEscuchadas) {
					Object[] fila = { c.getTitulo(), c.getInterprete(), c.getEstilo(), c.getNumReproducciones() };
					dtm.addRow(fila);
				}
			}
		});
	}
	
	//función para seleccionar una canción como actual, parando el reproductor 
	private void getSelectedSong(int row) {
		String titulo = tabla.getModel().getValueAt(row, 0).toString();
		String interprete = tabla.getModel().getValueAt(row, 1).toString();
		List<Cancion> canciones = RepositorioCanciones.getUnicaInstancia().filtrarCanciones(titulo, interprete,
				"Style");
		controlador.setCancionActual(canciones.get(0));
		controlador.stopCancion();
		setDefaultSearch();
	}
	
	//función auxiliar que establece el icono de play en el botón de reproducir
	private void setDefaultSearch() {
		btnPlay.setIcon(new ImageIcon(getClass().getResource(playURL)));
		isPlaying = false;
	}
	
	//función para asignar dimensiones a un componente
	private void fixSize(JComponent c, int x, int y) {
		c.setMinimumSize(new Dimension(x, y));
		c.setMaximumSize(new Dimension(x, y));
		c.setPreferredSize(new Dimension(x, y));
	}
	
	//función para obtener todos los estilos entre las canciones del repositorio
	private void getAllEstilos() {
		cEstilos.removeAllItems();
		cEstilos.addItem(ESTILO_POR_DEFECTO);	//estilo por defecto
		if (opciones != null) {
			for (String item : opciones) {
				cEstilos.addItem(item);	//los añadimos al ComboBox
			}
		}
	}
}
