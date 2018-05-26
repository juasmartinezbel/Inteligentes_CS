package unalcol.agents.examples.games.reversi.test2;

import java.util.ArrayDeque;
import java.util.HashMap;

import unalcol.agents.Percept;


public class Board2 {
	
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
    
    /**
     * Inicializa el tablero
     * @param color del jugador
     * @param rival del rival
     */
	public Board2 (String color, String rival) {
		COLOR = color;
		RIVAL = rival;
	}
	
	
	
	
/**************************************************************
 **************************************************************
 * 
 * Funciones de trivialización de operaciones
 *
 *************************************************************
 *************************************************************/
	
	/**
	 * Convierte las cordenadas 'x' & 'y' en un String del hash de juego
	 * @param x
	 * @param y
	 * @return
	 */
	public String square (int x, int y) {
    	return x+":"+y;
    }
	
    
	/**
	 * Me retorna el valor del cuadro con coordenadas 'i' y 'j'
	 * @param p
	 * @param i
	 * @param j
	 * @return
	 */
    public String getCell(Percept p, int i, int j) {
    	return String.valueOf(p.getAttribute(square(i,j)));
    }    
    
    
    /**
     * Me retorna el valor del cuadro con coordenadas de la cadena 's' 
     * @param p
     * @param s
     * @return
     */
    public String getCell(Percept p, String s) {
    	return String.valueOf(p.getAttribute(s));
    }   
	
    /**
     * Me separa las coordenadas de una cadena en números
     * @param s
     * @return
     */
	public int [] splitString(String s) {
		String []ij = s.split(":");
		int [] intij = { Integer.valueOf(ij[0]), Integer.valueOf(ij[1])};
		return intij;
	}
	
	
	
	
	
/**************************************************************
 **************************************************************
 * 
 * Funciones de euristicas de regiones
 *
 *************************************************************
 *************************************************************/
	
	/**
	 * Me inicializa las regiones y lista vacía
	 * @param size
	 * @param p
	 */
	public void regions(int size, Percept p) {
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
        		if(getCell(p,i,j).equals("space"))
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
	
	
	/**
	 * Me mira si se aplica la euristica de pesos según la región
	 * @param size
	 * @param p
	 */
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

	
	
	
	
	
	
	
/**************************************************************
 **************************************************************
 * 
 * Funciones de Busqueda general de siguiente paso
 *
 *************************************************************
 *************************************************************/

	
	/**
	 * Itera la lista de cuadros vacíos para buscar un potencial movimiento siguiente
	 * @param p
	 * @return
	 */
	public ArrayDeque<BoardState> findAllMoves(Percept p) {
		changesStates = new ArrayDeque<BoardState>();
		//Creamos un ArrayDeque de los vacíos
		ArrayDeque <String> localEmpty= empty.clone();
		
		
		/*Iteramos el clon de empty, si no es ficha, se remueves
		 * En teoría, será solo una ficha el cambio, la que puso el rival,
		 * Ya que el mapa se actualizará automaticamente con la decisión
		 */
		for(String s: localEmpty){
			if(getCell(p, s).equals("space")) {
				int [] ij=splitString(s);
				BoardState validMove = analizeValidMove(p, ij[0], ij[1]);
				if(validMove!=null) changesStates.add(validMove);		
			}else {
				empty.remove(s);
			}
		}
		return changesStates;
	}
	

	/**
	 * Me analiza si un espacio vacío es potencial movimiento siguiente
	 * @param p
	 * @return
	 */
	public BoardState analizeValidMove(Percept p, int x, int y) {
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
					totalChanges.putAll(tmp);
				}
			}
		}
		
		
		if(changes) {
			totalChanges.put(tile, COLOR);
			score+=regionWeights(x,y);
			return new BoardState(tile,-1,1,totalChanges,score, COLOR);			
		}
		return null;
	}

	
	/**
	 * Me analiza cual de las opciones es la más indicada.
	 * @param p
	 * @return
	 */
	public String choice(Percept p) {
		findAllMoves(p);
		int max=Integer.MIN_VALUE;
		String best_choice ="";
		while(!changesStates.isEmpty()) {
			BoardState bs=changesStates.pop();
			bs.setEmpty(empty);
			int value = minimax_decision(p, bs);
			if(value > max) {
				best_choice = bs.changed;
				max=value;
			}
		}
		return best_choice;
	}

	

	
	
	
	
	
/**************************************************************
 **************************************************************
 * 
 * Funciones de Analisis Minimax
 *
 *************************************************************
 *************************************************************/
	
	/**
	 * Analisis Minimax para la posibilidad
	 * @param p
	 * @param bs
	 * @return
	 */
	public int minimax_decision(Percept p, BoardState bs) {
		
		int max=Integer.MIN_VALUE;
		
		if(bs.level>=LEVEL_DEPTH) {
			return bs.score;
		}
		
		for(String s: bs.emptyTiles) {	
			int ij[]=splitString(s);
			BoardState newState=minimaxAnalizeValidMove(p, ij[0], ij[1], bs);
			if(newState==null) continue;
			int value = minimax_decision(p, newState)*bs.max;
			max = value>max ? value:max;
		}
		
		if(max==Integer.MIN_VALUE)
			max=bs.score;
		
		return max*bs.max;
	}
	
	/**
	 * Analisis de qué movimientos son válidos dentro de los futuros imaginarios
	 * @param p
	 * @param x
	 * @param y
	 * @param bs
	 * @return
	 */
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
			BoardState nbs = new BoardState(tile, newMax, 1+bs.level, totalChanges, nScore, bs.rival);
			nbs.setEmpty(bs.emptyTiles);
			return nbs;
		}
		return null;
	}
	


	
	
	
	
/**************************************************************
 **************************************************************
 * 
 * Estructuras de datos utilizadas
 *
 *************************************************************
 *************************************************************/
	class BoardState {
		public String changed;
		public int max; // 1= Va Maximizar al nivel de abajo || -1=Va a Minimizar el nivel de abajo 
		public int level;
		public HashMap <String, String> changedMap;
		public int score;
		public String color;
		public String rival;
		public ArrayDeque <String> emptyTiles; 
		
		public BoardState(String changed, int max, int level, HashMap <String, String> changedMap, int score, String color) {
			this.changed=changed;
			this.max=max;
			this.level=level;
			this.changedMap= (HashMap <String, String>)changedMap.clone();
			this.score=score;
			this.color=color;
			rival=color.equals("white") ? "black":"white";
		}
		
		
		// Me inicializa un mapa de los vacíos basado en el mapa padre + la ficha representante.
		public void setEmpty(ArrayDeque<String> e) {
			emptyTiles = e.clone();
			emptyTiles.remove(changed);
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
