package javafxlibrary.utils.HelperFunctionsTests;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafxlibrary.TestFxAdapterTest;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import mockit.Expectations;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ObjectToNodeTest extends TestFxAdapterTest {

    Button button;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void objectToNode_StringQuery() {
        button = new Button();
        new Expectations() {
            {
                getRobot().lookup("#testNode").query(); result = button;
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
        thrown.expect(JavaFXLibraryNonFatalException.class);
        thrown.expectMessage("Given target \"java.lang.Integer\" is not an instance of Node or a query string for node!");
        HelperFunctions.objectToNode(new Integer("2009"));
    }

    @Test
    public void objectToNode_NullObject() {
        thrown.expect(JavaFXLibraryNonFatalException.class);
        thrown.expectMessage("Target object was null");
        HelperFunctions.objectToNode(null);
    }
}
