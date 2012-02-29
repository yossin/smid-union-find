package mta.ads.smid.model;

import mta.ads.smid.model.IUFkForestException.NameOutOfRangeException;



/**
 * Data Structure for managing k-tree nodes
 * <br> the nodes are saved in an array and are issued by IDs (the id is the index of the node in the array)
 * @author Yossi Naor & Yosi Zilberberg
 *
 */
/**
 * @author Yossi Naor & Yosi Zilberberg
 *
 */
/**
 * @author Yossi Naor & Yosi Zilberberg
 *
 */
class NodeManager{
	/**
	 * node array. it's size is (n*7)/3+1;
	 */
	private final Node nodes[];
	/**
	 * available list IDs (indexes) of nodes in nodes array.  
	 * <br>unavailable value: NULL_ID = (-1)
	 * <br>available value: points to the next available position (index)
	 */
	private final int availableNodes[];
	/**
	 * first available node (index) in the available list 
	 * <br>NULL_ID = (-1) if none
	 */
	private int firstAvailableNode;
	/**
	 * Cache root IDs (indexes) by name
	 */
	private final int rootIds[];
	/**
	 * k-tree size
	 */
	private final int k;
	/**
	 * NULL_ID constant = -1
	 */
	final private static int NULL_ID=-1;
	
	/**
	 * Node interface.
	 * <u>implementations are:</u>
	 * <ul>
	 * <li>Root</il>
	 * <li>NonRoot</il>
	 * <li>Leaf</il>
	 * </ul>
	 * @author Yossi Naor & Yosi Zilberberg
	 *
	 */
	static interface Node {}
	/**
	 * NonRoot element. 
	 * <br>a non root element contains a parent.
	 * <br>the parent can be replaced only by NodeManager
	 * @author Yossi Naor & Yosi Zilberberg
	 *
	 */
	static class NonRoot implements Node {
		/**
		 * parent id 
		 */
		private int parentId;
		/**
		 * @param parentId parent id
		 */
		NonRoot(int parentId) {
			this.parentId = parentId;
		}
		/**
		 * @return parent id
		 */
		int getParentId() {
			return parentId;
		}
	}
	/**
	 * Leaf element. 
	 * <br>a leaf element extends <i>NonRoot</i> element.
	 * <br>it contains the name of the leaf. once the name is set it cannot be changed.
	 * @author Yossi Naor & Yosi Zilberberg
	 *
	 */
	static class Leaf extends NonRoot{
		/**
		 * the name of the leaf
		 */
		final int name;
		/**
		 * @param name the name of the leaf
		 * @param parentId parent id
		 */
		Leaf(int name, int parentId) {
			super(parentId);
			this.name=name;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			StringBuilder out = new StringBuilder();
			out.append("[Leaf: name=").append(name)
			.append("]");
			return out.toString();
		}
	}
	class Root implements Node{
		final private int id;
		final int height;
		final int name;
		final private int sonsId[];
		private int numberOfSons=0;

		Root(int id, int name, int height){
			this.id=id;
			this.name=name;
			this.height=height;
			this.sonsId=new int[k];
		}
		void addSon(int sonId){
			sonsId[numberOfSons++]=sonId;
		}
		int getSonId(int index) {
			return sonsId[index];
		}
		int getNumberOfSons() {
			return numberOfSons;
		}
		int getId() {
			return id;
		}
		@Override
		public String toString() {
			StringBuilder out = new StringBuilder();
			out.append("[Root: id=").append(id)
				.append(", name=").append(name)
				.append(", height=").append(height)
				.append(", numberOfSons=").append(numberOfSons)
				.append("]");
			return out.toString();
		}
	}
	NodeManager(int k, int n) {
		this.k=k;
		int maxNodes=(n*7)/3+1;
		nodes=new Node[maxNodes];
		availableNodes=new int[maxNodes];
		rootIds=new int[n];
		
		// create a leaf which is linked into it's root
		for (int i=0; i<n; i++){
			int rootId=n+i;
			Root root = new Root(rootId, i,1);
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

	// return root or null (if name is not root)
	Root getRoot(int name){
		int id = rootIds[name];
		if (id == NULL_ID) {
			return null;
		} else {
			return (Root)nodes[id];
		}
	}
		
	Leaf getLeaf(int name) throws NameOutOfRangeException{
		return (Leaf)nodes[name];
	}
	NonRoot getNonRoot(int id){
		return (NonRoot)nodes[id];
	}
	
	Node getNode(int id){
		return nodes[id];
	}

	void disposeRoot(int name){
		int id=rootIds[name];
		nodes[id]=null;
		availableNodes[id]=firstAvailableNode;
		firstAvailableNode=id; 
		rootIds[name]=NULL_ID;
	}

	Root createRoot(int name, int height, Root rootChild1, Root rootChild2){
		int rootId = firstAvailableNode;
		Root root = new Root(rootId, name, height);
		addRootAsChild(rootChild1, root);
		addRootAsChild(rootChild2, root);
		rootIds[name]=rootId;
		assign(root, rootId);
		return root;
	}
	
	private void assign(Node node, int i){
		try {
		nodes[i]=node;
		firstAvailableNode=availableNodes[i];
		availableNodes[i]=NULL_ID;
		} catch (ArrayIndexOutOfBoundsException e){
			for (int j=0;j<availableNodes.length;j++){
				
			}
			
			e.printStackTrace();
			throw e;
		}
	}

	private void addRootAsChild(Root childRoot, Root parentRoot){
		int childRootId = childRoot.getId();
		parentRoot.addSon(childRootId);
		nodes[childRootId] = new NonRoot(parentRoot.getId());
		rootIds[childRoot.name]=NULL_ID;
	}
	
	// link sons of root into a different node (by it's Id) 
	void upLink(int intoNodeId, Root from) throws IUFkForestException{
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