package unalcol.agents.examples.labyrinth.Mandingas_agent;

import unalcol.agents.simulate.util.SimpleLanguage;

public class Mandingas_agent2 extends Mandingas_agent{
	
	  public Mandingas_agent2(   SimpleLanguage _language, int _id  ) {
		  super.language = _language;
		  super.actuator = new Actuator();
		  super.id=_id;
	  }
	  
	  public Mandingas_agent2(   SimpleLanguage _language) {
		  super.language = _language;
		  super.actuator = new Actuator();
	  }
}
