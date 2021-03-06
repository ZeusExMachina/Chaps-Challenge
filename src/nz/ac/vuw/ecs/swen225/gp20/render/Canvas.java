package nz.ac.vuw.ecs.swen225.gp20.render;

import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.maze.Position;
import nz.ac.vuw.ecs.swen225.gp20.maze.WallTile;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Class to display and handle the drawing of the board and actors.
 *
 * @author Devon Gregory 300414962
 */
public class Canvas extends JPanel {
	/**
	 * TODO
	 */
	private Maze maze;
	/**
	 * The position the display should be centered around the board.
	 */
	private Position origin;
	/**
	 * Whether the game has started or not. Stops class trying
	 * to display when things haven't been initialised yet.
	 */
	private boolean isGameStarted = false;
	private BufferedImage defaultImage = null;
	private BufferedImage chapImage = null;
	
	private int centerOffset = 4;

	/**
	 * Constructor, sets the size and allows it to be focusable.
	 */
	Canvas() {
		this.setPreferredSize(new Dimension(576, 576));
		this.setFocusable(true);
		this.requestFocus();
	}

	/**
	 * Override of the original JPanel paint, draws the board.
	 *
	 * @param g - Graphics object (do not call this method)
	 */
	 public void paint(Graphics g) {
		if (!isGameStarted) return;

		BufferedImage[][] board = maze.getImages();
		int xIndex = origin.getX() - centerOffset;
		int yIndex = origin.getY() - centerOffset;
		int NUM_COLS = 9;
		int TILE_SIZE = 64;
		for (int xPlace = 0; xPlace < NUM_COLS * TILE_SIZE; xPlace += TILE_SIZE) {
			int NUM_ROWS = 9;
			for (int yPlace = 0; yPlace < NUM_ROWS * TILE_SIZE; yPlace += TILE_SIZE) {
				try {
					g.drawImage(board[yIndex][xIndex], xPlace, yPlace, this);
				} catch (ArrayIndexOutOfBoundsException e) {
					g.drawImage(defaultImage, xPlace, yPlace, this);
				}
				yIndex++;
			}
			xIndex++;
			yIndex = origin.getY() - centerOffset;
		}
		g.drawImage(chapImage, centerOffset * TILE_SIZE, centerOffset * TILE_SIZE, this);
		drawHelpText(g);
	}

	/**
	 * Draws the help text when chap is standing on a help tile.
	 * @param g - Graphics element to draw on.
	 */
	private void drawHelpText(Graphics g){
		String help = maze.isOnHelp();
		int line = 1;
		if (help != null) {
			g.setColor(new Color(70,70,70,190));
			int numLines  = help.length() - help.replace("\n", "").length();
			g.fillRect(30,150, this.getWidth() - 60,  50+numLines*50);
			g.setColor(Color.WHITE);
			g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
			while (help.length() > 0) { // split up help string so it doesn't go off screen
				int split = help.indexOf('\n');
				if (split == -1) split = help.length();

				String toDisplay = help.substring(0, split);
				if (split < help.length()) help = help.substring(split+1);
				else throw new IllegalArgumentException("Help text should have a \n at the end.");
				g.drawString(toDisplay, 50, 150+50*line);
				line++;
			}
		}
	}

	/**
	 * Displays the board to the screen.
	 * @param maze - the maze instance to get the information from
	 */
	void display(Maze m) {
		isGameStarted = true;
		maze = m;
		defaultImage = new WallTile(0, 0).getImage();
		chapImage = maze.getChapImage();
		origin = maze.getChapPosition();
		this.repaint();
	}
	
	/**
	 * returns true if the x and y position
	 * are currently being displayed on screen.
	 * @param x - the x coordinate of the tile.
	 * @param y - the y coordinate of the tile.
	 * @return true if position is on screen.
	 */
	boolean isPositionOnScreen(int x, int y) {
		return (x > origin.getX() - centerOffset && x < origin.getX() + centerOffset && y < origin.getY() + centerOffset && y > origin.getY() - centerOffset);
	}


}
