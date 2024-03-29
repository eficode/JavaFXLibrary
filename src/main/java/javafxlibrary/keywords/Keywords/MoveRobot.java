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

import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import javafxlibrary.utils.RobotLog;
import javafxlibrary.utils.TestFxAdapter;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;
import org.testfx.api.FxRobotInterface;
import org.testfx.robot.Motion;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;

import static javafxlibrary.utils.HelperFunctions.*;
import static org.testfx.util.WaitForAsyncUtils.asyncFx;

@RobotKeywords
public class MoveRobot extends TestFxAdapter {

    @RobotKeyword("Moves mouse over a node located using given locator.\n\n "
            + "``locator`` is either a _query_ or _Object:Bounds, Node, Point2D, PointQuery, Scene, Window_ for identifying the element, see "
            + "`3. Locating JavaFX Nodes`. \n\n"
            + "``motion`` defines the path for mouse to move to a target location. Default value is _DIRECT_. \n\n"
            + "\nExample: \n"
            + "| ${x} | Evaluate | ${400} + ${SCENE_MINX} | \n"
            + "| ${y} | Evaluate | ${150} + ${SCENE_MINY} | \n"
            + "| ${point} | Create Point | ${x} | ${y} | \n"
            + "| Move To | ${POINT} | VERTICAL_FIRST | | # moves mouse on top of given Point object by moving first vertically and then horizontally |")
    @ArgumentNames({"locator", "motion=DIRECT"})
    public void moveTo(Object locator, String motion) {
        checkObjectArgumentNotNull(locator);
        try {
            RobotLog.info("Moving to target \"" + locator + "\" using motion: \"" + getMotion(motion) + "\"");
            Object node;
            if (locator instanceof String) {
                node = asyncFx(() -> {
                    try {
                        return objectToNode(locator);
                    } catch (Exception e) {
                        RobotLog.info("Locator not found: " + e.getCause());
                        return null;
                    }
                }).get();
                if (node == null)
                    throw new JavaFXLibraryNonFatalException("Given locator \"" + locator + "\" was not found.");
            } else
                node = locator;
            if (isMac()) {
                // TODO: why asyncFx thread does not work in mac?
                Method method = MethodUtils.getMatchingAccessibleMethod(robot.getClass(), "moveTo", node.getClass(), Motion.class);
                method.invoke(robot, node, getMotion(motion));
            } else {
                boolean success = asyncFx(() -> {
                    try {
                        Method method = MethodUtils.getMatchingAccessibleMethod(robot.getClass(), "moveTo", node.getClass(), Motion.class);
                        method.invoke(robot, node, getMotion(motion));
                        return true;
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        RobotLog.trace("failed in asyncFx thread moveTo");
                        return false;
                    }
                }).get();
                if (!success)
                    throw new JavaFXLibraryNonFatalException("moveTo: Could not execute move to using locator \"" + locator + "\" " +
                            "and motion " + motion);
            }
        } catch (InterruptedException | ExecutionException iee) {
            throw new JavaFXLibraryNonFatalException("moveTo: Could not execute move to using locator \"" + locator + "\" " +
                    "and motion " + motion + " (asyncFx thread): " + iee.getCause());
        } catch (JavaFXLibraryNonFatalException e) {
            throw e;
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new JavaFXLibraryNonFatalException("moveTo: Could not execute move to using locator \"" + locator + "\" " +
                    "and motion " + motion + ": " + e.getCause());
        }
    }

    @RobotKeyword("Moves mouse directly from current location to new location specified by _x_ and _y_ offsets\n\n"
            + "``x`` is an integer value for horizontal axis x-offset. \n\n"
            + "``y`` is an integer value for vertical axis y-offset. \n\n"
            + "Optional argument ``motion`` defines the path for mouse to move to given coordinates. Default value is _DIRECT_. \n\n"
            + "\nExample: \n"
            + "| Move By | 75 | 75 | \n")
    @ArgumentNames({"x", "y", "motion=DIRECT"})
    public FxRobotInterface moveBy(int x, int y, String motion) {
        try {
            RobotLog.info("Moving by [" + x + ", " + y + "] using motion: \"" + motion + "\"");
            return robot.moveBy(x, y, HelperFunctions.getMotion(motion));
        } catch (Exception e) {
            if (e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to move by using coordinates: " + x + ", " + y, e);
        }
    }

    @RobotKeyword("Moves mouse to given coordinates.\n\n"
            + "``x`` is an integer value for horizontal axis x-coordinate. \n\n"
            + "``y`` is an integer value for vertical axis y-coordinate. \n\n"
            + "Optional argument ``motion`` defines the path for mouse to move to given coordinates. Default value is _DIRECT_. \n\n"
            + "\nExample: \n"
            + "| ${x} | Evaluate | ${SCENE_MINX} + ${200} | \n "
            + "| ${y} | Evaluate | ${SCENE_MINY} + ${200} | \n "
            + "| Move To Coordinates | ${x} | ${y} | HORIZONTAL_FIRST | \n"
            + "| Label Text Should Be | \\#locationLabel | 200 | 200 | \n")
    @ArgumentNames({"x", "y", "motion=DIRECT"})
    public FxRobotInterface moveToCoordinates(int x, int y, String motion) {
        try {
            RobotLog.info("Moving to coordinates: [" + x + ", " + y + "] using motion: \"" + motion + "\"");
            return robot.moveTo(x, y, HelperFunctions.getMotion(motion));
        } catch (Exception e) {
            if (e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to move to coordinates: [" + x + ", " + y +
                    "] using motion: \"" + motion + "\"", e);
        }
    }
}