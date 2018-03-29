package unalcol.agents.examples.labyrinth.Mandingas_agent;

import java.util.HashMap;
import java.util.Queue;

public class Actuator {
	
	private static int MINIMUN_SIZE = 5;
	
	private Map map;
	private Integer x;
	private Integer y;
	private Integer orientation;
	private Queue<String> queue;
	private boolean goEat=false;
	private int lastEnergy = 0;
	Actuator(){		
		x = 0;
		y = 0;
		orientation = 0;
		map = new Map();
	}
	
	public int getOrientation() {
		return orientation;
	}
	
	public void changeOrientation(int or) {
		orientation=or;
	}

	public void changeCoordinates(boolean print) {
		switch(orientation) {
		case(0):
			y--;
			if(print) System.out.println("X: "+x+"Y: "+y);
			break;
		case(1):
			x++;
			if(print) System.out.println("X: "+x+"Y: "+y);
			break;
		case(2):
			y++;
			if(print) System.out.println("X: "+x+"Y: "+y);
			break;
		case(3):
			x--;
			if(print) System.out.println("X: "+x+"Y: "+y);
			break;
		default:
			System.out.println("Error de Programa");
			System.exit(-1);
		}
	}
	
	public int movement(boolean PF, boolean PR, boolean PB, boolean PL, boolean MT, boolean FAIL, boolean FOOD, Integer energy) {
		lastEnergy=energy;
	
		if (MT) return -1;
		if ((energy < 15) || goEat) {
			goEat=true;
			return eat(PF, PR, PB, PL, MT, FAIL, FOOD, energy);
		}
		return search(PF, PR, PB, PL, MT, FAIL, FOOD, energy);
	}
	
	public int search(boolean PF, boolean PR, boolean PB, boolean PL, boolean MT, boolean FAIL, boolean FOOD, Integer energy) {
		boolean [] neigh = getDirections( PF, PR, PB, PL);
		Node current = new Node(FOOD);
		map.add(x, y, current);
		
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
	
	public boolean[] getDirections(boolean PF, boolean PR, boolean PB, boolean PL) {
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
	
	
	public int eat(boolean PF, boolean PR, boolean PB, boolean PL, boolean MT, boolean FAIL, boolean FOOD, Integer energy) {
		if(FOOD) {
			goEat=(energy<40);
			return 4;
		}
		return search(PF, PR, PB, PL, MT, FAIL, FOOD, energy);
	}
	
}
