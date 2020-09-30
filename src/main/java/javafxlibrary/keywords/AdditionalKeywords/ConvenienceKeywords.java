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
import javafxlibrary.keywords.Keywords.ClickRobot;
import javafxlibrary.keywords.Keywords.KeyboardRobot;
import javafxlibrary.utils.HelperFunctions;
import javafxlibrary.utils.RobotLog;
import javafxlibrary.utils.TestFxAdapter;
import javafxlibrary.utils.finder.XPathFinder;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;
import org.testfx.robot.Motion;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Semaphore;

import static javafxlibrary.utils.HelperFunctions.*;

@RobotKeywords
public class ConvenienceKeywords extends TestFxAdapter {

    @RobotKeyword("Brings the given stage to front\n\n"
            + "``stage`` is an Object:Stage to be set in front of others, see `3.2 Using locators as keyword arguments`. \n\n")
    @ArgumentNames({ "stage" })
    public void bringStageToFront(Stage stage) {
        RobotLog.info("Bringing following Stage to front: \"" + stage + "\"");
        try {
            robot.targetWindow(stage);
            Platform.runLater(stage::toFront);
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Unable to bring stage to front.", e);
        }
    }

    @RobotKeyword("Calls a given method for a given java object.\n\n"
            + "``object`` is a Java object retrieved using JavaFXLibrary keywords, see `3.2 Using locators as keyword arguments`.\n\n"
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
        /* Javalib Core changes all parameters to Strings after runKeywords automatic argument replacement, so arguments
           are replaced with objects from objectMap here instead. */
        object = HelperFunctions.useMappedObject(object);
        Object[] tempArgs = HelperFunctions.checkMethodArguments(arguments);
        Object[] finalArgs = HelperFunctions.useMappedObjects(tempArgs);
        Object result = callMethod(object, method, finalArgs, false);

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
        // Check callObjectMethod for info about argument replacing.
        object = HelperFunctions.useMappedObject(object);
        Object[] tempArgs = HelperFunctions.checkMethodArguments(arguments);
        Object[] finalArgs = HelperFunctions.useMappedObjects(tempArgs);
        callMethod(object, method, finalArgs, true);
    }

    @RobotKeyword("Lists methods available for given node.\n"
            + "``node`` is the Object:Node which methods to list, see `3.2 Using locators as keyword arguments`. \n\n"
            + "When working with custom components you may use this keyword to discover methods you can call "
            + "with `Call Method` keyword.\n\n"
            + "Example:\n"
            + "| List Component Methods | ${my node} |\n")
    @ArgumentNames({ "node" })
    public String[] listNodeMethods(Node node) {
        RobotLog.info("Listing all available methods for node: \"" + node + "\"");
        try {
            Class klass = node.getClass();
            ArrayList<String> list = new ArrayList<>();
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
            return list.toArray(new String[0]);
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Listing node methods failed.", e);
        }
    }

    private String getMethodDescription(Method m) {
        StringBuilder entry = new StringBuilder(m.getReturnType().getName() + " ");
        entry.append(m.getName());
        entry.append("(");
        Class[] args = m.getParameterTypes();
        for (int i = 0; i < args.length; i++) {
            entry.append(args[i].getName());
            if (i != args.length - 1)
                entry.append(", ");
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
            RobotLog.info("Printing tree structure for node: \"" + root + "\"");
            printTreeStructure((Parent) objectToNode(root));
        } catch (ClassCastException e) {
            throw new JavaFXLibraryNonFatalException(root.getClass() + " is not a subclass of javafx.scene.Parent");
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
        RobotLog.info(logger.getFxml((Parent) objectToNode(root)));
    }

    @RobotKeyword("Enables/Disables clicking outside of visible JavaFX application windows. Safe clicking is on by" +
            " default, preventing clicks outside of the tested application.\n\n" +
            "``value`` can be any of the following: on, off.\n\n"
            + "Parameter _value_ specifies whether safety should be toggled on or off")
    @ArgumentNames({ "value" })
    public void setSafeClicking(String value) {
        switch (value.toLowerCase()) {
            case "off":
                RobotLog.info("Setting safe clicking mode to OFF");
                HelperFunctions.setSafeClicking(false);
                break;
            case "on":
                RobotLog.info("Setting safe clicking mode to ON");
                HelperFunctions.setSafeClicking(true);
                break;
            default:
                throw new JavaFXLibraryNonFatalException("Unknown value: \"" + value + "\". Expected values are `on` or `off`");
        }
    }

    @RobotKeyword("Sets the time waited for nodes to become available. Keyword returns old timeout value as return "
            + "value. Default value is 5 seconds.\n\n"
            + "``timeout`` is an Integer value for timeout in seconds.\n\n"
            + "\nExample:\n"
            + "| ${old_timeout}= | Set Timeout | 20 | \n"
            + "| Click On | id=myidthatshallcomeavailable | | \n"
            + "| [Teardown] | Set Timeout | ${old_timeout} | \n")
    public Integer setTimeout(int timeout) {
        RobotLog.info("Setting timeout to " + timeout + "s");
        Integer oldTimeoutValue = getWaitUntilTimeout();
        setWaitUntilTimeout(timeout);
        return oldTimeoutValue;
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
        RobotLog.info("Switching window for: \"" + switchAmount + "\" times.");

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
            + "`3. Locating JavaFX Nodes`. \n\n"
            + "\nExample:\n"
            + "| ${states}= | Get Pseudo Class States | ${node} | \n"
            + "| Log List | ${states} | \n")
    @ArgumentNames({ "node" })
    public Set<PseudoClass> getPseudoClassStates(Object locator) {
        Node node = objectToNode(locator);

        try {
            RobotLog.info("Getting pseudoclass states for node: \"" + node + "\"");
            return node.getPseudoClassStates();
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Unable to get pseudoClassStates for node: " + node.toString());
        }
    }

    @RobotKeyword("Returns text value of the Node. \n\n"
            + "``locator`` is either a _query_ or _Object_ for a node whose getText method will be called, see "
            + "`3. Locating JavaFX Nodes`. \n\n")
    @ArgumentNames({ "locator" })
    public String getNodeText(Object locator) {
        Node node = objectToNode(locator);
        RobotLog.info("Getting text value for node: \"" + node + "\"");
        Class<? extends Node> c = node.getClass();
        try {
            return (String) c.getMethod("getText").invoke(node);
        } catch (NoSuchMethodException e) {
            throw new JavaFXLibraryNonFatalException("Get node text failed for node: " + node + ": Node has no getText method");
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Get node text failed for node: " + node, e);
        }
    }

    @RobotKeyword("Returns image name and path of the node. \n\n"
            + "``locator`` is either a _query_ or _Object_ for a node whose getHeight method will be called, see "
            + "`3. Locating JavaFX Nodes`. \n\n"
            + "Returns full image path by subsequently calling impl_getUrl -method. \n\n"
            + "Note, impl_getUrl -method is deprecated! Support for this method will be removed from Java in the future.")
    @ArgumentNames({ "node" })
    public String getNodeImageUrl(Object locator) {
        Node node = objectToNode(locator);
        RobotLog.info("Getting image url from node: \"" + node + "\"");
        try {
            Method[] methods = node.getClass().getMethods();
            for (Method m : methods) {
                if (m.getName().equals("getImage") && m.getParameterCount() == 0) {
                    RobotLog.trace("Method getImage() found. Invoking it on node: \"" + node + "\"");
                    try {
                        Object result = m.invoke(node, (Object) null);
                        Image image = (Image) result;
                        RobotLog.trace("Calling deprecated method impl_getUrl() for image: \"" + image + "\"");
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
            + "`3. Locating JavaFX Nodes`. \n\n")
    @ArgumentNames({ "node" })
    public Object getNodeParent(Object locator) {
        Node node = objectToNode(locator);

        try {
            RobotLog.info("Getting node parent object for: \"" + node + "\"");
            return mapObject(node.getParent());
        } catch (Exception e) {
            if( e instanceof JavaFXLibraryNonFatalException )
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to get node parent for node: " + node.toString(), e);
        }
    }

    @RobotKeyword("Returns the class name of a given node. \n\n"
            + "``locator`` is either a _query_ or _Object_ for a node whose getSimpleName method will be called, see "
            + "`3. Locating JavaFX Nodes`. \n\n")
    @ArgumentNames({ "locator" })
    public String getObjectClassName(Object locator) {
        Node node = objectToNode(locator);

        try {
            RobotLog.info("Getting class name for object: \"" + node + "\"");
            return node.getClass().getSimpleName();
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Unable to get class name for object: " + node.toString(), e);
        }
    }

    @RobotKeyword("Returns Scene of the given object. \n\n"
            + "``locator`` is either a _query_, a _Node_ or a _Window_, see `3.2 Using locators as keyword arguments`\n\n")
    @ArgumentNames({ "locator" })
    public Object getScene(Object locator) {
        try {
            RobotLog.info("Getting a Scene object for: \"" + locator + "\"");
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
            + "`3.2 Using locators as keyword arguments`. This keyword can be coupled with e.g. `List Windows` -keyword.\n\n")
    @ArgumentNames({ "window" })
    public String getWindowTitle(Object object) {
        RobotLog.info("Getting the window title for: \"" + object + "\"");

        try {
            Method m = object.getClass().getMethod("getTitle");
            return (String) m.invoke(object, (Object[]) null);
        } catch (NoSuchMethodException e) {
            RobotLog.info("This window type has no getTitle() -method. Returning null");
            return "";
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Unable to get title for window: " + object.toString(), e);
        }
    }

    @RobotKeyword("Returns the bounds of primary screen. \n")
    public Object getPrimaryScreenBounds() {
        try {
            RobotLog.info("Getting the primary screen bounds");
            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
            return mapObject(new BoundingBox(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight()));
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Unable to get primary screen bounds.", e);
        }
    }

    @RobotKeyword("Returns the library version from POM file")
    public String getLibraryVersion() {
        return HelperFunctions.getVersion();
    }

    @RobotKeyword("Returns the value of cell in the given location\n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the TableView element, see "
            + "`3. Locating JavaFX Nodes`. \n\n"
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
            + "`3. Locating JavaFX Nodes`. \n\n"
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
            + "`3. Locating JavaFX Nodes`. \n\n"
            + "``column`` Integer value for the column")
    @ArgumentNames({ "table", "column" })
    public List<Object> getTableColumnValues(Object locator, int column) {
        try {
            TableView table = (TableView) objectToNode(locator);
            ObservableList items = table.getItems();
            List<Object> values = new ArrayList<>();
            TableColumn tableColumn = (TableColumn) table.getColumns().get(column);

            if (tableColumn.getText() != null)
                RobotLog.info("Getting values from column " + tableColumn.getText());
            else
                RobotLog.info("Getting values from column using index " + column);

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
            + "`3. Locating JavaFX Nodes`. \n\n"
            + "``column`` Integer value for the column")
    @ArgumentNames({ "table", "column" })
    public List<Object> getTableColumnCells(Object locator, int column) {
        try {
            TableView table = (TableView) objectToNode(locator);
            List<Object> columnCells = new ArrayList<>();
            VirtualFlow<?> vf = (VirtualFlow<?>) ( (TableViewSkin<?>) table.getSkin() ).getChildren().get( 1 );

            for(int i = vf.getFirstVisibleCell().getIndex(); i < vf.getLastVisibleCell().getIndex() + 1; i++) {
                RobotLog.info("Index number: " + i);
                columnCells.add(mapObject(vf.getCell(i).getChildrenUnmodifiable().get(column)));
            }

            return mapObjects(columnCells);

        } catch (ClassCastException cce) {
            throw new JavaFXLibraryNonFatalException("Unable to handle argument as TableView!");
        }
    }

    @RobotKeyword("Returns the given table row cells in a dictionary in form of name:node pairs. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the TableView element, see "
            + "`3. Locating JavaFX Nodes`. \n\n"
            + "``row`` Integer value for the column"
            + "\nExample:\n"
            + "| ${row cells}= | Get Table Row Cells | \\#table-id | ${2} | \n"
            + "| Dictionary Should Contain Key | ${row cells} | column name | \n"
            + "| ${cell text}= | Get Node Text | &{row cells}[column name] | # assuming that cell is a node that has a text value |\n")
    @ArgumentNames({ "table", "row" })
    public List<Object> getTableRowValues(Object locator, int rowNumber) {
        RobotLog.info("Getting values from table row: " + rowNumber);
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
            + "`3. Locating JavaFX Nodes`. \n\n"
            + "``row`` Integer value for the column"
            + "\nExample:\n"
            + "| ${row cells}= | Get Table Row Cells | \\#table-id | ${2} | \n"
            + "| Dictionary Should Contain Key | ${row cells} | column name | \n"
            + "| ${cell text}= | Get Node Text | &{row cells}[column name] | # assuming that cell is a node that has a text value |\n")
    @ArgumentNames({ "table", "row" })
    public Map<String, Object> getTableRowCells(Object locator, int row) {
        RobotLog.info("Getting cell nodes from table row: " + row);

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
            + "`3. Locating JavaFX Nodes`. \n\n")
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
            + "Notice that relative paths are from current work dir of JavaFXLibrary:\n"
            + "- In case of Java Agent it comes from Application Under Test (AUT).\n"
            + "- In case of JavaFXLibrary is started with \"java -jar *\" command it uses the current working directory as source.\n"
            + "``directory`` is a path to a folder which is to be set as current screenshot directory in host where "
            + "JavaFXLibrary is run.\n\n"
            + "``logDirectory`` is a path that is put to log.html files that can be used after screenshots are moved "
            + "from target system to e.g. CI workspace. Typically this is relative path.\n\n\n"
            + "Example:\n"
            + "| Set Screenshot Directory | /Users/robotuser/output/AUT-screenshots/ | ./output/AUT-screenshots/ | \n"
            + "or\n"
            + "| Set Screenshot Directory | ./output/AUT-screenshots/ | \n")
    @ArgumentNames({ "directory", "logDirectory=" })
    public void setScreenshotDirectory(String dir, String logDir){
        RobotLog.info("Setting screenshot directory to \"" + dir + "\".");
        if (logDir != null && !logDir.isEmpty()) {
            RobotLog.info("Log directory is set to \"" + logDir + "\"");
        }
        setCurrentSessionScreenshotDirectory(dir, logDir);
    }

    @RobotKeyword("Gets the screenshot directory for current application")
    public String getScreenshotDirectory(){
        return getCurrentSessionScreenshotDirectory();
    }

    @RobotKeyword("Returns the value of the given field\n\n"
            + "``object`` is a _Object:Node_ whose property values are to be checked, see `3.2 Using locators as keyword arguments`. \n\n"
            + "``fieldName`` is a String specifying which field value should be read")
    @ArgumentNames({ "object", "fieldName" })
    public Object getObjectProperty(Object object, String fieldName) {
        return mapObject(getFieldsValue(object, object.getClass(), fieldName));
    }

    @RobotKeyword("Prints a list of all fields and their values of the given Java object\n\n"
            + "``object`` is a _Object:Node_ whose property field values will be printed, see `3.2 Using locators as keyword arguments`. \n\n")
    @ArgumentNames({ "object" })
    public void printObjectProperties(Object object) {
        printFields(object, object.getClass());
    }


    @RobotKeyword("Gets the max value for a given scrollbar. \n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the ScrollBar element, see "
            + "`3. Locating JavaFX Nodes`. \n\n")
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
            + "`3. Locating JavaFX Nodes`. \n\n")
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
            + "`3. Locating JavaFX Nodes`. \n\n")
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
            + "`3. Locating JavaFX Nodes`. \n\n")
    @ArgumentNames({ "locator" })
    public Boolean getCheckBoxSelection(Object locator) {

        try {
            CheckBox box = (CheckBox) objectToNode(locator);
            return box.isSelected();

        } catch (ClassCastException cce) {
            throw new JavaFXLibraryNonFatalException("Given locator could not be handled as CheckBox!", cce);
        }
    }

    @RobotKeyword("Returns the selected RadioButton Node from the same group as given locator points to.\n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the RadioButton element, see "
            + "`3. Locating JavaFX Nodes`. \n\n")
    @ArgumentNames({ "locator" })
    public Object getSelectedRadioButton(Object locator) {

        try{
            RadioButton rb = (RadioButton)objectToNode(locator);
            return rb.getToggleGroup().getSelectedToggle();

        } catch (ClassCastException cce) {
            throw new JavaFXLibraryNonFatalException("Unable to handle given locator as RadioButton!");
        }
    }


    @RobotKeyword("Returns the current value of given spinner element. \n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the Spinner element, see "
            + "`3. Locating JavaFX Nodes`. \n\n")
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
            + "`3. Locating JavaFX Nodes`. \n\n"
            + "\nExample:\n"
            + "| ${tabs}= | Get Tab pane Tabs | \\#tab-pane-id | \n"
            + "| Dictionary Should Contain Key | ${tabs} | tab name | \n")
    @ArgumentNames({ "locator" })
    public Map<String, Object> getTabPaneTabs(Object locator) {
        RobotLog.info("Getting a dictionary for all tabs in TabPane: " + locator);
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
            + "`3. Locating JavaFX Nodes`. \n\n"
            + "\nExample:\n"
            + "| ${tab}= | Get Tab Pane Selected Tab | \\#pane-id | \n"
            + "| Dictionary Should contain Key | ${tab} | tab name | \n")
    @ArgumentNames({ "locator" })
    public Map<String, Object> getSelectedTabPaneTab(Object locator) {
        RobotLog.info("Getting the selected tab from TabPane: " + locator);
        Map<String, Object> tab = new HashMap<>();

        try {
            TabPane tabPane = (TabPane) objectToNode(locator);
            tab.put(getSelectedTabName(tabPane), mapObject(getSelectedTab(tabPane)));
            return tab;

        } catch (ClassCastException cce) {
            throw new JavaFXLibraryNonFatalException("Given locator: \"" + locator + "\" could not be handled as TabPane!", cce);
        }
    }

    @RobotKeyword("Selects the given Tab from TabPane.\n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the TabPane element, see "
            + "`3. Locating JavaFX Nodes`. \n\n"
            + "``tabName`` is the name of the tab to be selected\n"
            + "\nExamples:\n"
            + "| Select Tab Pane Tab | ${Tab Pane} | tab name | \n"
            + "| Select Tab Pane Tab | \\#tab-id | tab name | \n")
    @ArgumentNames({"locator", "tabName"})
    public void selectTabPaneTab (Object locator, String tabName) {
        RobotLog.info("Selecting tab: \"" + tabName + "\" from TabPane: \"" + locator + "\"");
        try {
            Node headerArea = getTabPaneHeaderArea((TabPane) objectToNode(locator));

            for (Node node : headerArea.lookupAll(".tab .tab-label")) {
                if( node instanceof Labeled){
                    String tabLabel = ((Labeled)node).getText();
                    if ( tabLabel != null ) {
                        if (tabLabel.equals(tabName)) {
                            RobotLog.trace("Clicking on node: " + node);
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
            + "`3. Locating JavaFX Nodes`. \n\n")
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
            + "`3. Locating JavaFX Nodes`. \n\n")
    @ArgumentNames({"locator"})
    public Double getScrollPaneHorizontalValue(Object locator){
        try {
            ScrollPane pane = (ScrollPane) objectToNode(locator);
            return pane.getHvalue();
        } catch (ClassCastException cce) {
            throw new JavaFXLibraryNonFatalException("Unable to handle target as ScrollPane!");
        }
    }

    @RobotKeyword("Returns the selected date from given DatePicker element\n\n"
            + "``locator`` is either a _query_ or _Object:Node_ for identifying the DatePicker element, see "
            + "`3. Locating JavaFX Nodes`. \n\n"
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
            + "`3. Locating JavaFX Nodes`. \n\n"
            + "\nExample:\n"
            + "| Clear Text Input | .text-field | \n")
    @ArgumentNames({ "locator" })
    public void clearTextInput(Object locator) {
        try {
            TextInputControl textInputControl = (TextInputControl) objectToNode(locator);
            new ClickRobot().clickOn(textInputControl, "DIRECT");
            new KeyboardRobot().selectAll();
            robot.push(KeyCode.BACK_SPACE);
        } catch (ClassCastException e) {
            throw new JavaFXLibraryNonFatalException("Target is not an instance of TextInputControl!");
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
            + " `3. Locating JavaFX Nodes`. \n\n")
    @ArgumentNames({ "locator" })
    public Object getProgressBarValue(Object locator) {
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
    @ArgumentNames({ "timeout=5" })
    public void waitForEventsInFxApplicationThread(int timeout) {

        final Throwable[] threadException = new JavaFXLibraryNonFatalException[1];
        try {
            Semaphore semaphore = new Semaphore(0);
            Platform.runLater(semaphore::release);
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
            t.setUncaughtExceptionHandler((thread, e) -> threadException[0] = e);
            t.start();
            semaphore.acquire();

            if (threadException[0] != null)
                throw new JavaFXLibraryNonFatalException(threadException[0].getMessage());

        } catch (InterruptedException e) {
            throw new JavaFXLibraryNonFatalException("Wait For Events in Fx Application Thread was interrupted: "
                    + e.getMessage());
        }
    }
}