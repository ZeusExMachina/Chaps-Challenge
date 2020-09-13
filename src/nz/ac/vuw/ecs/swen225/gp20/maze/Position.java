package nz.ac.vuw.ecs.swen225.gp20.maze;

import com.google.common.base.Preconditions;

import java.util.Objects;

/**
 * Stores position of Tile on board
 */
public final class Position {
	/**
	 * Column or x-coordinate
	 */
	private final int x;

	/**
	 * Row or y-coordinate
	 */
	private final int y;

	/**
	 * Construct a new position
	 * @param x column/x-coordinate
	 * @param y row/y-coordinate
	 */
	public Position(int x, int y) {
		Preconditions.checkArgument(y >= 0, "y-coordinate is negative: %s", y);
		Preconditions.checkArgument(x >= 0, "x-coordinate is negative: %s", x);

		this.x = x;
		this.y = y;
	}

	/**
	 * Retrieve column or x-coordinate
	 * @return x-coordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * Retrieve row or y-coordinate
	 * @return y-coordinate
	 */
	public int getY() {
		return y;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Position position = (Position) o;
		return x == position.x &&
				y == position.y;
	}

	/**
	 * Determine if position is equal to given integer coordinates
	 * @param x COLUMN or x-coord or j-index
	 * @param y ROW or y-coord or i-index
	 * @return if this position is equal to given ints
	 */
	public boolean equals(int x, int y) {
		return x == this.x && y == this.y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}
