package javafxlibrary.utils.HelperFunctionsTests;

import javafx.scene.Node;
import javafx.scene.control.Button;
import org.testfx.framework.junit.ApplicationTest;
import javafxlibrary.exceptions.JavaFXLibraryTimeoutException;
import javafxlibrary.utils.HelperFunctions;
import mockit.Mock;
import mockit.MockUp;
import org.junit.*;

public class WaitUntilVisibleTest extends ApplicationTest {

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
    public void waitUntilVisible_IsVisible() {
        Node node = HelperFunctions.waitUntilVisible(".button", 1, "SECONDS");
        Assert.assertEquals(button, node);
    }

    @Test
    public void waitUntilVisible_IsVisibleWithDelay() {

        button.setVisible(false);

        Thread t = setVisibleAfterTimeout();
        t.start();
        Node node = HelperFunctions.waitUntilVisible(".button", 1, "SECONDS");
        Assert.assertEquals(button, node);
    }

    @Test
    public void waitUntilVisible_IsNotVisible() {
        button.setVisible(false);
        JavaFXLibraryTimeoutException exception = Assert.assertThrows(JavaFXLibraryTimeoutException.class, () -> {
            HelperFunctions.waitUntilVisible(".button", 1, "SECONDS");
        });
        Assert.assertEquals("Given target \"" + button + "\" did not become visible within given timeout of 1 SECONDS", exception.getMessage());
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
