package mta.ads.smid.model;

import java.util.LinkedList;
import java.util.List;

import mta.ads.smid.model.IUFkForestException.NameOutOfRangeException;



class NodeManager{
	private final Node nodes[];
	private final int availableNodes[];
	private int firstAvailableNode;
	private final int rootIds[];
	private final int k;
	final private static int NULL_ID=-1;
	
	static interface Node {}
	static class NonRoot implements Node {
		private int parentId;
		NonRoot(int parentId) {
			this.parentId = parentId;
		}
		int getParentId() {
			return parentId;
		}
	}
	static class Leaf extends NonRoot{
		final int name;
		Leaf(int name, int parentId) {
			super(parentId);
			this.name=name;
		}
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
		//TODO: check with AMIR if we can use List<Integer> instead
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
		//TODO: exact number?
		int maxNodes=(n*7)/3+1;
		//int maxNodes=2*n+1;
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

	Root createRoot(int name, int height, int rootChild1, int rootChild2){
		int rootId = firstAvailableNode;
		Root root = new Root(rootId, name, height);
		// TODO: change root ID passing
		addRootAsChild(rootChild1, root, rootId);
		addRootAsChild(rootChild2, root, rootId);
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
	// TODO: change root ID passing
	private void addRootAsChild(int childRoot, Root parentRoot, int parentRootId){
		int childRootId = rootIds[childRoot];
		parentRoot.addSon(childRootId);
		nodes[childRootId] = new NonRoot(parentRootId);
		rootIds[childRoot]=NULL_ID;
	}
	
	// link sons of root into a different node (by it's Id) 
	List<Integer> upLink(int intoNodeId, Root from) throws IUFkForestException{
		//TODO: replace this if AMIR approves using lists..
		List<Integer> linkedSons = new LinkedList<Integer>();
		// up-link (son->parent): link sons to their new root 
		for (int i=0;i<from.getNumberOfSons();i++){
			int sonId = from.getSonId(i);
			linkedSons.add(sonId);
			NonRoot nonRoot = getNonRoot(sonId);
			nonRoot.parentId=intoNodeId;
		}
		// dispose 'old' root
		disposeRoot(from.name);
		return linkedSons;
	}


}