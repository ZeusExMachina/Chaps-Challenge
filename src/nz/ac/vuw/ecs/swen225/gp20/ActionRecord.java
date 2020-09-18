package nz.ac.vuw.ecs.swen225.gp20.recordAndReplay;

/**
 * Represents an action done by an actor in the game.
 * @author Elijah Guarina
 */
public class ActionRecord {
	private final String actorName;
	private final Action action;
	private final MoveDirection direction;
	private final double timeStamp;
	
	public static enum Action { MOVE, PICKUP_KEY, PICKUP_CHIP, UNLOCK_DOOR, UNLOCK_EXIT_LOCK, EXIT_LEVEL }
	public static enum MoveDirection { UP, LEFT, DOWN, RIGHT }
	
	/**
	 * Create a new record of an action with a given actor, information 
	 * about what the actor did, and the time at which the action was performed.
	 * @param actor is the name of the actor that performed this action
	 * @param act is the type of action performed
	 * @param dir is the direction the actor moved in
	 * @param time is the time stamp at which this action was performed
	 */
	public ActionRecord(String actor, Action act, MoveDirection dir, double time) {
		this.actorName = actor;
		this.action = act;
		this.direction = dir;
		this.timeStamp = time;
	}
	
	/**
	 * Get the name of the actor of this ActionRecord.
	 * @return the name of the actor of this ActionRecord
	 */
	public String getActorName() {
		return actorName;
	}
	
	/**
	 * Get the action of this ActionRecord
	 * @return the action performed
	 */
	public Action getAction() {
		return action;
	}
	
	/**
	 * Get the direction that the actor moved in
	 * @return the direction the actor moved in for this action
	 */
	public MoveDirection getMoveDirection() {
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
		return actorName.concat(action.toString()).concat(direction.toString())
				.concat(Double.toString(timeStamp));
	}
}
