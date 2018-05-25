package unalcol.agents.examples.games.reversi.isi20181.mandingas;

import java.util.HashMap;
import java.util.ArrayDeque;
import unalcol.agents.Percept;

public class Board {
	
	public HashMap <String, Integer> possibles;
    public ArrayDeque <String> region4=new ArrayDeque<String>();
    public ArrayDeque <String> region3=new ArrayDeque<String>();
    public ArrayDeque <String> region2=new ArrayDeque<String>();
    public ArrayDeque <String> region1=new ArrayDeque<String>();
    public ArrayDeque <String> validMoves;
    
    protected String COLOR;
    protected String RIVAL;
    public int SIZE;
	public Board (String color, String rival) {
		COLOR = color;
		RIVAL = rival;
	}
	
	public String square (int x, int y) {
    	return x+":"+y;
    }
    
    public String move (int x, int y) {
    	return x+":"+y+":"+COLOR;
    }
    
    public String getCell(Percept p, int i, int j) {
    	return String.valueOf(p.getAttribute(square(i,j)));
    }    

	public void regions(int size) {
		region4=new ArrayDeque<String>();
	    region3=new ArrayDeque<String>();
	    region2=new ArrayDeque<String>();
	    region1=new ArrayDeque<String>();
		SIZE=size;
		
    	int border=(size-1);
    	for(int i=0;i<size;i++) {
    		for(int j=0;j<size;j++) {
        		region1.add(i+":"+j);
        	}
    	}
    	
    	//Region 4: Corners, Best Priority
    	region4.add("0:0"); region4.add("0:"+border);
    	region4.add(border+":0"); region4.add(border+":"+border);
    	
    	//Region 3: Buffer, Bad Priority
    	int buffer=(border-1);
    	region3.add("1:0"); region3.add("0:1"); region3.add("1:1");
    	region3.add(buffer+":0"); region3.add(border+":1"); region3.add(buffer+":1");
    	region3.add("0:"+buffer); region3.add("1:"+buffer); region3.add("1:"+border);
    	region3.add(border+":"+buffer); region3.add(buffer+":"+buffer); region3.add(buffer+":"+border);
    	
    	//Region 2: Edges, Good Priority
    	int edges=size-4;
    	
    	for(int i=0;i<4;i++) {
    		for(int j=2;j<(2+edges);j++) {
    			switch(i) {
    				case 0:
    					region2.add("0:"+j);
    					break;
    				case 1:
    					region2.add(j+":0");
    					break;
    				case 2:
    					region2.add(border+":"+j);
    					break;
    				case 3:
    					region2.add(j+":"+border);
    					break;
    			}
    		}
    	}
    	
    	//Region 1: Center. Neutral Priority
    	region1.removeAll(region2);
    	region1.removeAll(region3);
    	region1.removeAll(region4);
    }
	
	public void printPossibles() {
		for(String key : possibles.keySet()) {
			System.out.println(key + " " + possibles.get(key));
		}
	}
	
	public void findAllMoves(Percept p) {
		
		possibles = new HashMap<String, Integer>();
		for(int i=0; i<SIZE; i++) {
			for(int j=0; j<SIZE; j++) {
				if(getCell(p,i,j).equals(COLOR)) {
					analizeValidMove(p, i, j);
				}
			}
		}
		
	}
	

	
	public void analizeValidMove(Percept p, int x, int y) {
		
		ArrayDeque <MovementInInAnalisis> list = new ArrayDeque<MovementInInAnalisis>();
		
				
		for(int i=-1; i<2; i++) {
			if((x+i)<0 || (x+i)>=SIZE)
				continue;
			
			for(int j=-1; j<2; j++) {
				if((y+j)<0 || (y+j)>=SIZE)
					continue;
				
				if((j==0&&i==0))
					continue;
				
				if (getCell(p,x+i,y+j).equals(RIVAL)) {
					list.add(new MovementInInAnalisis(x+i,y+j,1,i,j));
				}
			}
		}
		
		while(!list.isEmpty()) {
			MovementInInAnalisis actual = list.poll();
			if((actual.x+actual.i)<0 || (actual.x+actual.i)>=SIZE || (actual.y+actual.j)<0 || (actual.y+actual.j)>=SIZE) {
				continue;
			}
			if(getCell(p, actual.x + actual.i, actual.y + actual.j).equals(RIVAL)) {
				list.add(new MovementInInAnalisis(actual.x + actual.i, actual.y + actual.j, actual.fichasCambiadas + 1, actual.i, actual.j));
			}else if(getCell(p, actual.x + actual.i, actual.y + actual.j).equals("space")) {
				if(possibles.containsKey(square(actual.x, actual.y))) {
					possibles.put(square(actual.x + actual.i, actual.y + actual.j), possibles.get(square(actual.x, actual.y)+actual.fichasCambiadas));
				}else {
					possibles.put(square(actual.x + actual.i, actual.y + actual.j),actual.fichasCambiadas);
				}
			}
			
		}
	
	}
	
	class MovementInInAnalisis {
	       public int x;
	       public int y;
	       public int fichasCambiadas;
	       public int i;
	       public int j;

	
	public MovementInInAnalisis(int x, int y, int fichasCambiadas, int i, int j) {
	      this.x = x;
	      this.y = y;
	      this.fichasCambiadas = fichasCambiadas;
	      this.i = i;
	      this.j = j;
	   }
	}
	
}
