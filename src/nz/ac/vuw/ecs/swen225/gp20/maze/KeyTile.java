package nz.ac.vuw.ecs.swen225.gp20.maze;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * From handout: Actors can move onto those tiles. If Chap moves onto
 * such a tile, he picks up the key with this colour, once this is done,
 * the tile turns into a free tile.
 */
public class KeyTile implements Tile {
	@Override
	public boolean canMoveTo() {
		return true;
	}

	/**
	 * Enumerates colours available in assets
	 */
	public enum Colour {
		COLOUR_ONE,
		COLOUR_TWO,
		COLOUR_THREE,
		COLOUR_FOUR
	}

	@Override
	public BufferedImage getImage() throws IOException {
		return ImageIO.read(new File("key.png"));
	}
}
