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
import javafxlibrary.utils.TestFxAdapter;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;
import javafx.geometry.Point2D;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Window;

@RobotKeywords
public class PointLocation extends TestFxAdapter {

    @RobotKeyword("Sets the current position pointer to a point located using given locator and returns a PointQuery object for it. \n\n"
            + "``locator`` is either a _query_ or _Object:Bounds, Node, Point2D, Scene, Window_ for identifying the element, see "
            + "`3. Locating or specifying UI elements`. \n\n"
            + "\nExample: \n"
            + "| ${point query}= | Point To | ${node} |\n"
            + "| Move To | ${point query} | \n"
            + "| ${point query position}= | Call Method | ${point query} | getPosition | \n"
            + "| Set Target Position | BOTTOM_RIGHT | \n"
            + "| ${point query}= | Point To | ${some node} | \n"
            + "| Move To | ${point query} | | | # moves to bottom right corner of a node that was stored in PointQuery object. |\n")
    @ArgumentNames({"locator"})
    public Object pointTo(Object locator) {
        try {
            if (locator instanceof Window) {
                HelperFunctions.robotLog("INFO", "Returning a pointquery to Window: \"" + locator.toString() + "\"");
                return HelperFunctions.mapObject(robot.point((Window) locator));
            } else if (locator instanceof Scene) {
                HelperFunctions.robotLog("INFO", "Returning a pointquery to Scene: \"" + locator.toString() + "\"");
                return HelperFunctions.mapObject(robot.point((Scene) locator));
            } else if (locator instanceof Bounds) {
                HelperFunctions.robotLog("INFO", "Returning a pointquery to Bounds: \"" + locator.toString() + "\"");
                return HelperFunctions.mapObject(robot.point((Bounds) locator));
            } else if (locator instanceof Point2D) {
                HelperFunctions.robotLog("INFO", "Returning a pointquery to Point2D: \"" + locator.toString() + "\"");
                return HelperFunctions.mapObject(robot.point((Point2D) locator));
            } else if (locator instanceof Node) {
                HelperFunctions.robotLog("INFO", "Returning a pointquery to Node: \"" + locator.toString() + "\"");
                return HelperFunctions.mapObject(robot.point((Node) locator));
            } else if (locator instanceof String) {
                HelperFunctions.robotLog("INFO", "Returning a pointquery to query: \"" + locator.toString() + "\"");
                return HelperFunctions.mapObject(robot.point((String) locator));
            }

            throw new JavaFXLibraryNonFatalException("Unsupported locator type: \"" + locator.toString() + "\"");

        } catch (Exception e) {
            if(e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to point to locator: \"" + locator.toString() + "\"", e);
        }
    }

    @RobotKeyword("Sets the current position pointer to new location based on x,y coordinates and returns a PointQuery object for it.\n\n"
            + "``x`` and ``y`` defines the Integer values for the x- and y -coordinates.\n\n"
            + "\nExample: \n"
            + "| ${point query}= | Point To Coordinates | 100 | 200 | \n")
    @ArgumentNames({"x", "y"})
    public Object pointToCoordinates(int x, int y) {
        try {
            HelperFunctions.robotLog("INFO", "Returning a pointquery to coordinates: ["
                    + Integer.toString(x) + ", " + Integer.toString(y) + "]");
            return HelperFunctions.mapObject(robot.point((double) x, (double) y));
        } catch (Exception e) {
            if(e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to point to coordinates: [" +
                    Integer.toString(x) + ", " + Integer.toString(y) + "]", e);
        }
    }
}