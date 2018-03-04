import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;



public class Puzzle15 {
	
	public static class PuzzleSearch{
		public static int xTile;
		public static int yTile;
		public static int[][] In_puzzle= new int[4][4];
		
		
		public static int[][] End_puzzle = {
				{1, 2, 3, 4},
				{5, 6, 7, 8},
				{9, 10, 11, 12},
				{13, 14, 15, 0}
				/*{0, 1, 2, 3},
  				{4, 5, 6, 7},
  				{8, 9, 10, 11},
  				{12, 13, 14, 15}*/
		};

		
		/**
		 * 
		 * @params map: Map representation of the current space in the puzzle
		 * @params i: X coordinate of the current 0/empty tile.
		 * @params j: Y coordinate of the current 0/empty tile.
		 * @params last: Direction of the last movement
		 * @params path: The current path the puzzle has taken.
		 * 
		 */
		public static class State {
			public int[][] map;
			public int i;
			public int j;
			public char last;
			public Queue<int[][]> path;
			public int f = Integer.MAX_VALUE;
			public State(int[][] _map, int _i, int _j, char lastIn, Queue<int[][]> _path) {
				// TODO Auto-generated constructor stub
				map = _map;
				i = _i;
				j = _j;
				last = lastIn;
				path = _path;
			}
			
		}
		
		public class StateComparer implements Comparator<State> {

		    @Override
		    public int compare(State x, State y) {
		        if (x.f < y.f) {
		            return -1;
		        }
		        if (x.f > y.f) {
		            return 1;
		        }
		        return 0;
		    }
		}
		
		/**
		 * 
		 * Initializes the problem.
		 * 			-A personal set
		 * 			-A random generated one [Uncomment line 64]
		 * 
		 */
		public PuzzleSearch() {
			int [][] my_in_Puzzle={
					{5, 2, 0, 3},
					{6, 1, 7, 4},
					{9, 13, 11, 8},
					{ 14, 10, 15, 12}
					/*{1, 2, 6, 3},
					{4, 9, 5, 7},
					{8, 13, 11, 15},
					{ 12, 14, 0, 10}*/
					
					
			};
			//my_in_Puzzle=randomize();
			In_puzzle=my_in_Puzzle;
			getTiles();
			
		}

/*****************************************************
 * 
 * 
 * Search Methods
 * 
 * 		map
 ******************************************************/

		/**
		 * Breadth First Search Tree for the puzzle
		 */
		public void BFS(){
			 Queue<int[][]> path = new  LinkedList<int[][]>(); 
			 path.add(In_puzzle);
			
			 Queue<State> queue = new  LinkedList<State>(); 
			 State state = new State(In_puzzle, xTile, yTile, ' ', path);
			 queue.add(state);
			 
			 String last = "";
			 State newState;
			 int maxQueue = 0;
			 int[][] newMap;
			 
			 while(!queue.isEmpty()){	
				 										//If true	 if false
				 maxQueue = queue.size() > maxQueue ? queue.size() : maxQueue;
				 state = queue.poll();
				 
				 if(equals(state.map, End_puzzle)){
					 //printPath(path);
					 //print(state.map);
					 System.out.println("BFS: "  +
							 			"Steps: " + (state.path.size() - 1) + " " +
							 			"MaxQueue: " + maxQueue);
					 break;
				 }
				 
				 //Down
				 if(state.i+1 < 4 && state.last != 'D'){		
					 newMap = swap(state.map, state.i, state.j, state.i + 1, state.j);
					 newState = new State(newMap, state.i + 1, state.j, 'U', new LinkedList<int[][]>(state.path));	
					 newState.path.add(newMap);
					 queue.add(newState);
				 }
			
				 //Left
				 if(state.j-1 >= 0 && state.last != 'L'){	
					 newMap = swap(state.map, state.i, state.j, state.i, state.j - 1);
					 newState = new State(newMap, state.i, state.j - 1, 'R', new LinkedList<int[][]>(state.path));
					 newState.path.add(newMap);
					 queue.add(newState);
				 }
			
				 //Right
				 if(state.j + 1 < 4 && state.last != 'R'){
					 newMap = swap(state.map, state.i, state.j, state.i, state.j + 1);
					 newState = new State(newMap, state.i, state.j + 1, 'L', new LinkedList<int[][]>(state.path));
					 newState.path.add(newMap);
					 queue.add(newState);
				 }
				 				 
				 //Up
				 if(state.i - 1 >= 0 && state.last != 'U'){
					 newMap = swap(state.map, state.i, state.j, state.i - 1, state.j);
					 newState = new State(newMap, state.i - 1, state.j, 'D', new LinkedList<int[][]>(state.path));
					 newState.path.add(newMap);
					 queue.add(newState);
				 }
			 }
		}
	
		
		/**
		 * 
		 * Depth First Search
		 * 
		 */
			
		public boolean DFS(int n){
			 Queue<int[][]> path = new  LinkedList<int[][]>(); 
			 path.add(In_puzzle);
			
			 Stack<State> stack = new  Stack<State>(); 
			 State state = new State(In_puzzle, xTile, yTile, ' ', path);
			 stack.add(state);
			 
			 
			 String last = " ";
			 State newState;
			 int[][] newMap;
			 int maxStack = 0;
			 boolean finish = false;
			 
			 while(!stack.isEmpty()){

				 maxStack = stack.size() > maxStack ? stack.size() : maxStack;
			
				 state = stack.pop();
				 
				 finish = equals(state.map, End_puzzle);				 
				 if(finish){
					 System.out.println("DFS: " + 
					 			"Steps: " + (state.path.size() - 1) + " " +
					 			"MaxQueue: " + maxStack);
					 return true;
				 }
				 
				 if(state.path.size() > n){
					 continue;
				 }
				 
				 //Down
				 if(state.i + 1 < 4 && state.last != 'D'){
					 newMap = swap(state.map, state.i, state.j, state.i + 1, state.j);
					 newState = new State(newMap, state.i + 1, state.j, 'U', new LinkedList<int[][]>(state.path));
					 newState.path.add(newMap);
					 stack.add(newState);					 

				 }
				 
				 //Left
				 if(state.j - 1 >= 0 && state.last != 'L'){	
					 newMap = swap(state.map, state.i, state.j, state.i, state.j - 1);
					 newState = new State(newMap, state.i, state.j - 1, 'R', new LinkedList<int[][]>(state.path));
					 newState.path.add(newMap);
					 stack.add(newState);
				 }
				 
				 //Right
				 if(state.j + 1 < 4 && state.last != 'R'){
					 newMap = swap(state.map, state.i, state.j, state.i, state.j + 1);
					 newState = new State(newMap, state.i, state.j + 1, 'L', new LinkedList<int[][]>(state.path));
					 newState.path.add(newMap);
					 stack.add(newState);
				 }
				 
				 //Up
				 if(state.i - 1 >= 0 && state.last != 'U'){
					 newMap = swap(state.map, state.i, state.j, state.i - 1, state.j);
					 newState = new State(newMap, state.i - 1, state.j, 'D', new LinkedList<int[][]>(state.path));
					 newState.path.add(newMap);
					 stack.add(newState);
				 }				 
			 }
			 return false;
		}
		
		/**
		 * Iterative Depth First Search
		 */
		public void iDFS(){
			int i = 0;
			System.out.print("Iterative ");
			while(!DFS(i)){
				i++;
			}
		}
		
		
		/**
		 * 
		 * A* Search
		 * 
		 */
		public boolean AStar() {
			boolean h1;
			int heuristic;
			int heu1=heuristic_1(In_puzzle);
			int heu2=heuristic_2(In_puzzle);
			if (heu1<=heu2) {
				h1=false;
				heuristic=heu2;
			}else {
				h1=true;
				heuristic=heu1;
			}
			Queue<int[][]> path = new  LinkedList<int[][]>(); 
			path.add(In_puzzle);
			
			StateComparer comparer = new StateComparer();
			Queue<State> queue = new  PriorityQueue<State>(10, comparer); 
			
			State state = new State(In_puzzle, xTile, yTile, ' ', path);
			queue.add(state);
			
			String startid=mapId(In_puzzle);
			Hashtable<String, Integer> f = new Hashtable<String, Integer>();
			f.put(startid, heuristic);
			state.f=0;
			
			Hashtable<String, Integer> g = new Hashtable<String, Integer>();
			g.put(startid, 0);
			LinkedList<String> visited = new LinkedList<String>();
			
			
			String last = " ";
			State newState;
			int[][] newMap;
			int c = 0;
			int maxQueue=0;
			boolean finish = false;
			
			while(!queue.isEmpty()){
				maxQueue = queue.size() > maxQueue ? queue.size() : maxQueue;
				state = queue.remove();
				String currentMap=mapId((state.map));
				
				visited.add(currentMap);
				finish = equals(state.map, End_puzzle);
				
				if(finish){
					System.out.println(
								"A*: " + 
					 			"Steps: " + (state.path.size() - 1) + " " +
					 			"MaxQueue: " + maxQueue);
					return true;
				}
				
				LinkedList<State>neighbors= new LinkedList<State>();
				//Down
				 if(state.i+1 < 4 && state.last != 'D'){			 
					 newMap = swap(state.map, state.i, state.j, state.i + 1, state.j);
					 newState = new State(newMap, state.i + 1, state.j, 'U', new LinkedList<int[][]>(state.path));	
					 newState.path.add(newMap);
					 neighbors.add(newState);
				 }
			
				 //Left
				 if(state.j-1 >= 0 && state.last != 'L'){	
					 newMap = swap(state.map, state.i, state.j, state.i, state.j - 1);
					 newState = new State(newMap, state.i, state.j - 1, 'R', new LinkedList<int[][]>(state.path));
					 newState.path.add(newMap);
					 neighbors.add(newState);
				}
			
				 //Right
				 if(state.j + 1 < 4 && state.last != 'R'){
					 newMap = swap(state.map, state.i, state.j, state.i, state.j + 1);
					 newState = new State(newMap, state.i, state.j + 1, 'L', new LinkedList<int[][]>(state.path));
					 newState.path.add(newMap);
					 neighbors.add(newState);
					 
				 }
				 				 
				 //Up
				 if(state.i - 1 >= 0 && state.last != 'U'){
					 newMap = swap(state.map, state.i, state.j, state.i - 1, state.j);
					 newState = new State(newMap, state.i - 1, state.j, 'D', new LinkedList<int[][]>(state.path));
					 newState.path.add(newMap);
					 neighbors.add(newState);
				 }
				 String stateId="";
				 
				 for (State s : neighbors) {
					 int[][] neigMap=s.map;
					 stateId=mapId(neigMap);
					 if(visited.contains(stateId)) {
						 continue;
					 }
					 //print(s.map);
					 
					
					 boolean add=false;
					 for (State is : queue) {
						if (mapId(is.map).equals(stateId)) {
							add=true;
							break;
						}
					 }
					 if(!add) {
						 queue.add(s);
					 }
					 
					 int tentative_g = g.get(currentMap)+1;
					 if(g.containsKey(stateId)) {
						 if(g.get(stateId)<=tentative_g) continue;
					 }
					 g.put(stateId, tentative_g);
					 
					 
					 int fPoints=g.get(stateId)+heur(h1, s.map);
					 f.put(stateId, fPoints);
					 s.f=fPoints;
					 queue.add(s);
				}
				
			}
			System.out.println("FRACASO");
			return false;
		}

/*******************************************************
 * 
 * 
 * 
 * Heuristics
 * 
 * 
 ******************************************************/
		/**
		 * 
		 * This heuristic returns the number of misplaced tiles
		 * 
		 * @param state
		 */
		public static int heuristic_1(int[][] state) {
			int misplaced=-1; //It will count the empty tile, so we discount it at once
			for(int i = 0; i < 4; i++){
				for(int j = 0; j < 4; j++){
					if(state[i][j]!=End_puzzle[i][j]){
						misplaced++;
					}
				}
			}
			return misplaced;
		}
		
		/**
		 * 
		 * This heuristic returns the Manhattan distance
		 * (i.e., no. of squares from desired location of each tile)
		 * 
		 * @param state
		 */
		public static int heuristic_2(int[][]state) {
			int manhattan=0;
			
			for(int i = 0; i < 4; i++){
				for(int j = 0; j < 4; j++){
					int tile=state[i][j];
					if(tile!=End_puzzle[i][j]){
						if(tile!=0){
							int yDistance= Math.abs(i-(tile-1)/4);
							int xDistance= Math.abs(j-(tile-1)%4);
							manhattan+=(yDistance+xDistance);
						}
					}
				}
			}
			
			
			return manhattan;
		}
		
		/**
		 * 
		 * Returns the value of the respective heuristic
		 * 
		 */
		public static int heur(boolean h1, int[][] state) {
			if (h1) {
				return heuristic_1(state);
			}else {
				return heuristic_2(state);
			}
		}
		
/*****************************************************
 * 
 * 
 * 
 * Other Functions
 * 
 * 
 ******************************************************/
		
		/**
		 * Returns a String that represents an unique set of a hash
		 * @param state
		 * @return
		 */
		public String mapId(int[][] state){
			String hashValue = "";
			String tmp ="";
			for(int i = 0; i < 4; i++){
				for(int j = 0; j < 4; j++){
					Integer tmps = state[i][j];
					tmp = tmps.toString();
					if(tmp.length()==1)
						tmp="0"+tmp;
					hashValue += tmp;
				}
			}
			return hashValue;
		}
		
		/**
		 * Makes a copy of the current map
		 * @param state is the map that i'll be copied square by square
		 * @return A copy of the current map
		 */
		public static int[][] copyState(int[][] state){
			int[][] newState = new int[4][4];
			
			for(int i = 0; i < 4; i++){
				for(int j = 0; j < 4; j++){
					newState[i][j] = state[i][j];
				}
			}
			return newState;
		}
		
		/**
		 * Creates a new state where the tiles have changed
		 * @param state map of the current map
		 * @param i1 x coordinate of the first number
		 * @param j1 y coordinate of the first number
		 * @param i2 x coordinate of the second number
		 * @param j2 y coordinate of the second number
		 * @return A new state
		 */
		public int[][] swap(int[][] state, int i1, int j1, int i2, int j2){
			int[][] newState = copyState(state);
			newState[i2][j2] = state[i1][j1];
			newState[i1][j1] = state[i2][j2]; 
			return newState;
		}
		
		/**
		 *  Compares two maps
		 * @param A
		 * @param B
		 * @return
		 */
		public boolean equals (int[][] A, int[][] B){
			for(int i = 0; i < 4; i++){
				for(int j = 0; j < 4; j++){
					if(A[i][j] != B[i][j]){
						return false;
					}
				}
			}
			return true;
		}
		
		/**
		 * Prints the path
		 * @param state
		 */
		public void printPath(Queue<int[][]> state){
			for(int[][] step : state){
				 print(step);
			 }
		}
		 

		/**
		 * Prints the current map
		 * @param State
		 */
		public static void print (int[][] State){
			for(int i = 0; i < 4; i++){
				for(int j = 0; j < 4; j++){
					System.out.print(State[i][j] + " ");
				}
				System.out.println();
			}
			System.out.println();
		}
		
		
		/**
		 * Gets the coordenates for where the tile 0 is.
		 */
		public static void getTiles() {
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					if(In_puzzle[i][j]==0) {
						xTile=i;
						yTile=j;
						break;
					}
				}
			}
		}
		
		
		/**
		 * A function that returns a randomly generated 4x4 puzzle
		 * @return a randomly generated matrix
		 */
		public static int[][] randomize(){
			int [][] endArray=new int[4][4];
			Random rand=new Random();
			LinkedList <Integer> use = new LinkedList<Integer>();
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					  int randomNum = rand.nextInt((15 - 0) + 1) ;
					  while (use.contains(randomNum)) {
						  randomNum = rand.nextInt((15 - 0) + 1) ;
					  }
					  use.add(randomNum);
					  endArray[i][j]=randomNum;
						  
				}
			}
			print(endArray);
			return endArray; 
		}
		
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PuzzleSearch puzzle = new PuzzleSearch();
		puzzle.BFS();
		puzzle.DFS(20);
		puzzle.iDFS();
		puzzle.AStar();
		
	}

}
