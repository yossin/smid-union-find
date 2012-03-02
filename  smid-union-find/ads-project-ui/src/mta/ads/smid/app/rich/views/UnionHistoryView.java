package mta.ads.smid.app.rich.views;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Vector;

import mta.ads.smid.app.rich.model.IUFkForestsManager;
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
import org.eclipse.ui.part.ViewPart;

/**
 * 
 * View for displaying union history
 * 
 * @author Yossi Naor & Yosi Zilberberg
 *
 */
public class UnionHistoryView extends ViewPart {
	/**
	 * view id
	 */
	public static final String ID = "ads-project-ui.unionHistoryView";
    /**
     * forest manager, to delegate action
     */
    private final IUFkForestsManager manager = IUFkForestsManager.getInstance();
    /**
     * UI list viewer
     */
    private ListViewer listViewer;
	/**
	 * union list, to displayed in the list viewer
	 */
	final Vector<UnionPair> unionList = createUnions1();

	/**
	 * create union list example
	 * <br>eager union for making the shortest tree, in 2-trees. 
	 * @param k k-tree size
	 * @param n number of elements
	 * @return union sequence
	 */
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

	/**
	 * @return
	 */
	private static Vector<UnionPair> createUnions(){
		Vector<UnionPair> list = new Vector<UnionPair>();
		for (int i=0;i<10; i+=2){
			UnionPair pair = new UnionPair(i, i+1);
			list.add(pair);
		}
		return list;
	}
	
	/**
	 * @return union list example
	 */
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
	
	/**
	 * dynamic proxy that will bw used for refreshing the union history list
	 * 
	 * @author Yossi Naor & Yosi Zilberberg
	 *
	 */
	class ListDynamicProxyClass implements InvocationHandler {
		  /* (non-Javadoc)
		 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
		 */
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			try {
				Object result = method.invoke(unionList, args);
				listViewer.refresh();
				listViewer.setSelection(null);
				return result;
			} catch (InvocationTargetException e) {
				throw e.getTargetException();
			} catch (Exception e) {
				throw e;
			}
		}
	}

	
	
	/**
	 * factory method for creating a list dynamic proxy
	 * <br>union history list will be updated
	 * @return
	 */
	private List<?> createDynamicProxy(){
		InvocationHandler handler = new ListDynamicProxyClass();
		Class<?>[] interfaces ={List.class};
		ClassLoader loader = getClass().getClassLoader();
		return (List<?>) Proxy.newProxyInstance(loader , interfaces, handler);
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) {

		listViewer = new ListViewer(parent, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
		listViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

	    listViewer.setContentProvider(new IStructuredContentProvider() {
	        public Object[] getElements(Object inputElement) {
	          Vector<UnionPair> v = (Vector<UnionPair>)inputElement;
	          return v.toArray();
	        }
	        
	        public void dispose() {
	        }

	        public void inputChanged(
	          Viewer viewer,
	          Object oldInput,
	          Object newInput) {
	        }
	      });
	    
	    listViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				UnionPair pair = (UnionPair) ((StructuredSelection)event.getSelection()).getFirstElement();
				manager.setSelectedPair(pair);
			}
		});
	    
	    
	      
	    List<UnionPair> proxy = (List<UnionPair>) createDynamicProxy();
	    manager.setUnionList(proxy);
	      listViewer.setInput(unionList);
	      
	      
	      listViewer.setLabelProvider(new LabelProvider() {
	        public Image getImage(Object element) {
	          return null;
	        }

	        public String getText(Object element) {
	          return element.toString();
	        }
	      });
	}
	
	

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
	}
}