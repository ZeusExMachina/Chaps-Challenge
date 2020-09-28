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
	private BufferedImage[][] board;
	private final int NUM_COLS = 9;
	private final int NUM_ROWS = 9;
	private final int TILE_SIZE = 64;
	private Position origin;
	private BufferedImage defaultImage;
	private BufferedImage chapImage;
	private final int centerOffset = 4;
	private boolean isGameStarted = false;

	/**
	 * private constructor as there should only be one instance of canvas at a time.
	 * Has a lot of code for debugging.
	 */
	public Canvas() {
		this.setPreferredSize(new Dimension(576, 576));
		this.setFocusable(true);
		this.requestFocus();
	}
	
	public void setDefaultImage(BufferedImage b){
		this.defaultImage = b;
	}
	
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
	 * Update and display the board.
	 * <p>
	 * //   * @param board - 2D array of tiles representing the board
	 * //   * @param chap - The actor chap
	 * //   *             Tile[][] board, Actor chap
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
		int maxXY = board.length-1; // change from board
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
