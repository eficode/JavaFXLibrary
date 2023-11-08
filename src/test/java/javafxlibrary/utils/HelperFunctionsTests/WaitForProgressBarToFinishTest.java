package javafxlibrary.utils.HelperFunctionsTests;

import javafx.scene.control.ProgressBar;
import org.testfx.framework.junit.ApplicationTest;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class WaitForProgressBarToFinishTest extends ApplicationTest {

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
        JavaFXLibraryNonFatalException exception = Assert.assertThrows(JavaFXLibraryNonFatalException.class, () -> {
            HelperFunctions.waitForProgressBarToFinish(progressBar, 1);
        });
        Assert.assertEquals("Given ProgressBar did not complete in 1 seconds!", exception.getMessage());
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
