package testutils;

import javafx.application.Platform;
import javafx.stage.Stage;
import javafxlibrary.utils.HelperFunctions;
import mockit.Mock;
import mockit.MockUp;

import java.util.concurrent.Semaphore;

public class TestFunctions {

    public static void useWindows() {
        new MockUp<HelperFunctions>() {
            @Mock
            boolean isMac() {
                return false;
            }
        };
    }

    public static void useMac() {
        new MockUp<HelperFunctions>() {
            @Mock
            boolean isMac() {
                return true;
            }
        };
    }

    public static Stage setupStageInJavaFXThread() {
        Stage[] stages = new Stage[1];
        Platform.runLater(() -> stages[0] = new Stage());
        waitForEventsInJavaFXThread();
        return stages[0];
    }

    public static void waitForEventsInJavaFXThread() {
        try {
            Semaphore semaphore = new Semaphore(0);
            Platform.runLater(() -> semaphore.release());
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
