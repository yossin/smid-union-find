package mta.ads.smid.model.nm;




/**
 * Data Structure for managing k-tree nodes
 * <br> the nodes are saved in an array and are issued by IDs (the id is the index of the node in the array)
 * @author Yossi Naor & Yosi Zilberberg
 *
 */
public class NodeManager{
	/**
	 * node array. it's size is (n*7)/3+1;
	 * @uml.property  name="nodes"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	private final Node nodes[];
	/**
	 * available list IDs (indexes) of nodes in nodes array.   <br>unavailable value: NULL_ID = (-1) <br>available value: points to the next available position (index)
	 * @uml.property  name="availableNodes" multiplicity="(0 -1)" dimension="1"
	 */
	private final int availableNodes[];
	/**
	 * first available node (index) in the available list  <br>NULL_ID = (-1) if none
	 * @uml.property  name="firstAvailableNode"
	 */
	private int firstAvailableNode;
	/**
	 * Cache root IDs (indexes) by name
	 * @uml.property  name="rootIds" multiplicity="(0 -1)" dimension="1"
	 */
	private final int rootIds[];
	/**
	 * k-tree size
	 * @uml.property  name="k"
	 */
	private final int k;
	/**
	 * NULL_ID constant = -1
	 */
	final private static int NULL_ID=-1;
	
	/**
	 * NodeManager Constructor 
	 * <br><u>implementation highlights:</u>
	 * <ul>
	 * <li>store k as a member</li>
	 * <li>manage a maximum number of (n*7)/3+1 nodes</li>
	 * <li>create n leaves managed by n roots (each leave has it's own root)</li>
	 * <li>initialize available </li>
	 * <li>initialize available available nodes</li>
	 * <ul>
	 * <li>initialize availableNodes[] array</li>
	 * <li>initialize firstAvailableNode for the first available entry</li>
	 * </ul>
	 * </ul>
	 * 
	 * @param k the k-tree size
	 * @param n the number of elements to manage
	 */
	public NodeManager(int k, int n) {
		this.k=k;
		int maxNodes=(n*7)/3+1;
		nodes=new Node[maxNodes];
		availableNodes=new int[maxNodes];
		rootIds=new int[n];
		
		// create a leaf which is linked into it's root
		for (int i=0; i<n; i++){
			int rootId=n+i;
			Root root = new Root(rootId, i,1,k);
			Leaf leaf = new Leaf(i,rootId);
			root.addSon(i);
			nodes[rootId]=root;
			nodes[i]=leaf;
			availableNodes[i]=availableNodes[n+i]=NULL_ID;
			rootIds[i]=rootId;
		}
		
		// set available nodes
		for (int i=2*n;i<maxNodes;i++){
			availableNodes[i]=i+1;
		}
		availableNodes[maxNodes-1]=NULL_ID;
		firstAvailableNode=maxNodes<=2*n?NULL_ID:2*n;
	}

	/**
	 * return a matching <i>Root</i> element by a given name.
	 * <br>if there is no corresponding root matching that name return null.
	 * <br><b>at this point we assume that name is in range</b>
	 * @param name requested <i>Root</i> name
	 * @return <i>Root</i> element
	 */
	public Root getRoot(int name){
		int id = rootIds[name];
		if (id == NULL_ID) {
			return null;
		} else {
			return (Root)nodes[id];
		}
	}
		
	/**
	 * return a matching <i>Leaf</i> element by a given name.
	 * <br><b>at this point we assume that name is in range</b>
	 * @param name requested <i>Leaf</i> name
	 * @return <i>Leaf</i> element
	 */
	public Leaf getLeaf(int name){
		return (Leaf)nodes[name];
	}
	/**
	 * return a matching <i>NonRoot</i> element by a given id.
	 * <br><b>at this point we assume that id is in range</b>
	 * @param name requested <i>NonRoot</i> id
	 * @return <i>NonRoot</i> element
	 */
	private NonRoot getNonRoot(int id){
		return (NonRoot)nodes[id];
	}
	
	/**
	 * return a matching <i>Node</i> element by a given id.
	 * <br><b>at this point we assume that id is in range</b>
	 * @param name requested <i>Node</i> id
	 * @return <i>Node</i> element
	 */
	public Node getNode(int id){
		return nodes[id];
	}

	/**
	 * Dispose a <i>Root</i> element by it's name
	 * <br><u>implementation highlights:</u>
	 * <ul>
	 * <li>update available nodes</li>
	 * <li>remove root from cached roots array</li>
	 * </ul>
	 * <br><b>at this point we assume that id is in range</b>
	 * @param name requested <i>Root</i> name to be disposed
	 */
	private void disposeRoot(int name){
		int id=rootIds[name];
		nodes[id]=null;
		availableNodes[id]=firstAvailableNode;
		firstAvailableNode=id; 
		rootIds[name]=NULL_ID;
	}

	
	/**
	 * Create a new <i>Root</i> element (for union case 2)
	 * <br><u>implementation highlights:</u>
	 * <ul>
	 * <li>update available nodes</li>
	 * <li>convert rootChild1 and rootChild2 into a <i>NonRoot</i> nodes and set them as children of the new root</li>
	 * </ul>
	 * @param name the name of the new root
	 * @param height the height of the new root
	 * @param rootChild1 first root child to be converted into <i>NonRoot</i> child
	 * @param rootChild2 second root child to be converted into <i>NonRoot</i> child
	 * @return the created root
	 */
	public Root createRoot(int name, int height, Root rootChild1, Root rootChild2){
		int rootId = firstAvailableNode;
		Root root = new Root(rootId, name, height, k);
		addRootAsChild(rootChild1, root);
		addRootAsChild(rootChild2, root);
		rootIds[name]=rootId;
		assign(root, rootId);
		return root;
	}
	
	/**
	 * Assign a new Node at index position i
	 * <br><u>implementation highlights:</u>
	 * <ul>
	 * <li>update nodes[] array</li>
	 * <li>update available nodes</li>
	 * </ul>
	 * @param node the node to be assigned
	 * @param i index at the nodes[] array
	 */
	private void assign(Node node, int i){
		nodes[i]=node;
		firstAvailableNode=availableNodes[i];
		availableNodes[i]=NULL_ID;
	}

	/**
	 * Add Root as Child (for union case 2)
	 * @param childRoot child root to be converted into <i>NonRoot</i> element
	 * @param parentRoot parent root for holding the child root
	 */
	private void addRootAsChild(Root childRoot, Root parentRoot){
		int childRootId = childRoot.getId();
		parentRoot.addSon(childRootId);
		nodes[childRootId] = new NonRoot(parentRoot.getId());
		rootIds[childRoot.name]=NULL_ID;
	}
	
	/**
	 * Link the sons of a root into a different node (can be a <i>Root</i> element or a <i>NonRoot</i> element)
	 * <br><u>implementation highlights:</u>
	 * <ul>
	 * <li>all sons of the source root ('from') are linked into the new node id. son->parent link</li>
	 * <li>the source root is disposed</li>
	 * </ul>
	 * @param intoNodeId the destination node 
	 * @param from the source root, that it's sons are transfered
	 */
	public void upLink(int intoNodeId, Root from) {
		// up-link (son->parent): link sons to their new root 
		for (int i=0;i<from.getNumberOfSons();i++){
			int sonId = from.getSonId(i);
			NonRoot nonRoot = getNonRoot(sonId);
			nonRoot.parentId=intoNodeId;
		}
		// dispose 'old' root
		disposeRoot(from.name);
	}


}