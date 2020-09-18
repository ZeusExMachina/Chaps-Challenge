package nz.ac.vuw.ecs.swen225.gp20.application;

import nz.ac.vuw.ecs.swen225.gp20.maze.Direction;

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
    JDialog pauseMenu = new JDialog(mainFrame, "PAUSED");




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


        pauseMenu.setSize(350,200);
        pauseMenu.setLayout(new GridLayout(2,1,0,0));
        JLabel pauseTitle = new JLabel("PAUSED", SwingConstants.CENTER);
        pauseTitle.setFont(new java.awt.Font("Arial", Font.BOLD, 20));
        pauseMenu.add(pauseTitle);
        pauseMenu.add(new JLabel("ESC to resume", SwingConstants.CENTER));
        pauseMenu.setLocationRelativeTo(null);
        pauseMenu.setVisible(false);
        pauseMenu.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                    pauseState = false;
                    System.out.println("Resume Game");
                    hidePauseDialog();
                    timer = new Timer();
                    startTime();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {

            }
        });




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

        setLevel();//TODO: to be received form other class
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

        setTime();
        setChipsRemaining();

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
                if(!pauseState) {
                    if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                        control = true;
                    } else if (control) {
                        controlKeyUse(e);
                    } else {
                        singleKeyUse(e);
                    }
                }else if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                    pauseState = false;
                    System.out.println("Resume Game");
                    hidePauseDialog();
                    timer = new Timer();
                    startTime();

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

    public void displayPauseDialog(){
        if(timeVal > 0) pauseMenu.setVisible(true);
    }

    public void hidePauseDialog(){
        pauseMenu.setVisible(false);
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
                System.out.println("Resume saved game");
                break;
            case KeyEvent.VK_P:
                System.out.println("Last Unfinished Level");
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
                moveCalled(Direction.NORTH);
                return;
            case KeyEvent.VK_RIGHT:
                moveCalled(Direction.EAST);
                return;
            case KeyEvent.VK_DOWN:
                moveCalled(Direction.SOUTH);
                return;
            case KeyEvent.VK_LEFT:
                moveCalled(Direction.WEST);
                return;
            case KeyEvent.VK_SPACE:
                if(!pauseState) {
                    pauseState = true;
                    displayPauseDialog();
                    System.out.println("Pause Game");
                    timer.cancel();
                }else{
                    System.out.println("game already paused");
                }
                return;

        }

    }

    /**
     * called when move key received
     * checks updates for keys and chips
     *
     * @param d
     */
    public void moveCalled(Direction d){
        setChipsRemaining();
        //check keys somehow also
        System.out.println(d);
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
                    //restart level
                }else{
                    timeVal = timeVal -1 ;
                }
            }
        };

        return task;
    }

    //setters
    public void setLevel(){
        this.level = 1;//change to get from maze
        levelLabel.setText("LEVEL: " + String.format("%02d",this.level));
    }

    public void setTime(){
        this.timeVal = 10;//get from maze
    }

    public void setChipsRemaining(){
        this.chipsRemaining = 4;//maze
        chipsLabel.setText("Chips Remaining: " + String.valueOf(this.chipsRemaining));
        if(this.chipsRemaining == 0){
            System.out.println("All chips found");
        }
    }

    public void addKey(Object key){
        keys.add(key);
    }

    public void useKey(Object key){
        keys.remove(key);
    }
}
