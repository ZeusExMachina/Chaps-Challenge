package nz.ac.vuw.ecs.swen225.gp20.persistence;

import nz.ac.vuw.ecs.swen225.gp20.maze.Actor;
import nz.ac.vuw.ecs.swen225.gp20.maze.Tile;
import nz.ac.vuw.ecs.swen225.gp20.recnplay.Recorder;

import java.util.List;

/**
 * This class collects all the data required to save and load
 * game states in a class to be easily serialised altogether
 * using the GSON library.
 */
class State {
    /**
     * The time or clock value field in GameGUI
     */
    private final int timeVal;
    /**
     * The recorder object field in GameGUI
     */
    private final Recorder recorder;
    /**
     * The Actor field in Maze
     */
    private final Actor chap;
    /**
     * The board field in Maze
     */
    private final Tile[][] board;
    /**
     * The inventory field in Maze
     */
    private final List<Tile> inventory;
    /**
     * The treasuresLeft field in Maze
     */
    private final int treasuresLeft;

    /**
     * @param timeVal clock or timer number
     * @param recorder The Recorder object
     * @param chap The Actor
     * @param board The board
     * @param inventory The inventory
     * @param treasuresLeft The treasuresLeft number
     */
    protected State(int timeVal, Recorder recorder, Actor chap,
                    Tile[][] board, List<Tile> inventory,
                    int treasuresLeft){
        this.timeVal = timeVal;
        this.recorder = recorder;
        this.chap = chap;
        this.board = board;
        this.inventory = inventory;
        this.treasuresLeft = treasuresLeft;
    }
}
