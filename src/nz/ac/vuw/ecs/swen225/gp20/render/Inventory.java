package nz.ac.vuw.ecs.swen225.gp20.render;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Inventory extends JPanel {
  BufferedImage defaultImage;
  private final int TILE_SIZE = 64;
  ArrayList<BufferedImage> inventoryImages;
  public Inventory(){
    this.setPreferredSize(new Dimension(192,128));
    System.out.println("Width: " + this.getWidth() +"\nHeight: " + this.getHeight());
  }
  
  public void paint(Graphics g){
    if(inventoryImages == null)return;
    int xIndex = 0;
    int yIndex = 0;
    for (BufferedImage image : inventoryImages) {
      g.drawImage(image, xIndex, yIndex, this);
      if(xIndex == 3*TILE_SIZE){
        xIndex=0;
        yIndex+=TILE_SIZE;
      }else xIndex+=TILE_SIZE;
    }
    
  }
  public void setDefaultImage(BufferedImage image){
    defaultImage = image;
    display();
  }
  
  public void setInventoryImages(ArrayList<BufferedImage> inventoryImages) {
    this.inventoryImages = inventoryImages;
    display();
  }
  
  public void display() {
    this.repaint();
  }
}
