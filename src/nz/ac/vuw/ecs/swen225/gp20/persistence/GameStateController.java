package nz.ac.vuw.ecs.swen225.gp20.persistence;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import nz.ac.vuw.ecs.swen225.gp20.application.GameGUI;
import nz.ac.vuw.ecs.swen225.gp20.maze.*;
import nz.ac.vuw.ecs.swen225.gp20.recnplay.Recorder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Controls the saving and loading of game states.
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
     *
     * @return
     */
    private State createStateToSave(){
        int levelNumber = game.getLevel();
        double timeVal = game.getTime();
        Recorder recorder = game.getRecorder();
        Chap chap = (Chap) Maze.getInstance().getChap();
        String[][] board = convertBoardToString(Maze.getInstance().getBoard());
        ArrayList<String> inventory = convertInventoryToString(Maze.getInstance().getInventory());
        int treasuresLeft = Maze.getInstance().getTreasuresLeft();
        return new State(levelNumber, timeVal, chap, board, inventory, treasuresLeft);
    }

    /**
     * Automatically saves a copy of the current game
     * state to the default location.
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
     * @param board
     * @return
     */
    private String[][] convertBoardToString(Tile[][] board){
        String[][] result = new String[board.length][board[0].length];
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                Tile tile = board[i][j];
                if(tile == null){
                    result[i][j] = null;
                } else {
                    result[i][j] = tile.getPosition().toString() + tile.code();
                }
            }
        }
        return result;
    }

    /**
     * @param inventory
     * @return
     */
    private ArrayList<String> convertInventoryToString(List<Tile> inventory){
        ArrayList<String> result = new ArrayList<>();
        for(int i = 0; i < inventory.size(); i++){
            Tile tile = inventory.get(i);
            result.add(tile.getPosition().toString() + tile.code());
        }
        return result;
    }

    /**
     * Loads a game state from the default location.
     */
    public void loadState(){
        Gson gson = new Gson();
        State savedState = null;
        for(File state : detectStateFile()){
            try {
                JsonReader reader = new JsonReader(new FileReader(state, StandardCharsets.UTF_8));
                savedState = gson.fromJson(reader, State.class);
            } catch(IOException ignored) {}
        }
        if(savedState != null){
            savedState.setMaze();
            savedState.setGame(game);
        }
    }

    /**
     * @return
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
     * @return
     */
    public boolean deletePreviousState(){
        for(File f : detectStateFile()){
            return f.delete();
        }
        return false;
    }
}
