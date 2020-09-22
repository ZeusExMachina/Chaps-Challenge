package nz.ac.vuw.ecs.swen225.gp20.recnplay;

import java.util.Queue;
import java.util.ArrayDeque;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;

/**
 * Stores the history of actions done by actors in a game.
 * Can also save the current history as a JSON file.
 * @author Elijah Guarina
 */
public class Recorder {
	
	// ----------------------------------------
	// --------------- FIELDS -----------------
	// ----------------------------------------
	
	/**
	 * Stores the history of actions done by actors as a Queue of ActionRecords.
	 */
	private final Queue<ActionRecord> actionHistory;
	
	// ----------------------------------------
	// ------------ CONSTRUCTOR ---------------
	// ----------------------------------------
	
	/**
	 * Make a new Recorder with an empty actions history.
	 */
	public Recorder() {
		this.actionHistory = new ArrayDeque<ActionRecord>();
	}
	
	// ----------------------------------------
	// --------------- RECORD -----------------
	// ----------------------------------------
	
	/**
	 * Add a new action to the action history stored in the Recorder.
	 * @param actorName is a String holding the name of the entity that performed this action
	 * @param action describes what happened for this action (e.g. if actor unlocked a door, picked up a key, or just moved, etc.)
	 * @param direction determines which direction the actor moved in
	 * @param timeStamp is the time at which this action was performed
	 */
	public void recordNewAction(String actorName, ActionRecord.Action action, ActionRecord.MoveDirection direction, double timeStamp) {
		actionHistory.add(new ActionRecord(actorName, action, direction, timeStamp));
	}
	
	// ----------------------------------------
	// ---------------- SAVE ------------------
	// ----------------------------------------
	
	/**
	 * Save the game history as a JSON file.
	 * @param filename is what the resulting file will be named
	 */
	public void saveGame(String filename) {
		// TODO: Add the level/stage number so that when replaying, Replayer object can tell Persistence module to load a new game with a particular level
		try {
			Writer writer = Files.newBufferedWriter(Paths.get(filename));
			new Gson().toJson(actionHistory, writer);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}