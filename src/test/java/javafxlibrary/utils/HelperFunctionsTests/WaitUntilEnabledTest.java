package javafxlibrary.utils.HelperFunctionsTests;

import javafx.scene.Node;
import javafx.scene.control.Button;
import org.testfx.framework.junit.ApplicationTest;
import javafxlibrary.exceptions.JavaFXLibraryTimeoutException;
import javafxlibrary.utils.HelperFunctions;
import mockit.Mock;
import mockit.MockUp;
import org.junit.*;

public class WaitUntilEnabledTest extends ApplicationTest {

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
    public void waitUntilEnabled_IsEnabled() {
        Node node = HelperFunctions.waitUntilEnabled(".button", 1, "SECONDS");
        Assert.assertEquals(button, node);
    }

    @Test
    public void waitUntilEnabled_IsEnabledWithDelay() {
        button.setDisable(true);
        Thread t = enableButtonAfterTimeout();
        t.start();
        Node node = HelperFunctions.waitUntilEnabled(".button", 1, "SECONDS");
        Assert.assertEquals(button, node);
    }

    @Test
    public void waitUntilEnabled_IsNotEnabled() {
        button.setDisable(true);
        JavaFXLibraryTimeoutException exception = Assert.assertThrows(JavaFXLibraryTimeoutException.class, () -> {
            HelperFunctions.waitUntilEnabled(".button", 1, "SECONDS");
        });
        Assert.assertEquals("Given target \"" + button + "\" did not become enabled within given timeout of 1 seconds.", exception.getMessage());
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
