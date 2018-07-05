package javafxlibrary.utils.HelperFunctionsTests;

import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class GetTimeUnitTest {

    @Test
    public void getTimeUnit_ValidArgument() {
        TimeUnit result = HelperFunctions.getTimeUnit("DAYS");
        Assert.assertEquals(TimeUnit.DAYS, result);
    }

    @Test
    public void getTimeUnit_InvalidArgument() {
        try {
            HelperFunctions.getTimeUnit("LINES_OF_CODE");
            Assert.fail("Expected a JavaFXLibraryNonFatalException to be thrown");
        } catch (JavaFXLibraryNonFatalException e) {
            String target = "\"LINES_OF_CODE\" is not a valid TimeUnit. Accepted values are: [NANOSECONDS, " +
                    "MICROSECONDS, MILLISECONDS, SECONDS, MINUTES, HOURS, DAYS]";
            Assert.assertEquals(target, e.getMessage());
        }
    }
}
