package javafxlibrary.utils.HelperFunctionsTests;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafxlibrary.TestFxAdapterTest;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.finder.Finder;
import javafxlibrary.utils.HelperFunctions;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ObjectToNodeTest extends TestFxAdapterTest {

    @Mocked
    private Finder finder;
    Button button;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

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
                finder.find("#testNode"); result = button;
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
        thrown.expectMessage("given target \"java.lang.Integer\" is not an instance of Node or a query string for node!");
        HelperFunctions.objectToNode(new Integer("2009"));
    }

    @Test
    public void objectToNode_NullObject() {
        thrown.expect(JavaFXLibraryNonFatalException.class);
        thrown.expectMessage("target object was empty (null)");
        HelperFunctions.objectToNode(null);
    }
}
