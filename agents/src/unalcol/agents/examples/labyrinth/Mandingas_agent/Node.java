package unalcol.agents.examples.labyrinth.Mandingas_agent;

import java.util.LinkedList;
/**
*
* @author Cristian Rojas y Sebastian Martinez
*/
public class Node {
	private LinkedList<Node> neighbors;
	private boolean food=false;
	private boolean badFood=false;
	
	public Node() {
	}
	
	public void thisIsFood() {
		food = true;
	}
	
	public boolean isFood() {
		return food;
	}
	
	public LinkedList<Node> getNeighbors() {
		return neighbors;
	}
	
	public void setFood(boolean food) {
		this.food = food;
	}
	
	public void thisIsBadFood() {
		badFood=true;
	}
	
	public boolean isBadFood() {
		return badFood;
	}
	
	public void setNeighbors(LinkedList<Node> neighbors) {
		this.neighbors = neighbors;
	}
	
	
	
	
}
