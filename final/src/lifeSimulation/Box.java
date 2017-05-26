package lifeSimulation;

import javafx.fxml.FXML;

/**
 * Created by meyera on 5/26/17.
 */
public class Box {

    @FXML private boolean alive;
    @FXML private double positionX;
    @FXML private double positionY;

    public Box(double positionX, double positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public void liveOrDie(Boolean alive) {
        this.alive = alive;
    }

    public boolean amIAlive() {
        return alive;
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }
}
