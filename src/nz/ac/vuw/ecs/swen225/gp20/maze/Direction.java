package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 * Enumerates which directions we can travel from current position
 */
public enum Direction {
	/**
	 * One row up
	 */
	NORTH,
	/**
	 * One row down
	 */
	SOUTH,
	/**
	 * One column right
	 */
	EAST,
	/**
	 * One column left
	 */
	WEST;

	/**
	 * Get new position from given position and direction
	 * @param p original position
	 * @return new position
	 */
	public Position movePosition(Position p) {
		if (this == NORTH) return new Position(p.getX(), p.getY()-1);
		if (this == SOUTH) return new Position(p.getX(), p.getY()+1);
		if (this == EAST) return new Position(p.getX()+1, p.getY());
		if (this == WEST) return new Position(p.getX()-1, p.getY());
		throw new AssertionError("nz.ac.vuw.ecs.swen225.gp20.maze.Direction not given 4.");
	}
}