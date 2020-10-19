package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 *  From handout: Chap can be moved by key strokes (up-right-down-left),
 *  his movement is restricted by the nature of the tiles (for instance, he
 *  cannot move into walls). Note that the icon may depend on the current
 *  direction of movement.
 */
public class Chap extends Actor {

    /**
     * Create a new Chap, starting by facing south towards camera.
     *
     * @param p starting position
     * @param n name of actor
     */
    public Chap(Position p, String n) {
        super(p, n);
    }

    @Override
    public void move(Maze m) {
        throw new RuntimeException("Chap should only be directed via user-input");
    }

    @Override
    public String getCode() {
        return "!";
    }
}
