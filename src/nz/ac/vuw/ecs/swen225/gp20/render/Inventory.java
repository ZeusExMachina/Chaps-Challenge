package nz.ac.vuw.ecs.swen225.gp20.render;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Inventory extends JPanel {
  private static Inventory instance = new Inventory();
  BufferedImage defaultImage;
  private  Inventory(){
    this.setPreferredSize(new Dimension(192,128));
    System.out.println("Width: " + this.getWidth() +"\nHeight: " + this.getHeight());
  }
  
  public void paint(Graphics g){
    if(defaultImage == null)return;
    g.drawImage(defaultImage, 0, 0, this);
  }
  public void setDefaultImage(BufferedImage image){
    defaultImage = image;
    this.repaint();
  }
  
  public static Inventory getInstance(){
    return instance;
  }

}
