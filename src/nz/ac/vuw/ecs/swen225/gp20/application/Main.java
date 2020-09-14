package nz.ac.vuw.ecs.swen225.gp20.application;

import javax.swing.*;

public class Main {

    //create board/gui object here
    private gameGUI board;



    public Main(){
        SwingUtilities.invokeLater(() -> board = new gameGUI());
    }
    /**
     * Starts the game
     * @param args - ignored
     */
    public static void main(String[] args){
        new Main();
    }
}
