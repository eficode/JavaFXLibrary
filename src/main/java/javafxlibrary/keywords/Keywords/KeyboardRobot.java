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

package javafxlibrary.keywords.Keywords;

import javafx.scene.input.KeyCode;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.RobotLog;
import javafxlibrary.utils.TestFxAdapter;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.Autowired;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;
import org.testfx.api.FxRobot;
import org.testfx.api.FxRobotInterface;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Arrays;
import static javafxlibrary.utils.HelperFunctions.*;

@RobotKeywords
public class KeyboardRobot extends TestFxAdapter {

    @Autowired
    ClickRobot clickRobot;

    private int sleepMillis = 0;

    // Below press- and push -keywords uses AWT robot for simulating 'real' keyboard events
    @RobotKeyword("Presses given keys, until explicitly released via keyword 'Release'. Once pressed, \n\n"
            + "``keys`` is the list of keys to be pressed, see a list of different KeyCodes in `5. Used ENUMs`. \n\n"
            + "\nExample: \n"
            + "| Press | CONTROL | SHIFT | G | \n")
    @ArgumentNames({ "*keys" })
    public FxRobotInterface press(String... keys) {
        try {
            RobotLog.info("Pressing keys: " + Arrays.asList(keys));
            return robot.press(getKeyCode(keys));
        } catch (Exception e) {
            if (e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to press keys: " + Arrays.asList(keys), e);
        }
    }

    @RobotKeyword("Releases given keys. \n\n"
            + "``keys`` is the list of keys to be released, see a list of different KeyCodes in `5. Used ENUMs`. \n\n"
            + "\nExample: \n"
            + "| Release | CONTROL | SHIFT | G | \n"
            + "Note: passing in an empty list will release all pressed keys.\n\n")
    @ArgumentNames({ "*keys" })
    public FxRobotInterface release(String... keys) {
        try {
            RobotLog.info("Releasing keys: " + Arrays.asList(keys));
            return robot.release(getKeyCode(keys));
        } catch (Exception e) {
            if (e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to release keys: " + Arrays.asList(keys), e);
        }
    }


    @RobotKeyword("Pushes a given key/key combination.\n\n"
            + "``keys`` is the list of keys to be pushed, see a list of different KeyCodes in `5. Used ENUMs`. \n\n"
            + "\nExample:\n"
            + "| Push | CONTROL | SHIFT | G | \n")
    @ArgumentNames({ "*keys" })
    public FxRobotInterface push(String... keys) {
        try {
            RobotLog.info("Pushing combination: " + Arrays.asList(keys));
            return robot.push(getKeyCode(keys));
        } catch (Exception e) {
            if (e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to push combination: " + Arrays.asList(keys), e);
        }
    }

    @RobotKeyword("Pushes a given key/key combination multiple times.\n\n"
            + "``times`` defines how many times to push\n"
            + "``keys`` is the key combination to push, see a list of different KeyCodes in `5. Used ENUMs`. \n\n"
            + "\nExample:\n"
            + "| Push Many Times | 2 | LEFT | \n"
            + "| Push Many Times | 5 | SHIFT | X |\n")
    @ArgumentNames({ "times", "*keys" })
    public void pushManyTimes(int times, String... keys) {
        RobotLog.info("Pushing combination: \"" + Arrays.asList(keys) + "\" for \"" + times + "\" times.");
        try {
            for (int i = 0; i < times; i++) {
                robot.push(getKeyCode(keys));
                sleepFor(50);
            }
        } catch (Exception e) {
            if (e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to push: " + Arrays.asList(keys), e);
        }
    }

    @RobotKeyword("Pushes given keys one at a time.\n\n"
            + "``keys`` is the list of keys to be pushed, see a list of different KeyCodes in `5. Used ENUMs`. \n\n"
            + "\nExample:\n"
            + "| Push In Order | H | e | l | l | o | \n"
            + "| Push In Order | BACK_SPACE | LEFT | BACK_SPACE | \n")
    @ArgumentNames({ "*keys" })
    public void pushInOrder(String... keys) {
        RobotLog.info("Pushing following keys: " + Arrays.asList(keys));
        try {
            for (String key : keys) {
                robot.push(KeyCode.valueOf(key));
            }
        } catch (Exception e) {
            if (e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to push keys: " + Arrays.toString(keys), e);
        }
    }

    @RobotKeyword("Erases the given number of characters from the active element.\n\n"
            + "``amount`` is the number of characters to erase\n"
            + "\nExample:\n"
            + "| Erase Text | 5 | \n")
    @ArgumentNames({ "amount" })
    public FxRobotInterface eraseText(int amount) {
        RobotLog.info("Erasing \"" + amount + "\" characters.");
        return robot.eraseText(amount);
    }

    @RobotKeyword("Closes the current window, same as ALT + F4 in Windows \n\n")
    public FxRobotInterface closeCurrentWindow() {
        try {
            if (isMac()) {
                RobotLog.info("Closing window via: META + W");
                return robot.push(KeyCode.META, KeyCode.W).sleep(100);
            } else if (robot instanceof FxRobot) {
                RobotLog.info("Closing window via: ALT + F4");
                return ((FxRobot) robot).closeCurrentWindow();
            }

            throw new JavaFXLibraryNonFatalException("No instance available for closing.");

        } catch (Exception e) {
            if(e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to Close current window.", e);
        }
    }

    // -----------------------------------------------------------------------------------------------
    // Write uses JavaFX events
    @RobotKeyword("Writes a given text characters one after the other.\n\n"
            + "``text`` is the text characters to write\n"
            + "\nExample: \n"
            + "| Write | Robot Framework | \n")
    @ArgumentNames({ "text" })
    public FxRobotInterface write(String text) {
        try {
            return robot.write(text, sleepMillis);
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Unable to write text: \"" + text + "\"", e);
        }
    }

    @RobotKeyword("Writes a given text to system clipboard and pastes the content to active element.\n\n"
            + "``text`` is the text characters to write\n"
            + "\nExample: \n"
            + "| Write Fast | Robot Framework | \n")
    @ArgumentNames({ "text" })
    public void writeFast(String text) {
        try {
            Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection testData = new StringSelection(text);
            c.setContents(testData, testData);

            if(isMac())
                robot.push(KeyCode.META, KeyCode.V).sleep(100);
            else
                robot.push(KeyCode.CONTROL, KeyCode.V).sleep(100);
        } catch (Exception e) {
            if(e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to write text using copy/paste method.", e);
        }
    }

    @RobotKeyword("Writes a given text characters one after the other to given locator.\n\n"
            + "``locator`` is either a _query_ or _Object:Bounds, Node, Point2D, PointQuery, Scene, Window_ for identifying the element, see "
            + "`3. Locating or specifying UI elements`. \n\n"
            + "``text`` is the text characters to write\n"
            + "\nExample: \n"
            + "| Write To | Robot Framework | \n")
    @ArgumentNames({ "locator", "text" })
    public FxRobotInterface writeTo(Object locator, String text) {
        RobotLog.info("Writing to " + locator);

        try {
            clickRobot.clickOn(locator);
            return robot.write(text, sleepMillis);
        } catch (Exception e) {
            if(e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to write to: " + locator, e);
        }
    }

    @RobotKeyword("Pushes CTRL/CMD + A key combination to select all.")
    public void selectAll() {
        if (isMac())
            robot.push(KeyCode.META, KeyCode.A);
        else
            robot.push(KeyCode.CONTROL, KeyCode.A);
    }

    @RobotKeyword("Sets the time waited between every character when typing\n\n" +
            "``milliseconds`` is the time waited between each character in milliseconds.")
    @ArgumentNames({ "milliseconds" })
    public void setWriteSpeed(int milliseconds) {
        this.sleepMillis = milliseconds;
    }

}