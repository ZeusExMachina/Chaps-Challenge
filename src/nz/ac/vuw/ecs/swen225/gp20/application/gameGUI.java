package nz.ac.vuw.ecs.swen225.gp20.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class gameGUI{



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

        mainFrame.addKeyListener(new KeyListener() {
            boolean control = false;
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_CONTROL) {
                    control = true;
                }else if(control){
                    controlKeyUse(e);
                }else{
                    singleKeyUse(e);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_CONTROL) control = false;
            }
        });



    }

    /**
     * calls key functions that require ctrl key to be pressed.
     * @param e - key event that is NOT control
     */
    public void controlKeyUse(KeyEvent e){
        int keyPressed = e.getKeyCode();

        switch (keyPressed){

            case KeyEvent.VK_X:
                System.out.println("Exit Game");
                break;
            case KeyEvent.VK_S:
                System.out.println("Save Game");
                break;
            case KeyEvent.VK_R:
                System.out.println("Resume Game");
                break;
            case KeyEvent.VK_P:
                System.out.println("Pause Game");
                break;
            case KeyEvent.VK_1:
                System.out.println("New Game");
                break;

        }
    }

    /**
     * checks key event and checks if corresponds to control
     * @param e key event
     */
    public void singleKeyUse(KeyEvent e){
        int keyPressed = e.getKeyCode();

        switch (keyPressed){

            case KeyEvent.VK_UP:
                System.out.println("Move Up");
                return;
            case KeyEvent.VK_RIGHT:
                System.out.println("Move Right");
                return;
            case KeyEvent.VK_DOWN:
                System.out.println("Move Down");
                return;
            case KeyEvent.VK_LEFT:
                System.out.println("Move Left");
                return;
            case KeyEvent.VK_ESCAPE:
                System.out.println("Close Game Paused");
                return;
            case KeyEvent.VK_SPACE:
                System.out.println("Display Game Pause");
                return;

        }

    }
}
