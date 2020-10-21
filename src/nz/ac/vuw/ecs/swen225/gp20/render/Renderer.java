package nz.ac.vuw.ecs.swen225.gp20.render;

import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.maze.Position;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

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
   * the audio clip for the background music.
   */
  private Clip backgroundMusic;
  
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
   *
   * @return instance of the renderer.
   */
  public static Renderer getInstance() {
    return instance;
  }
  
  /**
   * Sets the Maze to be used for getting game information from.
   *
   * @param m - Maze instance.
   */
  public void setMaze(Maze m) {
    maze = m;
  }
  
  /**
   * Get the current Canvas instance.
   *
   * @return current Canvas instance.
   */
  public Canvas getCanvas() {
    return canvas;
  }
  
  /**
   * Get the current Inventory instance.
   *
   * @return current Inventory instance.
   */
  public Inventory getInventory() {
    return inventory;
  }
  
  /**
   * Updates both the Canvas and Inventory instances to display
   * the correct images and information.
   */
  public void update() {
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
  
  /**
   * starts the background music for the game.
   */
  public void startBackgroundMusic() {
    backgroundMusic = Renderer.playSound("main_game");
    FloatControl volume = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
    double gain = 0.25;
    float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
    volume.setValue(dB);
    backgroundMusic.loop(backgroundMusic.LOOP_CONTINUOUSLY);
  }
  
  /**
   * stops the background music for the game.
   */
  public void stopBackgroundMusic() {
    if(backgroundMusic != null) {
      backgroundMusic.stop();
    }
  }
  
  /**
   * Takes a file name and plays the corresponding sound.
   *
   * @param filename - name of audio file.
   * @return the clip that is being played.
   */
  public static Clip playSound(String filename) {
      try {
        Clip clip = AudioSystem.getClip();
        File audioFile = new File("resources/sounds/" + filename + ".wav");
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(audioFile.toURI().toURL());
        clip.open(audioIn);
        clip.start();
        return clip;
      } catch (Exception e) {
        System.err.println(e.getMessage());
      }
      return null;
  }
  
  /**
   * returns true if the given position is currently on the screen.
   *
   * @param pos - the position you want to check.
   * @return - true if the position is on the screen.
   */
  public boolean isPositionOnScreen(Position pos) {
    return canvas.isPositionOnScreen(pos.getX(), pos.getY());
  }
  
}
