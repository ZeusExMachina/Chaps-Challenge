package nz.ac.vuw.ecs.swen225.gp20.persistence;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 *  A class responsible for loading level data from json files.
 */
public class LevelLoader {

    /**
     * Stores the JSON files that hold level layout data
     */
    private final File[] levelFiles;

    /**
     * Store the Level objects created from the JSON files
     */
    private final TreeMap<Integer, Level> levels;

    /**
     * Constructor for LevelLoader.
     */
    public LevelLoader(){
        levelFiles = detectLevelFiles();
        levels = createLevels();
    }

    /**
     * Get JSON files from the levels folder.
     *
     * @return An Array of files that store the game levels
     */
    private File[] detectLevelFiles(){
        File levelsFolder = new File("levels");
        return levelsFolder.listFiles(((dir, name) ->
                name.matches("^level[0-9]+\\.json$")));
    }

    /**
     * Converts the JSON files into Level objects, and orders them in
     * a List by ascending level number.
     *
     * @return a sorted List of Levels
     */
    private TreeMap<Integer, Level> createLevels(){
        TreeMap<Integer, Level> allLevels = new TreeMap<>();
        for(File levelFile : levelFiles){
            try {
                Gson gson = new Gson();
                JsonReader reader = new JsonReader(new FileReader(levelFile));
                Level currentLevel = gson.fromJson(reader, Level.class);
                allLevels.put(currentLevel.getLevelNumber(), currentLevel);
            } catch(FileNotFoundException ignored) {}
        }
        return allLevels;
    }

    /**
     * Check the level number is valid.
     *
     * @param levelNumber The number of the level (starting from 1)
     * @return True if there is a level that matches the level number given.
     */
    private boolean isValidLevel(int levelNumber){
        return levels.containsKey(levelNumber);
    }

    /**
     * Get the level layout of Tiles in a String representation.
     *
     * @param levelNumber The number of the level
     * @return A String representation of the level layout.
     */
    public String[] getLevelLayout(int levelNumber){
        if(!isValidLevel(levelNumber)){
            throw new IllegalArgumentException("That level number is invalid.");
        }
        return levels.get(levelNumber).getLayout();
    }

    /**
     * Get the clock time for the provided level.
     *
     * @param levelNumber The number of the level
     * @return The amount of time in seconds to complete the level
     */
    public int getLevelClock(int levelNumber){
        if(!isValidLevel(levelNumber)){
            throw new IllegalArgumentException("That level number is invalid.");
        }
        return levels.get(levelNumber).getClock();
    }

    /**
     * Obtain an ordered list of all levels loaded.
     * @return A List of level numbers in ascending order.
     */
    public List<Integer> getAllLevelNumbers(){
        return new ArrayList<>(levels.keySet());
    }

    /**
     * Just for testing this class before I write the JUnit tests
     *
     * @param args String args
     */
    public static void main(String[] args){
        LevelLoader test = new LevelLoader();
        System.out.println(test.getAllLevelNumbers());
        String[] test2 = test.getLevelLayout(1);
        System.out.println("test");
    }
}
