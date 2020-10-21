package nz.ac.vuw.ecs.swen225.gp20.application;

import nz.ac.vuw.ecs.swen225.gp20.maze.Actor;
import nz.ac.vuw.ecs.swen225.gp20.maze.Direction;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.persistence.GameStateController;
import nz.ac.vuw.ecs.swen225.gp20.persistence.LevelLoader;
import nz.ac.vuw.ecs.swen225.gp20.recnplay.Recorder;
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

/**
 * The main display gui
 * controls level display and controls
 */
public class GameGUI {
    /**
     * main game frame
     */
    private final JFrame mainFrame = new JFrame("Chap's Challenge");
    /**
     * left jPanel- content changes with each game state
     */
    private final JPanel controls = new JPanel();
    /**
     * Timer object for game, adjusts time display
     */
    private Timer timer = new Timer();

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
     * define GameStateController used for game saving and loading
     */
    private final GameStateController gameState = new GameStateController(this);
    /**
     * A button in the drop-down menu to restart the current level.
     */
    private final JMenuItem restartLevel = new JMenuItem("Restart Level");
    /**
     * A button in the drop-down menu to save the current game state and exit the program.
     */
    private final JMenuItem saveGame = new JMenuItem("Save and Exit");
    /**
     * A button in the drop-down menu to save a recording of the current game.
     */
    private final JMenuItem saveRecording = new JMenuItem("Save Recording");
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
     * true if currently replaying game
     */
    private boolean currentReplay = false;
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
     * Record moves made by the player.
     */
    private Recorder recorder;
    /**
     * Replay moves from recorded games.
     */
    private Replayer replayer;
    /**
     * manages secondary actor movements
     */
    private int actorMoveCount = 1;

    /**
     * Constructor for the gui
     * made up of two made frames, maze display and control panel
     */
    public GameGUI(){


        //INITIALISE GAME STATE
        maze = Maze.getInstance();
        Canvas board = render.getCanvas();

        //check for saved progress
        if(gameState.previousStateFound()){
            setGameLevel(gameState.getLevel());
            gameState.loadGameState();
            gameState.loadMazeState(loader.getLevelHelpText(level));
            startTime();
        }else {
            setGameLevel(level);
        }
        render.setMaze(maze);
        render.display();


        //CONSTRUCT FRAME
        mainFrame.setSize(900, 600);
        mainFrame.setVisible(true);
        mainFrame.setLayout(new GridBagLayout());
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);
        mainFrame.setLayout(new BorderLayout());


        //MENU BAR
        JMenuBar mb = new JMenuBar();
        JMenu load = new JMenu("Modes");
        mb.add(load);

        JMenuItem loadReplay = new JMenuItem("Load Replay");
        JMenuItem gameMenu = new JMenuItem("Main Menu");

        loadReplay.addActionListener(e -> {
            clearControlFrame();
            replayControls();
        });

        restartLevel.addActionListener(e -> {
            //restart the game - from current level
            resetMaze();
            setTime();
            clearControlFrame();
            controlsGamePlay();
        });

        saveGame.addActionListener(e -> saveGameState());
        
        saveRecording.addActionListener(e -> {
        	if (!currentReplay && recorder!=null) { recorder.saveGame(); }
        });

        gameMenu.addActionListener(e -> {
            render.stopBackgroundMusic();
            clearControlFrame();
            controlsStart();
        });
        load.add(loadReplay);
        load.add(gameMenu);
        load.add(restartLevel);
        load.add(saveGame);
        load.add(saveRecording);

        mainFrame.setJMenuBar(mb);

        //ADD MAP
        board.setSize((2*(mainFrame.getWidth()/3)), mainFrame.getHeight());
        mainFrame.getContentPane().add(board, BorderLayout.LINE_START);

        //SIDE PANEL
        //controls.setSize(new Dimension((int)(mainFrame.getWidth()/3), mainFrame.getHeight()));
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
                    hidePauseDialog();
                    timer = new Timer();
                    render.startBackgroundMusic();
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
                if(!pauseState) {
                    if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                        control = true;
                    } else if (control) {
                        controlKeyUse(e);
                    } else {
                        singleKeyUse(e);
                    }
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_CONTROL) control = false;
            }
        });

        if(gameState.previousStateFound()){
            controlsGamePlay();
        }else{
            controlsStart();
        }

    }

    /**
     * Get the current level
     * @return the level number
     */
    public int getLevel() {
        return level;
    }

    /**
     * construct display for game start/menu
     * (before level start)
     */
    public void controlsStart(){

        currentReplay = false;
        inGame = false;
        stopTime();
        
        // Disabling menu items
        restartLevel.setEnabled(false);
        saveGame.setEnabled(false);
        saveRecording.setEnabled(false);

        controls.setLayout(new GridLayout(3,1,0,0));

        constructControlGrid("LEVEL: " + String.format("%02d",this.level));

        JPanel startPanel = new JPanel();
        JButton startGame = new JButton("Start Game");


        JPanel lowerPanel = new JPanel();
        lowerPanel.setLayout(new GridLayout(10,2,10,0));
        lowerPanel.add(new JLabel("Arrow Keys : Move"));
        lowerPanel.add(new JLabel("Space      : Pause"));
        lowerPanel.add(new JLabel("Esc        : Resume"));
        lowerPanel.add(new JLabel("Ctrl + X   : Exit Game"));
        lowerPanel.add(new JLabel("Ctrl + 1   : Reset Level"));


        startGame.setFocusable(false);
        ActionListener aL = e -> {
            clearControlFrame();
            setTime();
            controlsGamePlay();
            resetMaze();
            if (replayer != null) { replayer.endCurrentReplay(); }
        };

        startGame.addActionListener(aL);
        startPanel.add(startGame);
        controls.add(startPanel);
        controls.add(lowerPanel);

        repaintDisplayPanels();

    }


    /**
     * construct display of control panels for in game play
     */
    public void controlsGamePlay(){
        inGame = true;
        render.startBackgroundMusic();
        setChipsRemaining();
        
        // Enabling menu items
        restartLevel.setEnabled(true);
        saveGame.setEnabled(true);
        saveRecording.setEnabled(true);

        constructControlGrid("LEVEL: " + String.format("%02d",this.level));

        controls.add(createGameStatsPanel(24));
        controls.add(createInventoryPanel());
        // Start a new recorder
        recorder = new Recorder(level, timeVal);

        repaintDisplayPanels();
    }

    /**
     * adjust control display for replay control options
     */
    public void operateReplayControls(){
        inGame = true;
        render.startBackgroundMusic();
        setChipsRemaining();

        constructControlGrid("REPLAY LEVEL: " + String.format("%02d",this.level));
        controls.setLayout(new GridLayout(4,1,0,10));


        controls.add(createGameStatsPanel(18));
        JPanel replayControls = new JPanel();

        JLabel replayLabel = new JLabel("Replay Controls");
        replayLabel.setFont(new java.awt.Font("Arial", Font.BOLD, 18));
        replayControls.add(replayLabel);

        JButton stepForward = new JButton("Step replay");
        stepForward.setEnabled(replayer != null && !replayer.isAutoReplaying());
        stepForward.setFocusable(false);

        ActionListener stepAL = e -> {
            if (replayer != null) {
                // Replay the next move and change the time
                double nextMoveTimestamp = replayer.replayNextAction();
                if (nextMoveTimestamp > 0) {
                    timeVal = loader.getLevelClock(level) - nextMoveTimestamp;
                    timeLabel.setText(String.format("%03d",((int)timeVal)));
                }
            }
        };
        stepForward.addActionListener(stepAL);

        JButton replayModeToggle = new JButton(replayer!=null&&replayer.isAutoReplaying() ? "Toggle to Step-by-Step" : "Toggle to Auto-replay");
        replayModeToggle.setFocusable(false);

        ActionListener toggleAL = e -> {
            if (replayer != null) {
                replayer.toggleReplayType();
                if (replayer.isAutoReplaying()) {
                    // Auto-replay mode
                    replayModeToggle.setText("Toggle to Step-by-step");
                    stepForward.setEnabled(false);
                    long timeBeforeNextMove = replayer.getTimeBeforeNextMove();
                    if (timeBeforeNextMove >= 0) {
                        timer = new Timer();
                        timer.schedule(createTask(), timeBeforeNextMove,
                                (long)(100L/replayer.getReplaySpeed()));
                    }
                } else {
                    // Step-by-step mode
                    replayModeToggle.setText("Toggle to Auto-replay");
                    stepForward.setEnabled(true);
                    stopTime();
                }
            }

        };
        replayModeToggle.addActionListener(toggleAL);
        replayControls.add(replayModeToggle);
        replayControls.add(stepForward);
        controls.add(replayControls);
        controls.add(createInventoryPanel());
        repaintDisplayPanels();

    }

    /**
     * controls for selecting type and file for game replay
     */
    public void replayControls(){
        render.stopBackgroundMusic();
        currentReplay = true;
        inGame = false;
        stopTime();
        if (replayer != null) { replayer.endCurrentReplay(); }
        constructControlGrid("LOAD REPLAY");
        replayer = new Replayer(this);
        
        // Disabling menu items
        restartLevel.setEnabled(false);
        saveGame.setEnabled(false);
        saveRecording.setEnabled(false);

        //start button - enabled to toggle
        JButton startReplay = new JButton("Start Replay");
        startReplay.setEnabled(false);
        startReplay.setFocusable(false);

        JPanel widgets = new JPanel();
        widgets.setLayout(new GridLayout(3,1,10,0));

        //LOAD PANEL = LOAD FILE OPTIONS
        JPanel loadPanel = new JPanel();
        widgets.add(loadPanel);

        JButton selectFile = new JButton("Load File");
        selectFile.setFocusable(false);
        JLabel fileNameDisplay = new JLabel("No File");

        ActionListener aL = e -> {
            JFileChooser j = new JFileChooser();
            j.setFocusable(false);
            j.showOpenDialog(mainFrame);
            try {
                replayer.loadGameReplay(j.getSelectedFile());
                fileNameDisplay.setForeground(Color.black);
                fileNameDisplay.setText(j.getSelectedFile().getName());
                startReplay.setEnabled(true);
            } catch (IOException | com.google.gson.JsonSyntaxException exp) {
                fileNameDisplay.setForeground(Color.red);
                fileNameDisplay.setText("Invalid File");
                startReplay.setEnabled(false);
                //exp.printStackTrace();
            } catch(NullPointerException exp){
                fileNameDisplay.setForeground(Color.black);
                fileNameDisplay.setText("No File");
                startReplay.setEnabled(false);
            }
        };

        selectFile.addActionListener(aL);

        loadPanel.add(selectFile);
        loadPanel.add(fileNameDisplay);

        //SPEED PANEL - speed options
        JPanel speedPanel = new JPanel();
        widgets.add(speedPanel);
        JLabel speedTitle = new JLabel("Replay Speed:");
        speedPanel.add(speedTitle);
        List<Double> speedValues = Replayer.REPLAY_SPEEDS;
        String[] speedStrings = speedValues.stream().map(Object::toString).toArray(String[]::new);

        JComboBox<String> speedSelectBox = new JComboBox<>(speedStrings);
        speedSelectBox.setSelectedItem(speedStrings[3]);//should be value 1
        speedPanel.add(speedSelectBox);

        //start panel - button and speed error
        JPanel startPanel = new JPanel();
        widgets.add(startPanel);

        ActionListener startAL = e -> {
            try {
                replayer.setReplaySpeed(Double.parseDouble(Objects.requireNonNull(speedSelectBox.getSelectedItem()).toString()));
                setGameLevel(replayer.getRecordingLevel());
                resetMaze();
                clearControlFrame();
                setTime();
                operateReplayControls();
            } catch (IllegalArgumentException | NullPointerException exp) {
                System.out.println("Error loading replay");
            }
        };
        startReplay.addActionListener(startAL);
        startPanel.add(startReplay);
        controls.add(widgets);

        repaintDisplayPanels();


    }

    /**
     * create the JPanel to display in game statistics/scores
     * @param size - font size of statistics
     * @return Panel to display
     */
    public JPanel createGameStatsPanel(int size){
        //grid layout
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new GridLayout(4,2,0,10));

        //TIME
        JLabel timeTitle = new JLabel("Time: ", SwingConstants.RIGHT);
        timeTitle.setFont(new java.awt.Font("Arial", Font.BOLD, size));
        statsPanel.add(timeTitle);
        timeLabel.setText(String.format("%03d",(int)timeVal));
        timeLabel.setFont(new java.awt.Font("Arial", Font.BOLD, size));
        statsPanel.add(timeLabel);

        //CHIPS
        JLabel chipsTitle1 = new JLabel("Chips  ", SwingConstants.RIGHT);
        chipsTitle1.setFont(new java.awt.Font("Arial", Font.BOLD, size));
        JLabel chipsTitle = new JLabel("Remaining: ", SwingConstants.RIGHT);
        chipsTitle.setFont(new java.awt.Font("Arial", Font.BOLD, size));
        for(int i = 0; i < 2; i++) statsPanel.add(new JLabel());
        statsPanel.add(chipsTitle1);
        statsPanel.add(new JLabel());
        statsPanel.add(chipsTitle);
        chipsLabel.setFont(new java.awt.Font("Arial", Font.BOLD, size));
        statsPanel.add(chipsLabel);

        return statsPanel;
    }

    /**
     * call inventory panel from render
     * @return completed inventory panel
     */
    public JPanel createInventoryPanel(){
        Inventory keysPanel = render.getInventory();
        keysPanel.setBorder(BorderFactory.createTitledBorder("Inventory"));
        return keysPanel;
    }

    /**
     * get all level information and update display information based on given int
     * @param levelNum level number to be played
     */
    public void setGameLevel(int levelNum){
        this.level = loader.getAllLevelNumbers().get(levelNum-1);
        //levelLabel.setText("LEVEL: " + String.format("%02d",this.level));
        Set<Actor> secondaries = new HashSet<>();
        if (loader.getActorLoader().isRequiredForThisLevel(level)) {
            secondaries = loader.getActorLoader().getSetOfSecondaryActors(level, loader);
        }
        maze.loadLevel(loader.getLevelLayout(level), loader.getLevelHelpText(level), secondaries);
    }


    /**
     * construct the heading and gridlayout for control panel after reset
     * @param subtitle - subtitle text to be displayed
     */
    public void constructControlGrid(String subtitle){
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new GridLayout(2,1,10,0));
        JLabel title = new JLabel("Chap's Challenge");
        title.setFont(new java.awt.Font("Impact", Font.BOLD, 34));
        title.setHorizontalAlignment(JLabel.CENTER);
        titlePanel.add(title);
        JLabel subTitle = new JLabel(subtitle);
        subTitle.setFont(new java.awt.Font("Arial", ~Font.BOLD, 16));
        titlePanel.add(subTitle);
        controls.add(titlePanel);

    }

    /**
     * save the current game state and exit the program
     */
    public void saveGameState(){
        System.out.println("Saving Game state...");
        try{
            gameState.saveState();
        } catch (IOException e){
            return;
        }
        System.exit(0);
    }


    /**
     * reset the control frame for (clear all components)
     */
    public void clearControlFrame() {
        inGame = false;
        for (Component c : controls.getComponents()) {
            controls.remove(c);
        }
        controls.repaint();
    }

    /**
     * repaints controls frame for each different use
     */
    public void repaintDisplayPanels(){
        controls.revalidate();
        controls.repaint();


    }

    /**
     * reset the maze display
     */
    public void resetMaze(){
        try {
            Set<Actor> secondaries = new HashSet<>();
            if (loader.getActorLoader().isRequiredForThisLevel(level)) {
                secondaries = loader.getActorLoader().getSetOfSecondaryActors(level, loader);
            }
            maze.loadLevel(loader.getLevelLayout(level), loader.getLevelHelpText(level), secondaries);
            setChipsRemaining();
            render.update();
            stopTime();
            startTime();
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
                //exit the program
                System.exit(0);
                break;
            case KeyEvent.VK_S:
                saveGameState();
                break;
            case KeyEvent.VK_R:
                System.out.println("Resume saved game");
                break;
            case KeyEvent.VK_P:
                System.out.println("Last Unfinished Level");
                break;
            case KeyEvent.VK_1:
                //restart the game - from current level
                resetMaze();
                clearControlFrame();
                controlsStart();
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
        if(inGame && !currentReplay) {
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
                        render.stopBackgroundMusic();
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

        String move = maze.moveChap(d);
        if (move != null) {
            render.update();
            Renderer.playSound(move);
        }
        maze.getChap().isMoving();
        if(maze.isLevelDone()){
            levelCompleteDialog();
        }
        if (!currentReplay) recorder.recordNewAction(d, timeVal);
        setChipsRemaining();
    }

    /**
     * reset and start timer
     */
    public void startTime(){
        timeLabel.setForeground(Color.black);
        stopTime();
        actorMoveCount = 1;
        timer = new Timer();
        if (!currentReplay) { timer.schedule(createTask(), 500,100); }

    }

    /**
     * stop the current timer, prevent time out from occurring
     */
    public void stopTime(){
        try{
            timer.cancel();
            timer.purge();
        }catch (Exception ignored){

        }
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
                if (maze.getChap() == null || render == null) return;
            	if (currentReplay && replayer!=null && !replayer.hasMovesToReplay()) { timer.cancel(); return; }
                timeLabel.setText(String.format("%03d",((int)timeVal)));
                maze.getChap().updateFrame();
                render.update();
                render.display();
                if(actorMoveCount == 3){
                    maze.moveSecondaryActors();
                    for (Actor secondaryActor : maze.getSecondaryActors()) {
                        if(render.isPositionOnScreen(secondaryActor.getPosition())) Renderer.playSound(secondaryActor.getName());
                    }
                    actorMoveCount = 0;
                }
                actorMoveCount += 1;
                if(timeVal <= 10) {
                    timeLabel.setForeground(Color.red);
                }
                if(timeVal <= 0 || !maze.isChapAlive()){
                    timer.cancel();
                    gameOverDialog(maze.isChapAlive());
                }else{
                    timeVal = timeVal - 0.1 ;
                }
            }
        };
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
        chipsLabel.setText(String.format("%03d",(maze.getTreasuresLeft())));


    }

    /**
     * display a dialog if the player does not complete the level in the allocated time
     * @param time - true if cause of game over is out of time
     */
    public void gameOverDialog(Boolean time){
        render.stopBackgroundMusic();
        inGame = false;
        JDialog timeOutDisplay = new JDialog(mainFrame, "Time is Up!");
        timeOutDisplay.setSize(350,200);
        JLabel gameOverLabel = new JLabel("Time's Up!", SwingConstants.CENTER);
        if(!time){gameOverLabel.setText("You Died!");}
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
        render.stopBackgroundMusic();
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
            resetMaze();
            setTime();
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
            setGameLevel(level + 1);
            levelComplete.dispose();
            clearControlFrame();
            resetMaze();
            render.startBackgroundMusic();
            setTime();
            controlsGamePlay();
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

    /**
     * get the current remaining time
     * @return timeVal
     */
    public double getTime(){
        return timeVal;
    }

    /**
     * set time to specific value
     * @param t - time left
     */
    public void setTime(double t){
        this.timeVal = t;
    }

}