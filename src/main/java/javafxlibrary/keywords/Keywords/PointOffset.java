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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static javafxlibrary.utils.HelperFunctions.*;

@RobotKeywords
public class PointOffset extends TestFxAdapter {

    @RobotKeyword("Convenience method: Creates and returns a PointQuery pointing to the target with the given offset values. \n\n"
            + "``locator`` is either a _query_ or _Object:Bounds, Node, Point2D, Scene, Window_ for identifying the element, see "
            + "`3. Locating JavaFX Nodes`. \n\n"
            + "Parameters ``offsetX`` and ``offsetY`` are Double type values for x- and y-axis offsets.\n "
            + "\nExample: \n"
            + "| ${point query}= | Point To With Offset | ${some node} | 10.0 | -10.0 | \n"
            + "| ${point query offset}= | Call Method | ${point query} | getOffset | \n")
    @ArgumentNames({"locator", "offsetX", "offsetY"})
    public Object pointToWithOffset(Object locator, double offsetX, double offsetY) {
        checkObjectArgumentNotNull(locator);
        try {
            RobotLog.info("Creating a point query for target: \"" + locator + "\" with offset: [" + offsetX + ", " + offsetY + "]");
            if (locator instanceof String)
                locator = objectToNode(locator);
            Method method = MethodUtils.getMatchingAccessibleMethod(robot.getClass(), "offset",
                    locator.getClass(), double.class, double.class);
            return mapObject(method.invoke(robot, locator, offsetX, offsetY));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new JavaFXLibraryNonFatalException("Could not execute 'point to with offset' using locator \"" + locator
                    + "\": " + e.getCause().getMessage());
        }
    }
}