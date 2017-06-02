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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.Timer;
import java.util.TimerTask;


public class Controller implements EventHandler<MouseEvent> {
    final private double FRAMES_PER_SECOND = 2.0;

    @FXML private Button pauseButton;
    @FXML private Button playButton;
    @FXML private Label timeKeeperLabel;
    @FXML private Label scoreLabel;
    @FXML private Text helpSection;
    @FXML private Button questionButton;
    @FXML private AnchorPane gameBoard;

    private int score; //number of boxes that are alive
    private int time = 0;
    private boolean paused;
    private boolean helpBoxVisible;
    private Timer timer;

    // This is the Model
    private ArrayList<Boolean> BoxList = new ArrayList<Boolean>();

    private int numberOfRows = 30;
    private int numberOfCols = 40;

    private int gameBoardWidth = 800;     //get from Main() window width
    private int gameBoardHeight = 600;    // get from Main() window height - top AnchorPane in FXML file
    private double boxWidth = gameBoardWidth / numberOfCols;

    public void initialize() {
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
                //box.setStyle("-fx-fill: palevioletred");
                gameBoard.getChildren().add(box);
                BoxList.add(false);
            }
        }

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
        this.helpBoxVisible=false;
        this.score = 0;
        this.time = 0;
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
    }

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

    @FXML
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



    /**
     * We found this technique on
     * https://stackoverflow.com/questions/20825935/javafx-get-node-by-row-and-column
     *
     * @param row
     * @param col
     * @return the node corresponding to the box that we're changing to alive or dead
     */
    /*private boolean findBoxByRowCol(int row, int col) {
        javafx.collections.ObservableList<javafx.scene.Node> children = gameBoard.getChildren();

        for (javafx.scene.Node node : children) {
            if(gameBoard.getRowIndex(node) == row && gameBoard.getColumnIndex(node) == col) {
                result = node;
                break;
            }
        }
        return result;
    }
    */



    @Override
    @FXML
    public void handle(MouseEvent click) {
      //for some reason we need this to run
    }

    public void handleClick(MouseEvent click) {
        if (helpBoxVisible) {
            //hide help box somehow
            helpBoxVisible = false;
        }
        int row = (int) ((click.getY()- click.getY() % boxWidth) / ((int)boxWidth));
        int col = (int) ((click.getX() - click.getX() % boxWidth) / ((int)boxWidth));
        if (paused) {
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
        }
        this.paused = false;
    }

    public void onQuestionButton(ActionEvent actionEvent) {
        if (! helpBoxVisible) {
            //this.helpSection.setText("Welcome to the Game of Life. To begin playing, click on any set of boxes." +
             //               "When you press play, a simulation will begin. If a box is 'alive', then it will stay alive if it's next to 2 or 3 other 'alive' boxes. If a box " +
             //       "is dead, it will become alive if it has exactly 3 live neighbors. You can press Pause to look at a particular configuration or add more alive boxes " +
              //              "(but doing this will restart the generation timer). The arrow at the bottom of the screen toggles a graph that shows population of the cells over time " +
              //      "as the simulation is taking place.");
            this.helpSection.setWrappingWidth(200);
            this.helpSection.setText("Welcome to the Game of Life. To begin playing, click on any set of boxes. " +
                            "When you press play, a simulation will begin. If a box is 'alive', then it will stay alive if it's next to 2 or 3 other " +
                    "'alive' boxes. If a box is dead, it will become alive if it has exactly 3 live neighbors. You can press Pause to look at a particular " +
                    "configuration or add more alive boxes (but doing this will restart the generation timer). The arrow at the bottom of the screen toggles " +
                            "a graph that shows population of the cells over time as the simulation is taking place.");
            helpBoxVisible = true;

            //show help box somehow
        }
        else {
            this.helpSection.setText("");
            helpBoxVisible = false;
            // hide help box somehow
        }
    }
}




