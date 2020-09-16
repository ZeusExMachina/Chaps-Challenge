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
	/**
	 * Stores tile's Position on Maze board
	 */
	private final Position position;

	/**
	 * Make a TreasureTile
	 * @param row row or y-coordinate of position
	 * @param col column or x-coordinate of position
	 */
	public TreasureTile(int row, int col) {
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
		return true;
	}

	@Override
	public BufferedImage getImage() throws IOException {
		return ImageIO.read(new File("treasure.png"));
	}

	@Override
	public String code() {
		return "#";
	}
}
