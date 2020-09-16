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
}
