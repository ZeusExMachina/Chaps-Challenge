package nz.ac.vuw.ecs.swen225.gp20.render;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


/**
 * Class to display and handle the drawing of the board and actors.
 */
public class Canvas extends JPanel {
	private static final Canvas instance = new Canvas();
	private Maze maze; // formerly Tile[][]
	private final int NUM_COLS = 9;
	private final int NUM_ROWS = 9;
	private final int TILE_SIZE = 64;
	private Position origin;
	private BufferedImage defaultImage;
	private final int centerOffset = 4;
	//  private Actor chap;
	private boolean isGameStarted = false;

	/**
	 * private constructor as there should only be one instance of canvas at a time.
	 * Has a lot of code for debugging.
	 */
	private Canvas() {
		this.setPreferredSize(new Dimension(576, 576));
		this.setFocusable(true);
		this.requestFocus();

		try {
			defaultImage = ImageIO.read(new File("resources/wall.png"));
		} catch (IOException e) {
			System.out.println("Could not find 'wall.png' in resources");
		}

	}

	/**
	 * Set Maze object reference.
	 * @param m new maze
	 */
	public void setMaze(Maze m) {
		maze = m;
		origin = maze.getChapPosition();
	}

	/**
	 * Override of the original JPanel paint, draws the board.
	 *
	 * @param g - Graphics object (do not call this method)
	 */
	public void paint(Graphics g) {
		if (!isGameStarted) return;
		int xIndex = origin.getX() - centerOffset;
		int yIndex = origin.getY() - centerOffset;
		BufferedImage[][] board = maze.getImages();
		for (int xPlace = 0; xPlace < NUM_COLS * TILE_SIZE; xPlace += TILE_SIZE) {
			for (int yPlace = 0; yPlace < NUM_ROWS * TILE_SIZE; yPlace += TILE_SIZE) {
				try {
					BufferedImage imageToDraw = board[yIndex][xIndex];
					g.drawImage(imageToDraw, xPlace, yPlace, this);
				} catch (ArrayIndexOutOfBoundsException e) {
					g.drawImage(defaultImage, xPlace, yPlace, this);
				}
				yIndex++;
			}
			xIndex++;
			yIndex = origin.getY() - centerOffset;
		}
		try {
			g.drawImage(maze.getChapImage(), 4*TILE_SIZE, 4*TILE_SIZE, this);
		} catch (IOException e) {
			System.out.println("Chap image missing.");
		}
		String help = maze.isOnHelp();
		int line = 1;
		if (help != null) {
			g.setColor(Color.WHITE);
			g.setFont(g.getFont().deriveFont(g.getFont().getSize()*2F));
			while (help.length() > 0) { // split up help string so it doesn't go off screen
				int split = help.indexOf('\n');
				if (split == -1) split = help.length();

				String toDisplay = help.substring(0, split);
				help = help.substring(split+1);
				// TODO: change font, maybe monospace for retro feel
				g.drawString(toDisplay, 50, 150+50*line);
				line++;
			}
		}
	}

	/**
	 * Get the current instance of Canvas.
	 *
	 * @return - The current instance of Canvas.
	 */
	public static Canvas getInstance() {
		return instance;
	}

	/**
	 * Update and display the board.
	 * <p>
	 * //   * @param board - 2D array of tiles representing the board
	 * //   * @param chap - The actor chap
	 * //   *             Tile[][] board, Actor chap
	 */
	public void display() {
		isGameStarted = true;
		this.repaint();
	}

	/**
	 * Sets the origin (the position on the board where chap is)
	 * and makes sure that the x and y coords are in bounds.
	 *
	 * @param d direction to move
	 */
	public void changeOrigin(Direction d) {
		int maxXY = maze.getImages().length - 1; // change from board
		int xPos = origin.getX();
		int yPos = origin.getY();

		if (d.equals(Direction.NORTH)) yPos--;
		if (d.equals(Direction.SOUTH)) yPos++;
		if (d.equals(Direction.WEST)) xPos--;
		if (d.equals(Direction.EAST)) xPos++;

		if (xPos > maxXY) xPos = maxXY;
		if (xPos < 0) xPos = 0;
		if (yPos > maxXY) yPos = maxXY;
		if (yPos < 0) yPos = 0;
		this.origin = new Position(xPos, yPos);
		this.repaint();
	}

}
