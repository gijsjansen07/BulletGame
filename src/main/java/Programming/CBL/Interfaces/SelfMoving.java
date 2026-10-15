package Programming.CBL.Interfaces;

public interface SelfMoving {
    /**
     * Updates the position of the selfmoving object according to the passed delta time. 
     * @param dt delta time
     * @return 
    */
    public boolean update(double dt);
}
