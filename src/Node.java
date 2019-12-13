import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;



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
 *     Plik: Node.java
 *	  		 definicja klasy Node
 *			 definicja typu wyliczeniowego NodeType
 *
 *    Autor: Micha� Tkacz 248869
 *	   Data: 22.11.2019r.
 *	Zaj�cia: Pi�tek TN 11:15
 */

enum NodeType {
	BASIC_NODE("W�ze� zwyk�y"),
	SPECIAL_NODE("W�ze� specjalny");
	
	String nodeType;
	
	NodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	
	@Override
	public String toString() {
		return nodeType;
	}
}

public class Node implements Serializable{

	private static final long serialVersionUID = -7357466511459361679L;

	protected int x;
	protected int y;
	protected int r;
	
	protected NodeType nodeType;

	
	public Node(int x, int y) {
		this.x = x;
		this.y = y;
		this.r = 5;
		nodeType = NodeType.BASIC_NODE;
	}
	

	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}

	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillOval(x-r, y-r, r+r, r+r);
		g.setColor(Color.BLACK);
		g.drawOval(x-r, y-r, r+r, r+r);
	}

	public boolean isUnderCursor(int mx, int my) {
		int a = x - mx;
		int b = y - my;
		
		return a*a + b*b <= r*r;
	}

	public void move(int dx, int dy) {
		x += dx;
		y += dy;
	}	
	
	@Override
	public String toString() {
		return "[" + nodeType.toString() + "]: (" + Integer.toString(x) + ", " + Integer.toString(y) + ") ";

	}
	
}
