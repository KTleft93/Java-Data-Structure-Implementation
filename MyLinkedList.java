
public class MyLinkedList<E> extends MyAbstractList<E> {
	
	private Node<E> head, tail;
	
	public MyLinkedList() {
	}
	
	public MyLinkedList(E[] objects) {
	super(objects);
	}
	
	public E getFirst() {
	if (size == 0) {
	return null;
	}
	else {
	return head.element;
	}
	}
	
	public E getLast() {
	if (size == 0) {
	return null;
	}
	else {
	return tail.element;
	}
	}
	
	public void addFirst(E e) {
		Node<E> newNode = new Node<>(e); 
		 newNode.next = head;
		 head = newNode; 
		 size++; 
		if (tail == null) 
		tail = head;
	}
	
	public void addLast(E e) {
		Node<E> newNode = new Node<>(e); 
		if (tail == null) {
		 head = tail = newNode;
		}
		else {
		tail.next = newNode; 
		tail = tail.next; 
		}
		size++; 
	}
	@Override 
	public void add(int index, E e) {
		if (index == 0) addFirst(e); 
		else if (index >= size) addLast(e); 
		else { 
		Node<E> current = head;
		for (int i = 1; i < index; i++)
		current = current.next;
		Node<E> temp = current.next;
		current.next = new Node<E>(e);
		(current.next).next = temp;
		size++;
	}
	}
	public E removeFirst(int index) {
		if (size == 0) return null; 
		else {
			Node<E> temp = head; 
			head = head.next; 
			size--; 
			if (head == null) tail = null;
			return temp.element;
		}
	}
	public E removeLast() {
		if (size == 0) return null; // Nothing to remove
		else if (size == 1) { // Only one element in the list
		Node<E> temp = head;
		head = tail = null; // list becomes empty
		size = 0;
		return temp.element;
		}
		else {
		Node<E> current = head;
		for (int i = 0; i < size - 2; i++)
		current = current.next;
		Node<E> temp = tail;
		tail = current;
		tail.next = null;
		size--;
		return temp.element;
}
}
	public E remove(int index) {
		if (index < 0 || index >= size) return null; 
		else if (index == 0) return removeFirst(index); 
		else if (index == size - 1) return removeLast(); 
		else {
		Node<E> previous = head;
		for (int i = 1; i < index; i++) {
		previous = previous.next;
		}
		Node<E> current = previous.next;
		previous.next = current.next;
		size--;
		return current.element;
		}
}
	public String toString() {
		StringBuilder result = new StringBuilder("[");
		Node<E> current = head;
		for (int i = 0; i < size; i++) {
		result.append(current.element);
		current = current.next;
		if (current != null) {
		result.append(", "); // Separate two elements with a comma
		}
		else {
		result.append("]"); // Insert the closing ] in the string
		}
		}
		return result.toString();
	}
	@Override 
	public void clear() {
	size = 0;
	head = tail = null;
	}
	@Override 
	public boolean contains(E e) {
	System.out.println("Implementation left as an exercise");
	return true;
	}
	@Override 
	public E get(int index) {
	System.out.println("Implementation left as an exercise");
	return null;
	}
	@Override 
	public int indexOf(E e) {
	System.out.println("Implementation left as an exercise");
	return 0;
	}
	@Override 
	public int lastIndexOf(E e) {
	System.out.println("Implementation left as an exercise");
	return 0;
}
	@Override 
	public E set(int index, E e) {
	System.out.println("Implementation left as an exercise");
	return null;
	}
	@Override 
	public java.util.Iterator<E> iterator() {
	return new LinkedListIterator();
	}
	private class LinkedListIterator
	implements java.util.Iterator<E> {
	private Node<E> current = head; 
	@Override
	public boolean hasNext() {
	return (current != null);
	}
	@Override
	public E next() {
	E e = current.element;
	current = current.next;
	return e;
	}
	@Override
	public void remove() {
	System.out.println("Implementation left as an exercise");
	}
	}
	
	private static class Node<E> {
	E element;
	Node<E> next;
	public Node(E element) {
	this.element = element;
	}
	}
}