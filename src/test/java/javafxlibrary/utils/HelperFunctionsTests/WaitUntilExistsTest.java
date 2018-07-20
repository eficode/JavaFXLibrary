package javafxlibrary.utils.HelperFunctionsTests;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafxlibrary.TestFxAdapterTest;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.Finder;
import javafxlibrary.utils.HelperFunctions;
import mockit.*;
import org.junit.*;
import org.junit.rules.ExpectedException;
import testutils.DelayedObject;

public class WaitUntilExistsTest extends TestFxAdapterTest {

    @Mocked private Finder finder;
    private Button button;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

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
                finder.find(".button"); result = button;
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
                finder.find(".button"); result = null;
            }
        };

        thrown.expect(JavaFXLibraryNonFatalException.class);
        thrown.expectMessage("Given element \".button\" was not found within given timeout of 500 MILLISECONDS");
        HelperFunctions.waitUntilExists(".button", 500, "MILLISECONDS");
    }
}
