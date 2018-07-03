package javafxlibrary.utils.HelperFunctionsTests;

import javafx.scene.control.Button;
import javafxlibrary.TestFxAdapterTest;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import javafxlibrary.utils.TestFxAdapter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class MapObjectsTest extends TestFxAdapterTest {

    private Button button;

    @Before
    public void setup() {
        button = new Button("JavaFXLibrary");
    }

    @After
    public void cleanup() {
        TestFxAdapter.objectMap.clear();
    }

    @Test
    public void mapObjects_FromList() {
        List<Button> list = new ArrayList<>();
        list.add(button);
        List<Object> keys = HelperFunctions.mapObjects(list);
        Button b = (Button) TestFxAdapter.objectMap.get(keys.get(0));
        Assert.assertEquals(button, b);
    }

    @Test
    public void mapObjects_FromSet() {
        Set<Button> set = new HashSet<>();
        set.add(button);
        List<Object> keys = HelperFunctions.mapObjects(set);
        Button b = (Button) TestFxAdapter.objectMap.get(keys.get(0));
        Assert.assertEquals(button, b);
    }

    @Test
    public void mapObjects_FromQueue() {
        Queue<Button> queue = new PriorityQueue<>();
        queue.add(button);
        List<Object> keys = HelperFunctions.mapObjects(queue);
        Button b = (Button) TestFxAdapter.objectMap.get(keys.get(0));
        Assert.assertEquals(button, b);
    }

    @Test
    public void mapObjects_FromCollection() {
        Collection<Button> collection = new ArrayList<>();
        collection.add(button);
        List<Object> keys = HelperFunctions.mapObjects(collection);
        Button b = (Button) TestFxAdapter.objectMap.get(keys.get(0));
        Assert.assertEquals(button, b);
    }

    @Test
    public void mapObjects_NullValue() {
        List<Object> list = new ArrayList<>();
        list.addAll(Arrays.asList(button, null));

        try {
            HelperFunctions.mapObjects(list);
            Assert.fail("Expected a JavaFXLibraryNonFatalException to be thrown");
        } catch (JavaFXLibraryNonFatalException e) {
            Assert.assertEquals("Object was null, unable to map object!", e.getMessage());
        }
    }

    @Test
    public void mapObjects_EmptyList() {
        List<Object> list = new ArrayList<>();

        try {
            HelperFunctions.mapObjects(list);
            Assert.fail("Expected a JavaFXLibraryNonFatalException to be thrown");
        } catch (JavaFXLibraryNonFatalException e) {
            Assert.assertEquals("List was empty, unable to map anything!", e.getMessage());
        }
    }
}
