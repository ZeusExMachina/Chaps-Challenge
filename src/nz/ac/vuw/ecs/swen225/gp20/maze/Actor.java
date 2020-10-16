package nz.ac.vuw.ecs.swen225.gp20.maze;

import com.google.common.base.Preconditions;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * From handout: An Actor is a game character that moves around, like Chap,
 * the hero of the game. Chap can be moved by key strokes (up-right-down-left),
 * his movement is restricted by the nature of the tiles (for instance, he
 * cannot move into walls). Note that the icon may depend on the current
 * direction of movement.
 * You should also use a second type of actor in level 2 that interacts with
 * Chap (for instance, by exploding and eating Chap or robbing him). Unlike
 * Chap, actors will move around on their own (randomly, or following
 * some pattern), and are not directed by user input.
 */
public class Actor {
	/**
	 * Stores position of actor.
	 */
	private Position position;
	/**
	 * Stores direction actor previously travelled,
	 * i.e. the way he's currently facing.
	 */
	private Direction direction;
	/**
	 * Stores name of actor for accessing image, e.g. "chap".
	 */
	private final String name;
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
	 * Make a new actor, starting by facing south towards camera.
	 *
	 * @param p starting position
	 * @param n name of actor
	 */
	public Actor(Position p, String n) {
		position = p;
		direction = Direction.SOUTH;
		name = n;
		if (n.equals("roach")) {
			path = Arrays.asList(Direction.EAST,
					Direction.EAST,
					Direction.WEST,
					Direction.WEST);
			direction = Direction.EAST;
		} else {
			path = null;
		}
		pathPtr = -1;
	}

	/**
	 * Make a new actor, starting by facing south towards camera.
	 *
	 * @param p starting position
	 * @param n name of actor
	 * @param d list of directions actor moves to
	 */
	public Actor(Position p, String n, List<Direction> d) {
		position = p;
		direction = d.get(d.size()-1);
		name = n;
		path = Collections.unmodifiableList(d);
		pathPtr = -1;
	}

	/**
	 * Get actor's name
	 * @return name of actor
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get actor's position on the board.
	 *
	 * @return actor's position on board
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * Get the image representing actor, e.g. "chap-south.png".
	 *
	 * @return buffered image to display
	 */
	public BufferedImage getImage() {
		String path = "resources/" +name + "_" + direction.toString() + ".png";
		try {
			return ImageIO.read(new File(path));
		} catch (IOException e) {
			// Go to runtime exception
		}
		throw new RuntimeException("Chap image not found");
	}

	/**
	 * Move the actor if it's secondary
	 *
	 * @param m maze instance
	 */
	public void moveSecondaryActor(Maze m) {
		if (name.equals("chap"))
			throw new AssertionError("Cannot move Chap as a secondary actor.");
		if (path == null)
			throw new AssertionError("Secondary actor not set up.");

		pathPtr++;
		if (pathPtr >= path.size()) pathPtr = 0;
		try {
			Direction d = path.get(pathPtr);
			Tile newTile = m.getNeighbouringTile(position, d);
			if (newTile instanceof FreeTile) {
				move(newTile.getPosition(), d);
			}
		} catch (IllegalArgumentException ignored) {
			throw new RuntimeException("Secondary actor making illegal moves");
		}
	}

	/**
	 * Change actor's position and direction facing.
	 *
	 * @param p new position
	 * @param d new direction
	 */
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
}
