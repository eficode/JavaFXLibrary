package javafxlibrary.utils.HelperFunctionsTests;

import javafx.geometry.Pos;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class GetPositionTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getPosition_ValidArgument() {
        Pos result = HelperFunctions.getPosition("BOTTOM_CENTER");
        Assert.assertEquals(Pos.BOTTOM_CENTER, result);
    }

    @Test
    public void getPosition_InvalidArgument() {
        thrown.expect(JavaFXLibraryNonFatalException.class);
        thrown.expectMessage("Position: \"NEXT_TO_THE_IMAGE\" is not a valid position. Accepted values are: [TOP_LEFT");
        HelperFunctions.getPosition("NEXT_TO_THE_IMAGE");
    }
}
