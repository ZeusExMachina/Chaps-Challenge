package nz.ac.vuw.ecs.swen225.gp20.persistence;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 *  A class responsible for loading level data from json files.
 */
public class LevelLoader {

    /**
     * Stores the JSON files that hold level layout data
     */
    private final File[] levels;

    /**
     * Constructor for LevelLoader.
     */
    public LevelLoader(){
        levels = detectLevelFiles();
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
     * Gets the number of levels in the game
     *
     * @return The number of levels
     */
    public int getNumberOfLevels(){
        return levels.length;
    }

    /**
     * Get a String representation of the level.
     *
     * @param levelNumber The number of the level (starting from 1)
     * @return A String array representing the level tiles
     * @throws IllegalArgumentException If the level number doesn't exist
     * @throws FileNotFoundException If the level file is unable to be read
     */
    public String[] getLevelData(int levelNumber) throws IllegalArgumentException, FileNotFoundException {
        if(levelNumber < 1 || levelNumber > getNumberOfLevels()){
            throw new IllegalArgumentException("Not a valid level number.");
        }
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(levels[levelNumber-1]));
        return gson.fromJson(reader, String[].class);
    }

    /**
     * Just for testing this class before I write the JUnit tests
     *
     * @param args String args
     */
    public static void main(String[] args){
        LevelLoader test = new LevelLoader();
        try {
            String[] test2 = test.getLevelData(1);
        } catch (FileNotFoundException e) {
            System.out.println("Could not load level file");
        }
        System.out.println("test");
    }
}
