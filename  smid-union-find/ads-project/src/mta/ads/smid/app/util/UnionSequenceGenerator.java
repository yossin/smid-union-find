package mta.ads.smid.app.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import mta.ads.smid.Constants;

public class UnionSequenceGenerator {
	/**
	 * union sequence container, for adding pairs
	 * @author Yossi Naor & Yosi Zilberberg
	 *
	 */
	private static interface UnionSequenceContainer{
		/**
		 * add a pair into the container
		 * @param pair
		 */
		void add(UnionPair pair);
	}
	/**
	 * save unions in a file.
	 * @author Yossi Naor & Yosi Zilberberg
	 *
	 */
	private static class FileSequenceContainer implements UnionSequenceContainer{
		/**
		 * file writer
		 */
		BufferedWriter writer;
		/**
		 * @param file output file for storing the union pairs
		 */
		FileSequenceContainer(File file){
			try {
				writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
			
		}
		
		/**
		 * write a header of the file, which is the number of elements (n)
		 * @param n the number of elements
		 */
		void writeHeader(int n){
			try {
				writer.write(n+Constants.NEW_LINE);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		/* (non-Javadoc)
		 * @see mta.ads.smid.app.util.UnionSequenceGenerator.UnionSequenceContainer#add(mta.ads.smid.app.util.UnionPair)
		 */
		@Override
		public void add(UnionPair pair) {
			try {
				writer.write(pair+Constants.NEW_LINE);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		/**
		 * close the file
		 */
		void close(){
			try {
				writer.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
	}
	
	/**
	 * save unions in a list (stored in memory).
	 * @author Yossi Naor & Yosi Zilberberg
	 *
	 */
	private static class ListSequenceContainer implements UnionSequenceContainer{
		/**
		 * save a reference to the container
		 */
		private List<UnionPair> list;
		/**
		 * @param list for storing the union pairs
		 */
		ListSequenceContainer(List<UnionPair> list){
			this.list=list;
		}
		
		/* (non-Javadoc)
		 * @see mta.ads.smid.app.util.UnionSequenceGenerator.UnionSequenceContainer#add(mta.ads.smid.app.util.UnionPair)
		 */
		@Override
		public void add(UnionPair pair) {
			list.add(pair);
		}
	}
	/**
	 * union strategy
	 * @author Yossi Naor & Yosi Zilberberg
	 *
	 */
	private static interface UnionSequenceStrategy{
		/**
		 * generate union sequence
		 * @param container for saving the union pairs into
		 */
		void generateSequence(UnionSequenceContainer container);
		/**
		 * @return the number of elements (n)
		 */
		int getN();
	}
	/**
	 * random union strategy. the elements are picked randomly.
	 * <br>union continues continues long as the there is more than one root in the forest.
	 * @author Yossi Naor & Yosi Zilberberg
	 *
	 */
	private static class RandomStrategy implements UnionSequenceStrategy{
		/**
		 * the number of elements (n)
		 */
		private int n;
		/**
		 * @param n the number of elements 
		 */
		RandomStrategy(int n) {
			this.n = n;
		}
		/**
		 * @param q pick a random element from the list
		 * @return
		 */
		private int randPoll(LinkedList<Integer> q){
			int size = q.size();
			if (size==0){
				throw new ArrayIndexOutOfBoundsException();
			}
			int position = (int)(Math.random()*(double)(size-1));
			return q.remove(position);
		}

		/* (non-Javadoc)
		 * @see mta.ads.smid.app.util.UnionSequenceGenerator.UnionSequenceStrategy#generateSequence(mta.ads.smid.app.util.UnionSequenceGenerator.UnionSequenceContainer)
		 */
		@Override
		public void generateSequence(UnionSequenceContainer container) {
			LinkedList<Integer> q1 = new LinkedList<Integer>();
			for (int i=0;i<n;i++){
				q1.add(i);
			}
			
			do {
				
				int x=randPoll(q1);
				int y=randPoll(q1);
				container.add(new UnionPair(x, y));

				q1.add(x);
			} while(q1.size()>1);
			
		}
		/* (non-Javadoc)
		 * @see mta.ads.smid.app.util.UnionSequenceGenerator.UnionSequenceStrategy#getN()
		 */
		@Override
		public int getN() {
			return n;
		}
		
	}
	/**
	 * balanced union strategy. the elements are selected in a sequential order for creating a balanced tree (for k^log/k(n) elements).
	 * <br>union continues continues long as the there is more than one root in the forest.
	 * @author Yossi Naor & Yosi Zilberberg
	 *
	 */
	private static class BalancedStrategy implements UnionSequenceStrategy{

		/**
		 * the k-size for the k-Forest
		 */
		private int k;
		/**
		 * the number of elements (n)
		 */
		private int n;
		/**
		 * @param k k-size for the k-Forest
		 * @param n the number of elements 
		 */
		BalancedStrategy(int k, int n) {
			this.k = k;
			this.n = n;
		}


		/* (non-Javadoc)
		 * @see mta.ads.smid.app.util.UnionSequenceGenerator.UnionSequenceStrategy#generateSequence(mta.ads.smid.app.util.UnionSequenceGenerator.UnionSequenceContainer)
		 */
		@Override
		public void generateSequence(UnionSequenceContainer container) {
			k=k-1;
			Queue<Integer> q1 = new LinkedList<Integer>();
			for (int i=0;i<n;i++){
				q1.add(i);
			}
			
			do {
				Queue<Integer> q2 = new LinkedList<Integer>();
				
				while (q1.isEmpty() == false){
					int x=q1.poll();
					q2.add(x);
					int size=(k<q1.size()?k:q1.size());
					for (int i=0;i<size;i++){
						int y=q1.poll();
						container.add(new UnionPair(x, y));
					}
				}
				q1=q2;
			} while(q1.size()>1);
			
		}
		
		/* (non-Javadoc)
		 * @see mta.ads.smid.app.util.UnionSequenceGenerator.UnionSequenceStrategy#getN()
		 */
		@Override
		public int getN() {
			return n;
		}
	}
	
	/**
	 * create a union list according to the given strategy
	 * @param strategy to be used (random / balanced)
	 * @return a list of union pair
	 */
	private static List<UnionPair> generateList(UnionSequenceStrategy strategy){
		List<UnionPair> list = new LinkedList<UnionPair>();
		ListSequenceContainer container = new ListSequenceContainer(list);
		strategy.generateSequence(container);
		return list;
	}
	
	/**
	 * create a union file according to the given strategy
	 * @param strategy to be used (random / balanced)
	 * @param file for saving the union list
	 */
	private static void generateFile(UnionSequenceStrategy strategy, File file){
		FileSequenceContainer container=null;
		try {
			container = new FileSequenceContainer(file);
			container.writeHeader(strategy.getN());
			strategy.generateSequence(container);
		} finally {
			if (container != null){
				container.close();
			}
		}
	}
	
	
	/**
	 * generate a random union file
	 * @param n the number of elements
	 * @param file for saving the union list
	 */
	public static void generateRandomFile(int n, File file){
		generateFile(new RandomStrategy(n), file);
	}

	/**
	 * generate a random union list
	 * @param n the number of elements
	 * @return list of unions
	 */
	public static List<UnionPair> generateRandomList(int n){
		return generateList(new RandomStrategy(n));
	}
	
	/**
	 * generate a balanced union file
	 * @param k the k-size for the k-Forest
	 * @param n the number of elements
	 * @param file for saving the union list
	 */
	public static void generateBalancedFile(int k, int n, File file){
		generateFile(new BalancedStrategy(k, n), file);
	}
	
	/**
	 * generate a balanced union list
	 * @param k the k-size for the k-Forest
	 * @param n the number of elements
	 * @return list of unions
	 */
	public static List<UnionPair> generateBalancedList(int k, int n){
		return generateList(new BalancedStrategy(k, n));
	}
	
}
