package javafxlibrary.utils.HelperFunctionsTests;

import javafx.application.Platform;
import javafx.stage.Stage;
import javafxlibrary.TestFxAdapterTest;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;

import static testutils.TestFunctions.setupStageInJavaFXThread;
import static testutils.TestFunctions.waitForEventsInJavaFXThread;

public class CallMethodTest extends TestFxAdapterTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void callMethod_InSameThread_NoArgs_WithReturnValue() {
        String name = "JavaFXLibrary";
        String result = (String) HelperFunctions.callMethod(name, "toUpperCase", false);
        Assert.assertEquals("JAVAFXLIBRARY", result);
    }

    @Test
    public void callMethod_InSameThread_NoArgs_NoReturnValue() {
        TestPoint testPoint = new TestPoint(0, 0);
        HelperFunctions.callMethod(testPoint, "setLocationTo2017", false);
        Assert.assertEquals(20, testPoint.getX(), 0);
        Assert.assertEquals(17, testPoint.getY(), 0);
    }

    @Test
    public void callMethod_InSameThread_WithArgs_WithReturnValue() {
        String name = "JavaFXLibrary";
        List<Object> arguments = Arrays.asList(4, 9);
        List<Object> types = Arrays.asList("int", "int");
        String result = (String) HelperFunctions.callMethod(name, "substring", arguments, types, false);
        Assert.assertEquals("FXLib", result);
    }

    @Test
    public void callMethod_InSameThread_WithArgs_NoReturnValue() {
        Point point = new Point(0, 0);
        List<Object> arguments = Arrays.asList(20, 17);
        List<Object> types = Arrays.asList("int", "int");
        HelperFunctions.callMethod(point, "setLocation", arguments, types, false);
        Assert.assertEquals(20, point.getX(), 0);
        Assert.assertEquals(17, point.getY(), 0);
    }

    @Test
    public void callMethod_InJavaFXThread_WithArgs() {
        Stage stage = setupStageInJavaFXThread();
        stage.setTitle("Original title");
        Platform.runLater(() -> stage.show());
        waitForEventsInJavaFXThread();

        List<Object> arguments = Arrays.asList("Changed Title");
        List<Object> types = Arrays.asList("java.lang.String");
        HelperFunctions.callMethod(stage, "setTitle", arguments, types, true);
        waitForEventsInJavaFXThread();

        Assert.assertEquals("Changed Title", stage.getTitle());
        Platform.runLater(() -> stage.close());
    }

    @Test
    public void callMethod_InJavaFXThread_NoArgs() {
        Stage stage = setupStageInJavaFXThread();
        Assert.assertFalse(stage.isShowing());
        HelperFunctions.callMethod(stage, "show", true);
        waitForEventsInJavaFXThread();
        Assert.assertTrue(stage.isShowing());
        Platform.runLater(() -> stage.close());
    }

    @Test
    public void callMethod_InWrongThread() {
        Stage stage = setupStageInJavaFXThread();
        thrown.expect(JavaFXLibraryNonFatalException.class);
        thrown.expectMessage("Couldn't execute Call Method: Not on FX application thread; currentThread = main");
        HelperFunctions.callMethod(stage, "show", false);
    }

    @Test
    public void callMethod_WithWrongTypes() {
        Point point = new Point(0, 0);
        List<Object> arguments = Arrays.asList("20", "17");
        List<Object> types = Arrays.asList("java.lang.String", "java.lang.String");

        thrown.expect(JavaFXLibraryNonFatalException.class);
        thrown.expectMessage("class java.awt.Point has no method \"setLocation\" with arguments [class java.lang.String, class java.lang.String]");

        HelperFunctions.callMethod(point, "setLocation", arguments, types, false);
    }

    public class TestPoint extends Point {

        private TestPoint(int x, int y) {
            super(x, y);
        }

        public void setLocationTo2017() {
            this.setLocation(20, 17);
        }
    }
}
