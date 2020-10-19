package nz.ac.vuw.ecs.swen225.gp20.maze;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class Monster extends Actor {

    /**
     * Provides a list of directions that a secondary actor uses
     * to simulate automatic movement.
     */
    private final List<Direction> path;
    /**
     * Points to which direction a secondary actor should move to next
     */
    private int pathPtr;

    /**
     * Make a new Monster with a specific path
     *
     * @param p starting position
     * @param n name of actor
     * @param d list of directions actor moves to
     */
    public Monster(Position p, String n, List<Direction> d) {
        super(p, n);
        direction = d.get(d.size()-1);
        path = Collections.unmodifiableList(d);
        pathPtr = -1;
    }

    @Override
    public void move(Maze m) {
        pathPtr++;
        if (pathPtr >= path.size()) pathPtr = 0;
        try {
            Direction d = path.get(pathPtr);
            Tile newTile = m.getNeighbouringTile(position, d);
            Position p = newTile.getPosition();
            if (newTile instanceof FreeTile) {
                move(p, d);
            }
        } catch (IllegalArgumentException ignored) {
            throw new RuntimeException("Secondary actor making illegal moves");
        }
    }

    @Override
    public String getCode() {
        return "1";
    }
}
