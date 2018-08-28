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
import javafxlibrary.TestFxAdapterTest;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.keywords.AdditionalKeywords.ConvenienceKeywords;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class WaitForEventsInFxApplicationThreadTest extends TestFxAdapterTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private static ConvenienceKeywords keywords;

    @BeforeClass
    public static void setupTests() {
        keywords = new ConvenienceKeywords();
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
        thrown.expect(JavaFXLibraryNonFatalException.class);
        thrown.expectMessage("Events did not finish within the given timeout of 1 seconds.");
        Button b = createDelayedButton(3000);
        Platform.runLater(() -> b.fire());
        keywords.waitForEventsInFxApplicationThread(1);
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
