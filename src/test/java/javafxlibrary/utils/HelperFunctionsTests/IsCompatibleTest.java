package javafxlibrary.utils.HelperFunctionsTests;

import javafx.scene.control.Button;
import org.testfx.framework.junit.ApplicationTest;
import javafxlibrary.utils.HelperFunctions;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class IsCompatibleTest extends ApplicationTest {

    @Test
    public void isCompatible_TestAllValidTypes() {
        // Void cannot be instantiated
        Object[] objects = new Object[]{Integer.valueOf("2"), Double.valueOf("2.00"), Long.valueOf("200"),
                Float.valueOf("2.00"), Character.valueOf('b'), Boolean.TRUE, Byte.valueOf("10"), Short.valueOf("1"),
                "String", new ArrayList<>()};

        for (Object o : objects) {
            Assert.assertTrue(HelperFunctions.isCompatible(o));
        }
    }

    @Test
    public void isCompatible_InvalidType() {
        Button button = new Button();
        Assert.assertFalse(HelperFunctions.isCompatible(button));
    }
}
