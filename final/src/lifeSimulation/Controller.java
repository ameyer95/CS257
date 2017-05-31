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
    private ArrayList<Box> BoxList = new ArrayList<Box>();
//Make box extend node so that this'll work?

    private int numberOfRows = 20;
    private int numberOfCols = 40;

    public void initialize() {
        for (int row = 0; row < numberOfRows; row ++) {
            for (int col = 0; col < numberOfCols; col ++) {
                gameBoard.add(new Button(), col, row);
                BoxList.add(new Box(col, row));
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
        ArrayList<Box> aliveList = new ArrayList<Box>();
        ArrayList<Box> deadList = new ArrayList<Box>();
        Boolean aliveBox;
        int XPos;
        int YPos;

        for (int i=0; i < BoxList.size(); i++) {
           aliveBox = findNeighbors(BoxList.get(i), i);
           if (aliveBox) {
               aliveList.add(BoxList.get(i));
           }
           else {
               deadList.add(BoxList.get(i));
           }
        }
        for (int j = 0; j < aliveList.size(); j++) {
            aliveList.get(j).changeLifeStatus(true);
            XPos = aliveList.get(j).getPositionX();
            YPos = aliveList.get(j).getPositionY();
            colorSquareAlive(XPos, YPos);
        }
        for (int k = 0; k < deadList.size(); k++) {
            deadList.get(k).changeLifeStatus(false);
            XPos = deadList.get(k).getPositionX();
            YPos = deadList.get(k).getPositionY();
            colorSquareDead(XPos, YPos);
        }
    }

    private Boolean findNeighbors(Box thisBox, int i) {
        int numberOfNeighborsAlive = 0;
        ArrayList<Integer> listOfNeighbors = new ArrayList<Integer>();
        listOfNeighbors.addAll(findSideNeighbors(i));
        listOfNeighbors.addAll(findAboveNeighbors(i));
        listOfNeighbors.addAll(findBelowNeighbors(i));

        for (int j = 0; j < listOfNeighbors.size(); j++) {
            if (BoxList.get(j).amIAlive()) {
                numberOfNeighborsAlive ++;
            }
        }

        Boolean lifeStatus = thisBox.amIAlive();
        if (numberOfNeighborsAlive < 2) {
            return false;
        }
        else if (numberOfNeighborsAlive > 3) {
            return false;
        }
        else if (numberOfNeighborsAlive == 3 ) {
            return true;
        }
        return lifeStatus;
    }


    private ArrayList<Integer> findSideNeighbors(int i) {
        ArrayList<Integer> listOfNeighbors = new ArrayList<Integer>();
        if ((i % numberOfCols) == 0) {
            listOfNeighbors.addAll(leftColumnSpecialCase(i));
        }
        else if ((i % numberOfCols) == (numberOfCols -1 )) {
            listOfNeighbors.addAll(rightColumnSpecialCase(i));
        }
        else {
            listOfNeighbors.add(i-1);
            listOfNeighbors.add(i+1);
        }
        return listOfNeighbors;
    }

    private ArrayList<Integer> leftColumnSpecialCase(int i) {
        ArrayList<Integer> listOfNeighbors = new ArrayList<Integer>();
        listOfNeighbors.add(i+1);
        listOfNeighbors.add(i + (numberOfCols -1));
        return listOfNeighbors;
    }

    private ArrayList<Integer> rightColumnSpecialCase(int i) {
        ArrayList<Integer> listOfNeighbors = new ArrayList<Integer>();
        listOfNeighbors.add(i-1);
        listOfNeighbors.add(i - (numberOfCols -1));
        return listOfNeighbors;
    }

    private ArrayList<Integer> findAboveNeighbors(int i) {
        ArrayList<Integer> listOfNeighbors = new ArrayList<Integer>();
        if ((i % numberOfCols) == 0) {
            if (i >= numberOfCols) {
                listOfNeighbors.addAll(leftColumnSpecialCase(i - numberOfCols));
            }
            else {
                listOfNeighbors.addAll(leftColumnSpecialCase(numberOfCols * (numberOfRows - 1)));
            }
        }
        else if ((i % numberOfCols) == (numberOfCols - 1)) {
            if (i > numberOfCols) {
                listOfNeighbors.addAll(rightColumnSpecialCase(i - numberOfCols));
            }
            else {
                listOfNeighbors.addAll(rightColumnSpecialCase(numberOfCols * numberOfRows - 1));
            }
        }
        else {
            if (i < numberOfCols) {
                listOfNeighbors.add(i + (numberOfCols * (numberOfRows - 1)));
                listOfNeighbors.add(i + 1 + (numberOfCols * (numberOfRows - 1)));
                listOfNeighbors.add(i - 1 + (numberOfCols * (numberOfRows - 1)));
            }
            else {
                listOfNeighbors.add(i - numberOfCols);
                listOfNeighbors.add(i - 1 - numberOfCols);
                listOfNeighbors.add(i + 1 - numberOfCols);
            }
        }
        return listOfNeighbors;
    }

    private ArrayList<Integer> findBelowNeighbors(int i) {
        ArrayList<Integer> listOfNeighbors = new ArrayList<Integer>();
        if ((i % numberOfCols) == 0) {
            if (i < (numberOfCols*(numberOfRows - 1))) {
                listOfNeighbors.addAll(leftColumnSpecialCase(i + numberOfCols));
            }
            else {
                listOfNeighbors.addAll(leftColumnSpecialCase(i % numberOfCols));
            }
        }
        else if ((i % numberOfCols) == (numberOfCols - 1)) {
            if (i < (numberOfCols * (numberOfRows - 1))) {
                listOfNeighbors.addAll(rightColumnSpecialCase(i + numberOfCols));
            }
            else {
                listOfNeighbors.addAll(rightColumnSpecialCase(i % numberOfCols));
            }
        }
        else {
            if (i >= numberOfCols * (numberOfRows-1)) {
                listOfNeighbors.add((i % numberOfCols));
                listOfNeighbors.add((i % numberOfCols) + 1);
                listOfNeighbors.add((i % numberOfCols) - 1);
            }
            else {
                listOfNeighbors.add(i + numberOfCols);
                listOfNeighbors.add(i + 1 + numberOfCols);
                listOfNeighbors.add(i - 1 + numberOfCols);
            }
        }
        return listOfNeighbors;
    }

    private void colorSquareAlive(int XPos, int YPos) {
        javafx.scene.Node ourNode = findNodeByRowCol(YPos, XPos);
        ourNode.setStyle("-fx-base: #b6e7c9");
    }

    /**
     * We found this technique on
     * https://stackoverflow.com/questions/20825935/javafx-get-node-by-row-and-column
     *
     * @param row
     * @param col
     * @return the node corresponding to the box that we're changing to alive or dead
     */
    private javafx.scene.Node findNodeByRowCol(int row, int col) {
        javafx.scene.Node result = null;
        javafx.collections.ObservableList<javafx.scene.Node> children = gameBoard.getChildren();

        for (javafx.scene.Node node : children) {
            if(gameBoard.getRowIndex(node) == row && gameBoard.getColumnIndex(node) == col) {
                result = node;
                break;
            }
        }
        return result;
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
