package javafxlibrary.utils.HelperFunctionsTests;

import javafx.geometry.Pos;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import org.junit.Assert;
import org.junit.Test;

public class GetPositionTest {

    @Test
    public void getPosition_ValidArgument() {
        Pos result = HelperFunctions.getPosition("BOTTOM_CENTER");
        Assert.assertEquals(Pos.BOTTOM_CENTER, result);
    }

    @Test
    public void getPosition_InvalidArgument() {
        try {
            HelperFunctions.getPosition("NEXT_TO_THE_IMAGE");
            Assert.fail("Expected a JavaFXLibraryNonFatalException to be thrown");
        } catch (JavaFXLibraryNonFatalException e) {
            String target = "Position: \"NEXT_TO_THE_IMAGE\" is not a valid position. Accepted values are: [TOP_LEFT";
            Assert.assertTrue(e.getMessage().startsWith(target));
        }
    }
}
