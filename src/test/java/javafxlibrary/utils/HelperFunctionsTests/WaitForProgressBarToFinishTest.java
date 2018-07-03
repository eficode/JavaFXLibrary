package javafxlibrary.utils.HelperFunctionsTests;

import javafx.scene.control.ProgressBar;
import javafxlibrary.TestFxAdapterTest;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WaitForProgressBarToFinishTest extends TestFxAdapterTest {
    private ProgressBar progressBar;

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
        try {
            HelperFunctions.waitForProgressBarToFinish(progressBar, 1);
            Assert.fail("Expected a JavaFXLibraryNonFatalException to be thrown");
        } catch (JavaFXLibraryNonFatalException e) {
            String target = "Given ProgressBar did not complete in 1 seconds!";
            Assert.assertEquals(e.getMessage(), target);
        }
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
