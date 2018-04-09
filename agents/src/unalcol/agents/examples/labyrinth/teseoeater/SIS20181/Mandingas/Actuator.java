package unalcol.agents.examples.labyrinth.teseoeater.SIS20181.Mandingas;

import java.util.HashMap;
import java.util.Queue;
/**
*
* @author Cristian Rojas y Sebastian Martinez
*/
public class Actuator {
	
	private static int MINIMUN_SIZE = 5;
	
	private Map map;
	private Integer x;
	private Integer y;
	private Integer orientation;
	private Queue<String> queue;	
	private Queue<Integer> path;
	
	private boolean keepEating;
	private boolean lookingForFood;
	private int tryFood;
	private boolean ratherWait;
	
	private boolean rivalAlive;
	
	public int maxHealth;
	
	Actuator(){		
		x = 0;
		y = 0;
		orientation = 0;
		map = new Map();
		keepEating=false;
		lookingForFood = false;
		tryFood=0;
		ratherWait=false;
		maxHealth=Integer.MAX_VALUE;
		/*
		 * 0->Looking for first food
		 * 1->Being careful
		 * 2->Now can eat without worries
		 */
	}
	
	/**
	 * 
	 * @return the orientation
	 */
	public int getOrientation() {
		return orientation;
	}
	
	
	public void setMaxHealth(int _maxHealth) {
		maxHealth=_maxHealth;
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
	 * Adds/edits a new node;
	 * @param walls
	 */
	public void addNode(boolean [] walls){
		Node current;
		
		if(map.contains(x, y)) {
			current=map.node(x, y);
			current.setNeighbors(x, y, walls);
		}else {
			current = new Node(x, y, walls);
		}
		map.add(x, y, current);
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

		if (MT) return -1;
		
		boolean [] isAgent = getSurroundings(AF, AR, AB, AL);
		boolean [] isWalls = getSurroundings(PF, PR, PB, PL);
		//Add the the node depending on the circustances
		addNode(isWalls);
		//Each time the agent finds food it recharge the energy and update the map
		if(FOOD) {
			lookingForFood = false;
			Node thisNode=map.node(x, y);
			if(!thisNode.isFood()) {	
				if(tryFood%2==0) {
					thisNode.thisIsFood();
					map.add(x, y, thisNode);
					
					if(tryFood!=2)
						tryFood = (int)(Math.random()*4);
				}
				return 4;
			}
			if(isGood || thisNode.isGoodFood()) {
				tryFood=2;
				if(!thisNode.isGoodFood()) {
					thisNode.thisIsGoodFood();
					map.add(x, y, thisNode);
				}
				if (((energy < (maxHealth-1)) || keepEating)) {
					keepEating=true;
					return eat(PF, PR, PB, PL, MT, FAIL, AF, AR, AB, AL, FOOD, energy, isGood);
				}
			}				
		}
		
		//Changes the state when the the agent is hungry
		if(energy < (maxHealth/2) && !lookingForFood) {
			path = map.nearestFood(x, y);
			if(!path.isEmpty()) {
				lookingForFood = true;
				//System.out.println("lookingForFood..");
			}
		}
		
		if(path== null || path.size()==0) {
			
			path = map.nearestUnexplored(x, y);
			rivalAlive=!map.checkCoincidence();
			map.checkPending();
			//System.out.println("Searching..");
		}
		
		
		//Follow a path when path has a path 
		if(path!= null && path.size() > 0) {
			int u = path.peek();
			int v=u;
			
			//If the path is blocked, then we evaluate what kind of action should be taken.
			if(isAgent[u] || isWalls[u]) {
				//Let's first check if there isn't any route we should take care of
				rivalAlive=!map.checkCoincidence();
				map.checkPending();
				Queue <Integer> tmpPath=map.alternativeRoute(x, y, u, lookingForFood, isWalls[u]);
				if(tmpPath!= null && tmpPath.size() > 0) {
					u = tmpPath.peek();
					if(u==v) {
						u=-2;
					}else{
						path=tmpPath;
						u=path.poll();
					}
				}else{
					u=-2;
				}
			}else {
				path.poll();
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
		Node thisNode=map.node(x, y);
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
		lookingForFood=false;
		map.clear(false);
		path.clear();
	}
	
	public void hardReset() {
		x = 0;
		y = 0;
		lookingForFood=false;
		map.clear(true);
		path.clear();
	}
	
	public void killRival() {
		rivalAlive=false;
	}
	
	public boolean rivalIsAlive() {
		return rivalAlive;
	}
	/*
	 * TODO constant checker i'm in the right map judging if there is food in the corresponding place or not
	 * 
	 */
		
}
