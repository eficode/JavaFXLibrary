package javafxlibrary.utils.HelperFunctionsTests;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafxlibrary.TestFxAdapterTest;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import mockit.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WaitUntilVisibleTest extends TestFxAdapterTest {

    private Button button;

    @Before
    public void setup() {
        button = new Button("JavaFXLibrary");
    }

    @Test
    public void waitUntilVisible_IsVisible() {
        new Expectations() {
            {
                getRobot().lookup(".button").query();
                result = button;
            }
        };

        Node node = HelperFunctions.waitUntilVisible(".button", 1);
        Assert.assertEquals(button, node);
    }

    @Test
    public void waitUntilVisible_IsVisibleWithDelay() {

        button.setVisible(false);

        new Expectations() {
            {
                getRobot().lookup(".button").query();
                result = button;
            }
        };

        Thread t = setVisibleAfterTimeout();
        t.start();
        Node node = HelperFunctions.waitUntilVisible(".button", 1);
        Assert.assertEquals(button, node);
    }

    @Test
    public void waitUntilVisible_IsNotVisible() {
        button.setVisible(false);
        new Expectations() {
            {
                getRobot().lookup(".button").query();
                result = button;
            }
        };

        try {
            Node node = HelperFunctions.waitUntilVisible(".button", 1);
            Assert.fail("Expected a JavaFXLibraryNonFatalException to be thrown");
        } catch (JavaFXLibraryNonFatalException e) {
            String target = "Given target \"" + button + "\" did not become visible within given timeout of 1 seconds.";
            Assert.assertEquals(e.getMessage(), target);
        }
    }

    private Thread setVisibleAfterTimeout() {
        return new Thread(() -> {
            try {
                Thread.sleep(200);
                button.setVisible(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
