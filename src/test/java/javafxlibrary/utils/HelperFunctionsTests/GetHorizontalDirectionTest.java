package javafxlibrary.utils.HelperFunctionsTests;

import javafx.geometry.HorizontalDirection;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import org.junit.Assert;
import org.junit.Test;

import static testutils.TestFunctions.useMac;
import static testutils.TestFunctions.useWindows;

public class GetHorizontalDirectionTest {

    @Test
    public void getHorizontalDirection_WindowsAndLinux() {
        useWindows();
        HorizontalDirection result = HelperFunctions.getHorizontalDirection("LEFT");
        Assert.assertEquals(HorizontalDirection.LEFT, result);
    }

    @Test
    public void getHorizontalDirection_MacNaturalScrolling() {
        useMac();
        HorizontalDirection result = HelperFunctions.getHorizontalDirection("LEFT");
        Assert.assertEquals(HorizontalDirection.RIGHT, result);
    }

    @Test
    public void getHorizontalDirection_InvalidValue() {
        try {
            HelperFunctions.getHorizontalDirection("BACKWARD");
            Assert.fail("Expected a JavaFXLibraryNonFatalException to be thrown");
        } catch (JavaFXLibraryNonFatalException e) {
            String target = "Direction: \"BACKWARD\" is not a valid direction. Accepted values are: [LEFT, RIGHT]";
            Assert.assertEquals(target, e.getMessage());
        }
    }
}
