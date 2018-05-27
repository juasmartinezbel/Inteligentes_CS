/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unalcol.agents.examples.games.reversi;

import unalcol.agents.Agent;
import unalcol.agents.examples.games.reversi.isi20181.mandingas.*;
import unalcol.agents.examples.games.reversi.test1.*;
import unalcol.agents.examples.games.reversi.test2.*;
/**
 *
 * @author Jonatan
 */
public class ReversiMain {
  public static void main( String[] argv ){
    // Reflection
    Agent w_agent = new Agent( new Mandingas_Agent2("white") );
    Agent b_agent = new Agent( new Mandingas_Agent1("black") );
    
    ReversiMainFrame frame = new ReversiMainFrame( w_agent, b_agent );
    frame.setVisible(true);
  }
    
}
