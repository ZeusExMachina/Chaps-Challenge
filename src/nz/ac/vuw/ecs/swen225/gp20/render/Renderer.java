package nz.ac.vuw.ecs.swen225.gp20.render;

import nz.ac.vuw.ecs.swen225.gp20.maze.Direction;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class the controls the display and information sent to
 * the Canvas and Inventory classes.
 */
public class Renderer {
  /**
   * The current instance of Renderer.
   */
  private static final Renderer instance = new Renderer();
  /**
   * The current Maze instance, needed to send game information to
   * the canvas and inventory.
   */
  private Maze maze;
  /**
   * The current instance of the Canvas being used.
   */
  private final Canvas canvas;
  /**
   * The current instance of the Inventory being used.
   */
  private final Inventory inventory;
  
  /**
   * Private constructor, creates the Canvas and Inventory instances to
   * be used throughout the project.
   */
  private Renderer() {
    canvas = new Canvas();
    inventory = new Inventory();
    BufferedImage defaultImage = null;
    try {
      defaultImage = ImageIO.read(new File("resources/wall.png"));
    } catch (IOException e) {
      System.out.println("Could not find 'wall.png' in resources");
    }
    canvas.setDefaultImage(defaultImage);
  }
  
  /**
   * Returns the current instance of the Renderer.
   * @return instance of the renderer.
   */
  public static Renderer getInstance() {
    return instance;
  }
  
  /**
   * Sets the Maze to be used for getting game information from.
   * @param m - Maze instance.
   */
  public void setMaze(Maze m){
    this.maze = m;
    canvas.setOrigin(maze.getChapPosition());
  }
  
  /**
   * Get the current Canvas instance.
   * @return current Canvas instance.
   */
  public Canvas getCanvas(){
    return canvas;
  }
  
  /**
   * Get the current Inventory instance.
   * @return current Inventory instance.
   */
  public Inventory getInventory(){
    return inventory;
  }
  
  /**
   * Updates both the Canvas and Inventory instances to display
   * the correct images and information.
   * @param d - The direction chap last moved.
   */
  public void update(Direction d){
    canvas.changeOrigin(d);
    ArrayList<BufferedImage> imageInventory = new ArrayList<>(maze.getInventory());
    inventory.setInventoryImages(imageInventory);
    display();
  }
  
  /**
   * Displays the canvas and the inventory.
   */
  public void display() {
    try{
      canvas.display(maze.getImages(), maze.getChapImage());
    }catch(IOException e){
      System.out.println("Couldn't find chap image.");
    }
    inventory.display();
    
  }
}
