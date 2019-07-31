package a4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import junit.framework.AssertionFailedError;

public class Heap<E, P> implements PriorityQueue<E, P> {

	/** Comparator<P> is the comparator Heap uses to arrange its elements. */
	Comparator<P> cmp;
	
	/** Elements are stored in an ArrayList. */
	ArrayList<Element<E, P>> array;
	
	/** Elements' value and index are stored in an HashMap. */
	HashMap<E, Integer> map;
	
	/**
	 * Creates an empty heap with the given comparator.
	 * @param c, Comparator<P>
	 */
	public Heap(Comparator<P> c) {
		this.cmp = c;
		array = new ArrayList<Element<E, P>>();
		map = new HashMap<E, Integer>();
	}
	
	/** Return the comparator used for ordering priorities */
	@Override
	public Comparator<? super P> comparator() {
		return cmp;
	}

	/** Return the number of elements in this.  Runs in O(1) time. */
	@Override
	public int size() {
		return array.size();
	}
	
	/** Return the priority of the left child leaf. Runs in O(1) time.
	 * Return null if the given index is bigger than the size
	 * @param i, index of the parent leaf
	 */
	public P left(int i) {
		if (2*i + 1 >= size()) {
			return null;
		}
		return array.get(2*i + 1).priority();
	}
	
	
	/** Return the priority of the right child leaf. Runs in O(1) time.
	 * Return null if the given index is bigger than the size
	 * @param i, index of the parent leaf
	 */
	public P right(int i) {
		if (2*i + 2 >= size()) {
			return null;
		}
		return array.get(2*i + 2).priority();
	}
	
	/** Return the priority of the parent leaf. Runs in O(1) time.
	 * Return null if the given index is  0, i.e. it is the root
	 * @param i, index of the child leaf
	 */
	public P parent(int i) {
		if (i == 0) {
			return null;
		}
		return array.get((i - 1)/2).priority();
	}


	/**
	 * Remove and return the largest element of this, according to comparator()
	 * Runs in O(log n) time.
	 * 
	 * @throws NoSuchElementException if this is empty 
	 */
	@Override
	public E poll() throws NoSuchElementException {
		if (size() == 0) {
			throw new NoSuchElementException();
		}
		
		Element<E, P> remove = array.get(0);
		// Replace the root with the last leaf
		array.set(0, array.get(size()-1));
		map.replace(array.get(0).value(), 0);
		array.remove(size()-1);
		map.remove(remove.value());
		
		// Keep swapping the parent with the larger of the two children. 
		int i = 0;
		while (left(i) !=null 
				&& (cmp.compare(array.get(i).priority(), left(i)) < 0
						|| (right(i) != null
						&&cmp.compare(array.get(i).priority(), right(i)) < 0))) {
			if (right(i) == null) {
				swap(array, i, 2 * i + 1);
				i = 2 * i + 1;
			}else {
				if (cmp.compare(left(i), right(i)) > 0) {
					swap(array, i, 2 * i + 1);
					i = 2 * i + 1;
				} else {
					swap(array, i, 2 * i + 2);
					i = 2 * i + 2;
				}
			}
		}
		return remove.value();
	}
	
	/**
	 * Swap the elements at index a and b, and update HashMap
	 * @param array is ArrayList.
	 * @param a is an int.
	 * @param b is an int.
	 */
	private void swap(ArrayList<Element<E, P>> array, int a, int b) {
		Element<E, P> tem = array.get(a);
		array.set(a, array.get(b));
		array.set(b, tem);
		map.replace(array.get(a).value(), a);
		map.replace(array.get(b).value(), b);
	}

	/**
	 * Return the largest element of this, according to comparator().
	 * Runs in O(1) time.
	 * 
	 * @throws NoSuchElementException if this is empty.
	 */
	@Override
	public E peek() throws NoSuchElementException {
		if (size() == 0) {
			throw new NoSuchElementException();
		}
		return array.get(0).value();
	}

	/**
	 * Add the element e with priority p to this.  Runs in O(log n + a) time,
	 * where a is the time it takes to append an element to an ArrayList of size
	 * n.
	 * 
	 * @throws IllegalArgumentException if this already contains an element that
	 *                                  is equal to e (according to .equals())
	 */
	@Override
	public void add(E e, P p) throws IllegalArgumentException {
		if (map.containsKey(e)) {
			throw new IllegalArgumentException();
		}
		
		Element<E, P> element= new Element<E, P>(e, p);
		array.add(element);
		int i = size() - 1;
		while ( i != 0
				&& cmp.compare(element.priority(), parent(i)) > 0){
					swap(array, i, (i - 1)/2);
					i = (i - 1)/2;
				}
		map.put(e, i);
	}

	/**
	 * Change the priority associated with e to p.
	 *
	 * @throws NoSuchElementException if this does not contain e.
	 */
	@Override
	public void changePriority(E e, P p) throws NoSuchElementException {
		if (!map.containsKey(e)) {
			throw new IllegalArgumentException();
		}
		
		int i = map.get(e);
		Element<E, P> element= array.get(i);
		element.setPriority(p);	
		while(true) {
			if(parent(i) != null && cmp.compare(element.priority(), parent(i)) > 0)
			{
				swap(array, i, (i - 1)/2);
				i = (i - 1)/2;
			}
			else if(right(i) != null && cmp.compare(element.priority(), right(i)) < 0)
			{
				swap(array, i, 2*i + 2);
				i = 2*i + 2;
			}
			else if(left(i) != null && cmp.compare(element.priority(), left(i)) < 0)
			{
				swap(array,i, 2*i+1);
				i = 2*i+1;
			}
			else {
				break;
			}
		}
	}
	
	/**
	 * Set the format of printing Heap
	 */
	@Override
	public String toString() {
		String s = "[";
		for (int i = 0; i < size(); i++) {
			if (i == 0) {
				s = s + array.get(i).value();
			}else {
				s = s + ", " + array.get(i).value(); 
			}
		}
		s = s + "]";
		return s;	
	}
	
	/**
	 * Inner class Element<E, P> represents a single element in PriorityQueue.
	 */
	 private class Element<E, P> {
		
		/** Value stored in each Element*/
		E value;
		/** Priority assigned to each Element*/
		P priority;
		
		/** Constructor */
		private Element(E value, P priority){
			this.value = value;
			this.priority = priority;
		}
        
		/** Return the value of the element */
		private E value() {
			return value;
		}
		
		/** Return the priority of the element */
		private P priority() {
			return priority;
		}
		
		/** Set the priority of the element with given value */
		private void setPriority(P priority) {
			this.priority = priority;
		}
	}
	 
	/**
	 * AssertInvariants are used to check that the parents are larger than both of its children
	 * and the HashMap has the correct index.
	 */
	public void assertInvariants() {
		// Check that all the parents are larger than their children
		if (size() != 0) {
			int i = 0;
			while (left(i) != null) {
				P parent = array.get(i).priority();
				if (right(i) != null) {
					assertTrue(cmp.compare(parent, left(i)) > 0
							&& cmp.compare(parent, right(i)) > 0);
				}else {
					assertTrue(cmp.compare(parent, left(i)) > 0);
				}
				i++;
			}
		
			// Check that HashMap has the right key and index.
			for (int j = 0; j < size(); j++) {
				E value = array.get(j).value();
				assertEquals(j, map.get(value).intValue());
			}
		}
	}
	
}
