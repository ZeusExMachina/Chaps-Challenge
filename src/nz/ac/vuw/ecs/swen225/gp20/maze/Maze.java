package nz.ac.vuw.ecs.swen225.gp20.maze;

import com.google.common.base.Preconditions;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * From handout: This is the domain model that forms the core of the application, it
 * conceptualises the maze, its elements and their interaction.  This module is
 * responsible for representing and maintaining the state of the game, such as what
 * type of objects are on the maze, where these objects are, and which actions are
 * allowed to change the state of those objects.
 */
public class Maze {
	/**
	 * The current instance of Maze.
	 */
	private static final Maze instance = new Maze();
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
	 * Private constructor so only 1 instance made
	 */
	private Maze() {
	}

	/**
	 * Singleton pattern, return the single instance of Maze
	 * @return Maze instance
	 */
	public static Maze getInstance() {
		return instance;
	}

	/**
	 * Parse given String array converting each character into a cell.
	 *
	 * @param in input String array
	 * @param helpText helpText String array
	 */
	public void loadLevel(String[] in, String[] helpText) {
		inventory.clear();
		treasuresLeft = 0;

		for (String row : in) {
			Preconditions.checkArgument(row.length() == in[0].length(),
					"Non-rectangular shaped array");
		}

		board = new Tile[in.length][in[0].length()];
		boolean isExitSet = false;
		int helpTileIndex = 0;
		for (int row = 0; row < in.length; row++) {
			for (int col = 0; col < in[row].length(); col++) {
				char c = in[row].charAt(col);
				if (c == '!') {
					chap = new Actor(new Position(col, row), "chap");
					board[row][col] = new FreeTile(row, col);
				} else {
					board[row][col] = makeTile(c, row, col);
					if (board[row][col] instanceof ExitTile) isExitSet = true;
					if (board[row][col] instanceof HelpTile) {
						setHelp(helpTileIndex, helpText[helpTileIndex]);
						helpTileIndex++;
					}
				}
			}
		}

		if (chap == null) {
			throw new AssertionError("Board should have a Chap '!'");
		}
		if (!isExitSet) {
			throw new AssertionError("Board should have an exit '@'");
		}
	}

	/**
	 * Set the tooltip text of a given HelpTile, specified by giving
	 * its index (e.g. 1st HelpTile in array order, index = 0).
	 *
	 * @param index number it appears in 2D array if it was collapsed into a 1D array
	 * @param text  text to set
	 */
	protected void setHelp(int index, String text) {
		for (Tile[] row : board) {
			for (Tile t : row) {
				if (t instanceof HelpTile) {
					HelpTile h = (HelpTile) t;
					if (index == 0) {
						h.setHelp(text);
						return;
					}
					index--;
				}
			}
		}
		throw new IllegalArgumentException("Help tile #" + index + " doesn't exist.");
	}

	/**
	 * Called to check if Chap is standing on a HelpTile. If so,
	 * return the corresponding help text.
	 *
	 * @return the corresponding help text, null if not
	 */
	public String isOnHelp() {
		Position pos = chap.getPosition();
		Tile t = board[pos.getY()][pos.getX()];
		if (t instanceof HelpTile) {
			return ((HelpTile) t).getHelp();
		}
		return null;
	}

	/**
	 * Construct tile based on character representation
	 *
	 * @param c   tile code to make
	 * @param row row or y-coordinate of position
	 * @param col column or x-coordinate of position
	 * @return tile made
	 */
	private Tile makeTile(char c, int row, int col) {
		if (c >= 'A' && c <= 'D') return new DoorTile(c, row, col);
		if (c >= 'a' && c <= 'd') return new KeyTile(c, row, col);
		if (c == 'X') return new ExitLockTile(row, col);
		if (c == '@') return new ExitTile(row, col);
		if (c == '_') return new FreeTile(row, col);
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
		for (int i = 0; i < board.length; i++) {
			sb.append('|');
			for (int j = 0; j < board[i].length; j++) {
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
	 *
	 * @param p position of tile wanted
	 * @return tile wanted
	 */
	private Tile getTile(Position p) {
		Preconditions.checkArgument(p.getY() >= 0, "y-coordinate is negative: %s", p);
		Preconditions.checkArgument(p.getX() >= 0, "x-coordinate is negative: %s", p);
		Preconditions.checkArgument(p.getY() < board.length, "y-coordinate is out of bounds: %s", p);
		Preconditions.checkArgument(p.getX() < board[0].length, "x-coordinate is out of bounds: %s", p);

		return board[p.getY()][p.getX()];
	}

	/**
	 * Retrieve tile in given direction from given position.
	 *
	 * @param p position of original tile
	 * @param d direction to move to
	 * @return position of new tile
	 */
	private Tile getNeighbouringTile(Position p, Direction d) {
		// Preconditions will be handled in getTile() and Position constructor

		Position newPos = d.movePosition(p);
		return getTile(newPos);
	}

	/**
	 * Free up tile at given position, e.g. if picked up or unlocked.
	 *
	 * @param p position to clear
	 */
	private void clearTile(Position p) {
		Preconditions.checkNotNull(getTile(p), "Tile doesn't exist");

		board[p.getY()][p.getX()] = new FreeTile(p.getY(), p.getX());
	}

	/**
	 * Find out how many items in the inventory match a certain class/type.
	 *
	 * @param type Tile class to count
	 * @return count of items belonging to certain class
	 */
	protected int countTypesInInventory(Class<? extends Tile> type) {
		int result = 0;
		for (Tile t : inventory) {
			if (t.getClass().equals(type)) result++;
		}
		return result;
	}

	/**
	 * Find out if a key of a given colour is inside the inventory.
	 *
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
	 *
	 * @param d given direction
	 * @return if move successful
	 */
	public boolean moveChap(Direction d) {
		try {
			Tile t = getNeighbouringTile(chap.getPosition(), d);
			if (t.canMoveTo(this)) {
				chap.move(t.getPosition(), d);
				if (t.isCleared()) {
					if (t.isInventoried()) inventory.add(t);
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
			// go straight to return false
		}
		return false;
	}

	/**
	 * Retrieve the amount of treasures left to obtain on board.
	 *
	 * @return treasures left
	 */
	public int getTreasuresLeft() {
		return treasuresLeft;
	}

	/**
	 * Figure out if player is at the end of level
	 *
	 * @return true if player is done
	 */
	public boolean isLevelDone() {
		for (Tile[] row : board) {
			for (Tile t : row) {
				if (t instanceof ExitTile) {
					ExitTile e = (ExitTile) t;
					if (e.getPosition().equals(chap.getPosition())) return true;
				}
			}
		}
		return false;
	}

	/**
	 * Get the images required to render the board, restricting access
	 * to the board.
	 * @return array of images to display
	 */
	public BufferedImage[][] getImages() {
		BufferedImage[][] result = new BufferedImage[board.length][board[0].length];
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				result[row][col] = board[row][col].getImage();
			}
		}
		return result;
	}

	/**
	 * Get an unmodifiable list of what the player has in inventory
	 * @return immutable inventory list
	 */
	public List<BufferedImage> getInventory() {
		return inventory.stream().map(Tile::getImage).collect(Collectors.toUnmodifiableList());
	}

	/**
	 * Retrieve Chap's current position.
	 * @return chap's position
	 */
	public Position getChapPosition() {
		return chap.getPosition();
	}

	/**
	 * Retrieve image representing Chap
	 * @return chap's image
	 * @throws IOException if image isn't found
	 */
	public BufferedImage getChapImage() throws IOException {
		return chap.getImage();
	}
}
