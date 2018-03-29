package unalcol.agents.examples.labyrinth.Mandingas_agent;

import java.util.HashMap;
/**
*
* @author Cristian Rojas y Sebastian Martinez
*/
public class Map {
	private HashMap<String, Node> map;
	
	public Map() {
		map = new HashMap<String, Node>();
	}
	
	
	public String hashFunction(Integer x, Integer y) {
		return (String.valueOf(x) +"|"+ String.valueOf(y));
	}
	
	
	public int size() {
		return map.size();
	}
	
	public void add(Integer x, Integer y, Node node) {
		map.put(hashFunction(x, y), node);
	}
	
	public Node node(Integer x, Integer y) {
		return map.get((String.valueOf(x) +"|"+ String.valueOf(y)));
	}
	
	public boolean contains(Integer x, Integer y) {
		return map.containsKey(((String.valueOf(x) +"|"+ String.valueOf(y))));
	}

}
