package unalcol.agents.examples.labyrinth.multeseo.eater;

import unalcol.agents.Agent;
import unalcol.agents.AgentProgram;
import unalcol.agents.examples.labyrinth.Labyrinth;
//import unalcol.agents.examples.labyrinth.Mandingas_agent.*;
import unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.mandingas.copy.*;
import unalcol.agents.examples.labyrinth.multeseo.eater.test.mandingas1.*;
import unalcol.agents.examples.labyrinth.multeseo.eater.test.mandingas2.*;
import unalcol.agents.examples.labyrinth.LabyrinthDrawer;
import unalcol.agents.simulate.util.SimpleLanguage;
import unalcol.types.collection.vector.Vector;



public class MultiTeseoEaterMain {
    private static SimpleLanguage getLanguage(){
	    return  new SimpleLanguage( new String[]{"front", "right", "back", "left", "treasure", "fail",
	        "afront", "aright", "aback", "aleft", "resource", "resource-color", "resource-shape", "resource-size", "resource-weight", "energy_level"},
	                                   new String[]{"no_op", "die", "advance", "rotate", "eat"}
	                                   );
	  }

	  public static void main( String[] argv ){
	    AgentProgram[] teseo = new AgentProgram[12];
	    int index1 = 0;
	    int index2 = 1;
	   
	    teseo[index1] = new Mandingas_agent2( getLanguage());
	    teseo[index2] = new Mandingas_agent( getLanguage());
	    
	    LabyrinthDrawer.DRAW_AREA_SIZE = 600;
	    LabyrinthDrawer.CELL_SIZE = 40;
	    Labyrinth.DEFAULT_SIZE = 15;
	    
	    Agent agent1 = new Agent(teseo[index1]);    
	    Agent agent2 = new Agent(teseo[index2]);
	    
	    //Agent agent3 = new Agent(p3);
	    Vector<Agent> agent = new Vector<Agent>();
	    agent.add(agent1);
	    agent.add(agent2);
//	    Agent agent = new Agent( new RandomReflexTeseoAgentProgram( getLanguage() ) );
	    MultiTeseoEaterMainFrame frame = new MultiTeseoEaterMainFrame( agent, getLanguage() );
	    frame.setVisible(true); 
	  }
}
