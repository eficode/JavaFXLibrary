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
import javafx.stage.Window;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import javafxlibrary.utils.TestFxAdapter;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;
import org.testfx.api.FxRobotInterface;
import org.testfx.robot.Motion;
import org.testfx.service.query.PointQuery;

@RobotKeywords
public class MoveRobot extends TestFxAdapter {

    @RobotKeyword("Moves mouse over a node located using given locator.\n\n "
            + "``locator`` is either a _query_ or _Object:Bounds, Node, Point2D, PointQuery, Scene, Window_ for identifying the element, see "
            + "`3. Locating or specifying UI elements`. \n\n"
            + "``motion`` defines the path for mouse to move to a target location. Default value is _DIRECT_. \n\n"
            + "\nExample: \n"
            + "| ${x} | Evaluate | ${400} + ${SCENE_MINX} | \n"
            + "| ${y} | Evaluate | ${150} + ${SCENE_MINY} | \n"
            + "| ${point} | Create Point | ${x} | ${y} | \n"
            + "| Move To | ${POINT} | VERTICAL_FIRST | | # moves mouse on top of given Point object by moving first vertically and then horizontally |")
    @ArgumentNames({ "locator", "motion=DIRECT" })
    public FxRobotInterface moveTo(Object locator, String motion) {

        try {
            if (locator instanceof Window) {
                HelperFunctions.robotLog("INFO", "Moving to Window: \""
                        + locator.toString() + "\" using motion: \"" + motion  + "\"");
                return robot.moveTo((Window) locator, HelperFunctions.getMotion(motion));
            } else if (locator instanceof Scene) {
                HelperFunctions.robotLog("INFO", "Moving to Scene: \""
                        + locator.toString() + "\" using motion: \"" + motion  + "\"");
                return robot.moveTo((Scene) locator, HelperFunctions.getMotion(motion));
            } else if (locator instanceof Bounds) {
                HelperFunctions.robotLog("INFO", "Moving to Bounds: \""
                        + locator.toString() + "\" using motion: \"" + motion  + "\"");
                return robot.moveTo((Bounds) locator, HelperFunctions.getMotion(motion));
            } else if (locator instanceof Point2D) {
                HelperFunctions.robotLog("INFO", "Moving to Point2D: \""
                        + locator.toString() + "\" using motion: \"" + motion  + "\"");
                return robot.moveTo((Point2D) locator, HelperFunctions.getMotion(motion));
            } else if (locator instanceof PointQuery) {
                HelperFunctions.robotLog("INFO", "Moving to Pointquery: \""
                        + locator.toString() + "\" using motion: \"" + motion  + "\"");
                return robot.moveTo((PointQuery) locator, HelperFunctions.getMotion(motion));
            } else if (locator instanceof Node) {
                HelperFunctions.robotLog("INFO", "Moving to Node: \""
                        + locator.toString() + "\" using motion: \"" + motion  + "\"");
                return robot.moveTo((Node) locator, HelperFunctions.getMotion(motion));
            } else if (locator instanceof String) {
                HelperFunctions.robotLog("INFO", "Moving to string query \""
                        + locator.toString() + "\" using motion: \"" + motion  + "\"");
                return robot.moveTo((String) locator, HelperFunctions.getMotion(motion));
            }

            throw new JavaFXLibraryNonFatalException("Unsupported locator type: \"" + locator.toString() + "\"");

        } catch (Exception e) {
            if(e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to move to locator: \"" + locator.toString()
                    + "\" using motion: \"" + motion  + "\"", e);
        }
    }

    @RobotKeywordOverload
    public FxRobotInterface moveTo(Object locator) {
        return moveTo(locator, "DIRECT");
    }

    @RobotKeyword("Moves mouse directly from current location to new location specified by _x_ and _y_ offsets\n\n"
            + "``x`` is an integer value for horizontal axis x-offset. \n\n"
            + "``y`` is an integer value for vertical axis y-offset. \n\n"
            + "Optional argument ``motion`` defines the path for mouse to move to given coordinates. Default value is _DIRECT_. \n\n"
            + "\nExample: \n"
            + "| Move By | 75 | 75 | \n")
    @ArgumentNames({ "x", "y", "motion=DIRECT" })
    public FxRobotInterface moveBy(int x, int y, String motion) {
        try {
            HelperFunctions.robotLog("INFO", "Moving by [" + Integer.toString(x) + ", "
                    + Integer.toString(y) + "] using motion: \"" + motion  + "\"" );
            return robot.moveBy((double) x, (double) y, HelperFunctions.getMotion(motion));
        } catch (Exception e) {
            if(e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to move by using coordinates: " +
                    Integer.toString(x) + ", " + Integer.toString(y), e);
        }
    }

    @RobotKeywordOverload
    public FxRobotInterface moveBy(int x, int y) {
        return robot.moveBy((double) x, (double) y, Motion.DIRECT);
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
    @ArgumentNames({ "x", "y", "motion=DIRECT" })
    public FxRobotInterface moveToCoordinates(int x, int y, String motion) {
        try {
            HelperFunctions.robotLog("INFO", "Moving to coordinates: [" +
                    Integer.toString(x) + ", " + Integer.toString(y) + "] using motion: \"" + motion + "\"");
            return robot.moveTo((double) x, (double) y, HelperFunctions.getMotion(motion));
        } catch (Exception e) {
            if(e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to move to coordinates: [" +
                    Integer.toString(x) + ", " + Integer.toString(y) + "] using motion: \"" + motion + "\"", e);
        }
    }

    @RobotKeywordOverload
    public FxRobotInterface moveToCoordinates(int x, int y) {
        return moveToCoordinates(x, y, "DIRECT");
    }

}