package game;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Node implements graph.Node<Node,Edge> {
	
    /** The unique numerical identifier of this Node */
    private final long id;
    /** Represents the edges outgoing from this Node */

    private final Map<Node,Edge> outgoing;
    private final Map<Node,Edge> incoming;
    
    /** Extra state that belongs to this node */
    private final Tile tile;
    
    /** Constructor: a Node for tile t using t's row 
    /* package */ Node(Tile t, int numCols) {
    	this(t.getRow() * numCols + t.getColumn(), t);
    }

    /** Constructor: a node for tile t with id givenId. */
    /* package */ Node(long givenId, Tile t) {
        this.id= givenId;
        this.outgoing = new HashMap<>();
        this.incoming = new HashMap<>();
        
        this.tile= t;
    }
    
    public String toString() {
    	return "(" + this.tile.getRow() + "," + this.tile.getColumn() + ")";
    }
    
    /* package */ void addEdge(Edge e) {
    	if (e.source() == this) {
    		outgoing.put(e.target(), e);
    		incoming.put(e.target(), e.twin());
    	}
    	else if (e.target() == this) {
    		outgoing.put(e.source(), e.twin());
    		incoming.put(e.target(), e);
    	}
    	else throw new IllegalArgumentException("can only add an edge connected to the node");
    }
    
    /**  Return the unique Identifier of this Node. */
    public long getId() {
        return id;
    }
    
    /**Return the Edge of this Node that connects to Node q. 
     * Throw an IllegalArgumentException if edge doesn't exist */
    public Edge getEdge(Node q) {
    	if (!this.outgoing.containsKey(q))
    		throw new IllegalArgumentException();
    	return this.outgoing.get(q);
    }
    
    /**  Return an unmodifiable view of the Edges leaving this Node.   */
    public Collection<Edge> getExits() {
    	return this.outgoing.values();
    }
    
    /**  Return an unmodifiable view of the Nodes neighboring this Node.   */
    public Set<Node> getNeighbors() {
        return this.outgoing.keySet();
    }
    
    /**  Return the Tile corresponding to this Node. */
    public Tile getTile() {
        return tile;
    }
    
    /** Return true iff ob is a Node with the same id as this one. */
    @Override public boolean equals(Object ob) {
        if (ob == this) return true;
        if (!(ob instanceof Node)) return false;
        return id == ((Node)ob).id;
    }
    
    @Override public int hashCode() {
        return Objects.hash(id);
    }

	@Override
	public Map<Node, ? extends Edge> outgoing() {
		return outgoing;
	}

	@Override
	public Map<Node, ? extends Edge> incoming() {
		return incoming;
	}
}
