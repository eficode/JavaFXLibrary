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
        JavaFXLibraryNonFatalException exception = Assert.assertThrows(JavaFXLibraryNonFatalException.class, () -> {
            HelperFunctions.getPosition("NEXT_TO_THE_IMAGE");
        });
        String expectedMessage = "Position: \"NEXT_TO_THE_IMAGE\" is not a valid position. Accepted values are: [TOP_LEFT";
        String actualMessage = exception.getMessage();
        Assert.assertTrue(actualMessage.contains(expectedMessage));
    }
}
