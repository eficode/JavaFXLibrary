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
        Object[] objects = new Object[]{new Integer("2"), new Double("2.00"), new Long("200"),
                new Float("2.00"), new Character('b'), Boolean.TRUE, new Byte("10"), new Short("1"),
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
