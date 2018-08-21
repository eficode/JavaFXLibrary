package javafxlibrary.utils.HelperFunctionsTests;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafxlibrary.TestFxAdapterTest;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.exceptions.JavaFXLibraryTimeoutException;
import javafxlibrary.utils.HelperFunctions;
import mockit.*;
import org.junit.*;
import org.junit.rules.ExpectedException;

public class WaitUntilVisibleTest extends TestFxAdapterTest {

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
    public void waitUntilVisible_IsVisible() {
        Node node = HelperFunctions.waitUntilVisible(".button", 1);
        Assert.assertEquals(button, node);
    }

    @Test
    public void waitUntilVisible_IsVisibleWithDelay() {

        button.setVisible(false);

        Thread t = setVisibleAfterTimeout();
        t.start();
        Node node = HelperFunctions.waitUntilVisible(".button", 1);
        Assert.assertEquals(button, node);
    }

    @Test
    public void waitUntilVisible_IsNotVisible() {
        button.setVisible(false);
        thrown.expect(JavaFXLibraryTimeoutException.class);
        thrown.expectMessage("Given target \"" + button + "\" did not become visible within given timeout of 1 seconds.");
        HelperFunctions.waitUntilVisible(".button", 1);
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
