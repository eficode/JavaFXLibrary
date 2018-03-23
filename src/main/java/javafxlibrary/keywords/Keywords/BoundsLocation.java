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
import javafxlibrary.utils.TestFxAdapter;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;
import javafx.geometry.Point2D;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Window;
import org.testfx.service.query.PointQuery;
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
    public Object createBounds(double minX,
                               double minY,
                               double width,
                               double height) {
        try {
            robotLog("INFO", "Creating bounds object with minX=\"" + minX + "\""
                                                                                     + ", minY=\"" + minY + "\""
                                                                                     + ", width=\"" + width + "\""
                                                                                     + " and height=\"" + height + "\"");
            return mapObject(robot.bounds(minX, minY, width, height).query());
        } catch (Exception e) {
            if ( e instanceof JavaFXLibraryNonFatalException ) {
                throw e;
            }
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
            robotLog("INFO", "Creating point object with x=\"" + x + "\""
                                                                                    + " and y=\"" + y + "\"");
            return mapObject(new Point2D(x, y));
        } catch (Exception e) {
            if ( e instanceof JavaFXLibraryNonFatalException ) {
                throw e;
            }
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
            robotLog("INFO", "Creating retangle object with minX=\"" + minX + "\""
                                                                                    + ", minY=\"" + minY + "\""
                                                                                    + ", width=\"" + width + "\""
                                                                                    + "and height=\"" + height + "\"");
            return mapObject(new Rectangle2D(minX, minY, width, height));
        } catch (Exception e) {
            if ( e instanceof JavaFXLibraryNonFatalException ) {
                throw e;
            }
            throw new JavaFXLibraryNonFatalException("Unable to create Rectangle object: " + e);
        }
    }

    /*
        TestFX has a bug in BoundQueryUtils boundsOnScreen(Bounds b, Window w) method which causes window location data
        to be incorrect. Problem is that the method first takes windows location with getMinX() and getMinY() (that
        already return the location on the screen) and then add an extra offset (getMinX() and getMinY() again) to them.
        This results the coordinates to be always twice as large than they should be.

        This bug also affects bounds(Scene s) method, as it uses the buggy boundsOnScreen to count the offset for the
        scene. Both of these method calls have been replaced with pure JavaFX, and shouldn't be affected in any way in
        case TestFX gets changed.

        Details:
        - version: testfx-core 4.0.6-alpha
        - location: main/java/org/testfx/api/util/BoundsQueryUtils.java: rows 153-160
     */
    @RobotKeyword("Returns a Bounds object for a region located using given locator. \n\n"
            + "``locator`` is either a _query_ or _Object:Node, Point2D, Scene, or Window_ for identifying the region"
            + ", see `3. Locating or specifying UI elements`. \n\n"
            + "\nExample:\n"
            + "| ${bounds}= | Get Bounds | ${node} | \n"
            + "| ${target}= | Create Bounds | 150 | 150 | 200 | 200 | \n"
            + "| Should Be Equal | ${bounds} | ${target} | \n")
    @ArgumentNames({ "locator", "logLevel=" })
    public Object getBounds(Object locator, String logLevel) {
        try {
            if (locator instanceof Window) {
                Window window = (Window) locator;
                robotLog(logLevel, "Getting bounds with window \"" + locator.toString() + "\"");
                return mapObject(new BoundingBox(window.getX(), window.getY(), window.getWidth(), window.getHeight()));

            } else if (locator instanceof Scene) {
                Scene scene = (Scene) locator;
                robotLog(logLevel, "Getting bounds with scene \"" + locator.toString() + "\"");
                return mapObject(new BoundingBox(scene.getX() + scene.getWindow().getX(), scene.getY() +
                        scene.getWindow().getY(), scene.getWidth(), scene.getHeight()));

            } else if (locator instanceof Point2D) {
                robotLog(logLevel, "Getting bounds with point object \"" + locator.toString() + "\"");
                return mapObject(robot.bounds((Point2D) locator).query());

            } else if (locator instanceof Node) {
                robotLog(logLevel, "Getting bounds with node \"" + locator.toString() + "\"");
                return mapObject(robot.bounds((Node) locator).query());

            } else if (locator instanceof String) {
                waitUntilExists((String) locator);
                Node node = robot.lookup((String) locator).query();
                robotLog(logLevel, "Getting bounds with query \"" + locator.toString() + "\"");
                return mapObject(robot.bounds(node).query());

            } else if (locator instanceof Bounds) {
                robotLog(logLevel, "Getting bounds with bounds object \"" + locator.toString() + "\"");
                return mapObject(locator);

            } else if (locator instanceof PointQuery) {
                robotLog(logLevel, "Getting bounds with point query \"" + locator.toString() + "\"");
                return mapObject(robot.bounds(((PointQuery) locator).query()).query());
            }

            throw new JavaFXLibraryNonFatalException("Unsupported parameter type: " + locator.toString());

        } catch (Exception e) {
            if ( e instanceof JavaFXLibraryNonFatalException ) {
                throw e;
            }
            throw new JavaFXLibraryNonFatalException("Couldn't find \"" + locator + "\"");
        }
    }

    @RobotKeywordOverload
    public Object getBounds(Object locator) {
        return getBounds(locator, "INFO");
    }

}