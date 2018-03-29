package unalcol.agents.examples.labyrinth.Mandingas_agent;

import java.util.HashMap;

public class Map {
	private HashMap<String, Node> map;
	
	public Map() {
		// TODO Auto-generated constructor stub
		map = new HashMap<String, Node>();
		
	}
	
	public String hashFunction(Integer x, Integer y) {
		return String.format("%02d", x) + String.format("%02d", y);
	}
	
	public int size() {
		return map.size();
	}
	
	public void add(Integer x, Integer y, Node node) {
		map.put(hashFunction(x, y), node);
	}

}
