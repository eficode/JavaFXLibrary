package javafxlibrary.utils.HelperFunctionsTests;

import javafx.scene.Node;
import javafx.scene.control.Button;
import org.testfx.framework.junit.ApplicationTest;
import javafxlibrary.exceptions.JavaFXLibraryTimeoutException;
import javafxlibrary.utils.HelperFunctions;
import javafxlibrary.utils.finder.Finder;
import mockit.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import testutils.DelayedObjectRemoval;

public class WaitUntilDoesNotExistsTest extends ApplicationTest {

    @Mocked
    private Finder finder;
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
    public void waitUntilDoesNotExists_DoesNotExist() {
        new Expectations() {
            {
                finder.find(".button");
                result = null;
            }
        };

        HelperFunctions.waitUntilDoesNotExists(".button", 500, "MILLISECONDS");
    }

    @Test
    public void waitUntilDoesNotExists_DoesNotExistsWithDelay() {
        DelayedObjectRemoval delayedObject = new DelayedObjectRemoval(button, 500);

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
        HelperFunctions.waitUntilDoesNotExists(".button", 1000, "MILLISECONDS");

    }

    @Test
    public void waitUntilDoesNotExists_Exist() {

        new Expectations() {
            {
                finder.find(".button");
                result = button;
            }
        };

        thrown.expect(JavaFXLibraryTimeoutException.class);
        thrown.expectMessage("Given element \".button\" was still found within given timeout of 500 MILLISECONDS");
        HelperFunctions.waitUntilDoesNotExists(".button", 500, "MILLISECONDS");
    }
}
