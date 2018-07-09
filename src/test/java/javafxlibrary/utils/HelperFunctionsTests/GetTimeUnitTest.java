package javafxlibrary.utils.HelperFunctionsTests;

import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.concurrent.TimeUnit;

public class GetTimeUnitTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getTimeUnit_ValidArgument() {
        TimeUnit result = HelperFunctions.getTimeUnit("DAYS");
        Assert.assertEquals(TimeUnit.DAYS, result);
    }

    @Test
    public void getTimeUnit_InvalidArgument() {
        thrown.expect(JavaFXLibraryNonFatalException.class);
        thrown.expectMessage("\"LINES_OF_CODE\" is not a valid TimeUnit. Accepted values are: [NANOSECONDS, " +
                "MICROSECONDS, MILLISECONDS, SECONDS, MINUTES, HOURS, DAYS]");
        HelperFunctions.getTimeUnit("LINES_OF_CODE");
    }
}
