package unalcol.agents.examples.games.reversi.test2;

import java.util.ArrayDeque;
import java.util.HashMap;

import unalcol.agents.Percept;


public class Board2 {
	
	public HashMap <String, Integer> possibles;
	public ArrayDeque <BoardState> changesStates;
    public ArrayDeque <String> region4;
    public ArrayDeque <String> region3;
    public ArrayDeque <String> region2;
    public ArrayDeque <String> region1;
    public ArrayDeque <String> empty;
    public int CurrentScore = 2;
    private static final int LEVEL_DEPTH=3;
    private static final boolean EURISTHIC=false;
    protected String COLOR;
    protected String RIVAL;
    public int SIZE;
	public Board2 (String color, String rival) {
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
		if(!EURISTHIC)
			return 0;
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
	}
	

	public void analizeValidMove(Percept p, int x, int y) {
		int score=1;
		boolean changes=false;
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
					
				if((x+2*i)<0 || (x+2*i)>=SIZE || (y+2*j)<0 || (y+2*j)>=SIZE)
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
					changes=true;
					score+=tmpScore;
					possibles.put(tile,score);
					totalChanges.putAll(tmp);
				}
			}
		}
		
		
		if(changes) {
			totalChanges.put(tile, COLOR);
			score+=regionWeights(x,y);
			changesStates.push(new BoardState(tile,-1,1,totalChanges,score, COLOR));
			possibles.put(tile,score);
		}
	}
	
	
	public String choice(Percept p) {
		int max=Integer.MIN_VALUE;
		String best_choice ="";
		while(!changesStates.isEmpty()) {
			BoardState bs=changesStates.pop();
			int value = minimax_decision(p, bs);
			if(value > max) {
				best_choice = bs.changed;
				max=value;
			}
		}
		return best_choice;
	}
	
	public int minimax_decision(Percept p, BoardState bs) {
		ArrayDeque <String> toAnalize = empty.clone();
		toAnalize.remove(bs.changed);
		int max=Integer.MIN_VALUE;
		
		if(bs.level>=LEVEL_DEPTH) {
			return bs.score;
		}
		
		for(String s: toAnalize) {
			if(bs.getCell(p, s).equals("space")) {
				String []ij = s.split(":");
				int i=Integer.valueOf(ij[0]);
				int j=Integer.valueOf(ij[1]);
				BoardState newState=minimaxAnalizeValidMove(p, i, j, bs);
				if(newState==null)
					continue;
				int value = minimax_decision(p, newState)*bs.max;
				max = value>max ? value:max;
			}
		}
		
		if(max==Integer.MIN_VALUE)
			max=bs.score;
		
		return max*bs.max;
	}
	
	public BoardState minimaxAnalizeValidMove(Percept p, int x, int y, BoardState bs) {
		HashMap <String, String> totalChanges = (HashMap <String, String>) bs.changedMap.clone();
		int score=1;
		String tile=square(x,y);
		boolean changes=false;
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
				String n=bs.getCell(p, actualX, actualY);
				
				if (!n.equals(bs.color)) 
					continue;
					
				if((x+2*i)<0 || (x+2*i)>=SIZE || (y+2*j)<0 || (y+2*j)>=SIZE)
					continue;
				
				int tmpScore=0;
				boolean found=true;
				HashMap <String, String> tmp=new HashMap<String,String>();
				while(n.equals(bs.color)) {
					tmp.put(square(actualX,actualY),bs.rival);
					tmpScore+=1;
					actualX+=i;
					actualY+=j;
					if((actualX)<0 || (actualX)>=SIZE || (actualY)<0 || (actualY)>=SIZE) {
						found=false;
						break;
					}
					n=bs.getCell(p, actualX, actualY);
				}
				
				if(n.equals(bs.rival) && found) {
					changes=true;
					score+=tmpScore;
					totalChanges.putAll(tmp);
				}
			}
		}
		
		if(changes) {
			totalChanges.put(tile, bs.rival);
			int newMax = -bs.max;
			int nScore = bs.score + score*bs.max;
			return new BoardState(tile, newMax, 1+bs.level, totalChanges, nScore, bs.rival);
		}
		return null;
	}
	
	
	class BoardState {
		public String changed;
		public int max; // 1= Va Maximizar al nivel de abajo || -1=Va a Minimizar el nivel de abajo 
		public int level;
		public HashMap <String, String> changedMap;
		public int score;
		public String color;
		public String rival;
		 
		
		public BoardState(String changed, int max, int level, HashMap <String, String> changedMap, int score, String color) {
			this.changed=changed;
			this.max=max;
			this.level=level;
			this.changedMap= (HashMap <String, String>)changedMap.clone();
			this.score=score;
			this.color=color;
			rival=color.equals("white") ? "black":"white";
		}
		
	    public String getCell(Percept p, int x, int y) {
	    	return getCell(p, square(x,y));
	    }    
	    
	    public String getCell(Percept p, String s) {
	    	if (!changedMap.containsKey(s)) {
	    		return String.valueOf(p.getAttribute(s));
	    	}
	    	return changedMap.get(s);
	    }
	    
	    
		public void print() {
			System.out.println("--------------------------");
			System.out.println("Tile changed: "+changed);
			System.out.println("Changed Map: "+changedMap.toString());
			System.out.println("Score: "+score);
		}
	}
}
