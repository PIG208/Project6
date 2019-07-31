package a5;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import common.NotImplementedError;
import graph.Edge;
import graph.Node;
import graph.AdjacencyListGraph;
import graph.LabeledEdge;
import a4.Heap;

/** We've provided depth-first search as an example; you need to implement Dijkstra's algorithm.
 */
public class GraphAlgorithms  {
	/** Return the Nodes reachable from start in depth-first-search order */
	public static <N extends Node<N,E>, E extends Edge<N,E>>
	List<N> dfs(N start) {
		
		Stack<N> worklist = new Stack<N>();
		worklist.add(start);
		
		Set<N>   visited  = new HashSet<N>();
		List<N>  result   = new ArrayList<N>();
		while (!worklist.isEmpty()) {
			// invariants:
			//    - everything in visited has a path from start to it
			//    - everything in worklist has a path from start to it
			//      that only traverses visited nodes
			//    - nothing in the worklist is visited
			N next = worklist.pop();
			visited.add(next);
			result.add(next);
			for (N neighbor : next.outgoing().keySet())
				if (!visited.contains(neighbor))
					worklist.add(neighbor);
		}
		return result;
	}
	
	/**
	 * Return a minimal path from start to end.  This method should return as
	 * soon as the shortest path to end is known; it should not continue to search
	 * the graph after that. 
	 * 
	 * @param <N> The type of nodes in the graph
	 * @param <E> The type of edges in the graph; the weights are given by e.label()
	 * @param start The node to search from
	 * @param end   The node to find
	 */
	public static <N extends Node<N,E>, E extends LabeledEdge<N,E,Integer>>
	List<N> shortestPath(N start, N end) {
		//invariants:
		//- Visited list has all v with minimal path start ~> (length d) v in entire graph
		//- Worklist: each v has minimal path start ~. U -> v with start ~> u visited
		//Initialization:		
		HashMap<N, PathNode<N, E>>    pathNodeMap  = new HashMap<N, PathNode<N,E>>();
		Heap<PathNode<N, E>, Integer> workHeap     = new Heap<PathNode<N, E>, Integer>(Comparator.reverseOrder());
		
		workHeap.add(new PathNode<N, E>(start, 0), 0);
		
		//progress + preservation
		
		while(workHeap.size() > 0) {//termination
			PathNode<N, E> pathNode = workHeap.poll();
			pathNode.visit();
			
			if(pathNode.node.equals(end)) {
				pathNodeMap.put(end, pathNode);
				break;//termination
			}
			
			//Iterate over all the adjacent nodes
			for(E outEdge : pathNode.node.outgoing().values()) {
				
				//Store the target node into a PathNode object
				PathNode<N, E> target = pathNodeMap.get(outEdge.target());
				
				//Skip visited nodes
				if(target != null && target.isVisited)
					continue;
				
				//Prevent target from having a null value
				if(target == null)
					target = new PathNode<N, E>(outEdge.target());

				//The cost to a node equals the total distance from the start node to it
				int cost = pathNode.cost + outEdge.label();
				
				if(cost <= target.cost) {
					//Update priority if the node has already been marked with a cost
					if(pathNodeMap.containsKey(target.node)) {
						workHeap.changePriority(target, cost);
					}
					else {
						workHeap.add(target, cost);
						pathNodeMap.put(target.node, target);
					}
					target.cost = cost;
					target.prevPathNode = pathNode;
				}
			}
		}
		//termination: shortest path is shown
		Stack<N> result = new Stack<N>();
		
		for(PathNode<N, E> curNode = pathNodeMap.get(end); curNode != null; curNode = curNode.prevPathNode)
			result.add(0, curNode.node);
		
		if(result.size() == 0 || !result.get(0).equals(start))
			return new Stack<N>();
		
		return result;
	}
	
	private static class PathNode<N extends Node<N,E>, E extends LabeledEdge<N,E,Integer>> {
		
		N node;
		
		PathNode<N, E> prevPathNode;
		
		int cost = Integer.MAX_VALUE;
		
		boolean isVisited = false;
		
		PathNode(N node){
			this.node = node;
		}
		
		PathNode(N node, int cost){
			this.node = node;
			this.cost = cost;
		}
		
		void visit() {
			isVisited = true;
		}
		
	}
	
}
