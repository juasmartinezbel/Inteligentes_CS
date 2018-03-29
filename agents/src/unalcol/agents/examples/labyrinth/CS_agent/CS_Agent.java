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
	  public CS_Agent( ) {
	  }

	  public CS_Agent(   SimpleLanguage _language  ) {
		  language = _language;
		  
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
	      boolean FOOD_COLOR = ( (Boolean) p.getAttribute(language.getPercept(11))).
	              booleanValue();
	      boolean FOOD_SHAPE = ( (Boolean) p.getAttribute(language.getPercept(12))).
	              booleanValue();
	      boolean FOOD_SIZE = ( (Boolean) p.getAttribute(language.getPercept(13))).
	              booleanValue();
	      boolean FOOD_WEIGHT = ( (Boolean) p.getAttribute(language.getPercept(14))).
	              booleanValue();
	      Integer energy = ( (Integer) p.getAttribute(language.getPercept(15)));


	      int d = movement(PF, PR, PB, PL, MT, FAIL, FOOD, FOOD_COLOR, FOOD_SHAPE, FOOD_SIZE, FOOD_WEIGHT, energy);
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
	  
	  public int movement(boolean PF, boolean PR, boolean PB, boolean PL, boolean MT, boolean FAIL, boolean FOOD, boolean FOOD_COLOR, boolean FOOD_SHAPE, boolean FOOD_SIZE, boolean FOOD_WEIGHT, Integer energy) {
	        if (MT) return -1;
	        boolean flag = true;
	        int k=0;
	        while( flag ){
	            k = (int)(Math.random()*4);
	            switch(k){
	                case 0:
	                    flag = PF;
	                    break;
	                case 1:
	                    flag = PR;
	                    break;
	                case 2:
	                    flag = PB;
	                    break;
	                default:
	                    flag = PL;
	                    break;                    
	            }
	        }
	        System.out.println(FOOD + ", " + FOOD_COLOR + ", " + FOOD_SHAPE + ", " + FOOD_SIZE + ", " + FOOD_WEIGHT + ", " + energy);
	        return 4;
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
