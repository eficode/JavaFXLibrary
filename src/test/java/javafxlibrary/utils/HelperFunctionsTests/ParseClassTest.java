package javafxlibrary.utils.HelperFunctionsTests;

import javafxlibrary.TestFxAdapterTest;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ParseClassTest extends TestFxAdapterTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void parseClass_PrimitiveTypes() {
        String[] names = new String[]{"boolean", "byte", "char", "double", "float", "int", "long", "short", "void"};
        Class[] target = new Class[]{boolean.class, byte.class, char.class, double.class, float.class, int.class,
                long.class, short.class, void.class};

        for (int i = 0; i < names.length; i++)
            Assert.assertEquals(target[i], HelperFunctions.parseClass(names[i]));
    }

    @Test
    public void parseClass_Class() {
        Class result = HelperFunctions.parseClass("javafx.scene.control.Button");
        Assert.assertEquals(javafx.scene.control.Button.class, result);
    }

    @Test
    public void parseClass_InvalidType() {
        thrown.expect(JavaFXLibraryNonFatalException.class);
        thrown.expectMessage("Could not parse class \"That's no class!\"");
        HelperFunctions.parseClass("That's no class!");
    }
}
