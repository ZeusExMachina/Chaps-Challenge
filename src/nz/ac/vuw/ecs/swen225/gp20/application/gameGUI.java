package nz.ac.vuw.ecs.swen225.gp20.application;

import javax.swing.*;
import java.awt.*;

public class gameGUI {



    public gameGUI(){
        JFrame mainFrame = new JFrame("Chip's Challenge");
        mainFrame.setSize(900, 600);
        mainFrame.setVisible(true);
        mainFrame.setLayout(new GridBagLayout());
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.BOTH;

        JPanel map = new JPanel();
        map.setBackground(Color.red);

        //to remove, contents to be map
        JLabel tempLabel = new JLabel();
        tempLabel.setText("The map goes here");
        map.add(tempLabel);

        gc.weightx = 0.7;
        gc.weighty = 1;
        gc.gridx = 0;
        gc.gridy = 0;
        mainFrame.getContentPane().add(map, gc);

        JPanel controls = new JPanel();
        controls.setBorder(BorderFactory.createTitledBorder("Controls"));
        gc.weightx = 0.3;
        gc.weighty = 1;
        gc.gridx = 1;
        gc.gridy = 0;
        mainFrame.getContentPane().add(controls, gc);




    }
}
