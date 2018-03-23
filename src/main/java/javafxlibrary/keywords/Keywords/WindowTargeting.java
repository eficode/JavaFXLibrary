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

import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Window;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import javafxlibrary.utils.TestFxAdapter;
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
            + "locator can be a search string for _pattern=_, _title=_ or Integer number. See `3. Locating or specifying UI elements`. \n\n"
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
        try {
            if (locator instanceof String) {
                if (((String) locator).startsWith("pattern=")){
                    locator = ((String) locator).replace("pattern=","");
                    HelperFunctions.robotLog("DEBUG", "String which is pattern, converting...");
                    setTargetWindow((Pattern) Pattern.compile((String)locator));
                } else if (((String) locator).matches("[0-9]+")) {
                    HelperFunctions.robotLog("DEBUG", "String which is integer, converting...");
                    setTargetWindow(Integer.parseInt(locator.toString()));
                } else {
                    if (((String) locator).startsWith("title=")) { locator = ((String) locator).replace("title=", "");}
                    HelperFunctions.robotLog("INFO", "Setting target window with title \"" + locator + "\"");
                    robot.targetWindow((String) locator);
                }
            }
            if (locator instanceof Window) {
                HelperFunctions.robotLog("INFO", "Setting target window according to window \"" + locator.toString() + "\"");
                robot.targetWindow((Window) locator);
            }
            if (locator instanceof Integer) {
                HelperFunctions.robotLog("INFO", "Setting target window according to window index \"" + locator.toString() + "\"");
                robot.targetWindow((Integer) locator);
            }
            if (locator instanceof Scene) {
                HelperFunctions.robotLog("INFO", "Setting target window according to window scene \"" + locator.toString() + "\"");
                robot.targetWindow((Scene) locator);
            }
            if (locator instanceof Node) {
                HelperFunctions.robotLog("INFO", "Setting target window according to window node \"" + locator.toString() + "\"");
                robot.targetWindow((Node) locator);
            }
            if (locator instanceof Pattern) {
                HelperFunctions.robotLog("INFO", "Setting target window according to window title pattern \"" + locator.toString() + "\"");
                robot.targetWindow((Pattern) locator);
            }

            Platform.runLater( (robot.targetWindow())::requestFocus );

        } catch (Exception e) {
            if (e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to set target window: \"" + locator.toString() + "\"", e);
        }
    }
}