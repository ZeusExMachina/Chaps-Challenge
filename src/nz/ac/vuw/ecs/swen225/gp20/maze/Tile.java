package nz.ac.vuw.ecs.swen225.gp20.maze;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * Tiles make up the maze.
 */
public abstract class Tile {

	/**
	 * Find out whether or not the player can move onto this tile
	 * @param m current maze instance
	 * @return true if player can move onto tile
	 */
	abstract boolean canMoveTo(Maze m);

	/**
	 * Find out whether or not the player can clear this tile
	 * @return true if player can clear tile
	 */
	abstract boolean isCleared();

	/**
	 * Find out whether or not the player can pick up this tile
	 * and put into inventory
	 * @return true if player can put in inventory
	 */
	abstract boolean isInventoried();

	/**
	 * Load the image representing tile
	 * @param file name of file to load
	 * @return buffered image to display
	 */
	protected static BufferedImage loadImage(String file) {
		String path = "resources/" + file;
		URL resource = Thread.currentThread().getContextClassLoader().getResource(path);
		if (resource != null) {
			try {
				return ImageIO.read(resource);
			} catch (IOException e) {
				// Go to runtime exception
			}
		}
		throw new RuntimeException(file+" not found");
	}

	/**
	 * Get the BufferedImage representing the tile
	 * @return loaded BufferedImage
	 */
	abstract BufferedImage getImage();

	/**
	 * Get tile's Position on Maze board
	 * @return position of tile
	 */
	abstract Position getPosition();

	/**
	 * Get ASCII representation of Tile for testing purposes
	 * @return tile's code
	 */
	abstract String code();
}
