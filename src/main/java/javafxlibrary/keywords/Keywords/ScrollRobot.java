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
import javafxlibrary.utils.HelperFunctions;
import javafxlibrary.utils.RobotLog;
import javafxlibrary.utils.TestFxAdapter;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;

@RobotKeywords
public class ScrollRobot extends TestFxAdapter {

    @RobotKeyword("Scrolls vertically by amount (in terms of ticks of a mouse wheel) in given direction.\n\n"
            + "``amount`` is the number of scroll ticks, defaults to 1. \n\n"
            + "``direction`` specifies whether to scroll UP or DOWN. \n\n"
            + "\nExample:\n"
            + "| Move To | ${some node} | \n"
            + "| Scroll Vertically | DOWN | 25 | \n")
    @ArgumentNames({ "direction", "amount=1" })
    public void scrollVertically(String direction, int amount) {
        try {
            RobotLog.info("Scrolling \"" + direction + "\" by \"" + Integer.toString(amount) + "\" ticks.");
            robot.scroll(amount, HelperFunctions.getVerticalDirection(direction));
        } catch (Exception e) {
            if(e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to scroll vertically to direction: \"" + direction + "\"", e);
        }
    }

    @RobotKeywordOverload
    public void scrollVertically(String direction) {
        scrollVertically(direction, 1);
    }

    /*
     *    Current version of TestFX uses java.awt.Robots mouseWheel-method for scrolling, which only supports
     *    vertical scrolling. This solution uses SHIFT + MWHEEL combination for horizontal scrolling. Note that this
     *    combination does not work out of the box on Linux desktops.
     */
    @RobotKeyword("Scrolls horizontally by amount (in terms of ticks of a mouse wheel) in given direction.\n\n"
            + "``amount`` is the number of scroll ticks, defaults to 1. \n\n"
            + "``direction`` specifies whether to scroll RIGHT or LEFT. \n\n"
            + "\nExample:\n"
            + "| Move To | ${some node} | \n"
            + "| Scroll Horizontally | RIGHT | \n")
    @ArgumentNames({ "direction", "amount=1" })
    public void scrollHorizontally(String direction, int amount) {

        try {
            RobotLog.info("Scrolling \"" + direction + "\" by \"" + Integer.toString(amount) + "\" ticks.");
            robot.press(KeyCode.SHIFT);
            robot.scroll(amount, HelperFunctions.getHorizontalDirection(direction));
            robot.release(KeyCode.SHIFT);
        } catch (Exception e) {
            if(e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to scroll horizontally to direction: \"" + direction + "\"", e);
        }
    }

    @RobotKeywordOverload
    public void scrollHorizontally(String direction) {
        scrollHorizontally(direction, 1);
    }

}