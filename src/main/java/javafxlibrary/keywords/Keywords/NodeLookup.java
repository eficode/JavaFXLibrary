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

import javafx.scene.Node;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.Finder;
import javafxlibrary.utils.HelperFunctions;
import javafxlibrary.utils.RobotLog;
import javafxlibrary.utils.TestFxAdapter;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RobotKeywords
public class NodeLookup extends TestFxAdapter {

    @RobotKeyword("Returns the root node of given element.\n\n"
            + "``locator`` is either a _query_ or _Object:Node, Window, Scene_ for identifying the element, see "
            + "`3. Locating or specifying UI elements`. \n\n"
            + "\nExamples for different kind of locators: \n\n"
            + "Window:\n"
            + "| ${window}= | Get Window | title=ClickRobot Test | \n"
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
        if (locator instanceof String) {
            Node node = new Finder().find((String) locator);
            if( node != null )
                return getRootNodeOf(node);
            throw new JavaFXLibraryNonFatalException("Unable to find any node with query: \"" + locator.toString() + "\"");
        }

        RobotLog.info("Getting root node of target \"" + locator + "\"");
        Method method = MethodUtils.getMatchingAccessibleMethod(robot.getClass(), "rootNode", locator.getClass());
        try {
            return HelperFunctions.mapObject(method.invoke(robot, locator));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new JavaFXLibraryNonFatalException("Could not execute get root node of using locator \"" + locator
                    + "\": " + e.getCause().getMessage());
        }
    }
}