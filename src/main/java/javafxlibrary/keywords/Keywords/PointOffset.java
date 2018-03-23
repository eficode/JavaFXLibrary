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
public class PointOffset extends TestFxAdapter {

    @RobotKeyword("Convenience method: Creates and returns a PointQuery pointing to the target with the given offset values. \n\n"
            + "``locator`` is either a _query_ or _Object:Bounds, Node, Point2D, Scene, Window_ for identifying the element, see "
            + "`3. Locating or specifying UI elements`. \n\n"
            + "Parameters ``offsetX`` and ``offsetY`` are Double type values for x- and y-axis offsets.\n "
            + "\nExample: \n"
            + "| ${point query}= | Point To With Offset | ${some node} | 10.0 | -10.0 | \n"
            + "| ${point query offset}= | Call Method | ${point query} | getOffset | \n")
    @ArgumentNames({"locator", "offsetX", "offsetY"})
    public Object pointToWithOffset(Object locator, double offsetX, double offsetY) {
        try {
            if (locator instanceof Window) {
                HelperFunctions.robotLog("INFO", "Returning a pointquery to Window: \"" + locator.toString()
                        + "\" with offset: [" + Double.toString(offsetX) + ", " + Double.toString(offsetY) + "]");
                return HelperFunctions.mapObject(robot.offset((Window) locator, offsetX, offsetY));
            } else if (locator instanceof Scene) {
                HelperFunctions.robotLog("INFO", "Returning a pointquery to Scene: \"" + locator.toString()
                        + "\" with offset: [" + Double.toString(offsetX) + ", " + Double.toString(offsetY) + "]");
                return HelperFunctions.mapObject(robot.offset((Scene) locator, offsetX, offsetY));
            } else if (locator instanceof Bounds) {
                HelperFunctions.robotLog("INFO", "Returning a pointquery to Bounds: \"" + locator.toString()
                        + "\" with offset: [" + Double.toString(offsetX) + ", " + Double.toString(offsetY) + "]");
                return HelperFunctions.mapObject(robot.offset((Bounds) locator, offsetX, offsetY));
            } else if (locator instanceof Point2D) {
                HelperFunctions.robotLog("INFO", "Returning a pointquery to Point2D: \"" + locator.toString()
                        + "\" with offset: [" + Double.toString(offsetX) + ", " + Double.toString(offsetY) + "]");
                return HelperFunctions.mapObject(robot.offset((Point2D) locator, offsetX, offsetY));
            } else if (locator instanceof Node) {
                HelperFunctions.robotLog("INFO", "Returning a pointquery to Node: \"" + locator.toString()
                        + "\" with offset: [" + Double.toString(offsetX) + ", " + Double.toString(offsetY) + "]");
                return HelperFunctions.mapObject(robot.offset((Node) locator, offsetX, offsetY));
            } else if (locator instanceof String) {
                HelperFunctions.robotLog("INFO", "Returning a pointquery to query string: \"" + locator.toString()
                        + "\" with offset: [" + Double.toString(offsetX) + ", " + Double.toString(offsetY) + "]");
                return HelperFunctions.mapObject(robot.offset((String) locator, offsetX, offsetY));
            }

            throw new JavaFXLibraryNonFatalException("Unsupported locator type: \"" + locator.toString() + "\"");

        } catch (Exception e) {
            if(e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to point to locator: \"" + locator.toString() +
                    "\" with offset: [" + Double.toString(offsetX) + ", " + Double.toString(offsetY) + "]", e );
        }
    }
}