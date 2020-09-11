package nz.ac.vuw.ecs.swen225.gp20.maze;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Tiles make up the maze.
 */
public interface Tile {
	/**
	 * Find out whether or not the player can move onto this tile
	 * @return true if player can move onto tile
	 */
	boolean canMoveTo();

	/**
	 * Get the image representing tile
	 * @return buffered image to display
	 * @throws IOException when the file name isn't found
	 */
	BufferedImage getImage() throws IOException;
}
