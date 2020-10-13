package nz.ac.vuw.ecs.swen225.gp20.render;

import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;

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
    maze = m;
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
   */
  public void update(){
    inventory.setInventoryImages(maze.getInventoryImages());
    display();
  }
  
  /**
   * Displays the canvas and the inventory.
   */
  public void display() {
    canvas.display(maze);
    inventory.display();
  }
}
