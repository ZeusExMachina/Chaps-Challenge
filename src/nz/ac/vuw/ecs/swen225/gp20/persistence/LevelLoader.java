package nz.ac.vuw.ecs.swen225.gp20.persistence;

import com.google.gson.Gson;

import java.io.File;

/**
 *  A class responsible for loading level data from json files.
 */
public class LevelLoader {

    /**
     * Stores the JSON files that hold level layout data
     */
    File[] levels;

    /**
     * Constructor for LevelLoader.
     */
    LevelLoader(){
        levels = detectLevelFiles();
    }

    /**
     * Get JSON files from the levels folder.
     * @return an Array of files that store the game levels
     */
    private File[] detectLevelFiles(){
        File levelsFolder = new File("levels");
        return levelsFolder.listFiles(((dir, name) -> name.endsWith(".json")));
    }

    /**
     * Gets the number of levels in the game
     * @return the number of levels
     */
    public int getNumberOfLevels(){
        return levels.length;
    }

    /**
     * Just for testing this class
     * @param args string args
     */
    public static void main(String[] args){
        LevelLoader test = new LevelLoader();
        System.out.println("test");
    }
}
