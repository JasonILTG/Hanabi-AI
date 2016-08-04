package main.game.utils;

import com.sun.istack.internal.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class CyclicIterator<T>
		implements Iterator<T>
{
	List<? extends T> internalList;
	/** The internal iterator that will get reset each time the iterator reaches the end of the internalList */
	ListIterator<? extends T> internalIterator;
	
	public CyclicIterator(@NotNull List<? extends T> listIn)
	{
		this.internalList = listIn;
		internalIterator = listIn.listIterator();
	}
	
	@Override
	public boolean hasNext()
	{
		return true;
	}
	
	@Override
	public T next()
	{
		// If the internal iterator has reached its end, reset to beginning.
		if (!internalIterator.hasNext()) {
			internalIterator = internalList.listIterator();
		}
		
		return internalIterator.next();
	}
	
	public T preview()
	{
		// If the iterator reached the end, return the first item in the list. Otherwise, return the next item, then go back.
		if (!internalIterator.hasNext()) {
			return internalList.get(0);
		}
		else {
			T preview = internalIterator.next();
			internalIterator.previous();
			return preview;
		}
	}
}
