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
        JavaFXLibraryNonFatalException exception = Assert.assertThrows(JavaFXLibraryNonFatalException.class, () -> {
            HelperFunctions.getTimeUnit("LINES_OF_CODE");
        });
        String expectedMessage = "\"LINES_OF_CODE\" is not a valid TimeUnit. Accepted values are: [NANOSECONDS, " +
                "MICROSECONDS, MILLISECONDS, SECONDS, MINUTES, HOURS, DAYS]";
        String actualMessage = exception.getMessage();
        Assert.assertTrue(actualMessage.contains(expectedMessage));
    }
}
