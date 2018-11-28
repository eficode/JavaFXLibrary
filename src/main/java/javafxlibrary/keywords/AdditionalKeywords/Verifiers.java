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

package javafxlibrary.keywords.AdditionalKeywords;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Window;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.keywords.Keywords.ScreenCapturing;
import javafxlibrary.matchers.ExtendedNodeMatchers;
import javafxlibrary.matchers.ToggleMatchers;
import javafxlibrary.utils.RobotLog;
import javafxlibrary.utils.TestFxAdapter;
import org.robotframework.javalib.annotation.*;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.base.WindowMatchers;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TextMatchers;
import org.testfx.matcher.control.TextFlowMatchers;
import org.testfx.matcher.control.TextInputControlMatchers;
import org.testfx.service.support.PixelMatcherResult;
import org.testfx.service.support.impl.PixelMatcherRgb;
import org.hamcrest.core.IsNot;

import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.*;
import static javafxlibrary.utils.HelperFunctions.*;

@RobotKeywords
public class Verifiers extends TestFxAdapter {

    @RobotKeyword("Waits until given element can be found.\n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the element, see "
            + "`3. Locating or specifying UI elements`. \n\n"
            + "``timeout`` is the maximum waiting time value, defaults to 10 \n\n"
            + "``timeUnit`` is the time unit to be used, defaults to SECONDS, see `5. Used ENUMs` for more options for _timeUnit_. \n\n"
            + "\nExample:\n"
            + "| Wait Until Element Exists | \\#some-node-id | \n"
            + "| Wait Until Element Exists | \\#some-node-id | 200 | MILLISECONDS | \n")
    @ArgumentNames({"locator", "timeout=10", "timeUnit=SECONDS"})
    public Object waitUntilElementExists(String locator, int timeout, String timeUnit) {
        RobotLog.info("Waiting until page contains element: \"" + locator + "\", timeout=\"" + timeout + "\", timeUnit= \"" + timeUnit + "\"");

        try {
            return mapObject(waitUntilExists(locator, timeout, timeUnit));
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new JavaFXLibraryNonFatalException("Something went wrong while waiting element \"" + locator + "\" to appear.", e );
        }
    }

    @RobotKeywordOverload
    @ArgumentNames({"locator"})
    public Object waitUntilElementExists(String locator) {
        return waitUntilElementExists(locator, 10, "SECONDS");
    }

    @RobotKeyword("Waits until a node located by given locator becomes visible. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the element, see "
            + "`3. Locating or specifying UI elements`. \n\n"
            + "``timeout`` is the maximum waiting time in seconds, defaults to 5. \n\n")
            @ArgumentNames({"locator", "timeout=5"})
    public void waitUntilNodeIsVisible(Object locator, int timeout) {
        RobotLog.info("Waiting for node \"" + locator + "\" to be visible, timeout=\"" + timeout + "\"");

        try {
            waitUntilVisible(locator, timeout);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new JavaFXLibraryNonFatalException("");
        }
    }

    @RobotKeywordOverload
    @ArgumentNames({"locator"})
    public void waitUntilNodeIsVisible(Object locator) {
        waitUntilNodeIsVisible(locator, 5);
    }

    @RobotKeyword("Waits until a node located using given locator becomes enabled. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the element, see "
            + "`3. Locating or specifying UI elements`. \n\n"
            + "``timeout`` is the maximum waiting time in seconds, defaults to 5. \n\n")
    @ArgumentNames({"locator", "timeout=5"})
    public void waitUntilNodeIsEnabled(Object locator, int timeout) {
        RobotLog.info("Waiting for node \"" + locator + "\" to be visible, timeout=\"" + timeout + "\"");

        try {
            waitUntilEnabled(locator, timeout);
        } catch (IllegalArgumentException | NullPointerException e){
            throw new JavaFXLibraryNonFatalException("");
        }
    }

    @RobotKeywordOverload
    @ArgumentNames({"locator"})
    public void waitUntilNodeIsEnabled(Object locator) {
        waitUntilNodeIsEnabled(locator, 5);
    }

    @RobotKeyword("Verifies that node is visible. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the Node, see "
            + "`3. Locating or specifying UI elements`. \n\n")
    @ArgumentNames({ "locator" })
    public static void nodeShouldBeVisible(Object locator) {
        verifyThat(objectToNode(locator), isVisible() );
    }

    @RobotKeyword("Verifies that node is invisible. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the Node, see "
            + "`3. Locating or specifying UI elements`. \n\n")
    @ArgumentNames({ "locator" })
    public static void nodeShouldBeInvisible(Object locator) {
        verifyThat(objectToNode(locator), isInvisible() );
    }

    @RobotKeyword("Verifies that node is focused. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the Node, see "
            + "`3. Locating or specifying UI elements`. \n\n")
    @ArgumentNames({ "locator" })
    public static void nodeShouldBeFocused(Object locator) {
        verifyThat(objectToNode(locator), isFocused() );
    }

    @RobotKeyword("Verifies that node is not focused. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the Node, see "
            + "`3. Locating or specifying UI elements`. \n\n")
    @ArgumentNames({ "locator" })
    public static void nodeShouldNotBeFocused(Object locator) {
        verifyThat(objectToNode(locator), isNotFocused() );
    }

    @RobotKeyword("Verifies that node is enabled. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the Node, see "
            + "`3. Locating or specifying UI elements`. \n\n")
    @ArgumentNames({ "locator" })
    public static void nodeShouldBeEnabled(Object locator) {
        verifyThat(objectToNode(locator), isEnabled() );
    }

    @RobotKeyword("Verifies that node is disabled. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the Node, see "
            + "`3. Locating or specifying UI elements`. \n\n")
    @ArgumentNames({ "locator" })
    public static void nodeShouldBeDisabled(Object locator) {
        verifyThat(objectToNode(locator), NodeMatchers.isDisabled() );
    }

    @RobotKeyword("Verifies that node is hoverable with mouse. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the Node, see "
            + "`3. Locating or specifying UI elements`. \n\n")
    @ArgumentNames({ "locator" })
    public static void nodeShouldBeHoverable(Object locator) {
        try {
            verifyThat(objectToNode(locator), ExtendedNodeMatchers.isHoverable());
        } catch (AssertionError ae){
            Node node = getHoveredNode();
            RobotLog.info("Given locator node: \"" + locator + "\" was not hoverable! Instead, following " +
                    "node was found: \"" + node + "\". See screenshot below: ");
            new ScreenCapturing().captureImage(node);
            throw ae;
        }
    }

    @RobotKeyword("Verifies that given node has text. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the Node, see "
            + "`3. Locating or specifying UI elements`. \n\n"
            + "``text`` is the String to be searched for")
    @ArgumentNames({ "locator", "text" })
    public static void nodeShouldHaveText(Object locator, String text) {
        Object node = objectToNode(locator);

        if (node instanceof Text)
            verifyThat((Text) node, TextMatchers.hasText(text));
        else if (node instanceof Labeled)
            verifyThat((Labeled) node, LabeledMatchers.hasText(text));
        else if (node instanceof TextInputControl)
            verifyThat((TextInputControl) node, TextInputControlMatchers.hasText(text));
        else if (node instanceof TextFlow)
            verifyThat((TextFlow) node, TextFlowMatchers.hasText(text));
    }

    @RobotKeyword("Verifies that given node has not text. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the Node, see "
            + "`3. Locating or specifying UI elements`. \n\n"
            + "``text`` is the String to be searched for")
    @ArgumentNames({ "locator", "text" })
    public static void nodeShouldNotHaveText(Object locator, String text) {
        Object node = objectToNode(locator);

        if (node instanceof Text)
            verifyThat((Text) node, IsNot.not(TextMatchers.hasText(text)));
        else if (node instanceof Labeled)
            verifyThat((Labeled) node, IsNot.not(LabeledMatchers.hasText(text)));
        else if (node instanceof TextInputControl)
            verifyThat((TextInputControl) node, IsNot.not(TextInputControlMatchers.hasText(text)));
        else if (node instanceof TextFlow)
            verifyThat((TextFlow) node, IsNot.not(TextFlowMatchers.hasText(text)));
    }

    @RobotKeyword("Verifies that given window is showing. \n\n"
            + "``window`` is the _Object:Window_ that specifies which window should be showing, see `3.2 Using objects`")
    @ArgumentNames({ "window" })
    public static void windowShouldBeShowing(Object window) {
        verifyThat((Window) window, WindowMatchers.isShowing());
    }

    @RobotKeyword("Verifies that given window is focused. \n\n"
            + "``window`` is the _Object:Window_ that specifies which window should be focused, see `3.2 Using objects`")
    @ArgumentNames({ "window" })
    public static void windowShouldBeFocused(Object window) {
        verifyThat((Window) window, WindowMatchers.isFocused());
    }

    @RobotKeyword("Checks if given two bounds are equal. \n\n"
            + "``firstBounds`` is an _Object:Bounds_ that specifies the first comparable Bounds\n\n"
            + "``secondBounds`` is an _Object:Bounds_ that specifies the second comparable Bounds, see `3.2 Using objects`")
    @ArgumentNames({ "firstBounds", "secondBounds" })
    public void boundsShouldBeEqual(Bounds firstBounds, Bounds secondBounds) {
        RobotLog.info("Checking if \"" + firstBounds + "\" equals with \"" + secondBounds + "\"");
        assertTrue(firstBounds + " != " + secondBounds, firstBounds.equals(secondBounds));
    }

    @RobotKeyword("Fails if images are not similar enough\n\n"
            + "``image1`` is an _Object:Image_ for the first comparable image.\n\n"
            + "``image2`` is an _Object:Image_ for the second comparable image.\n\n"
            + "``percentage`` the percentage of pixels that should match, defaults to 100.\n\n"
            + "This keyword can be coupled with e.g. `Capture Image` -keyword.")
    @ArgumentNames({ "image1", "image2", "percentage=100" })
    public void imagesShouldMatch(Image image1, Image image2, double percentage) {
        RobotLog.info("Checking if " + percentage + "% of " + image1 + " matches with " + image2);

        if (image1.getHeight() != image2.getHeight() || image1.getWidth() != image2.getWidth())
            throw new JavaFXLibraryNonFatalException("Images must be same size to compare: Image1 is " + (int)image1.getWidth()
                    + "x" + (int)image1.getHeight() + " and Image2 is " + (int)image2.getWidth() + "x" + (int)image2.getHeight());

        PixelMatcherResult result = robotContext().getCaptureSupport().matchImages(image1, image2, new PixelMatcherRgb());
        int sharedPixels = (int) (result.getMatchFactor() * 100);
        RobotLog.info("Matching pixels: " + sharedPixels + "%");

        if (sharedPixels < percentage)
            throw new JavaFXLibraryNonFatalException("Images do not match - Expected at least " + (int) percentage + "% " +
                    "similarity, got " + sharedPixels + "%");
    }

    @RobotKeywordOverload
    @ArgumentNames( {"image1", "image2"} )
    public void imagesShouldMatch(Image image1, Image image2) {
        imagesShouldMatch(image1, image2, 100);
    }

    @RobotKeyword("Fails if images are too similar\n\n"
            + "``image1`` is an _Object:Image_ for the first comparable image.\n\n"
            + "``image2`` is an _Object:Image_ for the second comparable image.\n\n"
            + "``percentage`` the percentage of pixels that should not match, defaults to 100.\n\n"
            + "This keyword can be coupled with e.g. `Capture Image` -keyword.")
    @ArgumentNames({ "image1", "image2", "percentage=100" })
    public void imagesShouldNotMatch(Image image1, Image image2, double percentage) {
        RobotLog.info("Checking if " + percentage + "% of " + image1 + " differs with " + image2);

        if (image1.getHeight() != image2.getHeight() || image1.getWidth() != image2.getWidth())
            throw new JavaFXLibraryNonFatalException("Images must be same size to compare: Image1 is " + (int)image1.getWidth()
                    + "x" + (int)image1.getHeight() + " and Image2 is " + (int)image2.getWidth() + "x" + (int)image2.getHeight());

        PixelMatcherResult result = robotContext().getCaptureSupport().matchImages(image1, image2, new PixelMatcherRgb());
        int nonSharedPixels = (int) (result.getNonMatchFactor() * 100);
        RobotLog.info("Matching pixels: " + nonSharedPixels + "%");

        if (nonSharedPixels < percentage)
            throw new JavaFXLibraryNonFatalException("Images are too similar - Expected at least " + (int) percentage + "% " +
                    "difference, got " + nonSharedPixels + "%");
    }

    @RobotKeywordOverload
    @ArgumentNames( {"image1", "image2"} )
    public void imagesShouldNotMatch(Image image1, Image image2) {
        imagesShouldNotMatch(image1, image2, 100);
    }

    @RobotKeyword("Verifies that RadioButton is selected. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the RadioButton element, see "
            + "`3. Locating or specifying UI elements`. \n\n")
    @ArgumentNames({ "locator" })
    public static void radioButtonShouldBeSelected(Object locator) {
        try {
            verifyThat((RadioButton) objectToNode(locator), ToggleMatchers.isSelected());
        } catch (ClassCastException cce) {
            throw new JavaFXLibraryNonFatalException("Unable to handle given locator as RadioButton!");
        }
    }

    @RobotKeyword("Verifies that RadioButton is not selected. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the RadioButton element, see "
            + "`3. Locating or specifying UI elements`. \n\n")
    @ArgumentNames({ "locator" })
    public static void radioButtonShouldNotBeSelected(Object locator) {
        try {
            verifyThat((RadioButton) objectToNode(locator), ToggleMatchers.isNotSelected());
        } catch (ClassCastException cce) {
            throw new JavaFXLibraryNonFatalException("Unable to handle given locator as RadioButton!");
        }
    }

    @RobotKeyword("Verifies that ToggleButton is selected. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the ToggleButton element, see "
            + "`3. Locating or specifying UI elements`. \n\n")
    @ArgumentNames({ "locator" })
    public static void toggleButtonShouldBeSelected(Object locator) {
        try {
            verifyThat((ToggleButton) objectToNode(locator), ToggleMatchers.isSelected());
        } catch (ClassCastException cce) {
            throw new JavaFXLibraryNonFatalException("Unable to handle given locator as ToggleButton!");
        }
    }

    @RobotKeyword("Verifies that ToggleButton is not selected. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the ToggleButton element, see "
            + " `3. Locating or specifying UI elements`. \n\n")
    @ArgumentNames({ "locator" })
    public static void toggleButtonShouldNotBeSelected(Object locator) {
        try{
            verifyThat((ToggleButton) objectToNode(locator), ToggleMatchers.isNotSelected());
        } catch (ClassCastException cce) {
            throw new JavaFXLibraryNonFatalException("Unable to handle given locator as ToggleButton!");
        }
    }
    @RobotKeyword("Waits until given ProgressBar is finished or timeout expires. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the ToggleButton element, see "
            + " `3. Locating or specifying UI elements`. \n\n"
            + "``timeout`` is an integer value for timeout in seconds, defaults to 20 seconds.")
    @ArgumentNames({ "locator", "timeout=20" })
    public static void waitUntilProgressBarIsFinished(Object locator, int timeout) {
        try {
            ProgressBar pb = (ProgressBar) objectToNode(locator);
            waitForProgressBarToFinish(pb, timeout);
        } catch (ClassCastException cce) {
            throw new JavaFXLibraryNonFatalException("Unable to handle given locator as ProgressBar!");
        }
    }

    @RobotKeywordOverload
    public static void waitUntilProgressBarIsFinished(Object locator) {
        waitUntilProgressBarIsFinished(locator, 20);
    }

}