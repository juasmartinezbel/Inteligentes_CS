package unalcol.agents.examples.labyrinth.Mandingas_agent;

import java.util.LinkedList;
/**
*
* @author Cristian Rojas y Sebastian Martinez
*/
public class Node {
	private LinkedList<int[]> neighbors;
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
	
	public LinkedList<int[]> getNeighbors() {
		return neighbors;
	}
	
	public void thisIsBadFood() {
		badFood=true;
	}
	
	public boolean isBadFood() {
		return badFood;
	}
	
	public void setNeighbors(LinkedList<int[]> neighbors) {
		this.neighbors = neighbors;
	}
	
	
	
	
	
}
