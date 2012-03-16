package mta.ads.smid.app.rich.views;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Vector;

import mta.ads.smid.app.rich.model.IUFkForestsManager;
import mta.ads.smid.app.util.UnionPair;
import mta.ads.smid.app.util.UnionSequenceGenerator;

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
    private final static IUFkForestsManager manager = IUFkForestsManager.getInstance();
    /**
     * UI list viewer
     */
    private ListViewer listViewer;
	/**
	 * union list, to displayed in the list viewer
	 */
	final Vector<UnionPair> unionList = createRandomUnionList();
	/**
	 * create a random union list example
	 * @return union sequence
	 */
	private static Vector<UnionPair> createRandomUnionList() {
		IUFkForestsManager manager = IUFkForestsManager.getInstance();
		List<UnionPair> list = UnionSequenceGenerator.generateRandomList(manager.getN());
		return new Vector<UnionPair>(list);
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
	@SuppressWarnings("unchecked")
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