package nz.ac.vuw.ecs.swen225.gp20.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *  Feeling cute might delete later
 *  Temporary class helping me generate the level JSON files
 */
public class LevelCreator {

    /**
     * Constructor for LevelLoader.
     */
    LevelCreator(){
        generateLevelOne();
    }

    /**
     * Create the JSON file
     */
    private void generateLevelOne(){
        String[] levelOneLayout = {
                "_____________________",
                "_____________________",
                "_____________________",
                "_____/////_/////_____",
                "_____/___///___/_____",
                "_____/_#_/@/_#_/_____",
                "___/////C/X/C/////___",
                "___/_d_A_____B_d_/___",
                "___/_#_/a_?_b/_#_/___",
                "___/////#_!_#/////___",
                "___/_#_/a___b/_#_/___",
                "___/___B__#__A___/___",
                "___//////D/D//////___",
                "_______/__/__/_______",
                "_______/__/#_/_______",
                "_______/_#/c_/_______",
                "_______///////_______",
                "_____________________",
                "_____________________",
                "_____________________"
        };
        try {
            Writer writer = Files.newBufferedWriter(Paths.get("levels/level1.json"));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Level levelOne = new Level(1, levelOneLayout, 100);
            gson.toJson(levelOne, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates the output file
     * @param args string args
     */
    public static void main(String[] args){
        new LevelCreator();
    }
}
