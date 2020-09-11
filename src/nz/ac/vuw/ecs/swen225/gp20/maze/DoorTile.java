package nz.ac.vuw.ecs.swen225.gp20.maze;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * From handout: Chap can only move onto those tiles if they have the
 * key with the matching colour -- this unlocks the door. After unlocking
 * the door, the locked door turns into a free tile, and Chap keeps the key.
 */
public class DoorTile implements Tile {
	/**
	 * Stores the kind of key that will unlock the door
	 */
	private KeyTile.Colour colour;

	/**
	 * Movable only if player possesses matching key
	 * @return true if player has matching key
	 */
	@Override
	public boolean canMoveTo() {
		return false;
	}

	@Override
	public BufferedImage getImage() throws IOException {
		return ImageIO.read(new File("door.png"));
	}
}
