package nz.ac.vuw.ecs.swen225.gp20.application;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

public class Main {

    //create board/gui object here
    private gameGUI board;
    int timeVal = 10;


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
