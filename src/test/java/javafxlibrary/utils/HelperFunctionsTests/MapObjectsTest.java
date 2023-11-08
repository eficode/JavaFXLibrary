package javafxlibrary.utils.HelperFunctionsTests;

import javafx.scene.control.Button;
import javafxlibrary.TestFxAdapterTest;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import javafxlibrary.utils.TestFxAdapter;
import org.junit.*;

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
        Queue<Button> queue = new LinkedList<>();
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

        JavaFXLibraryNonFatalException exception = Assert.assertThrows(JavaFXLibraryNonFatalException.class, () -> {
            HelperFunctions.mapObjects(list);
        });
        Assert.assertEquals("Object was null, unable to map object!", exception.getMessage());
    }

    @Test
    public void mapObjects_EmptyList() {
        List<Object> list = new ArrayList<>();
        JavaFXLibraryNonFatalException exception = Assert.assertThrows(JavaFXLibraryNonFatalException.class, () -> {
            HelperFunctions.mapObjects(list);
        });
        Assert.assertEquals("List was empty, unable to map anything!", exception.getMessage());
    }
}
