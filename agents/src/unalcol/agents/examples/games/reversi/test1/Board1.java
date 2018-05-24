package unalcol.agents.examples.games.reversi.test1;

import java.util.ArrayDeque;
import java.util.HashMap;

import unalcol.agents.Percept;

public class Board1 {
	
	public HashMap <String, Integer> possibles;
	public ArrayDeque <BoardState> changesStates;
    public ArrayDeque <String> region4;
    public ArrayDeque <String> region3;
    public ArrayDeque <String> region2;
    public ArrayDeque <String> region1;
    public ArrayDeque <String> empty;
    
    protected String COLOR;
    protected String RIVAL;
    public int SIZE;
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
    
    public String getCell(Percept p, String s) {
    	return String.valueOf(p.getAttribute(s));
    }   
    

	public void regions(int size) {
		region4=new ArrayDeque<String>();
	    region3=new ArrayDeque<String>();
	    region2=new ArrayDeque<String>();
	    region1=new ArrayDeque<String>();
	    empty=new ArrayDeque<String>();
		SIZE=size;
		
    	int border=(size-1);
    	for(int i=0;i<size;i++) {
    		for(int j=0;j<size;j++) {
        		region1.add(i+":"+j);
        		empty.add(i+":"+j);
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
	
	public int regionWeights(int x, int y) {
		int score=0;
		String s=square(x,y);
		//region4: Corners [Best Priority]
		//region2: Borders [Second Best Priority]
		//region3: Buffer  [Bad Priority]
		if(region4.contains(s)) {
			score=SIZE;
			region2.add(square(x+1,y));
			region2.add(square(x,y+1));
			region2.add(square(x+1,y+1));
		}else if(region2.contains(s)) {
			score=(int)SIZE/4;
		}else if(region3.contains(s)) {
			score=-SIZE/2;
		}
		return score;
	}
	
	public void printPossibles() {
		for(String key : possibles.keySet()) {
			System.out.println(key + " " + possibles.get(key));
		}
	}
	
	public void findAllMoves(Percept p) {
		changesStates = new ArrayDeque<BoardState>();
		possibles = new HashMap<String, Integer>();
		ArrayDeque <String> toAnalize=new ArrayDeque<String>();
		toAnalize.addAll(empty);
		
		for(String s: toAnalize) {
			if(getCell(p,s).equals("space")) {
				String []ij = s.split(":");
				int i=Integer.valueOf(ij[0]);
				int j=Integer.valueOf(ij[1]);
				analizeValidMove(p, i, j);
			}else {
				empty.remove(s);
			}
		}
		
		/*for(BoardState b: changesStates) {
			b.print();
		}*/
	}
	

	public void analizeValidMove(Percept p, int x, int y) {
		int score=0;
		String tile=square(x,y);
		HashMap <String,String> totalChanges=new HashMap<String,String>();
		for(int i=-1; i<2; i++) {
			if((x+i)<0 || (x+i)>=SIZE)
				continue;
			
			for(int j=-1; j<2; j++) {
				if((y+j)<0 || (y+j)>=SIZE)
					continue;
				
				if((j==0&&i==0))
					continue;
				
				int actualX= x+i;
				int actualY= y+j;
				String n=getCell(p, actualX, actualY);
				
				if (!n.equals(RIVAL)) 
					continue;
					
				if((x+2*i)<0 || (x+2*i)>=SIZE || (y+2*j)<0 || (y+2*i)>=SIZE)
					continue;
				
				
				int tmpScore = 0;
				boolean found=true;
				HashMap <String,String> tmp=new HashMap<String,String>();
				while(n.equals(RIVAL)) {
					tmp.put(square(actualX,actualY),COLOR);
					tmpScore+=1;
					actualX+=i;
					actualY+=j;
					if((actualX)<0 || (actualX)>=SIZE || (actualY)<0 || (actualY)>=SIZE) {
						found=false;
						break;
					}
					n=getCell(p, actualX, actualY);
				}
				
				if(n.equals(COLOR) && found) {
					score+=tmpScore;
					possibles.put(tile,score);
					totalChanges.putAll(tmp);
				}
			}
		}
		
		
		if(possibles.containsKey(tile)) {
			totalChanges.put(tile, COLOR);
			score+=regionWeights(x,y);
			changesStates.push(new BoardState(tile,1,1,totalChanges,score));
			possibles.put(tile,score);
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
		public String changed;
		public int level;
		public int max; // 1=Maximiza -1=Minimiza 
		public HashMap <String, String> changedMap;
		public int score;
		public String color;
		 
		
		public BoardState(String changed, int max, int level, HashMap <String, String> changedMap, int score) {
			this.changed=changed;
			this.max=max;
			this.level=level;
			this.changedMap=new HashMap<String, String>();
			this.changedMap.putAll(changedMap);
			this.score=score;
		}
		
		public void print() {
			System.out.println("--------------------------");
			System.out.println("Tile changed: "+changed);
			System.out.println("Changed Map: "+changedMap.toString());
			System.out.println("Score: "+score);
		}
	}
}
