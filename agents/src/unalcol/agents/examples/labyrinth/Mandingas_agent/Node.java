package unalcol.agents.examples.labyrinth.Mandingas_agent;

import static unalcol.agents.examples.labyrinth.Mandingas_agent.Map.*;
import java.util.LinkedList;

/**
*
* @author Cristian Rojas y Sebastian Martinez
*/
public class Node {
	private LinkedList<String> neighbors;
	private boolean food=false;
	private boolean badFood=true;
	
	public Node(Integer x, Integer y, boolean [] neighbors) {
		this.neighbors = new LinkedList<String>();
		if(!neighbors[0]) this.neighbors.add(hashFunction(x,y-1));
		if(!neighbors[1]) this.neighbors.add(hashFunction(x+1,y));
		if(!neighbors[2]) this.neighbors.add(hashFunction(x,y+1));
		if(!neighbors[3]) this.neighbors.add(hashFunction(x-1,y));
		
	}
	
	public void thisIsFood() {
		food = true;
	}
	
	public boolean isFood() {
		return food;
	}
	
	public LinkedList<String> getNeighbors() {
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
	
	public void printNeighbors() {
		for(String neighbor : this.neighbors) {
			System.out.print( neighbor + " ");
		}
		System.out.println();
	}
}
