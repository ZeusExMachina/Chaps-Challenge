package nz.ac.vuw.ecs.swen225.gp20.render;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Creates and displays chaps inventory
 */
public class Inventory extends JPanel {
  /**
   * ArrayList of image of objects that are in chaps inventory.
   */
  ArrayList<BufferedImage> inventoryImages;
  
  /**
   * Constructor, sets the preferred size.
   */
  
  public Inventory(){
    this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),"Inventory" ));
    this.setPreferredSize(new Dimension(192,128));
  }
  
  /**
   * Overrides the JComponent Paint method (Do not call this method)
   * @param g - Graphics component to draw with.
   */
  public void paint(Graphics g){
    if(inventoryImages == null)return;
    int xIndex = 0;
    int yIndex = 0;
    for (BufferedImage image : inventoryImages) {
      g.drawImage(image, xIndex, yIndex, this);
      int TILE_SIZE = 64;
      if(xIndex == 3* TILE_SIZE){
        xIndex=0;
        yIndex+= TILE_SIZE;
      }else xIndex+= TILE_SIZE;
    }
    
  }
  
  /**
   * Set the images to display.
   * @param inventoryImages - images of whats in chaps inventory.
   */
  public void setInventoryImages(ArrayList<BufferedImage> inventoryImages) {
    this.inventoryImages = inventoryImages;
    display();
  }
  
  /**
   * Display the Inventory images.
   */
  public void display() {
    this.repaint();
  }
}
