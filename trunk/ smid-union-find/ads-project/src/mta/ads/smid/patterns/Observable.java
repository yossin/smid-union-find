package mta.ads.smid.patterns;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * A generic implementation of an Observable object as defined by the Observer pattern.
 * As a type parameter the interface for the Observer needs to be specified.
 * 
 * @author Steven Jeuris
 * 
 * @param <T>
 *            The interface which should be implemented by the observers.
 */
public class Observable<T> {

    private T m_eventDispatcher = null;
    private final ObserverPool<T> m_observers = new ObserverPool<T>();

    /**
     * Get the event dispatcher through which you can notify the observers.
     * 
     * @return The event dispatcher through which you can notify the observers.
     */
    protected T getEventDispatcher() {
        // Only create one instance of the dispatcher.
        if ( m_eventDispatcher == null ) {
            // Use reflection to get the generic parameter type.
            Type superClass = this.getClass().getGenericSuperclass();
            if ( superClass instanceof Class<?> ) {
                new RuntimeException( "Observable requires a parameter type!" );
            }
            else {
                // Get the parameter type.
                ParameterizedType genericType = (ParameterizedType)superClass;
                Type[] typeArguments = genericType.getActualTypeArguments();
                
                m_eventDispatcher = m_observers.createEventDispatcher( (Class<?>)typeArguments[0] );
            }

        }

        return m_eventDispatcher;
    }

    /**
     * Add an observer which will listen to the actions of this object.
     * 
     * @param observer
     *            The observer which should listen to this observable.
     */
    public void addObserver( T observer ) {
        m_observers.addObserver( observer );
    }

    /**
     * Remove an observer which was listening to this object.
     * 
     * @param observer
     *            The observer to remove.
     * 
     * @return True, when observer was found and removed, false otherwise.
     */
    public boolean removeObserver( T observer ) {
        return m_observers.removeObserver( observer );
    }

}