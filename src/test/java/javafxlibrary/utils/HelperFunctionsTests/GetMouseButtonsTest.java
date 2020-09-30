package javafxlibrary.utils.HelperFunctionsTests;

import javafx.scene.input.MouseButton;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class GetMouseButtonsTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getMouseButtons_Middle() {
        MouseButton result = HelperFunctions.getMouseButtons(new String[]{"MIDDLE"})[0];
        Assert.assertEquals(MouseButton.MIDDLE, result);
    }

    @Test
    public void getMouseButtons_None() {
        MouseButton result = HelperFunctions.getMouseButtons(new String[]{"NONE"})[0];
        Assert.assertEquals(MouseButton.NONE, result);
    }

    @Test
    public void getMouseButtons_Primary() {
        MouseButton result = HelperFunctions.getMouseButtons(new String[]{"PRIMARY"})[0];
        Assert.assertEquals(MouseButton.PRIMARY, result);
    }

    @Test
    public void getMouseButtons_Secondary() {
        MouseButton result = HelperFunctions.getMouseButtons(new String[]{"SECONDARY"})[0];
        Assert.assertEquals(MouseButton.SECONDARY, result);
    }

    @Test
    public void getMouseButtons_MultipleValues() {
        MouseButton[] target = new MouseButton[] { MouseButton.PRIMARY, MouseButton.SECONDARY,
                MouseButton.MIDDLE, MouseButton.NONE };

        MouseButton[] result = HelperFunctions.getMouseButtons(new String[]{"PRIMARY", "SECONDARY", "MIDDLE", "NONE"});
        Assert.assertArrayEquals(target, result);
    }

    @Test
    public void getMouseButtons_InvalidValue() {
        thrown.expect(JavaFXLibraryNonFatalException.class);
       // thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("\"HUGE_RED_ONE\" is not a valid MouseButton. Accepted values are: [NONE, PRIMARY, MIDDLE, SECONDARY]");
        HelperFunctions.getMouseButtons(new String[]{"HUGE_RED_ONE"});
    }

}
