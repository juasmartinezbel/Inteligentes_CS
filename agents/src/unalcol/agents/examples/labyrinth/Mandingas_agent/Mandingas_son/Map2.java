package unalcol.agents.examples.labyrinth.Mandingas_agent.Mandingas_son;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
/**
*
* @author Cristian Rojas y Sebastian Martinez
*/
public class Map2 {
	private HashMap<String, Node2> map;
	private boolean pending=false;
	private String nodePending="";
	private String keyPending="";
	
	
	public Map2() {
		map = new HashMap<String, Node2>();
	}	
	
	public static String hashFunction(Integer x, Integer y) {
		return (String.valueOf(x) +","+ String.valueOf(y));
	}
	
	public int size() {
		return map.size();
	}
	
	public void add(Integer x, Integer y, Node2 node) {
		map.put(hashFunction(x, y), node);
	}
	
	public Node2 node(Integer x, Integer y) {
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
			
			for (String neighbor : map.get(node).getNeighbors()) {
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
	public Queue<Integer> alternativeRoute(Integer x, Integer y, int agent, boolean lookingForFood){
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
			Node2 tmpNode= map.get(bye);
			map.remove(bye);
			if(lookingForFood) {
				newPath=nearestFood(x, y);
				if(newPath.isEmpty()) {
					newPath=hideNeighbor(x,y,bye);
				}
			}else {
				newPath=hideNeighbor(x,y,bye);
			}
			//System.out.println("Se ha removido: "+bye+" que era el paso "+agent+". Ahora el agenta dará un paso en la dirección "+newPath.peek());
			map.put(bye, tmpNode);
		}else{
			newPath=hideNeighbor(x,y,bye);
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
	 * @return
	 */
	public Queue<Integer> hideNeighbor(Integer x, Integer y, String bye) {
		String tmpKey=hashFunction(x,y);
		Node2 tmpNode= map.get(tmpKey);
		nodePending=bye;
		keyPending=tmpKey;
		tmpNode.removeNeighbor(bye);
		map.put(tmpKey,tmpNode);
		pending=true;
		return nearestUnexplored(x, y);
	}
	
	public void checkPending() {
		if(pending&&!keyPending.equals("")) {
			//System.out.println("Going back to normal");
			Node2 p = map.get(keyPending);
			p.addNeighbor(nodePending);
			map.put(keyPending,p);
			nodePending="";
			keyPending="";
		}
		pending=false;
	}
	
	public void clear() {
		map.clear();
		pending=false;
		nodePending="";
		keyPending="";
	}	

}
