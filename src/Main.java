import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class that is responsible for launching the app
 * *RUNTIME ERROR*
 * I've encountered a runtime error when trying to launch the "Add Part" menu from the main. The application would
 * throw a NullPointerException when I tried changing the screens. After an investigation I found an error in the
 * FXML file path that was the cause of it. Correcting the FXML path to the existing one fixed the issue.
 * *FUTURE IMPROVEMENTS*
 * On the next step of the development I would set up a database on a server to store all the parts and products
 * information. This would allow the use of application from separate locations ensuring data consistency.
 * Another functionality that can be added is separation of the parts that are in stock and out to store historical data.
 * As for the code, I would restructure the project so Part and Product implement an Item interface to avoid duplication
 * of code. Controllers could also be restructured under one "umbrella" interface to eliminate the code duplication.
 *
 * @author Iulia Bejsovec StudentID: 001248083
 */
public class Main extends Application {

    /**
     * The main entry point for all JavaFX applications. It is called after the init method has returned, and after the
     * system is ready for the application to begin running
     *
     * @param primaryStage the primary stage for this application, onto which the application scene can be set. The
     *                     primary stage will be embedded in the browser if the application was launched as an applet.
     *                     Applications may create other stages, if needed, but they will not be primary stages and
     *                     will not be embedded in the browser.
     * @throws Exception if fxml file cannot be loaded
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("View/MainMenu.fxml"));
        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(new Scene(root, 1249, 510));
        primaryStage.show();
    }

    /**
     * Method that launches the app
     *
     * @param args arguments passed to the main method
     */
    public static void main(String[] args) {
        launch(args);
    }
}
