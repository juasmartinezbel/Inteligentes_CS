package unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.mandingas;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
/**
*
* @author Cristian Rojas y Sebastian Martinez
*/
public class Map {
	private HashMap<String, Node> map;
	private boolean pending=false;
	private String nodePending="";
	private String keyPending="";
	private String lastKey="";
	
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
		
		Queue<String> q = new LinkedList <String> ();
		Queue<Integer> path = new LinkedList <Integer> ();
		HashMap<String, Queue<Integer>> checked = new HashMap<String, Queue<Integer>>();  
		
		q.add(hashFunction(x, y));
		checked.put(hashFunction(x, y), path);
		
		boolean goal = false;
		while(!goal && q.size()!=0) {
			String node = q.poll();	
			
			if(map.containsKey(node)) {
				
				if(map.get(node).isFood() && map.get(node).isGoodFood()) {
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
		return new LinkedList <Integer>();
	}
	
	public Queue<Integer> nearestUnexplored(Integer x, Integer y){
		Queue<String> q = new LinkedList <String> ();
		Queue<Integer> path = new LinkedList <Integer> ();
		HashMap<String, Queue<Integer>> checked = new HashMap<String, Queue<Integer>>();  
		
		q.add(hashFunction(x, y));
		checked.put(hashFunction(x, y), path);

		while(q.size()!=0) {			
			String node = q.poll();
			
			if(!map.containsKey(node)) {
				return checked.get(node);
			}
			LinkedList<String> ls= map.get(node).getNeighbors();
			LinkedList<String> notVisited = new LinkedList<String>();
			
			
			for (String neighbor:ls) {
				if(!map.containsKey(neighbor))
					notVisited.add(neighbor);		
			}
			
			if(!notVisited.isEmpty()) {
				ls=rearrange(notVisited);
			}
			
			for (String neighbor:ls) {
				if(!checked.containsKey(neighbor)) {
					q.add(neighbor);
					path = new LinkedList <Integer> (checked.get(node));
					path.add(getProximity(neighbor, node));
					checked.put(neighbor, path);					
				}
			}	
			
		}
		return new LinkedList <Integer>();
	}
	
	public LinkedList<String> rearrange(LinkedList<String> n){
		Collections.shuffle(n);
		return n;
	}
	
	/**
	 * 
	 * Looks for an alternative route to take if the agent faces another agent
	 * 
	 * @param x
	 * @param y
	 * @param agent
	 * @param lookingForFood Defines what kind of search should be done
	 * @return
	 */
	public Queue<Integer> alternativeRoute(Integer x, Integer y, int agent, boolean lookingForFood, boolean isWall){
		Queue<Integer> newPath;
		
		int x_next=x;
		int y_next=y;
		switch(agent){
			case(0):
				y_next--;
				break;
			case(1):
				x_next++;
				break;
			case(2):
				y_next++;
				break;
			case(3):
				x_next--;
				break;
		}
		
		String bye=hashFunction(x_next,y_next);
		
		//Checks if the node is visited or if it is one that should be explored
		if(map.containsKey(bye)) {
			Node tmpNode= map.get(bye);
			map.remove(bye);
			if(lookingForFood) {
				newPath=nearestFood(x, y);
			}else {
				newPath=hideNeighbor(x,y,bye, isWall);
			}
			if(!isWall)
				map.put(bye, tmpNode);
		}else{
			newPath=hideNeighbor(x,y,bye, isWall);
		}
		
		//If the path in empty, we return that there is no other choice but that path, then the agent should wait
		if (newPath.isEmpty()) {
			newPath.add(agent);
		}
		
		return newPath;
	}
	
	/**
	 * Hides temporally the neighbor to see if is able to visit it again
	 * @param x
	 * @param y
	 * @param bye
	 * @param isWall verifies if the thing blocking is a wall
	 * @return
	 */
	public Queue<Integer> hideNeighbor(Integer x, Integer y, String bye, boolean isWall) {
		String tmpKey=hashFunction(x,y);
		Node tmpNode= map.get(tmpKey);
		if(!isWall) {
			nodePending=bye;
			keyPending=tmpKey;
			pending=true;
		}
		tmpNode.removeNeighbor(bye);
		map.put(tmpKey,tmpNode);
		
		return nearestUnexplored(x, y);
	}
	
	public void checkPending() {
		if(pending&&!keyPending.equals("")) {
			Node p = map.get(keyPending);
			p.addNeighbor(nodePending);
			map.put(keyPending,p);
			lastKey=keyPending;
			nodePending="";
			keyPending="";
		}
		pending=false;
	}
	
	/**
	 * Checks one last time if we get to the point to see if the rival is dead
	 * @return
	 */
	public boolean checkCoincidence() {
		if(!keyPending.equals("")) {
			return keyPending.equals(lastKey);
		}
		return false;
	}
	
	
	/**
	 * Clears the map if the Agent is stuck, except for the nodes that have food.
	 * 
	 */
	public void clear(boolean isHard) {
		if(isHard) {
			map.clear();
		}else {
			LinkedList <String> S = new LinkedList<String>();
			S.addAll(map.keySet());
			for(String s : S) {
				if(!map.get(s).isFood()) {
					map.remove(s);
				}
			}
		}
		pending=false;
		nodePending="";
		keyPending="";
	}	

}
