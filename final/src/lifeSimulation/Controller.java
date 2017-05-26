package lifeSimulation;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.Timer;
import java.util.TimerTask;


public class Controller implements EventHandler<MouseEvent> {
    final private double FRAMES_PER_SECOND = 20.0;

    @FXML private Button pauseButton;
    @FXML private Button playButton;
    @FXML private Label timeKeeperLabel;
    @FXML private AnchorPane gameBoard;
    //use a for loop to create lots of boxes. Maybe determine # of boxes based on size of window? Otherwise could have a fixed number.

    private int score; //number of boxes that are alive
    private boolean paused;
    private Timer timer;

    public Controller() {
        this.paused = true;
        this.score = 0;
    }

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
    private void updateAnimation() {
        // check positions & neighbors and recolor boxes as necessary
    }

    @Override
    public void handle(MouseEvent click) {
        Double Xcoord = click.getScreenX();
        Double Ycoord = click.getScreenY();
    }

    public void onPauseButton(ActionEvent actionEvent) {
        if (!this.paused) {
            this.timer.cancel();
        }
        this.paused = true;
    }

    public void onPlayButton(ActionEvent actionEvent) {
        if (this.paused) {
            this.startTimer();
        }
        this.paused = false;
    }
}
