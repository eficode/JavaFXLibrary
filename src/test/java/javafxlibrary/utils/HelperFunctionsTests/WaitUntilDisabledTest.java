package javafxlibrary.utils.HelperFunctionsTests;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafxlibrary.TestFxAdapterTest;
import javafxlibrary.exceptions.JavaFXLibraryTimeoutException;
import javafxlibrary.utils.HelperFunctions;
import mockit.Mock;
import mockit.MockUp;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class WaitUntilDisabledTest extends TestFxAdapterTest {

    private Button button;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        button = new Button("JavaFXLibrary");
        new MockUp<HelperFunctions>() {
            @Mock
            Node waitUntilExists(String target, int timeout, String timeUnit) {
                return button;
            }
        };
    }

    @Test
    public void waitUntilDisabled_IsNotEnabled() {
        button.setDisable(true);
        Node node = HelperFunctions.waitUntilDisabled(".button", 1, "SECONDS");
        Assert.assertEquals(button, node);
    }

    @Test
    public void waitUntilDisabled_IsNotEnabledWithDelay() {
        button.setDisable(false);
        Thread t = disableButtonAfterTimeout();
        t.start();
        Node node = HelperFunctions.waitUntilDisabled(".button", 1, "SECONDS");
        Assert.assertEquals(button, node);
    }

    @Test
    public void waitUntilDisabled_IsEnabled() {
        button.setDisable(false);
        thrown.expect(JavaFXLibraryTimeoutException.class);
        thrown.expectMessage("Given target \"" + button + "\" did not become disabled within given timeout of 1 seconds.");
        HelperFunctions.waitUntilDisabled(".button", 1, "SECONDS");
    }

    private Thread disableButtonAfterTimeout() {
        return new Thread(() -> {
            try {
                Thread.sleep(200);
                button.setDisable(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
