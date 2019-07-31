package graph;

/**
 * Labeled edges are edges that have a label associated with them (of type L).
 * This interface is useful for specifying generic algorithms that require
 * weighted edges.  For example, Dijkstra's algorithm requires integer weights;
 * this is apparent in the type of GraphAlgorithms.dijkstra:
 * 
 * public static <N extends Node<N,E>, E extends LabeledEdge<N,E,Integer>> ...
 * 
 * This means that dijkstra's can only be called if E is a LabeledEdge with integer lables.
 * 
 * @param <N> The type of nodes in the graph
 * @param <E> The type of edges in the graph
 * @param <L> The type of labels on the edges
 */
public interface LabeledEdge<N extends Node<N,E>, E extends Edge<N,E>, L> extends Edge<N, E> {
	/** Get the label associated with this edge */
	public L label();
}
