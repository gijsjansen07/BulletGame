package Programming.CBL;

/**
 * 
 * Actor
 */
public abstract class Actor {
    protected int health;
    protected int[] pos  = new int[2]; 

    public abstract void move(int[] newPos);

    public abstract void reduceHealth(int amount);
    
    public abstract void increaseHealth(int amount);
}
