package unalcol.agents.examples.labyrinth.Mandingas_agent;

import unalcol.agents.Action;
import unalcol.agents.AgentProgram;
import unalcol.agents.Percept;
import unalcol.agents.simulate.util.SimpleLanguage;
import unalcol.types.collection.vector.Vector;
/**
*
* @author Cristian Rojas y Sebastian Martinez
*/
public class Mandingas_agent implements AgentProgram{
	
	  protected SimpleLanguage language;
	  protected Vector<String> cmd = new Vector<String>();
	  private Actuator actuator;
	
	  public Mandingas_agent( ) {
	  }

	  public Mandingas_agent(   SimpleLanguage _language  ) {
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
	   * Execute the required movements.
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

	      //Defines the kind of task the actuator is going to make, for now is random
	      int d = actuator.task(PF, PR, PB, PL, MT, FAIL, FOOD, energy);
	      
	      if (0 <= d && d < 4) {
	        directions(d); //Sets the directions
	      }else if(d == 4) {
	    	cmd.add(language.getAction(4));  //eat
	      }else {
	        cmd.add(language.getAction(0)); // die
	      }
	    }
	    String x = cmd.get(0);
	    cmd.remove(0);
	    
	    //Updates the coordinates
	    if(x.equals(language.getAction(2))) { 
	       actuator.changeCoordinates(true); 
	    } 
	    return new Action(x);
	  }  
	  
	  
	  /**
	   * Rotates the agent as the direction that is indicated
	   * @param d
	   */
	  public void directions(int d) {
		  int orientation=actuator.getOrientation();
		  actuator.changeOrientation(d);
		  
		  if(d!=orientation) {
			  int change = d==0 ? 4 : d;
			  change= change > orientation ? change : 4+change;
			  for (int i = orientation; i < change; i++) {
		          cmd.add(language.getAction(3));
		      }
		  }
		  cmd.add(language.getAction(2));
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
