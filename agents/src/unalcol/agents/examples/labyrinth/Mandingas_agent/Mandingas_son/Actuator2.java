package unalcol.agents.examples.labyrinth.Mandingas_agent.Mandingas_son;

import java.util.HashMap;
import java.util.Queue;
/**
*
* @author Cristian Rojas y Sebastian Martinez
*/
public class Actuator2 {
	
	private static int MINIMUN_SIZE = 5;
	
	private Map2 map;
	private Integer x;
	private Integer y;
	private Integer orientation;
	private Queue<String> queue;	
	private Queue<Integer> path;
	
	private boolean keepEating;
	private boolean lookingForFood;
	private boolean tryFood;
	
	Actuator2(){		
		x = 0;
		y = 0;
		orientation = 0;
		map = new Map2();
		keepEating=false;
		lookingForFood = false;
		tryFood=true;
	}
	
	/**
	 * 
	 * @return the orientation
	 */
	public int getOrientation() {
		return orientation;
	}
	
	/**
	 * Change the orientation to a new direction
	 * 0 -> North
	 * 1 -> East
	 * 2 -> South
	 * 3 -> West
	 * @param or, the new orientation
	 */
	public void changeOrientation(int or) {
		orientation=or;
	}
	
	
	/**
	 * 
	 * Change the x/y coordinates if the agent moves
	 * 
	 * @param print defines if we want to print the coordinates for convinence
	 */
	public void changeCoordinates(boolean print, int id) {
		switch(orientation) {
		case(0):
			y--;
			if(print) System.out.println("---------\nid: "+id+"\nX: "+x+"| Y: "+y+"\n");
			break;
		case(1):
			x++;
			if(print) System.out.println("---------\nid: "+id+"\nX: "+x+" | Y: "+y+"\n");
			break;
		case(2):
			y++;
			if(print) System.out.println("---------\nid: "+id+"\nX: "+x+" | Y: "+y+"\n");
			break;
		case(3):
			x--;
			if(print) System.out.println("---------\nid: "+id+"\nX: "+x+" | Y: "+y+"\n");
			break;
		default:
			System.out.println("Error de Programa");
			System.exit(-1);
		}		
	}
	
	/**
	 * 
	 * Defines the tasks the agent is going to make, whether is to move or eat. Also sets if the food was bad
	 * 
	 * @param The perceptions
	 * @return No identifier for the actions
	 * 
	 */
	public int task(boolean PF, boolean PR, boolean PB, boolean PL, boolean MT, boolean FAIL, boolean AF, boolean AR, boolean AB, boolean AL, boolean FOOD, Integer energy, boolean isGood) {
		boolean [] isAgent = getSurroundings(AF, AR, AB, AL);
		if (MT) return -1;
		
		//Add the the node if its not contained 
		if(!map.contains(x, y)) {
			Node2 current = new Node2(x, y, getSurroundings(PF, PR, PB, PL));
			map.add(x, y, current);
		}	
		

		

		//Each time the agent finds food it recharge the energy and update the map
		if(FOOD) {
			lookingForFood = false;
			Node2 thisNode=map.node(x, y);
			if(!thisNode.isFood()&&tryFood) {	
				tryFood = Math.random() < 0.5;
				thisNode.thisIsFood();
				map.add(x, y, thisNode);
				return 4;
			}
			if(isGood || thisNode.isGoodFood()) {
				if(!thisNode.isGoodFood()) {
					thisNode.thisIsGoodFood();
					map.add(x, y, thisNode);
				}
				if (((energy < 39) || keepEating)) {
					
					keepEating=true;
					return eat(PF, PR, PB, PL, MT, FAIL, AF, AR, AB, AL, FOOD, energy, isGood);
				}
			}				
		}
		
		//Changes the state when the the agent is hungry
		if(energy < 20 && !lookingForFood) {
			path = map.nearestFood(x, y);
			if(!path.isEmpty()) {
				tryFood=true;
				lookingForFood = true;
				//System.out.println("lookingForFood..");
			}
		}
		
		if(path== null || path.size()==0) {
			path = map.nearestUnexplored(x, y);
			map.checkPending();
			//System.out.println("Searching..");
		}
		
		
		//Follow a path when path has a path 
		if(path!= null && path.size() > 0) {
			int u = path.poll();
			int v=u;
			
			//If the path is blocked, then we evaluate what kind of action should be taken.
			if(isAgent[u]) {
				path=map.alternativeRoute(x, y, u, lookingForFood);
				if(path!= null && path.size() > 0) {
					u = path.poll();
					if(u==v) {
						u=-2;
					}
				}else {
					u=-2;
				}
			}
			return u; 
		}

		return -2; 
	}
	
	/**
	 * 
	 * Defines that, if there is food, it should eat until it's energy is above 40
	 * 
	 * @param The perceptions
	 * @return No identifier for the actions
	 * 
	 */
	public int eat(boolean PF, boolean PR, boolean PB, boolean PL,  boolean MT, boolean FAIL, boolean AF, boolean AR, boolean AB, boolean AL, boolean FOOD, Integer energy, boolean isBad) {
		Node2 thisNode=map.node(x, y);
		boolean shouldEat=thisNode.isGoodFood();
		
		if(shouldEat) {
			keepEating=(energy<40);
			return 4;
		}else {
			return search(PF, PR, PB, PL, MT, FAIL, AF, AR, AB, AL, FOOD, energy);
		}
		
	}
	
	/**
	 * 
	 * Defines movement accord of where the agent needs to rotate.
	 * 
	 * @param The perceptions
	 * @return No identifier for the actions
	 */
	public int search(boolean PF, boolean PR, boolean PB, boolean PL, boolean MT, boolean FAIL, boolean AF, boolean AR, boolean AB, boolean AL, boolean FOOD, Integer energy) {
		boolean [] neigh = getSurroundings(PF, PR, PB, PL);
		boolean [] agent = getSurroundings(AF, AR, AB, AL);
		boolean flag = true;
        int k=0;
        //System.out.println(orientation+": "+neigh[0]+" "+neigh[1]+" "+neigh[2]+" "+neigh[3]);
        while( flag ){
            k = (int)(Math.random()*4);
            switch(k){
                case 0:
                    flag = neigh[0]&&agent[0];
                    break;
                case 1:
                    flag = neigh[1]&&agent[1];
                    break;
                case 2:
                    flag = neigh[2]&&agent[2];
                    break;
                default:
                    flag = neigh[3]&&agent[3];
                    break;                    
            }
        }
        return k;
		
	}
	
	
	/**
	 * 
	 * Defines the surroundings accord where the agent is looking
	 * 
	 * @param The perceptions
	 * @return No identifier for the actions
	 */
	public boolean[] getSurroundings(boolean PF, boolean PR, boolean PB, boolean PL) {
		/*
		 * 
		 * 0 -> North
		 * 1 -> East
		 * 2 -> South
		 * 3 -> West
		 * 
		 */
		boolean [] u = new boolean [4];
		switch(orientation) {
		case(0):
			u[0] = PF;u[1] = PR;u[2] = PB;u[3] = PL;
			break;		
		case(1):
			u[0] = PL; u[1] = PF; u[2]=PR;u[3] = PB;
			break;
		case(2):
			u[0] = PB;u[1] = PL;u[2] = PF;u[3] = PR;
			break;
		case(3):
			u[0] = PR;u[1] = PB;u[2] = PL;u[3] = PF;
			break;
		default:
			System.out.println("Error de Programa");
			System.exit(-1);
		}
		return u;
	}
	
	public void resetMap() {
		map.clear();
		path.clear();
		//System.out.println("SE BORRÓ TODO TODILLO");
	}
		
}
