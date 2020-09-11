package nz.ac.vuw.ecs.swen225.gp20.maze;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * From handout: Behaves like a wall time for Chap as long as there are still
 * uncollected treasures. Once the treasure chest is full (all treasures have
 * been collected), Chap can pass through the lock. Once Chap reaches this tile,
 * the game level is finished.
 */
public class ExitTile implements Tile {
	/**
	 * Stores whether or not we can exit
	 */
	private boolean isLocked;

	/**
	 * Get state of exit tile.
	 * @return true if locked
	 */
	public boolean isLocked() { return isLocked; }

	/**
	 * Unlock exit tile (only if all treasures are picked up). Not reversible.
	 */
	public void unlock() {
		// TODO: precondition, check amount of treasure
		isLocked = true;
	}

	@Override
	public boolean canMoveTo() {
		return !isLocked;
	}

	@Override
	public BufferedImage getImage() throws IOException {
		if (isLocked) ImageIO.read(new File("exit_lock.png"));
		return ImageIO.read(new File("exit.png"));
	}
}
