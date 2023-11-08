package javafxlibrary.utils.HelperFunctionsTests;

import javafx.scene.Node;
import javafx.scene.control.Button;
import org.testfx.framework.junit.ApplicationTest;
import javafxlibrary.exceptions.JavaFXLibraryTimeoutException;
import javafxlibrary.utils.HelperFunctions;
import mockit.Mock;
import mockit.MockUp;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WaitUntilDisabledTest extends ApplicationTest {

    private Button button;

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
        JavaFXLibraryTimeoutException exception = Assert.assertThrows(JavaFXLibraryTimeoutException.class, () -> {
            HelperFunctions.waitUntilDisabled(".button", 1, "SECONDS");
        });
        Assert.assertEquals("Given target \"" + button + "\" did not become disabled within given timeout of 1 seconds.", exception.getMessage());
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
