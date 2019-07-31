package graph;

/** An edge in a directed graph.
 * @param <N> The type of Nodes in the graph
 * @param <E> The type of Edges in the graph
 */
public interface Edge<N extends Node<N,E>, E extends Edge<N,E>> {
	/** The node from whence this edge comes. */
	public N source();
	
	/** The node to which this edge goes. */
	public N target();
}
