package utils;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * An iterator that will go through the given list and return to the first element when the end of the list is reached.
 */
public class CyclicIterator<T>
		implements Iterator<T>
{
	private final List<T> internalList;
	private Iterator<T> internalIterator;
	
	public CyclicIterator(List<T> listIn)
	{
		this(listIn, 0);
	}
	
	/**
	 * Initialize the iterator at a given index. The next item that this iterator will return is the item in the list at the given index.
	 */
	public CyclicIterator(List<T> listIn, int startIndex)
	{
		internalList = listIn;
		internalIterator = listIn.listIterator(startIndex);
	}
	
	@Override
	public boolean hasNext()
	{
		// Always has next.
		return true;
	}
	
	@Override
	public T next()
	{
		// If the internal iterator ran out, re-create the iterator.
		if (internalIterator == null || !internalIterator.hasNext())
		{
			internalIterator = internalList.iterator();
		}
		
		return internalIterator.next();
	}
	
	@Override
	public void forEachRemaining(Consumer<? super T> action)
	{
		synchronized (internalList)
		{
			int length = internalList.size();
			for (int i = 0; i < length; i++)
			{
				action.accept(next());
			}
		}
	}
}
