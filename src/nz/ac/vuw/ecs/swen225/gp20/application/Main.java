package nz.ac.vuw.ecs.swen225.gp20.application;
import javax.swing.*;

/**
 * Main class for chip's challenge. Starts the game and call for gui construction
 */
public class Main {
    /**
     * Starts the game
     * @param args - ignored
     */
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> new GameGUI());
    }

}
