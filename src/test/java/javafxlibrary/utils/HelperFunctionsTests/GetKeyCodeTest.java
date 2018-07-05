package javafxlibrary.utils.HelperFunctionsTests;

import javafx.scene.input.KeyCode;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import org.junit.Assert;
import org.junit.Test;

public class GetKeyCodeTest {

    @Test
    public void getKeyCode_ValidArgument() {
        KeyCode result = HelperFunctions.getKeyCode(new String[]{"EURO_SIGN"})[0];
        Assert.assertEquals(KeyCode.EURO_SIGN, result);
    }

    @Test
    public void getKeyCode_MultipleArguments() {
        KeyCode[] target = new KeyCode[]{KeyCode.EURO_SIGN, KeyCode.DOLLAR, KeyCode.ESCAPE};
        KeyCode[] result = HelperFunctions.getKeyCode(new String[]{"EURO_SIGN", "DOLLAR", "ESCAPE"});
        Assert.assertArrayEquals(target, result);
    }

    @Test
    public void getKeyCode_InvalidArgument() {
        try {
            HelperFunctions.getKeyCode(new String[]{"SAUSAGE"});
            Assert.fail("Expected a JavaFXLibraryNonFatalException to be thrown");
        } catch (JavaFXLibraryNonFatalException e) {
            String target = "\"SAUSAGE\" is not a valid Keycode. Accepted values are: [ENTER, BACK_SPACE, TAB";
            Assert.assertTrue(e.getMessage().startsWith(target));
        }
    }
}
