package nz.ac.vuw.ecs.swen225.gp20.persistence;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import nz.ac.vuw.ecs.swen225.gp20.application.GameGUI;
import nz.ac.vuw.ecs.swen225.gp20.maze.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Controls the saving and loading of game states.
 *
 * @author Jared Boult 300256617
 */
public class GameStateController {

    /**
     * The running Application class
     */
    GameGUI game;

    /**
     * Constructor for the GameStateController
     *
     * @param game the running application class
     */
    public GameStateController(GameGUI game){
        this.game = game;
    }

    /**
     * Fetches the state variables and forms a State object to store them in
     *
     * @return a State object
     */
    private State createStateToSave(){
        int levelNumber = game.getLevel();
        double timeVal = game.getTime();
        Chap chap = (Chap) Maze.getInstance().getChap();
        String[] board = Maze.getInstance().toString().split("\\n");
        ArrayList<String> inventory = convertInventoryToString(Maze.getInstance().getInventory());
        int treasuresLeft = Maze.getInstance().getTreasuresLeft();
        return new State(levelNumber, timeVal, chap, board, inventory, treasuresLeft);
    }

    /**
     * Automatically saves a copy of the current game
     * state to the default location.
     *
     * @throws IOException if file cannot be written
     */
    public void saveState() throws IOException {
        State current = createStateToSave();
        Writer writer = Files.newBufferedWriter(Paths.get("levels/saved_state.json"));

        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Gson gson = builder.create();

        gson.toJson(current, writer);
        writer.flush();
        writer.close();
    }

    /**
     * Converts the List of  Tiles representing the Inventory into
     * a List of Strings for serialisation by the Gson library.
     *
     * @param inventory the inventory from Maze
     * @return a List of Strings
     */
    private ArrayList<String> convertInventoryToString(List<Tile> inventory){
        ArrayList<String> result = new ArrayList<>();
        for (Tile tile : inventory) {
            result.add(tile.getPosition().toString() + tile.code());
        }
        return result;
    }

    /**
     * Update Maze with data from the State object
     *
     * @param helpText the helpText for the level tiles
     * @param secondaries the Set of secondary actors
     */
    public void loadMazeState(String[] helpText, Set<Actor> secondaries){
        State s = createState();
        s.setMaze(helpText, secondaries);
    }

    /**
     * Update GameGUI with data from the State object
     */
    public void loadGameState(){
        State s = createState();
        s.setGame(game);
    }

    /**
     * Loads a game state from the default location.
     *
     * @return a State object
     */
    public State createState(){
        Gson gson = new Gson();
        State savedState = null;
        for(File state : detectStateFile()){
            try {
                JsonReader reader = new JsonReader(new FileReader(state, StandardCharsets.UTF_8));
                savedState = gson.fromJson(reader, State.class);
            } catch(IOException ignored) {}
        }
        return savedState;
    }

    /**
     * Get the level number of the saved game
     *
     * @return the level of the saved game
     */
    public int getLevel(){
        if(createState() != null){
            return createState().getLevelNumber();
        }
        return 0;
    }

    /**
     * Return an array containing the saved state JSON file or
     * an empty array if the saved state doesn't exist
     * @return an Array of Files that match a saved state format
     */
    private File[] detectStateFile(){
        File levelsFolder = new File("levels");
        return levelsFolder.listFiles(((dir, name) ->
                name.matches("saved_state.json")));
    }

    /**
     * Checks if a previous game state is present in the
     * default location.
     *
     * @return True if a game state is present in the states
     * directory.
     */
    public boolean previousStateFound(){
        return detectStateFile().length > 0;
    }

    /**
     * Remove any previous state files saved
     * @return true if deleted successfully or file never existed,
     * false if file could not be deleted.
     */
    public boolean deletePreviousState(){
        for(File f : detectStateFile()){
            return f.delete();
        }
        return true;
    }
}
