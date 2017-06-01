/**
 * Main.java
 * Created by Patty Commins and Anna Meyer for Software Design final project, Spring 2017
 *
 * The main program for the Game of Life simulation. When the program is called, it uses a
 * controller to communicate between the data associated with the simulation and the
 * user interface where graphic representations of the data are displayed.
 */

package lifeSimulation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    /**
     * Runs the Game of Life simulation
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("lifeSimulation.fxml"));
        primaryStage.setTitle("Game of Life");
        primaryStage.setScene(new Scene(root, 600, 660));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
