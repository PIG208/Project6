package game;

import java.util.Map;

/** An Edge represents an immutable directed, weighted edge.
 * @author eperdew*/
public class Edge implements graph.LabeledEdge<Node, Edge, Integer> {

    private final Node src; /** The Node this edge is coming from */
    private final Node dest; /** The node this edge is going to */
    public final int length; /** The length of this edge */
    private Edge twin;

    /** Constructor: an edge from src to dest with length length. */
    public Edge(Node src, Node dest, int length) {
    	this(src,dest,length,null);
    	twin = new Edge(dest,src,length,this);
    }

    private Edge(Node src, Node dest, int length, Edge twin) {
        this.src    = src;
        this.dest   = dest;
        this.length = length; 
        this.twin   = twin;
    }
    
    /** Constructor: an edge that is isomorphic to isomorphism. */
    public Edge(Edge e, Map<Node,Node> isomorphism) {
        this(isomorphism.get(e.src), isomorphism.get(e.dest), e.length);
    }

    /** Return the <tt>Node</tt> on this <tt>Edge</tt> that is not equal to <tt>n</tt>.
     * Throws an <tt>IllegalArgumentException</tt> if <tt>n</tt> is not in this <tt>Edge</tt>.
     * @param n A <tt>Node</tt> on this <tt>Edge</tt>
     * @return The <tt>Node</tt> not equal to <tt>n</tt> on this <tt>Edge</tt>
     */
    public Node getOther(Node n) {
        if (src == n)  return dest;
        if (dest == n) return src;
        throw new IllegalArgumentException("getOther: Edge must contain provided node");

    }
    
    public Edge twin() {
    	return twin;
    }
    
    /** Return the length of this <tt>Edge</tt> */
    public int length() {
        return length;
    }

    /** Return the source of this edge. */
    public Node getSource() {
        return src;
    }

    /** Return destination of edge */
    public Node getDest() {
        return dest;
    }

	@Override
	public Node source() {
		return src;
	}

	@Override
	public Node target() {
		return this.dest;
	}

	@Override
	public Integer label() {
		return this.length;
	}
}
