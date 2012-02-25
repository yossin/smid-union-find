package mta.ads.smid.model;

import java.util.List;

public interface IUFkEvent{
	// case 1+3
	void union(int orLeaf, int nrLeaf, int oldRoot, int newRoot, List<Integer> children);
	// case 2
	void union(int leaf1, int leaf2, int root, int child1, int child2);
}