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
	 * Amount of treasure tiles left in level
	 */
	private int treasuresLeft;
	/**
	 * Stores information on current player
	 */
	private Actor chap;

	/**
	 * Make Maze as defined in the given String array
	 * @param in input array
	 */
	public Maze(String[] in) {
		loadLevel(in);
	}

	/**
	 * Parse given String array converting each character into a cell.
	 * @param in input String array
	 */
	public void loadLevel(String[] in) {
		for (String row : in) {
			Preconditions.checkArgument(row.length() == in[0].length(),
					"Irregularly shaped array");
		}

		board = new Tile[in.length][in[0].length()];
		for (int i=0; i<in.length; i++) {
			for (int j=0; j<in[i].length(); j++) {
				char c = in[i].charAt(j);
				if (c == '!') {
					chap = new Actor(new Position(j, i), "chap");
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
		if (c == '@') return new ExitTile(row, col);
		if (c == ' ') return new FreeTile(row, col);
		if (c == '?') return new HelpTile(row, col);
		if (c == '#') {
			treasuresLeft++;
			return new TreasureTile(row, col);
		}
		if (c == '/') return new WallTile(row, col);
		throw new IllegalArgumentException("Code doesn't exist");
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<board.length; i++) {
			sb.append('|');
			for (int j=0; j<board[i].length; j++) {
				if (chap.getPosition().equals(j, i)) sb.append('!');
				else sb.append(board[i][j].code());
				sb.append('|');
			}
			sb.append('\n');
		}
		return sb.toString();
	}

	/**
	 * Retrieve tile in given position
	 * @param p position of tile wanted
	 * @return tile wanted
	 */
	public Tile getTile(Position p) {
		Preconditions.checkArgument(p.getY() >= 0, "y-coordinate is negative: %s", p);
		Preconditions.checkArgument(p.getX() >= 0, "x-coordinate is negative: %s", p);
		Preconditions.checkArgument(p.getY() < board.length, "y-coordinate is out of bounds: %s", p);
		Preconditions.checkArgument(p.getX() < board[0].length, "x-coordinate is out of bounds: %s", p);

		return board[p.getY()][p.getX()];
	}

	/**
	 * Retrieve tile in given direction from given position.
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
	 * Free up tile at given position, e.g. if picked up or unlocked.
	 * @param p position to clear
	 */
	private void clearTile(Position p) {
		Preconditions.checkNotNull(getTile(p), "Tile doesn't exist");

		board[p.getY()][p.getX()] = new FreeTile(p.getX(), p.getY());
	}

	/**
	 * Find out how many items in the inventory match a certain class/type.
	 * @param type class to count
	 * @return count of items belonging to certain class
	 */
	private int countTypesInInventory(Class<?> type) {
		int result = 0;
		for (Tile t : inventory) {
			if (t.getClass().equals(type)) result++;
		}
		return result;
	}

	/**
	 * Find out if a key of a given colour is inside the inventory.
	 * @param c colour to query
	 * @return true if there is a key of given colour
	 */
	protected boolean containsKey(KeyTile.Colour c) {
		for (Tile t : inventory) {
			if (t instanceof KeyTile) {
				KeyTile k = (KeyTile) t;
				if (k.getColour().equals(c)) return true;
			}
		}
		return false;
	}

	/**
	 * Unlock all exit locks on board
	 */
	private void unlockExitLocks() {
		Preconditions.checkArgument(treasuresLeft == 0, "Not all treasures collected.");

		for (Tile[] row : board) {
			for (Tile t : row) {
				if (t instanceof ExitLockTile) {
					((ExitLockTile) t).unlock();
				}
			}
		}
	}

	/**
	 * Move Chap in a given direction
	 * @param d given direction
	 * @return if move successful
	 */
	public boolean moveChap(Direction d) {
		try {
			Tile t = getNeighbouringTile(chap.getPosition(), d);
			if (t.canMoveTo(this)) {
				chap.move(t.getPosition(), d);
				if (t.isObtainable()) {
					inventory.add(t);
					clearTile(chap.getPosition());
					if (t instanceof TreasureTile) {
						treasuresLeft--;
						if (treasuresLeft < 0) {
							throw new AssertionError("Treasures left shouldn't be negative.");
						}
					}
				}
				if (treasuresLeft == 0) {
					unlockExitLocks();
				}
				return true;
			}
		} catch (IllegalArgumentException ignored) {
		}
		return false;
	}
}
