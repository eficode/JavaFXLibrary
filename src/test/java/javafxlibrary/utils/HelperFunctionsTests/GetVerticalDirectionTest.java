package javafxlibrary.utils.HelperFunctionsTests;

import javafx.geometry.VerticalDirection;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import org.junit.Assert;
import org.junit.Test;

import static testutils.TestFunctions.useMac;
import static testutils.TestFunctions.useWindows;

public class GetVerticalDirectionTest {

    @Test
    public void getVerticalDirection_WindowsAndLinux() {
        useWindows();
        System.out.println(HelperFunctions.isMac());
        VerticalDirection result = HelperFunctions.getVerticalDirection("DOWN");
        Assert.assertEquals(VerticalDirection.DOWN, result);
    }

    @Test
    public void getVerticalDirection_MacNaturalScrolling() {
        useMac();
        VerticalDirection result = HelperFunctions.getVerticalDirection("DOWN");
        Assert.assertEquals(VerticalDirection.UP, result);
    }

    @Test
    public void getVerticalDirection_InvalidValue() {
        try {
            HelperFunctions.getVerticalDirection("FORWARD");
            Assert.fail("Expected a JavaFXLibraryNonFatalException to be thrown");
        } catch (JavaFXLibraryNonFatalException e) {
            String target = "Direction: \"FORWARD\" is not a valid direction. Accepted values are: [UP, DOWN]";
            Assert.assertEquals(target, e.getMessage());
        }
    }
}
