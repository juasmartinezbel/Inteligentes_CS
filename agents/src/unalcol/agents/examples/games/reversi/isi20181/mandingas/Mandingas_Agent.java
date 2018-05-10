package unalcol.agents.examples.games.reversi.isi20181.mandingas;

import unalcol.agents.Action;
import unalcol.agents.AgentProgram;
import unalcol.agents.Percept;
import unalcol.agents.examples.games.reversi.Reversi;

public class Mandingas_Agent implements AgentProgram {
	/*
	 * Azul = White
	 * Rojo = Black
	 */
    private String [] percepts = {"size", "black_time", "white_time", "play"};
    
    private static final int SIZE=0;
    protected static int MY_TIME;
	protected static int RIVAL_TIME;
    private static final int TURN=3;
    private static final String PASS="PASS";
    protected String color;
    
    public Mandingas_Agent( String color ){
        this.color = color;
        MY_TIME = color.equals("white") ? 2:1;
        RIVAL_TIME = MY_TIME==1 ? 2:1;
    }
    
    public String square (int x, int y) {
    	return x+":"+y;
    }
    
    public String move (int x, int y) {
    	return x+":"+y+":"+color;
    }
    
    @Override
    public Action compute(Percept p) { 
        long time = (long)(200 * Math.random());
        try{
           Thread.sleep(time);
        }catch(Exception e){}
        if( p.getAttribute(percepts[TURN]).equals(color) ){
            int i = (int)(8*Math.random());
            int j = (int)(8*Math.random());
            return new Action(move(i,j));
        }
        return new Action(PASS);
    }

    @Override
    public void init() {
    }
    
}