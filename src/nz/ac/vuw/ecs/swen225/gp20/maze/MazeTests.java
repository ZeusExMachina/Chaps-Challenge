package nz.ac.vuw.ecs.swen225.gp20.maze;

import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite ensuring game logic is sound.
 */
public class MazeTests {

	/**
	 * Construct a new Maze with level loaded.
	 * @return maze instance
	 */
	private Maze getMaze() {
		String[] in = {
				"/////////",
				"/d#/@/#c/",
				"//A/X/B//",
				"_C__!__D_",
				"#/b_?_a/#",
				"/////////"
		};
		Maze m = Maze.getInstance();
		String[] help = {"Unit tests"};
		m.loadLevel(in, help);
		return m;
	}

	/**
	 * Ensure maze can be set up with all kinds of basic tiles.
	 */
	@Test
	public void test01_setUpMaze() {
		Maze m = getMaze();
		String expected = "/////////\n" +
						  "/d#/@/#c/\n" +
						  "//A/X/B//\n" +
						  "_C__!__D_\n" +
						  "#/b_?_a/#\n" +
						  "/////////\n";
		assertEquals(expected, m.toString());
		assertEquals("(4,3)", m.getChapPosition().toString());
	}

	/**
	 * Ensure Chap can move south.
	 */
	@Test
	public void test02_moveChapSouth() {
		Maze m = getMaze();
		assertTrue(m.moveChap(Direction.SOUTH));
		String expected = "/////////\n" +
						  "/d#/@/#c/\n" +
						  "//A/X/B//\n" +
						  "_C_____D_\n" +
						  "#/b_!_a/#\n" +
						  "/////////\n";
		assertEquals(expected, m.toString());
	}

	/**
	 * Ensure Chap can move south and north, resulting in the help tile persisting
	 */
	@Test
	public void test03_moveChapBack() {
		Maze m = getMaze();
		assertTrue(m.moveChap(Direction.SOUTH));
		assertTrue(m.moveChap(Direction.NORTH));
		String expected = "/////////\n" +
						  "/d#/@/#c/\n" +
						  "//A/X/B//\n" +
						  "_C__!__D_\n" +
						  "#/b_?_a/#\n" +
						  "/////////\n";
		assertEquals(expected, m.toString());
	}

	/**
	 * Ensure Chap can't move north due to exit lock.
	 */
	@Test
	public void test04_moveChapNorth() {
		Maze m = getMaze();
		assertFalse(m.moveChap(Direction.NORTH));
		String expected = "/////////\n" +
						  "/d#/@/#c/\n" +
						  "//A/X/B//\n" +
						  "_C__!__D_\n" +
						  "#/b_?_a/#\n" +
						  "/////////\n";
		assertEquals(expected, m.toString());
	}

	/**
	 * Ensure Chap can pick up a key.
	 */
	@Test
	public void test05_pickUp() {
		Maze m = getMaze();
		assertTrue(m.moveChap(Direction.WEST));
		assertTrue(m.moveChap(Direction.WEST));
		assertTrue(m.moveChap(Direction.SOUTH));
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.containsKey(KeyTile.Colour.GREEN));
		String expected = "/////////\n" +
						  "/d#/@/#c/\n" +
						  "//A/X/B//\n" +
						  "_C_____D_\n" +
						  "#/_!?_a/#\n" +
						  "/////////\n";
		assertEquals(expected, m.toString());
	}

	/**
	 * Ensure Chap can unlock a door if they've picked up the right key.
	 */
	@Test
	public void test06_unlock() {
		Maze m = getMaze();
		assertTrue(m.moveChap(Direction.WEST));
		assertTrue(m.moveChap(Direction.WEST));
		assertTrue(m.moveChap(Direction.SOUTH));
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.moveChap(Direction.NORTH));
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.containsKey(KeyTile.Colour.GREEN));
		assertTrue(m.moveChap(Direction.NORTH));
		assertFalse(m.containsKey(KeyTile.Colour.GREEN));
		String expected = "/////////\n" +
						  "/d#/@/#c/\n" +
						  "//A/X/!//\n" +
						  "_C_____D_\n" +
						  "#/__?_a/#\n" +
						  "/////////\n";
		assertEquals(expected, m.toString());
	}

	/**
	 * Ensure Chap can't unlock a door without the right key.
	 */
	@Test
	public void test07_noUnlock() {
		Maze m = getMaze();
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.moveChap(Direction.EAST));
		assertFalse(m.moveChap(Direction.NORTH));
		String expected = "/////////\n" +
						  "/d#/@/#c/\n" +
						  "//A/X/B//\n" +
						  "_C____!D_\n" +
						  "#/b_?_a/#\n" +
						  "/////////\n";
		assertEquals(expected, m.toString());
	}

	/**
	 * Ensure Chap can unlock a door if they've picked up the right key.
	 */
	@Test
	public void test08_getTreasure() {
		Maze m = getMaze();
		assertTrue(m.moveChap(Direction.WEST));
		assertTrue(m.moveChap(Direction.WEST));
		assertTrue(m.moveChap(Direction.SOUTH));
		assertTrue(m.containsKey(KeyTile.Colour.GREEN));
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.moveChap(Direction.NORTH));
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.moveChap(Direction.NORTH));
		assertEquals(m.getTreasuresLeft(), 4);
		assertTrue(m.moveChap(Direction.NORTH));
		assertEquals(m.getTreasuresLeft(), 3);
		String expected = "/////////\n" +
						  "/d#/@/!c/\n" +
						  "//A/X/_//\n" +
						  "_C_____D_\n" +
						  "#/__?_a/#\n" +
						  "/////////\n";
		assertEquals(expected, m.toString());
	}

	/**
	 * Ensure Chap can finish the level.
	 */
	@Test
	public void test09_finishLevel() {
		Maze m = getMaze();
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
		assertEquals(2, m.countTypesInInventory(KeyTile.class));
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
		assertTrue(m.moveChap(Direction.WEST));
		assertTrue(m.moveChap(Direction.SOUTH)); // get treasure
		assertTrue(m.moveChap(Direction.NORTH));
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.moveChap(Direction.EAST)); // unlock D
		assertTrue(m.moveChap(Direction.EAST));
		assertTrue(m.moveChap(Direction.SOUTH)); // get treasure
		assertTrue(m.moveChap(Direction.NORTH));
		assertTrue(m.moveChap(Direction.WEST));
		assertTrue(m.moveChap(Direction.WEST));
		assertTrue(m.moveChap(Direction.WEST));
		assertTrue(m.moveChap(Direction.WEST));
		assertTrue(m.moveChap(Direction.NORTH)); // unlock exit lock
		assertFalse(m.isLevelDone());
		assertTrue(m.moveChap(Direction.NORTH));
		assertTrue(m.isLevelDone());
		assertEquals(0, m.countTypesInInventory(DoorTile.class));
		assertEquals(0, m.countTypesInInventory(ExitLockTile.class));
		assertEquals(0, m.countTypesInInventory(KeyTile.class));
		assertEquals(0, m.countTypesInInventory(TreasureTile.class));
		try {
			m.removeKey(KeyTile.Colour.BLUE);
		} catch (AssertionError ignored) {
			// OK
		}
		String expected = "/////////\n" +
						  "/__/!/__/\n" +
						  "//_/_/_//\n" +
						  "_________\n" +
						  "_/__?__/_\n" +
						  "/////////\n";
		assertEquals(expected, m.toString());
	}

	/**
	 * Attempt to parse unknown character ('$') to maze
	 */
	@Test
	public void test10_badSetup() {
		String[] in = {
				"/////////",
				"/d#/$/#c/",
				"//A/X/B//",
				"_C__!__D_",
				"#/b_?_a/#",
				"/////////"
		};
		String[] help = {"Unit tests"};
		try {
			Maze.getInstance().loadLevel(in, help);
		} catch (IllegalArgumentException ignored) {
			// OK
		}
	}

	/**
	 * Attempt to parse board without Chap
	 */
	@Test
	public void test11_badSetup() {
		String[] in = {
				"/////////",
				"/d#/@/#c/",
				"//A/X/B//",
				"_C_____D_",
				"#/b_?_a/#",
				"/////////"
		};
		String[] help = {"Unit tests"};
		try {
			Maze.getInstance().loadLevel(in, help);
		} catch (AssertionError e) {
			return;
		}
		fail("Exception expected");
	}

	/**
	 * Attempt to parse board without exit
	 */
	@Test
	public void test12_badSetup() {
		String[] in = {
				"/////////",
				"/d#/_/#c/",
				"//A/X/B//",
				"_C__!__D_",
				"#/b_?_a/#",
				"/////////"
		};
		String[] help = {"Unit tests"};
		try {
			Maze.getInstance().loadLevel(in, help);
		} catch (AssertionError ignored) {
			return;
		}
		fail("Exception expected");
	}

	/**
	 * Attempt to move to wall
	 */
	@Test
	public void test13_movetoWall() {
		Maze m = getMaze();
		assertTrue(m.moveChap(Direction.EAST));
		assertFalse(m.moveChap(Direction.NORTH));
	}

	/**
	 * Set the text of the help tile.
	 */
	@Test
	public void test14_setHelp() {
		Maze m = getMaze();
		assertTrue(m.moveChap(Direction.SOUTH));
		assertEquals("Unit tests", m.isOnHelp());
	}

	/**
	 * Ensure no help text is displayed when not on a help tile
	 */
	@Test
	public void test15_noHelp() {
		Maze m = getMaze();
		assertNull(m.isOnHelp());
	}

	/**
	 * Set the text of the help tile.
	 */
	@Test
	public void test16_badHelp() {
		Maze m = getMaze();
		try {
			m.setHelp(1, "Help!");
		} catch (IllegalArgumentException e) {
			return;
		}
		fail("Exception expected");
	}

	/**
	 * Runs all getImage methods in all Tile classes to ensure they exist.
	 */
	@Test
	public void test17_checkImages() {
		Maze m = getMaze();
		BufferedImage[][] images = m.getImages();
		for (BufferedImage[] row : images) {
			for (BufferedImage i : row) {
				assertNotNull(i);
			}
		}
		Actor chap = new Chap(new Position(0,0), "chap");
		assertNotNull(chap.getImage());
//		for (KeyTile.Colour c : KeyTile.Colour.values()) {
//			DoorTile d = new DoorTile((char) ('A'+c.ordinal()), 0, 0);
//			assertNotNull(d.getImage());
//		}
//		ExitLockTile l = new ExitLockTile( 0, 0);
//		assertNotNull(l.getImage());
//		ExitTile e = new ExitTile( 0, 0);
//		assertNotNull(e.getImage());
//		FreeTile f = new FreeTile(0,0);
//		assertNotNull(f.getImage());
//		HelpTile h = new HelpTile(0, 0);
//		assertNotNull(h.getImage());
//		for (KeyTile.Colour c : KeyTile.Colour.values()) {
//			KeyTile k = new KeyTile((char) ('a'+c.ordinal()), 0, 0);
//			assertNotNull(k.getImage());
//		}
//		TreasureTile t = new TreasureTile(0, 0);
//		assertNotNull(t.getImage());
//		WallTile w = new WallTile(0, 0);
//		assertNotNull(w.getImage());
	}

	/**
	 * Test that loading secondary actors work and collision detection against Chap works
	 */
	@Test
	public void test18_secondaryActors(){
		String[] in = {
				"/////////",
				"/d#/@/#c/",
				"//A/X/B//",
				"//_____//",
				"_C__!__D_",
				"//_____//",
				"#/b_?_a/#",
				"/////////"
		};
		Maze m = Maze.getInstance();
		String[] help = {"Unit tests"};
		m.loadLevel(in, help);
		List<Direction> path = Arrays.asList(Direction.EAST,
				Direction.EAST,
				Direction.WEST,
				Direction.WEST);
		Actor roach1 = new Monster(new Position(3,3), "roach", path);
		Actor roach2 = new Monster(new Position(3,5), "roach", path);
		m.addSecondaryActor(roach1);
		m.addSecondaryActor(roach2);
		m.moveSecondaryActors();
		assertTrue(m.isChapAlive());
		m.moveChap(Direction.SOUTH);
		assertFalse(m.isChapAlive());
	}

	/**
	 * Test that actors without resources fail properly
	 */
	@Test
	public void test19_badActor(){
		Actor a = new Monster(new Position(3,3), "roach", Arrays.asList(Direction.EAST, Direction.EAST));
		try {
			assertNull(a.getImage());
		} catch (RuntimeException e) {
			return;
		}
		fail("Exception expected");
	}
}
