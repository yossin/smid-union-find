package tests;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import mta.ads.smid.model.IUFkEvent;
import mta.ads.smid.model.IUFkForest;
import mta.ads.smid.model.IUFkForestException;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape.Line;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.layout.LayoutTransition;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.util.Animator;

public class IUFkForestDemoPanel extends JPanel implements IUFkEvent{
	private static final long serialVersionUID = -4239061624657394397L;
	/**
	 * @uml.property  name="forest"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private DelegateForest<Integer,Integer> forest = null;
    /**
	 * @uml.property  name="iufkForest"
	 * @uml.associationEnd  
	 */
    private IUFkForest iufkForest;
    /**
	 * @uml.property  name="viewer"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private VisualizationViewer<Integer,Integer> viewer = null;
    /**
	 * @uml.property  name="layout"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private TreeLayout<Integer,Integer> layout = null;
	
    /**
	 * @uml.property  name="timer"
	 */
    Timer timer = new Timer();
    
    public IUFkForestDemoPanel(){
    	forest = new DelegateForest<Integer, Integer>();
    	layout = new  TreeLayout<Integer,Integer>(forest);
    	VisualizationModel<Integer,Integer> model = new DefaultVisualizationModel<Integer,Integer>(layout);

        viewer = new VisualizationViewer<Integer,Integer>(model);
        viewer .getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        viewer.getRenderContext().setEdgeShapeTransformer(new Line<Integer, Integer>());
        viewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Integer>());

        
        DefaultModalGraphMouse<Integer, Integer> graphMouse = new DefaultModalGraphMouse<Integer, Integer>();
        viewer.setGraphMouse(graphMouse);
        
        

        //add(new GraphZoomScrollPane(viewer));
        add(viewer);
        int i=5000;
        timer.schedule(new CreateTask(), i+=1000);
        timer.schedule(new UnionTask(0,1), i+=1000);
        timer.schedule(new UnionTask(2,3), i+=1000);
        timer.schedule(new UnionTask(4,5), i+=1000);
        timer.schedule(new UnionTask(6,7), i+=1000);
        
        timer.schedule(new UnionTask(0,2), i+=1000);
        timer.schedule(new UnionTask(4,6), i+=1000);
        
        timer.schedule(new UnionTask(0,4), i+=1000);
    }
    
    private class CreateTask extends TimerTask{

		@Override
		public void run() {
	        createForest(10, 2);
		}
    }
    
    private class UnionTask extends TimerTask{
    	int x;
		int y;

		UnionTask(int x, int y) {
			super();
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
    private void paintTransition(){
    	TreeLayout<Integer,Integer> layout1 = new  TreeLayout<Integer,Integer>(forest);
    	LayoutTransition<Integer,Integer> lt = new LayoutTransition<Integer,Integer>(viewer, layout, layout1);
    	
    	Animator animator = new Animator(lt);
    	animator.start();
    	viewer.getRenderContext().getMultiLayerTransformer().setToIdentity();
    	viewer.repaint();
    	this.layout=layout1;

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
	public void union(int oldLeaf, int newLeaf, int oldRoot, int newRoot) {
		List<Integer> children = new LinkedList<Integer>(forest.getChildren(oldRoot));
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
	
	
	/*


        // add IUFk Size panel
        JPanel iufkPanel = new JPanel(new GridLayout(1,2));
        iufkPanel.setBorder(BorderFactory.createTitledBorder("IUFk:(max size 99)"));
        final JLabel kLbl = new JLabel("k:");
        final JTextField createNTxt = new JTextField("10");
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

        
        
        iufkPanel.add(createBtn);
        iufkPanel.add(nLbl);
        iufkPanel.add(createNTxt);
        iufkPanel.add(kLbl);
        iufkPanel.add(createKTxt);
        iufkPanel.add(unionBtn);

	 */


}