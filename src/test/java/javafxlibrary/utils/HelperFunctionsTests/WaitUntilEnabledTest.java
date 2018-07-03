package javafxlibrary.utils.HelperFunctionsTests;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafxlibrary.TestFxAdapterTest;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import mockit.Expectations;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WaitUntilEnabledTest extends TestFxAdapterTest {

    private Button button;

    @Before
    public void setup() {
        button = new Button("JavaFXLibrary");
    }

    @Test
    public void waitUntilEnabled_IsEnabled() {
        new Expectations() {
            {
                getRobot().lookup(".button").query();
                result = button;
            }
        };

        Node node = HelperFunctions.waitUntilEnabled(".button", 1);
        Assert.assertEquals(button, node);
    }

    @Test
    public void waitUntilEnabled_IsEnabledWithDelay() {
        button.setDisable(true);

        // Enable button after 200 milliseconds have passed
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(200);
                button.setDisable(false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        new Expectations() {
            {
                getRobot().lookup(".button").query();
                result = button;
            }
        };

        t.start();
        Node node = HelperFunctions.waitUntilEnabled(".button", 1);
        Assert.assertEquals(button, node);
    }

    @Test
    public void waitUntilEnabled_IsNotEnabled() {
        button.setDisable(true);
        new Expectations() {
            {
                getRobot().lookup(".button").query();
                result = button;
            }
        };

        try {
            Node node = HelperFunctions.waitUntilEnabled(".button", 1);
            Assert.fail("Expected a JavaFXLibraryNonFatalException to be thrown");
        } catch (JavaFXLibraryNonFatalException e) {
            String target = "Given target \"" + button + "\" did not become enabled within given timeout of 1 seconds.";
            Assert.assertEquals(e.getMessage(), target);
        }
    }
}
