package nz.ac.vuw.ecs.swen225.gp20.maze;

import com.google.common.base.Preconditions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * From handout: An Actor is a game character that moves around, like Chap,
 * the hero of the game. You should also use a second type of actor in level 2
 * that interacts with Chap (for instance, by exploding and eating Chap or
 * robbing him). Unlike Chap, actors will move around on their own (randomly,
 * or following some pattern), and are not directed by user input.
 */

public abstract class Actor {
    /**
     * Stores position of actor.
     */
    protected Position position;
    /**
     * Stores direction actor previously travelled,
     * i.e. the way he's currently facing.
     */
    protected Direction direction;
    /**
     * Stores name of actor for accessing image, e.g. "chap".
     */
    protected final String name;
    
    /**
     *  Allows program to know which frames to display for actor.
     */
    protected boolean isMoving = true;
    
    /**
     * which frame to currently display for the actor.
     */
    protected int frameCounter = 0;
    
    /**
     * the max amount of frames, so program doesn't try display an image that isn't there.
     */
    protected int maxFrame = 3;
    
    /**
     * The time it takes to switch between moving animation
     * and idling animation
     */
    protected int movingCooldown = 3;
    /**
     * the current time in the cool down.
     */
    protected int movingTimer = 0;

    /**
     * Make a new actor, starting by facing south towards camera.
     *
     * @param p starting position
     * @param n name of actor
     */
    public Actor(Position p, String n) {
        position = p;
        direction = Direction.SOUTH;
        name = n;
    }

    /**
     * Get actor's name
     * @return name of actor
     */
    public String getName() {
        return name;
    }

    /**
     * Get actor's position on the board.
     *
     * @return actor's position on board
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Get the image representing actor, e.g. "chap-south.png".
     *
     * @return buffered image to display
     */
    public BufferedImage getImage() {
        String movement = (isMoving) ? "run" : "idle";
        String dir = direction.toString();
        String path = "resources/"+name+"_animation/rescaled/" + name + "_" + movement + "_" + frameCounter + "_" + dir + ".png";
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            // Go to runtime exception
        }
        throw new RuntimeException("Chap image not found");
    }
    
    /**
     * updates the frame to be displayed
     */
    public void updateFrame(){
        frameCounter = (frameCounter == maxFrame) ? 0 : frameCounter + 1;
        if(isMoving && movingTimer == movingCooldown) {
            isMoving = false;
            movingTimer = 0;
        }else movingTimer++;
        
    }
    
    /**
     * tells the character whether it is moving or not.
     */
    public void isMoving(){
        isMoving = true;
        movingTimer = 0;
    }

    /**
     * Change actor's position and direction facing.
     *
     * @param p new position
     * @param d new direction
     */
    public void move(Position p, Direction d) {
        if (d.equals(Direction.NORTH)) {
            Preconditions.checkArgument(p.getX() == position.getX(), "Moving north should keep x constant");
            Preconditions.checkArgument(p.getY() == position.getY() - 1, "Moving north should move y 1 down");
        } else if (d.equals(Direction.SOUTH)) {
            Preconditions.checkArgument(p.getX() == position.getX(), "Moving south should keep x constant");
            Preconditions.checkArgument(p.getY() == position.getY() + 1, "Moving south should move y 1 down");
        } else if (d.equals(Direction.WEST)) {
            Preconditions.checkArgument(p.getY() == position.getY(), "Moving west should keep y constant");
            Preconditions.checkArgument(p.getX() == position.getX() - 1, "Moving west should move x 1 down");
        } else if (d.equals(Direction.EAST)) {
            Preconditions.checkArgument(p.getY() == position.getY(), "Moving east should keep y constant");
            Preconditions.checkArgument(p.getX() == position.getX() + 1, "Moving east should move x 1 up");
        }
        position = p;
        direction = d;
    }

    /**
     * Move a secondary actor the next step on its path
     * @param m maze instance to verify it can move to next path
     */
    public abstract void move(Maze m);

    /**
     * Get a single character String code that represents the actor
     * @return a String with length 1
     */
    public abstract String getCode();
}
