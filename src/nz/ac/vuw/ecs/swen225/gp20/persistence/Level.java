package nz.ac.vuw.ecs.swen225.gp20.persistence;

import nz.ac.vuw.ecs.swen225.gp20.maze.Direction;
import nz.ac.vuw.ecs.swen225.gp20.maze.Position;

import java.util.List;
import java.util.Map;

/**
 * This class represents a level of the game. Each Level consists of a
 * level number, a clock value, and a layout of tiles. Each of these classes
 * contains information read in from a JSON file in the levels folder.
 */
class Level {

    /**
     * The arrangement of tiles
     */
    private final String[] layout;
    /**
     * Number of seconds to play the level
     */
    private final int clock;
    /**
     * The level number shown in the GUI
     */
    private final int levelNumber;

    /**
     * The help information to be displayed
     */
    private final String[] helpText;

    /**
     * The Map object here contains each numeric string code in the Level layout
     * can have 1 or more secondary actors, and each of those actors takes a list
     * of Directions in its constructor.
     */
    private final Map<String, List<List<Direction>>> secondaryActorPaths;

    /**
     * The Map object here contains each numeric string code in the Level layout
     * can have 1 or more secondary actors, and each of those actors take a String as
     * a name in its constructor.
     */
    private final Map<String, String> secondaryActorNames;

    private final Map<String, List<Position>> secondaryActorPositions;

    /**
     * Constructor for a Level without secondary actors
     *
     * @param levelNumber the level number displayed on the GUI
     * @param levelLayout the arrangement of Tiles represented as a 2D array
     *                    of Strings
     * @param clock the number of seconds allowed to play the level
     * @param helpText the instructions given by HelpTiles
     */
    protected Level(int levelNumber, String[] levelLayout,
                    int clock, String[] helpText){
        this.layout = levelLayout;
        this.levelNumber = levelNumber;
        this.clock = clock;
        this.helpText = helpText;
        this.secondaryActorPaths = null;
        this.secondaryActorNames = null;
        this.secondaryActorPositions = null;
    }

    /**
     * Constructor for a Level including secondary actors
     * @param levelNumber
     * @param levelLayout
     * @param clock
     * @param helpText
     * @param secondaryActorPaths
     * @param secondaryActorNames
     * @param secondaryActorPositions
     */
    protected Level(int levelNumber, String[] levelLayout, int clock, String[] helpText, Map<String,
            List<List<Direction>>> secondaryActorPaths, Map<String, String> secondaryActorNames,
                    Map<String, List<Position>> secondaryActorPositions){
        this.layout = levelLayout;
        this.levelNumber = levelNumber;
        this.clock = clock;
        this.helpText = helpText;
        this.secondaryActorPaths = secondaryActorPaths;
        this.secondaryActorNames = secondaryActorNames;
        this.secondaryActorPositions = secondaryActorPositions;
    }

    public Map<String, List<List<Direction>>> getSecondaryActorPaths() {
        return secondaryActorPaths;
    }

    public Map<String, String> getSecondaryActorNames() {
        return secondaryActorNames;
    }

    public Map<String, List<Position>> getSecondaryActorPositions() {
        return secondaryActorPositions;
    }

    /**
     * Get the level's layout
     *
     * @return a String representation of the Level's Tiles
     */
    protected String[] getLayout() {
        return layout;
    }

    /**
     * Get the level's clock value
     *
     * @return the number of seconds allowed to complete this level
     */
    protected int getClock() {
        return clock;
    }

    /**
     * Get the level number as defined in the JSON filename
     *
     * @return the level number
     */
    protected int getLevelNumber() {
        return levelNumber;
    }

    /**
     * Get the levels help text
     *
     * @return an array of help instructions
     */
    protected String[] getHelpText(){ return helpText; }


}
