package student;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import a4.Heap;
import a5.GraphAlgorithms;
import game.FindState;
import game.FleeState;
import game.NodeStatus;
import game.SewerDiver;
import graph.AdjacencyListGraph;

import common.NotImplementedError;

public class DiverMin implements SewerDiver {

	/** Get to the ring in as few steps as possible. Once you get there, <br>
	 * you must return from this function in order to pick<br>
	 * it up. If you continue to move after finding the ring rather <br>
	 * than returning, it will not count.<br>
	 * If you return from this function while not standing on top of the ring, <br>
	 * it will count as a failure.
	 *
	 * There is no limit to how many steps you can take, but you will receive<br>
	 * a score bonus multiplier for finding the ring in fewer steps.
	 *
	 * At every step, you know only your current tile's ID and the ID of all<br>
	 * open neighbor tiles, as well as the distance to the ring at each of <br>
	 * these tiles (ignoring walls and obstacles).
	 *
	 * In order to get information about the current state, use functions<br>
	 * currentLocation(), neighbors(), and distanceToRing() in state.<br>
	 * You know you are standing on the ring when distanceToRing() is 0.
	 *
	 * Use function moveTo(long id) in state to move to a neighboring<br>
	 * tile by its ID. Doing this will change state to reflect your new position.
	 *
	 * A suggested first implementation that will always find the ring, but <br>
	 * likely won't receive a large bonus multiplier, is a depth-first walk. <br>
	 * Some modification is necessary to make the search better, in general. */
	static Heap<Node, Integer> headHeap;
	HashSet<Long> visited;
	
	@Override
	public void find(FindState state) {
		//consider whether last move approaches the destination
		visited 					   			   = new HashSet<Long>(); 
		AdjacencyListGraph<Long, Integer> mapGraph = new AdjacencyListGraph<Long, Integer>();
		/*Heap<Node, Integer>*/ headHeap 		   = new Heap<Node, Integer>(Comparator.reverseOrder());

		headHeap.add(new Node(mapGraph, state.currentLocation(), state.distanceToRing()), state.distanceToRing());
		
		while(state.distanceToRing() > 0) {
			Node source = headHeap.poll();
			navigateTo(state, Node.getNode(state.currentLocation()), source);
			visited.add(source.id);
			
			for(NodeStatus nodeStatus : state.neighbors()) {
				if(!visited.contains(nodeStatus.getId())) {
					Node target;
					if(Node.contains(nodeStatus.getId()))
						target = Node.getNode(nodeStatus.getId());
					else {
						target = new Node(mapGraph, nodeStatus.getId(), nodeStatus.getDistanceToTarget());
						headHeap.add(target, nodeStatus.getDistanceToTarget());
					}
					mapGraph.addEdge(source.node, target.node, 1);
					mapGraph.addEdge(target.node, source.node, 1);
				}
			}
			
		}
		return;
	}
	
	/*private int noOfUnvisitedNeighbors(FindState state) {
		int visitCount = 0;
		for(NodeStatus nodeStatus:state.neighbors()) {
			if(!visited.contains(nodeStatus.getId())) {
				visitCount++;
			}
		}
		return visitCount;
	}*/
	
	//-8915455580838214402
	private static class Node {
		static HashMap<Long, Node> nodeMap = new HashMap<Long, Node>();
		 
		AdjacencyListGraph<Long, Integer>.Node node;
		
		long id;
		
		int distance;
		
		Node(AdjacencyListGraph<Long, Integer> graph, long id, int distance) {
			this.id = id;
			this.distance = distance;
			node = graph.addNode(id);
			nodeMap.put(id, this);
		}
		
		static boolean contains(Long id) {
			return nodeMap.containsKey(id);
		}
		
		static Node getNode(Long id) {
			return nodeMap.get(id);
		}
		
	}
	
	private void navigateTo(FindState state, Node source, Node target) {
		List<AdjacencyListGraph<Long, Integer>.Node> path = GraphAlgorithms.shortestPath(source.node, target.node);
		System.out.printf("from %s to %s, path: %d\n", Long.toString(source.id), Long.toString(target.id), path.size());
		for(AdjacencyListGraph<Long, Integer>.Node node:path) {
			if(state.currentLocation() == node.getData()) {
				continue;
			}
			state.moveTo(node.getData());
			visited.add(node.getData());
			System.out.printf("moved to %s\n", node.toString());
			System.out.println("size: " + headHeap.size());
		}
		System.out.println("==========================");
	}
	
	private void navigateTo(FleeState state, game.Node source, game.Node target) {
		List<game.Node> path = GraphAlgorithms.shortestPath(source, target);
		for(game.Node node:path) {
			System.out.printf("going to %s\n", node.toString());
			if(state.currentNode().equals(node))
				continue;
			state.moveTo(node);
			System.out.printf("moved to %s\n", node.toString());
			System.out.println("size: " + headHeap.size());
		}
		System.out.println("==========================");
	}
	/** Flee the sewer system before the steps are all used, trying to <br>
	 * collect as many coins as possible along the way. Your solution must ALWAYS <br>
	 * get out before the steps are all used, and this should be prioritized above<br>
	 * collecting coins.
	 *
	 * You now have access to the entire underlying graph, which can be accessed<br>
	 * through FleeState. currentNode() and getExit() will return Node objects<br>
	 * of interest, and getNodes() will return a collection of all nodes on the graph.
	 *
	 * You have to get out of the sewer system in the number of steps given by<br>
	 * getStepsRemaining(); for each move along an edge, this number is <br>
	 * decremented by the weight of the edge taken.
	 *
	 * Use moveTo(n) to move to a node n that is adjacent to the current node.<br>
	 * When n is moved-to, coins on node n are automatically picked up.
	 *
	 * You must return from this function while standing at the exit. Failing <br>
	 * to do so before steps run out or returning from the wrong node will be<br>
	 * considered a failed run.
	 *
	 * Initially, there are enough steps to get from the starting point to the<br>
	 * exit using the shortest path, although this will not collect many coins.<br>
	 * For this reason, a good starting solution is to use the shortest path to<br>
	 * the exit. */
	@Override
	public void flee(FleeState state) {
		navigateTo(state, state.currentNode(), state.getExit());
	}

}
