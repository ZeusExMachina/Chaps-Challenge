package nz.ac.vuw.ecs.swen225.gp20.maze;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * From handout: Actors can freely move onto those tiles.
 */
public class FreeTile implements Tile {
	@Override
	public boolean canMoveTo() {
		return true;
	}


	@Override
	public BufferedImage getImage() throws IOException {
		return ImageIO.read(new File("free.png"));
	}
}
