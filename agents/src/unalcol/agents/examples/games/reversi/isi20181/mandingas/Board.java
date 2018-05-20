package unalcol.agents.examples.games.reversi.isi20181.mandingas;

import java.util.LinkedList;
import unalcol.agents.Percept;

public class Board {
	
    public LinkedList <String> region4=new LinkedList<String>();
    public LinkedList <String> region3=new LinkedList<String>();
    public LinkedList <String> region2=new LinkedList<String>();
    public LinkedList <String> region1=new LinkedList<String>();
    public LinkedList <String> validMoves;
    
    protected String COLOR;
    protected String RIVAL;
    private static int SIZE;
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
    
    
	public void regions(int size) {
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
	
	public LinkedList<String> findAllMoves(Percept p) {
		
		validMoves = new LinkedList<String>();
		
		for(int i=0; i<SIZE; i++) {
			for(int j=0; j<SIZE; j++) {
				if(analizeValidMove(i, j, p))
					validMoves.add(square(i,j));
			}
		}
		return validMoves;
	}
	
	
	public boolean analizeValidMove(int x, int y, Percept p) {
		String space=String.valueOf(p.getAttribute(square(x,y)));
		if(!space.equals("space")) {
			return false;
		}
		
		for(int i=-1; i<2; i++) {
			if((x+i)<0 || (x+i)>=SIZE)
				continue;
			
			for(int j=-1; j<2; j++) {
				if((y+j)<0 || (y+j)>=SIZE)
					continue;
				
				if((y+j==0&&x+i==0))
					continue;
				
				
				
			}
		}
		return false;
	}
	
}
