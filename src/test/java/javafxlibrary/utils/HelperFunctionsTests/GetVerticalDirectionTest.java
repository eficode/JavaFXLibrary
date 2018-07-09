package javafxlibrary.utils.HelperFunctionsTests;

import javafx.geometry.VerticalDirection;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static testutils.TestFunctions.useMac;
import static testutils.TestFunctions.useWindows;

public class GetVerticalDirectionTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getVerticalDirection_WindowsAndLinux() {
        useWindows();
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
        thrown.expect(JavaFXLibraryNonFatalException.class);
        thrown.expectMessage("Direction: \"FORWARD\" is not a valid direction. Accepted values are: [UP, DOWN]");
        HelperFunctions.getVerticalDirection("FORWARD");
    }
}
