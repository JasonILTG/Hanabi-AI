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
		// Retrieve all the listeners that should be triggered. This includes the listener for the event itself and any listeners listening to its super classes.
		Set<EventListener> listenerSet = new HashSet<>();
		Set<EventListener> baseSet = LISTENER_MAP.get(event.getClass());
		// Add only if the baseSet is not null to prevent NullPointerException
		if (baseSet != null) {
			listenerSet.addAll(baseSet);
		}
		
		// Add any listeners for the superclasses.
		for (Class<?> superEventClass : event.getClass().getClasses()) {
			// Only accept subclasses of the event class
			if (Event.class.isAssignableFrom(superEventClass)) {
				// Load the set to append
				Set<EventListener> appendSet = LISTENER_MAP.get(superEventClass);
				
				// Null check
				if (appendSet != null) {
					listenerSet.addAll(appendSet);
				}
			}
		}
		
		// Inform the eventListeners about the event.
		for (EventListener listener : listenerSet) {
			listener.onEvent(event);
		}
	}
}
