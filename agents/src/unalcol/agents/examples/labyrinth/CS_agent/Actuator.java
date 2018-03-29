package unalcol.agents.examples.labyrinth.CS_agent;

import java.util.HashMap;
import java.util.Queue;

public class Actuator {
	
	private static int MINIMUN_SIZE = 5;
	
	private Map map;
	private Integer x;
	private Integer y;
	private Integer Orientation;
	private Queue<String> queue;
	
	
	Actuator(){		
		x = 0;
		y = 0;
		Orientation = 0;
		map = new Map();
	}
	
	
	public int movement(boolean PF, boolean PR, boolean PB, boolean PL, boolean MT, boolean FAIL, boolean FOOD, Integer energy) {
		if (MT) return -1;
		if (energy < 15 || map.size() > MINIMUN_SIZE) return eat(PF, PR, PB, PL, MT, FAIL, FOOD, energy);
		return search(PF, PR, PB, PL, MT, FAIL, FOOD, energy);
	}
	
	public int search(boolean PF, boolean PR, boolean PB, boolean PL, boolean MT, boolean FAIL, boolean FOOD, Integer energy) {
		//Current Node
		Node current = new Node(FOOD);
		map.add(x, y, current);		
        
        boolean flag = true;
        int k=0;
        while( flag ){
            k = (int)(Math.random()*4);
            switch(k){
                case 0:
                    flag = PF;
                    break;
                case 1:
                    flag = PR;
                    break;
                case 2:
                    flag = PB;
                    break;
                default:
                    flag = PL;
                    break;                    
            }
        }
        //System.out.println(PF  + ", " + PR  + ", " + PB  + ", " + PL  + ", " + FOOD + ", " + energy);
        return k;
	}
	
	public int eat(boolean PF, boolean PR, boolean PB, boolean PL, boolean MT, boolean FAIL, boolean FOOD, Integer energy) {
		return 4;
	}
	
}
