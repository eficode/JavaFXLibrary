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

import javafx.geometry.BoundingBox;
import javafx.geometry.Rectangle2D;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import javafxlibrary.utils.RobotLog;
import javafxlibrary.utils.TestFxAdapter;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.stage.Window;
import org.testfx.service.query.BoundsQuery;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static javafxlibrary.utils.HelperFunctions.*;

@RobotKeywords
public class BoundsLocation extends TestFxAdapter {

    @RobotKeyword("Creates a new Bounds object with the given parameters\n\n"
        + "``minX``, ``minY``, ``width``, ``height`` are Double type arguments.\n\n"
        + "\nExample:\n"
        + "| ${target bounds}= | Create Bounds | 150 | 150 | 0 | 0 | \n"
        + "| ${capture}= | Capture Bounds | ${target bounds} |\n"
        + "See more at: https://docs.oracle.com/javase/8/javafx/api/javafx/geometry/Bounds.html")
    @ArgumentNames({"minX", "minY", "width", "height"})
    public Object createBounds(double minX, double minY, double width, double height) {
        try {
            RobotLog.info("Creating bounds object with minX=\"" + minX + "\", minY=\"" + minY + "\", width=\"" + width +
                    "\" and height=\"" + height + "\"");
            return mapObject(robot.bounds(minX, minY, width, height).query());
        } catch (Exception e) {
            if ( e instanceof JavaFXLibraryNonFatalException )
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to create Bounds object: " + e);
        }
    }

    @RobotKeyword("Creates a new Point2D object with the given parameters\n\n"
        + "``x`` and ``y`` are both Double type arguments.\n\n"
        + "\nExample:\n"
        + "| ${point}= | Create Point | 150 | 150 | \n"
        + "| Drop To | ${point} | \n"
        + "See more at: https://docs.oracle.com/javase/8/javafx/api/javafx/geometry/Point2D.html")
    @ArgumentNames({"x", "y"})
    public Object createPoint(double x, double y) {
        try {
            RobotLog.info("Creating point object with x=\"" + x + "\"" + " and y=\"" + y + "\"");
            return mapObject(new Point2D(x, y));
        } catch (Exception e) {
            if (e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to create Point object: " + e);
        }
    }

    @RobotKeyword("Creates a new Rectangle2D object with the given parameters\n\n"
        + "``minX``, ``minY``, ``width``, ``height`` are Double type arguments.\n\n"
        + "\nExample:\n"
        + "| ${rectangle} | Create Rectangle | ${minX} | ${minY} | 240 | 240 | \n"
        + "| ${image1} | Capture Screen Region | ${rectangle} | \n\n"
        + "See more at: https://docs.oracle.com/javase/8/javafx/api/javafx/geometry/Rectangle2D.html")
    @ArgumentNames({"minX", "minY", "width", "height"})
    public Object createRectangle(double minX, double minY, double width, double height) {
        try {
            RobotLog.info("Creating retangle object with minX=\"" + minX + "\", minY=\"" + minY + "\", width=\"" +
                    width + "\" and height=\"" + height + "\"");
            return mapObject(new Rectangle2D(minX, minY, width, height));
        } catch (Exception e) {
            if (e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to create Rectangle object: " + e);
        }
    }

    @RobotKeyword("Returns a Bounds object for a region located using given locator. \n\n"
            + "``locator`` is either a _query_ or _Object:Node, Point2D, Scene, or Window_ for identifying the region"
            + ", see `3. Locating or specifying UI elements`. \n\n"
            + "\nExample:\n"
            + "| ${bounds}= | Get Bounds | ${node} | \n"
            + "| ${target}= | Create Bounds | 150 | 150 | 200 | 200 | \n"
            + "| Should Be Equal | ${bounds} | ${target} | \n")
    @ArgumentNames({ "locator" })
    public Object getBounds(Object locator) {
        RobotLog.info("Getting bounds using locator \"" + locator + "\"");
        // TODO: Test if Window and Scene objects get correct Bound locations on scaled displays
        try {
            if (locator instanceof Window) {
                Window window = (Window) locator;
                return mapObject(new BoundingBox(window.getX(), window.getY(), window.getWidth(), window.getHeight()));
            } else if (locator instanceof Scene) {
                Scene scene = (Scene) locator;
                return mapObject(new BoundingBox(scene.getX() + scene.getWindow().getX(), scene.getY() +
                        scene.getWindow().getY(), scene.getWidth(), scene.getHeight()));
            }

            if (locator instanceof String)
                return getBounds(waitUntilExists((String) locator));

            Method method = MethodUtils.getMatchingAccessibleMethod(robot.getClass(), "bounds", locator.getClass());
            BoundsQuery bounds = (BoundsQuery) method.invoke(robot, locator);
            return HelperFunctions.mapObject(bounds.query());

        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new JavaFXLibraryNonFatalException("Could not execute move to using locator \"" + locator + "\": "
                    + e.getCause().getMessage());
        } catch (Exception e) {
            if ( e instanceof JavaFXLibraryNonFatalException )
                throw e;
            throw new JavaFXLibraryNonFatalException("Couldn't find \"" + locator + "\"");
        }
    }
}