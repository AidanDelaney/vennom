package org.eulerdiagrams.vennom.apCircles;

import javax.swing.*;

import org.eulerdiagrams.vennom.apCircles.display.APCircleDisplay;
import org.eulerdiagrams.vennom.graph.*;
import org.eulerdiagrams.vennom.graph.dialogs.*;
import org.eulerdiagrams.vennom.graph.drawers.*;
import org.eulerdiagrams.vennom.graph.experiments.*;
import org.eulerdiagrams.vennom.graph.utilities.*;
import org.eulerdiagrams.vennom.graph.views.*;

import java.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.font.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/**
 * A panel on which a graph is displayed.
 * <p>
 * Functions:
 * <br>
 * - Add a node with double button 1 click on the background.
 * <br>
 * - Add an edge with a button 3 drag (picks closest nodes to start
 *   and end of the drag, but does not add self sourcing nodes).
 * <br>
 * - Edit a node or edge with a double button 1 click on the item.
 * <br>
 * - Drag a node with a button 1 drag on a node.
 * <br>
 * - Select a node with a single button 1 click on a node or a button 1 drag on the background,
 *   add new nodes to the selection by pressing the control key whilst selecting.
 * <br>
 * - Delete the selection with Del or Backspace
 *
 * @author Peter Rodgers
 */
public class APCirclePanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

	public final Color PANELBACKGROUNDCOLOR = Color.white;
	public final Color SELECTEDPANELAREACOLOR = Color.gray;
    public final BasicStroke SELECTEDPANELAREASTROKE = new BasicStroke(1.0f);
	public final String LABELFONTNAME = "Arial";
	public final int LABELFONTSTYLE = Font.BOLD;
	public final int LABELFONTSIZE = 12;

	static public Color[] CIRCLE_COLORS = {Color.RED,Color.GREEN,Color.MAGENTA,Color.BLUE,Color.CYAN,Color.BLACK,Color.YELLOW,Color.LIGHT_GRAY,Color.DARK_GRAY};

    public final BasicStroke circleStroke = new BasicStroke(2.0f);

	
	public static final Dimension BUTTONSIZE = new Dimension(78,32);
	public static final Dimension LARGEBUTTONSIZE = new Dimension(116,32);

	public static final Point ZEROOFFSET = new Point(0,0);
	public static final int OFFSETINCREMENT = 5;

	protected boolean showNodeLabel = true;
	protected boolean showEdgeLabel = true;
/** Indicates if parallel edges should be separated when displayed. */
	protected boolean separateParallel = true;
	protected boolean showCirclesFlag = true;
	protected boolean showNodesFlag = true;
	protected boolean showMovableEdgesFlag = true;
	protected boolean showFixedEdgesFlag = true;

	protected Graph graph;
	protected ArrayList<GraphDrawer> graphDrawerList = new ArrayList<GraphDrawer>();
	protected ArrayList<GraphUtility> graphUtilityList = new ArrayList<GraphUtility>();
	protected ArrayList<GraphView> graphViewList = new ArrayList<GraphView>();
	protected ArrayList<GraphExperiment> graphExperimentList = new ArrayList<GraphExperiment>();
	protected GraphSelection selection;
	protected boolean dragSelectionFlag = false;
	protected Node dragNode = null;
	protected Node selectNode = null;
	protected Edge selectEdge = null;
	protected Node newEdgeNode = null;
	protected Point newEdgePoint = null;
	protected Point pressedPoint = null;
	protected Point lastPoint = null;
	protected Point dragSelectPoint = null;
	protected Edge addBendEdge = null;
	protected boolean spaceDown = false;

	protected Frame containerFrame = null;
	protected Applet containerApplet = null;
	protected Color panelBackgroundColor = PANELBACKGROUNDCOLOR;
	protected Color selectedPanelAreaColor = SELECTEDPANELAREACOLOR;
	protected BasicStroke selectedPanelAreaStroke = SELECTEDPANELAREASTROKE;
	
	protected AreaSpecification specification;	

	/** debugging code */
	public static ArrayList<Ellipse2D.Double> ellipses = new ArrayList<Ellipse2D.Double>();
	/** debugging code */
	public static ArrayList<Polygon> polygons = new ArrayList<Polygon>();


	public APCirclePanel(Applet inApplet) {

		super();

		containerFrame = null;
		containerApplet = inApplet;
		
		setup();
	}

	public APCirclePanel(Frame inContainerFrame) {

		super();

		containerFrame = inContainerFrame;
		containerApplet = null;

		setup();
	}
	
	protected void setup() {

		graph = new Graph();
		specification = new AreaSpecification("");

		selection = new GraphSelection(graph);
		setBackground(panelBackgroundColor);
		addMouseListener(this);
		addKeyListener(this);
	}

	public boolean getShowNodeLabel() {return showNodeLabel;}
	public boolean getShowEdgeLabel() {return showEdgeLabel;}
	public boolean getSeparateParallel() {return separateParallel;}
	public Graph getGraph() {return graph;}
	public GraphSelection getSelection() {return selection;}
	public ArrayList<GraphDrawer> getGraphDrawerList() {return graphDrawerList;}
	public ArrayList<GraphUtility> getGraphUtilityList() {return graphUtilityList;}
	public ArrayList<GraphView> getGraphViewList() {return graphViewList;}
	public ArrayList<GraphExperiment> getGraphExperimentList() {return graphExperimentList;}
	public Frame getContainerFrame() {return containerFrame;}
	public boolean getShowCirclesFlag() {return showCirclesFlag;}
	public boolean getShowNodesFlag() {return showNodesFlag;}
	public boolean getShowMovableEdgesFlag() {return showMovableEdgesFlag;}
	public boolean getShowFixedEdgesFlag() {return showFixedEdgesFlag;}
	public AreaSpecification getSpecification() {return specification;}

	public void setGraph(Graph g) {graph = g;}
	public void setShowNodeLabel(boolean flag) {showNodeLabel = flag;}
	public void setShowEdgeLabel(boolean flag) {showEdgeLabel = flag;}
	public void setSeparateParallel(boolean flag) {separateParallel = flag;}
	public void setShowCirclesFlag(boolean flag) {showCirclesFlag = flag;}
	public void setShowNodesFlag(boolean flag) {showNodesFlag = flag;}
	public void setShowMovableEdgesFlag(boolean flag) {showMovableEdgesFlag = flag;}
	public void setShowFixedEdgesFlag(boolean flag) {showFixedEdgesFlag = flag;}
	public void setSpecification(AreaSpecification as) {specification = as;}

/** Add a drawing algorithm to the panel. */
	public void addGraphDrawer(GraphDrawer gd) {
		graphDrawerList.add(gd);
		gd.setGraphPanel(this);
	}

/** Removes a drawing algorithm from the panel. */
	public void removeGraphDrawer(GraphDrawer gd) {
		graphDrawerList.remove(gd);
		gd.setGraphPanel(null);
	}

/** Add a graph utility to the panel. */
	public void addGraphUtility(GraphUtility gu) {
		graphUtilityList.add(gu);
		gu.setGraphPanel(this);
	}

/** Removes a graph utility from the panel. */
	public void removeGraphUtility(GraphUtility gu) {
		graphUtilityList.remove(gu);
		gu.setGraphPanel(null);
	}

/** Add a graph view to the panel. */
	public void addGraphView(GraphView gv) {
		graphViewList.add(gv);
		gv.setGraphPanel(this);
	}

/** Removes a graph view from the panel. */
	public void removeGraphView(GraphView gv) {
		graphViewList.remove(gv);
		gv.setGraphPanel(null);
	}

/** Add a graph experiment to the panel. */
	public void addGraphExperiment(GraphExperiment ge) {
		graphExperimentList.add(ge);
		ge.setGraphPanel(this);
	}

/** Removes a graph experiment from the panel. */
	public void removeGraphExperiment(GraphExperiment ge) {
		graphExperimentList.remove(ge);
		ge.setGraphPanel(null);
	}


	public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

//paint background
		super.paintComponent(g2);

//draw the edges
		if(!separateParallel) {
			paintOverlaidEdges(g2,graph);
		} else {
			paintSeparateEdges(g2,graph);
		}


//draw the new edge drag
		if (newEdgePoint != null) {
			g2.setColor(selectedPanelAreaColor);
			Point centre = newEdgeNode.getCentre();
			g2.drawLine(centre.x,centre.y,newEdgePoint.x,newEdgePoint.y);
		}

		//draw the nodes
		if(showNodesFlag) {
			for(Node n : graph.getNodes()) {
				paintNode(g2,n);
			}
		}

		//draw the circles
		if(showCirclesFlag) {
			int count = 0;
			for(Node n : graph.getNodes()) {
				if(count == CIRCLE_COLORS.length) {
					count = 0;
				}
				
				Color color = CIRCLE_COLORS[count];

				paintCircle(g2,n,color);
				
				count++;
			}
		}

// draw the area selection
		if (dragSelectPoint != null) {
			g2.setColor(selectedPanelAreaColor);
	        g2.setStroke(selectedPanelAreaStroke);

			Shape r = convertPointsToRectangle(pressedPoint,dragSelectPoint);
			g2.draw(r);
		}
		
		
//debugging code
g2.setColor(Color.RED);
for(Ellipse2D.Double e : ellipses) {
g2.draw(e);	
}
//debugging code
g2.setColor(Color.BLUE);
for(Polygon p : polygons) {
g2.draw(p);	
}


	}


/** This is used when parallel edges are overlaid */
	protected void paintOverlaidEdges(Graphics2D g2, Graph g) {

		for(Edge e : graph.getEdges()) {
			paintEdge(g2,e,ZEROOFFSET);
		}
	}



/** This is used when parallel edges are displayed separately */
	protected void paintSeparateEdges(Graphics2D g2, Graph g) {

// set up the lists of parallel edges
		ParallelEdgeList parallelList = new ParallelEdgeList(g);

// iterate through the lists displaying the edges
// first consider any edge type order in the nodes neigbouring the parallel edges.
// second order by edge type priority
// third, choose randomly

		parallelList.setAllSorted(false);

		for(ParallelEdgeTuple tuple : parallelList.getParallelList()) {
			ParallelEdgeTuple sortedTuple = null;

// check for current order from neighbouring nodes
//			sortedTuple = getSortedNeigbour(tuple);
// TBD this will order the edges based on the neigbours ordering. Is this needed?

// order by sorting
			if(sortedTuple == null) {
				tuple.sortList();
			}

// set up the offset values

			Node n1 = tuple.getFromNode();
			Node n2 = tuple.getToNode();

			double x = n1.getX()-n2.getX();
			double y = n1.getY()-n2.getY();

			double incrementX = 0;
			double incrementY = 0;
			double divisor = Math.abs(x)+Math.abs(y);

			if (divisor != 0) {
				incrementX = y/divisor;
				incrementY = -x/divisor;
			}

			incrementX *= OFFSETINCREMENT;
			incrementY *= OFFSETINCREMENT;

// find a sensible starting offset
			double numberOfEdges = tuple.getList().size();
			Point offset = new Point((int)(-((numberOfEdges-1)*incrementX)/2),(int)(-((numberOfEdges-1)*incrementY)/2));

// display the edges given the order
			for(Edge e : tuple.getList()) {
				paintEdge(g2,e,offset);
				offset.x += (int)incrementX;
				offset.y += (int)incrementY;
			}

			tuple.setSorted(true);
		}


	}




/** Draws an edge on the graphics */
	public void paintEdge(Graphics2D g2, Edge e, Point offset) {

		EdgeType et = e.getType();
		
		if(!showMovableEdgesFlag) {
			if(et.equals(APCircleDisplay.ATTRACTOR) || et.equals(APCircleDisplay.REPULSOR)) {
				return;
			}
		}

		if(!showFixedEdgesFlag) {
			if(et.equals(APCircleDisplay.FIXED)) {
				return;
			}
		}

		if(!selection.contains(e)) {
			g2.setColor(et.getLineColor());
		} else {
			g2.setColor(et.getSelectedLineColor());
		}
		if(!selection.contains(e)) {
			Stroke stroke = et.getStroke();
			if(et.equals(APCircleDisplay.FIXED)) {
				Node n1 = e.getFrom();
				Node n2 = e.getTo();
				double desiredLength = 0;
				try {
					desiredLength = Double.parseDouble(e.getLabel());
				} catch (Exception exception) {
					desiredLength = 0;
				}
				double actualLength = Util.distance(e.getFrom().getCentre(),e.getTo().getCentre());
				double error = actualLength-desiredLength;
				if(Math.abs(error) > 1) {
					stroke = new BasicStroke(5);
				}
			}
			if(et.equals(APCircleDisplay.ATTRACTOR)) {
				double maxLength = Double.POSITIVE_INFINITY;
				try {
					maxLength = Double.parseDouble(e.getLabel());
				} catch (Exception exception) {
					maxLength = Double.POSITIVE_INFINITY;
				}
				double actualLength = Util.distance(e.getFrom().getCentre(), e.getTo().getCentre());
				if(maxLength<actualLength) {
					stroke = new BasicStroke(5);
				}
			}
			if(et.equals(APCircleDisplay.REPULSOR)) {
				double minLength = Double.POSITIVE_INFINITY;
				try {
					minLength = Double.parseDouble(e.getLabel());
				} catch (Exception exception) {
					minLength = Double.POSITIVE_INFINITY;
				}
				double actualLength = Util.distance(e.getFrom().getCentre(), e.getTo().getCentre());
				if(minLength > actualLength) {
					stroke = new BasicStroke(5);
				}
			}
			
	        g2.setStroke(stroke);
		} else {
	        g2.setStroke(et.getSelectedStroke());
		}

		Shape edgeShape = e.generateShape(offset);
		g2.draw(edgeShape);

		if(!e.getLabel().equals("") && showEdgeLabel) {

//TBD if there are edge bends, put the label at the middle edge bend

			int n1X = e.getFrom().getCentre().x;
			int n1Y = e.getFrom().getCentre().y;
			int n2X = e.getTo().getCentre().x;
			int n2Y = e.getTo().getCentre().y;
			int x = 0;
			int y = 0;
			if(n1X-n2X > 0) {
				x = n2X+(n1X-n2X)/2;
			} else {
				x = n1X+(n2X-n1X)/2;
			}
			if(n1Y-n2Y > 0) {
				y = n2Y+(n1Y-n2Y)/2;
			} else {
				y = n1Y+(n2Y-n1Y)/2;
			}

			x += offset.x;
			y += offset.y;

			Font font = new Font(LABELFONTNAME,LABELFONTSTYLE,LABELFONTSIZE);
			FontRenderContext frc = g2.getFontRenderContext();
			TextLayout labelLayout = new TextLayout(e.getLabel(), font, frc);

			g2.setColor(PANELBACKGROUNDCOLOR);
			Rectangle2D bounds = labelLayout.getBounds();
			bounds.setRect(bounds.getX()+x-2, bounds.getY()+y-2, bounds.getWidth()+4,bounds.getHeight()+4);
			g2.fill(bounds);

			if(!selection.contains(e)) {
				g2.setColor(et.getTextColor());
			} else {
				g2.setColor(et.getSelectedTextColor());
			}
			labelLayout.draw(g2,x,y);

		}
	}


	/** Draws a node on the graphics */
	public void paintNode(Graphics2D g2, Node n) {

		NodeType nt = n.getType();
		Point centre = n.getCentre();

		if(!selection.contains(n)) {
			g2.setColor(nt.getFillColor());
		} else {
			g2.setColor(nt.getSelectedFillColor());
		}
		if(!selection.contains(n)) {
	        g2.setStroke(nt.getStroke());
		} else {
	        g2.setStroke(nt.getSelectedStroke());
		}

		Shape nodeShape = n.generateShape();
		g2.fill(nodeShape);

		if(!selection.contains(n)) {
			g2.setColor(nt.getBorderColor());
		} else {
			g2.setColor(nt.getSelectedBorderColor());
		}

		g2.draw(nodeShape);

		if(!n.getLabel().equals("") && showNodeLabel) {
			if(!selection.contains(n)) {
				g2.setColor(nt.getTextColor());
			} else {
				g2.setColor(nt.getSelectedTextColor());
			}
			
			Font font = new Font(LABELFONTNAME,LABELFONTSTYLE,LABELFONTSIZE);
			FontRenderContext frc = g2.getFontRenderContext();
			TextLayout labelLayout = new TextLayout(n.getLabel(), font, frc);

			Rectangle2D labelBounds = labelLayout.getBounds();
			int labelX = (int)Math.round(centre.x-(labelBounds.getWidth()/2));
			int labelY = (int)Math.round(centre.y+(labelBounds.getHeight()/2));

			labelLayout.draw(g2,labelX,labelY);

		}
	}

	
	private Point getLabelCentre(Node n) {
		// TODO Auto-generated method stub
		return null;
	}

	/** Draws a circle on the graphics */
	public void paintCircle(Graphics2D g2, Node n, Color c) {

		Point centre = n.getCentre();
		String label = n.getLabel();
		String contour = n.getContour();
		
		double radius = 30;
		
		try {
			radius = Double.parseDouble(label);
		} catch (Exception e) {
		}

		g2.setStroke(circleStroke);
		g2.setColor(c);

//		shape = new Ellipse2D.Double(centre.x-width/2,centre.y-height/2,width,height);
		Shape shape = new Ellipse2D.Double(centre.x-radius,centre.y-radius,radius*2,radius*2);
		
		g2.draw(shape);

		if(contour.length() == 0) {
			return;
		}
		
		Font font = new Font(LABELFONTNAME,LABELFONTSTYLE,LABELFONTSIZE);
		FontRenderContext frc = g2.getFontRenderContext();

		TextLayout labelLayout = new TextLayout(contour, font, frc);

		int x = n.getX()-Util.convertToInteger(radius);
		int y = n.getY();

		Rectangle2D textBBox = labelLayout.getBounds();

		x = x - (int) (textBBox.getWidth()*1.7);
		y = y - (int) (textBBox.getHeight()*1.7);

		textBBox.setRect(textBBox.getX() + x - 2, textBBox.getY() + y - 2, textBBox.getWidth() + 4, textBBox.getHeight() + 4);

		g2.setColor(PANELBACKGROUNDCOLOR);
		
		g2.fill(textBBox);

		g2.setColor(c);

		labelLayout.draw(g2, x, y);


	}


/**
 * This converts two points to a rectangle, with first two
 * coordinates always the top left of the rectangle
 */
	public Shape convertPointsToRectangle (Point p1, Point p2) {
		int x1,x2,y1,y2;

		if (p1.x < p2.x) {
			x1 = p1.x;
			x2 = p2.x;
		} else {
			x1 = p2.x;
			x2 = p1.x;
		}
		if (p1.y < p2.y) {
			y1 = p1.y;
			y2 = p2.y;
		} else {
			y1 = p2.y;
			y2 = p1.y;
		}
		return (new Rectangle2D.Double(x1,y1,x2-x1,y2-y1));

	}


	public void mouseClicked(MouseEvent event) {

		if (addBendEdge != null) {
			addBendEdge.addBend(event.getPoint());
			addBendEdge = null;
			repaint();
			return;
		}


// left button only
		if (!SwingUtilities.isLeftMouseButton(event)) {
			selection.clear();
			repaint();
			return;
		}

		selectNode = graph.getNodeNearPoint(event.getPoint(),1);
		if (selectNode == null) {

			selectEdge = graph.getEdgeNearPoint(event.getPoint(),2);
			if (selectEdge == null) {
// no node or edge selected so add a node on double click
				if (event.getClickCount() > 1) {
					graph.addNode(new Node(new Point(event.getPoint())));
					selection.clear();
				} else {
// single click might have been a missed selection
					if (!event.isControlDown()) {
						selection.clear();
					}
				}
				repaint();
			} else {
				if (event.getClickCount() == 1) {
// edge selected, so add it to the selection
					if (!event.isControlDown()) {
						selection.clear();
					}
					selection.addEdge(selectEdge);
					repaint();
				} else {
// edit edge dialog on double click
					ArrayList<Edge> el = new ArrayList<Edge>();
					el.add(selectEdge);
					editEdges(el);
				}
			}
		} else {
			if (event.getClickCount() == 1) {
// node selected
				if (!event.isControlDown()) {
					selection.clear();
				}
				selection.addNode(selectNode);
				repaint();
			} else {
// edit node dialog on double click
				ArrayList<Node> nl = new ArrayList<Node>();
				nl.add(selectNode);
				editNodes(nl);
			}
			selectNode = null;
		}
		event.consume();
	}



/** Call this to edit nodes in the graph panel */
	public void editNodes(ArrayList<Node> nodes) {
		if(nodes.size() == 0) {
			return;
		}
		new EditNodeDialog(nodes,this);
	}



/** Call this to edit edges in the graph panel */
	public void editEdges(ArrayList<Edge> edges) {
		if(edges.size() == 0) {
			return;
		}
		new EditEdgeDialog(edges,this);
	}

/** Call this to edit all edge types */
	public void editEdgeTypes() {
		new ManageEdgeTypesDialog(this);
		repaint();
	}

/** Call this to edit all node types */
	public void editNodeTypes() {
		new ManageNodeTypesDialog(this);
		repaint();
	}


/** Call this to move the graph around */
	public void moveGraph() {

		new MoveGraphFrame(this);
	}

/** Call this to allow the user to add an edge bend to the selected edge by clicking on the panel. */
	public void addEdgeBend() {

		if(selection.getEdges().size() != 1) {
			return;
		}
		addBendEdge = (Edge)selection.getEdges().get(0);

		repaint();
	}



/** Call this to remove all edge bends from selected edges */
	public void removeEdgeBends() {
		for(Edge e : selection.getEdges()) {
			e.removeAllBends();
		}
		repaint();
	}


	public void mousePressed(MouseEvent event) {
		requestFocus();
		pressedPoint = event.getPoint();
		lastPoint = event.getPoint();
		addMouseMotionListener(this);
		if (!spaceDown && SwingUtilities.isLeftMouseButton(event)) {
			Node chosenNode = graph.getNodeNearPoint(pressedPoint,1);
			if(chosenNode != null) {
				if(selection.contains(chosenNode)) {
// if its a selected node then we are dragging a selection
					dragSelectionFlag = true;
				} else {
// otherwise just drag the node
					dragNode = chosenNode;
				}
			} else {
// no node chosen, so drag an area selection
				dragSelectPoint = new Point(event.getPoint());
			}
//			graph.moveNodeToEnd(dragNode);
			repaint();
		}
		if (SwingUtilities.isRightMouseButton(event) || (spaceDown && SwingUtilities.isLeftMouseButton(event))) {
			newEdgeNode = graph.getClosestNode(pressedPoint);
//			graph.moveNodeToEnd(newEdgeNode);
			newEdgePoint = new Point(event.getPoint());
			repaint();
		}
		event.consume();
	}



	public void mouseReleased(MouseEvent event) {

// dont do anything if no drag occurred
		removeMouseMotionListener(this);
		if(pressedPoint.distance(event.getPoint()) < 1) {
			dragSelectionFlag = false;
			dragNode = null;
			dragSelectPoint = null;
			newEdgeNode = null;
			newEdgePoint = null;
			return;
		}

		addBendEdge = null;

		// select all in the area
		if (dragSelectPoint != null) {

// if no control key modifier, then replace current selection
			if (!event.isControlDown()) {
				selection.clear();
			}

			Shape r = convertPointsToRectangle(pressedPoint,event.getPoint());

			for(Node node : graph.getNodes()) {
				Point centre = node.getCentre();

				if(r.contains(centre) && !selection.contains(node)) {
					selection.addNode(node);
				}
			}

			for(Edge edge : graph.getEdges()) {
				Rectangle edgeBounds = edge.shape().getBounds();

//rectangles with zero dimension dont get included, so quick hack
				edgeBounds.grow(1,1);

				if(r.contains(edgeBounds) && !selection.contains(edge)) {
					selection.addEdge(edge);
				}
			}

			dragSelectPoint = null;
			repaint();
		}

// finish the selection drag
		if (dragSelectionFlag) {
			dragSelectionFlag = false;
			repaint();
		}

// finish the node drag
		if (dragNode != null) {
			dragNode = null;
			repaint();
		}

// finish adding an edge
		if (newEdgeNode != null) {
			Node toNode = graph.getClosestNode(event.getPoint());
// dont add an self sourcing edge
			if (newEdgeNode != toNode) {
				graph.addEdge(new Edge(newEdgeNode,toNode));
			}
			newEdgeNode = null;
			newEdgePoint = null;
//			graph.moveNodeToEnd(toNode);
			repaint();
		}
		event.consume();
	}

	public void mouseEntered(MouseEvent event) {
	}

	public void mouseExited(MouseEvent event) {
	}

	public void mouseDragged(MouseEvent event) {

		if (dragSelectPoint != null) {
			dragSelectPoint = event.getPoint();
			repaint();
		}

		if (newEdgePoint != null) {
			newEdgePoint = event.getPoint();
			repaint();
		}

		if (dragSelectionFlag) {
			int deltaX = event.getX()-lastPoint.x;
			int deltaY = event.getY()-lastPoint.y;

			for(Node n : selection.getNodes()) {
				Point centre = n.getCentre();
				centre.setLocation(centre.x+deltaX,centre.y+deltaY);
			}
			lastPoint = event.getPoint();

			repaint();
		}

		if (dragNode != null) {

			int deltaX = event.getX()-lastPoint.x;
			int deltaY = event.getY()-lastPoint.y;

			Point centre = dragNode.getCentre();
			centre.setLocation(centre.x+deltaX,centre.y+deltaY);

			lastPoint = event.getPoint();

			repaint();
		}
		event.consume();
	}


	public void mouseMoved(MouseEvent event) {
	}

	public void keyTyped(KeyEvent event) {
	}


	public void keyPressed(KeyEvent event) {
		if(event.getKeyChar() == KeyEvent.VK_SPACE) {
			spaceDown = true;
		}
	}


	public void keyReleased(KeyEvent event) {
		if(event.getKeyChar() == KeyEvent.VK_SPACE) {
			spaceDown = false;
		}

// this stuff would be in keyTyped, but it doesnt register delete
		if((event.getKeyChar() == KeyEvent.VK_BACK_SPACE) || (event.getKeyChar() == KeyEvent.VK_DELETE)) {
			graph.removeEdges(selection.getEdges());
			graph.removeNodes(selection.getNodes());
			selection.clear();
			repaint();
		}
	}

	public boolean saveCirclesSVG(File file) {
		String svg = fileCirclesToSVG();
		try {
			BufferedWriter b = new BufferedWriter(new FileWriter(file));

// save the nodes
			b.write(svg);
			b.newLine();
			b.close();
		}
		catch(IOException e){
			System.out.println("An IO exception occured when executing fileSaveSVG("+file.getName()+"\n"+e+"\n");
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * Generates an svg of the circles
	 */
	public String fileCirclesToSVG() {
		
		String ret = "<svg  width=\""+getWidth()+"\" height=\""+getHeight()+"\">\n";
		for (Node n : getGraph().getNodes()) {
			double x = n.getX();
			double y = n.getY();
			if(n.getPreciseCentre() != null) {
				x = n.getPreciseCentre().x;
				y = n.getPreciseCentre().y;
			}
			ret += "\t<circle cx=\""+x+"\" cy=\""+y+"\" r=\""+n.getPreciseRadius()+"\" fill=\"none\" stroke=\"black\" stroke-width=\"2\" />\n";
			ret += "\t<text x=\""+x+"\" y=\""+y+"\">"+n.getContour()+"</text>\n";
		}
		
		ret += "</svg>";
		return ret;
	}

}



