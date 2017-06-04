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
import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import jdk.internal.util.xml.impl.Pair;
import java.util.LinkedList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.Timer;
import java.util.TimerTask;


public class Controller implements EventHandler<MouseEvent> {
    final private double FRAMES_PER_SECOND = 2.0;

    @FXML private Button pauseButton;
    @FXML private Button playButton;
    @FXML private Button resetButton;
    @FXML private Label timeKeeperLabel;
    @FXML private Label scoreLabel;
    @FXML private Text helpSection;
    @FXML private Rectangle helpRectangle;
    @FXML private Button questionButton;
    @FXML private AnchorPane gameBoard;
    @FXML private Polygon graphArrow;
    @FXML private NumberAxis xAxis;
    @FXML private NumberAxis yAxis;
    @FXML private LineChart lineChart;
    @FXML private XYChart.Series series = new XYChart.Series();

    private int score; //number of boxes that are alive
    private int time = 0;
    private boolean paused;
    private boolean helpBoxVisible;
    private boolean graphVisible = true;
    private Timer timer;

    // This is the Model
    private ArrayList<Boolean> BoxList = new ArrayList<Boolean>();
    private LinkedList<Integer> dataList = new LinkedList<Integer>();

    private int numberOfRows = 30;
    private int numberOfCols = 40;

    private int gameBoardWidth = 800;     //get from Main() window width
    private int gameBoardHeight = 600;    // get from Main() window height - top AnchorPane in FXML file
    private double boxWidth = gameBoardWidth / numberOfCols;

    public void initialize() {
        //make arrow clearish
        graphArrow.setStyle("-fx-opacity:0.75;");
        this.xAxis.setAutoRanging(true);
        this.yAxis.setAutoRanging(true);

        //draw vertical lines on gameBoard
        for (int col = 1; col < numberOfCols; col++){
            Line line = new Line();
            line.setStartX(col * boxWidth);
            line.setStartY(0);
            line.setEndX(col * boxWidth);
            line.setStartY(gameBoardHeight);
            line.setId("lineCol" + col);
            gameBoard.getChildren().add(line);
        }

        //draw horizontal lines on gameBoard
        for (int row = 1; row < numberOfRows; row++){
            Line line = new Line();
            line.setStartX(0);
            line.setStartY(row * boxWidth);
            line.setEndX(gameBoardWidth);
            line.setEndY(row * boxWidth);
            line.setId("lineRow" + row);
            gameBoard.getChildren().add(line);
        }

        //create list of boxes, initialize to be dead
        for (int row = 0; row < numberOfRows; row ++) {
            for (int col = 0; col < numberOfCols; col ++) {
                Rectangle box = new Rectangle();
                box.setY(row*(gameBoardWidth/numberOfCols));
                box.setX(col*(gameBoardHeight/numberOfRows));
                box.setWidth(boxWidth);
                box.setHeight(boxWidth);
                box.setFill(Color.LIGHTGRAY);
                box.setStroke(Color.BLACK);
                box.setId(row + "_" + col);
                gameBoard.getChildren().add(box);
                BoxList.add(false);
            }
        }
        hideGraph();

        gameBoard.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                handleClick(event);
            }
        });
    }

    /**
     * Constructor
     */
    public Controller() {
        this.paused = true;
        this.helpBoxVisible=true;
        this.score = 0;
        this.time = 0;
    }

    private void hideGraph() {
        this.graphVisible = false;
        this.lineChart.toBack();
        this.xAxis.toBack();
        this.yAxis.toBack();
    }

    private void showGraph() {
        this.graphVisible = true;
        this.lineChart.toFront();
        this.xAxis.toFront();
        this.yAxis.toFront();
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
                        time ++;
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
        ArrayList<Integer> aliveList = new ArrayList<Integer>();
        ArrayList<Integer> deadList = new ArrayList<Integer>();
        Boolean aliveBox;
        dataList.add(score);
        int col;
        int row;

        for (int i=0; i < BoxList.size(); i++) {
            aliveBox = findNeighbors(BoxList.get(i), i);
            if (aliveBox) {
                aliveList.add(i);
            }
            else {
                deadList.add(i);
            }
        }
        this.score = aliveList.size();
        this.timeKeeperLabel.setText("Generation: " + this.time);
        this.scoreLabel.setText("Population: " + this.score);
        for (int j = 0; j < aliveList.size(); j++) {
            BoxList.set(aliveList.get(j),true);
            col = aliveList.get(j) % numberOfCols;
            row = (aliveList.get(j) - col) / numberOfCols;
            colorSquareAlive(row, col);
        }
        for (int k = 0; k < deadList.size(); k++) {
            BoxList.set(deadList.get(k), false);
            col = deadList.get(k) % numberOfCols;
            row = (deadList.get(k) - col) / numberOfCols;
            colorSquareDead(row, col);
        }
        this.series.getData().add(new XYChart.Data(time, score));
    }

    /**
     * Counts the number of i's neighbors who are alive and specifies the color of i for the next generation accordingly
     * @param alive includes the current life status of cell i to simplify the case where i has 2 alive neighbors
     * @param i the index in BoxList of the cell
     * @return boolean value specifying the lifeStatus of cell i in the next generation of the simulation
     */
    private Boolean findNeighbors(boolean alive, int i) {
        int numberOfNeighborsAlive = 0;
        ArrayList<Integer> listOfNeighbors = new ArrayList<Integer>();
        listOfNeighbors.addAll(findSideNeighbors(i));
        listOfNeighbors.addAll(findAboveNeighbors(i));
        listOfNeighbors.addAll(findBelowNeighbors(i));

        for (int j = 0; j < listOfNeighbors.size(); j++) {
            if (BoxList.get(listOfNeighbors.get(j))) {
                numberOfNeighborsAlive ++;
            }
        }
        Boolean lifeStatus = alive;
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

    /**
     * Finds the index of the cells directly to the left and right of the cell
     * that is in position BoxList[i]
     * @param i index of our cell in BoxList
     * @return a list of integers where each integer corresponds to the index
     * in BoxList of one of i's neighbors
     */
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

    /**
     * Handles finding the neighbors of cell i when i is in the left-most column
     * @param i index in BoxList of the cell we are finding neighbors of
     * @return a list of integers where each integer corresponds to the index in
     * BoxList of one of i's neighbors
     */
    private ArrayList<Integer> leftColumnSpecialCase(int i) {
        ArrayList<Integer> listOfNeighbors = new ArrayList<Integer>();
        listOfNeighbors.add(i+1);
        listOfNeighbors.add(i + (numberOfCols -1));
        return listOfNeighbors;
    }

    /**
     * Handles finding the neighbors of cell i when i is in the right-most column
     * @param i index in BoxList of the cell we are finding neighbors of
     * @return a list of integers where each integer corresponds to the index in
     * BoxList of one of i's neighbors.
     */
    private ArrayList<Integer> rightColumnSpecialCase(int i) {
        ArrayList<Integer> listOfNeighbors = new ArrayList<Integer>();
        listOfNeighbors.add(i-1);
        listOfNeighbors.add(i - (numberOfCols -1));
        return listOfNeighbors;
    }

    /**
     * Find the 3 boxes that are the neighbors of cell i in the row above.
     * @param i the index in BoxList of the cell whose neighbors we are finding
     * @return a list of integers where each integer corresponds to the position within
     * BoxList of one of i's neighbors.
     */
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

    /**
     * Find the 3 boxes that are the neighbors of cell i in the row below.
     * @param i index in BoxList of the cell whose neighbors we want to find
     * @return a list of integers where each integer corresponds to the location
     * in BoxList of one of cell i's neighbors
     */
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


    @FXML
    /**
     * Finds the box corresponding to the input row,col in BoxList and changes its status to true.
     * Finds the appropriate box from the children of gameBoard and colors this box pink.
     */
    private void colorSquareAlive(int row, int col) {
        BoxList.set((row*numberOfCols)+col, true);
        String ourId = row + "_" + col;
        for (int i=0; i<((numberOfCols*numberOfRows)+numberOfRows+numberOfCols-2);i++) {
            if (gameBoard.getChildren().get(i).getId().equals(ourId)) {
                gameBoard.getChildren().get(i).setStyle("-fx-fill: palevioletred");
                break;
            }
        }
    }


    @FXML
    /**
     * Finds the box corresponding to the input row, col in BoxList and changes its status to false.
     * Finds the appropriate box from the children of gameBoard and colors this box gray.
     */
    private void colorSquareDead(int row, int col) {
        String ourId = row + "_"+ col;
        BoxList.set((row*numberOfCols)+col, false);
        for (int i=0; i<((numberOfCols*numberOfRows)+numberOfRows+numberOfCols-2);i++) {
            if (gameBoard.getChildren().get(i).getId().equals(ourId)) {
                gameBoard.getChildren().get(i).setStyle("-fx-fill: lightgrey");
                break;
            }
        }
    }


    @Override
    @FXML
    public void handle(MouseEvent click) {
      //for some reason we need this to run
    }


    /**
     * Interprets the user's click by finding which box they clicked on and calling the appropriate
     * method to change that box's color.
     *
     * @param click
     */
    public void handleClick(MouseEvent click) {
        if (helpBoxVisible) {
            this.helpSection.toBack();
            this.helpRectangle.toBack();
            helpBoxVisible = false;
        }
        else {
            int row = (int) ((click.getY() - click.getY() % boxWidth) / ((int) boxWidth));
            int col = (int) ((click.getX() - click.getX() % boxWidth) / ((int) boxWidth));
            if (paused) {
                this.series.getData().clear();
                this.time = 0;
                int listIndex = (row * numberOfCols) + col;
                Boolean lifeStatus = BoxList.get(listIndex);
                if (lifeStatus) {
                    colorSquareDead(row, col);
                    BoxList.set(listIndex, false);
                } else {
                    colorSquareAlive(row, col);
                    BoxList.set(listIndex, true);
                }
            }
        }
    }



    /**
     * Pause the simulation
     *
     * @param actionEvent
     */
    public void onPauseButton(ActionEvent actionEvent) {
        if (!this.paused) {
            this.timer.cancel();
            playButton.setStyle("-fx-base: f2f2f2");
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
            playButton.setStyle("-fx-base: A9A9A9");
            this.lineChart.getData().add(series);
        }
        this.paused = false;
    }

    @FXML
    /**
     * Changes all squares to dead and stops the timer
     */
    public void onResetButton(ActionEvent actionEvent) {
        if (!this.paused) {
            this.timer.cancel();
        }
        this.paused = true;
        for (int i=0; i<BoxList.size(); i++) {
            BoxList.set(i, false);
            gameBoard.getChildren().get(i+numberOfCols+numberOfRows-2).setStyle("-fx-fill: lightgrey");
        }
        this.score=0;
        this.time=0;
        playButton.setStyle("-fx-base: f2f2f2");
        //this.lineChart.getData().clear();
        this.series.getData().clear();
    }

    /**
     * Makes the help box, that is, the instructions, visible along with the rectangle  that
     * goes behind the text to make it visible.
     * @param actionEvent
     */
    public void onQuestionButton(ActionEvent actionEvent) {
        if (! helpBoxVisible) {
            this.helpRectangle.toFront();
            this.helpSection.toFront();
            helpBoxVisible = true;
        }
        else {
            this.helpRectangle.toBack();
            this.helpSection.toBack();
            helpBoxVisible = false;
        }
    }
    public void onArrow(MouseEvent click) {
        if (!graphVisible){
            showGraph();
        }
        else{
            hideGraph();
        }
    }
}




