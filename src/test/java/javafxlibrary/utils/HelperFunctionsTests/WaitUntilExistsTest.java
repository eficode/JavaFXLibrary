package javafxlibrary.utils.HelperFunctionsTests;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafxlibrary.TestFxAdapterTest;
import javafxlibrary.exceptions.JavaFXLibraryTimeoutException;
import javafxlibrary.utils.HelperFunctions;
import javafxlibrary.utils.finder.Finder;
import mockit.*;
import org.junit.*;
import testutils.DelayedObject;

public class WaitUntilExistsTest extends TestFxAdapterTest {

    @Mocked
    private Finder finder;
    private Button button;

    @Before
    public void setup() {
        button = new Button("JavaFXLibrary");
        new MockUp<HelperFunctions>() {
            @Mock
            Finder createFinder() {
                return finder;
            }
        };
    }

    @Test
    public void waitUntilExists_Exist() {
        new Expectations() {
            {
                finder.find(".button");
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
                finder.find(".button");
                result = new Delegate<Node>() {
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
                finder.find(".button");
                result = null;
            }
        };

        JavaFXLibraryTimeoutException exception = Assert.assertThrows(JavaFXLibraryTimeoutException.class, () -> {
            HelperFunctions.waitUntilExists(".button", 500, "MILLISECONDS");
        });

        Assert.assertEquals("Given element \".button\" was not found within given timeout of 500 MILLISECONDS", exception.getMessage());
    }
}
