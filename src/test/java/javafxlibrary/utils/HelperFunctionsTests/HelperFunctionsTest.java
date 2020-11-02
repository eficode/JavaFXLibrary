package javafxlibrary.utils.HelperFunctionsTests;

import javafxlibrary.utils.HelperFunctions;
import org.junit.Assert;
import org.junit.Test;

public class HelperFunctionsTest {

    @Test
    public void helperFunctions_setSafeClickingOff() {
        HelperFunctions.setSafeClicking(false);
        Boolean result = (Boolean) HelperFunctions.getFieldsValue(null, HelperFunctions.class, "safeClicking");
        Assert.assertFalse(result);
    }

    @Test
    public void helperFunctions_setSafeClickingOn() {
        HelperFunctions.setSafeClicking(true);
        Boolean result = (Boolean) HelperFunctions.getFieldsValue(null, HelperFunctions.class, "safeClicking");
        Assert.assertTrue(result);
    }

    @Test
    public void helperFunctions_setWaitUntilTimeout() {
        HelperFunctions.setLibraryKeywordTimeout(2);
        Integer result = (Integer) HelperFunctions.getFieldsValue(null, HelperFunctions.class, "libraryKeywordTimeout");
        Assert.assertEquals(2, (int) result);
    }
}
