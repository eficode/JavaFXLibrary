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
import org.junit.Assert;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

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
                getRobot().listWindows();
                result = windows;
                stage.isShowing();
                result = true;
                stage.getX();
                result = 0;
                stage.getY();
                result = 0;
                stage.getWidth();
                result = 500;
                stage.getHeight();
                result = 500;
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
        setBoundsQueryExpectations(30, 30);
        HelperFunctions.checkObjectInsideActiveWindow(30, 30);
        Assert.assertEquals("*TRACE* Target location checks out OK, it is within active window\n", outContent.toString().substring(outContent.toString().length() - 56));
    }

    @Test
    public void checkClickLocation_IsOutsideVisibleWindow() throws Exception {
        setBoundsQueryExpectations(30, 800);
        String target = "Can't click Point2D at [30.0, 800.0]: out of window bounds. To enable clicking outside " +
                "of visible window bounds use keyword `Set Safe Clicking` with argument `off`";

        JavaFXLibraryNonFatalException exception = Assert.assertThrows(JavaFXLibraryNonFatalException.class, () -> {
            HelperFunctions.checkObjectInsideActiveWindow(30, 800);
        });

        Assert.assertEquals(target, exception.getMessage());
    }

    private void setBoundsQueryExpectations(double minX, double minY) {
        new Expectations() {
            {
                getRobot().bounds((Point2D) any);
                BoundsQuery bq = () -> new BoundingBox(minX, minY, 0, 0);
                result = bq;
            }
        };
    }
}
