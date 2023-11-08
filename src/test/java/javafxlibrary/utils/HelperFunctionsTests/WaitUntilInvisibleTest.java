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

public class WaitUntilInvisibleTest extends ApplicationTest {

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
    public void waitUntilInvisible_IsInvisible() {
        button.setVisible(false);
        Node node = HelperFunctions.waitUntilNotVisible(".button", 1, "SECONDS");
        Assert.assertEquals(button, node);
    }

    @Test
    public void waitUntilInvisible_IsInvisibleWithDelay() {

        button.setVisible(true);

        Thread t = setInvisibleAfterTimeout();
        t.start();
        Node node = HelperFunctions.waitUntilNotVisible(".button", 1, "SECONDS");
        Assert.assertEquals(button, node);
    }

    @Test
    public void waitUntilInvisible_IsVisible() {
        button.setVisible(true);
        JavaFXLibraryTimeoutException exception = Assert.assertThrows(JavaFXLibraryTimeoutException.class, () -> {
            HelperFunctions.waitUntilNotVisible(".button", 1, "SECONDS");
        });
        Assert.assertEquals("Given target \"" + button + "\" did not become invisible within given timeout of 1 SECONDS", exception.getMessage());
    }

    private Thread setInvisibleAfterTimeout() {
        return new Thread(() -> {
            try {
                Thread.sleep(200);
                button.setVisible(false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
