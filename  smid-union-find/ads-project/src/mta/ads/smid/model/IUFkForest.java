package mta.ads.smid.model;

import java.util.List;

import mta.ads.smid.model.IUFkForestException.NameOutOfRangeException;
import mta.ads.smid.model.NodeManager.Leaf;
import mta.ads.smid.model.NodeManager.Node;
import mta.ads.smid.model.NodeManager.NonRoot;
import mta.ads.smid.model.NodeManager.Root;
import mta.ads.smid.patterns.Observable;




public class IUFkForest extends Observable<IUFkEvent>{
	private final int k;
	private final int n;
	private final NodeManager nodeManager;
	private final Statistics statistics;
	private final IUFkEvent dispatcher;

	
	public IUFkForest(int k, int n){
		this.k=k;
		this.n=n;
		this.nodeManager = new NodeManager(k,n);
		this.statistics = new Statistics(n,n,n,k);
		this.dispatcher = getEventDispatcher();
	}
	
	
	private void verifyNameInRange(int name) throws NameOutOfRangeException{
		if (name>=n || name <0) throw new NameOutOfRangeException(name, n);
	}

	
	// TODO: add comment, we checked together
	private Root unionCase1(int intoLeaf, int fromLeaf, Root into, Root from) throws IUFkForestException{
		// up-link (son->root): link sons to their new root 
		List<Integer> childList = nodeManager.upLink(into.getId(), from);
		// down-link (root->son): link root to new sons 
		for (int i=0;i<from.getNumberOfSons();i++){
			into.addSon(from.getSonId(i));
		}
		// notify listeners
		dispatcher.union(fromLeaf, intoLeaf, from.getId(), into.getId(), childList);
		// add statistics
		statistics.performUnionCase1();

		return into;
	}
	// TODO: new method
	private Root unionCase2(int leaf1, int leaf2, Root root1, Root root2) throws IUFkForestException{
		int name = root1.name;
		int height = root1.height+1;
		// create a new root that contains r,s as sons
		// TODO: change root ID passing
		Root t = nodeManager.createRoot(name, height, root1.name, root2.name);
		// notify listeners
		dispatcher.union(leaf1, leaf2, t.getId(), root1.getId(), root2.getId());
		// add statistics
		statistics.performUnionCase2(height);

		return t;
	}
	
	// link sons of 'from' into the first son of 'intoFirstSon'
	private Root unionCase3(int intoFirstSonLeaf, int fromLeaf, Root intoFirstSon, Root from) throws IUFkForestException{
		int intoId = intoFirstSon.getSonId(0);
		// up-link (son->root): link sons to their new root 
		List<Integer> childList = nodeManager.upLink(intoId, from);
		// notify listeners
		dispatcher.union(fromLeaf, intoFirstSonLeaf, from.getId(), intoId, childList);
		// add statistics
		statistics.performUnionCase3();

		return intoFirstSon;
	}
	
	
	
	private Root union(int leafR, int leafS, Root r, Root s) throws IUFkForestException{
		// case 1 + 2: equal heights
		if (r.height == s.height){
			// case 1: equal heights & number of total sons <= k
			if ((r.getNumberOfSons()+s.getNumberOfSons()) <= k){
				if (r.getNumberOfSons() >= s.getNumberOfSons()){
					// move s sons into r
					return unionCase1(leafR, leafS, r, s);
				} else {
					// move r sons into s 
					return unionCase1(leafS, leafR, s, r);
				}
			}
			// case 2: equal heights & number of total sons > k
			else {
				if (r.getNumberOfSons()<s.getNumberOfSons()){
					return unionCase2(leafS, leafR, s, r);
				}else {
					return unionCase2(leafR, leafS, r, s);
				}
			}
		}
		// case 3: different heights
		else {
			if (r.height > s.height){
				// link s sons to an arbitrary son of r
				return unionCase3(leafR, leafS, r, s);
			} else {
				// link r sons to an arbitrary son of s
				return unionCase3(leafS, leafR, s, r);
			}
		}
	}
	
	
	// recursive find
	private Root find(NonRoot nonRoot){
		int parentId = nonRoot.getParentId();
		Node parentNode = nodeManager.getNode(parentId);
		if (parentNode instanceof Root){
			return (Root)parentNode;
		} else {
			return find((NonRoot)parentNode);
		}
	}
	
	// union by names
	public void union (int r, int s) throws IUFkForestException{
		verifyNameInRange(r);
		verifyNameInRange(s);
		int rRoot = find(r);
		int sRoot = find(s);
		if (rRoot != sRoot){
			Root rootR = nodeManager.getRoot(rRoot);
			Root rootS = nodeManager.getRoot(sRoot);
			union(r,s,rootR, rootS);
		}
	}

	// find by name
	public int find (int name) throws IUFkForestException{
		verifyNameInRange(name);
		// first check if 'name' is root - O(1)
		Root root = nodeManager.getRoot(name);
		if (root == null){
			// 'name' is not root, get corresponding leaf
			Leaf leaf = nodeManager.getLeaf(name);
			// perform find - O(log/k(n))
			root=find(leaf);
			// add statistics
			statistics.increaseRecursiveFind();
		} else {
			// add statistics
			statistics.increaseDirectFind();
		}
		return root.name;
	}
	public Statistics getStatistics() {
		return statistics;
	}
	
	public int getK() {
		return k;
	}
	public int getN() {
		return n;
	}
	public boolean isRoot(int id){
		Node node = nodeManager.getNode(id);
		return node instanceof Root;
	}
}