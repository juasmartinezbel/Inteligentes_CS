package unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.mandingas.copy;

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
	  protected int id=0;
	  protected SimpleLanguage language;
	  protected Vector<String> cmd = new Vector<String>();
	  protected Actuator actuator;
	  private boolean ate=false;
	  private int lastEnergy=0;
	  private int counter=0;
	  private boolean mhInput = false;
	  private boolean stuck = false;
	  private String lastMove ="";
	  public Mandingas_agent( ) {
	  }

	  public Mandingas_agent(   SimpleLanguage _language, int _id  ) {
		  language = _language;
		  actuator = new Actuator();
		  id=_id;
	  }
	  
	  public Mandingas_agent(   SimpleLanguage _language ) {
		  language = _language;
		  actuator = new Actuator();
	  }

	  public void setLanguage(  SimpleLanguage _language ){
	    language = _language;
	  }

	  public void init(){
	    cmd.clear();
	    actuator = new Actuator();
	    ate=false;
		lastEnergy=0;
	  }

	  
	  
	  /**
	   * Execute the required movements.
	   *
	   * @param perception Perception
	   * @return Action[]
	   */
	  public Action compute(Percept p){
		if( cmd.size() == 0 ) {
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
		      boolean AF = ( (Boolean) p.getAttribute(language.getPercept(6))).
			          booleanValue();
		      boolean AR = ( (Boolean) p.getAttribute(language.getPercept(7))).
		          booleanValue();
		      boolean AB = ( (Boolean) p.getAttribute(language.getPercept(8))).
		          booleanValue();
		      boolean AL = ( (Boolean) p.getAttribute(language.getPercept(9))).
		          booleanValue();
		      boolean FOOD = ( (Boolean) p.getAttribute(language.getPercept(10))).
		              booleanValue();
		      Integer energy = ( (Integer) p.getAttribute(language.getPercept(15)));
		      if(FAIL)
		    	  actuator.hardReset();
		      boolean isGood=false;
		      if(ate) {
		    	  isGood=lastEnergy<=energy;
		    	  if(lastEnergy==energy && !mhInput && isGood){
					  actuator.setMaxHealth(energy);
					  mhInput=true;					  
				  }
			  }
		      lastEnergy=energy;

		      //Defines the kind of task the actuator is going to make, for now is random
		      /**
		       * 0-3: Should rotate AND move
		       * 4: eat
		       * -2: wait/keep trying
		       * 0: die
		       * 
		       */
		      int d = actuator.task(PF, PR, PB, PL, MT, FAIL, AF, AR, AB, AL, FOOD, energy, isGood);
		      if (0 <= d && d < 4) {
		        directions(d); //Sets the directions
		      }else if(d == 4) {
		    	cmd.add(language.getAction(4));  //eat
		      }else if(d==-2) {
		        counter++;
		    	actuator.addNode(actuator.getSurroundings(PF, PR, PB, PL));
			    cmd.clear();
			    cmd.add(language.getAction(0));
		      }else{
		    	cmd.add(language.getAction(0)); // die
		      }
		    }
	    String x = cmd.get(0);
	    cmd.remove(0);
	    
	    //Updates the coordinates
	    if(x.equals(language.getAction(2))) {
	    	//Last second check, if a wall and/or an agent appeared, then we tell the agent to do nothing
	    	boolean AF=!((Boolean) p.getAttribute(language.getPercept(6))).booleanValue();
	    	boolean PF=!((Boolean) p.getAttribute(language.getPercept(0))).booleanValue();
	    	if(AF && PF) {
	    		actuator.changeCoordinates(false, id);
	    	}else{
	    		cmd.clear();
	    		x=language.getAction(0);
	    	}
	    }
	    ate=x.equals(language.getAction(4));
	    counter=x.equals(language.getAction(0)) ? counter++ : 0;
	    if(counter>15) {
	    	actuator.resetMap();
    		counter=0;
	    }
	    lastMove=x;
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

