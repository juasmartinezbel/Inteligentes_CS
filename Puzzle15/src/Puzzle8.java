import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;



public class Puzzle8 {
	
	public static class PuzzleSearch{
		public static int xTile;
		public static int yTile;
		public static int[][] In_puzzle= new int[3][3];
		public static int[][] End_puzzle = {
				{1, 2, 3},
				{4, 5, 6},
				{7, 8, 0}
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
		public static class State{
			public int[][] map;
			public int i;
			public int j;
			public char last;
			public Queue<int[][]> path;
			
			public State(int[][] _map, int _i, int _j, char lastIn, Queue<int[][]> _path) {
				// TODO Auto-generated constructor stub
				map = _map;
				i = _i;
				j = _j;
				last = lastIn;
				path = _path;
			}
		}
		
		/**
		 * 
		 * Initializes the problem.
		 * The problem can be: 
		 * 			-A personal set
		 * 			-A random generated one [Uncomment line 63]
		 * 
		 */
		public PuzzleSearch() {
			int [][] my_in_Puzzle={
					{7, 2, 4},
					{5, 0, 6},
					{8, 3, 1}
					
			};
			//my_in_Puzzle=randomize();
			In_puzzle=my_in_Puzzle;
			getTiles();
			
		}

				

		/**
		 * Breadth First Search Tree for the puzzle
		 */
		public void BFS(){
			 Queue<int[][]> path = new  LinkedList<int[][]>(); 
			 path.add(In_puzzle);
			
			 Queue<State> queue = new  LinkedList<State>(); 
			 State state = new State(In_puzzle, xTile, yTile, ' ', path);
			 queue.add(state);
			 
			 Hashtable<String, State> visited = new Hashtable<String, State>();
			 String last = "";
			 State newState;
			 int maxQueue = 0;
			 int[][] newMap;
			 String id="0";
			 
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
				 if(state.i+1 < 3 && state.last != 'D'){		
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
				 if(state.j + 1 < 3 && state.last != 'R'){
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
				 print(state.map);
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
				 if(state.i + 1 < 3 && state.last != 'D'){
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
				 if(state.j + 1 < 3 && state.last != 'R'){
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
		 * Makes a copy of the current map
		 * @param state is the map that i'll be copied square by square
		 * @return A copy of the current map
		 */
		public static int[][] copyState(int[][] state){
			int[][] newState = new int[3][3];
			
			for(int i = 0; i < 3; i++){
				for(int j = 0; j < 3; j++){
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
			for(int i = 0; i < 3; i++){
				for(int j = 0; j < 3; j++){
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
			for(int i = 0; i < 3; i++){
				for(int j = 0; j < 3; j++){
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
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
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
			int [][] endArray=new int[3][3];
			Random rand=new Random();
			LinkedList <Integer> use = new LinkedList<Integer>();
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					  int randomNum = rand.nextInt((8 - 0) + 1) ;
					  while (use.contains(randomNum)) {
						  randomNum = rand.nextInt((8 - 0) + 1) ;
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
		puzzle.DFS(10);

	}

}
