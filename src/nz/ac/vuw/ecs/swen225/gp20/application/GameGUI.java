package nz.ac.vuw.ecs.swen225.gp20.application;

import nz.ac.vuw.ecs.swen225.gp20.maze.Direction;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.persistence.LevelLoader;
import nz.ac.vuw.ecs.swen225.gp20.recnplay.Replayer;
import nz.ac.vuw.ecs.swen225.gp20.render.Canvas;
import nz.ac.vuw.ecs.swen225.gp20.render.Inventory;
import nz.ac.vuw.ecs.swen225.gp20.render.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.stream.Collectors;

/**
 * The main display gui
 * controls level display and controls
 */
public class GameGUI {
    /**
     * main game frame
     */
    private final JFrame mainFrame = new JFrame("Chip's Challenge");

    /**
     * left jPanel- content changes with each game state
     */
    private final JPanel controls = new JPanel();

    /**
     * Timer object for game, adjusts time display
     */
    private Timer timer = new Timer();

    //to be received form other classes
    /**
     * current game level - always starts with 1
     */
    private int level = 1;
    /**
     * time in seconds, allocated to level
     */
    private double timeVal;


    /**
     * Define level loader object - for calling correct level information
     */
    private final LevelLoader loader = new LevelLoader();

    /**
     * responsible for level number display
     */
    private final JLabel levelLabel = new JLabel();
    /**
     * responsible for time level display, adjusted in timer task
     */
    private final JLabel timeLabel = new JLabel();
    /**
     * responsible for remaining chips in current game and display
     */
    private final JLabel chipsLabel = new JLabel();

    /**
     * true if game is paused, restricts game functionality
     */
    private boolean pauseState = false;
    /**
     * true if currently inside a level, game is active
     */
    private boolean inGame = false;
    /**
     * dialog to display in paused state
     */
    private final JDialog pauseMenu = new JDialog(mainFrame, "PAUSED");

    /**
     * maze currently active, responsible for all objects inside level
     */
    private final Maze maze;
    /**
     * display the level and keys
     */
    private final Renderer render = Renderer.getInstance();

    /**
     * Constructor for the gui
     * made up of two made frames, maze display and control panel
     */
    public GameGUI(){


        maze = Maze.getInstance();
        setLevel(0); // start at level zero
        maze.loadLevel(loader.getLevelLayout(level), loader.getLevelHelpText(level));

        mainFrame.setSize(900, 600);
        mainFrame.setVisible(true);
        mainFrame.setLayout(new GridBagLayout());
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);
        mainFrame.setLayout(new BorderLayout());

        JMenuBar mb = new JMenuBar();
        JMenu load = new JMenu("Modes");
        mb.add(load);

        JMenuItem loadReplay = new JMenuItem("Load Replay");
        JMenuItem gameMenu = new JMenuItem("Main Menu");

        loadReplay.addActionListener(e -> {
            clearControlFrame();
            replayControls();
        });

        gameMenu.addActionListener(e -> {
            clearControlFrame();
            controlsStart();
        });
        load.add(loadReplay);
        load.add(gameMenu);

        mainFrame.setJMenuBar(mb);

        Canvas board = render.getCanvas();
        render.setMaze(maze);
        render.display();
        board.setSize((int)(0.7*mainFrame.getWidth()), mainFrame.getHeight());
        mainFrame.getContentPane().add(board, BorderLayout.LINE_START);

        controls.setSize(new Dimension((int)(0.3*mainFrame.getWidth()), mainFrame.getHeight()));
        mainFrame.getContentPane().add(controls, BorderLayout.LINE_END);
        controls.setPreferredSize(new Dimension(mainFrame.getWidth()/3, mainFrame.getHeight()));

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

        board.addKeyListener(new KeyAdapter() {
            /**
             * true if control key is currently held down
             */
            boolean control = false;
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
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


        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new GridLayout(2,1,10,0));
        JLabel title = new JLabel("Chip's Challenge");
        title.setFont(new java.awt.Font("Arial", Font.BOLD, 26));
        title.setHorizontalAlignment(JLabel.CENTER);
        titlePanel.add(title);
        levelLabel.setFont(new java.awt.Font("Arial", ~Font.BOLD, 16));
        titlePanel.add(levelLabel);
        topHalf.add(titlePanel);

        JPanel startPanel = new JPanel();
        JButton startGame = new JButton("Start Game");

        bottomHalf.setBorder(BorderFactory.createTitledBorder("Potentially display controls here"));


        startGame.setFocusable(false);
        ActionListener aL = e -> {
            clearControlFrame();
            startTime();
            controlsGamePlay();
            resetMaze();
        };

        startGame.addActionListener(aL);
        startPanel.add(startGame);

        topHalf.add(startPanel);

    }

    /**
     * construct display of control panels for in game play
     */
    public void controlsGamePlay(){
        inGame = true;
        controls.setLayout(new GridLayout(4,1,10,0));

        setTime();
        setChipsRemaining();

        //LEVEL
        JPanel levelPanel = new JPanel();
        levelLabel.setFont(new java.awt.Font("Arial", Font.BOLD, 24));
        levelPanel.add(levelLabel);
        controls.add(levelPanel);


        //TIME
        JPanel timerPanel = new JPanel();
        timeLabel.setText("TIME: " + String.format("%03d",(int)timeVal));
        timeLabel.setFont(new java.awt.Font("Arial", Font.BOLD, 24));
        timerPanel.add(timeLabel);
        controls.add(timerPanel);

        //CHIPS
        JPanel chipsPanel = new JPanel();
        chipsLabel.setFont(new java.awt.Font("Arial", Font.BOLD, 20));
        chipsPanel.add(chipsLabel);
        controls.add(chipsPanel);


        //KEYS
        Inventory keysPanel = render.getInventory();
        keysPanel.setBorder(BorderFactory.createTitledBorder("Inventory"));
        controls.add(keysPanel);


        controls.revalidate();
        controls.repaint();
    }

    /**
     * controls for selecting type and file for game replay
     */
    public void replayControls(){
        Replayer replayObject = new Replayer(this);
        controls.setLayout(new GridLayout(2,1,10,0));

        //start button - enabled to toggle
        JButton startReplay = new JButton("Start Replay");
        startReplay.setEnabled(false);

        //two frames for each half
        JPanel topHalf = new JPanel();
        topHalf.setLayout(new GridLayout(4,1,10,0));
        controls.add(topHalf);
        JPanel bottomHalf = new JPanel();
        bottomHalf.setLayout(new GridLayout(1,2,10,0));
        controls.add(bottomHalf);

        //top half - title and file select
        JLabel title = new JLabel("Replay Game");
        title.setFont(new java.awt.Font("Arial", Font.BOLD, 26));
        title.setHorizontalAlignment(JLabel.CENTER);
        topHalf.add(title);


        //LOAD PANEL = LOAD FILE OPTIONS
        JPanel loadPanel = new JPanel();
        topHalf.add(loadPanel);

        JButton selectFile = new JButton("Load File");
        selectFile.setFocusable(false);
        JLabel fileNameDisplay = new JLabel("No File");

        ActionListener aL = e -> {
            JFileChooser j = new JFileChooser();
            j.showSaveDialog(null);
            try { //TODO: test for valid file format
                replayObject.loadGameReplay(j.getSelectedFile().getName());
                fileNameDisplay.setForeground(Color.black);
                selectFile.setText(j.getSelectedFile().getName());
                startReplay.setEnabled(true);
            } catch (IOException | NullPointerException exp) {
                fileNameDisplay.setForeground(Color.red);
                fileNameDisplay.setText("Invalid File");
                startReplay.setEnabled(false);
                //exp.printStackTrace();
            }
        };

        selectFile.addActionListener(aL);

        loadPanel.add(selectFile);
        loadPanel.add(fileNameDisplay);

        //SPEED PANEL - speed options
        JPanel speedPanel = new JPanel();
        topHalf.add(speedPanel);
        JLabel speedTitle = new JLabel("Replay Speed:");
        speedPanel.add(speedTitle);
        List<Double> speedValues = Arrays.asList(Replayer.REPLAY_SPEEDS);
        String[] speedStrings = speedValues.stream().map(Object::toString).toArray(String[]::new);

        JComboBox<String> speedSelectBox = new JComboBox<>(speedStrings);
        speedSelectBox.setSelectedItem(speedStrings[3]);//should be value 1
        speedPanel.add(speedSelectBox);

        //start panel - button and speed error
        JPanel startPanel = new JPanel();
        topHalf.add(startPanel);

        ActionListener startAL = e -> {
            try {
                replayObject.setReplaySpeed(Double.parseDouble(Objects.requireNonNull(speedSelectBox.getSelectedItem()).toString()));
                resetMaze();
            } catch (IllegalArgumentException | NullPointerException exp) {
                System.out.println("Error loading replay");
            }
        };
        startReplay.addActionListener(startAL);
        startPanel.add(startReplay);

        controls.revalidate();
        controls.repaint();


    }

    /**
     * reset the control frame for (clear all components)
     */
    public void clearControlFrame(){
        inGame = false;
        for(Component c : controls.getComponents()){
            controls.remove(c);
        }
        controls.repaint();
    }

    /**
     * reset the mae display
     */
    public void resetMaze(){
        try {
            maze.loadLevel(loader.getLevelLayout(level), loader.getLevelHelpText(level));
            setChipsRemaining();
            render.reset();
            render.update();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * display the pause jDialog
     */
    public void displayPauseDialog(){
        if(timeVal > 0) pauseMenu.setVisible(true);
    }

    /**
     * hide the pause jDialog
     */
    public void hidePauseDialog(){
        pauseMenu.setVisible(false);
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
                System.out.println("Resume saved game");
                break;
            case KeyEvent.VK_P:
                System.out.println("Last Unfinished Level");
                break;
            case KeyEvent.VK_1:
                System.out.println("New Game");
                break;
            default:
                break;

        }
    }

    /**
     * checks key event and calls corresponding method
     * @param e key event
     */
    public void singleKeyUse(KeyEvent e){
        int keyPressed = e.getKeyCode();
        if(inGame) {
            switch (keyPressed) {

                case KeyEvent.VK_UP:
                    moveCalled(Direction.NORTH);
                    break;
                case KeyEvent.VK_RIGHT:
                    moveCalled(Direction.EAST);
                    break;
                case KeyEvent.VK_DOWN:
                    moveCalled(Direction.SOUTH);
                    break;
                case KeyEvent.VK_LEFT:
                    moveCalled(Direction.WEST);
                    break;
                case KeyEvent.VK_SPACE:
                    if (!pauseState) {
                        pauseState = true;
                        displayPauseDialog();
                        System.out.println("Pause Game");
                        timer.cancel();
                    } else {
                        System.out.println("game already paused");
                    }
                    break;
                default:
                    break;
            }
        }

    }

    /**
     * called when move key received
     * checks updates for chips and completion conditions
     *
     * @param d value of direction enum
     */
    public void moveCalled(Direction d){
        setChipsRemaining();
        if (maze.moveChap(d)) render.update();
        if(maze.isLevelDone()){
            levelCompleteDialog();
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
        timer.schedule(createTask(), 500,100);

    }

    /**
     * create the timer task
     * adjust time each second
     * @return timeTask
     */
    public TimerTask createTask(){
        return new TimerTask() {
            @Override
            public void run() {
                timeLabel.setText("TIME: " + String.format("%03d",((int)timeVal)));
                if(timeVal <= 0){
                    timer.cancel();
                    timeLabel.setForeground(Color.red);
                    timeOutDialog();
                }else{
                    timeVal = timeVal - 0.1 ;
                }
            }
        };
    }

    /**
     * set the current level to passed int
     * @param levelInt - level to set game to
     */
    public void setLevel(int levelInt){
        this.level = loader.getAllLevelNumbers().get(levelInt);
        levelLabel.setText("LEVEL: " + String.format("%02d",this.level));
    }

    /**
     * set level clock at start of level
     */
    public void setTime(){
        this.timeVal = loader.getLevelClock(level);
    }

    /**
     * Set number of chips remaining in maze object
     */
    public void setChipsRemaining(){
        chipsLabel.setText("Chips Remaining: " + maze.getTreasuresLeft());

    }

    /**
     * display a dialog if the player does not complete the level in the allocated time
     */
    public void timeOutDialog(){
        inGame = false;
        JDialog timeOutDisplay = new JDialog(mainFrame, "Time is Up!");
        timeOutDisplay.setSize(350,200);
        JLabel gameOverLabel = new JLabel("Time's Up!", SwingConstants.CENTER);
        gameOverLabel.setFont(new java.awt.Font("Arial", Font.BOLD, 20));
        JPanel buttonFrame = new JPanel();
        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> {
            timeOutDisplay.dispose();
            clearControlFrame();
            controlsStart();
        });
        timeOutDisplay.setLayout(new GridLayout(2,1,0,0));
        timeOutDisplay.add(gameOverLabel);
        buttonFrame.add(restartButton);
        timeOutDisplay.add(buttonFrame);
        timeOutDisplay.setVisible(true);
        timeOutDisplay.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);


    }
    /**
     * display a dialog when the players completes a level
     */
    public void levelCompleteDialog(){
        timer.cancel();
        inGame = false;
        JDialog levelComplete = new JDialog(mainFrame, "Level Completed");
        levelComplete.setLayout(new GridLayout(2,1,0,0));
        levelComplete.setSize(350,200);

        JLabel gameOverLabel = new JLabel("Level Complete!", SwingConstants.CENTER);
        gameOverLabel.setFont(new java.awt.Font("Arial", Font.BOLD, 20));

        JPanel buttonFrame = new JPanel();

        buttonFrame.setLayout(new GridLayout(2,1,0,0));
        JPanel leftFrame = new JPanel();
        JPanel midFrame = new JPanel();
        JPanel rightFrame = new JPanel();
        buttonFrame.setLayout(new GridLayout(1,3,10,0));
        buttonFrame.add(leftFrame);
        buttonFrame.add(midFrame);
        buttonFrame.add(rightFrame);
        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> {
            levelComplete.dispose();
            clearControlFrame();
            controlsGamePlay();
        });

        JButton menuButton = new JButton("Menu");
        menuButton.addActionListener(e -> {
            levelComplete.dispose();
            clearControlFrame();
            controlsStart();
        });


        JButton nextLevelButton = new JButton("Next Level");
        nextLevelButton.addActionListener(e -> {
            setLevel(level + 1);
            levelComplete.dispose();
            clearControlFrame();
            controlsStart();
        });

        if(!loader.getAllLevelNumbers().contains(this.level + 1)){//not the last level
            nextLevelButton.setEnabled(false);
        }

        levelComplete.add(gameOverLabel);
        leftFrame.add(restartButton);
        midFrame.add(nextLevelButton);
        rightFrame.add(menuButton);
        levelComplete.add(buttonFrame);
        levelComplete.setVisible(true);
        levelComplete.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);


    }

}
