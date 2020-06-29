import java.util.*;
public class PriorityQueue<E>extends AbstractQueue<E> 
	implements java.io.Serializable {

	    private static final long serialVersionUID = -7720805057305804111L;

	    private static final int DEFAULT_INITIAL_CAPACITY = 11;

	    private transient Object[] queue;

	    private int size = 0;

	    private final Comparator<? super E> comparator;

	    private transient int modCount = 0;

	    public PriorityQueue() {
	        this(DEFAULT_INITIAL_CAPACITY, null);
	    }

	    public PriorityQueue(int initialCapacity) {
	        this(initialCapacity, null);
	    }

	
	    public PriorityQueue(int initialCapacity, 
	                         Comparator<? super E> comparator) {
	        if (initialCapacity < 1)
	            throw new IllegalArgumentException();
	        this.queue = new Object[initialCapacity + 1];
	        this.comparator = comparator;
	    }

	    private void initializeArray(Collection<? extends E> c) {
	        int sz = c.size();
	        int initialCapacity = (int)Math.min((sz * 110L) / 100,
	                                            Integer.MAX_VALUE - 1);
	        if (initialCapacity < 1)
	            initialCapacity = 1;

	        this.queue = new Object[initialCapacity + 1];
	    }

	    private void fillFromSorted(Collection<? extends E> c) {
	        for (Iterator<? extends E> i = c.iterator(); i.hasNext(); )
	            queue[++size] = i.next();
	    }

	    private void fillFromUnsorted(Collection<? extends E> c) {
	        for (Iterator<? extends E> i = c.iterator(); i.hasNext(); )
	            queue[++size] = i.next();
	        heapify();
	    }

	   	     
	    public PriorityQueue(Collection<? extends E> c) {
	        initializeArray(c);
	        if (c instanceof SortedSet) {
	            SortedSet<? extends E> s = (SortedSet<? extends E>)c;
	            comparator = (Comparator<? super E>)s.comparator();
	            fillFromSorted(s);
	        } else if (c instanceof PriorityQueue) {
	            PriorityQueue<? extends E> s = (PriorityQueue<? extends E>) c;
	            comparator = (Comparator<? super E>)s.comparator();
	            fillFromSorted(s);
	        } else {
	            comparator = null;
	            fillFromUnsorted(c);
	        }
	    }

	    public PriorityQueue(PriorityQueue<? extends E> c) {
	        initializeArray(c);
	        comparator = (Comparator<? super E>)c.comparator();
	        fillFromSorted(c);
	    }


	    public PriorityQueue(SortedSet<? extends E> c) {
	        initializeArray(c);
	        comparator = (Comparator<? super E>)c.comparator();
	        fillFromSorted(c);
	    }
	    
	    private void grow(int index) {
	        int newlen = queue.length;
	        if (index < newlen) // don't need to grow
	            return;
	        if (index == Integer.MAX_VALUE)
	            throw new OutOfMemoryError();
	        while (newlen <= index) {
	            if (newlen >= Integer.MAX_VALUE / 2)  // avoid overflow
	                newlen = Integer.MAX_VALUE;
	            else
	                newlen <<= 2;
	        }
	        Object[] newQueue = new Object[newlen];
	        System.arraycopy(queue, 0, newQueue, 0, queue.length);
	        queue = newQueue;
	    }

	    public boolean offer(E o) {
	        if (o == null)
	            throw new NullPointerException();
	        modCount++;
	        ++size;

	        // Grow backing store if necessary
	        if (size >= queue.length) 
	            grow(size);

	        queue[size] = o;
	        fixUp(size);
	        return true;
	    }

	    public E peek() {
	        if (size == 0)
	            return null;
	        return (E) queue[1];
	    }

	    // Collection Methods - the first two override to update docs

	    public boolean add(E o) {
	        return offer(o);
	    }

	    public boolean remove(Object o) {
	        if (o == null)
	            return false;

	        if (comparator == null) {
	            for (int i = 1; i <= size; i++) {
	                if (((Comparable<E>)queue[i]).compareTo((E)o) == 0) {
	                    removeAt(i);
	                    return true;
	                }
	            }
	        } else {
	            for (int i = 1; i <= size; i++) {
	                if (comparator.compare((E)queue[i], (E)o) == 0) {
	                    removeAt(i);
	                    return true;
	                }
	            }
	        }
	        return false;
	    }

	    public Iterator<E> iterator() {
	        return new Itr();
	    }

	    private class Itr implements Iterator<E> {

	        private int cursor = 1;

	        private int lastRet = 0;

	        private int expectedModCount = modCount;

	        private ArrayList<E> forgetMeNot = null;

	        private Object lastRetElt = null;

	        public boolean hasNext() {
	            return cursor <= size || forgetMeNot != null;
	        }

	        public E next() {
	            checkForComodification();
	            E result;
	            if (cursor <= size) {
	                result = (E) queue[cursor];
	                lastRet = cursor++;
	            }
	            else if (forgetMeNot == null)
	                throw new NoSuchElementException();
	            else {
	                int remaining = forgetMeNot.size();
	                result = forgetMeNot.remove(remaining - 1);
	                if (remaining == 1) 
	                    forgetMeNot = null;
	                lastRet = 0;
	                lastRetElt = result;
	            }
	            return result;
	        }

	        public void remove() {
	            checkForComodification();

	            if (lastRet != 0) {
	                E moved = PriorityQueue.this.removeAt(lastRet);
	                lastRet = 0;
	                if (moved == null) {
	                    cursor--;
	                } else {
	                    if (forgetMeNot == null)
	                        forgetMeNot = new ArrayList<E>();
	                    forgetMeNot.add(moved);
	                }
	            } else if (lastRetElt != null) {
	                PriorityQueue.this.remove(lastRetElt);
	                lastRetElt = null;
	            } else {
	                throw new IllegalStateException();
	            }

	            expectedModCount = modCount;
	        }

	        final void checkForComodification() {
	            if (modCount != expectedModCount)
	                throw new ConcurrentModificationException();
	        }
	    }

	    public int size() {
	        return size;
	    }

	    /**
	     * Removes all elements from the priority queue.
	     * The queue will be empty after this call returns.
	     */
	    public void clear() {
	        modCount++;

	        // Null out element references to prevent memory leak
	        for (int i=1; i<=size; i++)
	            queue[i] = null;

	        size = 0;
	    }

	    public E poll() {
	        if (size == 0)
	            return null;
	        modCount++;

	        E result = (E) queue[1];
	        queue[1] = queue[size];
	        queue[size--] = null;  // Drop extra ref to prevent memory leak
	        if (size > 1)
	            fixDown(1);

	        return result;
	    }

	    private E removeAt(int i) { 
	        assert i > 0 && i <= size;
	        modCount++;

	        E moved = (E) queue[size];
	        queue[i] = moved;
	        queue[size--] = null;  // Drop extra ref to prevent memory leak
	        if (i <= size) {
	            fixDown(i);
	            if (queue[i] == moved) {
	                fixUp(i);
	                if (queue[i] != moved)
	                    return moved;
	            }
	        }
	        return null;
	    }

	    private void fixUp(int k) {
	        if (comparator == null) {
	            while (k > 1) {
	                int j = k >> 1;
	                if (((Comparable<E>)queue[j]).compareTo((E)queue[k]) <= 0)
	                    break;
	                Object tmp = queue[j];  queue[j] = queue[k]; queue[k] = tmp;
	                k = j;
	            }
	        } else {
	            while (k > 1) {
	                int j = k >>> 1;
	                if (comparator.compare((E)queue[j], (E)queue[k]) <= 0)
	                    break;
	                Object tmp = queue[j];  queue[j] = queue[k]; queue[k] = tmp;
	                k = j;
	            }
	        }
	    }

	    private void fixDown(int k) {
	        int j;
	        if (comparator == null) {
	            while ((j = k << 1) <= size && (j > 0)) {
	                if (j<size && 
	                    ((Comparable<E>)queue[j]).compareTo((E)queue[j+1]) > 0)
	                    j++; // j indexes smallest kid

	                if (((Comparable<E>)queue[k]).compareTo((E)queue[j]) <= 0)
	                    break;
	                Object tmp = queue[j];  queue[j] = queue[k]; queue[k] = tmp;
	                k = j;
	            }
	        } else {
	            while ((j = k << 1) <= size && (j > 0)) {
	                if (j<size && 
	                    comparator.compare((E)queue[j], (E)queue[j+1]) > 0)
	                    j++; // j indexes smallest kid
	                if (comparator.compare((E)queue[k], (E)queue[j]) <= 0)
	                    break;
	                Object tmp = queue[j];  queue[j] = queue[k]; queue[k] = tmp;
	                k = j;
	            }
	        }
	    }

	    private void heapify() {
	        for (int i = size/2; i >= 1; i--)
	            fixDown(i);
	    }

	    public Comparator<? super E> comparator() {
	        return comparator;
	    }

	    private void writeObject(java.io.ObjectOutputStream s)
	        throws java.io.IOException{
	        // Write out element count, and any hidden stuff
	        s.defaultWriteObject();

	        // Write out array length
	        s.writeInt(queue.length);

	        // Write out all elements in the proper order.
	        for (int i=1; i<=size; i++)
	            s.writeObject(queue[i]);
	    }

	 
	    private void readObject(java.io.ObjectInputStream s)
	        throws java.io.IOException, ClassNotFoundException {
	        // Read in size, and any hidden stuff
	        s.defaultReadObject();

	        // Read in array length and allocate array
	        int arrayLength = s.readInt();
	        queue = new Object[arrayLength];

	        // Read in all elements in the proper order.
	        for (int i=1; i<=size; i++)
	            queue[i] = (E) s.readObject();
	    }

	}

	

