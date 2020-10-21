package nz.ac.vuw.ecs.swen225.gp20.persistence;

import nz.ac.vuw.ecs.swen225.gp20.application.GameGUI;
import nz.ac.vuw.ecs.swen225.gp20.maze.Chap;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.maze.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * This class collects all the data required to save and load
 * game states in a class to be easily serialised altogether
 * using the GSON library.
 */
class State {

    /**
     *
     */
    private final int levelNumber;
    /**
     * The time or clock value field in GameGUI
     */
    private final double timeVal;
    /**
     * The Actor field in Maze
     */
    private final Chap chap;
    /**
     * The board in Maze as a String
     */
    private final String[] board;
    /**
     * The inventory field in Maze
     */
    private final ArrayList<String> inventory;
    /**
     * The treasuresLeft field in Maze
     */
    private final int treasuresLeft;



    /**
     * @param levelNumber the current level
     * @param timeVal clock or timer number
     * @param chap The Actor
     * @param board The board
     * @param inventory The inventory
     * @param treasuresLeft The treasuresLeft number
     */
    public State(int levelNumber, double timeVal, Chap chap,
                 String[] board, ArrayList<String> inventory,
                    int treasuresLeft){
        this.levelNumber = levelNumber;
        this.timeVal = timeVal;
        this.chap = chap;
        this.board = board;
        this.inventory = inventory;
        this.treasuresLeft = treasuresLeft;
    }

    /**
     * Get the level number
     * @return the level number
     */
    public int getLevelNumber() {
        return levelNumber;
    }

    /**
     * Set the Maze with the loaded components
     * @param helpText The help text of the level
     */
    public void setMaze(String[] helpText){
        List<Tile> tileInventory = convertInventoryToTile();
        Maze.getInstance().loadLevel(board, helpText,  chap, tileInventory, treasuresLeft);
    }

    /**
     * Set the GameGUI with the loaded components
     * @param game the current Game class
     */
    public void setGame(GameGUI game){
        game.setGameLevel(levelNumber);
        game.setTime(timeVal);
    }

    /**
     * Take the String representation of the Inventory and
     * create Tiles for each of the String entries
     * @return a List of Tiles
     */
    private List<Tile> convertInventoryToTile(){
        ArrayList<Tile> result = new ArrayList<>();
        for (String tileInfo : inventory) {
            result.add(convertToTile(tileInfo));
        }
        return result;
    }



//    /**
//     * Take the String representation of the Board and create
//     * Tiles for each of the String entries
//     * @return a 2D array of Tiles
//     */
//    private Tile[][] convertBoardToTile(){
//        Tile[][] result = new Tile[board.length][board[0].length];
//        for(int i = 0; i < board.length; i++){
//            for(int j = 0; j < board[i].length; j++){
//                String tileInfo = board[i][j];
//                if(tileInfo == null){
//                    result[i][j] = null;
//                } else {
//                    result[i][j] = convertToTile(tileInfo);
//                }
//            }
//        }
//        return result;
//    }

    /**
     * Take a serialised String and create a new Tile from it
     * @param tileInfo the String representation of a Tile
     * @return a Tile object with the parameters provided
     */
    private Tile convertToTile(String tileInfo){
        String[] coordinates = tileInfo.substring(1, tileInfo.length()-2).split(",");
        int x = Integer.parseInt(coordinates[0]);
        int y = Integer.parseInt(coordinates[1]);
        char code = tileInfo.substring(tileInfo.length()-1).charAt(0);
        return Maze.getInstance().makeTile(code, y, x);
    }
}
