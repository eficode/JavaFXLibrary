package javafxlibrary.utils.HelperFunctionsTests;

import javafx.geometry.HorizontalDirection;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static testutils.TestFunctions.useMac;
import static testutils.TestFunctions.useWindows;

public class GetHorizontalDirectionTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

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
        thrown.expect(JavaFXLibraryNonFatalException.class);
        thrown.expectMessage("Direction: \"BACKWARD\" is not a valid direction. Accepted values are: [LEFT, RIGHT]");
        HelperFunctions.getHorizontalDirection("BACKWARD");
    }
}
