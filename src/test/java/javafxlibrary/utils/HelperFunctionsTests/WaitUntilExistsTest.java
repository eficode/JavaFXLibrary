package javafxlibrary.utils.HelperFunctionsTests;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafxlibrary.TestFxAdapterTest;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import mockit.Delegate;
import mockit.Expectations;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import testutils.DelayedObject;

public class WaitUntilExistsTest extends TestFxAdapterTest {

    private Button button;

    @Before
    public void setup() {
        button = new Button("JavaFXLibrary");
    }

    @Test
    public void waitUntilExists_Exist() {
        new Expectations() {
            {
                getRobot().lookup(".button").query();
                result = button;
            }
        };

        Node node = HelperFunctions.waitUntilExists(".button", 500, "MILLISECONDS");
        Assert.assertEquals(button, node);
    }

    @Test
    public void waitUntilExists_ExistsWithDelay() {
        DelayedObject delayedObject = new DelayedObject(button, 500);

        new Expectations() {
            {
                getRobot().lookup(".button").query();
                result = new Delegate() {
                    public Node delegate() throws Exception {
                        return (Node) delayedObject.getValue();
                    }
                };
            }
        };

        delayedObject.start();
        Node node = HelperFunctions.waitUntilExists(".button", 1000, "MILLISECONDS");
        Assert.assertEquals(button, node);
    }

    @Test
    public void waitUntilExists_DoesNotExist() {
        new Expectations() {
            {
                getRobot().lookup(".button").query();
                result = null;
            }
        };

        try {
            Node node = HelperFunctions.waitUntilExists(".button", 500, "MILLISECONDS");
            Assert.fail("Expected a JavaFXLibraryNonFatalException to be thrown");
        } catch (JavaFXLibraryNonFatalException e) {
            Assert.assertEquals(e.getMessage(), "Given element \".button\" was not found within given timeout " +
                    "of 500 MILLISECONDS");
        }
    }
}
