package nz.ac.vuw.ecs.swen225.gp20.maze;

import com.google.common.base.Preconditions;

import java.awt.image.BufferedImage;

/**
 * From handout: Actors can move onto those tiles. If Chap moves onto
 * such a tile, he picks up the key with this colour, once this is done,
 * the tile turns into a free tile.
 */
public class KeyTile extends Tile {

	/**
	 * Colour of this key
	 */
	private final Colour colour;
	/**
	 * Stores tile's Position on Maze board
	 */
	private final Position position;
	/**
	 * Stores image representing tile
	 */
	private final BufferedImage image;

	/**
	 * Make a KeyTile with a colour represented by a character
	 * @param c character representation of DoorTile
	 * @param row row or y-coordinate of position
	 * @param col column or x-coordinate of position
	 */
	public KeyTile(char c, int row, int col) {
		int i = c - 'a';
		Preconditions.checkArgument(i >= 0, "Negative index given for door colour");
		Preconditions.checkArgument(i < Colour.values().length, "Non-existent index given for door colour");
		colour = Colour.values()[i];
		position = new Position(col, row);
		image = loadImage("key_" + colour.name().toLowerCase() + ".png");
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
		return true;
	}

	@Override
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * Enumerates colours available in assets
	 */
	public enum Colour {
		/**
		 * Red - colour 0
		 */
		RED,
		/**
		 * Green - colour 1
		 */
		GREEN,
		/**
		 * Blue - colour 2
		 */
		BLUE,
		/**
		 * Yellow - colour 3
		 */
		YELLOW
	}

	/**
	 * Retrieve colour of this key
	 *
	 * @return this key's colour
	 */
	public Colour getColour() {
		return colour;
	}

	@Override
	public Position getPosition() {
		return position;
	}

	@Override
	public String code() {
		return Character.toString('a'+colour.ordinal());
	}
}
