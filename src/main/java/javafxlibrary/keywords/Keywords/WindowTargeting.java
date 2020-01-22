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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import javafxlibrary.utils.RobotLog;
import javafxlibrary.utils.TestFxAdapter;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

@RobotKeywords
public class WindowTargeting extends TestFxAdapter {

    @RobotKeyword("Returns the last stored target window\n\n"
            + "\nExample: \n"
            + "| ${window}= | Get Target Window | \n")
    public Object getTargetWindow() {
        try {
            return HelperFunctions.mapObject(robot.targetWindow());
        } catch (Exception e) {
            if(e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to find target window.", e);
        }
    }

    @RobotKeyword("Sets active target window\n\n"
            + "``locator`` is either a _query_ or _Object:Node, Scene_ for identifying the Window. In addition to normal _query_, "
            + "locator can be a search string for _pattern=_, _title=_ or Integer number. See `3. Locating JavaFX Nodes`. \n\n"
            + "\nExamples for different kind of locators: \n\n"
            + "pattern (defaults to title):\n"
            + "| Set Target Window | My window title | \n"
            + "| Set Target Window | title=My window title | \n"
            + "| Set Target Window | pattern=W[i-w]{5} Title | \n\n"
            + "Index:\n"
            + "| Set Target Window | 0 | \n"
            + "| Set Target Window | ${2} | \n\n"
            + "Node:\n"
            + "| ${some_node}= | Find | \\#some_id | \n"
            + "| Set Target Window | ${some_node} | \n\n"
            + "Scene: \n"
            + "| ${some_scene}= | Get Nodes Scene | ${some_node} | \n"
            + "| Set Target Window | ${some_scene} | \n"
            )
    @ArgumentNames("locator")
    public void setTargetWindow(Object locator) {
        RobotLog.info("Setting target window according to locator \"" + locator + "\"");

        try {
            if (locator instanceof String) {
                if (((String) locator).startsWith("pattern=")){
                    locator = ((String) locator).replace("pattern=","");
                    RobotLog.debug("String which is pattern, converting...");
                    setTargetWindow(Pattern.compile((String)locator));
                } else if (((String) locator).matches("[0-9]+")) {
                    RobotLog.debug("String which is integer, converting...");
                    setTargetWindow(Integer.parseInt((String)locator));
                } else {
                    if (((String) locator).startsWith("title="))
                        locator = ((String) locator).replace("title=", "");
                    robot.targetWindow((String) locator);
                }
            } else {
                Method method = MethodUtils.getMatchingAccessibleMethod(robot.getClass(), "targetWindow", locator.getClass());
                method.invoke(robot, locator);
            }

            Platform.runLater((robot.targetWindow())::requestFocus);

        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new JavaFXLibraryNonFatalException("Could not execute set target window using locator \"" + locator + "\"");
        } catch (Exception e) {
            if (e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to set target window: \"" + locator.toString() + "\"", e);
        }
    }
}