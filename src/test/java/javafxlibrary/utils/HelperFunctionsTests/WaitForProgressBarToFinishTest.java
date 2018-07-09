package javafxlibrary.utils.HelperFunctionsTests;

import javafx.scene.control.ProgressBar;
import javafxlibrary.TestFxAdapterTest;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class WaitForProgressBarToFinishTest extends TestFxAdapterTest {

    private ProgressBar progressBar;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        progressBar = new ProgressBar();
    }

    @Test
    public void waitForProgressBarToFinish_IsFinished() {
        progressBar.setProgress(1);
        HelperFunctions.waitForProgressBarToFinish(progressBar, 1);
    }

    @Test
    public void waitForProgressBarToFinish_IsFinishedWithDelay() {
        progressBar.setProgress(0);
        Thread t = finishAfterTimeout();
        t.start();
        HelperFunctions.waitForProgressBarToFinish(progressBar, 1);
    }

    @Test
    public void waitForProgressBarToFinish_IsNotFinished() {
        progressBar.setProgress(0);
        thrown.expect(JavaFXLibraryNonFatalException.class);
        thrown.expectMessage("Given ProgressBar did not complete in 1 seconds!");
        HelperFunctions.waitForProgressBarToFinish(progressBar, 1);
    }

    private Thread finishAfterTimeout() {
        return new Thread(() -> {
            try {
                Thread.sleep(200);
                progressBar.setProgress(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
