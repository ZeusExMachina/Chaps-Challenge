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
	/**
	 * the images to be displayed on the screen.
	 */
	private BufferedImage[][] board;
	/**
	 * number of columns to be displayed on the screen
	 * at one time.
	 */
	private final int NUM_COLS = 9;
	/**
	 * number of rows to be displayed on the screen at one time.
	 */
	private final int NUM_ROWS = 9;
	/**
	 * The width and height of each tile.
	 */
	private final int TILE_SIZE = 64;
	/**
	 * The position the display should be centered around the board.
	 */
	private Position origin;
	/**
	 * The image to display when the display runs of the side
	 * of the board.
	 */
	private BufferedImage defaultImage;
	/**
	 * The image of chap that (which direction he is facing).
	 */
	private BufferedImage chapImage;
	/**
	 * the X and Y offset for where the center of the screen is.
	 */
	private final int centerOffset = 4;
	/**
	 * Whether the game has started or not. Stops class trying
	 * to display when things haven't been initialised yet.
	 */
	private boolean isGameStarted = false;

	/**
	 * Constructor, sets the size and allows it to be focusable.
	 */
	public Canvas() {
		this.setPreferredSize(new Dimension(576, 576));
		this.setFocusable(true);
		this.requestFocus();
	}
	
	/**
	 * Sets the default image to be displayed, if no image is found.
	 * @param b - BufferedImage to be set.
	 */
	public void setDefaultImage(BufferedImage b){
		this.defaultImage = b;
	}
	
	/**
	 * Sets the origin for where the display should be centered.
	 * @param origin - position where chap is.
	 */
	public void setOrigin(Position origin) {
		this.origin = origin;
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
		g.drawImage(chapImage, centerOffset*TILE_SIZE, centerOffset*TILE_SIZE, this);
	}
	
	/**
	 * Displays the board to the screen.
	 * @param b -  2Dimensional array of images to be displayed.
	 * @param chap - Image of chap to display.
	 */
	public void display(BufferedImage[][] b, BufferedImage chap) {
		isGameStarted = true;
		this.board = b;
		this.chapImage = chap;
		this.repaint();
	}

	/**
	 * Sets the origin (the position on the board where chap is)
	 * and makes sure that the x and y coords are in bounds.
	 *
	 * @param d direction to move
	 */
	public void changeOrigin(Direction d) {
		int maxXY = board.length-1;
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
