package nz.ac.vuw.ecs.swen225.gp20.recnplay;

import java.io.File;
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;

import com.google.gson.Gson;
import nz.ac.vuw.ecs.swen225.gp20.application.GameGUI;
import nz.ac.vuw.ecs.swen225.gp20.maze.Direction;

/**
 * Loads and plays through game replays for Chap's Challenge.
 * @author Elijah Guarina
 */
public class Replayer {
	
	// ------------------------------------------------
	// ------------------- FIELDS ---------------------
	// ------------------------------------------------
	
	/**
	 * Stores the history of actions done by actors from a loaded game replay.
	 */
	private Queue<ActionRecord> gameRecordHistory;
	/**
	 * The GUI associated with the game.
	 */
	private GameGUI gui;
	/**
	 * Keeps track of whether the program is auto-replaying through a recorded 
	 * game, or is going through it step-by-step.
	 */
	private boolean autoReplaying;
	/**
	 * The speed at which recorded actions are performed.
	 */
	private double replaySpeed;
	/**
	 * The timestamp within a loaded game that this Replayer is currently at.
	 */
	private double currentTimeInReplay;
	/**
	 * Timer used to perform actions on a regular basis.
	 */
	private Timer timer;
	/**
	 * This object replays actions regularly while replaying in auto-replay mode.
	 */
	private ActionPlayer replayedAction;
	
	// ------------------------------------------------
	// ----------------- CONSTANTS --------------------
	// ------------------------------------------------
	
	/**
	 * The period between two distinct timestamps in a game being replayed when the replay speed is 1.0x (only relevant while in auto-replay mode).
	 */
	private final long DEFAULT_DELAY_MILLIS = 100L;
	
	/**
	 * Holds the possible replay speeds that a recorded game can be replayed at.
	 */
	public static final Double[] REPLAY_SPEEDS = { 0.25, 0.5, 0.75, 1.0, 1.25, 1.5, 1.75, 2.0 };
	
	// ------------------------------------------------
	// ---------------- CONSTRUCTOR -------------------
	// ------------------------------------------------
	
	/**
	 * Create a new Replayer that is associated with a given 
	 * Controller.
	 * @param ui is the GUI associated with the game
	 */
	public Replayer(GameGUI ui) {
		this.gui = ui;
		this.autoReplaying = false;
		this.replaySpeed = 1.0;
		this.currentTimeInReplay = 0.0;
	}
	
	// ------------------------------------------------
	// ------------- GETTERS & SETTERS ----------------
	// ------------------------------------------------
	
	/**
	 * Check whether or not a replayed game is currently being 
	 * auto-replayed, or if it is replaying the actions in a 
	 * step-by-step manner.
	 * @return true if auto-replaying,
	 * 		   false if replaying step-by-step
	 */
	public boolean isAutoReplaying() {
		return autoReplaying;
	}
	
	/**
	 * Get the currently set replay speed for replaying games.
	 * @return the replay speed
	 */
	public double getReplaySpeed() {
		return replaySpeed;
	}
	
	/**
	 * Set the replay speed for the recorded game to be replayed at.
	 * Only accepts speeds from replaySpeeds.
	 * @param speed is the new replay speed to set
	 * @throws IllegalArgumentException
	 */
	public void setReplaySpeed(double speed) throws IllegalArgumentException {
		// First check if the entered replay speed is valid, if so set it.
		// Speed must be 0.0 < x < 2.0, and must be divisible by 0.25.
		if (speed < 0.0 || speed > 2.0 || speed%0.25 != 0) { 
			throw new IllegalArgumentException("Invalid replay speed value of " + speed + " entered.");
		} else {
			replaySpeed = speed;
			if (autoReplaying || gameRecordHistory == null || gameRecordHistory.isEmpty()) {
				resetTimer();
				timer.schedule(replayedAction, (long)(gameRecordHistory.peek().getTimeStamp()-currentTimeInReplay), (long)(DEFAULT_DELAY_MILLIS/replaySpeed));
			}
		}
	}
	
	// ----------------------------------------------
	// ---------------- REPLAYING -------------------
	// ----------------------------------------------
	
	/**
	 * Replay a recorded action from a loaded game by simulating 
	 * player input in the GUI.
	 */
	private class ActionPlayer extends TimerTask {
		/**
		 * Check if we can perform the next recorded action in the queue (if any).
		 * Also increment the current time in the game being replayed if necessary.
		 */
		@Override
		public void run() {
			//System.out.println("current time = " + currentTimeInReplay);
			// Only perform an action if there is a loaded game that has not been finished
			if (gameRecordHistory != null && !gameRecordHistory.isEmpty()) {
				// If the next action was performed at this time in the 
				// recorded game, 
				if (gameRecordHistory.peek().getTimeStamp() <= currentTimeInReplay) {
					replayAction();
				}
				// After replaying, check if the replay has finished. If so, stop auto-replaying.
				if (gameRecordHistory.isEmpty()) { 
					autoReplaying = false;
					resetTimer();
					return;
				}
			}
			// If in auto-replay mode, increment the replay time.
			if (autoReplaying) { currentTimeInReplay += DEFAULT_DELAY_MILLIS/1000.0; }
		}
		
		/**
		 * Perform a move on the GUI based on the values of the ActionRecord 
		 * of the next recorded action in the queue.
		 */
		private void replayAction() {
			ActionRecord actionToReplay = gameRecordHistory.poll();
			// TODO: Add the logic for deciding what move to make for which actor
			gui.moveCalled(actionToReplay.getMoveDirection(), false);
			
			// Just a test
			System.out.println("Replayed action: " + actionToReplay);
		}
	}
	
	/**
	 * Switch between the "auto-replay" setting and the 
	 * "step-by-step" setting for replaying games.
	 * If switching to auto-replay mode, then start 
	 * replaying actions at regular intervals. 
	 * If switching to step-by-step mode, replay actions 
	 * manually one at a time.
	 */
	public void toggleReplayType() {
		autoReplaying = !autoReplaying;
		if (gameRecordHistory == null || gameRecordHistory.isEmpty()) { return; }
		if (autoReplaying) {
			// Switched to Auto-Replay mode - Replay actions from a loaded game in real time.
			timer.schedule(replayedAction, 
				gameRecordHistory != null && gameRecordHistory.isEmpty() ? (long)(gameRecordHistory.peek().getTimeStamp()-currentTimeInReplay) : 0L,
				(long)(DEFAULT_DELAY_MILLIS/replaySpeed));
		} else {
			// Switched to Step-By-Step Relay mode - Replay actions in a stepwise fashion.
			resetTimer();
		}
	}
	
	/**
	 * When in step-by-step replay mode, perform the next action.
	 * Adjusts the current time in the replayed game to the 
	 * recorded time that the action to replay was performed.
	 * Should only be called while in step-by-step mode. If in 
	 * auto-replay mode, nothing will happen.
	 */
	public void replayNextAction() {
		// Do nothing if auto-replaying, or if there is no game to replay
		if (autoReplaying || gameRecordHistory == null || gameRecordHistory.isEmpty()) { return; }
		currentTimeInReplay = gameRecordHistory.peek().getTimeStamp();
		replayedAction.run();
	}
	
	/**
	 * Remove the previously scheduled actions to be automatically 
	 * replayed, and create a new Timer and ActionPlayer. This does 
	 * not affect the time that the replay has been playing for.
	 */
	private void resetTimer() {
		timer.cancel();
		timer.purge();
		timer = new Timer();
		replayedAction = new ActionPlayer();
	}
	
	// ----------------------------------------------
	// ----------------- LOADING --------------------
	// ----------------------------------------------
	
	/**
	 * Load a JSON file of a game replay and store it in the 
	 * gameHistory queue.
	 * @param file is the name of the file to load
	 * @throws IOException
	 * @throws NullPointerException
	 */
	public void loadGameReplay(File file) throws IOException, NullPointerException, com.google.gson.JsonSyntaxException {
		try (Reader reader = Files.newBufferedReader(file.toPath())) {
			gameRecordHistory = new ArrayDeque<>(
					Arrays.asList(new Gson().fromJson(reader, ActionRecord[].class)));
		}
		// Reset fields
		autoReplaying = false;
		currentTimeInReplay = 0.0;
		replaySpeed = 1.0;
		timer = new Timer();
		replayedAction = new ActionPlayer();
	}
	
	// ----------------------------------------------
	//  TESTING (WON'T BE INCLUDED IN FINAL PRODUCT) 
	// ----------------------------------------------
	
	/**
	 * Main method for testing
	 * @param args
	 */
	public static void main(String[] args) {
		Recorder recorder = new Recorder(1, 100.0);
		Replayer replayer = new Replayer(null);
		
		recorder.recordNewAction(Direction.WEST, 0.04);
		recorder.recordNewAction(Direction.EAST, 3.69);
		recorder.recordNewAction(Direction.EAST, 6.69);
		recorder.recordNewAction(Direction.NORTH, 9.69); 
		recorder.recordNewAction(Direction.SOUTH, 12.69);
		recorder.recordNewAction(Direction.WEST, 15.69);
		
		/*TODO fix this String filename = recorder.saveGame();
		try { replayer.loadGameReplay(filename); }
		catch (Exception e) { }
		replayer.replayNextAction();
		replayer.toggleReplayType();
		replayer.setReplaySpeed(1.0);*/
		//loadGameReplay(filename);
	}
}