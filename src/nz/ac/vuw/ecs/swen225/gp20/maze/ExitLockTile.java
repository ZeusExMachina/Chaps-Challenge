package nz.ac.vuw.ecs.swen225.gp20.maze;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * From handout: Behaves like a wall time for Chap as long as there are still
 * uncollected treasures. Once the treasure chest is full (all treasures have
 * been collected), Chap can pass through the lock.
 */
public class ExitLockTile implements Tile {
	/**
	 * Stores whether or not we can exit
	 */
	private boolean isLocked;

	/**
	 * Stores tile's Position on Maze board
	 */
	private final Position position;

	/**
	 * Make a ExitLockTile
	 * @param row row or y-coordinate of position
	 * @param col column or x-coordinate of position
	 */
	public ExitLockTile(int row, int col) {
		position = new Position(col, row);
		isLocked = true;
	}

	@Override
	public Position getPosition() {
		return position;
	}

	/**
	 * Unlock exit tile (only if all treasures are picked up). Not reversible.
	 */
	public void unlock() {
		isLocked = false;
	}

	@Override
	public boolean canMoveTo(Maze m) {
		return !isLocked;
	}

	@Override
	public boolean isObtainable() {
		return true;
	}

	@Override
	public BufferedImage getImage() throws IOException {
		return ImageIO.read(new File("resources/exit_lock.png"));
	}

	@Override
	public String code() {
		return "X";
	}
}
