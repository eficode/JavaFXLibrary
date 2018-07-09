package javafxlibrary.utils.HelperFunctionsTests;

import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.testfx.robot.Motion;

public class GetMotionTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getMotion_Default() {
        Motion motion = HelperFunctions.getMotion("DEFAULT");
        Assert.assertEquals(Motion.DEFAULT, motion);
    }

    @Test
    public void getMotion_Direct() {
        Motion motion = HelperFunctions.getMotion("DIRECT");
        Assert.assertEquals(Motion.DIRECT, motion);
    }

    @Test
    public void getMotion_HorizontalFirst() {
        Motion motion = HelperFunctions.getMotion("HORIZONTAL_FIRST");
        Assert.assertEquals(Motion.HORIZONTAL_FIRST, motion);
    }

    @Test
    public void getMotion_VerticalFirst() {
        Motion motion = HelperFunctions.getMotion("VERTICAL_FIRST");
        Assert.assertEquals(Motion.VERTICAL_FIRST, motion);
    }

    @Test
    public void getMotion_InvalidValue() {
        thrown.expect(JavaFXLibraryNonFatalException.class);
        thrown.expectMessage("\"ZIGZAG\" is not a valid Motion. Accepted values are: [DEFAULT, DIRECT, " +
                "HORIZONTAL_FIRST, VERTICAL_FIRST]");
        HelperFunctions.getMotion("ZIGZAG");
    }
}
