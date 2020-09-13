package nz.ac.vuw.ecs.swen225.gp20.maze;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

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
	 * Chap's current inventory
	 */
	private final List<Tile> inventory = new ArrayList<>();
	/**
	 * Stores position of chap
	 */
	private Position chap;

	/**
	 * Make Maze as defined in the given String array
	 * @param in input array
	 */
	public Maze(String[] in) {
		for (String row : in) {
			Preconditions.checkArgument(row.length() == in[0].length(), "Irregularly shaped array");
		}

		board = new Tile[in.length][in[0].length()];
		for (int i=0; i<in.length; i++) {
			for (int j=0; j<in[i].length(); j++) {
				char c = in[i].charAt(j);
				if (c == '!') {
					chap = new Position(j, i);
					board[i][j] = new FreeTile(i, j);
				} else {
					board[i][j] = makeTile(c, i, j);
				}
			}
		}
	}

	/**
	 * Construct tile based on character representation
	 * @param c tile code to make
	 * @param row row or y-coordinate of position
	 * @param col column or x-coordinate of position
	 * @return tile made
	 */
	private Tile makeTile(char c, int row, int col) {
		if (c >= 'A' && c <= 'D') return new DoorTile(c, row, col);
		if (c >= 'a' && c <= 'd') return new KeyTile(c, row, col);
		if (c == 'X') return new ExitLockTile(row, col);
		if (c == '@') {
			ExitLockTile e = new ExitLockTile(row, col);
			e.unlock();
			return e;
		}
		if (c == ' ') return new FreeTile(row, col);
		if (c == '?') return new HelpTile(row, col);
		if (c == '#') return new TreasureTile(row, col);
		if (c == '/') return new WallTile(row, col);
		throw new IllegalArgumentException("Code doesn't exist");
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<board.length; i++) {
			sb.append('|');
			for (int j=0; j<board[i].length; j++) {
				if (chap.equals(j, i)) sb.append('!');
				else sb.append(board[i][j].code());
				sb.append('|');
			}
		}
		return sb.toString();
	}

	/**
	 * Retrieve tile in given position
	 * @param p position of tile wanted
	 * @return tile wanted
	 */
	public Tile getTile(Position p) {
		Preconditions.checkArgument(p.getY() < board.length, "y-coordinate is out of bounds: %s", p);
		Preconditions.checkArgument(p.getX() < board[0].length, "x-coordinate is out of bounds: %s", p);

		return board[p.getY()][p.getX()];
	}

	/**
	 * Retrieve tile in given direction from given position
	 * @param p position of original tile
	 * @param d direction to move to
	 * @return position of new tile
	 */
	public Tile getNeighbouringTile(Position p, Direction d) {
		// Preconditions will be handled in getTile() and Position constructor

		Position newPos = d.movePosition(p);
		return getTile(newPos);
	}

	/**
	 * Move Chap in a given direction
	 * @param d given direction
	 * @return if move successful
	 */
	public boolean moveChap(Direction d) {
		try {
			Tile t = getNeighbouringTile(chap, d);
			if (t.canMoveTo()) {
				chap = t.getPosition();
				// TODO: clear tile if obtainable
			}
		} catch (IllegalArgumentException ignored) {
		}
		return false;
	}
}
