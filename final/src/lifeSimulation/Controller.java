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

import java.util.*;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;



public class Controller implements EventHandler<MouseEvent> {
    final private double FRAMES_PER_SECOND = 2.0;

    @FXML private Button playButton;
    @FXML private Label timeKeeperLabel;
    @FXML private Label scoreLabel;
    @FXML private Text helpSection;
    @FXML private Rectangle helpRectangle;
    @FXML private AnchorPane gameBoard;
    @FXML private Polygon graphArrow;
    @FXML private NumberAxis xAxis;
    @FXML private NumberAxis yAxis;
    @FXML private LineChart lineChart;
    @FXML private XYChart.Series series = new XYChart.Series();


    private int score; //number of boxes that are alive
    private int time;
    private boolean paused;
    private boolean helpBoxVisible;
    private boolean graphVisible;
    private Timer timer;

    // This is the Model
    private ArrayList<Boolean> BoxList = new ArrayList<Boolean>();

    private int numberOfRows = 31;
    private int numberOfCols = 41;

    private int gameBoardWidth = 820;
    private int gameBoardHeight = 620;
    private double boxWidth = gameBoardWidth / numberOfCols;

    /**
     * Sets up the board - draws the grid, makes the arrow transparent, creates list of boxes, and directs clicks on the
     * gameboard.
     */
    public void initialize() {

        //make graph arrow transparent
        graphArrow.setStyle("-fx-opacity:0.75;");

        this.xAxis.setAutoRanging(true);
        this.yAxis.setAutoRanging(true);
        this.lineChart.getData().add(series);
        this.lineChart.setLegendVisible(false);

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
        this.helpBoxVisible = false;
        this.graphVisible = false;
        this.score = 0;
        this.time = 0;
    }

    /**
     * hideGraph() puts the graph and all its components behind the gameBoard
     */
    private void hideGraph() {
        this.graphVisible = false;
        this.lineChart.toBack();
        this.xAxis.toBack();
        this.yAxis.toBack();
        this.graphArrow.setRotate(0);
    }

    /**
     * showGraph() brings the graph and its components in front of the gameBoard.
     */
    private void showGraph() {
        this.graphVisible = true;
        this.lineChart.toFront();
        this.xAxis.toFront();
        this.yAxis.toFront();
        this.graphArrow.setRotate(180);
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
        //colors boxes that change to alive
        for (int j = 0; j < aliveList.size(); j++) {
            BoxList.set(aliveList.get(j),true);
            col = aliveList.get(j) % numberOfCols;
            row = (aliveList.get(j) - col) / numberOfCols;
            colorSquareAlive(row, col);
        }
        //uncolors dead boxes
        for (int k = 0; k < deadList.size(); k++) {
            BoxList.set(deadList.get(k), false);
            col = deadList.get(k) % numberOfCols;
            row = (deadList.get(k) - col) / numberOfCols;
            colorSquareDead(row, col);
        }
        this.series.getData().add(new XYChart.Data(time, score));
    }

    /**
     * Counts the number of i's neighbors who are alive and determines i's life status
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

        //iterate through list of neighbors to add to alive count
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
        //only other case left is that numberOfNeighborsAlive is 2. If alive, rules dictate it survives. If dead, it
        //remains dead.
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
            listOfNeighbors.add(i + 1);
            listOfNeighbors.add(i + numberOfCols - 1);
        }
        else if ((i % numberOfCols) == (numberOfCols -1 )) {
            listOfNeighbors.add(i - 1);
            listOfNeighbors.add(i - numberOfCols + 1);
        }
        else {
            listOfNeighbors.add(i-1);
            listOfNeighbors.add(i+1);
        }
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
        //Edge case: L column
        if ((i % numberOfCols) == 0) {
            if (i >= numberOfCols) {
                listOfNeighbors.add(i- numberOfCols + 1);
                listOfNeighbors.add(i - 1);
                listOfNeighbors.add(i - numberOfCols);
            }
            //if box in top row, above neighbors are in bottom row
            else {
                listOfNeighbors.add(numberOfCols * (numberOfRows - 1));
                listOfNeighbors.add(numberOfCols * numberOfRows - 1);
                listOfNeighbors.add(numberOfCols * (numberOfRows - 1) + 1);
            }
        }
        //Edge case: R column
        else if ((i % numberOfCols) == (numberOfCols - 1)) {
            if (i > numberOfCols) {
                listOfNeighbors.add(i - numberOfCols);
                listOfNeighbors.add(i - numberOfCols -1);
                listOfNeighbors.add(i - 2 * numberOfCols + 1);
            }
            //if box in top row, above neighbors are in bottom row
            else {
                 listOfNeighbors.add(numberOfCols * numberOfRows - 1);
                 listOfNeighbors.add(numberOfCols * (numberOfRows - 1));
                 listOfNeighbors.add(numberOfCols * numberOfRows - 2);
            }
        }
        //standard case
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
        //Edge case: L column
        if ((i % numberOfCols) == 0) {
            if (i < (numberOfCols*(numberOfRows - 1))) {
                listOfNeighbors.add(i + numberOfCols);
                listOfNeighbors.add(i + numberOfCols + 1);
                listOfNeighbors.add(i + 2 * numberOfCols - 1);
            }
            //if box in bottom row, top row is bottom neighbors
            else {
                listOfNeighbors.add(0);
                listOfNeighbors.add(1);
                listOfNeighbors.add(numberOfCols - 1);
            }
        }
        //Edge case: R column
        else if ((i % numberOfCols) == (numberOfCols - 1)) {
            if (i < (numberOfCols * (numberOfRows - 1))) {
                listOfNeighbors.add(i + numberOfCols);
                listOfNeighbors.add(i + numberOfCols - 1);
                listOfNeighbors.add(i + 1);
            }
            //if box in bottom row, top row is bottom neighbors
            else {
                listOfNeighbors.add(0);
                listOfNeighbors.add(numberOfCols - 1);
                listOfNeighbors.add(numberOfCols - 2);
            }
        }
        //standard case
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
     * Finds the box corresponding to the input row, col in BoxList and changes its status to true.
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
      //we need this for the program to run
    }


    /**
     * Interprets the user's click by finding which box they clicked on and calling the appropriate
     * method to change that box's color.
     * @param click = A click on the gameboard
     */
    public void handleClick(MouseEvent click) {

        //hide help section upon click
        if (helpBoxVisible) {
            this.helpSection.toBack();
            this.helpRectangle.toBack();
            helpBoxVisible = false;
        }
        //find which box has been clicked from the given click coordinates
        else {
            int row = (int) ((click.getY() - click.getY() % boxWidth) / ((int) boxWidth));
            int col = (int) ((click.getX() - click.getX() % boxWidth) / ((int) boxWidth));
            //can only click boxes while simulation is paused
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
     * @param actionEvent = a click on the Pause button
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
     * @param actionEvent = a click on the Play button
     */
    public void onPlayButton(ActionEvent actionEvent) {
        if (this.paused) {
            this.startTimer();
            playButton.setStyle("-fx-base: A9A9A9");
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
        this.series.getData().clear();
        hideGraph();
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

    /**
     * Shows the graph of population vs time on click. Hides on repeated cick.
     * @param click = a click on the graph button
     */
    public void onArrow(MouseEvent click) {
        if (!graphVisible){
            showGraph();
        }
        else{
            hideGraph();
        }
    }


    /**
     * The following methods are called when the corresponding preset graph is called. Then they call preload(x), where
     * x corresponds to the preset graph the user wants to view
     * @param actionEvent
     */
    public void onItem0(ActionEvent actionEvent) {
        preload(0);
    }
    public void onItem1(ActionEvent actionevent) {
        preload(1);
    }
    public void onItem2(ActionEvent actionEvent) {
        preload(2);
    }
    public void onItem3(ActionEvent actionEvent) {
        preload(3);
    }
    public void onItem4(ActionEvent actionEvent) {
        preload(4);
    }
    public void onItem5(ActionEvent actionEvent){
        preload(5);
    }
    public void onItem6(ActionEvent actionEvent) { preload(6); }

    private void preload(int choice) {
        this.time = 0;
        this.series.getData().clear();
        ArrayList<Integer> choices = new ArrayList<>();
        if (choice == 0) {
            choices.addAll(Arrays.asList(127, 168, 209, 128, 170, 297, 338, 379, 298, 340, 182, 223, 264, 183, 225, 580, 621, 662,
                    581, 623, 672, 713, 754, 673, 715, 885, 926, 967, 886, 928, 358, 399, 440, 359, 401, 945, 986,
                    1027, 946, 988, 691, 732, 773, 692, 734));
        }
        else if (choice == 1) {
            choices.addAll(Arrays.asList(1250, 1209, 1168, 1127, 1086, 1045, 1004, 963, 922, 881, 840, 799, 758, 717,
                    676, 635, 594, 553, 512, 471, 430, 389, 348, 307, 266, 225, 184, 143, 102, 61, 20, 615, 616, 617,
                    618, 619, 620, 621, 622, 623, 624, 625, 626, 627, 628, 629, 630, 631, 632, 633, 634, 636, 637, 638,
                    639, 640, 641, 642, 643, 644, 645, 646, 647, 648, 649, 651, 650, 652, 653, 654, 655));
        }
        else if (choice == 2) {
            choices.addAll(Arrays.asList(552, 511, 470, 472, 513, 554, 596, 597, 598, 678, 679, 680, 718, 759, 800,
                    716, 757, 798, 674, 673, 672, 592, 591, 590, 391, 392, 393, 387, 386, 385, 465, 506, 547, 711,
                    752, 793, 877, 878, 879, 883, 884, 885, 805, 764, 723, 559, 477, 518));
        }
        else if (choice == 3) {
            choices.addAll(Arrays.asList(635, 675, 715, 755, 795, 835, 875, 915, 955, 995, 1035, 1075, 1115, 1155, 1195, 1235, 595, 677, 593,
                    555, 515, 475, 435, 395, 355, 315, 275, 235, 195, 155, 115, 75, 35, 719, 761, 803, 845, 887, 929,
                    971, 1013, 1055, 1097, 1139, 1181, 1223, 1265, 551, 509, 467, 425, 383, 341, 299, 257, 215, 173, 131, 89,
                    47, 5, 45, 85, 125, 165, 205, 77, 119, 161, 203, 245, 1225, 1185, 1145, 1105, 1065, 1193, 1151, 1109,
                    1067, 1025));
        }
        else if (choice == 4) {
            choices.addAll(Arrays.asList(513, 473, 593, 633, 673, 511, 469, 427, 595, 637, 679, 433, 713, 753, 793, 833,
                    873, 913, 953, 993, 1033, 1073,  721, 763, 805, 847, 889, 931, 973, 1015, 1057, 1099,
                    393, 353, 313, 273, 233, 193, 153, 113, 73, 33, 385, 343, 301, 259, 217,
                    175, 133, 91, 49, 7, 512, 554, 594, 552));
        }
        else if (choice == 5){
            choices.addAll(Arrays.asList(511, 470, 471, 512, 551, 554, 549, 590, 556, 597, 672, 714, 715, 756, 757, 758,
                    759, 718, 719, 679, 431, 432, 428, 427, 839, 880, 881, 840));
        }
        else {
            choices.addAll(Arrays.asList(1209, 1250, 1168, 1127, 1086, 1045, 1004, 963, 922, 881, 840, 799, 20, 61,
                    102, 143, 184, 225, 266, 307, 348, 389, 430, 471, 512, 553, 594, 635, 676, 717, 758));
        }
        for (int i=0; i<BoxList.size(); i++) {
            BoxList.set(i, false);
            colorSquareDead((i - (i % numberOfCols)) / numberOfCols, i % numberOfCols);
        }
        for (int i=0; i<choices.size(); i++) {
            BoxList.set(choices.get(i), true);
            colorSquareAlive((choices.get(i) - (choices.get(i) % numberOfCols)) / numberOfCols, choices.get(i) % numberOfCols);
        }
    }
}




