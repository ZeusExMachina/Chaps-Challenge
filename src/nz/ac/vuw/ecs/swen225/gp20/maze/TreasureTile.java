package nz.ac.vuw.ecs.swen225.gp20.maze;

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
	public boolean isCleared() {
		return true;
	}

	@Override
	public boolean isInventoried() {
		return false;
	}

	@Override
	public String getImageName() {
		return "treasure.png";
	}

	@Override
	public String code() {
		return "#";
	}
}
