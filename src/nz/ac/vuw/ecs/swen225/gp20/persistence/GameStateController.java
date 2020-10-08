package nz.ac.vuw.ecs.swen225.gp20.persistence;

import nz.ac.vuw.ecs.swen225.gp20.application.GameGUI;

/**
 * Controls the saving and loading of game states.
 */
public class GameStateController {

    /**
     * The running Application class
     */
    GameGUI instance;

    /**
     * Constructor for the GameStateController
     *
     * @param instance the running application class
     */
    public GameStateController(GameGUI instance){
        this.instance = instance;
    }

    /**
     * Automatically saves a copy of the current game
     * state to the default location.
     */
    public void saveState(){
        // get all state attributes (starting with timeVal)
        // create State object
        // serialise it to a file
    }

    /**
     * Loads a game state from the default location.
     */
    public void loadState(){
        // deserialise state from file
        // load all attributes back into Maze and Application
    }

    /**
     * Checks if a previous game state is present in the
     * default location.
     *
     * @return True if a game state is present in the states
     * directory.
     */
    public boolean previousStateFound(){
        return false;
    }
    
}
