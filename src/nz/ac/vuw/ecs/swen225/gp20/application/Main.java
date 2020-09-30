package nz.ac.vuw.ecs.swen225.gp20.application;
import javax.swing.*;

/**
 * Main class for chip's challenge. Starts the game and call for gui construction
 */
public class Main {

    //create board/gui object here
    /**
     * instance of board in gui
     */
    private GameGUI board;


    /**
     * construct main class by building gui display
     */
    public Main() {
        SwingUtilities.invokeLater(() -> board = new GameGUI());
    }
    /**
     * Starts the game
     * @param args - ignored
     */
    public static void main(String[] args){
        new Main();
    }
}
