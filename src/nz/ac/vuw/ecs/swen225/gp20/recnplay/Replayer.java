package nz.ac.vuw.ecs.swen225.gp20.recnplay;

import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import nz.ac.vuw.ecs.swen225.gp20.application.GameGUI;

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
	private final GameGUI gui;
	/**
	 * The level of the currently loaded game record.
	 */
	private int level;
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
	private final static long DEFAULT_DELAY_MILLIS = 100L;
	
	/**
	 * Holds the possible replay speeds that a recorded game can be replayed at.
	 */
	public static final List<Double> REPLAY_SPEEDS = Collections.unmodifiableList(Arrays.asList(0.25, 0.5, 0.75, 1.0, 1.25, 1.5, 1.75, 2.0));
	
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
		this.level = -1;
		this.autoReplaying = false;
		this.replaySpeed = 1.0;
		this.currentTimeInReplay = 0.0;
		this.replayedAction = new ActionPlayer();
	}
	
	// ------------------------------------------------
	// ------------- GETTERS & SETTERS ----------------
	// ------------------------------------------------
	
	/**
	 * Get the level of the loaded game record.
	 * @return the level of the loaded game record, or -1 if no game is loaded
	 */
	public int getRecordingLevel() {
		return level;
	}
	
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
	 * @throws IllegalArgumentException when an invalid speed value is given
	 */
	public void setReplaySpeed(double speed) throws IllegalArgumentException {
		// First check if the entered replay speed is valid, if so set it.
		// Speed must be 0.0 < x < 2.0, and must be divisible by 0.25.
		if (!REPLAY_SPEEDS.contains(speed)) { 
			throw new IllegalArgumentException("Invalid replay speed value of " + speed + " entered.");
		} else {
			replaySpeed = speed;
			if (autoReplaying || gameRecordHistory == null || gameRecordHistory.isEmpty()) {
				stopTimer();
				playTimer();
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
			// Only perform an action if there is a loaded game that has not been finished
			if (gameRecordHistory != null && !gameRecordHistory.isEmpty()) {
				// If the next action was performed at this time in the recorded game, replay the action
				if (gameRecordHistory.peek().getTimeStamp() <= currentTimeInReplay) {
					gui.moveCalled(gameRecordHistory.poll().getMoveDirection());
				}
				// Check if the replay has finished. If so, stop auto-replaying.
				if (gameRecordHistory.isEmpty()) {
					endCurrentReplay();
					return;
				}
			}
			// If in auto-replay mode, increment the replay time.
			if (autoReplaying) { currentTimeInReplay += DEFAULT_DELAY_MILLIS/1000.0; }
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
			playTimer();
		} else {
			// Switched to Step-By-Step Relay mode - Replay actions in a stepwise fashion.
			stopTimer();
			// TODO: Need to also stop timer of Application
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
	 * Clear the queue of any loaded game record and stop replaying.
	 */
	public void endCurrentReplay() {
		stopTimer();
		if (gameRecordHistory != null) { gameRecordHistory.clear(); }
		currentTimeInReplay = 0.0;
	}
	
	/**
	 * Stop the current timer from automatically replaying actions from a game recording.
	 */
	private void stopTimer() {
		if (timer != null) {
			timer.cancel();
			timer.purge();
		}
	}
	
	/**
	 * Start/Resume the timer to automatically replay actions from a game recording.
	 */
	private void playTimer() {
		timer = new Timer();
		replayedAction = new ActionPlayer();
		timer.schedule(replayedAction, 
				(long)(gameRecordHistory.peek().getTimeStamp()-currentTimeInReplay), (long)(DEFAULT_DELAY_MILLIS/replaySpeed));
	}
	
	// ----------------------------------------------
	// ----------------- LOADING --------------------
	// ----------------------------------------------
	
	/**
	 * Load a JSON file of a game replay and store it in the 
	 * gameHistory queue.
	 * @param file is the name of the file to load
	 * @throws IOException when an error occurs when reading the file
	 * @throws IllegalArgumentException when a file with an invalid filename is passed
	 * @throws NullPointerException when there is no file passed
	 * @throws JsonSyntaxException when there is an error parsing the JSON into ActionRecords
	 */
	public void loadGameReplay(File file) throws IOException, IllegalArgumentException, NullPointerException, JsonSyntaxException {
		endCurrentReplay();
		// Check for a valid file name
		if (!Pattern.matches(Recorder.VALID_FILE_FORMAT_PAT, file.getName())) {
			throw new IllegalArgumentException("Invalid file name " + file.getName());
		}
		// Parse the file, create the game history, and set the level
		int lvl = Integer.parseInt(String.valueOf(file.getName().charAt(17)));
		try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
			gameRecordHistory = new ArrayDeque<>(
					Arrays.asList(new Gson().fromJson(reader, ActionRecord[].class)));
		}
		level = lvl;
	}
}