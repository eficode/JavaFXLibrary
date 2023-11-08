/*
 * Copyright 2017-2018   Eficode Oy
 * Copyright 2018-       Robot Framework Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package javafxlibrary.keywords.AdditionalKeywordsTests.ConvenienceKeywords;

import javafx.application.Platform;
import javafx.scene.control.Button;
import org.testfx.framework.junit.ApplicationTest;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.keywords.AdditionalKeywords.ApplicationLauncher;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class WaitForEventsInFxApplicationThreadTest extends ApplicationTest {

    private static ApplicationLauncher keywords;

    @BeforeClass
    public static void setupKeywords() {
        keywords = new ApplicationLauncher();
    }

    @Test
    public void waitForEventsInFxApplicationThread() {
        Button b = createDelayedButton(2000);
        Platform.runLater(() -> b.fire());
        keywords.waitForEventsInFxApplicationThread(2);
        Assert.assertEquals("ChangedText", b.getText());
    }

    @Test
    public void waitForEventsInFxApplicationThread_TimeoutExceeded() {
        Button b = createDelayedButton(3000);
        Platform.runLater(() -> b.fire());
        JavaFXLibraryNonFatalException exception = Assert.assertThrows(JavaFXLibraryNonFatalException.class, () -> {
            keywords.waitForEventsInFxApplicationThread(1);
        });
        Assert.assertEquals("Events did not finish within the given timeout of 1 seconds.", exception.getMessage());
    }

    private Button createDelayedButton(int timeout) {
        Button b = new Button("OriginalText");
        b.setOnAction((e) -> {
            try {
                Thread.sleep(timeout);
                b.setText("ChangedText");
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        });
        return b;
    }
}
