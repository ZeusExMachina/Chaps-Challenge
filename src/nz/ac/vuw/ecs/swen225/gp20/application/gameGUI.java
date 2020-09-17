package nz.ac.vuw.ecs.swen225.gp20.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class gameGUI{
    JFrame mainFrame = new JFrame("Chip's Challenge");

    JPanel controls = new JPanel();

    java.util.Timer timer = new Timer();

    //to be received form other classes
    int level;
    int timeVal;
    int chipsRemaining;

    ArrayList<Object> keys = new ArrayList<>(); //TODO: Add appropriate class type

    JLabel levelLabel = new JLabel();
    JLabel timeLabel = new JLabel();
    JLabel chipsLabel = new JLabel();

    boolean pauseState = false;


    public gameGUI(){

        mainFrame.setSize(900, 600);
        mainFrame.setVisible(true);
        mainFrame.setLayout(new GridBagLayout());
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);

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
        map.setSize((int)0.7*mainFrame.getWidth(), mainFrame.getHeight());
        mainFrame.getContentPane().add(map, gc);

        gc.weightx = 0.3;
        gc.weighty = 1;
        gc.gridx = 1;
        gc.gridy = 0;
        controls.setSize(new Dimension((int)0.3*mainFrame.getWidth(), mainFrame.getHeight()));
        mainFrame.getContentPane().add(controls, gc);
        //controls.setMaximumSize(new Dimension(mainFrame.getWidth()/3, mainFrame.getHeight()));
        //controls.setMinimumSize(new Dimension(mainFrame.getWidth()/3, mainFrame.getHeight()));



        controlsStart();


    }

    /**
     * construct display for game start/menu
     * (before level start)
     */
    public void controlsStart(){
        controls.setLayout(new GridLayout(2,1,0,0));

        JPanel topHalf = new JPanel();
        topHalf.setLayout(new GridLayout(2,1,10,0));
        JPanel bottomHalf = new JPanel();
        controls.add(topHalf);
        controls.add(bottomHalf);

        setLevel(1);//TODO: to be received form other class
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new GridLayout(2,1,10,0));
        JLabel title = new JLabel("Chip's Challenge");
        title.setFont(new java.awt.Font("Arial", Font.BOLD, 26));
        title.setHorizontalAlignment(JLabel.CENTER);
        titlePanel.add(title);
        titlePanel.add(levelLabel);
        topHalf.add(titlePanel);

        JPanel startPanel = new JPanel();
        JButton startGame = new JButton("Start Game");

        bottomHalf.setBorder(BorderFactory.createTitledBorder("Potentially display controls here}"));


        startGame.setFocusable(false);
        ActionListener aL = e -> {
            for(Component c : controls.getComponents()){
                controls.remove(c);
            }
            startTime();
            controls.repaint();


            controlsGamePlay();
        };

        startGame.addActionListener(aL);
        startPanel.add(startGame);

        topHalf.add(startPanel);

    }

    /**
     * construct display of control panels for in game play
     */
    public void controlsGamePlay(){
        controls.setLayout(new GridLayout(4,1,10,0));

        //TODO info to be called from other classes - currently random numbers
        setTime(10);
        setChipsRemaining(4);

        //LEVEL
        JPanel levelPanel = new JPanel();
        //levelPanel.setBorder(BorderFactory.createTitledBorder("Level"));
        levelLabel.setFont(new java.awt.Font("Arial", Font.BOLD, 24));
        levelPanel.add(levelLabel);
        controls.add(levelPanel);


        //TIME
        JPanel timerPanel = new JPanel();
        //timerPanel.setBorder(BorderFactory.createTitledBorder("Timer"));
        timeLabel.setText("TIME: " + String.format("%03d",timeVal));
        timeLabel.setFont(new java.awt.Font("Arial", Font.BOLD, 24));
        timerPanel.add(timeLabel);

        controls.add(timerPanel);

        //CHIPS
        JPanel chipsPanel = new JPanel();
        //chipsPanel.setBorder(BorderFactory.createTitledBorder("Chips"));
        chipsLabel.setFont(new java.awt.Font("Arial", Font.BOLD, 20));
        chipsPanel.add(chipsLabel);
        controls.add(chipsPanel);


        //KEYS
        JPanel keysPanel = new JPanel();
        keysPanel.setBorder(BorderFactory.createTitledBorder("Keys"));
        controls.add(keysPanel);


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

        controls.revalidate();
        controls.repaint();

    }

    /**
     * calls key functions that require ctrl key to be pressed.
     * @param e - key event that is NOT control
     */
    public void controlKeyUse(KeyEvent e){
        int keyPressed = e.getKeyCode();
        System.out.println("test");
        switch (keyPressed){

            case KeyEvent.VK_X:
                System.out.println("Exit Game");
                break;
            case KeyEvent.VK_S:
                System.out.println("Save Game");
                break;
            case KeyEvent.VK_R:
                if(pauseState) {
                    pauseState = false;
                    System.out.println("Resume Game");
                    timer = new Timer();
                    startTime();
                }else{
                    System.out.println("Game already playing");
                }
                break;
            case KeyEvent.VK_P:
                if(!pauseState) {
                    pauseState = true;
                    System.out.println("Pause Game");
                    timer.cancel();
                }else{
                    System.out.println("game already paused");
                }
                break;
            case KeyEvent.VK_1:
                System.out.println("New Game");
                break;

        }
    }

    /**
     * checks key event and calls corresponding method
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

    /**
     * reset and start timer
     */
    public void startTime(){
        timeLabel.setForeground(Color.black);
        try{
            timer.cancel();
            timer.purge();
        }catch (Exception e){
            return;
        }
        timer = new Timer();
        timeVal = 10;
        timer.schedule(createTask(), 1000,1000);

    }

    /**
     * create the timer task
     * adjust time each second
     * @return timeTask
     */
    public TimerTask createTask(){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                timeLabel.setText("TIME: " + String.format("%03d",timeVal));
                if(timeVal == 0){
                    timer.cancel();
                    timeLabel.setForeground(Color.red);
                    //timeLabel.setText("TIME IS UP");
                }else{
                    timeVal = timeVal -1 ;
                }
            }
        };

        return task;
    }

    //setters
    public void setLevel(int l){
        this.level = l;
        levelLabel.setText("LEVEL: " + String.format("%02d",this.level));
    }

    public void setTime(int t){
        this.timeVal = t;
    }

    public void setChipsRemaining(int c){
        this.chipsRemaining = c;
        chipsLabel.setText("Chips Remaining: " + String.valueOf(this.chipsRemaining));
    }

    public void addKey(Object key){
        keys.add(key);
    }

    public void useKey(Object key){
        keys.remove(key);
    }
}
