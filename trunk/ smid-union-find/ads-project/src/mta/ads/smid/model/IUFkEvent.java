package mta.ads.smid.model;


/**
 * an Interface for observing union events
 * @author Yossi Naor & Yosi Zilberberg
 *
 */
public interface IUFkEvent{
	/**
	 * Union Event for cases 1+3
	 * @param fromLeafId selected leaf id from source tree 
	 * @param intoLeafId selected leaf id from destination tree 
	 * @param fromRootId root id in the source tree (to be disposed) 
	 * @param intoRootId root id in the destination tree
	 */
	void union(int fromLeafId, int intoLeafId, int fromRootId, int intoRootId);
	/**
	 * Union Event for case 2
	 * @param leaf1Id selected leaf id from source tree 1
	 * @param leaf2Id selected leaf id from source tree 2
	 * @param newRootId new root id for managing both trees
	 * @param rootChildId1 root id for tree 1 (to be a <i>NonRoot<i> element)
	 * @param rootChildId2 root id for tree 2 (to be a <i>NonRoot<i> element)
	 */
	void union(int leaf1Id, int leaf2Id, int newRootId, int rootChildId1, int rootChildId2);
}