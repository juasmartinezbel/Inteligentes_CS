package unalcol.agents.examples.labyrinth.Mandingas_agent;

import java.util.HashMap;
import java.util.LinkedList;
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
		return (String.valueOf(x) +","+ String.valueOf(y));
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
	
	public String[] key2coordinates(String key) {
		return key.split(",");
	}
	
	public int getProximity(String a, String b) {
		String[] A = key2coordinates(a);
		String[] B = key2coordinates(b);
		int dif_x = Integer.parseInt(B[0]) - Integer.parseInt(A[0]);
		int dif_y = Integer.parseInt(B[1]) - Integer.parseInt(A[1]);
		
		if(dif_y > 0) return 0;
		if(dif_x < 0) return 1;
		if(dif_y < 0) return 2;
		if(dif_x > 0) return 3;
		return -1;
	}
	
	public Queue<Integer> nearestFood(Integer x, Integer y) {
		
		Queue<String> q = new PriorityQueue <String> ();
		Queue<Integer> path = new LinkedList <Integer> ();
		HashMap<String, Queue<Integer>> checked = new HashMap<String, Queue<Integer>>();  
		
		q.add(hashFunction(x, y));
		checked.put(hashFunction(x, y), path);
		
		boolean goal = false;
		while(!goal && q.size()!=0) {
			String node = q.poll();	
			
			if(map.containsKey(node)) {
				
				if(map.get(node).isFood() && map.get(node).isGoodFood()) {
					System.out.println();
					return checked.get(node);
				}

				for (String neighbor : map.get(node).getNeighbors()) {
					if(!checked.containsKey(neighbor)) {
						q.add(neighbor);
						path = new LinkedList <Integer> (checked.get(node));
						path.add(getProximity(neighbor, node));
						checked.put(neighbor, path);
					}
				}
			}
			
			
		}
		return null;
	}
	

}
