package nz.ac.vuw.ecs.swen225.gp20.maze;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * From handout: If Chap steps onto the tile, the treasure (chip) is picked up
 * and added to the treasure chest. Then the tile turns into a free tile.
 */
public class TreasureTile implements Tile {
	@Override
	public boolean canMoveTo() {
		return true;
	}

	@Override
	public BufferedImage getImage() throws IOException {
		return ImageIO.read(new File("treasure.png"));
	}
}
