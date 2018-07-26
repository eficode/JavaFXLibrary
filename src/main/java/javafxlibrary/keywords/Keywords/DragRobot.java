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
import javafxlibrary.utils.HelperFunctions;
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
public class DragRobot extends TestFxAdapter {

    @RobotKeyword("Moves mouse on top of the element located using given _locator_ and presses the given mouse _button_.\n\n "
            + "``locator`` is either a _query_ or _Object:Bounds, Node, Point2D, PointQuery, Scene, Window_ for identifying the element, see "
            + "`3. Locating or specifying UI elements`. \n\n"
            + "Optional parameter ``button`` is the mouse button to be used, defaults to PRIMARY. See `5. Used ENUMs` for different MouseButtons\n\n"
            + "\nExample:\n"
            + "| ${node}= | Find | \\#some-node-id | \n"
            + "| Drag From | ${node} | SECONDARY | \n")
    @ArgumentNames({ "locator", "button=PRIMARY" })
    public FxRobotInterface dragFrom(Object locator, String button) {

        Object target = checkClickTarget(locator);

        try {
            if (target instanceof Window) {
                HelperFunctions.robotLog("INFO", "Drags from window \"" + target.toString() + "\""
                        + " with  button=\"" + button + "\"");
                return robot.drag((Window) target, MouseButton.valueOf(button));
            }
            else if (target instanceof Scene) {
                HelperFunctions.robotLog("INFO", "Drags from scene \"" + target.toString() + "\""
                        + " with  button=\"" + button + "\"");
                return robot.drag((Scene) target, MouseButton.valueOf(button));
            }
            else if (target instanceof Bounds){
                HelperFunctions.robotLog("INFO", "Drags from bounds \"" + target.toString() + "\""
                        + " with  button=\"" + button + "\"");
            return robot.drag((Bounds) target, MouseButton.valueOf(button));
            }
            else if (target instanceof Point2D){
                HelperFunctions.robotLog("INFO", "Drags from point \"" + target.toString() + "\""
                        + " with  button=\"" + button + "\"");
            return robot.drag((Point2D) target, MouseButton.valueOf(button));
            }
            else if (target instanceof Node){
                HelperFunctions.robotLog("INFO", "Drags from node \"" + target.toString() + "\""
                        + " with  button=\"" + button + "\"");
            return robot.drag((Node) target, MouseButton.valueOf(button));
            }
            else if (target instanceof String){
                HelperFunctions.robotLog("INFO", "Drags from query \"" + target.toString() + "\""
                        + " with  button=\"" + button + "\"");
            return robot.drag((String) target, MouseButton.valueOf(button));
            }
            else if (target instanceof PointQuery) {
                HelperFunctions.robotLog("INFO", "Drags from point query \"" + target.toString() + "\""
                        + " with  button=\"" + button + "\"");
                return robot.drag((PointQuery) target, MouseButton.valueOf(button));
            }

            throw new JavaFXLibraryNonFatalException("Unsupported locator type: " + target.toString());

        } catch (Exception e) {
            if ( e instanceof JavaFXLibraryNonFatalException ) {
                throw e;
            }
            throw new JavaFXLibraryNonFatalException("Unable to drag from: " + target.toString(), e);
        }
    }

    @RobotKeywordOverload
    public FxRobotInterface dragFrom(Object locator) {
        return dragFrom(locator, "PRIMARY");
    }

    @RobotKeyword("Moves mouse on top of the element located using given _locator_ and and releases the mouse button.\n\n "
            + "``locator`` is either a _query_ or _Object:Bounds, Node, Point2D, PointQuery, Scene, Window_ for identifying the element, see "
            + "`3. Locating or specifying UI elements`. \n\n"
            + "\nExample:\n"
            + "| Drop To | \\#some-node-id | \n")
    @ArgumentNames({ "locator" })
    public FxRobotInterface dropTo(Object locator) {

        Object target = checkClickTarget(locator);

        try {
            if (target instanceof Window) {
                HelperFunctions.robotLog("INFO", "Drops to window \"" + target.toString() + "\"");
                return robot.dropTo((Window) target);
            }
            else if (target instanceof Scene) {
                HelperFunctions.robotLog("INFO", "Drops to scene \"" + target.toString() + "\"");
                return robot.dropTo((Scene) target);
            }
            else if (target instanceof Bounds) {
                HelperFunctions.robotLog("INFO", "Drops to bounds \"" + target.toString() + "\"");
                return robot.dropTo((Bounds) target);
            }
            else if (target instanceof Point2D) {
                HelperFunctions.robotLog("INFO", "Drops to point \"" + target.toString() + "\"");
                return robot.dropTo((Point2D) target);
            }
            else if (target instanceof Node) {
                HelperFunctions.robotLog("INFO", "Drops to node \"" + target.toString() + "\"");
                return robot.dropTo((Node) target);
            }
            else if (target instanceof String){
                HelperFunctions.robotLog("INFO", "Drops to query \"" + target.toString() + "\"");
                return robot.dropTo((String) target);
            }
            else if (target instanceof PointQuery) {
                HelperFunctions.robotLog("INFO", "Drops to point query \"" + target.toString() + "\"");
                return robot.dropTo((PointQuery) target);
            }

            throw new JavaFXLibraryNonFatalException("Unsupported locator type: " + target.toString());

        } catch (Exception e) {
            if ( e instanceof JavaFXLibraryNonFatalException ) {
                throw e;
            }
            throw new JavaFXLibraryNonFatalException("Unable to drop to locator: " + target.toString(), e);
        }
    }

    @RobotKeyword("Presses the given mouse button(s) on whatever is under the mouse's current location. \n\n"
            + "Optional parameter ``buttons`` is a list of mouse buttons to be used, defaults to PRIMARY. See `5. Used ENUMs` for different MouseButtons\n\n")
    @ArgumentNames({ "*buttons" })
    public FxRobotInterface drag(String... buttons) {
        try {
            HelperFunctions.robotLog("INFO", "Dragging mouse buttons \"" + Arrays.toString(buttons) + "\"");
            return robot.drag(HelperFunctions.getMouseButtons(buttons));
        } catch (Exception e) {
            if ( e instanceof JavaFXLibraryNonFatalException ) {
                throw e;
            }
            throw new JavaFXLibraryNonFatalException("Unable to drag using " + Arrays.toString(buttons), e);
        }
    }

    @RobotKeyword("Releases the mouse at current position. \n")
    public FxRobotInterface drop() {
        try {
            return robot.drop();
        } catch (Exception e) {
            if ( e instanceof JavaFXLibraryNonFatalException ) {
                throw e;
            }
            throw new JavaFXLibraryNonFatalException("Drop failed: ", e);
        }
    }

    @RobotKeyword("Moves the mouse horizontally by _x_ and vertically by _y_ before releasing the mouse.\n\n"
            + "Integer argument ``x`` is the amount how much to move the mouse horizontally\n"
            + "Integer argument ``y`` is the amount how much to move the mouse vertically.\n"
            + "\nExample:\n"
            + "| Drag From | \\#node-id .css-name | \n"
            + "| Drop By | -300 | 0 | \n")
    @ArgumentNames({ "x", "y" })
    public FxRobotInterface dropBy(int x, int y) {

        try {
            HelperFunctions.robotLog("INFO", "Dropping by x=\"" + Integer.toString(x) + "\" and y=\""
            + Integer.toString(y) + "\"" );
            return robot.dropBy((double) x, (double) y);
        } catch (Exception e) {
            if ( e instanceof JavaFXLibraryNonFatalException ) {
                throw e;
            }
            throw new JavaFXLibraryNonFatalException("Unable to drop by: " +
                Integer.toString(x) + ", " + Integer.toString(y), e);
        }
    }

    @RobotKeyword("Moves the mouse to given coordinates _x_ and _y_ and presses the given mouse _buttons_\n\n"
            + "Integer argument ``x`` sets the source point for x -coordinate\n\n"
            + "Integer argument ``y`` sets the source point for y -coordinate\n\n"
            + "Optional parameter ``buttons`` is a list of mouse buttons to be used, defaults to PRIMARY. See `5. Used ENUMs` for different MouseButtons\n\n"
            + "\nExample:\n"
            + "| ${window}= | Get Window | title=Window Title | \n"
            + "| Drag From Coordinates | ${x} | ${y} | \n"
            + "| Drop To | ${window} | \n")
    @ArgumentNames({ "x", "y", "*buttons" })
    public FxRobotInterface dragFromCoordinates(int x, int y, String... buttons) {
        try {
            HelperFunctions.robotLog("INFO", "Dragging from x=\"" + Integer.toString(x) + "\" and y=\""
                    + Integer.toString(y) + "\" with buttons \"" + Arrays.toString(buttons) + "\"" );
            return robot.drag((double) x, (double) y, HelperFunctions.getMouseButtons(buttons));
        } catch (Exception e) {
            if ( e instanceof JavaFXLibraryNonFatalException ) {
                throw e;
            }
            throw new JavaFXLibraryNonFatalException("Unable to drag from coordinates: " +
                    Integer.toString(x) + ", " + Integer.toString(y), e);
        }
    }

    @RobotKeyword("Moves the mouse to given coordinates _x_ and _y_ and releases mouse buttons\n\n"
            + "Integer argument ``x`` sets the target point for x -coordinate\n\n"
            + "Integer argument ``y`` sets the target point for y -coordinate\n\n"
            + "\nExample:\n"
            + "| Drag From | \\#node-id | \n"
            + "| Drop To | 100 | 100 | \n")
    @ArgumentNames({ "x", "y" })
    public FxRobotInterface dropToCoordinates(int x, int y) {
        try {
            HelperFunctions.robotLog("INFO", "Dropping to x=\"" + Integer.toString(x) + "\" and y=\""
                    + Integer.toString(y) + "\"");
            return robot.dropTo((double) x, (double) y);
        } catch (Exception e) {
            if ( e instanceof JavaFXLibraryNonFatalException ) {
                throw e;
            }
            throw new JavaFXLibraryNonFatalException("Unable to drop to coordinates: " +
                    Integer.toString(x) + ", " + Integer.toString(y), e);
        }
    }
}