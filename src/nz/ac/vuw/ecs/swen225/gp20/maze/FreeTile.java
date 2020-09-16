package nz.ac.vuw.ecs.swen225.gp20.maze;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * From handout: Actors can freely move onto those tiles.
 */
public class FreeTile implements Tile {

	/**
	 * Stores tile's Position on Maze board
	 */
	private final Position position;

	/**
	 * Make a FreeTile
	 * @param row row or y-coordinate of position
	 * @param col column or x-coordinate of position
	 */
	public FreeTile(int row, int col) {
		position = new Position(col, row);
	}

	@Override
	public Position getPosition() {
		return position;
	}

	@Override
	public boolean canMoveTo(Maze m) {
		return true;
	}

	@Override
	public boolean isObtainable() {
		return false;
	}

	@Override
	public BufferedImage getImage() throws IOException {
		return ImageIO.read(new File("free.png"));
	}

	@Override
	public String code() {
		return " ";
	}
}
