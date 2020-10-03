package nz.ac.vuw.ecs.swen225.gp20.maze;

import java.awt.image.BufferedImage;

/**
 * From handout: If Chap steps onto the tile, the treasure (chip) is picked up
 * and added to the treasure chest. Then the tile turns into a free tile.
 */
public class TreasureTile extends Tile {
	/**
	 * Stores tile's Position on Maze board
	 */
	private final Position position;
	/**
	 * Stores image representing tile
	 */
	private final BufferedImage image;

	/**
	 * Make a TreasureTile
	 * @param row row or y-coordinate of position
	 * @param col column or x-coordinate of position
	 */
	public TreasureTile(int row, int col) {
		position = new Position(col, row);
		image = loadImage("treasure.png");
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
	public boolean isCleared() {
		return true;
	}

	@Override
	public boolean isInventoried() {
		return false;
	}

	@Override
	public BufferedImage getImage() {
		return image;
	}

	@Override
	public String code() {
		return "#";
	}
}
