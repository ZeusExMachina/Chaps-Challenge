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
                "__/////_/////__",
                "__/___///___/__",
                "__/_#_/@/_#_/__",
                "/////C/X/C/////",
                "/_d_A_____B_d_/",
                "/_#_/a_?_b/_#_/",
                "/////#_!_#/////",
                "/_#_/a___b/_#_/",
                "/_c_B__#__A___/",
                "//////D/D//////",
                "____/__/__/____",
                "____/__/#_/____",
                "____/_#/c_/____",
                "____///////____",
        };

        String[] levelOneHelpText = {
                        "Move Chap around using the arrows keys.\n" +
                        "Pick up keys to unlock the doors.\n" +
                        "Pick up treasures to unlock the exit.\n"
        };

        try {
            Writer writer = Files.newBufferedWriter(Paths.get("levels/level1.json"));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Level levelOne = new Level(1, levelOneLayout, 100, levelOneHelpText);
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
