package graph;

import java.util.Collection;

/** A simple but flexible directed graph interface.  The interface is generic
 * in the Node and Edge types (N and E); concrete implementations will typically
 * provide their own Node and Edge implementations and then use those types as
 * the parameters N and E.
 * 
 * @param <N> the type of nodes in this graph
 * @param <E> the type of edges in this graph
 */
public interface Graph<N extends Node<N,E>, E extends Edge<N,E>> {
	
	/** Does this graph contain any nodes? */
	public boolean isEmpty();

	/** The collection of nodes contained in this */
	public Collection<? extends N> nodes();

	/** The collection of edges contained in this graph */
	public Collection<? extends E> edges();

}
