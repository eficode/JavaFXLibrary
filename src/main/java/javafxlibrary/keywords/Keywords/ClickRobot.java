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

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.stage.Window;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.TestFxAdapter;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;
import org.testfx.api.FxRobotInterface;
import org.testfx.service.query.PointQuery;
import java.util.Arrays;

import static javafxlibrary.utils.HelperFunctions.*;

@RobotKeywords
public class ClickRobot extends TestFxAdapter {

    @RobotKeyword("Clicks an element specified by given locator.\n\n"
            + "``locator`` is either a _query_ or _Object:Bounds, Node, Point2D, PointQuery, Scene, Window_ for identifying the element, see "
            + "`3. Locating or specifying UI elements`. \n\n"
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

        try {

            if (target instanceof Window) {
                robotLog("INFO", "Clicking on window \"" + target.toString() + "\", motion=\"" + getMotion(motion) + "\"");
                return robot.clickOn((Window) target, getMotion(motion), MouseButton.PRIMARY);
            }
            else if (target instanceof Scene) {
                robotLog("INFO", "Clicking on scene \"" + target.toString() + "\", motion=\"" + getMotion(motion) + "\"");
                return robot.clickOn((Scene) target, getMotion(motion), MouseButton.PRIMARY);
            }
            else if (target instanceof Bounds) {
                robotLog("INFO", "Clicking on bounds \"" + target.toString() + "\", motion=\"" + getMotion(motion) + "\"");
                return robot.clickOn((Bounds) target, getMotion(motion), MouseButton.PRIMARY);
            }
            else if (target instanceof Point2D) {
                robotLog("INFO", "Clicking on point \"" + target.toString() + "\", motion=\"" + getMotion(motion) + "\"");
                return robot.clickOn((Point2D) target, getMotion(motion), MouseButton.PRIMARY);
            }
            else if (target instanceof Node) {
                robotLog("INFO", "Clicking on node \"" + target.toString() + "\", motion=\"" + getMotion(motion) + "\"");
                return robot.clickOn((Node) target, getMotion(motion), MouseButton.PRIMARY);
            }
            else if (target instanceof PointQuery) {
                robotLog("INFO", "Clicking on point query \"" + target.toString() + "\", motion=\"" + getMotion(motion) + "\"");
                return robot.clickOn((PointQuery) target, getMotion(motion), MouseButton.PRIMARY);
            }

            throw new JavaFXLibraryNonFatalException("Unsupported locator type: " + target.toString());

        } catch (Exception e) {
            if ( e instanceof JavaFXLibraryNonFatalException ) {
                throw e;
            }
            throw new JavaFXLibraryNonFatalException("Unable to click locator: " + target.toString(), e);
        }
    }

    @RobotKeywordOverload
    public FxRobotInterface clickOn(Object locator ) {
        return clickOn(locator, "DIRECT");
    }

    @RobotKeyword("Right clicks an element specified by given locator.\n\n"
            + "``locator`` is either a _query_ or _Object:Bounds, Node, Point2D, PointQuery, Scene, Window_ for identifying the element, see "
            + "`3. Locating or specifying UI elements`. \n\n"
            + "``motion`` defines the path for mouse to move to a target location. Default value is _DIRECT_. Especially with submenus, desired motion "
            + "is usually HORIZONTAL_FIRST.\n\n")
    @ArgumentNames({ "locator", "motion=DIRECT" })
    public FxRobotInterface rightClickOn(Object locator,String motion) {

        Object target = checkClickTarget(locator);

        try {
            if (target instanceof Window) {
                robotLog("INFO", "Right clicking on window \"" + target.toString() + "\", motion=\"" + getMotion(motion) + "\"");
                return robot.rightClickOn((Window) target, getMotion(motion));
            }
            else if (target instanceof Scene) {
                robotLog("INFO", "Right clicking on scene \"" + target.toString() + "\", motion=\"" + getMotion(motion) + "\"");
                return robot.rightClickOn((Scene) target, getMotion(motion));
            }
            else if (target instanceof Bounds) {
                robotLog("INFO", "Right clicking on bounds \"" + target.toString() + "\", motion=\"" + getMotion(motion) + "\"");
                return robot.rightClickOn((Bounds) target, getMotion(motion));
            }
            else if (target instanceof Point2D) {
                robotLog("INFO", "Right clicking on point \"" + target.toString() + "\", motion=\"" + getMotion(motion) + "\"");
                return robot.rightClickOn((Point2D) target, getMotion(motion));
            }
            else if (target instanceof Node) {
                robotLog("INFO", "Right clicking on node \"" + target.toString() + "\", motion=\"" + getMotion(motion) + "\"");
                return robot.rightClickOn((Node) target, getMotion(motion));
            }
            else if (target instanceof PointQuery) {
                robotLog("INFO", "Right clicking on point query \"" + target.toString() + "\", motion=\"" + getMotion(motion) + "\"");
                return robot.rightClickOn((PointQuery) target, getMotion(motion));
            }

            throw new JavaFXLibraryNonFatalException("Unsupported locator type: " + target.toString());

        } catch (Exception e) {
            if ( e instanceof JavaFXLibraryNonFatalException ) {
                throw e;
            }
            throw new JavaFXLibraryNonFatalException("Unable to right click locator: " + target.toString(), e);
        }
    }

    @RobotKeywordOverload
    public FxRobotInterface rightClickOn(Object locator) {
        return rightClickOn(locator, "DIRECT");
    }

    @RobotKeyword("Double clicks an element specified by given locator.\n\n"
            + "``locator`` is either a _query_ or _Object:Bounds, Node, Point2D, PointQuery, Scene, Window_ for identifying the element, see "
            + "`3. Locating or specifying UI elements`. \n\n"
            + "``motion`` defines the path for mouse to move to a target location. Default value is _DIRECT_.")
    @ArgumentNames({ "locator", "motion=DIRECT" })
    public FxRobotInterface doubleClickOn(Object locator, String motion) {

        Object target = checkClickTarget(locator);

        try {
            if (target instanceof Window) {
                robotLog("INFO", "Double clicking on window \"" + target.toString() + "\", motion=\"" + getMotion(motion) + "\"");
                return robot.doubleClickOn((Window) target, getMotion(motion), MouseButton.PRIMARY);
            }
            else if (target instanceof Scene) {
                robotLog("INFO", "Double clicking on scene \"" + target.toString() + "\", motion=\"" + getMotion(motion) + "\"");
                return robot.doubleClickOn((Scene) target, getMotion(motion), MouseButton.PRIMARY);
            }
            else if (target instanceof Bounds){
                robotLog("INFO", "Double clicking on bounds \"" + target.toString() + "\", motion=\"" + getMotion(motion) + "\"");
                return robot.doubleClickOn((Bounds) target, getMotion(motion), MouseButton.PRIMARY);
            }
            else if (target instanceof Point2D) {
                robotLog("INFO", "Double clicking on point \"" + target.toString() + "\", motion=\"" + getMotion(motion) + "\"");
                return robot.doubleClickOn((Point2D) target, getMotion(motion), MouseButton.PRIMARY);
            }
            else if (target instanceof Node) {
                robotLog("INFO", "Double clicking on node \"" + target.toString() + "\", motion=\"" + getMotion(motion) + "\"");
                return robot.doubleClickOn((Node) target, getMotion(motion), MouseButton.PRIMARY);
            }
            else if (target instanceof PointQuery){
                robotLog("INFO", "Double clicking on point query \"" + target.toString() + "\", motion=\"" + getMotion(motion) + "\"");
                return robot.doubleClickOn((PointQuery) target, getMotion(motion), MouseButton.PRIMARY);
            }

            throw new JavaFXLibraryNonFatalException("Unsupported locator type: " + target.toString());

        } catch (Exception e) {
            if ( e instanceof JavaFXLibraryNonFatalException ) {
                throw e;
            }
            throw new JavaFXLibraryNonFatalException("Unable to double click locator: " + target.toString(), e);
        }
    }

    @RobotKeywordOverload
    public FxRobotInterface doubleClickOn(Object locator) {
        return doubleClickOn(locator,"DIRECT");
    }

    @RobotKeyword("Clicks whatever is under the mouse pointer. \n\n"
            + "``buttons`` is a list of mouse buttons to click. See `5. Used ENUMs` for different mouse buttons available. ")
    @ArgumentNames({ "*buttons" })
    public FxRobotInterface ClickOnMouseButton(String... buttons) {
        try {
            robotLog("INFO", "Clicking mouse buttons \"" + Arrays.toString(buttons) + "\"");
            return robot.clickOn(getMouseButtons(buttons));
        } catch (Exception e) {
            if ( e instanceof JavaFXLibraryNonFatalException ) {
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
            robotLog("INFO", "Double clicking mouse buttons \"" + Arrays.toString(buttons) + "\"");
            return robot.doubleClickOn(getMouseButtons(buttons));
        } catch (Exception e) {
            if ( e instanceof JavaFXLibraryNonFatalException ) {
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
            if ( e instanceof JavaFXLibraryNonFatalException ) {
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
        robotLog("INFO", "Clicking on coordinates x=\"" + Integer.toString(x) + "\""
                                                                            + ", y=\"" + Integer.toString(y) + "\""
                                                                            + " and motion=\"" + motion + "\"");
        checkClickLocation(x, y);
        try {
            return robot.clickOn((double) x, (double) y, getMotion(motion), MouseButton.PRIMARY);
        } catch (Exception e) {
            if ( e instanceof JavaFXLibraryNonFatalException ) {
                throw e;
            }
            throw new JavaFXLibraryNonFatalException("Unable to click on coordinates: " +
                    Integer.toString(x) + " " + Integer.toString(y), e);
        }
    }

    @RobotKeywordOverload
    @ArgumentNames({ "x", "y" })
    public FxRobotInterface clickOnCoordinates(int x, int y) {
        return clickOnCoordinates(x, y, "DIRECT");
    }

    @RobotKeyword("Moves mouse directly to the given coordinates and double clicks the primary mouse button\n\n"
            + "``x`` and ``y`` defines the coordinates as integer values. \n\n"
            + "Optional argument ``motion`` defines how mouse pointer is moved to target. Defaults to _DIRECT_.")
    @ArgumentNames({ "x", "y", "motion=DIRECT" })
    public FxRobotInterface doubleClickOnCoordinates(int x, int y, String motion) {
        checkClickLocation(x, y);
        try {
            robotLog("INFO", "Double clicking on coordinates x=\"" + Integer.toString(x) + "\""
                                                                                + ", y=\"" + Integer.toString(y) + "\""
                                                                                + " and motion=\"" + motion + "\"");
            return robot.doubleClickOn((double) x, (double) y, getMotion(motion), MouseButton.PRIMARY);
        } catch (Exception e) {
            if ( e instanceof JavaFXLibraryNonFatalException ) {
                throw e;
            }
            throw new JavaFXLibraryNonFatalException("Unable to double click on coordinates: " +
                    Integer.toString(x) + " " + Integer.toString(y), e);
        }
    }

    @RobotKeywordOverload
    @ArgumentNames({ "x", "y" })
    public FxRobotInterface doubleClickOnCoordinates(int x, int y) {
        return doubleClickOnCoordinates(x, y, "DIRECT");
    }

    @RobotKeyword("Moves mouse directly to the given coordinates and right clicks the primary mouse button\n\n"
            + "``x`` and ``y`` defines the coordinates as integer values. \n\n"
            + "Optional argument ``motion`` defines how mouse pointer is moved to target. Defaults to _DIRECT_.")
    @ArgumentNames({ "x", "y", "motion=DIRECT" })
    public FxRobotInterface rightClickOnCoordinates(int x, int y, String motion) {
        checkClickLocation(x, y);
        try {
            robotLog("INFO", "Right clicking on coordinates x=\"" + Integer.toString(x) + "\""
                                                                                + ", y=\"" + Integer.toString(y) + "\""
                                                                                + " and motion=\"" + motion + "\"");
            return robot.rightClickOn((double) x, (double) y, getMotion(motion));
        } catch (Exception e) {
            if ( e instanceof JavaFXLibraryNonFatalException ) {
                throw e;
            }
            throw new JavaFXLibraryNonFatalException("Unable to right click on coordinates: " +
                    Integer.toString(x) + " " + Integer.toString(y), e);
        }
    }

    @RobotKeywordOverload
    @ArgumentNames({ "x", "y" })
    public FxRobotInterface rightClickOnCoordinates(int x, int y) {
        return rightClickOnCoordinates(x, y, "DIRECT");
    }

}
