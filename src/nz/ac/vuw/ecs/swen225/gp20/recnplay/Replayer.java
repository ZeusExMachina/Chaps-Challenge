package nz.ac.vuw.ecs.swen225.gp20.recnplay;

import java.util.Queue;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.google.gson.Gson;
import nz.ac.vuw.ecs.swen225.gp20.application.gameGUI;

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
	
	// TODO: Add the Persistence module object as a field
	
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
	 * The period between two replayed actions when the replay speed is 1.0x (only during auto-replay mode).
	 */
	private final long DEFAULT_DELAY_MILLIS = 1500l;
	
	/**
	 * Holds the possible replay speeds that a recorded game can be replayed at.
	 */
	public static final double[] REPLAY_SPEEDS = { 0.25, 0.5, 0.75, 1.0, 1.25, 1.5, 1.75, 2.0 };
	
	// ------------------------------------------------
	// ---------------- CONSTRUCTOR -------------------
	// ------------------------------------------------
	
	/**
	 * Create a new Replayer that is associated with a given 
	 * Controller.
	 * @param gui is the GUI associated with the game
	 */
	public Replayer(gameGUI gui) {
		this.autoReplaying = false;
		this.replaySpeed = 1.0;
		this.timer = new Timer();
		this.replayedAction = new ActionPlayer(gui);
		// TODO: Add the Persistence module object to this constructor
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
	 */
	public void setReplaySpeed(double speed) {
		// First check if the entered replay speed is valid, if so set it.
		// Speed must be 0.0 < x < 2.0, and must be divisible by 0.25.
		if (speed < 0.0 || speed > 2.0 || speed%0.25 != 0) { 
			throw new IllegalArgumentException("Invalid replay speed value of " + speed + " entered.");
		} else {
			replaySpeed = speed;
			timer.cancel();
			timer.purge();
			timer.schedule(replayedAction, (long)(DEFAULT_DELAY_MILLIS*replaySpeed), (long)(DEFAULT_DELAY_MILLIS*replaySpeed));
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
		 * The GUI associated with the game.
		 */
		private gameGUI gui;
		
		/**
		 * Create a new ActionPlayer to play through recorded actions.
		 * @param ui is the GUI associated with the games
		 */
		public ActionPlayer(gameGUI ui) {
			this.gui = ui;
		}
		
		/**
		 * Perform a move on the GUI based on the values of the ActionRecord.
		 */
		@Override
		public void run() {
			if (gameRecordHistory == null || gameRecordHistory.isEmpty()) { return; }
			ActionRecord actionToReplay = gameRecordHistory.poll();
			// TODO: add the logic for deciding what move to make for which actor
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
		if (autoReplaying) {
			// Switch to Auto-Replay mode - Regularly replay actions from a loaded game.
			timer.schedule(replayedAction, (long)(DEFAULT_DELAY_MILLIS*replaySpeed), (long)(DEFAULT_DELAY_MILLIS*replaySpeed));
		} else {
			// Switch to Step-By-Step Relay mode - Replay actions in a stepwise fashion.
			timer.cancel();
			timer.purge();
		}
	}
	
	/**
	 * When in step-by-step replay mode, perform the next action.
	 */
	public void replayNextAction() {
		if (autoReplaying) { return; }
		replayedAction.run();
	}
	
	// ----------------------------------------------
	// ----------------- LOADING --------------------
	// ----------------------------------------------
	
	/**
	 * Load a JSON file of a game replay and store it in the 
	 * gameHistory queue.
	 * @param filename is the name of the file to load
	 */
	public void loadGameReplay(String filename) {
		try {
			Reader reader = Files.newBufferedReader(Paths.get(filename));
			gameRecordHistory = new ArrayDeque<ActionRecord>(
					Arrays.asList(new Gson().fromJson(reader, ActionRecord[].class)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}