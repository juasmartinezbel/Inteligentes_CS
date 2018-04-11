package unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.mandingas;

import java.util.LinkedList;

/**
*
* @author Cristian Rojas y Sebastian Martinez
*/
public class Node {
	private LinkedList<String> neighbors;
	private boolean food=false;
	private boolean goodFood=false;
	
	public Node(Integer x, Integer y, boolean [] neighbors) {
		this.neighbors = new LinkedList<String>();
		setNeighbors(x, y, neighbors);
	}
	
	public void setNeighbors(Integer x, Integer y, boolean [] neighbors){
		this.neighbors.clear();
		if(!neighbors[0]) this.neighbors.add(hashFunction(x,y-1));
		if(!neighbors[1]) this.neighbors.add(hashFunction(x+1,y));
		if(!neighbors[2]) this.neighbors.add(hashFunction(x,y+1));
		if(!neighbors[3]) this.neighbors.add(hashFunction(x-1,y));
	}
	
	public static String hashFunction(Integer x, Integer y) {
		return (String.valueOf(x) +","+ String.valueOf(y));
	}
	
	public LinkedList<String> nodeNeighbors(){
		return neighbors;
	}
	
	public void setNeighbors(LinkedList<String> n){
		 neighbors=n;
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
	
	public void thisIsGoodFood() {
		goodFood=true;
	}
	
	public boolean isGoodFood() {
		return goodFood;
	}		
	
	public void addNeighbor(String n) {
		neighbors.add(n);
	}
	
	public void removeNeighbor(String n) {
		neighbors.remove(n);
	}
	
	public void printNeighbors() {
		for(String neighbor : this.neighbors) {
			System.out.print( neighbor + "|");
		}
		System.out.println();
	}
}
