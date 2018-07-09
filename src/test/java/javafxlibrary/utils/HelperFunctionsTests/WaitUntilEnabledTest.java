package javafxlibrary.utils.HelperFunctionsTests;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafxlibrary.TestFxAdapterTest;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import mockit.Expectations;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class WaitUntilEnabledTest extends TestFxAdapterTest {

    private Button button;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        button = new Button("JavaFXLibrary");
        new Expectations() {
            {
                getRobot().lookup(".button").query();
                result = button;
            }
        };
    }

    @Test
    public void waitUntilEnabled_IsEnabled() {
        Node node = HelperFunctions.waitUntilEnabled(".button", 1);
        Assert.assertEquals(button, node);
    }

    @Test
    public void waitUntilEnabled_IsEnabledWithDelay() {
        button.setDisable(true);
        Thread t = enableButtonAfterTimeout();
        t.start();
        Node node = HelperFunctions.waitUntilEnabled(".button", 1);
        Assert.assertEquals(button, node);
    }

    @Test
    public void waitUntilEnabled_IsNotEnabled() {
        button.setDisable(true);
        thrown.expect(JavaFXLibraryNonFatalException.class);
        thrown.expectMessage("Given target \"" + button + "\" did not become enabled within given timeout of 1 seconds.");
        HelperFunctions.waitUntilEnabled(".button", 1);
    }

    private Thread enableButtonAfterTimeout() {
        return new Thread(() -> {
            try {
                Thread.sleep(200);
                button.setDisable(false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
