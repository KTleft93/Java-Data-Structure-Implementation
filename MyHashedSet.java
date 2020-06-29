import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

public class MyHashedSet<E> implements MySet <E>
{
	private static int DEFAULT_INITIAL_CAPACITY = 4;
	
	private static int MAXIMUM_CAPACITY = 1 << 30;
	
	private int capacity;
	
	private static float DEFAULT_MAX_LOAD_FACTOR = 0.75f;
	
	private float loadFactorThreshold;
	
	private int size = 0;

	private LinkedList<E>[] table;
	
	public MyHashedSet()
	{
		this(DEFAULT_INITIAL_CAPACITY, DEFAULT_MAX_LOAD_FACTOR);
	}
	
	public MyHashedSet(int initialCapacity)
	{
		this(initialCapacity, DEFAULT_MAX_LOAD_FACTOR);
	}
	
	public MyHashedSet(List<E> lst)
	{
		this(DEFAULT_INITIAL_CAPACITY, DEFAULT_MAX_LOAD_FACTOR);
		for(E e: lst)
			this.add(e);
	}
	
	public MyHashedSet(int initialCapacity, float loadFactorThreshold)
	{
		if(initialCapacity > MAXIMUM_CAPACITY)
		{
			this.capacity = MAXIMUM_CAPACITY;
		}
		else
		{
			this.capacity = trimToPowerOf2(initialCapacity);
		}
		this.loadFactorThreshold = loadFactorThreshold;
		table = new LinkedList[capacity];
	}
	
	private int trimToPowerOf2(int initialCapacity)
	{
		int capacity = 1;
		while(capacity < initialCapacity)
		{
			capacity <<=1; // it is similar to *=2 but more efficient.
		}
		return capacity;
	}
	
	public Iterator<E> iterator()
	{
		return new MyHashedSetIterator(this);
	}
	
	private class MyHashedSetIterator implements java.util.Iterator<E>
	{
		private java.util.ArrayList<E> list;
		private int current = 0;
		private MyHashedSet<E> set;
		
		public MyHashedSetIterator(MyHashedSet<E> set)
		{
			this.set = set;
			list = setToList();
		}
		
		public boolean hasNext()
		{
			if(current < list.size())
			{
				return true;
			}
			return false;
		}
		
		public E next()
		{
			return list.get(current++);
		}
		
		public void remove()
		{
			set.remove(list.get(current));
			list.remove(current);
		}
	}
	
	private java.util.ArrayList<E> setToList()
	{
		java.util.ArrayList<E> list = new java.util.ArrayList<E>();
		for(int i = 0; i < capacity; i++)
		{
			if(table[i] != null)
			{
				for(E e: table[i])
				{
					list.add(e);
				}
			}
		}
		return list;
	}
	
	public void clear()
	{
		size = 0;
		removeElements();
	}
	
	private void removeElements()
	{
		for(int i = 0; i < capacity; i++)
		{
			if(table[i] != null)
			{
				table[i].clear();
			}
		}
	}
	
	public boolean contains(E e)
	{
		int bucketIndex = hash(e.hashCode());
		if(table[bucketIndex] != null)
		{
			LinkedList<E> bucket = table[bucketIndex];
			for(E element: bucket)
			{
				if(element.equals(e))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	private int hash(int hashCode)
	{
		return supplementalHash(hashCode) & (capacity - 1);
	}
	
	private int supplementalHash(int h)
	{
		h ^= (h >>> 20) ^ (h >>> 12);
		return h ^ (h >>> 7) ^ (h >>> 4);
	}
	
	public boolean add(E e)
	{
		if(contains(e))
		{
			return false;
		}
		if(size + 1 > capacity * loadFactorThreshold)
		{
			if(capacity == MAXIMUM_CAPACITY)
			{
				throw new RuntimeException("Exceeding maximum capacity");
			}
			rehash();
		}
		
		int bucketIndex = hash(e.hashCode());
		
		if(table[bucketIndex] == null)
		{
			table[bucketIndex] = new LinkedList<E>();
		}
		
		table[bucketIndex].add(e);
		size++;
		return true;
	}
	
	private void rehash()
	{
		java.util.ArrayList<E> list = setToList();
		capacity <<= 1; // It is similar to *= 2. <= is more efficient
		table = new LinkedList[capacity];
		size = 0;
		
		for(E element: list)
		{
			add(element);
		}
	}
	
	public boolean remove(E e)
	{
		if(!contains(e))
		{
			return false;
		}
		int bucketIndex = hash(e.hashCode());
		
		if(table[bucketIndex] != null)
		{
			LinkedList<E> bucket = table[bucketIndex];
			for(E element: bucket)
			{
				if(e.equals(element))
				{
					bucket.remove(element);
					break;
				}
			}
		}
		size--;
		return true;
	}
	
	public boolean isEmpty()
	{
		return size == 0;
	}
	
	public int size()
	{
		return size;
	}
	
	public String toString()
	{
		java.util.ArrayList<E> list = setToList();
		StringBuilder builder = new StringBuilder("[");
		
		for(int i = 0; i < list.size() - 1; i++)
		{
			builder.append(list.get(i) + ", ");
		}
		
		if(list.size() == 0)
		{
			builder.append("]");
		}
		else
		{
			builder.append(list.get(list.size() - 1) + "]");
		}
		return builder.toString();
	}

	public String[] split(String string) {
		// TODO Auto-generated method stub
		return null;
	}
}