package mta.ads.smid.app.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Paint;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
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
import edu.uci.ics.jung.visualization.VisualizationModel;
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

public class IUFkForestJPanel extends JPanel implements IUFkEvent{
	private static final long serialVersionUID = -4239061624657394397L;
	private DelegateForest<Integer,String> forest = null;
    private IUFkForest iufkForest;
    final private VisualizationViewer<Integer,String> viewer;
    private TreeLayout<Integer,String> layout = null;
    final private IUFkForestUnionModel unionModel;
	final private MultiPickedState<Integer> multiPickedState;
    private Timer timer = new Timer();
    private int k;
    
    public static interface IUFkForestUnionModel{
    	void union(int r, int s) throws IUFkForestException;
    	void initialize(int n);
    }
    public class IUFkForestUnionModelImpl implements IUFkForestUnionModel{
		private IUFkForestUnionModelImpl(){}
		public void union(int r, int s) throws IUFkForestException{
			if (iufkForest != null){
				iufkForest.union(r, s);
			}
		}
		@Override
		public void initialize(int n) {
			initializeForest(n);
			
		}
    }
    
    
    private class VertexColorTransformer implements Transformer<Integer, Paint>{

		@Override
		public Paint transform(Integer id) {
			if (viewer.getPickedVertexState().isPicked(id)){
				return Color.YELLOW;
			}
			int n = iufkForest.getN();
			if (id <n){
				return Color.GREEN;
			} else if (iufkForest.isRoot(id)){
				return Color.CYAN;
			}
			return Color.RED;
		}
    	
    }

    
    public IUFkForestJPanel(MultiPickedState<Integer> multiPickedState, int k, int n){
    	super(new BorderLayout());
    	this.k=k;
    	this.multiPickedState=multiPickedState;
    	unionModel = new IUFkForestUnionModelImpl();
    	forest = new DelegateForest<Integer,String>();
    	layout = new  TreeLayout<Integer,String>(forest);
    	VisualizationModel<Integer,String> model = new DefaultVisualizationModel<Integer,String>(layout);
    	
        viewer = new VisualizationViewer<Integer,String>(model);
        Renderer<Integer, String> renderer = viewer .getRenderer();
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
    
    private void createIUFkForest(int n){
    	if (iufkForest != null){
    		iufkForest.removeObserver(this);
    	}
        iufkForest = new IUFkForest(k, n);
        iufkForest.addObserver(this);
    }
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
    private static String createEdge(int v1, int v2){
    	return new StringBuilder().append(v1).append("-").append(v2).toString();
    }
    
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
    
    private synchronized void paintTransition(){
    	TreeLayout<Integer,String> newLayout = new  TreeLayout<Integer,String>(forest);
    	LayoutTransition<Integer,String> layoutTransition = new LayoutTransition<Integer,String>(viewer, layout, newLayout);
    	Animator animator = new Animator(layoutTransition);
    	animator.start();
    	this.layout=newLayout;
    }
    
    private void pick(List<Integer> children, Integer ...v){
    	PickedState<Integer> pickedState = viewer.getRenderContext().getPickedVertexState();
		pickedState.clear();
    	for (int x:v){
    		pickedState.pick(x, true);
    	}
    	if (children != null){
	    	for (int x:children){
	    		pickedState.pick(x, true);
	    	}
    	}
    }

	public void initializeForest(int k, int n){
		this.k=k;
		initializeForest(n);
	}

	@Override
	public void union(int oldLeaf, int newLeaf, int oldRoot, int newRoot, List<Integer> children) {
		pick(null, oldLeaf, newLeaf);
		
		Collection<Integer> children1 = new LinkedList<Integer>(forest.getChildren(oldRoot));
		Collection<String> edges1 = new LinkedList<String>(forest.getChildEdges(oldRoot));
		
		for (String e: edges1){
			forest.removeEdge(e, false);
		}
		
		for (int child: children1){
			String newEdge=createEdge(newRoot, child);
			forest.addEdge(newEdge, newRoot, child);
		}
		forest.removeVertex(oldRoot);

		scheduleTransition();
	}

	@Override
	public void union(int leaf1, int leaf2, int root, int child1, int child2) {
		pick(null,leaf1, leaf2);
		if (forest.containsVertex(root)==false){
			forest.addVertex(root);
		}
		forest.addEdge(createEdge(root, child1), root, child1);
		forest.addEdge(createEdge(root, child2), root, child2);
		scheduleTransition();
	}
	
	public IUFkForestUnionModel getUnionModel() {
		return unionModel;
	}
	
	public Set<Integer> getPiked(){
		return viewer.getRenderContext().getPickedVertexState().getPicked();
	}
	
	public MultiPickedState<Integer> getMultiPickedState(){
		return multiPickedState;
	}


}