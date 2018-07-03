package javafxlibrary.utils.HelperFunctionsTests;

import javafx.scene.control.Button;
import javafxlibrary.TestFxAdapterTest;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import javafxlibrary.utils.TestFxAdapter;
import mockit.Mock;
import mockit.MockUp;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

public class MapObjectTest extends TestFxAdapterTest {

    @After
    public void cleanup() {
        TestFxAdapter.objectMap.clear();
    }

    @Test
    public void mapObject_Node() {
        Button button = new Button("JavaFXLibrary");
        String key = (String) HelperFunctions.mapObject(button);
        Button b = (Button) TestFxAdapter.objectMap.get(key);
        Assert.assertEquals(button, b);
    }

    @Test
    public void mapObject_Null() {
        try {
            HelperFunctions.mapObject(null);
            Assert.fail("Expected a JavaFXLibraryNonFatalException to be thrown");
        } catch (JavaFXLibraryNonFatalException e) {
            Assert.assertEquals("Object was null, unable to map object!", e.getMessage());
        }
    }

    @Test
    public  void mapObject_NonJavaFXObject() {
        MapObjectTest object = new MapObjectTest();
        String key = (String) HelperFunctions.mapObject(object);
        Object result = TestFxAdapter.objectMap.get(key);
        Assert.assertEquals(object, result);
    }

    @Test
    public  void mapObject_CompatibleType() {
        makeEverythingCompatible();
        Button button = new Button("JavaFXLibrary");
        Object result = HelperFunctions.mapObject(button);
        Assert.assertEquals(button, result);
    }

    private void makeEverythingCompatible() {
        new MockUp<HelperFunctions>() {
            @Mock
            boolean isCompatible(Object o) {
                return true;
            }
        };
    }
}
