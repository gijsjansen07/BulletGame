package Programming.CBL;

/**
 * Actor class is an abstract class that is the root class of all actors within the 
 * game that have a position on screen and a health value.
 * 
 */
public abstract class Actor {
    protected int health;
    protected int[] pos  = new int[2]; 

    /**
     * Moves the actor to the newPos value.
     * @param newPos position the actor needs to move to
     */
    public abstract void move(int[] newPos);
    
    /**
     * Reduces healt of the actor.
     * @param amount amount health is reduced by.
     */
    public abstract void reduceHealth(int amount);
    
    /**
     * Increases healt amount of actor.  
     * @param amount amount health is increased by.
     */
    public abstract void increaseHealth(int amount);
}
