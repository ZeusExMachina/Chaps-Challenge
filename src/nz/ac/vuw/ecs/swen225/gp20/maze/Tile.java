package nz.ac.vuw.ecs.swen225.gp20.maze;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
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
	 * Retrieves file name of Tile's image
	 * @return filename of tile image
	 */
	abstract String getImageName();

	/**
	 * Get the image representing tile
	 * @return buffered image to display
	 * @throws IOException when the file name isn't found
	 */
	public final BufferedImage findImage() throws IOException {
		String path = "resources/" + getImageName();
		URL resource = Thread.currentThread().getContextClassLoader().getResource(path);
		if (resource != null) {
			return ImageIO.read(resource);
		}
		throw new IOException(getImageName()+" not found");
	}

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
