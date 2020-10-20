package nz.ac.vuw.ecs.swen225.gp20.persistence;

import nz.ac.vuw.ecs.swen225.gp20.application.GameGUI;
import nz.ac.vuw.ecs.swen225.gp20.maze.Actor;
import nz.ac.vuw.ecs.swen225.gp20.maze.Chap;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.maze.Tile;
import nz.ac.vuw.ecs.swen225.gp20.recnplay.Recorder;

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
     * The board field in Maze
     */
    private final String[][] board;
    /**
     * The inventory field in Maze
     */
    private final ArrayList<String> inventory;
    /**
     * The treasuresLeft field in Maze
     */
    private final int treasuresLeft;

    /**
     * @param timeVal clock or timer number
     * @param chap The Actor
     * @param board The board
     * @param inventory The inventory
     * @param treasuresLeft The treasuresLeft number
     */
    public State(int levelNumber, double timeVal, Chap chap,
                 String[][] board, ArrayList<String> inventory,
                    int treasuresLeft){
        this.levelNumber = levelNumber;
        this.timeVal = timeVal;
        this.chap = chap;
        this.board = board;
        this.inventory = inventory;
        this.treasuresLeft = treasuresLeft;
    }

    public void setMaze(){
        List<Tile> loadedInventory = convertInventoryToTile();
        Tile[][] loadedBoard = convertBoardToTile();
        Maze.getInstance().loadLevel(loadedBoard, chap, loadedInventory, treasuresLeft);

    }

    public void setGame(GameGUI game){
        game.setGameLevel(levelNumber);
        game.setTime();
    }

    public List<Tile> convertInventoryToTile(){
        ArrayList<Tile> result = new ArrayList<>();
        for(int i = 0; i < inventory.size(); i++){
            String tileInfo = inventory.get(i);
            String[] coordinates = tileInfo.substring(1, tileInfo.length()-2).split(",");
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);
            char code = tileInfo.substring(tileInfo.length()-1).charAt(0);
            result.add(Maze.getInstance().makeTile(code, x, y));
        }
        return result;
    }

    public Tile[][] convertBoardToTile(){
        Tile[][] result = new Tile[board.length][board[0].length];
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                String tileInfo = board[i][j];
                if(tileInfo == null){
                    result[i][j] = null;
                } else {
                    String[] coordinates = tileInfo.substring(1, tileInfo.length()-2).split(",");
                    int x = Integer.parseInt(coordinates[0]);
                    int y = Integer.parseInt(coordinates[1]);
                    char code = tileInfo.substring(tileInfo.length()-1).charAt(0);
                    result[i][j] = Maze.getInstance().makeTile(code, x, y);
                }
            }
        }
        return result;
    }
}
