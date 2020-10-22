package nz.ac.vuw.ecs.swen225.gp20.application;
import javax.swing.*;

/**
 * Main class for chip's challenge. Starts the game and call for gui construction
 *
 * @author Madeleine Mills 300472691
 */
public class Main {
    /**
     * Starts the game
     * @param args - ignored
     */
    public static void main(String[] args){
        SwingUtilities.invokeLater(GameGUI::new);
    }

}
