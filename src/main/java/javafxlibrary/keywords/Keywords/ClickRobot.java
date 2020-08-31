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

import javafx.scene.input.MouseButton;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
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
import java.util.Arrays;

import static javafxlibrary.utils.HelperFunctions.*;

@RobotKeywords
public class ClickRobot extends TestFxAdapter {

    @RobotKeyword("Clicks an element specified by given locator.\n\n"
            + "``locator`` is either a _query_ or _Object:Bounds, Node, Point2D, PointQuery, Scene, Window_ for identifying the element, see "
            + "`3. Locating JavaFX Nodes`. \n\n"
            + "``motion`` defines the path for mouse to move to a target location. Default value is _DIRECT_. Especially with submenus, desired motion "
            + "is usually HORIZONTAL_FIRST.\n\n"
            + "\nExample:\n"
            + "| Click On | ${node} | \n"
            + "| Click On | ${point} | \n"
            + "| Click On | \\#node-id | \n"
            + "| Click On | .css-name | Motion=VERTICAL_FIRST | \n")
    @ArgumentNames({ "locator", "motion=DIRECT" })
    public FxRobotInterface clickOn(Object locator, String motion) {
        Object target = checkClickTarget(locator);
        RobotLog.info("Clicking on target \"" + target + "\", motion=\"" + getMotion(motion) + "\"");
        Method method = MethodUtils.getMatchingAccessibleMethod(robot.getClass(), "clickOn",
                target.getClass(), Motion.class, MouseButton.class);

        try {
            return (FxRobotInterface) method.invoke(robot, target, getMotion(motion), new MouseButton[]{MouseButton.PRIMARY});
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new JavaFXLibraryNonFatalException("Could not execute click on using locator \"" + locator + "\" " +
                    "and motion " + motion + ": " + e.getCause().getMessage(), e);
        }
    }

    @RobotKeyword("Right clicks an element specified by given locator.\n\n"
            + "``locator`` is either a _query_ or _Object:Bounds, Node, Point2D, PointQuery, Scene, Window_ for identifying the element, see "
            + "`3. Locating JavaFX Nodes`. \n\n"
            + "``motion`` defines the path for mouse to move to a target location. Default value is _DIRECT_. Especially with submenus, desired motion "
            + "is usually HORIZONTAL_FIRST.\n\n")
    @ArgumentNames({ "locator", "motion=DIRECT" })
    public FxRobotInterface rightClickOn(Object locator, String motion) {
        Object target = checkClickTarget(locator);
        RobotLog.info("Right clicking on target \"" + target + "\", motion=\"" + getMotion(motion) + "\"");
        Method method = MethodUtils.getMatchingAccessibleMethod(robot.getClass(), "rightClickOn", target.getClass(), Motion.class);
        try {
            return (FxRobotInterface) method.invoke(robot, target, getMotion(motion));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new JavaFXLibraryNonFatalException("Could not execute right click on using locator \"" + locator + "\" " +
                    "and motion " + motion + ": " + e.getCause().getMessage(), e);
        }
    }

    @RobotKeyword("Double clicks an element specified by given locator.\n\n"
            + "``locator`` is either a _query_ or _Object:Bounds, Node, Point2D, PointQuery, Scene, Window_ for identifying the element, see "
            + "`3. Locating JavaFX Nodes`. \n\n"
            + "``motion`` defines the path for mouse to move to a target location. Default value is _DIRECT_.")
    @ArgumentNames({ "locator", "motion=DIRECT" })
    public FxRobotInterface doubleClickOn(Object locator, String motion) {
        Object target = checkClickTarget(locator);
        RobotLog.info("Double clicking on target \"" + target + "\", motion=\"" + getMotion(motion) + "\"");
        Method method = MethodUtils.getMatchingAccessibleMethod(robot.getClass(), "doubleClickOn",
                target.getClass(), Motion.class, MouseButton.class);

        try {
            return (FxRobotInterface) method.invoke(robot, target, getMotion(motion), new MouseButton[]{MouseButton.PRIMARY});
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new JavaFXLibraryNonFatalException("Could not execute double click on using locator \"" + locator + "\" " +
                    "and motion " + motion + ": " + e.getCause().getMessage(), e);
        }
    }

    @RobotKeyword("Clicks whatever is under the mouse pointer. \n\n"
            + "``buttons`` is a list of mouse buttons to click. See `5. Used ENUMs` for different mouse buttons available. ")
    @ArgumentNames({ "*buttons" })
    public FxRobotInterface ClickOnMouseButton(String... buttons) {
        try {
            RobotLog.info("Clicking mouse buttons \"" + Arrays.toString(buttons) + "\"");
            return robot.clickOn(getMouseButtons(buttons));
        } catch (Exception e) {
            if (e instanceof JavaFXLibraryNonFatalException) {
                throw e;
            }
            throw new JavaFXLibraryNonFatalException("Unable to click mouse button.", e);
        }
    }

    @RobotKeyword("Double clicks whatever is under the mouse pointer. \n\n"
            + "``buttons`` is a list of mouse buttons to click. See `5. Used ENUMs` for different mouse buttons available. ")
    @ArgumentNames({ "*buttons" })
    public FxRobotInterface doubleClickOnMouseButton(String... buttons) {
        try {
            RobotLog.info("Double clicking mouse buttons \"" + Arrays.toString(buttons) + "\"");
            return robot.doubleClickOn(getMouseButtons(buttons));
        } catch (Exception e) {
            if (e instanceof JavaFXLibraryNonFatalException) {
                throw e;
            }
            throw new JavaFXLibraryNonFatalException("Unable to double click mouse button.", e);
        }
    }

    @RobotKeyword("Clicks right mouse button on whatever is under the mouse pointer")
    public FxRobotInterface rightClickOnMouseButton() {
        try {
            return robot.rightClickOn();
        } catch (Exception e) {
            if (e instanceof JavaFXLibraryNonFatalException) {
                throw e;
            }
            throw new JavaFXLibraryNonFatalException("Unable to right click mouse button", e);
        }
    }

    @RobotKeyword("Moves mouse directly to the given coordinates and clicks the primary mouse button\n\n"
            + "``x`` and ``y`` defines the coordinates as integer values. \n\n"
            + "Optional argument ``motion`` defines how mouse pointer is moved to target. Defaults to _DIRECT_.")
    @ArgumentNames({ "x", "y", "motion=DIRECT" })
    public FxRobotInterface clickOnCoordinates(int x, int y, String motion) {
        RobotLog.info("Clicking on coordinates x=\"" + x + "\"" + ", y=\"" + y + "\"" + " and motion=\"" + motion + "\"");
        checkClickLocation(x, y);

        try {
            return robot.clickOn(x, y, getMotion(motion), MouseButton.PRIMARY);
        } catch (Exception e) {
            if (e instanceof JavaFXLibraryNonFatalException) {
                throw e;
            }
            throw new JavaFXLibraryNonFatalException("Unable to click on coordinates: " + x + ", " + y, e);
        }
    }

    @RobotKeyword("Moves mouse directly to the given coordinates and double clicks the primary mouse button\n\n"
            + "``x`` and ``y`` defines the coordinates as integer values. \n\n"
            + "Optional argument ``motion`` defines how mouse pointer is moved to target. Defaults to _DIRECT_.")
    @ArgumentNames({ "x", "y", "motion=DIRECT" })
    public FxRobotInterface doubleClickOnCoordinates(int x, int y, String motion) {
        checkClickLocation(x, y);
        try {
            RobotLog.info("Double clicking on coordinates x=\"" + x + "\"" + ", y=\"" + y + "\"" + " and motion=\"" + motion + "\"");
            return robot.doubleClickOn(x, y, getMotion(motion), MouseButton.PRIMARY);
        } catch (Exception e) {
            if (e instanceof JavaFXLibraryNonFatalException) {
                throw e;
            }
            throw new JavaFXLibraryNonFatalException("Unable to double click on coordinates: " + x + " " + y, e);
        }
    }

    @RobotKeyword("Moves mouse directly to the given coordinates and right clicks the primary mouse button\n\n"
            + "``x`` and ``y`` defines the coordinates as integer values. \n\n"
            + "Optional argument ``motion`` defines how mouse pointer is moved to target. Defaults to _DIRECT_.")
    @ArgumentNames({ "x", "y", "motion=DIRECT" })
    public FxRobotInterface rightClickOnCoordinates(int x, int y, String motion) {
        checkClickLocation(x, y);
        try {
            RobotLog.info("Right clicking on coordinates x=\"" + x + "\"" + ", y=\"" + y + "\"" + " and motion=\"" + motion + "\"");
            return robot.rightClickOn(x, y, getMotion(motion));
        } catch (Exception e) {
            if (e instanceof JavaFXLibraryNonFatalException) {
                throw e;
            }
            throw new JavaFXLibraryNonFatalException("Unable to right click on coordinates: " + x + " " + y, e);
        }
    }
}
