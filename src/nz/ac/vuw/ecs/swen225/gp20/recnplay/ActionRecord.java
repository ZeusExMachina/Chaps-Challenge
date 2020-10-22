package nz.ac.vuw.ecs.swen225.gp20.recnplay;

import nz.ac.vuw.ecs.swen225.gp20.maze.Direction;

/**
 * Represents an action done by an actor in the game.
 * @author Elijah Guarina 300447332
 */
public class ActionRecord {
	
	// ------------------------------------------------
	// ------------------- FIELDS ---------------------
	// ------------------------------------------------

	/**
	 * Describes the direction the action moved in for this recorded action.
	 */
	private final Direction direction;
	/**
	 * The time of the game at which the action was performed.
	 */
	private final double timeStamp;
	
	// ------------------------------------------------
	// ----------------- CONSTRUCTOR ------------------
	// ------------------------------------------------
	
	/**
	 * Create a new record of an action with a given actor, information 
	 * about what the actor did, and the time at which the action was performed.
	 * @param dir is the direction the actor moved in
	 * @param time is the time stamp at which this action was performed
	 */
	public ActionRecord(Direction dir, double time) {
		this.direction = dir;
		this.timeStamp = time;
	}
	
	// ------------------------------------------------
	// ------------- GETTERS & SETTERS ----------------
	// ------------------------------------------------
	
	/**
	 * Get the direction that the actor moved in
	 * @return the direction the actor moved in for this action
	 */
	public Direction getMoveDirection() {
		return direction;
	}
	
	/**
	 * Get the time stamp at which this action was performed
	 * @return the time this action was performed
	 */
	public double getTimeStamp() {
		return timeStamp;
	}
	
	/**
	 * Override the default toString() method.
	 */
	@Override
	public String toString() {
		return direction.toString().concat(",").concat(Double.toString(timeStamp));
	}
}