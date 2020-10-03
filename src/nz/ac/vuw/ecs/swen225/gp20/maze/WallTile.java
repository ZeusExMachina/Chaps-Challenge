package nz.ac.vuw.ecs.swen225.gp20.maze;

import java.awt.image.BufferedImage;

/**
 * From handout: Part of a wall, actors cannot move onto those tiles.
 */
public class WallTile extends Tile {
	/**
	 * Stores tile's Position on Maze board
	 */
	private final Position position;
	/**
	 * Stores image representing tile
	 */
	private final BufferedImage image;

	/**
	 * Make a ExitLockTile
	 * @param row row or y-coordinate of position
	 * @param col column or x-coordinate of position
	 */
	public WallTile(int row, int col) {
		position = new Position(col, row);
		image = loadImage("wall.png");
	}

	@Override
	public Position getPosition() {
		return position;
	}

	@Override
	public boolean canMoveTo(Maze m) {
		return false;
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
		return "/";
	}
}
