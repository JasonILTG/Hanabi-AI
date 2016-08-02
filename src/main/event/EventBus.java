package main.event;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EventBus
{
	/** A mapping from all the event classes to all the {@code EventListener}s attached to it */
	private static final Map<Class<? extends Event>, Set<EventListener>> LISTENER_MAP = new HashMap<>();
	
	/**
	 * Adds an {@code EventListener} to the given class.
	 *
	 * @param eventClass The event class that this listener will listen to
	 */
	public static void addListener(Class<? extends Event> eventClass, EventListener listener)
	{
		Set<EventListener> listenerSet = LISTENER_MAP.get(eventClass);
		
		// If the listener set does not exist yet, create the set and save it to the mapping.
		if (listenerSet == null) {
			listenerSet = new HashSet<>();
			LISTENER_MAP.put(eventClass, listenerSet);
		}
		
		// Add the listener to the set.
		listenerSet.add(listener);
	}
	
	/**
	 * Fires an event on this bus for all {@code EventListener}s to listen to
	 */
	public static void fireEvent(Event event)
	{
		// Retrieve all the listeners that should be triggered. This includes the listener for the event itself and any listeners listening to its
		// super classes.
		Set<EventListener> listenerSet = new HashSet<>();
		collectListeners(event.getClass(), event, listenerSet);
		
		// Inform the eventListeners about the event.
		for (EventListener listener : listenerSet) {
			listener.onEvent(event);
		}
	}
	
	/**
	 * Adds all listeners to be triggered.
	 * 
	 * @param eventClass The assigned type of the event
	 * @param event The event itself
	 * @param listeners The set of listeners to add to
	 */
	private static void collectListeners(Class<?> eventClass, Event event, Set<EventListener> listeners)
	{
		// Make sure the class implements Event
		if (eventClass == null || !Event.class.isAssignableFrom(eventClass)) {
			return;
		}
		
		// Add all the listeners linked to the class
		if (LISTENER_MAP.get(eventClass) != null) {
			listeners.addAll(LISTENER_MAP.get(eventClass));
		}
		
		// Fire event on superclass
		collectListeners(eventClass.getSuperclass(), event, listeners);
		
		// Fire event on superinterfaces
		for (Class interfaceClass : eventClass.getInterfaces()) {
			// Add listeners
			collectListeners(interfaceClass, event, listeners);
		}
	}
}
