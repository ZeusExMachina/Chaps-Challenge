package nz.ac.vuw.ecs.swen225.gp20.maze;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Tiles make up the maze.
 */
public interface Tile {
	/**
	 * Find out whether or not the player can move onto this tile
	 * @param m current maze instance
	 * @return true if player can move onto tile
	 */
	boolean canMoveTo(Maze m);

	/**
	 * Find out whether or not the player can clear this tile
	 * @return true if player can clear tile
	 */
	boolean isCleared();

	/**
	 * Find out whether or not the player can pick up this tile
	 * and put into inventory
	 * @return true if player can put in inventory
	 */
	boolean isInventoried();

	/**
	 * Get the image representing tile
	 * @return buffered image to display
	 * @throws IOException when the file name isn't found
	 */
	BufferedImage getImage() throws IOException;

	/**
	 * Get tile's Position on Maze board
	 * @return position of tile
	 */
	Position getPosition();

	/**
	 * Get ASCII representation of Tile for testing purposes
	 * @return tile's code
	 */
	String code();
}
