package unalcol.agents.examples.labyrinth.CS_agent;

import unalcol.agents.Action;
import unalcol.agents.AgentProgram;
import unalcol.agents.Percept;
import unalcol.agents.simulate.util.SimpleLanguage;
import unalcol.types.collection.vector.Vector;
/**
*
* @author Cristian Rojas y Sebastian Martinez
*/
public class CS_Agent implements AgentProgram{
	
	  protected SimpleLanguage language;
	  protected Vector<String> cmd = new Vector<String>();
	  private Actuator actuator;
	
	  public CS_Agent( ) {
	  }

	  public CS_Agent(   SimpleLanguage _language  ) {
		  language = _language;
		  actuator = new Actuator();
	  }

	  public void setLanguage(  SimpleLanguage _language ){
	    language = _language;
	  }

	  public void init(){
	    cmd.clear();
	    
	  }

	  
	  
	  /**
	   * execute
	   *
	   * @param perception Perception
	   * @return Action[]
	   */
	  public Action compute(Percept p){
	    if( cmd.size() == 0 ){

	      boolean PF = ( (Boolean) p.getAttribute(language.getPercept(0))).
	          booleanValue();
	      boolean PR = ( (Boolean) p.getAttribute(language.getPercept(1))).
	          booleanValue();
	      boolean PB = ( (Boolean) p.getAttribute(language.getPercept(2))).
	          booleanValue();
	      boolean PL = ( (Boolean) p.getAttribute(language.getPercept(3))).
	          booleanValue();
	      boolean MT = ( (Boolean) p.getAttribute(language.getPercept(4))).
	          booleanValue();
	      boolean FAIL = ( (Boolean) p.getAttribute(language.getPercept(5))).
	              booleanValue();
	      boolean FOOD = ( (Boolean) p.getAttribute(language.getPercept(10))).
	              booleanValue();
	      Integer energy = ( (Integer) p.getAttribute(language.getPercept(15)));


	      int d = actuator.movement(PF, PR, PB, PL, MT, FAIL, FOOD, energy);
	      
	      if (0 <= d && d < 4) {
	        for (int i = 1; i <= d; i++) {
	          cmd.add(language.getAction(3)); //rotate
	        }
	        cmd.add(language.getAction(2)); // advance
	        
	      }else if(d == 4) {
	    	cmd.add(language.getAction(4));  //eat
	      }else {
	        cmd.add(language.getAction(0)); // die
	      }
	    }
	    String x = cmd.get(0);
	    cmd.remove(0);
	    return new Action(x);
	  }  


	  /**
	   * goalAchieved
	   *
	   * @param perception Perception
	   * @return boolean
	   */
	  public boolean goalAchieved( Percept p ){
	    return (((Boolean)p.getAttribute(language.getPercept(4))).booleanValue());
	  }
}
