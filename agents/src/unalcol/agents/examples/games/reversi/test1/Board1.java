package unalcol.agents.examples.games.reversi.test1;


import java.util.*;

import unalcol.agents.Percept;


public class Board1 {
	
	public ArrayDeque <BoardState> changesStates;
    public HashMap <String, Integer> regions;
    public HashMap<String, Integer> empty;
    public int [] alphaBeta;
    private static final int LEVEL_DEPTH=3;
    private static final boolean EURISTHIC=true;
    protected String COLOR;
    protected String RIVAL;
    public int SIZE;
    
    /**
     * Inicializa el tablero
     * @param color del jugador
     * @param rival del rival
     */
	public Board1 (String color, String rival) {
		COLOR = color;
		RIVAL = rival;
		alphaBeta= new int[LEVEL_DEPTH];
		
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
		regions = new HashMap <String, Integer>();
	    empty=new HashMap<String, Integer>();
		SIZE=size;
		
    	int border=(size-1);
    	for(int i=0;i<size;i++) {
    		for(int j=0;j<size;j++) {
        		if(getCell(p,i,j).equals("space"))
        			empty.put(i+":"+j, null);
        	}
    	}
    	
    	//Region 4: Corners, Best Priority
    	regions.put("0:0",4); regions.put("0:"+border,4);
    	regions.put(border+":0",4); regions.put(border+":"+border,4);
    	
    	//Region 3: Buffer, Bad Priority
    	int buffer=(border-1);
    	regions.put("1:0",3); regions.put("0:1",3); regions.put("1:1",3);
    	regions.put(buffer+":0",3); regions.put(border+":1",3); regions.put(buffer+":1",3);
    	regions.put("0:"+buffer,3); regions.put("1:"+buffer,3); regions.put("1:"+border,3);
    	regions.put(border+":"+buffer,3); regions.put(buffer+":"+buffer,3); regions.put(buffer+":"+border,3);
    	
    	//Region 2: Edges, Good Priority
    	int edges=size-4;
    	
    	for(int i=0;i<4;i++) {
    		for(int j=2;j<(2+edges);j++) {
    			switch(i) {
    				case 0:
    					regions.put("0:"+j,2);
    					break;
    				case 1:
    					regions.put(j+":0",2);
    					break;
    				case 2:
    					regions.put(border+":"+j,2);
    					break;
    				case 3:
    					regions.put(j+":"+border,2);
    					break;
    			}
    		}
    	}
    }
	
	
	/**
	 * Me mira si se aplica la euristica de pesos según la región
	 * @param size
	 * @param p
	 */
	public int regionWeights(int x, int y) {
		int score=0;
		String s=square(x,y);
		Integer r = regions.get(s);
		if(!EURISTHIC) return 0;
		
		if (r==null) { score=0;}
		
	
		//region4: Corners [Best Priority]
		//region2: Borders [Second Best Priority]
		//region3: Buffer  [Bad Priority]
		else if(r==4) {
			score=SIZE;
			regions.put(square(x+1,y),2);
			regions.put(square(x,y+1),2);
			regions.put(square(x+1,y+1),2);
		}else if(r==2) {
			score=(int)SIZE/3;
		}else if(r==3) {
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
		HashMap<String, Integer> localEmpty= (HashMap<String, Integer>) empty.clone();
		
		
		/*Iteramos el clon de empty, si no es ficha, se remueves
		 * En teoría, será solo una ficha el cambio, la que puso el rival,
		 * Ya que el mapa se actualizará automaticamente con la decisión
		 */
		for(String s: localEmpty.keySet()){
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
		alphaBeta[bs.level]=Integer.MIN_VALUE;
				
		for(String s: bs.emptyTiles.keySet()) {	
			int ij[]=splitString(s);
			BoardState newState=minimaxAnalizeValidMove(p, ij[0], ij[1], bs);
			if(newState==null) continue;
			int value = minimax_decision(p, newState)*bs.max;
			if(!alpha_beta_analisis(value, bs)) return value; //Si no cumple, retorna
		}
		
		if(max==Integer.MIN_VALUE)
			max=bs.score;
		
		//Busca el nuevo Alpha-Beta para esa sección del sub-Arbol
		alphaBeta[bs.level-1] = max>alphaBeta[bs.level-1] ? max:alphaBeta[bs.level-1];
		
		max=max*bs.max;
		
		return max;
	}
	
	/**
	 * Defines, based in alpha-beta, if the analisis of the subtree should continue
	 * @param value
	 * @param bs
	 * @return
	 */
	public boolean alpha_beta_analisis(int value, BoardState bs) {
		
		if(bs.level==1)
			return true;
		
		
		int ab=alphaBeta[bs.level-1];
		
		/*
		 * En el caso de un nivel de maximizar:
		 * ab = 12
		 * v  = 9
		 * 
		 * Continua, ya que pueden haber números menores a 9 o mayores a 9.
		 * Si hay números menores, da igual, 9 ganará la maximización
		 * Si hay números mayores o iguales a ab, puede que esté entre 9-12 o sea mayor y se detenga la busqueda.  
		 * 
		 * 
		 * ab = 12
		 * v = 14
		 * 
		 * Se detiene, porque da igual que salga después, no se eligirá algo menor a 14.
		 * Y la minimización  del nivel superior va a elegir a 12
		 * 
		 * 
		 * 
		 * ----------------------
		 * 
		 * En el caso de un nivel de minimizar
		 * ab = 3
		 * v = 14
		 *
		 * Continua, ya que pueden haber números menores a 14 o mayores a 14.
		 * Si hay números mayores, da igual, 14 ganará la minimización
		 * Si hay números menores o iguales a ab, puede que esté entre 3-14 o sea mayor y se detenga la busqueda.
		 * 
		 *   
		 * ab = 3
		 * v = 2
		 * 
		 * Se detiene, porque da igual que salga después, no se eligirá algo mayor a 2.
		 * Y la maximización del nivel superior va a elegir a 3
		 *    
		 * 
		 */
		if(ab==Integer.MIN_VALUE)
			return true;
		
		if(value>=ab)
			return false;
		return true;
		
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
		public HashMap<String, Integer> emptyTiles; 
		
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
		public void setEmpty(HashMap<String, Integer> e) {
			emptyTiles = (HashMap<String, Integer>) e.clone();
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
