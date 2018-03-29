package unalcol.agents.examples.labyrinth.CS_agent;

import java.util.LinkedList;

public class Node {
	private boolean food;
	private LinkedList<Node> neighbors;
	
	public Node(boolean food) {
		// TODO Auto-generated constructor stub
		this.food = food;
	}
	
	public boolean getFood() {
		return food;
	}
	
	public LinkedList<Node> getNeighbors() {
		return neighbors;
	}
	
	public void setFood(boolean food) {
		this.food = food;
	}
	
	public void setNeighbors(LinkedList<Node> neighbors) {
		this.neighbors = neighbors;
	}
	
	
	
	
}
