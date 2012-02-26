package mta.ads.smid.app.rich.views;

import java.util.LinkedList;
import java.util.Vector;
import java.util.Queue;
import java.util.Vector;

import mta.ads.smid.app.util.UnionPair;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.part.ViewPart;

public class UnionHistoryView extends ViewPart {
	public static final String ID = "ads-project-ui.unionHistoryView";
	private List list;
	private ListViewer listViewer ;
	private final Vector<UnionPair> unionList = new Vector<UnionPair>();
	private int lastUnionPairSequence=-1;

	private static Vector<UnionPair> createUnions(int k, int n) {
		Vector<UnionPair> unions = new Vector<UnionPair>();
		k=k-1;
		Queue<Integer> q1 = new LinkedList<Integer>();
		for (int i=0;i<n;i++){
			q1.add(i);
		}
		
		boolean decrease=true;
		do {
			Queue<Integer> q2 = new LinkedList<Integer>();
			
			while (q1.isEmpty() == false){
				int x=q1.poll();
				q2.add(x);
				int size=(k<q1.size()?k:q1.size());
				for (int i=0;i<size;i++){
					int y=q1.poll();
					unions.add(new UnionPair(x, y));
				}
				if (decrease){
					k=2;
					decrease=false;
				}
			}
			q1=q2;
		} while(q1.size()>1);

		return unions;
	}

	private static Vector<UnionPair> createUnions(){
		Vector<UnionPair> list = new Vector<UnionPair>();
		for (int i=0;i<10; i+=2){
			UnionPair pair = new UnionPair(i, i+1);
			list.add(pair);
		}
		return list;
	}
	
	private static Vector<UnionPair> createUnions1(){
		Vector<UnionPair> list = new Vector<UnionPair>();
		list.add(new UnionPair(0, 1));
		list.add(new UnionPair(0, 2));
		list.add(new UnionPair(3, 4));
		list.add(new UnionPair(3, 5));
		list.add(new UnionPair(6, 7));
		list.add(new UnionPair(6, 8));
		list.add(new UnionPair(9, 10));
		list.add(new UnionPair(9, 11));
		list.add(new UnionPair(12, 13));
		list.add(new UnionPair(12, 14));
		list.add(new UnionPair(15, 16));
		list.add(new UnionPair(15, 17));
		list.add(new UnionPair(0, 3));
		list.add(new UnionPair(6, 9));
		list.add(new UnionPair(12, 15));
		list.add(new UnionPair(0, 6));
		//list.add(new UnionPair(, ));
		return list;
	}
	

	public void createPartControl(Composite parent) {
		list = new List(parent, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
		setUnionList(createUnions1());	
		
		listViewer = new ListViewer(parent, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
		listViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

				
	    listViewer.setContentProvider(new IStructuredContentProvider() {
	        public Object[] getElements(Object inputElement) {
	          Vector v = (Vector)inputElement;
	          return v.toArray();
	        }
	        
	        public void dispose() {
	          System.out.println("Disposing ...");
	        }

	        public void inputChanged(
	          Viewer viewer,
	          Object oldInput,
	          Object newInput) {
	          System.out.println("Input changed: old=" + oldInput + ", new=" + newInput);
	        }
	      });
	    
	    listViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				System.out.println("selection event"+((StructuredSelection)event.getSelection()).getFirstElement());
			}
		});
	      
	      //listViewer.setContentProvider(new ArrayContentProvider());
	      
	      listViewer.setInput(unionList);
	      //listViewer.setInput(unionList);
	      
	      
	      listViewer.setLabelProvider(new LabelProvider() {
	        public Image getImage(Object element) {
	          return null;
	        }

	        public String getText(Object element) {
	          return element.toString();
	        }
	      });		
		
		
//		ObservableListContentProvider	 contentProvider = new ObservableListContentProvider();
//		viewer.setContentProvider(contentProvider);
//		IObservableSet knownElements = contentProvider.getKnownElements();
//		final IObservableMap firstNames = BeanProperties.value(Person.class,
//				"firstName").observeDetail(knownElements);

		
	}
	
	
	
	
	public Vector<UnionPair> getUnionPairSequence(){
		Vector<UnionPair> result = new Vector<UnionPair>();
		String[] selections = list.getSelection();
		if (selections.length == 1){
			String selection =selections[0];
			lastUnionPairSequence=-1;
			for (UnionPair pair : unionList){
				lastUnionPairSequence++;
				result.add(pair);
				if (pair.toString().equals(selection)){
					return result;
				}
			}
		}
		return result;
	}
	
	public void addUnion(int x, int y){
		if (lastUnionPairSequence>-1){
			list.setSelection(lastUnionPairSequence);
			if (lastUnionPairSequence+1 <unionList.size()){
				Vector<UnionPair> retain = getUnionPairSequence();
				list.remove(lastUnionPairSequence+1, unionList.size()-1);
				unionList.retainAll(retain);
			}
		}
		

		lastUnionPairSequence=-1;
		UnionPair pair = new UnionPair(x, y);
		unionList.add(pair);
		list.add(pair.toString());
		list.setSelection(unionList.size()-1);
		listViewer.refresh();
		//list.redraw();
	}
	

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
	}

	public void setUnionList(java.util.List<UnionPair> unionList) {
		this.unionList.clear();
		list.removeAll();
		this.unionList.addAll(unionList);
		lastUnionPairSequence=-1;
		for (UnionPair pair: unionList){
			list.add(pair.toString());
		}
	}
}