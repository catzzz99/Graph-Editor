import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;


public class GraphEditor extends JFrame implements ActionListener{

	private static final long serialVersionUID = 508317535368185508L;
	
	private static final String APP_TITLE = "Graph Editor";
	private static final String APP_INFO =
		    "Aby wy�wietli� graf przyk�adowy:\n"
		  + "Menu \"Plik\" => \"Poka� przyk�ad\"\n"
		  + "(powoduje usuni�cie obecnego wy�wietlanego grafu).\n\n"
		  + "Obs�uga myszy:\n"
		  + "(akcja zale�na od po�o�enia kursora):\n"
		  + "- LPM - przesuwanie ca�ego grafu/wybranego w�z�a/wybranej kraw�dzi\n"
		  + "- PPM - menu kontekstowe og�lne/wybranego w�z�a/wybranej kraw�dzi\n\n"
		  + "Skr�ty klawiszowe:\n"
		  + "- \"q\" - w��cz/wy��cz siatk� pomocnicz�\n"
		  + "- \"z\" - dodaj w�z� w miejscu kursora\n"
		  + "- \"x\" - dodaj kraw�d� wychodz�c� z w�z�a b�d�cego pod kursorem\n"
		  + "- Alt + \"LITERA\" - otwiera menu odpowiadaj�ce literze podkre�lonej\n"
		  + "                   na pasku menu\n"
		  + "- Ctrl + \"s\" - zapisz graf do pliku\n"
		  + "- Ctrl + \"o\" - wczytaj graf z pliku\n\n"
		  + "Gdy kursor znajduje si� nad w�z�em/kraw�dzi� specjaln�\n"
		  + "- \"r\", \"g\", \"b\" - ustaw kolor czerwony/zielony/niebieski\n"
		  + "- \"+\" - zwi�ksz promie� w�z�a/grubo�� kraw�dzi\n"
		  + "- \"-\" - zmiejsz promie� w�z�a/grubo�� kraw�dzi\n"
		  + "\n\n Program posiada funkcj� autozapisu i autowczytania grafu.";
	
	
	private static final String AUTHOR_INFO =   
			  "Program do edycji graf�w.\n"
			+ "Program:  " + APP_TITLE + "\n" 
			+ "Autor:    Micha� Tkacz \n"
			+ "Data:     listopad 2019 r.";
	
	private static final String AUTOSAVE_FILE = "AUTOSAVE.bin";
	
	WindowAdapter windowListener = new WindowAdapter() {
		@Override
		public void windowClosed(WindowEvent e) {
			// metoda dispose()
			JOptionPane.showMessageDialog(null, "Program zako�czy� dzia�anie!");
		}

		@Override
		public void windowClosing(WindowEvent e) {
			// krzy�yk
			windowClosed(e);
		}

	};
	

	public static void main(String[] args) {
		new GraphEditor();

	}

	GraphPanel graphPanel = new GraphPanel(null);
	
	JMenuBar menuBar = new JMenuBar();
	
	JMenu fileMenu = new JMenu("Plik");
	JMenuItem newGraphMenuItem = new JMenuItem("Nowy graf", KeyEvent.VK_N);
	JMenuItem showExampleMenuItem = new JMenuItem("Poka� przyk�ad", KeyEvent.VK_P);
	JMenuItem saveMenuItem = new JMenuItem("Zapisz graf", KeyEvent.VK_Z);
	JMenuItem loadMenuItem = new JMenuItem("Wczytaj graf", KeyEvent.VK_W);
	
	JMenu graphMenu = new JMenu("Graf");
	JMenuItem nodesMenuItem = new JMenuItem("Lista w�z��w grafu", KeyEvent.VK_W);
	JMenuItem edgesMenuItem = new JMenuItem("Lista kraw�dzi grafu", KeyEvent.VK_K);
	
	JMenu helpMenu = new JMenu("Pomoc");
	JMenuItem drawGridCheckBoxMenuItem = new JCheckBoxMenuItem("Rysuj siatk�");
	JMenuItem appMenuItem = new JMenuItem("O programie", KeyEvent.VK_P);
	JMenuItem authorMenuItem = new JMenuItem("O autorze", KeyEvent.VK_A);
	
	private GraphEditor() {
		super(APP_TITLE);
		setSize(800, 600);
		setResizable(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setContentPane(graphPanel);
		UIManager.put("OptionPane.messageFont", new Font("Monospaced", Font.BOLD, 12));
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent event) {
				graphPanel.serializeGraph(AUTOSAVE_FILE);
			}

			@Override
			public void windowClosing(WindowEvent event) {
				windowClosed(event);
			}
		});
		
		addActionListeners();
		createMenuBar();	
		
		setVisible(true);
		
		showInstruction();

		graphPanel.deserializeGraph(AUTOSAVE_FILE);
	
	}
	
	private void addActionListeners() {
		newGraphMenuItem.addActionListener(this);
		showExampleMenuItem.addActionListener(this);
		saveMenuItem.addActionListener(this);
		loadMenuItem.addActionListener(this);
		nodesMenuItem.addActionListener(this);
		edgesMenuItem.addActionListener(this);
		drawGridCheckBoxMenuItem.addActionListener(this);
		appMenuItem.addActionListener(this);
		authorMenuItem.addActionListener(this);
	}
	
	private void createMenuBar() {
		fileMenu.setMnemonic(KeyEvent.VK_P);
		fileMenu.add(newGraphMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(showExampleMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(saveMenuItem);
		fileMenu.add(loadMenuItem);
		menuBar.add(fileMenu);
		
		graphMenu.setMnemonic(KeyEvent.VK_G);
		graphMenu.add(nodesMenuItem);
		graphMenu.add(edgesMenuItem);
		menuBar.add(graphMenu);
		
		helpMenu.setMnemonic(KeyEvent.VK_O);
		helpMenu.add(drawGridCheckBoxMenuItem);
		helpMenu.addSeparator();
		helpMenu.add(appMenuItem);
		helpMenu.add(authorMenuItem);
		menuBar.add(helpMenu);
		
		setJMenuBar(menuBar);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object eSource = e.getSource();
		
		if(eSource == newGraphMenuItem) {
			graphPanel.createNewGraph();
		}
		
		if(eSource == showExampleMenuItem) {
			graphPanel.showExampleGraph();
		}
		
		if(eSource == saveMenuItem) {
			graphPanel.serializeGraph();
		}
		
		if(eSource == loadMenuItem) {
			graphPanel.deserializeGraph();
		}
		
		if(eSource == nodesMenuItem) {
			graphPanel.showNodesList();
		}
		
		if(eSource == edgesMenuItem) {
			graphPanel.showEdgesList();
		}
		
		if(eSource == drawGridCheckBoxMenuItem) {
			graphPanel.enableGrid(drawGridCheckBoxMenuItem.isSelected());
		}

		if(eSource == appMenuItem) {
			showInstruction();
		}
		
		if(eSource == authorMenuItem) {
			showAuthorInfo();
		}
	}
	
	private void showInstruction() {
		JOptionPane.showMessageDialog(this, APP_INFO, "Informacje o programie", JOptionPane.INFORMATION_MESSAGE);
	}
	
	private void showAuthorInfo() {
		JOptionPane.showMessageDialog(this, AUTHOR_INFO, "Informacje o autorze", JOptionPane.INFORMATION_MESSAGE);
	}

}
