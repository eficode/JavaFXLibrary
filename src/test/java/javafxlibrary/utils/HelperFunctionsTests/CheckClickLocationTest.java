package javafxlibrary.utils.HelperFunctionsTests;

import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafxlibrary.TestFxAdapterTest;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.*;
import org.testfx.service.query.BoundsQuery;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.endsWith;

@Ignore("Fails when run with Maven")
public class CheckClickLocationTest extends TestFxAdapterTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private List<Window> windows;

    @Mocked
    Stage stage;

    @Before
    public void setup() {
        System.setOut(new PrintStream(outContent));
        windows = new ArrayList<>();
        new Expectations() {
            {
                getRobot().listWindows(); result = windows;
                stage.isShowing(); result = true;
                stage.getX(); result = 0;
                stage.getY(); result = 0;
                stage.getWidth(); result = 500;
                stage.getHeight(); result = 500;
            }
        };
        windows.add(stage);
    }

    @After
    public void cleanup() {
        System.setOut(originalOut);
    }

    @Test
    public void checkClickLocation_IsWithinVisibleWindow() {
        new Expectations() {
            {
                getRobot().bounds((Point2D) any);
                BoundsQuery bq = () -> new BoundingBox(30, 30, 0, 0);
                result = bq;
            }
        };

        HelperFunctions.checkClickLocation(30, 30);
        Assert.assertThat(outContent.toString(), endsWith("*TRACE* Target location checks out OK, it is within active window\n"));
    }

    @Test
    public void checkClickLocation_IsOutsideVisibleWindow() {
        new Expectations() {
            {
                getRobot().bounds((Point2D) any);
                BoundsQuery bq = () -> new BoundingBox(30, 800, 0, 0);
                result = bq;
            }
        };

        try {
            HelperFunctions.checkClickLocation(30, 800);
            Assert.fail("Expected a JavaFXLibraryNonFatalException to be thrown");
        } catch (JavaFXLibraryNonFatalException e) {
            String target = "Can't click Point2D at [30.0, 800.0]: out of window bounds. To enable clicking outside " +
                    "of visible window bounds use keyword SET SAFE CLICKING | OFF";
            Assert.assertEquals(target, e.getMessage());
        }
    }
}
