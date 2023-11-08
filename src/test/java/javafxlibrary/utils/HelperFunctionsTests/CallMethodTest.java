package javafxlibrary.utils.HelperFunctionsTests;

import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.testfx.framework.junit.ApplicationTest;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;

import static testutils.TestFunctions.setupStageInJavaFXThread;
import static testutils.TestFunctions.waitForEventsInJavaFXThread;

public class CallMethodTest extends ApplicationTest {

    @Test
    public void callMethod_InSameThread_NoArgs_WithReturnValue() {
        String name = "JavaFXLibrary";
        String result = (String) HelperFunctions.callMethod(name, "toUpperCase", new Object[]{}, false);
        Assert.assertEquals("JAVAFXLIBRARY", result);
    }

    @Test
    public void callMethod_InSameThread_NoArgs_NoReturnValue() {
        TestPoint testPoint = new TestPoint(0, 0);
        HelperFunctions.callMethod(testPoint, "setLocationTo2017", new Object[]{}, false);
        Assert.assertEquals(20, testPoint.getX(), 0);
        Assert.assertEquals(17, testPoint.getY(), 0);
    }

    @Test
    public void callMethod_InSameThread_WithArgs_WithReturnValue() {
        String name = "JavaFXLibrary";
        Object[] arguments = {4, 9};
        String result = (String) HelperFunctions.callMethod(name, "substring", arguments, false);
        Assert.assertEquals("FXLib", result);
    }

    @Test
    public void callMethod_InSameThread_WithArgs_NoReturnValue() {
        Point point = new Point(0, 0);
        Object[] arguments = {20, 17};
        HelperFunctions.callMethod(point, "setLocation", arguments, false);
        Assert.assertEquals(20, point.getX(), 0);
        Assert.assertEquals(17, point.getY(), 0);
    }

    @Test
    public void callMethod_InJavaFXThread_WithArgs() {
        Button button = new Button("Button");
        Object[] arguments = {"Changed"};
        HelperFunctions.callMethod(button, "setText", arguments, true);
        waitForEventsInJavaFXThread();
        Assert.assertEquals("Changed", button.getText());
    }

    @Test
    public void callMethod_InJavaFXThread_NoArgs() {
        Button button = new Button("Button");
        button.setOnAction((e) -> button.setText("Clicked"));
        HelperFunctions.callMethod(button, "fire", new Object[]{}, true);
        waitForEventsInJavaFXThread();
        Assert.assertEquals("Clicked", button.getText());
    }

    @Test
    public void callMethod_InWrongThread() {
        Stage stage = setupStageInJavaFXThread();
        JavaFXLibraryNonFatalException exception = Assert.assertThrows(JavaFXLibraryNonFatalException.class, () -> {
            HelperFunctions.callMethod(stage, "show", new Object[]{}, false);
        });
        String expectedMessage = "Couldn't execute Call Method: Not on FX application thread; currentThread = main";
        String actualMessage = exception.getMessage();
        Assert.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void callMethod_WithWrongTypes() {
        Point point = new Point(0, 0);
        Object[] arguments = {"20", "17"};

        Exception exception = Assert.assertThrows(JavaFXLibraryNonFatalException.class, () -> {
            HelperFunctions.callMethod(point, "setLocation", arguments, false);
        });

        String expectedMessage = "class java.awt.Point has no method \"setLocation\" with arguments [class java.lang.String, class java.lang.String]";
        String actualMessage = exception.getMessage();
        Assert.assertTrue(actualMessage.contains(expectedMessage));
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
