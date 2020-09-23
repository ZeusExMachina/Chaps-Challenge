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
    //all of this is for debugging
   
   
    this.setFocusable(true);
    this.requestFocus();
//    this.addKeyListener((new KeyAdapter() {
//      @Override
//      public void keyPressed(KeyEvent e) {
//        super.keyPressed(e);
//        int x = origin.getX();
//        int y = origin.getY();
//        Direction dir;
//        switch (e.getKeyChar()) {
//          case 'w':
//            y--;
//            dir = Direction.NORTH;
//            break;
//          case 's':
//            y++;
//            dir = Direction.SOUTH;
//            break;
//          case 'a':
//            x--;
//            dir = Direction.WEST;
//            break;
//          case 'd':
//            x++;
//            dir = Direction.EAST;
//            break;
//          default:
//            dir = Direction.SOUTH;
//        }
//        chap.move(new Position(x, y), dir);
//        setOrigin(x, y);
//      }
//    }));
    String[] in = {
            "/////////",
            "/d#/@/#c/",
            "//A/X/B//",
            "#C__!__D#",
            "_/b_?_a/_",
            "//_____//",
            "/////////"
    };
    maze = new Maze(in);
    // removed board field
    origin = maze.getChapPosition();
    // removed chap field
    
    
    //debugging code finishes here.
    try {
      defaultImage = ImageIO.read(new File("resources/wall.png"));
    } catch (IOException e) {
      System.out.println("Could not find 'wall.png' in resources");
    }

  }

  
  /**
   * Override of the original JPanel paint, draws the board.
   *
   * @param g - Graphics object (do not call this method)
   */
  public void paint(Graphics g) {
    if(!isGameStarted)return;
    int xIndex = origin.getX() - centerOffset;
    int yIndex = origin.getY() - centerOffset;
    BufferedImage[][] board = maze.getImages();
    for (int xPlace = 0; xPlace < NUM_COLS * TILE_SIZE; xPlace += TILE_SIZE) {
      for (int yPlace = 0; yPlace < NUM_ROWS * TILE_SIZE; yPlace += TILE_SIZE) {
        try { // change image to draw from array
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
    // don't draw chap, chap is in image array
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
   *
//   * @param board - 2D array of tiles representing the board
//   * @param chap - The actor chap
//   *             Tile[][] board, Actor chap
   */
  public void display() {
    isGameStarted = true;
//    this.board = board;
//    this.chap = chap;
//    setOrigin(chap.getPosition().getX(), chap.getPosition().getY());
    this.repaint();
  }
  
  /**
   * Sets the origin (the position on the board where chap is)
   *  and makes sure that the x and y coords are in bounds.
   *
   * @param x - x position of where the origin is to be set.
   * @param y - y position of where the origin is to be set.
   */
  private void setOrigin(int x, int y) {
    int maxXY = maze.getImages().length - 1; // change from board
    int xPos = x;
    int yPos = y;
    if (xPos > maxXY) xPos = maxXY;
    if (xPos < 0) xPos = 0;
    if (yPos > maxXY) yPos = maxXY;
    if (yPos < 0) yPos = 0;
    this.origin = new Position(xPos, yPos);
    this.repaint();
  }
  
  
  
}
