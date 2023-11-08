package javafxlibrary.utils.HelperFunctionsTests;

import javafx.scene.Node;
import javafx.scene.control.Button;
import org.testfx.framework.junit.ApplicationTest;
import javafxlibrary.exceptions.JavaFXLibraryTimeoutException;
import javafxlibrary.utils.HelperFunctions;
import javafxlibrary.utils.finder.Finder;
import mockit.*;
import org.junit.Before;
import org.junit.Test;
import testutils.DelayedObjectRemoval;
import org.junit.Assert;

public class WaitUntilDoesNotExistsTest extends ApplicationTest {

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
                result = new Delegate<Node>() {
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

        JavaFXLibraryTimeoutException exception = Assert.assertThrows(JavaFXLibraryTimeoutException.class, () -> {
            HelperFunctions.waitUntilDoesNotExists(".button", 500, "MILLISECONDS");
        });
        Assert.assertEquals("Given element \".button\" was still found within given timeout of 500 MILLISECONDS", exception.getMessage());
    }
}
