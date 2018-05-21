package unalcol.agents.examples.games.reversi.test1;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

import unalcol.agents.Percept;

public class Board1 {
	
	public HashMap <String, Integer> possibles;
    public LinkedList <String> region4=new LinkedList<String>();
    public LinkedList <String> region3=new LinkedList<String>();
    public LinkedList <String> region2=new LinkedList<String>();
    public LinkedList <String> region1=new LinkedList<String>();
    public LinkedList <String> validMoves;
    
    protected String COLOR;
    protected String RIVAL;
    private static int SIZE;
	public Board1 (String color, String rival) {
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
				if(getCell(p,i,j).equals("space")) {
					analizeValidMove(p, i, j);
				}
			}
		}
		
	}
	

	

	public void analizeValidMove(Percept p, int x, int y) {
		
		Stack <MovementInInAnalisis> list = new Stack<MovementInInAnalisis>();
		int score=0;
		String s=square(x,y);
		if(region4.contains(s)) {
			score=SIZE;
		}else if(region3.contains(s)) {
			score=SIZE/2;
		}else if(region2.contains(s)) {
			score=(int)SIZE/4;
		}
		
		for(int i=-1; i<2; i++) {
			if((x+i)<0 || (x+i)>=SIZE)
				continue;
			
			for(int j=-1; j<2; j++) {
				if((y+j)<0 || (y+j)>=SIZE)
					continue;
				
				if((j==0&&i==0))
					continue;
				
				if (getCell(p,x+i,y+j).equals(RIVAL)) {
					if((x+2*i)<0 || (x+2*i)>=SIZE || (y+2*j)<0 || (y+2*i)>=SIZE)
						continue;
					
					list.push(new MovementInInAnalisis(x+i,y+j,i,j));
				}
			}
		}
		
		while(!list.isEmpty()) {
			
			MovementInInAnalisis actual = list.pop();

			int actualX= actual.x;
			int actualY= actual.y;
			int tmpScore = 0;
			String n=getCell(p, actualX, actualY);
			boolean found=true;
			while(n.equals(RIVAL)) {
				tmpScore+=1;
				actualX+=actual.i;
				actualY+=actual.j;
				if((actualX)<0 || (actualX)>=SIZE || (actualY)<0 || (actualY)>=SIZE) {
					found=false;
					break;
				}
				n=getCell(p, actualX, actualY);
			}
			
			if(n.equals(COLOR) && found) {
				score+=tmpScore;
				possibles.put(square(x,y),score);
			}
			
		}
	}
	
	public void choice() {
		
	}
	
	class MovementInInAnalisis {
		public int x;
		public int y;
		public int i;
		public int j;
		
		
		public MovementInInAnalisis(int x, int y, int i, int j) {
			this.x = x;
			this.y = y;
			this.i = i;
			this.j = j;
		}
	}
	
	class BoardState {
		public int id;
		public int level;
		public boolean max; 
		public HashMap <String, Integer> currentMap;
		public int score;
		public String color;
	
		public BoardState(int id, boolean max, int level, HashMap <String, Integer> currentMap, int score) {
			this.id=id;
			this.max=max;
			this.level=level;
			this.currentMap=new HashMap<String, Integer>();
			this.currentMap.putAll(currentMap);
			this.score=score;
		}
	}
}
