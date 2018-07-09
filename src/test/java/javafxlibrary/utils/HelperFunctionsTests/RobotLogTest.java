package javafxlibrary.utils.HelperFunctionsTests;

import javafxlibrary.utils.HelperFunctions;
import org.junit.*;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class RobotLogTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void robotLog_InfoLevel() {
        HelperFunctions.robotLog("INFO", "Info level log message");
        Assert.assertEquals("*INFO* Info level log message\n", outContent.toString());
    }

    @Test
    public void robotLog_DebugLevel() {
        HelperFunctions.robotLog("DEBUG", "Debug level log message");
        Assert.assertEquals("*DEBUG* Debug level log message\n", outContent.toString());
    }

    @Test
    public void robotLog_TraceLevel() {
        HelperFunctions.robotLog("TRACE", "Trace level log message");
        Assert.assertEquals("*TRACE* Trace level log message\n", outContent.toString());
    }

    @Test
    public void robotLog_WarnLevel() {
        HelperFunctions.robotLog("WARN", "Warning level log message");
        Assert.assertEquals("*WARN* Warning level log message\n", outContent.toString());
    }

    @Test
    public void robotLog_ErrorLevel() {
        HelperFunctions.robotLog("ERROR", "Error level log message");
        Assert.assertEquals("*ERROR* Error level log message\n", outContent.toString());
    }

    @Test
    public void robotLog_LowerCaseLogLevel() {
        HelperFunctions.robotLog("debug", "Log level in lower case");
        Assert.assertEquals("*DEBUG* Log level in lower case\n", outContent.toString());
    }

    @Test
    public void robotLog_UnsupportedLogLevel() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Unsupported log level \"SAUSAGE\": Accepted levels are INFO, DEBUG, TRACE, WARN and ERROR.");
        HelperFunctions.robotLog("SAUSAGE", "This should raise an exception!");
    }
}
