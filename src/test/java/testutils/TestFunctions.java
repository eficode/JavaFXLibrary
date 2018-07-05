package testutils;

import javafxlibrary.utils.HelperFunctions;
import mockit.Mock;
import mockit.MockUp;

public class TestFunctions {

    public static void useWindows() {
        new MockUp<HelperFunctions>() {
            @Mock
            boolean isMac() {
                return false;
            }
        };
    }

    public static void useMac() {
        new MockUp<HelperFunctions>() {
            @Mock
            boolean isMac() {
                return true;
            }
        };
    }
}
