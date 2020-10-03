package nz.ac.vuw.ecs.swen225.gp20.maze;

import com.google.common.base.Preconditions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * From handout: Chap can only move onto those tiles if they have the
 * key with the matching colour -- this unlocks the door. After unlocking
 * the door, the locked door turns into a free tile, and Chap keeps the key.
 */
public class DoorTile extends Tile {
	/**
	 * Stores the kind of key that will unlock the door
	 */
	private final KeyTile.Colour colour;
	/**
	 * Stores tile's Position on Maze board
	 */
	private final Position position;

	/**
	 * Make a DoorTile with a colour represented by a character
	 * @param c character representation of DoorTile
	 * @param row row or y-coordinate of position
	 * @param col column or x-coordinate of position
	 */
	public DoorTile(char c, int row, int col) {
		int i = c - 'A';
		Preconditions.checkArgument(i >= 0, "Negative index given for door colour");
		Preconditions.checkArgument(i < KeyTile.Colour.values().length, "Non-existent index given for door colour");
		colour = KeyTile.Colour.values()[i];
		position = new Position(col, row);
	}

	/**
	 * Movable only if player possesses matching key
	 * @return true if player has matching key
	 */
	@Override
	public boolean canMoveTo(Maze m) {
		return m.containsKey(colour);
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
	public String getImageName() {
		return "door_" + colour.name().toLowerCase() + ".png";
	}

	@Override
	public String code() {
		return Character.toString('A'+colour.ordinal());
	}

	@Override
	public Position getPosition() {
		return position;
	}
}
