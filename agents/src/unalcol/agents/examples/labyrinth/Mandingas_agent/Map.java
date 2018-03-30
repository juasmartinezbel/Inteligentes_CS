package unalcol.agents.examples.labyrinth.Mandingas_agent;

import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;
/**
*
* @author Cristian Rojas y Sebastian Martinez
*/
public class Map {
	private HashMap<String, Node> map;
	
	public Map() {
		map = new HashMap<String, Node>();
	}	
	
	public static String hashFunction(Integer x, Integer y) {
		return (String.valueOf(x) +"|"+ String.valueOf(y));
	}
	
	public int size() {
		return map.size();
	}
	
	public void add(Integer x, Integer y, Node node) {
		map.put(hashFunction(x, y), node);
	}
	
	public Node node(Integer x, Integer y) {
		return map.get(hashFunction(x, y));
	}
	
	public boolean contains(Integer x, Integer y) {
		return map.containsKey(hashFunction(x, y));
	}
	
	public Queue<String> nearestFood(Integer x, Integer y) {
		
		Queue<String> q = new PriorityQueue <String> ();
		Queue<String> path = new PriorityQueue <String> ();
		HashMap<String, Queue<String>> checked = new HashMap<String, Queue<String>>();  
		
		q.add(hashFunction(x, y));
		path.add(hashFunction(x, y));
		checked.put(hashFunction(x, y), path);
		
		boolean goal = false;
		while(!goal && q.size()!=0) {
			String node = q.poll();	
			
			if(map.containsKey(node)) {
				
				if(map.get(node).isFood() && !map.get(node).isBadFood()) {
					return checked.get(node);
				}
				
				for (String neighbor : map.get(node).getNeighbors()) {
					if(!checked.containsKey(neighbor)) {
						q.add(neighbor);
						path = new PriorityQueue <String> ();
						path = checked.get(node);
						path.add(neighbor);
						checked.put(neighbor, path);
					}
				}
			}
			
			
		}
		return null;
	}
	

}
