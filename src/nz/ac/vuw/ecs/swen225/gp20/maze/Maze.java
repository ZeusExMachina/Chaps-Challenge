package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 * From handout: This is the domain model that forms the core of the application, it
 * conceptualises the maze, its elements and their interaction.  This module is
 * responsible for representing and maintaining the state of the game, such as what
 * type of objects are on the maze, where these objects are, and which actions are
 * allowed to change the state of those objects.
 */
public class Maze {
	/**
	 * Stores tiles currently on
	 */
	private Tile[][] board;

	/**
	 * Change board as constructed by persistence module
	 * @param b new board
	 */
	public void setBoard(Tile[][] b) {
		board = b;
	}

	/**
	 * Retrieve tile in given position
	 * @param p position of tile wanted
	 * @return tile wanted
	 */
	public Tile getTile(Position p) {
		// TODO: preconditions check bounds
		return board[p.getY()][p.getX()];
	}

	/**
	 * Retrieve tile in given direction from given position
	 * @param p position of original tile
	 * @param d direction to move to
	 * @return position of new tile
	 */
	public Tile getNeighbouringTile(Position p, Direction d) {
		// Preconditions will handled in getTile()
		Position newPos = d.movePosition(p);
		return getTile(newPos);
	}
}
