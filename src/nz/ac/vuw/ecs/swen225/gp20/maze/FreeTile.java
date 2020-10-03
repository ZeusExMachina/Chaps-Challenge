package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 * From handout: Actors can freely move onto those tiles.
 */
public class FreeTile extends Tile {

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
	public boolean isCleared() {
		return false;
	}

	@Override
	public boolean isInventoried() {
		return false;
	}

	@Override
	public String getImageName() {
		return "free.png";
	}

	@Override
	public String code() {
		return "_";
	}
}
