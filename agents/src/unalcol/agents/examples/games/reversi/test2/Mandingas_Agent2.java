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
    protected String play; //play
    protected int depth = 0;
    
    public Mandingas_Agent2( String color ){
        this.color = color;
        rival = color.equals("white") ? "black":"white";
        MY_TIME = color.equals("white") ? 2:1;
        RIVAL_TIME = MY_TIME==1 ? 2:1;
        board = new Board2(color, rival);
        firstTime=true;
        play = null;
    }
    @Override
    public Action compute(Percept p) {
    	firstTime=board.SIZE!=Integer.valueOf((String)p.getAttribute(percepts[SIZE]));
    	
    	if(firstTime) {
    		int size=Integer.valueOf((String)p.getAttribute(percepts[SIZE]));    		
    		board.regions(size, p);
    		board.startBoard(p);
    		firstTime=false;  		
    	}    	
    	
    	if(p.getAttribute(percepts[TURN]).equals(rival)) {
    		board.explore(p);
    	}
    	if(p.getAttribute(percepts[TURN]).equals(color)) {
        	while(board.setPlay(p)) {
//        		System.out.println(depth++);
        	}

        	String choice = board.play(p);        	

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