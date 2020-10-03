package nz.ac.vuw.ecs.swen225.gp20.maze;

import java.awt.image.BufferedImage;

/**
 * From handout: Like a free tile, but when Chap steps on this field, a help text will be displayed.
 */
public class HelpTile extends Tile {
	/**
	 * Stores tile's Position on Maze board
	 */
	private final Position position;
	/**
	 * Stores image representing tile
	 */
	private final BufferedImage image;

	/**
	 * Make a HelpTile
	 * @param row row or y-coordinate of position
	 * @param col column or x-coordinate of position
	 */
	public HelpTile(int row, int col) {
		position = new Position(col, row);
		image = loadImage("help.png");
	}

	@Override
	public Position getPosition() {
		return position;
	}

	/**
	 * Help text to display
	 */
	private String help;

	/**
	 * Retrieve help text to display
	 * @return displayed help text
	 */
	public String getHelp() {
		return help;
	}

	/**
	 * Chang help text to display
	 * @param h new help text
	 */
	public void setHelp(String h) {
		help = h;
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
		return "?";
	}
}
