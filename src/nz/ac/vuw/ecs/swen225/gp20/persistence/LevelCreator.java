package nz.ac.vuw.ecs.swen225.gp20.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nz.ac.vuw.ecs.swen225.gp20.maze.Direction;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 *  Feeling cute might delete later
 *  Temporary class helping me generate the level JSON files
 */
class LevelCreator {

    /**
     * Create the level1.json file
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

        String[] levelOneHelpText = {
                        "Move Chap around using the arrows keys.\n" +
                        "Pick up keys to unlock the doors.\n" +
                        "Pick up treasures to unlock the exit.\n"
        };

        try {
            Writer writer = Files.newBufferedWriter(Paths.get("levels/level1.json"));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Level levelOne = new Level(1, levelOneLayout, 100,
                    levelOneHelpText);
            gson.toJson(levelOne, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the level2.json file
     */
    private void generateLevelTwo(){
        String[] levelTwoLayout = {
                "//@///a/b#////b#/",
                "//X///c/__////__/",
                "/__///d/__////__/",
                "/B////b/__////__/",
                "/____/a/__////__/",
                "/__#_/c/__////__/",
                "/_#__/d/__////__/",
                "/1___/b/__////__/",
                "//D/////D//////A/",
                "/_____//________/",
                "/_____//!_?#_a__/",
                "/__#__//________/",
                "/_____//B//////A/",
                "/1____//_#////__/",
                "///A////__////__/",
                "/______/__////__/",
                "/______/__////__/",
                "/__#a__C_a////__/",
                "/__d#__/_#////__/",
                "/______/__////_#/",
                "/1_____/__////_c/"
        };

        String[] levelTwoHelpText = {
                            "Don't touch the monsters!\n" + "(Monsters to be added soon)\n"
        };

        Map<String, List<String>> levelTwoActorNames = new HashMap<>();
        levelTwoActorNames.put("1", new ArrayList<>());
        levelTwoActorNames.get("1").addAll(Arrays.asList("top_monster", "centre_monster", "bottom_monster"));

        Map<String, List<List<Direction>>> levelTwoActorPaths = new HashMap<>();
        levelTwoActorPaths.put("1", new ArrayList<>());

        // Top monsters path
        levelTwoActorPaths.get("1").add(Arrays.asList(Direction.EAST,
                Direction.EAST,
                Direction.EAST,
                Direction.EAST,
                Direction.NORTH,
                Direction.NORTH,
                Direction.NORTH,
                Direction.WEST,
                Direction.WEST,
                Direction.WEST,
                Direction.SOUTH,
                Direction.SOUTH,
                Direction.SOUTH));

        // Middle monsters path
        levelTwoActorPaths.get("1").add(Arrays.asList(Direction.EAST,
                Direction.EAST,
                Direction.EAST,
                Direction.EAST,
                Direction.EAST,
                Direction.NORTH,
                Direction.NORTH,
                Direction.NORTH,
                Direction.NORTH,
                Direction.WEST,
                Direction.WEST,
                Direction.WEST,
                Direction.WEST,
                Direction.SOUTH,
                Direction.SOUTH,
                Direction.SOUTH,
                Direction.SOUTH));

        // Bottom monsters path
        levelTwoActorPaths.get("1").add(Arrays.asList(Direction.EAST,
                Direction.EAST,
                Direction.EAST,
                Direction.EAST,
                Direction.EAST,
                Direction.EAST,
                Direction.NORTH,
                Direction.NORTH,
                Direction.NORTH,
                Direction.NORTH,
                Direction.NORTH,
                Direction.WEST,
                Direction.WEST,
                Direction.WEST,
                Direction.WEST,
                Direction.WEST,
                Direction.SOUTH,
                Direction.SOUTH,
                Direction.SOUTH,
                Direction.SOUTH,
                Direction.SOUTH));



        try {
            Writer writer = Files.newBufferedWriter(Paths.get("levels/level2.json"));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Level levelTwo = new Level(2, levelTwoLayout, 120,
                    levelTwoHelpText, levelTwoActorPaths, levelTwoActorNames);
            gson.toJson(levelTwo, writer);
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
        LevelCreator creator = new LevelCreator();
        creator.generateLevelTwo();
    }
}
