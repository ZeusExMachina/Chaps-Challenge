package nz.ac.vuw.ecs.swen225.gp20.maze;

import java.awt.image.BufferedImage;

/**
 * From handout: Actors can freely move onto those tiles.
 *
 * @author Johniel Bocacao 300490028
 */
public class FreeTile extends Tile {
	/**
	 * Stores tile's Position on Maze board
	 */
	private final Position position;
	/**
	 * Stores image representing tile
	 */
	private final BufferedImage image;

	/**
	 * Make a FreeTile
	 * @param row row or y-coordinate of position
	 * @param col column or x-coordinate of position
	 */
	public FreeTile(int row, int col) {
		position = new Position(col, row);
		image = loadImage("free.png");
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
		return false;
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
		return "_";
	}
}
