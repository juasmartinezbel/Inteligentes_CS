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
	
	public void changeOrientation() {
		orientation=(orientation+1)%4;
	}
	
	public void seeOrientation() {
		switch(orientation) {
		case(0):
			System.out.println("Norte");
			break;
		case(1):
			System.out.println("Este");
			break;
		case(2):
			System.out.println("Sur");
			break;
		case(3):
			System.out.println("Oeste");
			break;
		default:
			System.out.println("Error de Programa");
			System.exit(-1);
		}
	}
	
	public void changeCoordinates() {
		switch(orientation) {
		case(0):
			y--;
			System.out.println("X: "+x+"Y: "+y);
			break;
		case(1):
			x++;
			System.out.println("X: "+x+"Y: "+y);
			break;
		case(2):
			y++;
			System.out.println("X: "+x+"Y: "+y);
			break;
		case(3):
			x--;
			System.out.println("X: "+x+"Y: "+y);
			break;
		default:
			System.out.println("Error de Programa");
			System.exit(-1);
		}
	}
	
	public int movement(boolean PF, boolean PR, boolean PB, boolean PL, boolean MT, boolean FAIL, boolean FOOD, Integer energy) {
		lastEnergy=energy;
		System.out.println(lastEnergy);
		if (MT) return -1;
		if ((energy < 15) || goEat) {
			goEat=true;
			return eat(PF, PR, PB, PL, MT, FAIL, FOOD, energy);
		}
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
        return k;
	}
	
	public int eat(boolean PF, boolean PR, boolean PB, boolean PL, boolean MT, boolean FAIL, boolean FOOD, Integer energy) {
		if(FOOD) {
			goEat=(energy<40);
			return 4;
		}
		return search(PF, PR, PB, PL, MT, FAIL, FOOD, energy);
	}
	
}
