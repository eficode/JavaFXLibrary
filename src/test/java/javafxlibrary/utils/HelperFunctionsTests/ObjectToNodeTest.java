package javafxlibrary.utils.HelperFunctionsTests;

import javafx.scene.Node;
import javafx.scene.control.Button;
import org.testfx.framework.junit.ApplicationTest;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import javafxlibrary.utils.finder.Finder;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import org.junit.Assert;
import org.junit.Test;

public class ObjectToNodeTest extends ApplicationTest {

    @Mocked
    private Finder finder;
    Button button;

    @Test
    public void objectToNode_StringQuery() {
        button = new Button();
        new MockUp<HelperFunctions>() {
            @Mock
            Finder createFinder() {
                return finder;
            }
        };

        new Expectations() {
            {
                finder.find("#testNode");
                result = button;
            }
        };

        Node result = HelperFunctions.objectToNode("#testNode");
        Assert.assertEquals(button, result);
    }

    @Test
    public void objectToNode_Node() {
        button = new Button();
        Node result = HelperFunctions.objectToNode(button);
        Assert.assertEquals(button, result);
    }

    @Test
    public void objectToNode_InvalidType() {
        JavaFXLibraryNonFatalException exception = Assert.assertThrows(JavaFXLibraryNonFatalException.class, () -> {
            HelperFunctions.objectToNode(Integer.valueOf("2009"));
        });
        Assert.assertEquals("given target \"java.lang.Integer\" is not an instance of Node or a query string for node!", exception.getMessage());
    }

    @Test
    public void objectToNode_NullObject() {
        JavaFXLibraryNonFatalException exception = Assert.assertThrows(JavaFXLibraryNonFatalException.class, () -> {
            HelperFunctions.objectToNode(null);
        });
        Assert.assertEquals("target object was empty (null)", exception.getMessage());
    }
}
