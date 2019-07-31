package graph;

import java.util.Map;

/** A node in a directed graph.
 *
 * @param <N> The type of nodes in the graph
 * @param <E> The type of edges in the graph
 */
public interface Node<N extends Node<N,E>, E extends Edge<N,E>> {
	/** The set of outgoing edges, organized by their target. */
	public Map<N,? extends E> outgoing();
	
	/** The set of incoming edges, organized by their source. */
	public Map<N,? extends E> incoming();
}
