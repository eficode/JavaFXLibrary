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
import javafx.scene.Node;
import javafx.stage.Window;
import javafx.scene.Scene;

@RobotKeywords
public class NodeLookup extends TestFxAdapter {


    /* TODO: are these usabe in Robot Framework?
    @RobotKeyword
    public <T extends Node> NodeQuery lookup(Matcher<T> matcher) {
        return robot.lookup(matcher);
    }

    @RobotKeyword
    public <T extends Node> NodeQuery lookup(Predicate<T> predicate) {
        return robot.lookup(predicate);
    }
*/

    @RobotKeyword("Returns the root node of given element.\n\n"
            + "``locator`` is either a _query_ or _Object:Node, Window, Scene_ for identifying the element, see "
            + "`3. Locating or specifying UI elements`. \n\n"
            + "\nExamples for different kind of locators: \n\n"
            + "Window:\n"
            + "| ${window}= | Window By Title | ClickRobot Test | \n"
            + "| ${node}= | Get Root Node Of | ${window} | \n"
            + "Scene:\n"
            + "| ${some scene}= | Get Nodes Scene | ${some node} | \n"
            + "| ${root} | Get Root Node Of | ${some scene} | \n"
            + "Node:\n"
            + "| ${some node}= | find | \\#some-node-id | \n"
            + "| ${root} | Get Root Node Of | ${some node} | \n"
            + "Query:\n"
            + "| ${root} | Get Root Node Of | \\#some-node-id | \n" )
    @ArgumentNames({"locator"})
    public Object getRootNodeOf(Object locator) {
        try {
            if (locator instanceof Window) {
                HelperFunctions.robotLog("INFO", "Getting the root node for Window: \"" + locator.toString() + "\"");
                return HelperFunctions.mapObject(robot.rootNode((Window) locator));
            } else if (locator instanceof Scene) {
                HelperFunctions.robotLog("INFO", "Getting the root node for Scene: \"" + locator.toString() + "\"");
                return HelperFunctions.mapObject(robot.rootNode((Scene) locator));
            } else if (locator instanceof Node) {
                HelperFunctions.robotLog("INFO", "Getting the root node for Node: \"" + locator.toString() + "\"");
                return HelperFunctions.mapObject(robot.rootNode((Node) locator));
            } else if (locator instanceof String) {
                HelperFunctions.robotLog("INFO", "Getting the node for query: \"" + locator.toString() + "\"");
                Node node = robot.lookup((String) locator).query();
                if( node != null )
                    return getRootNodeOf(node);
                throw new JavaFXLibraryNonFatalException("Unable to find any node with query: \"" + locator.toString() + "\"");
            }

            throw new JavaFXLibraryNonFatalException("given object: \"" + locator.toString() + "\" was not a supported argument type!");

        } catch (Exception e) {
            if(e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to get root node for locator: \"" + locator.toString() + "\"", e);
        }
    }
}