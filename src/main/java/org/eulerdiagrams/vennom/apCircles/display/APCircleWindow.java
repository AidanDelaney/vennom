package org.eulerdiagrams.vennom.apCircles.display;

import javax.imageio.*;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import java.awt.image.*;

import org.eulerdiagrams.vennom.graph.*;
import org.eulerdiagrams.vennom.graph.drawers.GraphDrawer;
import org.eulerdiagrams.vennom.graph.experiments.GraphExperiment;
import org.eulerdiagrams.vennom.graph.utilities.GraphUtility;
import org.eulerdiagrams.vennom.apCircles.APCirclePanel;
import org.eulerdiagrams.vennom.graph.views.*;
import org.eulerdiagrams.vennom.apCircles.drawers.*;
import org.eulerdiagrams.vennom.apCircles.utilities.*;
import org.eulerdiagrams.vennom.apCircles.views.*;


/** Graph layout window using GraphPanel */
public class APCircleWindow extends JFrame implements ActionListener {

	Graph graph = null;
	APCirclePanel gp = null;
	APCircleWindow gw = null;
	File currentFile = null;
	File startDirectory;
	
	public GeneralXML generalXML;
	
	int width = 600;
	int height = 600;

	public APCircleWindow() {
		super("AP Circles");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		String startDirectoryName = System.getProperty("user.dir");
		startDirectory = new File(startDirectoryName);
// this for convenience, as the program normally starts in graph/display
//		startDirectory = startDirectory.getParentFile();

		gw = this;
		
		generalXML = new GeneralXML(graph);

		gp = new APCirclePanel(this);
		getContentPane().add(gp);
		
		graph = gp.getGraph();

		initView();
		initExperiment();
		initUtility();
		initLayout();
		initMenu();

		setSize(width,height);

		Dimension frameDim = Toolkit.getDefaultToolkit().getScreenSize();
		int posX = (frameDim.width - getSize().width)/2;
		int posY = (frameDim.height - getSize().height)/2;
		setLocation(posX, posY);

		setVisible(true);

		gp.requestFocus();
	}


/** Trival accessor. */
	public Graph getGraph() {return graph;}
/** Trival accessor. */
	public APCirclePanel getGraphPanel() {return gp;}

	private void initView() {
		gp.addGraphView(new GraphViewShowEdgeLabel(KeyEvent.VK_L, "Toggle Edge Labels",KeyEvent.VK_L));
		gp.addGraphView(new GraphViewShowNodeLabel(KeyEvent.VK_K, "Toggle Node Labels",KeyEvent.VK_K));
		gp.addGraphView(new CircleViewAPToggleShow(KeyEvent.VK_C, "Toggle Circles",KeyEvent.VK_C));
// toggle parallel edge separation
//		gp.addGraphView(new GraphViewSeparateEdges(KeyEvent.VK_P, "Toggle Separate Parallel Edges",KeyEvent.VK_P));
	}

	private void initExperiment() {
// for finding good values for metric graph drawing
//		gp.addGraphExperiment(new GraphExperimentMetricValues(KeyEvent.VK_N,"Metric Spring Embedder Experiment",KeyEvent.VK_N));
// Generate edge length data
//		gp.addGraphExperiment(new GraphExperimentEdgeLengthData(KeyEvent.VK_V,"Generate Edge Length Data",KeyEvent.VK_V));
// Generate second bit of edge length data
//		gp.addGraphExperiment(new GraphExperimentEdgeLengthDataAfterSE(KeyEvent.VK_W,"Generate Edge Length After SE Data",KeyEvent.VK_W));
	}


	private void initUtility() {

		gp.addGraphUtility(new CircleUtilityTest());
//		gp.addGraphUtility(new CreateRandomSpecificationByGraph());
		gp.addGraphUtility(new CreateRandomPiercedSpecificationByAbstractDescription());
		gp.addGraphUtility(new RectifyLengths());
		gp.addGraphUtility(new ReportPassingEdges());
		gp.addGraphUtility(new ReportAreaProportions());
		gp.addGraphUtility(new EnterPiercedSpecification());
		gp.addGraphUtility(new EnterGeneralSpecification());
		gp.addGraphUtility(new TestRandomPierced());
		gp.addGraphUtility(new TestExactGeneral());
		gp.addGraphUtility(new GeneralParameterFinder());
		
	}


	private void initLayout() {

		PiercedAPForceModel fm = new PiercedAPForceModel(KeyEvent.VK_Q,"Pierced area-proportional",true);
		fm.setRandomize(false);
		fm.setAnimateFlag(true);
		gp.addGraphDrawer(fm);
		
		PiercedAPForceModel fm1 = new PiercedAPForceModel(KeyEvent.VK_W,"Pierced area-proportional single iteration",true);
		fm1.setIterations(1);
		fm1.setRandomize(false);
		fm1.setAnimateFlag(true);
		gp.addGraphDrawer(fm1);
		
		GeneralAPForceModel fm2 = new GeneralAPForceModel(KeyEvent.VK_D,"General area-proportional",true);
		fm2.setRandomize(false);
		fm2.setAnimateFlag(true);
		gp.addGraphDrawer(fm2);
		
		GeneralAPForceModel fm3 = new GeneralAPForceModel(KeyEvent.VK_F,"General area-proportional single iteration",true);
		fm3.setIterations(1);
		fm3.setRandomize(false);
		fm3.setAnimateFlag(true);
		gp.addGraphDrawer(fm3);
		
		StandardSpringEmbedder se = new StandardSpringEmbedder(KeyEvent.VK_S,"Standard Spring Embedder",true);
		se.setRandomize(false);
		se.setAnimateFlag(true);
		gp.addGraphDrawer(se);

	}

	private void initMenu() {
	
		JMenuBar menuBar = new JMenuBar();

		setJMenuBar(menuBar);

// File Menu
		JMenu fileMenu = new JMenu("File");

		fileMenu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(fileMenu);
	
		JMenuItem fileNewItem = new JMenuItem("New",KeyEvent.VK_N);
		fileNewItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		fileMenu.add(fileNewItem);

		JMenuItem fileOpenItem = new JMenuItem("Open...",KeyEvent.VK_O);
		fileOpenItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		fileMenu.add(fileOpenItem);

		JMenuItem fileOpenAdjacencyItem = new JMenuItem("Open Adjacency File...");
		fileMenu.add(fileOpenAdjacencyItem);

		JMenuItem fileOpenWeightedAdjacencyItem = new JMenuItem("Open Weighted Adjacency File...");
		fileMenu.add(fileOpenWeightedAdjacencyItem);

		JMenuItem fileOpenXMLItem = new JMenuItem("Open XML File...");
		fileMenu.add(fileOpenXMLItem);

		JMenuItem fileSaveItem = new JMenuItem("Save",KeyEvent.VK_S);
		fileSaveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		fileMenu.add(fileSaveItem);

		JMenuItem fileSaveAsItem = new JMenuItem("Save As...");
		fileMenu.add(fileSaveAsItem);

		JMenuItem fileSaveSimpleItem = new JMenuItem("Save Simple Graph...");
		fileMenu.add(fileSaveSimpleItem);

		JMenuItem fileSaveXMLItem = new JMenuItem("Save XML File...");
		fileMenu.add(fileSaveXMLItem);

		JMenuItem fileSaveSVGItem = new JMenuItem("Save SVG File...");
		fileMenu.add(fileSaveSVGItem);

		JMenuItem filePNGItem = new JMenuItem("Export to png");
		fileMenu.add(filePNGItem);

		JMenuItem fileExitItem = new JMenuItem("Exit",KeyEvent.VK_X);
		fileExitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
		fileMenu.add(fileExitItem);

		fileExitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileExit();
			}
		});
		
		fileNewItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileNew();
			}
		});

		fileOpenItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileOpen();
			}
		});

		fileOpenXMLItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileOpenXML();
			}
		});

		fileSaveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileSave();
			}
		});

		fileSaveAsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileSaveAs();
			}
		});

		fileSaveSimpleItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileSaveSimple();
			}
		});

		fileSaveXMLItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileSaveXML();
			}
		});

		fileSaveSVGItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileSaveSVG();
			}
		});

		filePNGItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				filePNG();
			}
		});

// Edit Menu
		JMenu editMenu = new JMenu("Edit");

		editMenu.setMnemonic(KeyEvent.VK_E);
		menuBar.add(editMenu);
	
		JMenuItem editNodesItem = new JMenuItem("Edit Selected Nodes...",KeyEvent.VK_N);
		editNodesItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.SHIFT_MASK));
		editMenu.add(editNodesItem);

		JMenuItem editEdgesItem = new JMenuItem("Edit Selected Edges...",KeyEvent.VK_E);
		editEdgesItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.SHIFT_MASK));
		editMenu.add(editEdgesItem);

		JMenuItem editEdgeTypesItem = new JMenuItem("Edit Edge Types...");
		editMenu.add(editEdgeTypesItem);

		JMenuItem editNodeTypesItem = new JMenuItem("Edit Node Types...");
		editMenu.add(editNodeTypesItem);

		JMenuItem editMoveGraphItem = new JMenuItem("Move Graph...");
		editMenu.add(editMoveGraphItem);

		JMenuItem editAddEdgeBendItem = new JMenuItem("Add Edge Bend");
		editMenu.add(editAddEdgeBendItem);

		JMenuItem editRemoveEdgeBendsItem = new JMenuItem("Remove Edge Bends");
		editMenu.add(editRemoveEdgeBendsItem);

		JMenuItem editSelectAllItem = new JMenuItem("Select All",KeyEvent.VK_A);
		editSelectAllItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		editMenu.add(editSelectAllItem);

		editNodesItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				gp.editNodes(gp.getSelection().getNodes());
			}
		});
		
		editEdgesItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				gp.editEdges(gp.getSelection().getEdges());
			}
		});

		editEdgeTypesItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				gp.editEdgeTypes();
			}
		});

		editNodeTypesItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				gp.editNodeTypes();
			}
		});

		editMoveGraphItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				gp.moveGraph();
			}
		});

		editAddEdgeBendItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				gp.addEdgeBend();
			}
		});

		editRemoveEdgeBendsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				gp.removeEdgeBends();
			}
		});

		editSelectAllItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				editSelectAll();
			}
		});


// View Menu
		JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic(KeyEvent.VK_V);
		menuBar.add(viewMenu);

		for(GraphView v : gp.getGraphViewList()) {
	        JMenuItem menuItem = new JMenuItem(v.getMenuText(),v.getMnemonicKey());
			menuItem.setAccelerator(KeyStroke.getKeyStroke(v.getAcceleratorKey(),0));
			menuItem.addActionListener(this);
			viewMenu.add(menuItem);
		}

// Experiment Menu
		JMenu experimentsMenu = new JMenu("Experiments");
        experimentsMenu.setMnemonic(KeyEvent.VK_E);
		menuBar.add(experimentsMenu);

		for(GraphExperiment ge : gp.getGraphExperimentList()) {
	        JMenuItem menuItem = new JMenuItem(ge.getMenuText(),ge.getMnemonicKey());
			menuItem.setAccelerator(KeyStroke.getKeyStroke(ge.getAcceleratorKey(),0));
			menuItem.addActionListener(this);
			experimentsMenu.add(menuItem);
		}


// Utilities Menu
		JMenu utilitiesMenu = new JMenu("Utilities");
        utilitiesMenu.setMnemonic(KeyEvent.VK_U);
		menuBar.add(utilitiesMenu);

		for(GraphUtility u : gp.getGraphUtilityList()) {
	        JMenuItem menuItem = new JMenuItem(u.getMenuText(),u.getMnemonicKey());
			menuItem.setAccelerator(KeyStroke.getKeyStroke(u.getAcceleratorKey(),0));
			menuItem.addActionListener(this);
			utilitiesMenu.add(menuItem);
		}


		JMenu layoutMenu = new JMenu("Layout");
        layoutMenu.setMnemonic(KeyEvent.VK_L);
		menuBar.add(layoutMenu);

		for(GraphDrawer d : gp.getGraphDrawerList()) {
	        JMenuItem menuItem = new JMenuItem(d.getMenuText(), d.getMnemonicKey());
			menuItem.setAccelerator(KeyStroke.getKeyStroke(d.getAcceleratorKey(),0));
			menuItem.addActionListener(this);
			layoutMenu.add(menuItem);
		}
		
	}

	protected void fileNew() {
		if (currentFile != null) {
			if (!currentFile.isDirectory()) {
				currentFile = currentFile.getParentFile();
			}
		}
		graph.clear();
		gp.update(gp.getGraphics());
	}

	
	protected void fileOpen() {
		JFileChooser chooser = null;
		if (currentFile == null) {
			chooser = new JFileChooser(startDirectory);
		} else {
			chooser = new JFileChooser(currentFile);
		}
			
		int returnVal = chooser.showOpenDialog(gw);
		if (returnVal == JFileChooser.APPROVE_OPTION) {

			graph.clear();
			currentFile = chooser.getSelectedFile();
			graph.loadAll(currentFile);
			gp.update(gp.getGraphics());
		}
	}
	

	protected void fileOpenXML() {
		JFileChooser chooser = null;
		if (currentFile == null) {
			chooser = new JFileChooser(startDirectory);
		} else {
			chooser = new JFileChooser(currentFile);
		}
			
		int returnVal = chooser.showOpenDialog(gw);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			currentFile = chooser.getSelectedFile();
			generalXML.loadGraph(currentFile);
		}
		gp.update(gp.getGraphics());

	}


	protected void fileSave() {
		if (currentFile == null) {
			fileSaveAs();
		} else {
			if (currentFile.isDirectory()) {
				fileSaveAs();
			} else {
				graph.saveAll(currentFile);
				gp.update(gp.getGraphics());
			}
		}
	}


	protected void fileSaveAs() {
		JFileChooser chooser = null;
		if (currentFile == null) {
			chooser = new JFileChooser(startDirectory);
		} else {
			chooser = new JFileChooser(currentFile);
			if (!currentFile.isDirectory()) {
				chooser.setSelectedFile(currentFile);
			}
		}
		int returnVal = chooser.showSaveDialog(gw);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			currentFile = chooser.getSelectedFile();
			graph.saveAll(currentFile);
		}
	}


	protected void fileSaveSimple() {
		JFileChooser chooser = null;
		if (currentFile == null) {
			chooser = new JFileChooser(startDirectory);
		} else {
			chooser = new JFileChooser(currentFile);
			if (!currentFile.isDirectory()) {
				chooser.setSelectedFile(currentFile);
			}
		}
		int returnVal = chooser.showSaveDialog(gw);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			currentFile = chooser.getSelectedFile();
			graph.saveSimple(currentFile);
		}
	}



	protected void fileSaveXML() {
		JFileChooser chooser = null;
		if (currentFile == null) {
			chooser = new JFileChooser(startDirectory);
		} else {
			chooser = new JFileChooser(currentFile);
		}
		int returnVal = chooser.showSaveDialog(gw);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			currentFile = chooser.getSelectedFile();
			generalXML.saveGraph(currentFile);
		}
	}


	protected void filePNG() {

		JFileChooser chooser = null;
		File pngFile = null;
		if (currentFile == null) {
			chooser = new JFileChooser(startDirectory);
		} else {
			pngFile = new File(currentFile.getName()+".png");
			chooser = new JFileChooser(pngFile);
			if (!currentFile.isDirectory()) {
				chooser.setSelectedFile(currentFile);
			}
		}

		if (pngFile == null)
			return;
		try {
			BufferedImage image = new BufferedImage(getWidth(), getHeight(),BufferedImage.TYPE_INT_RGB);
			paint(image.getGraphics());
			ImageIO.write(image,"png",pngFile);
		} catch (Exception e) {}
		return;
/*		JFileChooser chooser = null;
		File pngFile = null;
		if (currentFile == null) {
			chooser = new JFileChooser(startDirectory);
		} else {
			pngFile = new File(currentFile.getName()+".png");
			chooser = new JFileChooser(pngFile);
			if (!currentFile.isDirectory()) {
				chooser.setSelectedFile(currentFile);
			}
		}
		int returnVal = chooser.showSaveDialog(gw);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File pngFile = chooser.getSelectedFile();

			int maxX = Integer.MIN_VALUE;
			int minX = Integer.MAX_VALUE;
			int maxY = Integer.MIN_VALUE;
			int minY = Integer.MAX_VALUE;

			while(Node node : getNodes()) {
				if(node.getX() > maxX) {
					maxX = node.getX();
				}
				if(node.getX() < minX) {
					minX = node.getX();
				}
				if(node.getY() > maxY) {
					maxY = node.getY();
				}
				if(node.getY() < minY) {
					minY = node.getY();
				}
			}

			BufferedImage image = new BufferedImage(maxX-minX+50,maxY-minY+50,BufferedImage.TYPE_INT_RGB);



			ImageIO.write(image,"png",pngFile);
		}
*/	}

	
	
	protected void fileSaveSVG() {
		JFileChooser chooser = null;
		if (currentFile == null) {
			chooser = new JFileChooser(startDirectory);
		} else {
			chooser = new JFileChooser(currentFile);
			if (!currentFile.isDirectory()) {
				chooser.setSelectedFile(currentFile);
			}
		}
		int returnVal = chooser.showSaveDialog(gw);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			currentFile = chooser.getSelectedFile();
			getGraphPanel().saveCirclesSVG(currentFile);
		}
	}
	

	protected void fileExit() {
		System.exit(0);
	}


	protected void editSelectAll() {
		gp.getSelection().addNodes(graph.getNodes());
		gp.getSelection().addEdges(graph.getEdges());
		gp.repaint();
	}


	public void actionPerformed(ActionEvent event) {
		JMenuItem source = (JMenuItem)(event.getSource());

		for(GraphView v : gp.getGraphViewList()) {
			if (v.getMenuText().equals(source.getText())) {
				v.view();
				repaint();
				return;
			}
		}

		for(GraphDrawer d : gp.getGraphDrawerList()) {
			if (d.getMenuText().equals(source.getText())) {
				d.layout();
				repaint();
				return;
			}
		}

		for(GraphUtility u : gp.getGraphUtilityList()) {
			if (u.getMenuText().equals(source.getText())) {
				u.apply();
				repaint();
				return;
			}
		}

		for(GraphExperiment ge : gp.getGraphExperimentList()) {
			if (ge.getMenuText().equals(source.getText())) {
				ge.experiment();
				repaint();
				return;
			}
		}

	}

}



