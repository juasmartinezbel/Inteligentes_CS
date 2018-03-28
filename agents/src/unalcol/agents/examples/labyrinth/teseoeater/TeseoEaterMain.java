package unalcol.agents.examples.labyrinth.teseoeater;
import unalcol.agents.Agent;

import unalcol.agents.examples.labyrinth.*;
import unalcol.agents.simulate.util.*;
import unalcol.agents.examples.labyrinth.teseo.simple.RandomReflexTeseo;

public class TeseoEaterMain {
  private static SimpleLanguage getLanguage(){
    return new TeseoEaterLanguage(
      new String[]{"front", "right", "back", "left", "treasure",
                   "resource", "resource-color", "resource-shape", "resource-size", "resource-weight", "resource-type", "energy_level" },
      new String[]{"no_op", "die", "advance", "rotate", "eat"}
      );
  }

  public static void main( String[] argv ){
    Agent agent = new Agent( new RandomReflexTeseo( getLanguage() ) );
    TeseoEaterMainFrame frame = new TeseoEaterMainFrame( agent, getLanguage() );
    LabyrinthDrawer.DRAW_AREA_SIZE = 600;
    LabyrinthDrawer.CELL_SIZE = 40;
    Labyrinth.DEFAULT_SIZE = 15;
    frame.setVisible(true);
  }
}
