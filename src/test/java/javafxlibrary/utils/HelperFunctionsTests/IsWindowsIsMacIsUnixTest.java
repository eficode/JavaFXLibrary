package javafxlibrary.utils.HelperFunctionsTests;

import javafxlibrary.utils.HelperFunctions;
import mockit.Mock;
import mockit.MockUp;
import org.junit.Assert;
import org.junit.Test;

public class IsWindowsIsMacIsUnixTest {

    @Test
    public void isWindows() {
        fakeWindows();
        Assert.assertTrue(HelperFunctions.isWindows());
        Assert.assertFalse(HelperFunctions.isMac());
        Assert.assertFalse(HelperFunctions.isUnix());
    }

    @Test
    public void isMac() {
        fakeMac();
        Assert.assertTrue(HelperFunctions.isMac());
        Assert.assertFalse(HelperFunctions.isWindows());
        Assert.assertFalse(HelperFunctions.isUnix());
    }

    @Test
    public void isUnix() {
        fakeUnix();
        Assert.assertTrue(HelperFunctions.isUnix());
        Assert.assertFalse(HelperFunctions.isMac());
        Assert.assertFalse(HelperFunctions.isWindows());
    }

    private void fakeWindows() {
        new MockUp<System>() {
            @Mock
            String getProperty(String any) {
                return "Windows";
            }
        };
    }

    private void fakeMac() {
        new MockUp<System>() {
            @Mock
            String getProperty(String any) {
                return "Mac";
            }
        };
    }

    private void fakeUnix() {
        new MockUp<System>() {
            @Mock
            String getProperty(String any) {
                return "Linux";
            }
        };
    }
}
