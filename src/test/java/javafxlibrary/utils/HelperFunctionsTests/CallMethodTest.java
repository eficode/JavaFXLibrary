package javafxlibrary.utils.HelperFunctionsTests;

import javafx.application.Platform;
import javafx.stage.Stage;
import javafxlibrary.TestFxAdapterTest;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import org.junit.Assert;
import org.junit.Test;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;

public class CallMethodTest extends TestFxAdapterTest {

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
        Stage stage = waitForStageSetup();
        stage.setTitle("Original title");

        Platform.runLater(() -> stage.show());
        waitForStageToShow(stage);

        List<Object> arguments = Arrays.asList("Changed Title");
        List<Object> types = Arrays.asList("java.lang.String");
        HelperFunctions.callMethod(stage, "setTitle", arguments, types, true);

        waitForTitleToChange(stage);
        Assert.assertEquals("Changed Title", stage.getTitle());
        Platform.runLater(()->stage.close());
    }

    @Test
    public void callMethod_InJavaFXThread_NoArgs() {
        Stage stage = waitForStageSetup();
        Assert.assertFalse(stage.isShowing());
        HelperFunctions.callMethod(stage, "show", true);
        waitForStageToShow(stage);
        Assert.assertTrue(stage.isShowing());
        Platform.runLater(()->stage.close());
    }

    @Test
    public void callMethod_InWrongThread() {
        Stage stage = waitForStageSetup();
        try {
            HelperFunctions.callMethod(stage, "show", false);
            Assert.fail("Expected a JavaFXLibraryNonFatalException to be thrown");
        } catch (JavaFXLibraryNonFatalException e) {
            String target = "Couldn't execute Call Method: Not on FX application thread; currentThread = main";
            Assert.assertEquals(target, e.getMessage());
        }
    }

    @Test
    public void callMethod_WithWrongTypes() {
        Point point = new Point(0, 0);
        List<Object> arguments = Arrays.asList("20", "17");
        List<Object> types = Arrays.asList("java.lang.String", "java.lang.String");
        try {
            HelperFunctions.callMethod(point, "setLocation", arguments, types, false);
            Assert.fail("Expected a JavaFXLibraryNonFatalException to be thrown");
        } catch (JavaFXLibraryNonFatalException e) {
            String target = "class java.awt.Point has no method \"setLocation\" with arguments [class java.lang.String, class java.lang.String]";
            Assert.assertEquals(target, e.getMessage());
        }
    }




    private Stage waitForStageSetup() {
        Stage[] stages = new Stage[1];
        Platform.runLater(() -> stages[0] = new Stage());

        while (stages[0] == null)
            sleepFor(500);

        return stages[0];
    }

    // Combine helper methods with generic method invocation?
    private void waitForTitleToChange(Stage stage) {
        int count = 0;
        String originalTitle = stage.getTitle();

        // Waits 2.5 seconds for the stage title to change
        while (count < 50 && stage.getTitle().equals(originalTitle)) {
            sleepFor(50);
            count++;
        }
    }

    private void waitForStageToShow(Stage stage) {
        int count = 0;

        // Waits 2.5 seconds for the stage to show
        while (count < 50 && !stage.isShowing()) {
            sleepFor(50);
            count++;
        }
        System.out.println("STAGE IS SHOWING" + stage.isShowing());
    }

    private void sleepFor(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
