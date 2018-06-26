package javafxlibrary.testapps;

import javafx.application.Application;
import javafx.stage.Stage;

public class SwingApplicationWrapper extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        SwingApplication.main(new String[0]);
    }

    // Close the JFrame when wrapper is stopping
    @Override
    public void stop() {
        SwingApplication.close();
    }
}
