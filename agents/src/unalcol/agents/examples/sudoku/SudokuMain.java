package unalcol.agents.examples.sudoku;
import unalcol.agents.*;

import unalcol.agents.simulate.util.*;
import unalcol.clone.DefaultClone;
import unalcol.services.Service;
import unalcol.services.ServicePool;
import unalcol.types.collection.vector.VectorClone;
import unalcol.types.collection.vector.Vector;
import unalcol.agents.examples.sudoku.naive.*;

public class SudokuMain {
	public static void init_services(){
		ServicePool service = new ServicePool();
		service.register(new DefaultClone(), Object.class);      
		service.register(new VectorClone<>(), Vector.class);      
		Service.set(service);
	}
	
  private static Language getLanguage(){
    return  new SudokuLanguage();
  }

  public static void main( String[] argv ){
	  init_services();
    //    Agent agent = new Agent( new InteractiveAgentProgram( getLanguage() ) );
    Agent agent = new Agent( new NaiveSudokuAgentProgram() );
    SudokuMainFrame frame = new SudokuMainFrame( agent, getLanguage() );
    frame.setVisible(true);
  }
}
