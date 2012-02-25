package mta.ads.smid.app.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import mta.ads.smid.model.IUFkEvent;
import mta.ads.smid.model.IUFkForest;
import mta.ads.smid.model.IUFkForestException;
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
import edu.uci.ics.jung.visualization.decorators.EdgeShape.Line;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.layout.LayoutTransition;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.util.Animator;

public class IUFkForestUI extends JApplet implements IUFkEvent{

	private static final long serialVersionUID = 4710225191268505805L;
	private DelegateForest<Integer,Integer> forest = null;
    private VisualizationViewer<Integer,Integer> viewer = null;
    private TreeLayout<Integer,Integer> layout = null;
    private IUFkForest iufkForest;

    private Timer timer;

    private void createForest(int n, int k){
        iufkForest = new IUFkForest(k, n);
        iufkForest.addObserver(this);

    	forest = new DelegateForest<Integer, Integer>();
    	for (int i=0;i<n;i++){
    		DelegateTree<Integer,Integer> tree = new DelegateTree<Integer, Integer>();
    		int v1=n+i;
    		int v2=i;
    		int e=i;
    		tree.addVertex(v1);
    		tree.addChild(e, v1, v2);
    		forest.addTree(tree);
    	}
    	paintTransition();
    }
    
    private class TreePanel extends JPanel{
    	TreePanel(){
    		super(new BorderLayout());
            timer = new Timer();
            
        	forest = new DelegateForest<Integer, Integer>();
        	
        	layout = new  TreeLayout<Integer,Integer>(forest);
        	VisualizationModel<Integer,Integer> model = new DefaultVisualizationModel<Integer,Integer>(layout);
            //Dimension dimension = new Dimension(800,800);

            viewer = new VisualizationViewer<Integer,Integer>(model);
            viewer .getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
            viewer.getRenderContext().setEdgeShapeTransformer(new Line<Integer, Integer>());
            viewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Integer>());
            
            

            add(new GraphZoomScrollPane(viewer));
    	}
    }
    
    private class ZoomPanel extends JPanel{
    	ZoomPanel(){
    		super(new GridLayout(1,2));
            setBorder(BorderFactory.createTitledBorder("Zoom"));
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
            add(plus);
            add(minus);

    	}
    }
    
    private class ModePanel extends JPanel{
    	ModePanel() {
        	DefaultModalGraphMouse<Integer, Integer> graphMouse = new DefaultModalGraphMouse<Integer, Integer>();
            viewer.setGraphMouse(graphMouse);

            setBorder(BorderFactory.createTitledBorder("Mouse Mode"));
            add(graphMouse.getModeComboBox());

		}
    }
    
    private class IUFkPanel extends JPanel{
    	IUFkPanel(){
    		super(new GridLayout(1,2));
            setBorder(BorderFactory.createTitledBorder("IUFk:(max size 99)"));
            final JLabel nLbl = new JLabel("n:");
            final JLabel kLbl = new JLabel("k:");
            final JTextField createNTxt = new JTextField("33");
            final JTextField createKTxt = new JTextField("2");
            JButton createBtn = new JButton("Create");
            createBtn.addActionListener(new ActionListener() {
    			@Override
    			public void actionPerformed(ActionEvent e) {
    				try {
    					int n=Integer.parseInt(createNTxt.getText());
    					int k=Integer.parseInt(createKTxt.getText());
    					if (n>-1 & n<100) {
    						if (k<=n & k>1){
    							createForest(n,k);
    							return;
    						} else {
    				            JOptionPane.showMessageDialog(null,new String("k must be between: 2<=k<=n"));
    				            return;
    						}
    					}
    				} catch (NumberFormatException ex){
    				}
    	            JOptionPane.showMessageDialog(null,new String("please insert numbers between 0-99."));
    			}
    		});

            JButton unionBtn = new JButton("Union");
            unionBtn.addActionListener(new ActionListener() {
    			@Override
    			public void actionPerformed(ActionEvent e) {
    		    	PickedState<Integer> pickedState = viewer.getRenderContext().getPickedVertexState();
    		    	Set<Integer> set = pickedState.getPicked();
    		    	if (set.size()>0 & set.size()<3){
    		    		Integer arr[] = set.toArray(new Integer[2]);
    		    		try {
    						iufkForest.union(arr[0], arr[1]);
    					} catch (IUFkForestException e1) {
    			            JOptionPane.showMessageDialog(null,new String("please pick leaves only!"));
    					}
    		    	} else {
    		            JOptionPane.showMessageDialog(null,new String("please pick 2 items exactly! (press shift for the second pick)"));
    		    	}
    			}
    		});

            
            
            add(createBtn);
            add(nLbl);
            add(createNTxt);
            add(kLbl);
            add(createKTxt);
            add(unionBtn);
    	}
    }
    
    private class ControlsPanel extends JPanel{
    	ControlsPanel(){
            // add zoom panel
            JPanel zoomPanel = new ZoomPanel();
            
            // add mode panel
            JPanel modePanel = new ModePanel();

            // add IUFk Size panel
            JPanel iufkPanel = new IUFkPanel();

            
            add(iufkPanel);
            add(zoomPanel);
            add(modePanel);

    	}
    }
    
    @Override
    public void init() {
        JPanel tree = new TreePanel();
        JPanel controls = new ControlsPanel(); 
        
        Container content = getContentPane();
        
        
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(tree);
        panel.add(controls, BorderLayout.SOUTH);
        
        add(panel);
		createForest(33,2);
        
        //content.add(tree);
        //content.add(controls, BorderLayout.SOUTH);

        
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
//    	schedule(0, 1);
//    	schedule(2, 3);
//    	schedule(4, 5);
//    	schedule(6, 7);
//    	schedule(8, 9);
//
//    	schedule(0, 2);
//    	schedule(4, 6);
//
//    	schedule(0, 4);
//
//    	schedule(0, 8);

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
