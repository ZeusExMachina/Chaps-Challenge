package nz.ac.vuw.ecs.swen225.gp20.recordAndReplay;

import java.util.Queue;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Arrays;

/**
 * Loads and plays through game replays for Chap's Challenge.
 * @author Elijah Guarina
 */
public class Replayer {
	/**
	 * The Controller that this Replayer is associated with.
	 */
	private final RecordReplayController controller;
	/**
	 * Stores the history of actions done by actors from a loaded game replay.
	 */
	private Queue<ActionRecord> gameRecordHistory;
	private boolean autoReplaying;
	private double replaySpeed;
	
	/**
	 * Holds the possible replay speeds that a recorded game can be replayed at.
	 */
	public static final double[] replaySpeeds = { 0.25, 0.5, 0.75, 1.0, 1.25, 1.5, 1.75, 2.0 };
	
	/**
	 * Create a new Replayer that is associated with a given 
	 * Controller.
	 * @param control is this Replayer's associated Controller
	 */
	public Replayer(RecordReplayController control) {
		this.controller = control;
		this.autoReplaying = false;
		this.replaySpeed = 1.0;
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
	 * Switch between the "auto-replay" setting and the 
	 * "step-by-step" setting for replaying games.
	 */
	public void toggleReplayType() {
		autoReplaying = !autoReplaying;
	}
	
	/**
	 * Set the replay speed for the recorded game to be replayed at.
	 * Only accepts speeds from replaySpeeds.
	 * @param speed is the new replay speed to set
	 */
	public void setReplaySpeed(double speed) {
		// First check if the entered replay speed is valid, if so set it
		if (speed < 0.0 || speed > 2.0 || speed%0.25 != 0) { 
			throw new IllegalArgumentException("Invalid replay speed value of " + speed + " entered.");
		} else {
			replaySpeed = speed;
		}
	}
	
	// TODO: Add functionality to play loaded games
	
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
