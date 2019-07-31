package graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A graph with vertices labeled by VD and edges labeled by ED.
 * This implementation uses a variant of an adjacency list (an adjacency hash table).
 * This class allows data of type VD to be stored with each vertex and data of
 * type VE to be stored with each edge.  See Node.setData and Node.getData.
 * 
 * @param <VD> The type of data stored at each vertex
 * @param <ED> The type of data stored at each edge
 */
public class AdjacencyListGraph<VD, ED> implements graph.Graph<AdjacencyListGraph<VD,ED>.Node,AdjacencyListGraph<VD,ED>.Edge> {
  /**
   * An edge in the graph.  All operations throw IllegalStateExceptions if the
   * edge has been removed from the graph.
   */
  public class Edge implements graph.LabeledEdge<Node,Edge,ED> {
    private ED       data;

    /** invariant: if not removed then source.outgoing[target] == this */
    private Node source;
    
    /** invariant: if not removed then target.incoming[source] == this */
    private Node target;

    /** invariant: if not removed then this is in graph's edges list */
    private boolean removed;
    
    public Node source()    { checkValid(); return this.source; }
    public Node target()    { checkValid(); return this.target; }
    public ED   getData()        { checkValid(); return this.data;   }
    public void setData(ED value) { checkValid(); this.data = value;  }
    public ED   label()         { return getData(); }
    
    public String toString()     { return data.toString(); }
    
    private void checkValid() {
      if (this.removed)
        throw new IllegalStateException("Vertex is no longer in graph");
    }
    
    Edge(Node source, Node target, ED data) {
      this.removed = false;
      this.data = data;
      this.source = source;
      this.target = target;
      if (source.outgoing.containsKey(target))
        throw new IllegalStateException("duplicate edge");
      this.source.outgoing.put(target,this);
      this.target.incoming.put(source,this);
      edges.add(this);
    }
    
    /**
     * Remove this edge from the graph.
     * @return the label of this.
     */
    public ED remove() {
      checkValid();
      this.source.outgoing.remove(target);
      this.target.incoming.remove(source);
      edges.remove(this);
      this.removed = true;
      return this.data;
    }
  }

  /**
   * A Node in the graph.  All operations will raise an IllegalStateException
   * if the Node has been removed from the graph.
   */
  public class Node implements graph.Node<Node, Edge>{
    private VD         data;
    
    /** invariant: if not removed, outgoing[n].source == this and outgoing[n].target == n */
    private Map<Node, Edge> outgoing;
    
    /** invariant: if not removed, incoming[n].target == this and incoming[n].source == n */
    private Map<Node, Edge> incoming;

    /** invariant: if not removed, this is in graph's nodes set */
    private boolean removed;
    
    public Map<Node, Edge>  outgoing()       { checkValid(); return this.outgoing; }
    public Map<Node, Edge>  incoming()       { checkValid(); return this.incoming; }
    public VD               getData()        { checkValid(); return this.data; }
    public void             setData(VD data) { checkValid(); this.data = data; }
    
    public String toString() { checkValid(); return data.toString(); }
    
    private void checkValid() {
      if (removed)
        throw new IllegalStateException("Vertex is no longer in graph");
    }
    
    Node(VD data) {
      this.removed  = false;
      this.incoming = new HashMap<Node,Edge>();
      this.outgoing = new HashMap<Node,Edge>();

      this.data  = data;
      nodes.add(this);
    }
    
    /** Remove this vertex and any edges to or from it from the graph. */
    public VD remove() {
      checkValid();
      
      for (Edge e : incoming.values())
        e.remove();
        
      for (Edge e : outgoing.values())
        e.remove();
      
      nodes.remove(this);
      this.removed = false;
      return this.data; 
    }
  }

	Set<Node> nodes;
	Set<Edge> edges;
	
	/** Initialize an empty graph */
	public AdjacencyListGraph() {
		this.nodes = new HashSet<Node>();
		this.edges = new HashSet<Edge>();
	}
	
	/** Does this graph contain any nodes? */
	public boolean isEmpty() {
		return nodes.isEmpty();
	}

	public Collection<? extends Node> nodes() {
		return this.nodes;
	}

	public Collection<? extends Edge> edges() {
		return this.edges;
	}

	public Node addNode(VD data) {
		return new Node(data);
	}

	/** @throws IllegalStateException if there is already an edge from source to target */
	public Edge addEdge(Node source, Node target, ED data) {
		return new Edge(source,target,data);
	}
}
