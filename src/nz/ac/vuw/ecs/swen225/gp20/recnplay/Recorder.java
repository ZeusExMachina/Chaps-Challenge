package nz.ac.vuw.ecs.swen225.gp20.recnplay;

import java.util.Queue;
import java.util.ArrayDeque;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import com.google.gson.Gson;

import nz.ac.vuw.ecs.swen225.gp20.maze.Direction;

/**
 * Stores the history of actions done by actors in a game.
 * Can also save the current history as a JSON file.
 * @author Elijah Guarina 300447332
 */
public class Recorder {
	
	// ----------------------------------------
	// --------------- FIELDS -----------------
	// ----------------------------------------
	
	/**
	 * Stores the history of actions done by actors as a Queue of ActionRecords.
	 */
	private final Queue<ActionRecord> actionHistory;
	/**
	 * The level number for the game being recorded.
	 */
	private final int levelNum;
	/**
	 * The starting time amount of the level for the game being recorded.
	 */
	private final double startingTime;
	/**
	 * A string for the regex for a valid file name.
	 */
	public static final String VALID_FILE_FORMAT_PAT = "chap-record_level\\d_\\d{1,2}\\-\\d{1,2}\\-\\d{4}_\\d{1,2}\\-\\d{1,2}.json";
	
	// ----------------------------------------
	// ------------ CONSTRUCTOR ---------------
	// ----------------------------------------
	
	/**
	 * Make a new Recorder with an empty actions history.
	 * @param lvlNum is the level number for the game being recorded.
	 * @param startTime is the starting time amount of the 
	 * 			level for the game being recorded
	 */
	public Recorder(int lvlNum, double startTime) {
		this.actionHistory = new ArrayDeque<>();
		this.levelNum = lvlNum;
		this.startingTime = startTime;
	}
	
	// ----------------------------------------
	// --------------- RECORD -----------------
	// ----------------------------------------
	
	/**
	 * Add a new action to the action history stored in the Recorder.
	 * @param direction determines which direction the actor moved in
	 * @param timeStamp is the time at which this action was performed
	 */
	public void recordNewAction(Direction direction, double timeStamp) {
		actionHistory.add(new ActionRecord(direction, startingTime-timeStamp));
	}
	
	// ----------------------------------------
	// ---------------- SAVE ------------------
	// ----------------------------------------
	
	/**
	 * Generate the name for a recorded game file.
	 * @return a String of the save file name
	 */
	private String generateSaveFileName() {
		LocalDateTime currentDateTime = LocalDateTime.now();
		return "chap-record_level" + levelNum
				+ "_" + currentDateTime.getDayOfMonth() + "-" + currentDateTime.getMonthValue() + "-" + currentDateTime.getYear()
				+ "_" + currentDateTime.getHour() + "-" + currentDateTime.getMinute()
				+ ".json";
	}
	
	/**
	 * Save the game history as a JSON file.
	 * @return the name of the saved file, or null if the file saving failed
	 */
	public String saveGame() {
		String fileName = null;
		try {
			Writer writer = Files.newBufferedWriter(Paths.get("replays", generateSaveFileName()));
			new Gson().toJson(actionHistory, writer);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileName;
	}
}