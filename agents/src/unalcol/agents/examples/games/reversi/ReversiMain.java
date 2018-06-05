/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unalcol.agents.examples.games.reversi;

import unalcol.agents.Agent;

/**
 *
 * @author Jonatan
 */
public class ReversiMain {
  public static void main( String[] argv ){
    // Reflection
    Agent w_agent = new Agent( new DummyReversiAgentProgram(Reversi.WHITE) );
    Agent b_agent = new Agent( new DummyReversiAgentProgram(Reversi.BLACK) );
    ReversiMainFrame frame = new ReversiMainFrame( w_agent, b_agent );
    frame.setVisible(true);
  }
    
}
