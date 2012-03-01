package mta.ads.smid.app.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Paint;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import mta.ads.smid.model.IUFkEvent;
import mta.ads.smid.model.IUFkForest;
import mta.ads.smid.model.IUFkForestException;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;
import edu.uci.ics.jung.visualization.decorators.EdgeShape.Line;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.layout.LayoutTransition;
import edu.uci.ics.jung.visualization.picking.MultiPickedState;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.util.Animator;

/**
 * Graphic JPanel for visualizing a union find forest.
 * <br> the graph is displayed using JUNG framework
 * 
 * @author Yossi Naor & Yosi Zilberberg
 *
 */
public class IUFkForestJPanel extends JPanel {
	private static final long serialVersionUID = -4239061624657394397L;
	/**
	 * forest for displaying on panel. 
	 * <br>vertexes are integers, edges are strings
	 */
	private DelegateForest<Integer,String> forest = null;
    /**
     * union find k-forest
     * <br><b>note: k could be changed</b>
     */
    private IUFkForest iufkForest;
    /**
     * union-find event
     */
    final private IUFkEventImpl iufkEvent;
    /**
     * visualization viewer
	 * <br>vertexes are integers, edges are strings
     */
    final private VisualizationViewer<Integer,String> viewer;
    /**
     * tree layout
     */
    private TreeLayout<Integer,String> layout = null;
    /**
     * union model for receiving events from UI
     */
    final private IUFkForestUnionModel unionModel;
    /**
     * timer for scheduling drawing events
     */
    private Timer timer = new Timer();
    /**
     * k-tree size
     */
    private int k;
    
    /**
     * Union Model Interface for displaying UI events
     * 
     * @author Yossi Naor & Yosi Zilberberg
     *
     */
    public static interface IUFkForestUnionModel{
    	/**
    	 * Union Event
    	 * @param r first leaf name 
    	 * @param s second leaf name
    	 * @throws IUFkForestException
    	 */
    	void union(int r, int s) throws IUFkForestException;
    	/**
    	 * Initialize Event
    	 * @param n number of elements
    	 */
    	void initialize(int n);
    }
    /**
     * Union Model Implementation for displaying UI events
     * <br>receive requests from UI and pass into model
     * 
     * @author Yossi Naor & Yosi Zilberberg
     *
     */
    public class IUFkForestUnionModelImpl implements IUFkForestUnionModel{
		/**
		 * 
		 */
		private IUFkForestUnionModelImpl(){}
		/* (non-Javadoc)
		 * @see mta.ads.smid.app.ui.IUFkForestJPanel.IUFkForestUnionModel#union(int, int)
		 */
		public void union(int r, int s) throws IUFkForestException{
			if (iufkForest != null){
				iufkForest.union(r, s);
			}
		}
		/* (non-Javadoc)
		 * @see mta.ads.smid.app.ui.IUFkForestJPanel.IUFkForestUnionModel#initialize(int)
		 */
		@Override
		public void initialize(int n) {
			initializeForest(n);
			
		}
    }
    
    
    /**
	 * Union Model Implementation for displaying model union events
	 * <br>receive events from UI and draw them
	 * 
	 * @author Yossi Naor & Yosi Zilberberg
	 *
	 */
	private class IUFkEventImpl implements IUFkEvent{
	
		/* (non-Javadoc)
		 * @see mta.ads.smid.model.IUFkEvent#union(int, int, int, int)
		 */
		@Override
		public void union(int fromLeafId, int intoLeafId, int fromRootId, int intoRootId) {
			pick(fromLeafId, intoLeafId);
			
			Collection<Integer> oldRootChildren = new LinkedList<Integer>(forest.getChildren(fromRootId));
			Collection<String> oldRootEdges = new LinkedList<String>(forest.getChildEdges(fromRootId));
			
			for (String e: oldRootEdges){
				forest.removeEdge(e, false);
			}
			
			for (int child: oldRootChildren){
				String newEdge=createEdge(intoRootId, child);
				forest.addEdge(newEdge, intoRootId, child);
			}
			forest.removeVertex(fromRootId);
	
			scheduleTransition();
		}
	
		/* (non-Javadoc)
		 * @see mta.ads.smid.model.IUFkEvent#union(int, int, int, int, int)
		 */
		@Override
		public void union(int leaf1Id, int leaf2Id, int newRootId, int childRootId1, int childRootId2) {
			pick(leaf1Id, leaf2Id);
			if (forest.containsVertex(newRootId)==false){
				forest.addVertex(newRootId);
			}
			forest.addEdge(createEdge(newRootId, childRootId1), newRootId, childRootId1);
			forest.addEdge(createEdge(newRootId, childRootId2), newRootId, childRootId2);
			scheduleTransition();
		}
	
	}
	/**
     * Vertex color transformer:
     * <br><u>assign the following colors for vertexes</u>
     * <ul>
     * <li>yellow for a picked vertex</li>
     * <li>green for a <i>Leaf</i></li>
     * <li>cyan for a <i>Root</i></li>
     * <li>red for a <i>NonRoot</i></li>
     * </ul> 
     * @author Yossi Naor & Yosi Zilberberg
     *
     */
    private class VertexColorTransformer implements Transformer<Integer, Paint>{

		/* (non-Javadoc)
		 * @see org.apache.commons.collections15.Transformer#transform(java.lang.Object)
		 */
		@Override
		public Paint transform(Integer id) {
			if (viewer.getPickedVertexState().isPicked(id)){
				return Color.YELLOW;
			}
			int n = iufkForest.getN();
			if (id <n){
				return Color.GREEN;
			}
			
			Integer parent = forest.getParent(id);
			if (parent==null){
				return Color.CYAN;
			}
			return Color.RED;
		}
    	
    }
    
    /**
     * Create an Improved Union-Find k-tree Panel
     * @param multiPickedState multi picked state for sharing picked vertexes with other panels
     * @param k k-tree size
     * @param n elements number
     */
    public IUFkForestJPanel(MultiPickedState<Integer> multiPickedState, int k, int n){
    	super(new BorderLayout());
    	this.k=k;
    	unionModel = new IUFkForestUnionModelImpl();
    	iufkEvent = new IUFkEventImpl();
    	forest = new DelegateForest<Integer,String>();
    	layout = new  TreeLayout<Integer,String>(forest);
        viewer = new VisualizationViewer<Integer,String>(new DefaultVisualizationModel<Integer,String>(layout));

        Renderer<Integer, String> renderer = viewer.getRenderer();
		renderer.getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
		
        RenderContext<Integer,String> renderContext = viewer.getRenderContext();
		renderContext.setEdgeShapeTransformer(new Line<Integer,String>());
        renderContext.setVertexLabelTransformer(new ToStringLabeller<Integer>());
        renderContext.setVertexFillPaintTransformer(new VertexColorTransformer());
        
        DefaultModalGraphMouse<Integer,String> graphMouse = new DefaultModalGraphMouse<Integer,String>();
        viewer.setGraphMouse(graphMouse);

        viewer.setPickedVertexState(multiPickedState);
        graphMouse.setMode(Mode.PICKING);
       
    	add(new GraphZoomScrollPane(viewer));
        initializeForest(n);
    }
    
    /**
     * "Factory method" create a new instance of IUFkForest 
     * <br>and register this instance as an Observer for IUFkEvent
     * @param n the number of elements
     */
    private void createIUFkForest(int n){
    	if (iufkForest != null){
    		iufkForest.removeObserver(iufkEvent);
    	}
        iufkForest = new IUFkForest(k, n);
        iufkForest.addObserver(iufkEvent);
    }
    
    /**
     * Initialize a simple forest with n elements linked to n roots.
     * @param n the number of elements
     */
    private void initializeForest(int n){
    	createIUFkForest(n);
    	forest = new DelegateForest<Integer,String>();
    	for (int i=0;i<n;i++){
    		DelegateTree<Integer,String> tree = new DelegateTree<Integer,String>();
    		int v1=n+i;
    		int v2=i;
    		String e=createEdge(v1, v2);
    		tree.addVertex(v1);
    		tree.addChild(e, v1, v2);
    		forest.addTree(tree);
    	}
    	scheduleTransition();
    }
    
    /**
     * Create an edge connecting 2 vertexes
     * <br>edge name is: v1-v2. for example 0-1 (for: v0=0, v1=1)
     * @param v1 parent vertex
     * @param v2 son vertex
     * @return edge name
     */
    private static String createEdge(int v1, int v2){
    	return new StringBuilder().append(v1).append("-").append(v2).toString();
    }
    
    /**
     * schedule transition
     */
    private void scheduleTransition(){
   		timer.cancel();
   		timer = new Timer();
    	timer.schedule(new TimerTask() {
			@Override
			public void run() {
				paintTransition();
			}
		}, 200);
    }
    
    /**
     * print transition
     */
    private synchronized void paintTransition(){
    	TreeLayout<Integer,String> newLayout = new TreeLayout<Integer,String>(forest);
    	LayoutTransition<Integer,String> layoutTransition = new LayoutTransition<Integer,String>(viewer, layout, newLayout);
    	Animator animator = new Animator(layoutTransition);
    	animator.start();
    	this.layout=newLayout;
    }
    
    /**
     * pick vertexes
     * @param vertexes vertex list to pick
     */
    private void pick(int ... vertexes){
    	PickedState<Integer> pickedState = viewer.getRenderContext().getPickedVertexState();
		pickedState.clear();
    	for (int vertex :vertexes){
    		pickedState.pick(vertex, true);
    	}
    }

	/**
	 * initialize forest
	 * @param k k-size
	 * @param n element number
	 */
	public void initializeForest(int k, int n){
		this.k=k;
		initializeForest(n);
	}
	
	/**
	 * getter for union model
	 * @return union model
	 */
	public IUFkForestUnionModel getUnionModel() {
		return unionModel;
	}

}