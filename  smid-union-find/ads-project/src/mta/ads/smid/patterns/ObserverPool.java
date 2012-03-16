package mta.ads.smid.patterns;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * The ObserverPool is a proxy which allows calls to an interface to be forwarded to a set of listeners.
 * 
 * @author Steven Jeuris
 * 
 * @param <T>
 *            The interface which defines which calls can be made to the listeners.
 */
class ObserverPool<T> implements InvocationHandler {

    /**
	 * @uml.property  name="m_pool"
	 */
    private List<T> m_pool = new ArrayList<T>();


    /**
     * Add an observer to which the calls will be made.
     * 
     * @param observer
     *            The observer to add.
     */
    void addObserver( T observer ) {
        m_pool.add( observer );
    }

    /**
     * Remove an observer to which calls where being made.
     * 
     * @param observer
     *            The observer to remove.
     * @return True, when the observer was found and removed, false otherwise.
     */
    boolean removeObserver( T observer ) {
        return m_pool.remove( observer );
    }

    /**
     * Create the proxy which allows to dispatch all calls to the observers.
     * @param observerClass The interface class of the observers.
     * @return  The dispatcher which can be used to make calls to all added observers.
     */
    @SuppressWarnings( { "unchecked", "rawtypes" } )
    T createEventDispatcher(Class observerClass ) {
        T dispatcher = (T)Proxy.newProxyInstance(
            observerClass.getClassLoader(),
            new Class[] { observerClass },
            this
        );

        return dispatcher;
    }

    /**
     * invoke() implementation of InvocationHandler.
     * This is called whenever a call is made to an event dispatcher.
     */
    @Override
    public Object invoke( Object object, Method method, Object[] args ) throws Throwable {
        // Forward the call to all observers.
        for ( T observer : m_pool ) {
            method.invoke( observer, args );
        }

        // No return object available.
        return null;
    }

}