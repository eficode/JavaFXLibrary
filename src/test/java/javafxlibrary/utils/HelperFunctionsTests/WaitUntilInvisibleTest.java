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

public class WaitUntilInvisibleTest extends TestFxAdapterTest {

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
    public void waitUntilInvisible_IsInvisible() {
        button.setVisible(false);
        Node node = HelperFunctions.waitUntilInvisible(".button", 1);
        Assert.assertEquals(button, node);
    }

    @Test
    public void waitUntilInvisible_IsInvisibleWithDelay() {

        button.setVisible(true);

        Thread t = setInvisibleAfterTimeout();
        t.start();
        Node node = HelperFunctions.waitUntilInvisible(".button", 1);
        Assert.assertEquals(button, node);
    }

    @Test
    public void waitUntilInvisible_IsVisible() {
        button.setVisible(true);
        thrown.expect(JavaFXLibraryTimeoutException.class);
        thrown.expectMessage("Given target \"" + button + "\" did not become invisible within given timeout of 1 SECONDS");
        HelperFunctions.waitUntilInvisible(".button", 1);
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
