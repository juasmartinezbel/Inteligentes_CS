package unalcol.agents.examples.games.reversi.isi20181.mandingas;

import java.util.HashMap;
import java.util.LinkedList;

import unalcol.agents.Action;
import unalcol.agents.AgentProgram;
import unalcol.agents.Percept;

public class Mandingas_Agent implements AgentProgram {
	/*
	 * Blue = White
	 * Red = Black
	 */
    private String [] percepts = {"size", "black_time", "white_time", "play"};
    
    protected static final int SIZE=0;
    protected static int MY_TIME;
	protected static int RIVAL_TIME;
	protected static final int TURN=3;
	protected static final String PASS="PASS";
    protected String color;
    protected String rival;
    private Board board;
    private static boolean firstTime=true;
    
    public Mandingas_Agent( String color ){
        this.color = color;
        rival = color.equals("white") ? "black":"white";
        MY_TIME = color.equals("white") ? 2:1;
        RIVAL_TIME = MY_TIME==1 ? 2:1;
        board = new Board(color, rival);
    }
    
    @Override
    public Action compute(Percept p) { 
    	if(firstTime) {
    		int size=Integer.valueOf((String)p.getAttribute(percepts[SIZE]));
    		board.regions(size);
    		firstTime=false;    		
    	}    	

        if(p.getAttribute(percepts[TURN]).equals(color)){
        	board.findAllMoves(p);
        	HashMap <String, Integer> possibles = board.possibles;
        	
    		if(possibles.size()>0) {
    			int r = (int)(possibles.size()*Math.random());
    			LinkedList<String> keys = new LinkedList<>(possibles.keySet());
    			return new Action(keys.get(r) + ":" + color);
    		}else {    		
    			return new Action(PASS);
    		}
        }
        return new Action(PASS);
    }

    @Override
    public void init() {
    }
    
}