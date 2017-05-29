/**
 * Controller.java
 * Created by Patty Commins and Anna Meyer
 *
 * Controller for the Game of Life Simulation, Software Design final project, Spring 2017
 */

package lifeSimulation;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import java.util.ArrayList;

import java.util.Timer;
import java.util.TimerTask;


public class Controller implements EventHandler<MouseEvent> {
    final private double FRAMES_PER_SECOND = 20.0;

    @FXML private Button pauseButton;
    @FXML private Button playButton;
    @FXML private Label timeKeeperLabel;
    @FXML private GridPane gameBoard;

    private int score; //number of boxes that are alive
    private boolean paused;
    private Timer timer;
    private ArrayList<Box> BoxList;
//Make box extend node so that this'll work?


    public void initialize() {
        for (int row = 0; row < 20; row ++) {
            for (int col = 0; col < 40; col ++) {
                gameBoard.add(new Button(), col, row);
                BoxList.add(new Box((double) col, (double) row));
            }
        }
    }
    /**
     * Constructor
     */
    public Controller() {
        this.paused = true;
        this.score = 0;
    }

    /**
     * Starts and maintains the timer
     */
    private void startTimer() {
        this.timer = new java.util.Timer();
        TimerTask timerTask = new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        updateAnimation();
                    }
                });
            }
        };

        long frameTimeInMilliseconds = (long)(1000.0 / FRAMES_PER_SECOND);
        this.timer.schedule(timerTask, 0, frameTimeInMilliseconds);
    }

    /**
     * Iterate through the grid to update the simulation to the next stage.
     * Check positions and neighbors and recolor boxes as necessary
     */
    private void updateAnimation() {
        // check positions & neighbors and recolor boxes as necessary
    }

    /**
     * Interprets when the user clicks on the screen
     *
     * @param click
     */
    @Override
    public void handle(MouseEvent click) {
        Double Xcoord = click.getScreenX();
        Double Ycoord = click.getScreenY();
    }

    /**
     * Pause the simulation
     *
     * @param actionEvent
     */
    public void onPauseButton(ActionEvent actionEvent) {
        if (!this.paused) {
            this.timer.cancel();
        }
        this.paused = true;
    }

    /**
     * Play the simulation
     *
     * @param actionEvent
     */
    public void onPlayButton(ActionEvent actionEvent) {
        if (this.paused) {
            this.startTimer();
        }
        this.paused = false;
    }
}
