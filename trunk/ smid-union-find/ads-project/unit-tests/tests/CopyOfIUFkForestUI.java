package tests;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JPanel;

import mta.ads.smid.model.IUFkForest;
import mta.ads.smid.model.IUFkForestException;
import mta.ads.smid.model.IUFkEvent;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.layout.LayoutTransition;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.util.Animator;

public class CopyOfIUFkForestUI extends JApplet implements IUFkEvent{

	private static final long serialVersionUID = 4710225191268505805L;
	private Forest<Integer,Integer> forest = null;
    private VisualizationViewer<Integer,Integer> viewer = null;
    private TreeLayout<Integer,Integer> layout = null;
    private IUFkForest iufkForest;

    private Timer timer;

    private static Forest<Integer,Integer> createForest(int n){
    	DelegateForest<Integer, Integer> forest = new DelegateForest<Integer, Integer>();
    	for (int i=0;i<n;i++){
    		DelegateTree<Integer,Integer> tree = new DelegateTree<Integer, Integer>();
    		int v1=n+i;
    		int v2=i;
    		int e=i;
    		tree.addVertex(v1);
    		tree.addChild(e, v1, v2);
    		forest.addTree(tree);
    	}
    	return forest;
    }
    
    @Override
    public void init() {
        timer = new Timer();
        int n=10;
        int k=2;
        
        iufkForest = new IUFkForest(k, n);
        iufkForest.addObserver(this);
        
    	forest = createForest(n);
    	
    	
    	layout = new  TreeLayout<Integer,Integer>(forest);
    	VisualizationModel<Integer,Integer> model = new DefaultVisualizationModel<Integer,Integer>(layout);
        Dimension dimension = new Dimension(800,800);

        viewer = new VisualizationViewer<Integer,Integer>(model, dimension);
        viewer .getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        viewer.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line());
        viewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Integer>());
        
        

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new GraphZoomScrollPane(viewer));
        
        DefaultModalGraphMouse<Integer, Integer> graphMouse = new DefaultModalGraphMouse<Integer, Integer>();
        viewer.setGraphMouse(graphMouse);

        final ScalingControl scaler = new CrossoverScalingControl();
        viewer.scaleToLayout(scaler);

        
        JButton plus = new JButton("+");
        plus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaler.scale(viewer, 1.1f, viewer.getCenter());
            }
        });
        JButton minus = new JButton("-");
        minus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaler.scale(viewer, 1/1.1f, viewer.getCenter());
            }
        });
        
        JPanel zoomPanel = new JPanel(new GridLayout(1,2));
        zoomPanel.setBorder(BorderFactory.createTitledBorder("Zoom"));


        JPanel modePanel = new JPanel();
        modePanel.setBorder(BorderFactory.createTitledBorder("Mouse Mode"));
        modePanel.add(graphMouse.getModeComboBox());
        
        JPanel controls = new JPanel();
        zoomPanel.add(plus);
        zoomPanel.add(minus);
        controls.add(zoomPanel);
        controls.add(modePanel);

        
        
        Container content = getContentPane();
        content.add(panel);
        content.add(controls, BorderLayout.SOUTH);

        
        
        
        
        //getContentPane().add(viewer);
    }
    
    
    private void paintTransition(){
    	TreeLayout<Integer,Integer> layout1 = new  TreeLayout<Integer,Integer>(forest);
    	LayoutTransition<Integer,Integer> lt = new LayoutTransition<Integer,Integer>(viewer, layout, layout1);
    	
    	Animator animator = new Animator(lt);
    	animator.start();
    	viewer.getRenderContext().getMultiLayerTransformer().setToIdentity();
    	viewer.repaint();
    	this.layout=layout1;

    }
    
    static int sleepTime=5000;
    private void schedule(int x, int y){
    	timer.schedule(new UnionTask(x, y), sleepTime+=2000);
    }
    
    @Override
    public void start() {
    	schedule(0, 1);
    	schedule(2, 3);
    	schedule(4, 5);
    	schedule(6, 7);
    	schedule(8, 9);

    	schedule(0, 2);
    	schedule(4, 6);

    	schedule(0, 4);

    	schedule(0, 8);

    	viewer.repaint();
    }
    
    @Override
    public void stop() {
    	timer.cancel();
    }
    
    class UnionTask extends TimerTask{
    	int x;
    	int y;
		public UnionTask(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public void run() {
			try {
				iufkForest.union(x, y);
			} catch (IUFkForestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	
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

	@Override
	public void union(int oldLeaf, int newLeaf, int oldRoot, int newRoot, List<Integer> children) {
		//pick(children, oldLeaf, newLeaf, oldRoot, newRoot);
		pick(null, oldLeaf, newLeaf);
		for (int child : children){
			forest.removeEdge(child);
			forest.addEdge(child, newRoot, child);
		}
		forest.removeVertex(oldRoot);
		paintTransition();
	}

	@Override
	public void union(int leaf1, int leaf2, int root, int child1, int child2) {
		//pick(null, root, child1, child2);
		//pick(null,leaf1, leaf2, root, child1, child2);
		pick(null,leaf1, leaf2);
		if (forest.containsVertex(root)==false){
			forest.addVertex(root);
		}
		forest.addEdge(child1, root, child1);
		forest.addEdge(child2, root, child2);
		paintTransition();
	}
    
}
