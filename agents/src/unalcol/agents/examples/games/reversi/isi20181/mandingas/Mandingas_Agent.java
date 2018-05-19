package unalcol.agents.examples.games.reversi.isi20181.mandingas;

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
    		System.out.println(board.findAllMoves(p).toString());
    		firstTime=false;
    	}
    	
        long time = (long)(200 * Math.random());
        try{
           Thread.sleep(time);
        }catch(Exception e){}
        if( p.getAttribute(percepts[TURN]).equals(color)){
            int i = (int)(8*Math.random());
            int j = (int)(8*Math.random());
            return new Action(board.move(i,j));
        }
        
        return new Action(PASS);
    }

    @Override
    public void init() {
    }
    
}