package nz.ac.vuw.ecs.swen225.gp20.render;

import nz.ac.vuw.ecs.swen225.gp20.maze.Direction;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Renderer {
  private static Renderer instance = new Renderer();
  private Maze maze;
  private Canvas canvas;
  private Inventory inventory;
  
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
    inventory.setDefaultImage(defaultImage);
  }
  public static Renderer getInstance() {
    return instance;
  }
  
  public void setMaze(Maze m){
    this.maze = m;
    canvas.setOrigin(maze.getChapPosition());
  }
  public Canvas getCanvas(){
    return canvas;
  }
  public Inventory getInventory(){
    return inventory;
  }
  public void update(Direction d){
    canvas.changeOrigin(d);
    ArrayList<BufferedImage> imageInventory = new ArrayList<>(maze.getInventory());
    inventory.setInventoryImages(imageInventory);
    display();
  }
  public void display() {
    try{
      canvas.display(maze.getImages(), maze.getChapImage());
    }catch(IOException e){
      System.out.println("Couldn't find chap image.");
    }
    inventory.display();
    
  }
}
