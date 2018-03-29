package unalcol.agents.examples.labyrinth.Mandingas_agent;

import java.util.HashMap;
import java.util.LinkedList;
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
	private boolean keepEating=false;
	
	Actuator(){		
		x = 0;
		y = 0;
		orientation = 0;
		map = new Map();
		Node current = new Node();
		map.add(x, y, current);
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
	public void changeCoordinates(boolean print) {
		switch(orientation) {
		case(0):
			y--;
			if(print) System.out.println("X: "+x+"| Y: "+y);
			break;
		case(1):
			x++;
			if(print) System.out.println("X: "+x+" | Y: "+y);
			break;
		case(2):
			y++;
			if(print) System.out.println("X: "+x+" | Y: "+y);
			break;
		case(3):
			x--;
			if(print) System.out.println("X: "+x+" | Y: "+y);
			break;
		default:
			System.out.println("Error de Programa");
			System.exit(-1);
		}
		if(!map.contains(x, y)) {
			Node current = new Node();
			map.add(x, y, current);
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
	public int task(boolean PF, boolean PR, boolean PB, boolean PL, boolean MT, boolean FAIL, boolean FOOD, Integer energy, boolean isBad) {
		
		if (MT) return -1;
		//Defines if it needs to eat if energy is below 15 or has a change to rise to 40
		
		
		
		if(FOOD) {
			Node thisNode=map.node(x, y);
			if(!thisNode.isFood()) {
				thisNode.thisIsFood();
				map.add(x, y, thisNode);
			}
			if(isBad) {
				if(!thisNode.isBadFood()) {
					thisNode.thisIsBadFood();
					map.add(x, y, thisNode);
				}	
			}else {
				if (((energy < 15) || keepEating)) {
					keepEating=true;
					return eat(PF, PR, PB, PL, MT, FAIL, FOOD, energy, isBad);
				}
			}
		}
		return search(PF, PR, PB, PL, MT, FAIL, FOOD, energy); 
	}
	
	/**
	 * 
	 * Defines that, if there is food, it should eat until it's energy is above 40
	 * 
	 * @param The perceptions
	 * @return No identifier for the actions
	 * 
	 */
	public int eat(boolean PF, boolean PR, boolean PB, boolean PL, boolean MT, boolean FAIL, boolean FOOD, Integer energy, boolean isBad) {
		Node thisNode=map.node(x, y);
		boolean shouldEat=!thisNode.isBadFood();
		
		if(shouldEat) {
			keepEating=(energy<40);
			return 4;
		}else {
			return search(PF, PR, PB, PL, MT, FAIL, FOOD, energy);
		}
	}
	
	/**
	 * 
	 * Defines movement accord of where the agent needs to rotate.
	 * 
	 * @param The perceptions
	 * @return No identifier for the actions
	 */
	public int search(boolean PF, boolean PR, boolean PB, boolean PL, boolean MT, boolean FAIL, boolean FOOD, Integer energy) {
		boolean [] neigh = getSurroundings(PF, PR, PB, PL);
		setNeighbors(neigh);
		printNeig();
		boolean flag = true;
        int k=0;
        
        //System.out.println(orientation+": "+neigh[0]+" "+neigh[1]+" "+neigh[2]+" "+neigh[3]);
        while( flag ){
            k = (int)(Math.random()*4);
            switch(k){
                case 0:
                    flag = neigh[0];
                    break;
                case 1:
                    flag = neigh[1];
                    break;
                case 2:
                    flag = neigh[2];
                    break;
                default:
                    flag = neigh[3];
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
	
	/**
	 * 
	 * Asigna todos los nodos como debe ser
	 * 
	 * @param Los vecinos de la casilla actual
	 */
	public void setNeighbors(boolean walls[]){
		LinkedList<int[]> neighbors = new LinkedList<int[]>();
		for(int i = 0; i<4;i++ ) {
			if(!walls[i]) {
				int[] n = new int [2];
				switch(i) {
					case(0):
						n[0]=x;
						n[1]=y-1;
						neighbors.add(n);
						break;
					case(1):
						n[0]=x+1;
						n[1]=y;
						neighbors.add(n);
						break;
					case(2):
						n[0]=x;
						n[1]=y+1;
						neighbors.add(n);
						break;
					case(3):
						n[0]=x-1;
						n[1]=y;
						neighbors.add(n);
						break;
				}
			}
		}
		Node current = map.node(x, y);
		
		current.setNeighbors(neighbors);
		map.add(x, y, current);
	}
	
	public void printNeig() {
		System.out.println("-------------------");
		System.out.println("X: "+x+"| Y: "+y);
		int i=1;
		for (int[] neigh : map.node(x,y).getNeighbors()) {
			System.out.println("Vecino: "+i);
			System.out.println("x: "+neigh[0]+"| y: "+neigh[1]);
			if(map.contains(neigh[0], neigh[1])) {
				if(map.node(neigh[0], neigh[1]).isFood()) {
					System.out.println("Tiene Comida");
				}
			}
		}
		System.out.println("\n");
	}
}
