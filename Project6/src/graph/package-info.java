/**
 * The main graph interface (Nodes, Edges, etc.); you'll want to review these interfaces.
 * 
 * <p>The interfaces are generic
 * in the Node and Edge types (N and E); concrete implementations will typically
 * provide their own Node and Edge implementations and then use those types as
 * the parameters N and E.  This allows us to ensure that a nodes neighbors have
 * the same type as the node, while still giving us the flexibility to work with
 * different kinds of graphs.
 * 
 * <p>The AdjacencyListGraph is a sample implementation, although for historical
 * reasons it is not used by the provided tests.  You can use it if you want to
 * construct your own tests without understanding the file loading code.
 * 
 * <p>The generic interfaces in this package do not contain any updateing methods;
 * these are intended as an interface between a concrete implementation and the
 * generic GraphAlgorithms; these algorithms don't need to modify the graphs, so
 * we prevent them from doing so. 
 */
package graph;
