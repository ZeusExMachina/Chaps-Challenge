package nz.ac.vuw.ecs.swen225.gp20.maze;

import com.google.common.base.Preconditions;

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
    public void move(Position p, Direction d) {
        if (d.equals(Direction.NORTH)) {
            Preconditions.checkArgument(p.getX() == position.getX(), "Moving north should keep x constant");
            Preconditions.checkArgument(p.getY() == position.getY() - 1, "Moving north should move y 1 down");
        } else if (d.equals(Direction.SOUTH)) {
            Preconditions.checkArgument(p.getX() == position.getX(), "Moving south should keep x constant");
            Preconditions.checkArgument(p.getY() == position.getY() + 1, "Moving south should move y 1 down");
        } else if (d.equals(Direction.WEST)) {
            Preconditions.checkArgument(p.getY() == position.getY(), "Moving west should keep y constant");
            Preconditions.checkArgument(p.getX() == position.getX() - 1, "Moving west should move x 1 down");
        } else if (d.equals(Direction.EAST)) {
            Preconditions.checkArgument(p.getY() == position.getY(), "Moving east should keep y constant");
            Preconditions.checkArgument(p.getX() == position.getX() + 1, "Moving east should move x 1 up");
        }
        position = p;
        direction = d;
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
