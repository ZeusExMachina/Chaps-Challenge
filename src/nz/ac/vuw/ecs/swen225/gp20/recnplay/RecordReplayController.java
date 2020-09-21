package nz.ac.vuw.ecs.swen225.gp20.recnplay;

import nz.ac.vuw.ecs.swen225.gp20.application.gameGUI;

/**
 * Communicates with the Application and the Persistence modules
 * to record and save the current game, or to load and replay a 
 * saved game.
 * @author Elijah Guarina
 */
public class RecordReplayController {
	
	// ----------------------------------------
	// --------------- FIELDS -----------------
	// ----------------------------------------
	
	/**
	 * Records moves done in the current game by the player.
	 */
	private Recorder recorder;
	/**
	 * Replays moves from a loaded game record.
	 */
	private Replayer replayer;
	
	// ----------------------------------------
	// ------------ CONSTRUCTOR ---------------
	// ----------------------------------------
	
	/**
	 * Create a new RecordReplayController that is associated 
	 * with a given GUI and a given Persistence module.
	 * @param ui is the GUI of this game
	 */
	public RecordReplayController(gameGUI ui) {
	  // TODO: Add Persistence Module object as another parameter, and also add it as an argument to the Replayer object
	  this.recorder = new Recorder();
	  this.replayer = new Replayer(ui);
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
	  recorder.recordNewAction(actorName, action, direction, timeStamp);
	}
	
	/**
	 * Save the current game history stored in the Recorder.
	 * @param filename is the name to save the file as
	 */
	public void saveGame(String filename) {
	  recorder.saveGame(filename);
	}
	
	// ----------------------------------------
	// --------------- REPLAY -----------------
	// ----------------------------------------
	
	/**
	 * Check whether or not a replayed game is currently being 
	 * auto-replayed, or if it is replaying the actions in a 
	 * step-by-step manner.
	 * @return true if auto-replaying,
	 * 		   false if replaying step-by-step
	 */
	public boolean isAutoReplaying() {
		return replayer.isAutoReplaying();
	}
	
	/**
	 * Get the currently set replay speed for replaying games.
	 * @return the replay speed
	 */
	public double getReplaySpeed() {
		return replayer.getReplaySpeed();
	}
	
	/**
	 * Switch between the "auto-replay" setting and the 
	 * "step-by-step" setting for replaying games.
	 */
	public void toggleReplayType() {
		replayer.toggleReplayType();
	}
	
	/**
	 * Set the replay speed for the recorded game to be replayed at.
	 * Only accepts speeds from replaySpeeds.
	 * @param speed is the new replay speed to set
	 */
	public void setReplaySpeed(double speed) {
		replayer.setReplaySpeed(speed);
	}
	
	/**
	 * When in step-by-step replay mode, perform the next action.
	 */
	public void stepReplayForward() {
		replayer.replayNextAction();
	}
	
	/**
	 * Load a game with a given filename.
	 * @param filename is the name of the file to load. Preferably done by getting the filename using a file chooser (e.g. JFileChooser)
	 */
	public void loadGameReplay(String filename) {
	  replayer.loadGameReplay(filename);
	}
	
	// ----------------------------------------------
	//  TESTING (WON'T BE INCLUDED IN FINAL PRODUCT) 
	// ----------------------------------------------
	
	/**
	 * Perform testing
	 */
	public void test() {
		recorder.recordNewAction("Bob", ActionRecord.Action.MOVE, ActionRecord.MoveDirection.LEFT, 5.04);
		recorder.recordNewAction("James", ActionRecord.Action.PICKUP_KEY, ActionRecord.MoveDirection.RIGHT, 10.69);
		
		String filename = "recorder_test.json";
		saveGame(filename);
		loadGameReplay(filename);
		//stepReplayForward();
		toggleReplayType();
	}
	
	/**
	 * Main method for testing
	 * @param args
	 */
	public static void main(String[] args) {
		RecordReplayController rrc = new RecordReplayController(null);
		rrc.test();
	}
}