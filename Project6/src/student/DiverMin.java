package student;

import static a5.GraphAlgorithms.shortestPath;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

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
	//The extra priority added to the neighbors
	static final int EX_PRIOR = 1;
	//These magic numbers are parameters optimizing result through learning
	public static int VISITED_POS = 17;
	public static int VISITED_NEG = 82;
	public static int EX_VISITED_POS = 22;
	public static int EX_VISITED_NEG = 132;
	public static int MOVE_LIMIT = 63;
	
	HashSet<Long> visited;
	HashMap<Long, Node> nodeMap = new HashMap<Long, Node>();
	 
	AdjacencyListGraph<Long, Integer> mapGraph = new AdjacencyListGraph<Long, Integer>();
	
	@Override
	public void find(FindState state) {
		//consider whether last move approaches the destination
		visited 					   		   = new HashSet<Long>(); 
		
		Heap<Node, Integer> headHeap 		   = new Heap<Node, Integer>(Comparator.reverseOrder());

		headHeap.add(new Node(state.currentLocation(), state.distanceToRing()), state.distanceToRing());
		visited.add(state.currentLocation());
		
		while(state.distanceToRing() > 0) {
			Node source = getNode(state.currentLocation());
			Node optimal = (headHeap.size() > 0)?headHeap.peek():null;
			
			// neighbors.size() is 0: 
			// neighbors.size() >  0: 
			for(NodeStatus nodeStatus : state.neighbors()) {
				if(!visited.contains(nodeStatus.getId())) {
					Node target;
					if(contains(nodeStatus.getId()))
						target = getNode(nodeStatus.getId());
					else {
						target = new Node(nodeStatus.getId(), nodeStatus.getDistanceToTarget());
						headHeap.add(target, nodeStatus.getDistanceToTarget());
					}
					
					connect(source, target);
					
					if(optimal == null || target.distance - EX_PRIOR < optimal.distance)
						optimal = target;
				}
			}
			if(optimal.equals(headHeap.peek()))
				optimal = headHeap.poll();
			navigateTo(state, getNode(state.currentLocation()), optimal);
			visited.add(optimal.id);
			
		}
		return;
	}
	
	//-5294851052382533493
	//-8915455580838214402
	private class Node {
		AdjacencyListGraph<Long, Integer>.Node node;
		
		HashSet<Node> AdjancentList; 
		
		long id;
		
		int distance;
		
		Node(long id, int distance) {
			this.id = id;
			this.distance = distance;
			AdjancentList = new HashSet<Node>();
			node = mapGraph.addNode(id);
			nodeMap.put(id, this);
		}
	}
	
	void connect(Node n1, Node n2) {
		if(n1.AdjancentList.contains(n2))
			return;
		
		n1.AdjancentList.add(n2);
		n2.AdjancentList.add(n1);
		mapGraph.addEdge(n1.node, n2.node, 1);
		mapGraph.addEdge(n2.node, n1.node, 1);
	}
	
	boolean contains(Long id) {
		return nodeMap.containsKey(id);
	}
	
	Node getNode(Long id) {
		return nodeMap.get(id);
	}
	
	private void navigateTo(FindState state, Node source, Node target) {
		List<AdjacencyListGraph<Long, Integer>.Node> path = shortestPath(source.node, target.node);
		for(AdjacencyListGraph<Long, Integer>.Node node:path) {
			if(state.currentLocation() == node.getData()) {
				continue;
			}
			state.moveTo(node.getData());
			visited.add(node.getData());
		}
	}
	//397714234465580674
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
	HashSet<game.Node> fleeVisited = new HashSet<game.Node>();
	@Override
	public void flee(FleeState state) {
		while(true) {
			game.Node DST = bestDST(state, false);
			if(DST == null)
				DST = bestDST(state, true);
			if(!navigateTo(state, state.currentNode(), DST) /*|| DST == null*/)
			{
				navigateTo(state, state.currentNode(), state.getExit());
				break;
			}
		}
		
		navigateTo(state, state.currentNode(), state.getExit());
		System.out.printf("return with pos: %d, neg: %d, expos: %d, exneg: %d, movelimit: %d\n",VISITED_POS, VISITED_NEG, EX_VISITED_POS, EX_VISITED_NEG, MOVE_LIMIT);
	}
	
	/**
	 * Calculate the length of the given path based on the length of edges along the path 
	 * @return the total length of the path, or 0 if there's only one node in the path
	 */
	private int getPathLength(List<game.Node> path) {
		Iterator<game.Node> itr = path.iterator();
		game.Node prevNode = itr.next();
		int sum = 0;
		while(itr.hasNext()) {
			game.Node temp = itr.next();
			sum += prevNode.getEdge(temp).length;
			prevNode = temp;
		}
		return sum;
	}
	
	/**
	 * Continuously call state.moveTo until Min arrives at the destination or it's running out steps
	 * @return false when Min must return
	 */
	private boolean navigateTo(FleeState state, game.Node source, game.Node target) {
		List<game.Node> path = shortestPath(source, target);
		/*if(path.size() <= 1) {
			return false;
		}*/
		for(game.Node node:path) {
			if(state.currentNode().equals(node)) {
				continue;
			}
			if(!target.equals(state.getExit()) && 
					getPathLength(shortestPath(state.currentNode(), node)) + 
					getPathLength(shortestPath(node, state.getExit())) > state.stepsLeft())
					return false;
			state.moveTo(node);
			fleeVisited.add(node);
		}
		return true;
	}
	/** Find the shortest paths to all nodes(not including visited ones)
	 * s
	 * @param node
	 * @return ArrayList
	 */
	private game.Node bestDST(FleeState state, boolean revisit) {
		int bestScore = Integer.MIN_VALUE;
		game.Node bestDST = null;
		int count = 0;
		for(game.Node node : state.allNodes()) {
			//When there's no unvisited node in reachable range(withim MOVE_LIMIT), bestDST cease restraining target node to be unvisited
			if(fleeVisited.contains(node) && !revisit) 
				continue;
			List<game.Node> curPath = GraphAlgorithms.shortestPath(state.currentNode(), node);
			//Set an upper limit of move to prevent computational overload and prevent returning currentNode
			if(node == state.currentNode() || getPathLength(curPath) > MOVE_LIMIT)
				continue;
			List<game.Node> curPathToExit = GraphAlgorithms.shortestPath(node, state.getExit());
			HashSet<game.Node> tempVisited = new HashSet<game.Node>();
			int pathScore = 0;
			int noOfNodes = 0;
			for(game.Node n: curPath) {
				tempVisited.add(n);
				noOfNodes++;
				if(fleeVisited.contains(n)) 
					pathScore-=VISITED_NEG;
				else
					pathScore = (int)(pathScore + VISITED_POS - noOfNodes*0.9 + n.getTile().coins()*1.1);
			}
			for(game.Node n : curPathToExit) {
				noOfNodes++;
				if(fleeVisited.contains(n) || tempVisited.contains(n)) 
					pathScore-=EX_VISITED_NEG;
				else
					pathScore = (int)(pathScore + EX_VISITED_POS -noOfNodes*0.9 + n.getTile().coins()*1.6);
			}
			if(pathScore > bestScore || (bestDST != null && bestDST.getId() == state.currentNode().getId())) {
				bestScore = pathScore;
				bestDST = node;
			}
		}
		//System.out.println("DST: " + (bestDST!=null?bestDST.getId():"null"));
		//System.out.println("======================");
		if(bestDST != null && bestDST.getId() == 350) {
			System.out.print(350);
		}
		return bestDST;
	}

}
