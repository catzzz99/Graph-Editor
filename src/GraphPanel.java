import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;

import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import javax.swing.filechooser.FileNameExtensionFilter;


/*
 * 	Program: Edytor graf�w.
 * 
 *    Pliki: GraphEditor.java, 
 *    		 GraphPanel.java
 *    		 Grid.java,
 *    		 Node.java,
 *    		 SpecialNode.java,
 *    		 Edge.java,
 *    		 SpecialEdge.java,
 *    		 Graph.java,
 *    
 *     Plik: GraphPanel.java
 *	  		 definicja klasy GraphPanel
 *
 *    Autor: Micha� Tkacz 248869
 *	   Data: 22.11.2019r.
 *	Zaj�cia: Pi�tek TN 11:15
 */

public class GraphPanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener,  ComponentListener {

	
	private static final long serialVersionUID = 3544581658578869882L;
	
	private Grid grid;
	private boolean drawGrid;
	
	private Graph graph;
	
	
	private boolean mouseLeftButton = false;
	@SuppressWarnings("unused")
	private boolean mouseRightButton = false;	
	
	private int mouseX;
	private int mouseY;
	
	private Node nodeUnderCursor;
	private Edge edgeUnderCursor;
	
	private boolean chooseNodeB = false;
	private Node newEdgeNodeA;
	private Node newEdgeNodeB;

	public GraphPanel(Graph g) {
		if(g == null) {
			graph = new Graph("Graf");
		}else {
			setGraph(g);
		}
		
		grid = new Grid(getSize(), 50);
		drawGrid = false;
		
		addMouseMotionListener(this);
		addMouseListener(this);
		addKeyListener(this);
		
		addComponentListener(this);
		
		setFocusable(true);
		requestFocus();
		
	}
	
	public Graph getGraph() {
		return graph;
	}

	public void setGraph(Graph graph) {
		if(graph == null)
			this.graph = new Graph("Graph");
		else
			this.graph = graph;
	}
	
	public void showExampleGraph() {	
		graph = new Graph("Example");
		Node a = new SpecialNode(250, 100, Color.RED, 20, "A");
		Node b = new SpecialNode(550, 100, Color.GREEN, 20, "B");	
		Node c = new SpecialNode(550, 400, Color.MAGENTA, 20, "C");
		Node d = new SpecialNode(250, 400, Color.ORANGE, 20, "D");
		
		Node e = new SpecialNode(350, 200, Color.RED, 20, "E");
		Node f = new SpecialNode(450, 200, Color.GREEN, 20, "F");	
		Node g = new SpecialNode(450, 300, Color.MAGENTA, 20, "G");
		Node h = new SpecialNode(350, 300, Color.ORANGE, 20, "H");
		
		Edge ab = new Edge(a, b);
		Edge bc = new Edge(b, c);
		Edge cd = new Edge(c, d);
		Edge da = new Edge(d, a);
		
		Edge ef = new Edge(e, f);
		Edge fg = new Edge(f, g);
		Edge gh = new Edge(g, h);
		Edge he = new Edge(h, e);
		
		Edge ae = new SpecialEdge(a, e, Color.RED, 4);
		Edge bf = new SpecialEdge(b, f, Color.GREEN, 4);
		Edge cg = new SpecialEdge(c, g, Color.MAGENTA, 4);
		Edge dh = new SpecialEdge(d, h, Color.ORANGE, 4);
		
		graph.addNode(a);
		graph.addNode(b);
		graph.addNode(c);
		graph.addNode(d);
		graph.addNode(e);
		graph.addNode(f);
		graph.addNode(g);
		graph.addNode(h);
		
		graph.addEdge(ab);
		graph.addEdge(bc);
		graph.addEdge(cd);
		graph.addEdge(da);
		
		graph.addEdge(ef);
		graph.addEdge(fg);
		graph.addEdge(gh);
		graph.addEdge(he);
		
		graph.addEdge(ae);
		graph.addEdge(bf);
		graph.addEdge(cg);
		graph.addEdge(dh);
		
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(grid != null && drawGrid) 
			grid.draw(g);

		if(graph != null)
			graph.draw(g);
	}

	public void createNewGraph() {
		setGraph(new Graph("Graph"));
		repaint();
	}
	
	
	public void serializeGraph(String fileName) {
		if(graph == null)
			return;
		
		if(!fileName.endsWith(".bin")) {
			fileName += ".bin";
		}
		try {
			Graph.serializeGraph(fileName, graph);
			JOptionPane.showMessageDialog(null, "Dane zosta�y zapisane do pliku " + fileName);
		} catch (GraphException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "B��d!", JOptionPane.ERROR_MESSAGE);
		}
			
	}
	
	public void serializeGraph() {
		if(graph == null)
			return;
		
		JFileChooser fc = new JFileChooser(".");
		fc.setMultiSelectionEnabled(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Pliki binarne *.bin", "bin");
		fc.addChoosableFileFilter(filter);
		fc.setFileFilter(filter);

		int choosenOption = fc.showSaveDialog(this);

		if (choosenOption == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fc.getSelectedFile();
			String fileName = selectedFile.getAbsolutePath();
			if(!fileName.endsWith(".bin")) {
				selectedFile = new File(fileName + ".bin");
			}
			try {
				Graph.serializeGraph(selectedFile, graph);
				JOptionPane.showMessageDialog(null, "Dane zosta�y zapisane do pliku " + selectedFile.getAbsolutePath());
			} catch (GraphException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "B��d!", JOptionPane.ERROR_MESSAGE);
			}
		}
			
	}
	
	public void deserializeGraph(String fileName) {
		if(graph == null)
			return;
		
		if(!fileName.endsWith(".bin")) {
			fileName += ".bin";
		}
		try {
			graph = Graph.deserializeGraph(fileName);
			JOptionPane.showMessageDialog(null, "Dane zosta�y wczytane z pliku " + fileName);
			repaint();
		} catch (GraphException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "B��d!", JOptionPane.ERROR_MESSAGE);
		}
			
	}
	
	public void deserializeGraph() {
		if(graph == null)
			return;
		
		JFileChooser fc = new JFileChooser(".");
		fc.setMultiSelectionEnabled(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Pliki binarne *.bin", "bin");
		fc.addChoosableFileFilter(filter);
		fc.setFileFilter(filter);
		
		int choosenOption = fc.showOpenDialog(this);
		if(choosenOption == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fc.getSelectedFile();
			try {
				graph = Graph.deserializeGraph(selectedFile);
				JOptionPane.showMessageDialog(null, "Dane zosta�y wczytane z pliku " + selectedFile.getAbsolutePath());
				repaint();
			} catch (GraphException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(),"B��d!", JOptionPane.ERROR_MESSAGE);
			}
		}

	}
	
	public void enableGrid(boolean drawGrid) {
		this.drawGrid = drawGrid;
		if(this.drawGrid) {
			grid.scaleGrid(getSize());
		}
		repaint();
	}
	
	public void showNodesList() {
		String nodesList = graph.getListOfNodes();
		JOptionPane.showMessageDialog(this, nodesList,"Lista w�z��w grafu", JOptionPane.INFORMATION_MESSAGE);
	}

	public void showEdgesList() {
		String nodesList = graph.getListOfEdges();
		JOptionPane.showMessageDialog(this, nodesList,"Lista w�z��w grafu", JOptionPane.INFORMATION_MESSAGE);
	}
	
	// --------------------------MOUSE
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if(mouseLeftButton) {
			moveGraphDrag(e.getX(), e.getY());
		}else {
			setMouseCursor(e);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		setMouseCursor(e);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			mouseLeftButton = true;
		}
		
		if(e.getButton() == MouseEvent.BUTTON3) {
			mouseRightButton = true;
		}
		
		setMouseCursor(e);
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			mouseLeftButton = false;
			finalizeAddEdge();
		}
		
		if(e.getButton() == MouseEvent.BUTTON3) {
			mouseRightButton = false;
			chooseNodeB = false;
			if(nodeUnderCursor != null) {
				createNodePopupMenu(e, nodeUnderCursor);
			}else if(edgeUnderCursor != null){
				createEdgePopupMenu(e, edgeUnderCursor);
			}else {
				createPlainPopupMenu(e);
			}
		}
		setMouseCursor(e);
	}
	
	// --------------------------POPUP MENU
	
	private void createPlainPopupMenu(MouseEvent e){
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem newNodeMenuItem = new JMenuItem("Stw�rz nowy w�ze�");
		popupMenu.add(newNodeMenuItem);
		newNodeMenuItem.addActionListener((action)->{
			createNewNode(e.getX(), e.getY());
		});
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
		
	}
	
	private void createNodePopupMenu(MouseEvent e, Node n){
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem removeNodeMenuItem = new JMenuItem("Usu� w�ze�");
		popupMenu.add(removeNodeMenuItem);
		removeNodeMenuItem.addActionListener((action)->{
			removeNode(n);
		});
		
		popupMenu.addSeparator();
		
		JMenuItem addEdgeMenuItem = new JMenuItem("Dodaj kraw�d�");
		popupMenu.add(addEdgeMenuItem);
		addEdgeMenuItem.addActionListener((action)->{
			initializeAddEdge(n);
		});

		if(nodeUnderCursor instanceof SpecialNode) {
			popupMenu.addSeparator();
			JMenuItem changeNodeRadiusMenuItem = new JMenuItem("Zmie� rozmiar w�z�a");
			popupMenu.add(changeNodeRadiusMenuItem);
			changeNodeRadiusMenuItem.addActionListener((action)->{		
				changeNodeRadius(n);
			});
			
			JMenuItem changeNodeColorMenuItem = new JMenuItem("Zmie� kolor w�z�a");
			popupMenu.add(changeNodeColorMenuItem);
			changeNodeColorMenuItem.addActionListener((action)->{		
				changeNodeColor(n);
			});
			
			JMenuItem changeTextMenuItem = new JMenuItem("Zmie� tekst w�z�a");
			popupMenu.add(changeTextMenuItem);
			changeTextMenuItem.addActionListener((action)->{		
				changeNodeText(n);
			});
			
		}
		
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
		
	}
	

	private void createEdgePopupMenu(MouseEvent event, Edge e) {
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem removeEdgeMenuItem = new JMenuItem("Usu� kraw�d�");
		popupMenu.add(removeEdgeMenuItem);
		removeEdgeMenuItem.addActionListener((action)->{
			removeEdge(e);
		});
		
		if(e instanceof SpecialEdge) {
			popupMenu.addSeparator();
			JMenuItem changeEdgeStrokeMenuItem = new JMenuItem("Zmie� grubo�� kraw�dzi");
			popupMenu.add(changeEdgeStrokeMenuItem);
			changeEdgeStrokeMenuItem.addActionListener((action)->{
				changeEdgeStroke(e);
			});
			
			JMenuItem changeEdgeColorMenuItem = new JMenuItem("Zmie� kolor kraw�dzi");
			popupMenu.add(changeEdgeColorMenuItem);
			changeEdgeColorMenuItem.addActionListener((action)->{
				changeEdgeColor(e);
			});
		}
		
		popupMenu.show(event.getComponent(), event.getX(), event.getY());
	}
	
	// --------------------------KEYBOARD
	
	@Override
	public void keyPressed(KeyEvent e) {
		int moveDistance;
		if (e.isShiftDown())
			moveDistance = 10;
		else
			moveDistance = 1;
		
		switch(e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			moveGraphStep(-moveDistance, 0);
			break;
		case KeyEvent.VK_RIGHT:
			moveGraphStep(moveDistance, 0);
			break;
		case KeyEvent.VK_UP:
			moveGraphStep(0, -moveDistance);
			break;
		case KeyEvent.VK_DOWN:
			moveGraphStep(0, moveDistance);
			break;
		case KeyEvent.VK_DELETE:
			if(nodeUnderCursor != null) {
				graph.removeNode(nodeUnderCursor);
			}else if(edgeUnderCursor != null) {
				graph.removeEdge(edgeUnderCursor);
			}
			break;
		}
		
		if(e.isControlDown()) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_S:
				serializeGraph();
				break;
			case KeyEvent.VK_O:
				deserializeGraph();
				break;
			}
		}
		
		repaint();
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		char key = e.getKeyChar();
		
		int quickChangeStep = 1;
		
		switch (key) {
		case 'r':
			quickSetColor(Color.RED);
			break;
		case 'g':
			quickSetColor(Color.GREEN);
			break;
		case 'b':
			quickSetColor(Color.BLUE);
			break;
		case 'q':
			enableGrid(!drawGrid);
			break;
		case 'z':
			createNewNode(mouseX, mouseY);
			break;
		case 'x':
			initializeAddEdge(nodeUnderCursor);
			break;		
		case '=':
			quickChangeSize(quickChangeStep);
			break;
		case '-':
			quickChangeSize(-quickChangeStep);
			break;
		}
		
	}
	
	private void quickChangeSize(int step){
		if(nodeUnderCursor instanceof SpecialNode) {
			((SpecialNode) nodeUnderCursor).changeRadius(step);
		}else if(edgeUnderCursor instanceof SpecialEdge) {
			((SpecialEdge) edgeUnderCursor).changeStroke(step);
		}
	}

	private void quickSetColor(Color c) {
		if(nodeUnderCursor instanceof SpecialNode) {
			((SpecialNode) nodeUnderCursor).setColor(c);
		}else if(edgeUnderCursor instanceof SpecialEdge) {
			((SpecialEdge) edgeUnderCursor).setColor(c);
		}
	}
	
	// --------------------------METHODS

	public void setMouseCursor(MouseEvent e) {
		if(e != null) {
			nodeUnderCursor = graph.findNodeUnderCursor(e.getX(), e.getY());
			if(nodeUnderCursor == null) {
				edgeUnderCursor = graph.findEdgeUnderCursor(e.getX(), e.getY());
			}
			mouseX = e.getX();
			mouseY = e.getY();
		}
		
		int mouseCursor;
		if (nodeUnderCursor != null) {
			mouseCursor = Cursor.HAND_CURSOR;
		}else if(edgeUnderCursor != null) {
			mouseCursor = Cursor.CROSSHAIR_CURSOR;
		}else if(chooseNodeB) {
			mouseCursor = Cursor.WAIT_CURSOR;
		} else if (mouseLeftButton) {
			mouseCursor = Cursor.MOVE_CURSOR;
		} else {
			mouseCursor = Cursor.DEFAULT_CURSOR;
		}
		setCursor(Cursor.getPredefinedCursor(mouseCursor));
		
	}
	
	private void moveGraphDrag(int mx, int my) {
		int dx = mx - mouseX;
		int dy = my - mouseY;
		
		if(nodeUnderCursor != null) {
			nodeUnderCursor.move(dx, dy);
		}else if(edgeUnderCursor != null){
			edgeUnderCursor.move(dx, dy);
		}else {
			graph.moveGraph(dx, dy);
		}
		
		mouseX = mx;
		mouseY = my;
		repaint();
	}
	
	private void moveGraphStep(int dx, int dy) {
		graph.moveGraph(dx, dy);
		repaint();
	}
	
	private void createNewNode(int mx, int my) {
		try {
			NodeType nodeType = (NodeType) JOptionPane.showInputDialog(this, "Wybierz typ w�z�a", "Nowa w�ze�", JOptionPane.DEFAULT_OPTION, null, NodeType.values(), NodeType.BASIC_NODE);
			if(nodeType == NodeType.BASIC_NODE) {
				graph.addNode(new Node(mx, my));
			}else if(nodeType == NodeType.SPECIAL_NODE) {
				Color color = JColorChooser.showDialog(this, "Wybierz kolor nowego w�z�a", Color.WHITE);
				int radius = ((Integer)JOptionPane.showInputDialog(this, "Wybierz promie�", "Nowy w�ze�", JOptionPane.DEFAULT_OPTION, null, SpecialNode.RADIUS_VALUES, SpecialNode.RADIUS_VALUES[0])).intValue();
				String text = JOptionPane.showInputDialog(this, "Wprowadx tekst:", "Nowy w�ze�", JOptionPane.QUESTION_MESSAGE);
				graph.addNode(new SpecialNode(mx, my, color, radius, text));
			}else {
				throw new NullPointerException();
			}
			repaint();
		}catch(NullPointerException e) {
			JOptionPane.showMessageDialog(this, "Tworzenie w�z�a zosta�o anulowane.", "Anulowano", JOptionPane.INFORMATION_MESSAGE);
		}
		
	}
	
	private void removeNode(Node n){
		graph.removeNode(n);
		repaint();
	}
	
	private void initializeAddEdge(Node n) {
		if(nodeUnderCursor != null) {
			newEdgeNodeA = n;
			chooseNodeB = true;
			setMouseCursor(null);
		}
	}
	
	private void finalizeAddEdge() {
		if(chooseNodeB) {
			if(nodeUnderCursor != null) {
				if(nodeUnderCursor.equals(newEdgeNodeA)) {
					JOptionPane.showMessageDialog(this, "Wybierz w�z� inny, ni� pocz�towy!", "B��d!", JOptionPane.ERROR_MESSAGE);
				}else {
					try {
						newEdgeNodeB = nodeUnderCursor;
						EdgeType edgeType = (EdgeType) JOptionPane.showInputDialog(this, "Wybierz typ kraw�dzi", "Nowa kraw�d�", JOptionPane.DEFAULT_OPTION, null, EdgeType.values(), EdgeType.BASIC_EDGE);
						if(edgeType == EdgeType.BASIC_EDGE) {
							graph.addEdge(new Edge(newEdgeNodeA, newEdgeNodeB));
						}else if(edgeType == EdgeType.SPECIAL_EDGE) {
							Color color = JColorChooser.showDialog(this, "Wybierz kolor nowej kraw�dzi", Color.BLACK);
							int stroke = ((Integer)JOptionPane.showInputDialog(this, "Wybierz grubo�� kraw�dzi", "Nowa kraw�d�", JOptionPane.DEFAULT_OPTION, null, SpecialEdge.STROKE_VALUES, SpecialEdge.STROKE_VALUES[0])).intValue();
							graph.addEdge(new SpecialEdge(newEdgeNodeA, newEdgeNodeB, color, stroke));
						}else {
							throw new NullPointerException();
						}
						repaint();
					}catch (NullPointerException e){
						JOptionPane.showMessageDialog(this, "Tworzenie kraw�dzi zosta�o anulowane.", "Anulowano", JOptionPane.INFORMATION_MESSAGE);
					}
					
				}
			}
			chooseNodeB = false;
		}
	}
	
	private void removeEdge(Edge e) {
		graph.removeEdge(e);
		repaint();
	}
	
	private void changeNodeRadius(Node n) {
		try {
			int radius = ((Integer)JOptionPane.showInputDialog(this, "Wybierz promie�:", "Edycja w�z�a", JOptionPane.QUESTION_MESSAGE, null, SpecialNode.RADIUS_VALUES, SpecialNode.RADIUS_VALUES[0])).intValue();
			((SpecialNode)n).setR(radius);
			repaint();
		}catch (ClassCastException e) {
			JOptionPane.showMessageDialog(this, "Ten rodzaj w�ze�a nie obs�uguje zmiany promienia.", "B��d!", JOptionPane.ERROR_MESSAGE);
		}catch (NullPointerException e) {
			JOptionPane.showMessageDialog(this, "Zmiana promienia zosta�a anulowana.", "Anulowano", JOptionPane.INFORMATION_MESSAGE);
		}
		
	}
	
	private void changeNodeColor(Node n) {
		try {
			Color color = JColorChooser.showDialog(this, "Wybierz nowy kolor w�z�a", ((SpecialNode) n).getColor());
			((SpecialNode)n).setColor(color);
			repaint();
		}catch(ClassCastException e){
			JOptionPane.showMessageDialog(this, "Ten rodzaj w�ze�a nie obs�uguje zmiany koloru.", "B��d!", JOptionPane.ERROR_MESSAGE);
		}catch (NullPointerException e) {
			JOptionPane.showMessageDialog(this, "Zmiana koloru zosta�a anulowana.", "Anulowano", JOptionPane.INFORMATION_MESSAGE);
		}
			
	}

	private void changeNodeText(Node n) {
		String text = JOptionPane.showInputDialog(this, "Wprowadx tekst:", "Edycja w�z�a", JOptionPane.QUESTION_MESSAGE);
		try {
			((SpecialNode)n).setText(text);
			repaint();
		}catch(ClassCastException e) {
			JOptionPane.showMessageDialog(this, "Ten rodzaj w�ze�a nie obs�uguje zmiany tekstu.", "B��d!", JOptionPane.INFORMATION_MESSAGE);
		}catch (NullPointerException e) {
			JOptionPane.showMessageDialog(this, "Zmiana tekstu zosta�a anulowana.", "Anulowano", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	private void changeEdgeStroke(Edge e) {
		try {
			int stroke = ((Integer)JOptionPane.showInputDialog(this, "Wybierz grubo�� kraw�dzi", "Nowa kraw�d�", JOptionPane.DEFAULT_OPTION, null, SpecialEdge.STROKE_VALUES, SpecialEdge.STROKE_VALUES[0])).intValue();
			((SpecialEdge) e).setStroke(stroke);
			repaint();
		} catch (ClassCastException exc) {
			JOptionPane.showMessageDialog(this, "Ten rodzaj kraw�dzi nie obs�uguje zmiany grubo�ci.", "B��d!", JOptionPane.INFORMATION_MESSAGE);
		}catch (NullPointerException exc) {
			JOptionPane.showMessageDialog(this, "Anulowano edycj�.", "B��d!", JOptionPane.INFORMATION_MESSAGE);
		}
		
	}

	private void changeEdgeColor(Edge e){
		try {
			Color color = JColorChooser.showDialog(this, "Wybierz kolor nowej kraw�dzi", Color.BLACK);
			((SpecialEdge) e).setColor(color);
			repaint();
		} catch (ClassCastException exc) {
			JOptionPane.showMessageDialog(this, "Ten rodzaj kraw�dzi nie obs�uguje zmiany koloru.", "B��d!", JOptionPane.INFORMATION_MESSAGE);
		}catch (NullPointerException exc) {
			JOptionPane.showMessageDialog(this, "Anulowano edycj�.", "B��d!", JOptionPane.INFORMATION_MESSAGE);
		}
		
	}

	// --------------------------COMPONENT_EVENT
	
	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentResized(ComponentEvent e) {
		Object eSource = e.getSource();
		if(eSource == this && drawGrid) {
			grid.scaleGrid(getSize());
			repaint();
		}
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
}
