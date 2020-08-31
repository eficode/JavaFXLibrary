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

import static org.junit.Assert.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.*;
import static javafxlibrary.utils.HelperFunctions.*;

@RobotKeywords
public class Verifiers extends TestFxAdapter {

    @RobotKeyword("Waits until given element can be found.\n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the element, see "
            + "`3. Locating JavaFX Nodes`. \n\n"
            + "``timeout`` is the maximum waiting time value, defaults to 10 \n\n"
            + "``timeUnit`` is the time unit to be used, defaults to SECONDS, see `5. Used ENUMs` for more options for _timeUnit_. \n\n"
            + "\nExample:\n"
            + "| Wait Until Element Exists | \\#some-node-id | \n"
            + "| Wait Until Element Exists | \\#some-node-id | 200 | MILLISECONDS | \n")
    @ArgumentNames({"locator", "timeout=10", "timeUnit=SECONDS"})
    public Object waitUntilElementExists(String locator, int timeout, String timeUnit) {
        try {
            RobotLog.info("Waiting until page contains element: \"" + locator + "\", timeout=\"" + timeout + "\", timeUnit=\"" + timeUnit + "\".");
            return mapObject(waitUntilExists(locator, timeout, timeUnit));
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new JavaFXLibraryNonFatalException("Something went wrong while waiting element \"" + locator + "\" to appear.", e );
        }
    }

    @RobotKeyword("Waits until given element is not found.\n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the element, see "
            + "`3. Locating JavaFX Nodes`. \n\n"
            + "``timeout`` is the maximum waiting time value, defaults to 10 \n\n"
            + "``timeUnit`` is the time unit to be used, defaults to SECONDS, see `5. Used ENUMs` for more options for _timeUnit_. \n\n"
            + "\nExample:\n"
            + "| Wait Until Element Does Not Exists | \\#some-node-id | \n"
            + "| Wait Until Element Does Not Exists | \\#some-node-id | 200 | MILLISECONDS | \n")
    @ArgumentNames({"locator", "timeout=10", "timeUnit=SECONDS"})
    public void waitUntilElementDoesNotExists(String locator, int timeout, String timeUnit) {
        try {
            RobotLog.info("Waiting until page does not contains element: \"" + locator + "\", timeout=\"" + timeout + "\", timeUnit= \"" + timeUnit + "\".");
            waitUntilDoesNotExists(locator, timeout, timeUnit);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new JavaFXLibraryNonFatalException("Something went wrong while waiting element \"" + locator + "\" to disappear.", e );
        }
    }

    @RobotKeyword("Waits until a node located by given locator becomes visible. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the element, see "
            + "`3. Locating JavaFX Nodes`. \n\n"
            + "``timeout`` is the maximum waiting time in seconds, defaults to 5. \n\n")
            @ArgumentNames({"locator", "timeout=5"})
    public void waitUntilNodeIsVisible(Object locator, int timeout) {
        try {
            RobotLog.info("Waiting for node \"" + locator + "\" to be visible, timeout=\"" + timeout + "\".");
            waitUntilVisible(locator, timeout);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new JavaFXLibraryNonFatalException("");
        }
    }

    @RobotKeyword("Waits until a node located by given locator becomes invisible. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the element, see "
            + "`3. Locating JavaFX Nodes`. \n\n"
            + "``timeout`` is the maximum waiting time in seconds, defaults to 5. \n\n")
    @ArgumentNames({"locator", "timeout=5"})
    public void waitUntilNodeIsNotVisible(Object locator, int timeout) {
        try {
            RobotLog.info("Waiting for node \"" + locator + "\" to be invisible, timeout=\"" + timeout + "\".");
            waitUntilInvisible(locator, timeout);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new JavaFXLibraryNonFatalException("");
        }
    }

    @RobotKeyword("Waits until a node located using given locator becomes enabled. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the element, see "
            + "`3. Locating JavaFX Nodes`. \n\n"
            + "``timeout`` is the maximum waiting time in seconds, defaults to 5. \n\n")
    @ArgumentNames({"locator", "timeout=5"})
    public void waitUntilNodeIsEnabled(Object locator, int timeout) {
        try {
            RobotLog.info("Waiting for node \"" + locator + "\" to be enabled, timeout=\"" + timeout + "\".");
            waitUntilEnabled(locator, timeout);
        } catch (IllegalArgumentException | NullPointerException e){
            throw new JavaFXLibraryNonFatalException("");
        }
    }

    @RobotKeyword("Waits until a node located using given locator becomes disabled. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the element, see "
            + "`3. Locating JavaFX Nodes`. \n\n"
            + "``timeout`` is the maximum waiting time in seconds, defaults to 5. \n\n")
    @ArgumentNames({"locator", "timeout=5"})
    public void waitUntilNodeIsNotEnabled(Object locator, int timeout) {
        try {
            RobotLog.info("Waiting for node \"" + locator + "\" to be disabled, timeout=\"" + timeout + "\".");
            waitUntilDisabled(locator, timeout);
        } catch (IllegalArgumentException | NullPointerException e){
            throw new JavaFXLibraryNonFatalException("");
        }
    }

    @RobotKeyword("Verifies that node is visible. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the Node, see "
            + "`3. Locating JavaFX Nodes`. \n\n")
    @ArgumentNames({ "locator" })
    public static void nodeShouldBeVisible(Object locator) {
        RobotLog.info("Checking that locator node is visible: \"" + locator + "\".");
        verifyThat(objectToNode(locator), isVisible() );
    }

    @RobotKeyword("Verifies that node is invisible. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the Node, see "
            + "`3. Locating JavaFX Nodes`. \n\n")
    @ArgumentNames({ "locator" })
    public static void nodeShouldNotBeVisible(Object locator) {
        RobotLog.info("Checking that locator node is not visible: \"" + locator + "\".");
        verifyThat(objectToNode(locator), isInvisible() );
    }

    @Deprecated
    @RobotKeyword("*DEPRECATED in version 0.6.0!* Use `Node Should Not Be Visible` instead.\n\n"
            + "Verifies that node is invisible. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the Node, see "
            + "`3. Locating JavaFX Nodes`. \n\n")
    @ArgumentNames({ "locator" })
    public static void nodeShouldBeInvisible(Object locator) {
        verifyThat(objectToNode(locator), isInvisible() );
    }

    @RobotKeyword("Verifies that node is focused. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the Node, see "
            + "`3. Locating JavaFX Nodes`. \n\n")
    @ArgumentNames({ "locator" })
    public static void nodeShouldBeFocused(Object locator) {
        RobotLog.info("Checking that locator node is focused: \"" + locator + "\".");
        verifyThat(objectToNode(locator), isFocused() );
    }

    @RobotKeyword("Verifies that node is not focused. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the Node, see "
            + "`3. Locating JavaFX Nodes`. \n\n")
    @ArgumentNames({ "locator" })
    public static void nodeShouldNotBeFocused(Object locator) {
        RobotLog.info("Checking that locator node is not focused: \"" + locator + "\".");
        verifyThat(objectToNode(locator), isNotFocused() );
    }

    @RobotKeyword("Verifies that node is enabled. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the Node, see "
            + "`3. Locating JavaFX Nodes`. \n\n")
    @ArgumentNames({ "locator" })
    public static void nodeShouldBeEnabled(Object locator) {
        RobotLog.info("Checking that locator node is enabled: \"" + locator + "\".");
        verifyThat(objectToNode(locator), isEnabled() );
    }

    @RobotKeyword("Verifies that node is disabled. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the Node, see "
            + "`3. Locating JavaFX Nodes`. \n\n")
    @ArgumentNames({ "locator" })
    public static void nodeShouldNotBeEnabled(Object locator) {
        RobotLog.info("Checking that locator node is not enabled: \"" + locator + "\".");
        verifyThat(objectToNode(locator), NodeMatchers.isDisabled() );
    }

    @Deprecated
    @RobotKeyword("*DEPRECATED in version 0.6.0!* Use `Node Should Not Be Enabled` instead."
            + "Verifies that node is disabled. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the Node, see "
            + "`3. Locating JavaFX Nodes`. \n\n")
    @ArgumentNames({ "locator" })
    public static void nodeShouldBeDisabled(Object locator) {
        verifyThat(objectToNode(locator), NodeMatchers.isDisabled() );
    }

    @RobotKeyword("Verifies that node is hoverable with mouse. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the Node, see "
            + "`3. Locating JavaFX Nodes`. \n\n")
    @ArgumentNames({ "locator" })
    public static void nodeShouldBeHoverable(Object locator) {
        try {
            RobotLog.info("Checking that locator node is hoverable: \"" + locator + "\".");
            verifyThat(objectToNode(locator), ExtendedNodeMatchers.isHoverable());
        } catch (AssertionError ae){
            Node node = getHoveredNode();
            RobotLog.info("Given locator node: \"" + locator + "\" was not hoverable! Instead, following " +
                    "node was found: \"" + node + "\".");
            throw ae;
        }
    }

    @RobotKeyword("Verifies that node is not hoverable with mouse. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the Node, see "
            + "`3. Locating JavaFX Nodes`. \n\n")
    @ArgumentNames({ "locator" })
    public static void nodeShouldNotBeHoverable(Object locator) {
        try {
            RobotLog.info("Checking that locator node is not hoverable: \"" + locator + "\".");
            verifyThat(objectToNode(locator), ExtendedNodeMatchers.isHoverable());
            throw new JavaFXLibraryNonFatalException("Expected \"" + locator + "\" to be not hoverable - failed!");
        } catch (AssertionError ae){
            // Was not hoverable, keyword should pass
        }
    }

    @RobotKeyword("Verifies that given node has text. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the Node, see "
            + "`3. Locating JavaFX Nodes`. \n\n"
            + "``text`` is the String to be searched for")
    @ArgumentNames({ "locator", "text" })
    public static void nodeShouldHaveText(Object locator, String text) {
        RobotLog.info("Checking that locator node \"" + locator + "\" has text \"" + text + "\".");
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
            + "`3. Locating JavaFX Nodes`. \n\n"
            + "``text`` is the String to be searched for")
    @ArgumentNames({ "locator", "text" })
    public static void nodeShouldNotHaveText(Object locator, String text) {
        RobotLog.info("Checking that locator node \"" + locator + "\" does not have text \"" + text + "\".");
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

    @RobotKeyword("Verifies that given window is visible.\n\n"
            + "``window`` is the _Object:Window_ that specifies which window should be visible, see `3.2 Using locators as keyword arguments`.")
    @ArgumentNames({ "window" })
    public static void windowShouldBeVisible(Object window) {
        RobotLog.info("Checking if window \"" + window + "\" is visible.");
        verifyThat((Window) window, WindowMatchers.isShowing());
    }

    @Deprecated
    @RobotKeyword("*DEPRECATED in version 0.6.0!* Please use `Window Should Be Visible` instead.\n\n" +"Verifies that given window is showing. \n\n"
            + "``window`` is the _Object:Window_ that specifies which window should be showing, see `3.2 Using locators as keyword arguments`.")
    @ArgumentNames({ "window" })
    public static void windowShouldBeShowing(Object window) {
        verifyThat((Window) window, WindowMatchers.isShowing());
    }

    @RobotKeyword("Verifies that given window is not visible.\n\n"
            + "``window`` is the _Object:Window_ that specifies which window should be not visible, see `3.2 Using locators as keyword arguments`.")
    @ArgumentNames({ "window" })
    public static void windowShouldNotBeVisible(Object window) {
        RobotLog.info("Checking if window \"" + window + "\" is not visible.");
        verifyThat((Window) window, WindowMatchers.isNotShowing());
    }

    @RobotKeyword("Verifies that given window is focused. \n\n"
            + "``window`` is the _Object:Window_ that specifies which window should be focused, see `3.2 Using locators as keyword arguments`.")
    @ArgumentNames({ "window" })
    public static void windowShouldBeFocused(Object window) {
        RobotLog.info("Checking if window \"" + window + "\" is focused.");
        verifyThat((Window) window, WindowMatchers.isFocused());
    }

    @RobotKeyword("Verifies that given window is not focused. \n\n"
            + "``window`` is the _Object:Window_ that specifies which window should be focused, see `3.2 Using locators as keyword arguments`.")
    @ArgumentNames({ "window" })
    public static void windowShouldNotBeFocused(Object window) {
        RobotLog.info("Checking if window \"" + window + "\" is not focused.");
        verifyThat((Window) window, WindowMatchers.isNotFocused());
    }

    @RobotKeyword("Checks if given two bounds are equal. \n\n"
            + "``firstBounds`` is an _Object:Bounds_ that specifies the first comparable Bounds\n\n"
            + "``secondBounds`` is an _Object:Bounds_ that specifies the second comparable Bounds, see `3.2 Using locators as keyword arguments`.")
    @ArgumentNames({ "firstBounds", "secondBounds" })
    public void boundsShouldBeEqual(Bounds firstBounds, Bounds secondBounds) {
        RobotLog.info("Checking if \"" + firstBounds + "\" equals with \"" + secondBounds + "\".");
        if (firstBounds == null || secondBounds == null )
            throw new JavaFXLibraryNonFatalException("One of the bounds is null. Check log for additional info.");
        assertEquals("Expected bounds to be equal:\n"
                + "  First bound:  " + firstBounds + "\n"
                + "  Second bound: " + secondBounds, firstBounds, secondBounds);
    }

    @RobotKeyword("Checks if given two bounds are not equal. \n\n"
            + "``firstBounds`` is an _Object:Bounds_ that specifies the first comparable Bounds\n\n"
            + "``secondBounds`` is an _Object:Bounds_ that specifies the second comparable Bounds, see `3.2 Using locators as keyword arguments`.")
    @ArgumentNames({ "firstBounds", "secondBounds" })
    public void boundsShouldNotBeEqual(Bounds firstBounds, Bounds secondBounds) {
        RobotLog.info("Checking if \"" + firstBounds + "\" are not equal with \"" + secondBounds + "\".");
        if (firstBounds == null || secondBounds == null )
            throw new JavaFXLibraryNonFatalException("One of the bounds is null. Check log for additional info.");
        assertNotEquals("Expected bounds to be not equal:\n"
                + "  First bound:  " + firstBounds + "\n"
                + "  Second bound: " + secondBounds, firstBounds, secondBounds);
    }

    @RobotKeyword("Fails if images are not similar enough\n\n"
            + "``image1`` is an _Object:Image_ for the first comparable image.\n\n"
            + "``image2`` is an _Object:Image_ for the second comparable image.\n\n"
            + "``percentage`` the percentage of pixels that should match, defaults to 100.\n\n"
            + "This keyword can be coupled with e.g. `Capture Image` -keyword.")
    @ArgumentNames({ "image1", "image2", "percentage=100" })
    public void imagesShouldMatch(Image image1, Image image2, double percentage) {
        RobotLog.info("Checking if " + percentage + "% of " + image1 + " matches with " + image2 + ".");

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

    @RobotKeyword("Fails if images are too similar\n\n"
            + "``image1`` is an _Object:Image_ for the first comparable image.\n\n"
            + "``image2`` is an _Object:Image_ for the second comparable image.\n\n"
            + "``percentage`` the percentage of pixels that should not match, defaults to 100.\n\n"
            + "This keyword can be coupled with e.g. `Capture Image` -keyword.")
    @ArgumentNames({ "image1", "image2", "percentage=100" })
    public void imagesShouldNotMatch(Image image1, Image image2, double percentage) {
        RobotLog.info("Checking if " + percentage + "% of " + image1 + " differs with " + image2 + ".");

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

    @RobotKeyword("Verifies that RadioButton is selected. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the RadioButton element, see "
            + "`3. Locating JavaFX Nodes`. \n\n")
    @ArgumentNames({ "locator" })
    public static void radioButtonShouldBeSelected(Object locator) {
        try {
            RobotLog.info("Checking that radio button is selected: \"" + locator + "\".");
            verifyThat((RadioButton) objectToNode(locator), ToggleMatchers.isSelected());
        } catch (ClassCastException cce) {
            throw new JavaFXLibraryNonFatalException("Unable to handle given locator as RadioButton!");
        }
    }

    @RobotKeyword("Verifies that RadioButton is not selected. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the RadioButton element, see "
            + "`3. Locating JavaFX Nodes`. \n\n")
    @ArgumentNames({ "locator" })
    public static void radioButtonShouldNotBeSelected(Object locator) {
        try {
            RobotLog.info("Checking that radio button is not selected: \"" + locator + "\".");
            verifyThat((RadioButton) objectToNode(locator), ToggleMatchers.isNotSelected());
        } catch (ClassCastException cce) {
            throw new JavaFXLibraryNonFatalException("Unable to handle given locator as RadioButton!");
        }
    }

    @RobotKeyword("Verifies that ToggleButton is selected. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the ToggleButton element, see "
            + "`3. Locating JavaFX Nodes`. \n\n")
    @ArgumentNames({ "locator" })
    public static void toggleButtonShouldBeSelected(Object locator) {
        try {
            RobotLog.info("Checking that toggle button is selected: \"" + locator + "\".");
            verifyThat((ToggleButton) objectToNode(locator), ToggleMatchers.isSelected());
        } catch (ClassCastException cce) {
            throw new JavaFXLibraryNonFatalException("Unable to handle given locator as ToggleButton!");
        }
    }

    @RobotKeyword("Verifies that ToggleButton is not selected. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the ToggleButton element, see "
            + " `3. Locating JavaFX Nodes`. \n\n")
    @ArgumentNames({ "locator" })
    public static void toggleButtonShouldNotBeSelected(Object locator) {
        try{
            RobotLog.info("Checking that toggle button is not selected: \"" + locator + "\".");
            verifyThat((ToggleButton) objectToNode(locator), ToggleMatchers.isNotSelected());
        } catch (ClassCastException cce) {
            throw new JavaFXLibraryNonFatalException("Unable to handle given locator as ToggleButton!");
        }
    }

    @RobotKeyword("Waits until given ProgressBar is finished or timeout expires. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the ToggleButton element, see "
            + " `3. Locating JavaFX Nodes`. \n\n"
            + "``timeout`` is an integer value for timeout in seconds, defaults to 20 seconds.")
    @ArgumentNames({ "locator", "timeout=20" })
    public static void waitUntilProgressBarIsFinished(Object locator, int timeout) {
        try {
            RobotLog.info("Waiting until progressbar is finished: \"" + locator + "\", timeout=\"" + timeout + "\".");
            ProgressBar pb = (ProgressBar) objectToNode(locator);
            waitForProgressBarToFinish(pb, timeout);
        } catch (ClassCastException cce) {
            throw new JavaFXLibraryNonFatalException("Unable to handle given locator as ProgressBar!");
        }
    }
}