package mta.ads.smid.model;

import java.util.List;

import mta.ads.smid.model.IUFkForestException.NameOutOfRangeException;
import mta.ads.smid.model.NodeManager.Leaf;
import mta.ads.smid.model.NodeManager.Node;
import mta.ads.smid.model.NodeManager.NonRoot;
import mta.ads.smid.model.NodeManager.Root;
import mta.ads.smid.patterns.Observable;


/**
 * A Data Structure Implementation for Union Find Problem, Having a Good Single-Operation Complexity.
 * <br>Based on the Michel H.M. Smid Document.
 * 
 * @author Yossi Naor & Yosi Zilberberg
 *
 */
public class IUFkForest extends Observable<IUFkEvent>{
	/**
	 * size of the k-tree
	 */
	private final int k;
	/**
	 * number of elements
	 */
	private final int n;
	/**
	 * data structure for managing tree node elements 
	 */
	private final NodeManager nodeManager;
	/**
	 * union & find statistics 
	 */
	private final Statistics statistics;
	/**
	 * for managing union events 
	 */
	private final IUFkEvent dispatcher;

	
	/**
	 * @param k The degree for each tree (k-tree)
	 * @param n The number of managed elements on the data structure (0,1,...,n-1)
	 */
	public IUFkForest(int k, int n){
		this.k=k;
		this.n=n;
		this.nodeManager = new NodeManager(k,n);
		this.statistics = new Statistics(n,n,n,k);
		this.dispatcher = getEventDispatcher();
	}
	
	
	/**
	 * Verifies that a given element name is in the correct range
	 * <br> The range is between 0 and (n-1).
	 * In case of an error throws NameOutOfRangeException exception.
	 * 
	 * @param name
	 * @throws NameOutOfRangeException
	 */
	private void verifyNameInRange(int name) throws NameOutOfRangeException{
		if (name>=n || name <0) throw new NameOutOfRangeException(name, n);
	}

	
	/**
	 * Union Case 1: union two trees, that their root have the same height and their number of sons <=k
	 * <br><u>perform the following:</u>
	 * <ul>
	 * <li>link sons from source tree to the root in the destination tree, disposing the source <i>Root</i> element</li>
	 * <li>link the root of the destination tree into the sons of the source tree</li>
	 * <li>notify listeners for performing a union</li>
	 * <li>write statistics</li>
	 * </ul>
	 * @param intoLeaf a given leaf name in the destination tree
	 * @param fromLeaf a given leaf name in the source tree
	 * @param into the <i>Root</i> element of the destination tree
	 * @param from the <i>Root</i> element of the source tree
	 * @return the <i>Root</i> element of the destination tree
	 * @throws IUFkForestException
	 */
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
	/**
	 * Union Case 2: union two trees (two roots), that their root have the same height and their number of sons > k
	 * <br><u>perform the following:</u>
	 * <ul>
	 * <li>create a new <i>Root</i> element</li>
	 * <ul>
	 * <li>add the two trees as sons of the new root, converting them into <i>NonRoot</i> nodes</li>
	 * <li>link the two trees (<i>NonRoot</i> nodes) into the new root</li>
	 * </ul>
	 * <li>notify listeners for performing a union</li>
	 * <li>write statistics</li>
	 * </ul>
	 * @param leaf1 a given leaf name in the first tree
	 * @param leaf2 a given leaf name in the second tree
	 * @param root1 the <i>Root</i> element of the first tree
	 * @param root2 the <i>Root</i> element of the second tree
	 * @return the new <i>Root</i> element
	 * @throws IUFkForestException
	 */
	private Root unionCase2(int leaf1, int leaf2, Root root1, Root root2) throws IUFkForestException{
		int name = root1.name;
		int height = root1.height+1;
		// create a new root that contains r,s as sons
		Root t = nodeManager.createRoot(name, height, root1.name, root2.name);
		// notify listeners
		dispatcher.union(leaf1, leaf2, t.getId(), root1.getId(), root2.getId());
		// add statistics
		statistics.performUnionCase2(height);

		return t;
	}
	
	/**
	 * Union Case 3: union two trees, in which the destination tree height is higher than the source tree.
	 * <br><u>perform the following:</u>
	 * <ul>
	 * <li>link sons from source tree to the first son of the root in the destination tree, disposing the source root element</li>
	 * <li>notify listeners for performing a union</li>
	 * <li>write statistics</li>
	 * </ul>
	 * @param intoFirstSonLeaf a given leaf name in the destination tree
	 * @param fromLeaf a given leaf name in the source tree
	 * @param intoFirstSon the <i>Root</i> element of the source tree
	 * @param from the <i>Root</i> element of the destination tree
	 * @return the <i>Root</i> element of the destination tree
	 * @throws IUFkForestException
	 */
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
	
	
	
	/**
	 * Performs a union by <i>Root</i> elements. 
	 * <br>The <i>Root</i> elements have already been searched before invoking this method. 
	 * <br>According to the properties of s and r root elements we invoke the correct case (1-3).
	 * <br><u>highlights for each case:</u>
	 * <ul>
	 * <li><b>case 1</b>: the root with the least sons is the source root</li>
	 * <li><b>case 2</b>: the 'name' of the new root will be set according to the root with most sons</li>
	 * </ul>
	 * @param leafR leaf name in tree R
	 * @param leafS leaf name in tree S
	 * @param r <i>Root</i> element of tree R
	 * @param s <i>Root</i> element of tree S
	 * @return the new <i>Root</i> element (could be s,r to a new <i>Root</i> element)
	 * @throws IUFkForestException
	 */
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
	
	
	/**
	 * Search for the <i>Root</i> element of a given <i>NonRoot</i> by climbing up the tree 
	 * @param nonRoot a <i>NonRoot</i> element
	 * @return the <i>Root</i> element
	 */
	private Root find(NonRoot nonRoot){
		int parentId = nonRoot.getParentId();
		Node parentNode = nodeManager.getNode(parentId);
		if (parentNode instanceof Root){
			return (Root)parentNode;
		} else {
			return find((NonRoot)parentNode);
		}
	}
	
	/**
	 * Union two leaves (in two different trees) 
	 * <br><u>implementation highlights:</u>
	 * <ul>
	 * <li>verify that r,s names are in range</li>
	 * <li>efficiently find <i>Root</i> elements for s,r</li>
	 * <li>if s,r already have the same <i>Root</i> element we do nothing, otherwise invoke the private union method</li>
	 * </ul>
	 * @param r leaf name in tree R (there might be a corresponding <i>Root</i> element matching that name)
	 * @param s leaf name in tree S (there might be a corresponding <i>Root</i> element matching that name)
	 * @throws IUFkForestException
	 */
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

	/**
	 * Find the root name for a given leaf name 
	 * <br><u>implementation highlights:</u>
	 * <ul>
	 * <li>check if there is a corresponding <i>Root</i> element matching the given name and return it (if exists) - <b>O(1)</b> </li>
	 * <li>otherwise, perform find recursively by invoking private find method - <b>O(log/k(n))</b></li>
	 * <li>add statistics (for reach case)</li>
	 * </ul>
	 * @param name a given leaf name (there might be a corresponding <i>Root</i> element matching that name)
	 * @return root name
	 * @throws IUFkForestException
	 */
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
	
	
	/**
	 * @return statistics
	 */
	public Statistics getStatistics() {
		return statistics;
	}
	
	/**
	 * @return k (the size of k-tree)
	 */
	public int getK() {
		return k;
	}
	
	/**
	 * @return n (the number of managed elements)
	 */
	public int getN() {
		return n;
	}
}