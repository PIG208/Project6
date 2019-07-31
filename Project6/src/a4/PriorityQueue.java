package a4;

import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * A priority queue stores elements of type E with priorities of
 * type P (typically P is Integer or Double).
 * 
 * This particular interface also only allows one copy of a given element to
 * be stored.  That is, if you try to add an element e where e.equals(f) for
 * some f in this, add will throw an exception.
 * 
 * In the efficiency requirements below, n refers to this.size().  For example,
 * poll() says it should run in O(log n) time; this means it should run in time
 * logarithmic in the number of elements in the queue.
 */
public interface PriorityQueue<E, P> {
	
	/** return the comparator used for ordering priorities */
	public Comparator<? super P> comparator();
	
	/** Return the number of elements in this.  Runs in O(1) time. */
	public int size();
	
	/**
	 * Remove and return the largest element of this, according to comparator()
	 * Runs in O(log n) time.
	 * 
	 * @throws NoSuchElementException if this is empty 
	 */
	public E poll() throws NoSuchElementException;
	
	/**
	 * Return the largest element of this, according to comparator().
	 * Runs in O(1) time.
	 * 
	 * @throws NoSuchElementException if this is empty.
	 */
	public E peek() throws NoSuchElementException;
	
	/**
	 * Add the element e with priority p to this.  Runs in O(log n + a) time,
	 * where a is the time it takes to append an element to an ArrayList of size
	 * n.
	 * 
	 * @throws IllegalArgumentException if this already contains an element that
	 *                                  is equal to e (according to .equals())
	 */
	public void add(E e, P p) throws IllegalArgumentException;

	/**
	 * Change the priority associated with e to p.
	 *
	 * @throws NoSuchElementException if this does not contain e.
	 */
	public void changePriority(E e, P p) throws NoSuchElementException;
}
