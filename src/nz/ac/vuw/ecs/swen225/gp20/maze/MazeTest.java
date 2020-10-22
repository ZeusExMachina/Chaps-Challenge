package nz.ac.vuw.ecs.swen225.gp20.maze;

import nz.ac.vuw.ecs.swen225.gp20.persistence.ActorLoader;
import nz.ac.vuw.ecs.swen225.gp20.persistence.LevelLoader;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite ensuring game logic is sound.
 */
public class MazeTest {

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
		LevelLoader ll = new LevelLoader();
		ActorLoader al = ll.getActorLoader();

		String[] help = {"Unit tests"};
		Set<Actor> monsters = new HashSet<>();
		List<Direction> path = Arrays.asList(Direction.EAST, Direction.WEST);

		Actor a1 = al.createSecondaryActor(2, "1", "monster",
				new Position(2,3), path);
		Actor a2 = al.createSecondaryActor(2, "1", "monster",
				new Position(5,3), path);
		monsters.add(a1);
		m.loadLevel(in, help, monsters);
		m.addSecondaryActor(a2);
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
		assertNotNull(m.getChap());
		assertEquals("(4,3)", m.getChapPosition().toString());
	}

	/**
	 * Ensure Chap can move south.
	 */
	@Test
	public void test02_moveChapSouth() {
		Maze m = getMaze();
		assertNotNull(m.moveChap(Direction.SOUTH));
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
		assertNotNull(m.moveChap(Direction.SOUTH));
		assertNotNull(m.moveChap(Direction.NORTH));
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
		assertNull(m.moveChap(Direction.NORTH));
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
		assertNotNull(m.moveChap(Direction.WEST));
		assertNotNull(m.moveChap(Direction.WEST));
		assertNotNull(m.moveChap(Direction.SOUTH));
		assertNotNull(m.moveChap(Direction.EAST));
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
		assertNotNull(m.moveChap(Direction.WEST));
		assertNotNull(m.moveChap(Direction.WEST));
		assertNotNull(m.moveChap(Direction.SOUTH));
		assertNotNull(m.moveChap(Direction.EAST));
		assertNotNull(m.moveChap(Direction.NORTH));
		assertNotNull(m.moveChap(Direction.EAST));
		assertNotNull(m.moveChap(Direction.EAST));
		assertNotNull(m.moveChap(Direction.EAST));
		assertTrue(m.containsKey(KeyTile.Colour.GREEN));
		assertNotNull(m.moveChap(Direction.NORTH));
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
		assertNotNull(m.moveChap(Direction.EAST));
		assertNotNull(m.moveChap(Direction.EAST));
		assertNull(m.moveChap(Direction.NORTH));
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
		assertNotNull(m.moveChap(Direction.WEST));
		assertNotNull(m.moveChap(Direction.WEST));
		assertNotNull(m.moveChap(Direction.SOUTH));
		assertTrue(m.containsKey(KeyTile.Colour.GREEN));
		assertNotNull(m.moveChap(Direction.EAST));
		assertNotNull(m.moveChap(Direction.NORTH));
		assertNotNull(m.moveChap(Direction.EAST));
		assertNotNull(m.moveChap(Direction.EAST));
		assertNotNull(m.moveChap(Direction.EAST));
		assertNotNull(m.moveChap(Direction.NORTH));
		assertEquals(m.getTreasuresLeft(), 4);
		assertNotNull(m.moveChap(Direction.NORTH));
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
		assertNotNull(m.moveChap(Direction.WEST));
		assertNotNull(m.moveChap(Direction.WEST));
		assertNotNull(m.moveChap(Direction.SOUTH)); // get key to B
		assertNotNull(m.moveChap(Direction.EAST));
		assertNotNull(m.moveChap(Direction.NORTH));
		assertNotNull(m.moveChap(Direction.EAST));
		assertNotNull(m.moveChap(Direction.EAST));
		assertNotNull(m.moveChap(Direction.EAST));
		assertNotNull(m.moveChap(Direction.NORTH)); // unlock B
		assertNotNull(m.moveChap(Direction.NORTH)); // get treasure
		assertNotNull(m.moveChap(Direction.EAST));  // get key to C
		assertNotNull(m.moveChap(Direction.WEST));
		assertNotNull(m.moveChap(Direction.SOUTH));
		assertNotNull(m.moveChap(Direction.SOUTH));
		assertNotNull(m.moveChap(Direction.SOUTH)); // get key to A
		assertEquals(2, m.countTypesInInventory(KeyTile.class));
		assertNotNull(m.moveChap(Direction.WEST));
		assertNotNull(m.moveChap(Direction.WEST));
		assertNotNull(m.moveChap(Direction.WEST));
		assertNotNull(m.moveChap(Direction.WEST));
		assertNotNull(m.moveChap(Direction.NORTH));
		assertNotNull(m.moveChap(Direction.NORTH)); // unlock A
		assertNotNull(m.moveChap(Direction.NORTH)); // get treasure
		assertNotNull(m.moveChap(Direction.WEST)); // get key to D
		assertNotNull(m.moveChap(Direction.EAST));
		assertNotNull(m.moveChap(Direction.SOUTH));
		assertNotNull(m.moveChap(Direction.SOUTH));
		assertNotNull(m.moveChap(Direction.WEST)); // unlock C
		assertNotNull(m.moveChap(Direction.WEST));
		assertNotNull(m.moveChap(Direction.SOUTH)); // get treasure
		assertNotNull(m.moveChap(Direction.NORTH));
		assertNotNull(m.moveChap(Direction.EAST));
		assertNotNull(m.moveChap(Direction.EAST));
		assertNotNull(m.moveChap(Direction.EAST));
		assertNotNull(m.moveChap(Direction.EAST));
		assertNotNull(m.moveChap(Direction.EAST));
		assertNotNull(m.moveChap(Direction.EAST));
		assertNotNull(m.moveChap(Direction.EAST)); // unlock D
		assertNotNull(m.moveChap(Direction.EAST));
		assertNotNull(m.moveChap(Direction.SOUTH)); // get treasure
		assertNotNull(m.moveChap(Direction.NORTH));
		assertNotNull(m.moveChap(Direction.WEST));
		assertNotNull(m.moveChap(Direction.WEST));
		assertNotNull(m.moveChap(Direction.WEST));
		assertNotNull(m.moveChap(Direction.WEST));
		assertNotNull(m.moveChap(Direction.NORTH)); // unlock exit lock
		assertFalse(m.isLevelDone());
		assertNotNull(m.moveChap(Direction.NORTH));
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
			Maze.getInstance().loadLevel(in, help, new HashSet<>());
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
			Maze.getInstance().loadLevel(in, help, new HashSet<>());
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
			Maze.getInstance().loadLevel(in, help, new HashSet<>());
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
		assertNotNull(m.moveChap(Direction.EAST));
		assertNull(m.moveChap(Direction.NORTH));
	}

	/**
	 * Set the text of the help tile.
	 */
	@Test
	public void test14_setHelp() {
		Maze m = getMaze();
		assertNotNull(m.moveChap(Direction.SOUTH));
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
		assertNotNull(m.getChapImage());
		List<BufferedImage> inventory = m.getInventoryImages();
		for (BufferedImage i : inventory) {
			assertNotNull(i);
		}
	}

	/**
	 * Test that loading secondary actors work and collision detection against Chap works
	 */
	@Test
	public void test18_secondaryActors(){
		Maze m = getMaze();
		m.moveSecondaryActors();
		assertTrue(m.isChapAlive());
		m.moveChap(Direction.WEST);
		assertFalse(m.isChapAlive());
		assertEquals(2, m.getSecondaryActors().size());
	}
}
