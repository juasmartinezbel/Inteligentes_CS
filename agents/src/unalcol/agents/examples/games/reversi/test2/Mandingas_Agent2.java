package unalcol.agents.examples.games.reversi.test2;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import unalcol.agents.Action;
import unalcol.agents.AgentProgram;
import unalcol.agents.Percept;

public class Mandingas_Agent2 implements AgentProgram {
	/*
	 * Blue = White
	 * Red = Black
	 */
    private String [] percepts = {"size", "black_time", "white_time", "play"};
    
    protected static final int SIZE=0;
    protected int MY_TIME;
	protected int RIVAL_TIME;
	protected static final int TURN=3;
	protected static final String PASS="PASS";
    protected String color;
    protected String rival;
    private Board2 board;
    private boolean firstTime;
    
    public Mandingas_Agent2( String color ){
        this.color = color;
        rival = color.equals("white") ? "black":"white";
        MY_TIME = color.equals("white") ? 2:1;
        RIVAL_TIME = MY_TIME==1 ? 2:1;
        board = new Board2(color, rival);
        firstTime=true;
    }
    @Override
    public Action compute(Percept p) {
    	firstTime=board.SIZE!=Integer.valueOf((String)p.getAttribute(percepts[SIZE]));
    	
    	if(firstTime) {
    		int size=Integer.valueOf((String)p.getAttribute(percepts[SIZE]));
    		board.regions(size);
    		firstTime=false;
    	}    	
    	
        if(p.getAttribute(percepts[TURN]).equals(color)){
        	board.findAllMoves(p);
        	String choice = board.choice(p);
        	if(!choice.equals("")) {
        		return new Action(choice+":"+color);
        	}
        }
        return new Action(PASS);
    }

    @Override
    public void init() {
    	firstTime=true;
    }
    
}