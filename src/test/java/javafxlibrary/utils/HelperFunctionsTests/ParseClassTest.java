package javafxlibrary.utils.HelperFunctionsTests;

import org.testfx.framework.junit.ApplicationTest;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import org.junit.Assert;
import org.junit.Test;

public class ParseClassTest extends ApplicationTest {

    @Test
    public void parseClass_PrimitiveTypes() {
        String[] names = new String[]{"boolean", "byte", "char", "double", "float", "int", "long", "short", "void"};
        Class<?>[] target = new Class[]{boolean.class, byte.class, char.class, double.class, float.class, int.class,
                long.class, short.class, void.class};

        for (int i = 0; i < names.length; i++)
            Assert.assertEquals(target[i], HelperFunctions.parseClass(names[i]));
    }

    @Test
    public void parseClass_Class() {
        Class<?> result = HelperFunctions.parseClass("javafx.scene.control.Button");
        Assert.assertEquals(javafx.scene.control.Button.class, result);
    }

    @Test
    public void parseClass_InvalidType() {
        JavaFXLibraryNonFatalException exception = Assert.assertThrows(JavaFXLibraryNonFatalException.class, () -> {
            HelperFunctions.parseClass("That's no class!");
        });
        Assert.assertEquals("Could not parse class \"That's no class!\"", exception.getMessage());
    }
}
