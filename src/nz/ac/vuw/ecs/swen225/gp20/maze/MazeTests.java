package nz.ac.vuw.ecs.swen225.gp20.maze;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite ensuring game logic is sound.
 */
public class MazeTests {

	/**
	 * Ensure maze can be set up with all kinds of basic tiles.
	 */
	@Test
	public void test01_setUpMaze() {
		String[] in = {
				"/////////",
				"/d#/@/#c/",
				"//A/X/B//",
				" C  !  D ",
				"#/b ? a/#",
				"/////////"
		};
		Maze m = new Maze(in);
		String expected = "|/|/|/|/|/|/|/|/|/|\n" +
						  "|/|d|#|/|@|/|#|c|/|\n" +
						  "|/|/|A|/|X|/|B|/|/|\n" +
						  "| |C| | |!| | |D| |\n" +
						  "|#|/|b| |?| |a|/|#|\n" +
						  "|/|/|/|/|/|/|/|/|/|\n";
		assertEquals(expected, m.toString());
	}

	/**
	 * Ensure Chap can move south.
	 */
	@Test
	public void test02_moveChapSouth() {
		String[] in = {
				"/////////",
				"/d#/@/#c/",
				"//A/X/B//",
				" C  !  D ",
				"#/b ? a/#",
				"/////////"
		};
		Maze m = new Maze(in);
		assertTrue(m.moveChap(Direction.SOUTH));
		String expected = "|/|/|/|/|/|/|/|/|/|\n" +
						  "|/|d|#|/|@|/|#|c|/|\n" +
						  "|/|/|A|/|X|/|B|/|/|\n" +
						  "| |C| | | | | |D| |\n" +
						  "|#|/|b| |!| |a|/|#|\n" +
						  "|/|/|/|/|/|/|/|/|/|\n";
		assertEquals(expected, m.toString());
	}

	/**
	 * Ensure Chap can move south and north, resulting in the help tile persisting
	 */
	@Test
	public void test03_moveChapBack() {
		String[] in = {
				"/////////",
				"/d#/@/#c/",
				"//A/X/B//",
				" C  !  D ",
				"#/b ? a/#",
				"/////////"
		};
		Maze m = new Maze(in);
		assertTrue(m.moveChap(Direction.SOUTH));
		assertTrue(m.moveChap(Direction.NORTH));
		String expected = "|/|/|/|/|/|/|/|/|/|\n" +
						  "|/|d|#|/|@|/|#|c|/|\n" +
						  "|/|/|A|/|X|/|B|/|/|\n" +
						  "| |C| | |!| | |D| |\n" +
						  "|#|/|b| |?| |a|/|#|\n" +
						  "|/|/|/|/|/|/|/|/|/|\n";
		assertEquals(expected, m.toString());
	}

	/**
	 * Ensure Chap can't move north due to exit lock.
	 */
	@Test
	public void test04_moveChapNorth() {
		String[] in = {
				"/////////",
				"/d#/@/#c/",
				"//A/X/B//",
				" C  !  D ",
				"#/b ? a/#",
				"/////////"
		};
		Maze m = new Maze(in);
		assertFalse(m.moveChap(Direction.NORTH));
		String expected = "|/|/|/|/|/|/|/|/|/|\n" +
						  "|/|d|#|/|@|/|#|c|/|\n" +
						  "|/|/|A|/|X|/|B|/|/|\n" +
						  "| |C| | |!| | |D| |\n" +
						  "|#|/|b| |?| |a|/|#|\n" +
						  "|/|/|/|/|/|/|/|/|/|\n";
		assertEquals(expected, m.toString());
	}

	/**
	 * Ensure Chap can pick up a key.
	 */
	@Test
	public void test05_pickUp() {
		String[] in = {
				"/////////",
				"/d#/@/#c/",
				"//A/X/B//",
				" C  !  D ",
				"#/b ? a/#",
				"/////////"
		};
		Maze m = new Maze(in);
		assertTrue(m.moveChap(Direction.WEST));
		assertTrue(m.moveChap(Direction.WEST));
		assertTrue(m.moveChap(Direction.SOUTH));
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.containsKey(KeyTile.Colour.COLOUR_TWO));
		String expected = "|/|/|/|/|/|/|/|/|/|\n" +
						  "|/|d|#|/|@|/|#|c|/|\n" +
						  "|/|/|A|/|X|/|B|/|/|\n" +
						  "| |C| | | | | |D| |\n" +
						  "|#|/| |!|?| |a|/|#|\n" +
						  "|/|/|/|/|/|/|/|/|/|\n";
		assertEquals(expected, m.toString());
	}

	/**
	 * Ensure Chap can unlock a door if they've picked up the right key.
	 */
	@Test
	public void test06_unlock() {
		String[] in = {
				"/////////",
				"/d#/@/#c/",
				"//A/X/B//",
				" C  !  D ",
				"#/b ? a/#",
				"/////////"
		};
		Maze m = new Maze(in);
		assertTrue(m.moveChap(Direction.WEST));
		assertTrue(m.moveChap(Direction.WEST));
		assertTrue(m.moveChap(Direction.SOUTH));
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.moveChap(Direction.NORTH));
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.moveChap(Direction.NORTH));
		assertTrue(m.containsKey(KeyTile.Colour.COLOUR_TWO));
		String expected = "|/|/|/|/|/|/|/|/|/|\n" +
						  "|/|d|#|/|@|/|#|c|/|\n" +
						  "|/|/|A|/|X|/|!|/|/|\n" +
						  "| |C| | | | | |D| |\n" +
						  "|#|/| | |?| |a|/|#|\n" +
						  "|/|/|/|/|/|/|/|/|/|\n";
		assertEquals(expected, m.toString());
	}

	/**
	 * Ensure Chap can't unlock a door without the right key.
	 */
	@Test
	public void test07_noUnlock() {
		String[] in = {
				"/////////",
				"/d#/@/#c/",
				"//A/X/B//",
				" C  !  D ",
				"#/b ? a/#",
				"/////////"
		};
		Maze m = new Maze(in);
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.moveChap(Direction.EAST));
		assertFalse(m.moveChap(Direction.NORTH));
		String expected = "|/|/|/|/|/|/|/|/|/|\n" +
						  "|/|d|#|/|@|/|#|c|/|\n" +
						  "|/|/|A|/|X|/|B|/|/|\n" +
						  "| |C| | | | |!|D| |\n" +
						  "|#|/|b| |?| |a|/|#|\n" +
						  "|/|/|/|/|/|/|/|/|/|\n";
		assertEquals(expected, m.toString());
	}

	/**
	 * Ensure Chap can unlock a door if they've picked up the right key.
	 */
	@Test
	public void test08_getTreasure() {
		String[] in = {
				"/////////",
				"/d#/@/#c/",
				"//A/X/B//",
				" C  !  D ",
				"#/b ? a/#",
				"/////////"
		};
		Maze m = new Maze(in);
		assertTrue(m.moveChap(Direction.WEST));
		assertTrue(m.moveChap(Direction.WEST));
		assertTrue(m.moveChap(Direction.SOUTH));
		assertTrue(m.containsKey(KeyTile.Colour.COLOUR_TWO));
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.moveChap(Direction.NORTH));
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.moveChap(Direction.NORTH));
		assertEquals(m.getTreasuresLeft(), 4);
		assertTrue(m.moveChap(Direction.NORTH));
		assertEquals(m.getTreasuresLeft(), 3);
		String expected = "|/|/|/|/|/|/|/|/|/|\n" +
						  "|/|d|#|/|@|/|!|c|/|\n" +
						  "|/|/|A|/|X|/| |/|/|\n" +
						  "| |C| | | | | |D| |\n" +
						  "|#|/| | |?| |a|/|#|\n" +
						  "|/|/|/|/|/|/|/|/|/|\n";
		assertEquals(expected, m.toString());
	}

	/**
	 * Ensure Chap can finish the level.
	 */
	@Test
	public void test09_finishLevel() {
		String[] in = {
				"/////////",
				"/d#/@/#c/",
				"//A/X/B//",
				"#C  !  D#",
				" /b ? a/ ",
				"/////////"
		};
		Maze m = new Maze(in);
		assertTrue(m.moveChap(Direction.WEST));
		assertTrue(m.moveChap(Direction.WEST));
		assertTrue(m.moveChap(Direction.SOUTH)); // get key to B
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.moveChap(Direction.NORTH));
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.moveChap(Direction.NORTH)); // unlock B
		assertTrue(m.moveChap(Direction.NORTH)); // get treasure
		assertTrue(m.moveChap(Direction.EAST));  // get key to C
		assertTrue(m.moveChap(Direction.WEST));
		assertTrue(m.moveChap(Direction.SOUTH));
		assertTrue(m.moveChap(Direction.SOUTH));
		assertTrue(m.moveChap(Direction.SOUTH)); // get key to A
		assertTrue(m.moveChap(Direction.WEST));
		assertTrue(m.moveChap(Direction.WEST));
		assertTrue(m.moveChap(Direction.WEST));
		assertTrue(m.moveChap(Direction.WEST));
		assertTrue(m.moveChap(Direction.NORTH));
		assertTrue(m.moveChap(Direction.NORTH)); // unlock A
		assertTrue(m.moveChap(Direction.NORTH)); // get treasure
		assertTrue(m.moveChap(Direction.WEST)); // get key to D
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.moveChap(Direction.SOUTH));
		assertTrue(m.moveChap(Direction.SOUTH));
		assertTrue(m.moveChap(Direction.WEST)); // unlock C
		assertTrue(m.moveChap(Direction.WEST)); // get treasure
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.moveChap(Direction.EAST)); // unlock D
		assertTrue(m.moveChap(Direction.EAST)); // get treasure
		assertTrue(m.moveChap(Direction.WEST));
		assertTrue(m.moveChap(Direction.WEST));
		assertTrue(m.moveChap(Direction.WEST));
		assertTrue(m.moveChap(Direction.WEST));
		assertTrue(m.moveChap(Direction.NORTH)); // unlock exit lock
		assertTrue(m.moveChap(Direction.NORTH));
		assertTrue(m.isLevelDone());
		String expected = "|/|/|/|/|/|/|/|/|/|\n" +
						  "|/| | |/|!|/| | |/|\n" +
						  "|/|/| |/| |/| |/|/|\n" +
						  "| | | | | | | | | |\n" +
						  "| |/| | |?| | |/| |\n" +
						  "|/|/|/|/|/|/|/|/|/|\n";
		assertEquals(expected, m.toString());
	}
}
