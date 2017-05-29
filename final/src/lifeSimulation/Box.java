/**
 * Box.java
 * Created by Patty Commins and Anna Meyer on 5/26/17.
 *
 * Box class for Game of Life simulation, Software Design final project Spring 2017
 */

package lifeSimulation;

import javafx.fxml.FXML;


public class Box {

    @FXML private boolean alive;
    @FXML private double positionX;
    @FXML private double positionY;

    /**
     * Constructor
     *
     * @param positionX column number
     * @param positionY row number
     */
    public Box(double positionX, double positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    /**
     * Change the box's life status to a given parameter
     *
     * @param alive True if we want box to be alive in next iteration; false if we want box
     *              to be dead in next iteration
     */
    public void changeLifeStatus(Boolean alive) { this.alive = alive; }


    /**
     * Allows the controller to access a given box's life status.
     *
     * @return True if alive. False otherwise.
     */
    public boolean amIAlive() {
        return alive;
    }

    /**
     * Allows the controller to access a given box's x-coordinate (it's column)
     *
     * @return numerical value corresponding to column number containing the box
     */
    public double getPositionX() {
        return positionX;
    }

    /**
     * Allows the controller to access a given box's y-coordinate (it's row)
     *
     * @return numerical value corresponding to row number containing the box
     */
    public double getPositionY() {
        return positionY;
    }
}
