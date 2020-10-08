package nz.ac.vuw.ecs.swen225.gp20.persistence;

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
     * Constructor for a Level
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
