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

package javafxlibrary.keywords.AdditionalKeywords;

import com.sun.javafx.scene.control.skin.TableViewSkin;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.geometry.BoundingBox;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.matchers.InstanceOfMatcher;
import javafxlibrary.utils.HelperFunctions;
import javafxlibrary.utils.TestFxAdapter;
import javafxlibrary.utils.XPathFinder;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;
import org.testfx.robot.Motion;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;
import static javafxlibrary.utils.HelperFunctions.*;

@RobotKeywords
public class ConvenienceKeywords extends TestFxAdapter {

    @Deprecated
    @RobotKeyword("*DEPRECATED!!* Use keyword `Find` instead.\n\n" +
            "Finder that mimics _xpath_ style search.\n\n"
            + "``query`` is a query locator, see `3.1 Using queries`.\n\n"
            + "``failIfNotFound`` specifies if keyword should fail if nothing is found. By default it's false and "
            + "keyword returns null in case lookup returns nothing.\n\n"
            + "\nExample:\n"
            + " | ${node}= | Find With Path | .main-view[0] .split-pane[0] \\#node-id class=GridPane .toggle-button[3] sometext | ")
    @ArgumentNames({"query", "failIfNotFound=False"})
    public Object findWithPath(String query, boolean failIfNotFound){

        try {
            return mapObject(findNode(query));

        } catch (JavaFXLibraryNonFatalException e){
            if(failIfNotFound)
                throw new JavaFXLibraryNonFatalException("Unable to find anything with query: \"" + query + "\"");
            return "";

        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Find operation failed for query: \"" + query + "\"", e);
        }
    }

    @Deprecated
    @RobotKeywordOverload
    @ArgumentNames({ "query" })
    public Object findWithPath(String query) {
        return findWithPath(query, false);
    }

    @RobotKeyword("Brings the given stage to front\n\n"
            + "``stage`` is an Object:Stage to be set in front of others`, see `3.2 Using objects`. \n\n")
    @ArgumentNames({ "stage" })
    public void bringStageToFront(Stage stage) {
        robotLog("INFO", "Bringing following Stage to front: \"" + stage + "\"");
        try {
            Platform.runLater(() -> stage.toFront());
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Unable to bring stage to front.", e);
        }
    }

    @RobotKeyword("Calls a given method for a given java object.\n\n"
            + "``object`` is a Java object retrieved using JavaFXLibrary keywords, see `3.2 Using objects`.\n\n"
            + "``method`` is the name of the method that will be called.\n\n"
            + "Optional ``arguments`` are variable-length arguments that will be provided for the method.\n "
            + "If argument type is boolean, byte, char, double, float, int, long or short, it must have \"casting instructions\" "
            + "in front of it, e.g. _\"(boolean)false\"_.\n\n"
            + "\nExample:\n"
            + "| ${node}= | Find | \\#node-id | \n"
            + "| ${max height}= | Call Object Method | ${node} | maxHeight | (double)10 | \n"
            + "| ${node text}= | Call Object Method | ${node} | getText | \n")
    @ArgumentNames({ "object", "method", "*arguments=" })
    public Object callObjectMethod(Object object, String method, Object... arguments) {
        /* Workaround for overloading the keyword, Javalib Core seems to have a bug which causes overloaded keywords that
           take varargs throw IllegalArgumentException occasionally. Some of the calls for the base keyword get directed
           to the overloaded keyword, so the method invocation fails because of incorrect arguments. */

        /* Javalib Core changes all parameters to Strings after runKeywords automatic argument replacement, so arguments
           are replaced with objects from objectMap here instead. */
        object = HelperFunctions.useMappedObject(object);
        Object[] tempArgs = HelperFunctions.checkMethodArguments(arguments);
        Object[] finalArgs = HelperFunctions.useMappedObjects(tempArgs);

        Object result;

        if (finalArgs.length == 0)
            result = callMethod(object, method, false);
        else
            result = callMethod(object, method, finalArgs, false);

        if (result != null)
            return mapObject(result);

        return null;
    }

    @RobotKeyword("Calls given method in FX Application Thread using Platform.runLater(). See `Call Object Method` "
            + "for further documentation.\n\n"
            + "\nExample:\n"
            + "| ${node}= | Find | \\#node-id | \n"
            + "| Call Object Method In Fx Application Thread | ${node} | maxHeight | (boolean)false | \n")
    @ArgumentNames({ "object", "method", "*arguments=" })
    public void callObjectMethodInFxApplicationThread(Object object, String method, Object... arguments) {
        // Check callObjectMethod for info about argument replacing and overloading in these keywords.
        object = HelperFunctions.useMappedObject(object);
        Object[] tempArgs = HelperFunctions.checkMethodArguments(arguments);
        Object[] finalArgs = HelperFunctions.useMappedObjects(tempArgs);

        if (finalArgs.length == 0)
            callMethod(object, method, true);
        else
            callMethod(object, method, finalArgs, true);
    }

    @Deprecated
    @RobotKeyword("*DEPRECATED!!* Use keyword `Find` instead.\n\n"
            + "Returns the *first* node matching the query. \n\n"
            + "``query`` is the Class name String to use in lookup.\n"
            + "\nExample:\n"
            + "| ${my node}= | Find | javafx.scene.control.Button | # button class |")
    @ArgumentNames({ "query" })
    public Object findClass(final String query) {
        try {
            Class<?> clazz = Class.forName(query);
            InstanceOfMatcher matcher = new InstanceOfMatcher(clazz);
            return mapObject(robot.lookup(matcher).query());
        } catch (Exception e) {
            robotLog("TRACE", "Problem has occurred during node lookup: " + e);
            return "";
        }
    }

    @Deprecated
    @RobotKeyword("*DEPRECATED!!* Use keyword `Find` instead.\n\n"
            + "Returns *all* descendant nodes of given node matching the query. \n\n"
            + "``node`` is the starting point Object:Node from where to start looking, see `3.2 Using objects`. \n\n"
            + "``query`` is a query locator, see `3.1 Using queries`.\n\n"
            + "``failIfNotFound`` specifies if keyword should fail if nothing is found. By default it's false and "
            + "keyword returns null in case lookup returns nothing.\n\n"
            + "\nExample:\n"
            + "| ${my nodes}= | Find All From Node | ${some node} | .css | \n"
            + "See keyword `Find` for further examples of query usage.\n")
    @ArgumentNames({ "node", "query", "failIfNotFound=False" })
    public List<Object> findAllFromNode(Object node, String query, boolean failIfNotFound) {
        try {
            if ( node instanceof Node ) {
                robotLog("INFO", "Trying to find all nodes with query: \"" + query
                        + "\" that are under starting point node: \"" + node.toString() + "\", failIfNotFound= \""
                        + Boolean.toString(failIfNotFound) + "\"");
                return mapObjects(((Node) node).lookupAll(query));
            }
            // fail in case no valid node argument.
            failIfNotFound = true;
            throw new JavaFXLibraryNonFatalException("Illegal argument type for node.");
        } catch (JavaFXLibraryNonFatalException e){
            if(failIfNotFound)
                throw e;
            return Collections.emptyList();

        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Find all from node operation failed for node: \"" + node.toString() +
                    "\" and query: " + query, e);
        }
    }

    @Deprecated
    @RobotKeywordOverload
    @ArgumentNames({ "node", "query" })
    public List<Object> findAllFromNode(Object node, String query) {
            return findAllFromNode(node, query, false);
    }

    @Deprecated
    @RobotKeyword("*DEPRECATED!!* Use keyword `Find` instead.\n\n"
            + "Returns *all* nodes matching query AND given pseudo-class state. \r\n"
            + "``query`` is a query locator, see `3.1 Using queries`.\n\n"
            + "``pseudo`` is a String value specifying pseudo class value.\n\n"
            + "``failIfNotFound`` specifies if keyword should fail if nothing is found. By default it's false and "
            + "keyword returns null in case lookup returns nothing.\n\n"
            + "\nExample:\n"
            + "| ${my node}= | Find All With Pseudo Class | .check-box-tree-cell .check-box | selected | \n")
    @ArgumentNames({ "query", "pseudo", "failIfNotFound=" })
    public List<Object> findAllWithPseudoClass(String query, String pseudo, boolean failIfNotFound) {
        robotLog("INFO", "Trying to find all nodes with query: \"" + query
                + "\" that has pseudoclass state as: \"" + pseudo + "\", failIfNotFound= \"" + Boolean.toString(failIfNotFound) + "\"");
        try {
            Set<Node> nodes = robot.lookup(query).queryAll();
            Set<Node> matches = nodes.stream()
                    .filter(n -> n.getPseudoClassStates().stream().
                            map(PseudoClass::getPseudoClassName).anyMatch(pseudo::contains))
                    .collect(Collectors.toSet());
            return mapObjects(matches);

        } catch (JavaFXLibraryNonFatalException e){
            if(failIfNotFound)
                throw e;
            return Collections.emptyList();

        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Find all with pseudo class operation failed for query: \"" +
                    query + "\" and pseudo: \"" + pseudo + "\"", e);
        }
    }

    @Deprecated
    @RobotKeywordOverload
    @ArgumentNames({ "query", "pseudo" })
    public List<Object> findAllWithPseudoClass(String query, String pseudo) {
        return findAllWithPseudoClass(query, pseudo, false);
    }

    @Deprecated
    @RobotKeyword("*DEPRECATED!!* Use keyword `Find` instead.\n\n"
            + "Returns the *first* descendant node of given node matching the query. \n\n"
            + "``node`` is the starting point Object:Node from where to start looking, see `3.2 Using objects`. \n\n"
            + "``query`` is a query locator, see `3.1 Using queries`.\n\n"
            + "``failIfNotFound`` specifies if keyword should fail if nothing is found. By default it's false and "
            + "keyword returns null in case lookup returns nothing.\n\n"
            + "\nExample:\n"
            + "| ${my node}= | Find From Node | ${some node} | .css |\n"
            + "See keyword `Find` for further examples of query usage.\n")
    @ArgumentNames({ "node", "query", "failIfNotFound=" })
    public Object findFromNode(Node node, String query, boolean failIfNotFound) {
        robotLog("INFO", "Trying to find: \"" + query + "\" from node: \"" + node.toString()
                + "\", failIfNotFound= \"" + Boolean.toString(failIfNotFound) + "\"");
        try {
            Node childNode = node.lookup(query);
            return mapObject(childNode);

        } catch (JavaFXLibraryNonFatalException e){
            if(failIfNotFound)
                throw e;
            return "";

        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Find from node operation failed for node: \"" + node.toString() +
                    "\" and query: " + query, e);
        }
    }

    @Deprecated
    @RobotKeywordOverload
    @ArgumentNames({ "node", "query" })
    public Object findFromNode(Node node, String query) {
        return findFromNode(node, query, false);
    }

    @RobotKeyword("Lists methods available for given node.\n"
            + "``node`` is the Object:Node which methods to list, see `3.2 Using objects`. \n\n"
            + "When working with custom components you may use this keyword to discover methods you can call "
            + "with `Call Method` keyword.\n\n"
            + "Example:\n"
            + "| List Component Methods | ${my node} |\n")
    @ArgumentNames({ "node" })
    public String[] listNodeMethods(Node node) {
        robotLog("INFO", "Listing all available methods for node: \"" + node.toString() + "\"" );
        try {
            Class klass = node.getClass();
            ArrayList<String> list = new ArrayList<String>();
            System.out.println("*INFO*");
            while (klass != null) {
                String name = String.format("\n*%s*\n", klass.getName());
                System.out.println(name);
                list.add(name);
                for (Method m : klass.getDeclaredMethods()) {
                    String entry = getMethodDescription(m);
                    System.out.println(entry);
                    list.add(entry);
                }
                klass = klass.getSuperclass();
            }
            return list.toArray(new String[list.size()]);
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Listing node methods failed.", e);
        }
    }

    private String getMethodDescription(Method m) {
        String entry = m.getReturnType().getName() + " ";
        entry += m.getName();
        entry += "(";
        Class[] args = m.getParameterTypes();
        for (int i = 0; i < args.length; i++) {
            entry += args[i].getName();
            if (i != args.length - 1)
                entry += ", ";
        }
        return entry + ")";
    }

    @RobotKeyword("Prints all child nodes starting from a given node.\n\n"
            + "Optional argument ``root`` is the starting point from where to start listing child nodes, "
            + "see `3.2 Using locators as keyword arguments`. Defaults to root node of current window. \n\n"
            + "\nExample:\n"
            + "| ${my node}= | Find | \\#node-id | \n"
            + "| Print Child Nodes | ${my node} | \n")
    @ArgumentNames({ "root=" })
    public void printChildNodes(Object root) {
        try {
            robotLog("INFO", "Printing tree structure for node: \"" + root.toString() + "\"");
            printTreeStructure((Parent) objectToNode(root));
        } catch (ClassCastException e) {
            throw new JavaFXLibraryNonFatalException(root.getClass() + " is not a subclass of javafx.scene.Parent");
        }
    }

    @RobotKeywordOverload
    public void printChildNodes() {
        try {
            printTreeStructure(robot.listTargetWindows().get(0).getScene().getRoot());
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Unable to find current root node.", e);
        }
    }

    // TODO: Should printChildNodes be deprecated?
    @RobotKeyword("Generates and prints FXML representation of the application starting from a given node.\n\n"
            + "Optional argument ``root`` is the starting point from where to start listing child nodes, "
            + "see `3.2 Using locators as keyword arguments`. Defaults to root node of current window. \n\n"
            + "\nExample:\n"
            + "| ${my node}= | Find | \\#node-id | \n"
            + "| Log FXML | ${my node} | \n")
    @ArgumentNames({"root="})
    public void logFXML(Object root) {
        XPathFinder logger = new XPathFinder();
        logger.setNodeLogging(false);
        robotLog("INFO", logger.getFxml((Parent) objectToNode(root)));
    }

    @RobotKeywordOverload
    public void logFXML() {
        XPathFinder logger = new XPathFinder();
        logger.setNodeLogging(false);
        robotLog("INFO", logger.getFxml(robot.listTargetWindows().get(0).getScene().getRoot()));
    }

    @RobotKeyword("Enables/Disables clicking outside of visible JavaFX application windows. Safe clicking is on by" +
            " default, preventing clicks outside of the tested application.\n\n" +
            "``value`` can be any of the following: ON, on, OFF, off.\n\n"
            + "Parameter _value_ specifies whether safety should be toggled on or off")
    @ArgumentNames({ "value" })
    public void setSafeClicking(String value) {
        switch (value) {
            case "OFF":
            case "off":
                robotLog("INFO", "Setting safe clicking mode to OFF");
                HelperFunctions.setSafeClicking(false);
                break;
            case "ON":
            case "on":
                robotLog("INFO", "Setting safe clicking mode to ON");
                HelperFunctions.setSafeClicking(true);
                break;
            default:
                throw new JavaFXLibraryNonFatalException("Unknown value: \"" + value + "\". Expected values are: on, ON, off and OFF.");
        }
    }

    @RobotKeyword("Sets the time waited for nodes to become available. Default value is 5 seconds."
            + "``timeout`` is an Integer value for timeout.")
    @ArgumentNames({ "timeout" })
    public void setTimeout(int timeout) {
        robotLog("INFO", "Setting timeout to " + timeout + "s");
        setWaitUntilTimeout(timeout);
    }

    /*
     * TODO: Switching between test applications using CMD + TAB doesn't work on Mac
     * cmd + tab moves between top level applications and multiple JavaFX applications launched by the testing framework
     * are bundled under a single tab named Java.
     */
    @RobotKeyword("Presses ALT/CMD + TAB for the given amount of times. \n\n"
            + "``switchAmount`` is an Integer value and specifies how many switches will be made in total")
    @ArgumentNames({ "switchAmount" })
    public void switchWindow(int switchAmount) {
        robotLog("INFO", "Switching window for: \"" + Integer.toString(switchAmount) + "\" times.");
        try {
            if (isMac()) {
                robot.press(KeyCode.META);
            } else {
                robot.press(KeyCode.ALT);
            }

            for (int i = 0; i < switchAmount; i++) {
                robot.push(KeyCode.TAB);
            }
            robot.release(KeyCode.META);
            robot.release(KeyCode.ALT);
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Unable to switch window.", e);
        }
    }

    // TODO: Implement getNodeProperty keyword and deprecate below get* keywords
    @RobotKeyword("Calls getPseudoClassStates() -method for a given node and returns a list of values returned by the method.\n\n"
            + "``locator`` is either a _query_ or _Object_ for node whose pseudo class states will be queried, see "
            + "`3. Locating or specifying UI elements`. \n\n"
            + "\nExample:\n"
            + "| ${states}= | Get Pseudo Class States | ${node} | \n"
            + "| Log List | ${states} | \n")
    @ArgumentNames({ "node" })
    public Set<PseudoClass> getPseudoClassStates(Object locator) {
        Node node = objectToNode(locator);

        try {
            robotLog("INFO", "Getting pseudoclass states for node: \"" + node.toString() + "\"");
            return node.getPseudoClassStates();
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Unable to get pseudoClassStates for node: " + node.toString());
        }
    }

    // TODO: Should this be deleted? Find All From Node has the same functionality
    @Deprecated
    @RobotKeyword("*DEPRECATED!!* Use keyword `Find` instead.\n\n"
            + "Returns *all* descendant nodes of given node matching the given Java class name. \n\n"
            + "``locator`` is either a _query_ or _Object_ for node whose children will be queried, see "
            + "`3.2 Using locators as keyword arguments`. \n\n"
            + "``className`` is the Java class name to look for.\n"
            + "\nExample:\n"
            + "| ${panes}= | Get Node Children By Class Name | ${some node} | BorderPane | \n"
            + "Returns an empty list if none is found. \n")
    @ArgumentNames({ "node", "className" })
    public Set<Object> getNodeChildrenByClassName(Object locator, String className) {
        Node node = objectToNode(locator);
        robotLog("INFO", "Getting node: \"" + node.toString() + "\" children by class name: \""
                + className + "\"");
        try {
            Set<Object> keys = new HashSet<Object>();
            Set childNodes = node.lookupAll("*");
            Iterator iter = childNodes.iterator();

            while (iter.hasNext()) {
                Node childNode = (Node) iter.next();
                if (childNode.getClass().getSimpleName().equals(className)) {
                    robotLog("TRACE", "Classname: \"" + className + "\" found: \"" + childNode.toString() + "\"");
                    keys.add(mapObject(childNode));
                }
            }
            return keys;
        } catch (Exception e) {
            if(e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to get node children for node: \"" + node.toString() +
                    "\" with class name: " + className, e);
        }
    }

    @RobotKeyword("Returns text value of the Node. \n\n"
            + "``locator`` is either a _query_ or _Object_ for a node whose getText method will be called, see "
            + "`3. Locating or specifying UI elements`. \n\n")
    @ArgumentNames({ "locator" })
    public String getNodeText(Object locator) {
        Node node = objectToNode(locator);
        robotLog("INFO", "Getting text value for node: \"" + node + "\"");
        Class<? extends Node> c = node.getClass();
        try {
            return (String) c.getMethod("getText").invoke(node);
        } catch (NoSuchMethodException e) {
            throw new JavaFXLibraryNonFatalException("Get node text failed for node: " + node + ": Node has no getText method");
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Get node text failed for node: " + node, e);
        }
    }

    @RobotKeyword("Returns height value of the node. \n\n"
            + "``locator`` is either a _query_ or _Object_ for a node whose getHeight method will be called, see "
            + "`3. Locating or specifying UI elements`. \n\n")
    @ArgumentNames({ "locator" })
    public String getNodeHeight(Object locator) {
        Node node = objectToNode(locator);
        try {
            Method[] methods = node.getClass().getMethods();
            for (Method m : methods) {
                if (m.getName().equals("getHeight")) {
                    try {
                        Object result = m.invoke(node, null);
                        return result.toString();
                    } catch (Exception e) {
                        throw new JavaFXLibraryNonFatalException("Problem calling method: .getHeight(): " + e.getMessage(), e);
                    }
                }
            }
            throw new JavaFXLibraryNonFatalException(
                    "Get node height failed for node: \"" + node.toString() + "\". Element has no method getHeight()");
        } catch (Exception e) {
            if (e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to get node height for node: " + node.toString(), e);
        }
    }

    @RobotKeyword("Returns image name and path of the node. \n\n"
            + "``locator`` is either a _query_ or _Object_ for a node whose getHeight method will be called, see "
            + "`3. Locating or specifying UI elements`. \n\n"
            + "Returns full image path by subsequently calling impl_getUrl -method. \n\n"
            + "Note, impl_getUrl -method is deprecated! Support for this method will be removed from Java in the future.")
    @ArgumentNames({ "node" })
    public String getNodeImageUrl(Object locator) {
        Node node = objectToNode(locator);
        robotLog("INFO", "Getting image url from node: \"" + node.toString() + "\"");
        try {
            Method[] methods = node.getClass().getMethods();
            for (Method m : methods) {
                if (m.getName().equals("getImage") && m.getParameterCount() == 0) {
                    robotLog("TRACE", "Method getImage() found. Invoking it on node: \"" + node.toString() + "\"");
                    try {
                        Object result = m.invoke(node, null);
                        Image image = (Image) result;
                        robotLog("TRACE", "Calling deprecated method impl_getUrl() for image: \"" + image.toString() + "\"");
                        return image.impl_getUrl();
                    } catch (Exception e) {
                        throw new JavaFXLibraryNonFatalException("Problem calling method: .getImage(): " + e.getMessage(), e);
                    }
                }
            }
            throw new JavaFXLibraryNonFatalException(
                    "Get node image url failed for node: \"" + node.toString() + "\". Element has no method impl_getUrl()");
        } catch (Exception e) {
            if( e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to get node image url for node: \"" + node.toString() + "\"", e );
        }
    }

    @RobotKeyword("Returns the parent node of node. \n\n"
            + "``locator`` is either a _query_ or _Object_ for a node whose getParent method will be called, see "
            + "`3. Locating or specifying UI elements`. \n\n")
    @ArgumentNames({ "node" })
    public Object getNodeParent(Object locator) {
        Node node = objectToNode(locator);

        try {
            robotLog("INFO", "Getting node parent object for: \"" + node.toString() + "\"");
            return mapObject(node.getParent());
        } catch (Exception e) {
            if( e instanceof JavaFXLibraryNonFatalException )
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to get node parent for node: " + node.toString(), e);
        }
    }

    @RobotKeyword("Returns the class name of a given node. \n\n"
            + "``locator`` is either a _query_ or _Object_ for a node whose getSimpleName method will be called, see "
            + "`3. Locating or specifying UI elements`. \n\n")
    @ArgumentNames({ "locator" })
    public String getObjectClassName(Object locator) {
        Node node = objectToNode(locator);

        try {
            robotLog("INFO", "Getting class name for object: \"" + node.toString() + "\"");
            return node.getClass().getSimpleName();
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Unable to get class name for object: " + node.toString(), e);
        }
    }

    @Deprecated
    @RobotKeyword("*DEPRECATED!!* Use keyword `Get Scene` instead.\n\n"
            +"Returns given locators Scene object. \n\n"
            + "``locator`` is either a _query_ or a _Node_, see `3.2 Using locators as keyword arguments`\n\n")
    @ArgumentNames({ "locator" })
    public Object getNodesScene(Object locator) {
        try {
            if (locator instanceof Node){
                robotLog("INFO", "Getting a Scene object for a Node: \"" + locator + "\"");
                return mapObject(((Node) locator).getScene());
            } else if (locator instanceof String) {
                robotLog("INFO", "Getting a Scene object for a query: \"" + locator + "\"");
                Node node = objectToNode(locator);
                return mapObject(node.getScene());
            }

            throw new JavaFXLibraryNonFatalException("locator type is not a Node or a query string!");

        } catch (Exception e) {
            if (e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to get Scene object for locator: \"" + locator + "\"", e);
        }
    }

    @RobotKeyword("Returns Scene of the given object. \n\n"
            + "``locator`` is either a _query_, a _Node_ or a _Window_, see `3.2 Using locators as keyword arguments`\n\n")
    @ArgumentNames({ "locator" })
    public Object getScene(Object locator) {
        try {
            robotLog("INFO", "Getting a Scene object for: \"" + locator + "\"");
            if (locator instanceof Node){
                return mapObject(((Node) locator).getScene());
            } else if (locator instanceof String) {
                Node node = objectToNode(locator);
                return mapObject(node.getScene());
            } else if (locator instanceof Window) {
                return mapObject(((Window) locator).getScene());
            }

            throw new JavaFXLibraryNonFatalException("Unsupported locator type. Locator must be an instance of Node, String or Window!");

        } catch (Exception e) {
            if (e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to get Scene object for locator: \"" + locator + "\"", e);
        }
    }

    @RobotKeyword("Returns the title of the given window. \n\n"
            + "``locator`` is an _Object:Window_ whose getTitle method will be called, see "
            + "`3.2 Using objects`. This keyword can be coupled with e.g. `List Windows` -keyword.\n\n")
    @ArgumentNames({ "window" })
    public String getWindowTitle(Object object) {
        robotLog("INFO", "Getting the window title for: \"" + object.toString() + "\"");

        try {
            Method m = object.getClass().getMethod("getTitle");
            return (String) m.invoke(object, null);
        } catch (NoSuchMethodException e) {
            robotLog("INFO", "This window type has no getTitle() -method. Returning null");
            return "";
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Unable to get title for window: " + object.toString(), e);
        }
    }

    @RobotKeyword("Returns the bounds of primary screen. \n")
    public Object getPrimaryScreenBounds() {
        try {
            robotLog("INFO", "Getting the primary screen bounds");
            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
            return mapObject(new BoundingBox(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight()));
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Unable to get primary screen bounds.", e);
        }
    }

    @RobotKeyword("Returns the library version from POM file")
    public String getLibraryVersion() {
        return HelperFunctions.loadRobotLibraryVersion();
    }

    @RobotKeyword("Returns the value of cell in the given location\n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the TableView element, see "
            + "`3. Locating or specifying UI elements`. \n\n"
            + "``row`` Integer value for the row\n\n"
            + "``column`` Integer value for the column")
    @ArgumentNames({ "table", "row", "column" })
    public Object getTableCellValue(Object locator, int row, int column) {
        try {
            TableView table = (TableView) objectToNode(locator);
            Object item = table.getItems().get(row);
            TableColumn col = (TableColumn) table.getColumns().get(column);
            Object value = col.getCellObservableValue(item).getValue();
            return HelperFunctions.mapObject(value);
        } catch (ClassCastException cce) {
            throw new JavaFXLibraryNonFatalException("Unable to handle argument as TableView!");
        } catch (IndexOutOfBoundsException e) {
            throw new JavaFXLibraryNonFatalException("Out of table bounds: " + e.getMessage());
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Couldn't get table cell value");
        }
    }

    @RobotKeyword("Returns the Node of cell in the given table location\n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the TableView element, see "
            + "`3. Locating or specifying UI elements`. \n\n"
            + "``row`` Integer value for the row\n\n"
            + "``column`` Integer value for the column")
    @ArgumentNames({ "table", "row", "column" })
    public Object getTableCell(Object locator, int row, int column) {
        try {
            TableView table = (TableView) objectToNode(locator);
            return mapObject(getTableRowCell(table, row, column));

        } catch (ClassCastException cce) {
            throw new JavaFXLibraryNonFatalException("Unable to handle argument as TableView!");
        }
    }

    @RobotKeyword("Returns list of values of the given table column.\n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the TableView element, see "
            + "`3. Locating or specifying UI elements`. \n\n"
            + "``column`` Integer value for the column")
    @ArgumentNames({ "table", "column" })
    public List<Object> getTableColumnValues(Object locator, int column) {
        try {
            TableView table = (TableView) objectToNode(locator);
            ObservableList items = table.getItems();
            List<Object> values = new ArrayList<>();
            TableColumn tableColumn = (TableColumn) table.getColumns().get(column);

            if (tableColumn.getText() != null)
                robotLog("INFO", "Getting values from column " + tableColumn.getText());
            else
                robotLog("INFO", "Getting values from column using index " + column);

            for(Object item : items) {
                Object value = tableColumn.getCellObservableValue(item).getValue();
                values.add(mapObject(value));
            }

            return values;

        } catch (IndexOutOfBoundsException e) {
            throw new JavaFXLibraryNonFatalException("Out of table bounds: " + e.getMessage());
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Couldn't get column values: " + e.getMessage());
        }
    }

    @RobotKeyword("Returns a list of *visible* cells(Nodes) of the given table column.\n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the TableView element, see "
            + "`3. Locating or specifying UI elements`. \n\n"
            + "``column`` Integer value for the column")
    @ArgumentNames({ "table", "column" })
    public List<Object> getTableColumnCells(Object locator, int column) {
        try {
            TableView table = (TableView) objectToNode(locator);
            List<Object> columnCells = new ArrayList<>();
            VirtualFlow<?> vf = (VirtualFlow<?>) ( (TableViewSkin<?>) table.getSkin() ).getChildren().get( 1 );

            for(int i = vf.getFirstVisibleCell().getIndex(); i < vf.getLastVisibleCell().getIndex() + 1; i++) {
                robotLog("INFO", "index number: " + Integer.toString(i));
                columnCells.add(mapObject(vf.getCell(i).getChildrenUnmodifiable().get(column)));
            }

            return mapObjects(columnCells);

        } catch (ClassCastException cce) {
            throw new JavaFXLibraryNonFatalException("Unable to handle argument as TableView!");
        }
    }

    @RobotKeyword("Returns the given table row cells in a dictionary in form of name:node pairs. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the TableView element, see "
            + "`3. Locating or specifying UI elements`. \n\n"
            + "``row`` Integer value for the column"
            + "\nExample:\n"
            + "| ${row cells}= | Get Table Row Cells | \\#table-id | ${2} | \n"
            + "| Dictionary Should Contain Key | ${row cells} | column name | \n"
            + "| ${cell text}= | Get Node Text | &{row cells}[column name] | # assuming that cell is a node that has a text value |\n")
    @ArgumentNames({ "table", "row" })
    public List<Object> getTableRowValues(Object locator, int rowNumber) {
        robotLog("INFO", "Getting values from table row: " + rowNumber);
        try {
            TableView table = (TableView) objectToNode(locator);
            Object row = table.getItems().get(rowNumber);
            List<Object> values = new ArrayList<>();

            for(Object tableColumn : table.getColumns()){
                values.add( ((TableColumn)tableColumn).getCellObservableValue(row).getValue());
            }
            return values;

        } catch (ClassCastException cce){
            throw new JavaFXLibraryNonFatalException("Unable to handle argument as TableView!");
        }
    }

    @RobotKeyword("Returns the given table row cells in a dictionary in form of name:node pairs. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the TableView element, see "
            + "`3. Locating or specifying UI elements`. \n\n"
            + "``row`` Integer value for the column"
            + "\nExample:\n"
            + "| ${row cells}= | Get Table Row Cells | \\#table-id | ${2} | \n"
            + "| Dictionary Should Contain Key | ${row cells} | column name | \n"
            + "| ${cell text}= | Get Node Text | &{row cells}[column name] | # assuming that cell is a node that has a text value |\n")
    @ArgumentNames({ "table", "row" })
    public Map<String, Object> getTableRowCells(Object locator, int row) {
        robotLog("INFO", "Getting cell nodes from table row: " + row);

        try {
            TableView table = (TableView) objectToNode(locator);
            Map<String, Object> cells = new HashMap<>();

            for (int i = 0; i < table.getColumns().size(); i++){
                cells.put(getTableColumnName(table, i), mapObject(getTableRowCell(table, row, i)));
            }
            return cells;

        } catch (ClassCastException cce){
            throw new JavaFXLibraryNonFatalException("Unable to handle argument as TableView!");
        }
    }

    @RobotKeyword("Returns the column count of the given table\n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the TableView element, see "
            + "`3. Locating or specifying UI elements`. \n\n")
    @ArgumentNames({ "table" })
    public int getTableColumnCount(Object locator){
        try {
            TableView table = (TableView) objectToNode(locator);
            return table.getColumns().size();
        } catch (ClassCastException cce){
            throw new JavaFXLibraryNonFatalException("Unable to handle argument as TableView!");
        }
    }

    @RobotKeyword("Sets the screenshot directory for current application\n\n"
            + "``directory`` is a path to a folder which is to be set as current screenshot directory")
    @ArgumentNames({ "directory" })
    public void setScreenshotDirectory(String dir){
        robotLog("INFO", "Setting new screenshot directory: " + dir);
        setCurrentSessionScreenshotDirectory(dir);
    }

    @RobotKeyword("Gets the screenshot directory for current application")
    public String getScreenshotDirectory(){
        return getCurrentSessionScreenshotDirectory();
    }

    @RobotKeyword("Returns the value of the given field\n\n"
            + "``object`` is a _Object:Node_ whose property values are to be checked, see `3.2 Using objects`. \n\n"
            + "``fieldName`` is a String specifying which field value should be read")
    @ArgumentNames({ "object", "fieldName" })
    public Object getObjectProperty(Object object, String fieldName) {
        return mapObject(getFieldsValue(object, object.getClass(), fieldName));
    }

    @RobotKeyword("Prints a list of all fields and their values of the given Java object\n\n"
            + "``object`` is a _Object:Node_ whose property field values will be printed, see `3.2 Using objects`. \n\n")
    @ArgumentNames({ "object" })
    public void printObjectProperties(Object object) {
        printFields(object, object.getClass());
    }


    @RobotKeyword("Gets the max value for a given scrollbar. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the ScrollBar element, see "
            + "`3. Locating or specifying UI elements`. \n\n")
    @ArgumentNames({"locator"})
    public Double getScrollBarMaxValue(Object locator){
        try {
            ScrollBar scrollBar = (ScrollBar) objectToNode(locator);
            return scrollBar.getMax();
        } catch (ClassCastException cce) {
            throw new JavaFXLibraryNonFatalException("Given locator could not be handled as ScrollBar!", cce);
        }
    }

    @RobotKeyword("Gets the min value for a given scrollbar. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the ScrollBar element, see "
            + "`3. Locating or specifying UI elements`. \n\n")
    @ArgumentNames({"locator"})
    public Double getScrollBarMinValue(Object locator){
        try{
            ScrollBar scrollBar = (ScrollBar) objectToNode(locator);
            return scrollBar.getMin();
        } catch (ClassCastException cce) {
            throw new JavaFXLibraryNonFatalException("Given locator could not be handled as ScrollBar!", cce);
        }
    }

    @RobotKeyword("Gets the current value for a given scrollbar \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the ScrollBar element, see "
            + "`3. Locating or specifying UI elements`. \n\n")
    @ArgumentNames({"locator"})
    public Double getScrollBarValue(Object locator){
        try {
            ScrollBar scrollBar = (ScrollBar) objectToNode(locator);
            return scrollBar.getValue();
        } catch (ClassCastException cce) {
            throw new JavaFXLibraryNonFatalException("Given locator could not be handled as ScrollBar!", cce);
        }
    }

    @RobotKeyword("Returns the 'Selected' value(true/false) for given checkbox. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the CheckBox element, see "
            + "`3. Locating or specifying UI elements`. \n\n")
    @ArgumentNames({ "locator" })
    public static Boolean getCheckBoxSelection(Object locator) {

        try {
            CheckBox box = (CheckBox) objectToNode(locator);
            return box.isSelected();

        } catch (ClassCastException cce) {
            throw new JavaFXLibraryNonFatalException("Given locator could not be handled as CheckBox!", cce);
        }
    }

    @RobotKeyword("Returns the selected RadioButton Node from the same group as given locator points to.\n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the RadioButton element, see "
            + "`3. Locating or specifying UI elements`. \n\n")
    @ArgumentNames({ "locator" })
    public static Object getSelectedRadioButton(Object locator) {

        try{
            RadioButton rb = (RadioButton)objectToNode(locator);
            return (Node)rb.getToggleGroup().getSelectedToggle();

        } catch (ClassCastException cce) {
            throw new JavaFXLibraryNonFatalException("Unable to handle given locator as RadioButton!");
        }
    }


    @RobotKeyword("Returns the current value of given spinner element. \n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the Spinner element, see "
            + "`3. Locating or specifying UI elements`. \n\n")
    @ArgumentNames({ "locator" })
    public Object getSpinnerValue(Object locator) {

        try{
            Spinner spinner = (Spinner) objectToNode(locator);
            return spinner.getValueFactory().getValue();

        }catch (ClassCastException cce){
            throw new JavaFXLibraryNonFatalException("Given locator could not be handled as Spinner!", cce);
        }
    }

    @RobotKeyword("Returns a dictionary containing key:value pairs for each tab name and tab content(Node).\n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the TabPane element, see "
            + "`3. Locating or specifying UI elements`. \n\n"
            + "\nExample:\n"
            + "| ${tabs}= | Get Tab pane Tabs | \\#tab-pane-id | \n"
            + "| Dictionary Should Contain Key | ${tabs} | tab name | \n")
    @ArgumentNames({ "locator" })
    public Map<String, Object> getTabPaneTabs(Object locator) {
        robotLog("INFO", "Getting a dictionary for all tabs in TabPane: " + locator);
        try {
            TabPane tabPane = (TabPane) objectToNode(locator);
            Map<String, Object> tabs = new HashMap<>();

            int i = tabPane.getTabs().size() - 1;
            for (Node node : tabPane.getChildrenUnmodifiable()) {
                if(node.getStyleClass().contains("tab-content-area")) {
                    tabs.put(getTabHeaderText(tabPane, i), mapObject(node));
                    i--;
                }
            }

            return tabs;

        } catch (ClassCastException cce) {

            throw new JavaFXLibraryNonFatalException("Given locator: \"" + locator + "\" could not be handled as TabPane!", cce);
        }
    }

    @RobotKeyword("Returns the selected TabPane Tab as a dictionary entry in form of 'name : Node' pair.\n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the TabPane element, see "
            + "`3. Locating or specifying UI elements`. \n\n"
            + "\nExample:\n"
            + "| ${tab}= | Get Tab Pane Selected Tab | \\#pane-id | \n"
            + "| Dictionary Should contain Key | ${tab} | tab name | \n")
    @ArgumentNames({ "locator" })
    public Map<String, Object> getSelectedTabPaneTab(Object locator){
        robotLog("INFO", "Getting the selected tab from TabPane: " + locator);
        Map<String, Object> tab = new HashMap<>();

        try {
            TabPane tabPane = (TabPane) objectToNode(locator);
            tab.put(getSelectedTabName(tabPane), mapObject(getSelectedTab(tabPane)));
            return tab;

        } catch (ClassCastException cce) {
            throw new JavaFXLibraryNonFatalException("Given locator: \"" + locator + "\" could not be handled as TabPane!", cce);
        }
    }

    @RobotKeyword("Selects the given Tab from TabPane"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the TabPane element, see "
            + "`3. Locating or specifying UI elements`. \n\n"
            + "``tabName`` is the name of the tab to be selected\n"
            + "\nExamples:\n"
            + "| Select Tab Pane Tab | ${Tab Pane} | tab name | \n"
            + "| Select Tab Pane Tab | \\#tab-id | tab name | \n")
    @ArgumentNames({"locator", "tabName"})
    public void selectTabPaneTab (Object locator, String tabName){
        robotLog("INFO", "Selecting tab: \"" + tabName + "\" from TabPane: \"" + locator + "\"");
        try {
            Node headerArea = getTabPaneHeaderArea((TabPane) objectToNode(locator));

            for (Node node : headerArea.lookupAll(".tab .tab-label")) {
                if( node instanceof Labeled){
                    String tabLabel = ((Labeled)node).getText();
                    if ( tabLabel != null ) {
                        if (tabLabel.equals(tabName)) {
                            robotLog("TRACE", "Clicking on node: " + node);
                            robot.clickOn(node);
                            return;
                        }
                    }
                }
            }
            throw new JavaFXLibraryNonFatalException("Unable to find a tab with name: " + tabName);

        } catch (ClassCastException cce) {
            throw new JavaFXLibraryNonFatalException("Given locator: \"" + locator + "\" could not be handled as TabPane!", cce);
        }
    }

    @RobotKeyword("Returns the vertical value for given ScrollPane element. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the ScrollPane element, see "
            + "`3. Locating or specifying UI elements`. \n\n")
    @ArgumentNames({"locator"})
    public Double getScrollPaneVerticalValue(Object locator){
        try {
            ScrollPane pane = (ScrollPane) objectToNode(locator);
            return pane.getVvalue();
        } catch (ClassCastException cce) {
            throw new JavaFXLibraryNonFatalException("Unable to handle target as ScrollPane!");
        }
    }

    @RobotKeyword("Returns the horizontal value for given ScrollPane element. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the ScrollPane element, see "
            + "`3. Locating or specifying UI elements`. \n\n")
    @ArgumentNames({"locator"})
    public Double getScrollPaneHorizontalValue(Object locator){
        try {
            ScrollPane pane = (ScrollPane) objectToNode(locator);
            return pane.getHvalue();
        } catch (ClassCastException cce) {
            throw new JavaFXLibraryNonFatalException("Unable to handle target as ScrollPane!");
        }
    }

    @RobotKeyword("Returns the selected date from given datepicker element\n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the DatePicker element, see "
            + "`3. Locating or specifying UI elements`. \n\n"
            + "\nExample:\n"
            + "| ${date}= | Get Selected Date Picker Date | \\#datepicker-id | \n")
    @ArgumentNames({"locator"})
    public Object getSelectedDatePickerDate(Object locator) {
        try {
            DatePicker dp = (DatePicker) objectToNode(locator);
            return mapObject(dp.getValue());
        } catch (ClassCastException cce) {
            throw new JavaFXLibraryNonFatalException("Unable to handle target as DatePicker!");
        }
    }

    @RobotKeyword("Clears the text value of given TextInputControl\n\n"
            + "``locator`` is either a _query_ or _TextInputControl_ object. For identifying the element, see "
            + "`3. Locating or specifying UI elements`. \n\n"
            + "\nExample:\n"
            + "| Clear Text Input | .text-field | \n")
    @ArgumentNames({ "locator" })
    public void clearTextInput(Object locator) {
        try {
            TextInputControl input = (TextInputControl) objectToNode(locator);
            input.clear();
        } catch (ClassCastException cce) {
            throw new JavaFXLibraryNonFatalException("Unable to handle target as TextInputControl!");
        }
    }

    @RobotKeyword("Returns context menu items as a dictionary containing menu name:node pairs. \n\n"
            + "Optional parameter ``locator`` is an _Object:Window_ for specifying which contextMenu(window) items should be collected. "
            + "Default value is the last window returned by `Get Target Windows` -keyword. \n"
            + "\nExamples:\n"
            + "| Click On | \\#menu-button-id | \n"
            + "| ${menu items}= | Get Context Menu Items | \n"
            + "| Dictionary Should Contain Key | ${menu items} | menu item name"
            + "| Click On | &{menu items}[menu item name] | \n\n")
    @ArgumentNames({"locator="})
    public Map<String, Object> getContextMenuItems(Window window){
        if (!(window instanceof ContextMenu))
            throw new JavaFXLibraryNonFatalException("Unable to handle target as ContextMenu!");

        Map<String, Object> menuItems = new HashMap<>();
        Set<Node> nodes = robot.rootNode(window).lookupAll(".menu-item");

        for (Node node : nodes)
            menuItems.put(getMenuItemText(node), mapObject(node));

        return menuItems;
    }

    @RobotKeywordOverload
    public Map<String, Object> getContextMenuItems(){
        List<Window> windows = robot.listTargetWindows();
        return getContextMenuItems(windows.get(windows.size() - 1));
    }

    @RobotKeyword("Clicks the given item from menu\n\n"
            + "``item`` is the name for the Context Menu item to be clicked. This keyword clicks the first menu item that matches the given "
            + "item name. Search of an item is started from the last target window.\n\n"
            + "Example:\n"
            + "| Click On | \\#menu-button-id | \n"
            + "| Select Context Menu Item | menu item name |")
    @ArgumentNames({"item"})
    public void selectContextMenuItem(String item){
        List<Window> windows = robot.listTargetWindows();
        ListIterator li = windows.listIterator(windows.size());
        while (li.hasPrevious()) {
            Set<Node> nodes = robot.rootNode((Window)li.previous()).lookupAll(".menu-item");
            for (Node node : nodes) {
                if (getMenuItemText(node).equals(item)) {
                    robot.clickOn(node, Motion.HORIZONTAL_FIRST);
                    return;
                }
            }
        }

        throw new JavaFXLibraryNonFatalException("unable to find menu item: " + item);
    }

    @RobotKeyword("Returns the current value for given ProgressBar element. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the ToggleButton element, see "
            + " `3. Locating or specifying UI elements`. \n\n")
    @ArgumentNames({ "locator" })
    public static Object getProgressBarValue(Object locator) {
        try{
            ProgressBar pb = (ProgressBar) objectToNode(locator);
            return mapObject(pb.getProgress());

        } catch (ClassCastException cce) {
            throw new JavaFXLibraryNonFatalException("Unable to handle given locator as ProgressBar!");
        }
    }

    @RobotKeyword("Waits for current events in Fx Application Thread event queue to finish before continuing.\n\n"
            + "``timeout`` is the maximum time in seconds that the events will be waited for. If the timeout is "
            + "exceeded the keyword will fail. Default timeout is 5 seconds.\n\n")
    @ArgumentNames({ "timeout=" })
    public static void waitForEventsInFxApplicationThread(int timeout) {

        try {
            Semaphore semaphore = new Semaphore(0);
            Platform.runLater(() -> semaphore.release());
            Thread t = new Thread(() -> {
                int passed = 0;
                try {
                    while (passed <= timeout) {
                        Thread.sleep(1000);
                        passed++;
                    }

                    if (semaphore.hasQueuedThreads())
                        throw new JavaFXLibraryNonFatalException("Events did not finish within the given timeout of "
                                + timeout + " seconds.");
                } catch (InterruptedException e) {
                    throw new JavaFXLibraryNonFatalException("Timeout was interrupted in Wait For Wait For Events in " +
                            "Fx Application Thread: " + e.getMessage());
                }
            });
            t.start();
            semaphore.acquire();
        } catch (InterruptedException e) {
            throw new JavaFXLibraryNonFatalException("Wait For Events in Fx Application Thread was interrupted: "
                    + e.getMessage());
        }
    }

    @RobotKeywordOverload
    public static void waitForEventsInFxApplicationThread() {
        waitForEventsInFxApplicationThread(HelperFunctions.getWaitUntilTimeout());
    }
}