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

package javafxlibrary.utils;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.css.PseudoClass;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.matchers.ProgressBarMatchers;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;
import org.hamcrest.Matchers;
import org.testfx.robot.Motion;
import javafx.scene.input.MouseButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.lang.*;

import javafx.scene.input.KeyCode;
import org.testfx.service.query.PointQuery;
import org.testfx.util.WaitForAsyncUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javafxlibrary.utils.TestFxAdapter.objectMap;
import static javafxlibrary.utils.TestFxAdapter.robot;
import static org.testfx.matcher.base.NodeMatchers.*;

public class HelperFunctions {

    private static boolean safeClicking = true;
    private static int waitUntilTimeout = 5;

    public static Node waitUntilExists(String target) {
        return waitUntilExists(target, waitUntilTimeout, "SECONDS");
    }

    public static Node waitUntilExists(String target, int timeout, String timeUnit) {
        robotLog("TRACE", "Waiting until target \"" + target + "\" becomes existent, timeout="
                + Integer.toString(timeout) + ", timeUnit=" + timeUnit);

        try {
            robotLog("TRACE", "setDefaultTimeout");
            Awaitility.setDefaultTimeout(timeout, getTimeUnit(timeUnit));
            robotLog("TRACE", "AtomicReference");
            AtomicReference<Node> node = new AtomicReference<>();
            robotLog("TRACE", "Awaitility");
            Awaitility.await().until(() -> {
                try {
                    robotLog("TRACE", "query");
                    node.set(robot.lookup(target).query());
                    robotLog("TRACE", "return");
                    return node.get() != null;
                } catch (Exception e) {
                    robotLog("TRACE", "Exception in waitUntilExists: " + e + "\n" + e.getCause().toString());
                    return Boolean.FALSE;
                }
            });

            robotLog("TRACE", "Node located: \"" + node.get().toString() + "\"");
            return node.get();

        } catch (ConditionTimeoutException e) {
            throw new JavaFXLibraryNonFatalException("Given element \"" + target + "\" was not found within given timeout of "
                    + Integer.toString(timeout) + " " + timeUnit);
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("waitUntilExist failed: " + e);
        }
    }

    // TODO: Take same parameters as waitUntilExists in all waitUntil methods
    public static Node waitUntilVisible(Object target, int timeout) {

        // if target is a query string, let's try to find the relevant node
        if (target instanceof String)
            target = waitUntilExists((String) target, timeout, "SECONDS");

        final Object finalTarget = target;
        robotLog("TRACE", "Waiting until target \"" + target.toString() + "\" becomes visible, timeout=" + Integer.toString(timeout));

        try {
            WaitForAsyncUtils.waitFor((long) timeout,
                    TimeUnit.SECONDS,
                    () -> Matchers.is(isVisible()).matches(finalTarget));
            return (Node) target;

        } catch (JavaFXLibraryNonFatalException nfe) {
            throw nfe;
        } catch (TimeoutException te) {
            throw new JavaFXLibraryNonFatalException("Given target \"" + target.toString() + "\" did not become visible within given timeout of "
                    + Integer.toString(timeout) + " seconds.");
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Something went wrong while waiting target to be visible: " + e.getMessage());
        }
    }

    public static Node waitUntilEnabled(Object target, int timeout) {

        if (target instanceof String)
            target = waitUntilExists((String) target, timeout, "SECONDS");

        final Object finalTarget = target;
        robotLog("TRACE", "Waiting until target \"" + target.toString() + "\" becomes enabled, timeout=" + Integer.toString(timeout));

        try {
            WaitForAsyncUtils.waitFor((long) timeout,
                    TimeUnit.SECONDS,
                    () -> Matchers.is(isEnabled()).matches(finalTarget));
            return (Node) target;

        } catch (JavaFXLibraryNonFatalException nfe) {
            throw nfe;
        } catch (TimeoutException te) {
            throw new JavaFXLibraryNonFatalException("Given target \"" + target.toString() + "\" did not become enabled within given timeout of "
                    + Integer.toString(timeout) + " seconds.");
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Something went wrong while waiting target to be enabled: " + e.getMessage());
        }
    }

    public static void waitForProgressBarToFinish(ProgressBar pb, int timeout) {
        try {
            WaitForAsyncUtils.waitFor((long) timeout,
                    TimeUnit.SECONDS,
                    () -> Matchers.is(ProgressBarMatchers.isComplete()).matches(pb));
        } catch (TimeoutException te) {
            throw new JavaFXLibraryNonFatalException("Given ProgressBar did not complete in " + Integer.toString(timeout) + " seconds!");
        }
    }

    public static Object mapObject(Object object) {
        if (object != null) {
            if (isCompatible(object))
                return object;
            String key = object.hashCode() + object.toString();
            objectMap.put(key, object);
            return key;
        }
        throw new JavaFXLibraryNonFatalException("Object was null, unable to map object!");
    }

    public static List<Object> mapObjects(Iterable objects) {
        List<Object> keys = new ArrayList<>();
        for (Object o : objects) {
            keys.add(mapObject(o));
        }
        if (keys.isEmpty())
            throw new JavaFXLibraryNonFatalException("List was empty, unable to map anything!");
        return keys;
    }

    public static Object callMethod(Object o, String method, boolean runLater) {
        robotLog("INFO", "Calling method " + method + " of object " + o);
        Class c = o.getClass();
        try {
            Method m = c.getMethod(method, null);
            try {
                if (!runLater) {
                    return m.invoke(o, null);
                } else {
                    Platform.runLater(() -> {
                        try {
                            m.invoke(o, null);
                        } catch (InvocationTargetException | IllegalAccessException e) {
                            throw new JavaFXLibraryNonFatalException("Couldn't execute Call Method: " +
                                    e.getCause().getMessage());
                        }
                    });
                }
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new JavaFXLibraryNonFatalException("Couldn't execute Call Method: " + e.getCause().getMessage());
            }
        } catch (NoSuchMethodException e) {
            throw new JavaFXLibraryNonFatalException(c + " has no method \"" + method + "()\"");
        } catch (JavaFXLibraryNonFatalException e) {
            throw e;
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Couldn't execute Call Method: " + e.getMessage());
        }
        return null;
    }

    public static Object callMethod(Object o, String method, List<Object> arguments, List<Object> argumentTypes, boolean runLater) {
        robotLog("INFO", "Calling method \"" + method + "\" of object \"" + o +
                "\" with arguments \"" + Arrays.toString(arguments.toArray()) + "\"");
        Class c = o.getClass();
        List<Class> aTypes = new ArrayList<>();

        for (Object className : argumentTypes)
            aTypes.add(parseClass((String) className));

        try {
            Method m = c.getMethod(method, aTypes.toArray(new Class[aTypes.size()]));
            if (!runLater) {
                try {
                    return m.invoke(o, arguments.toArray());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new JavaFXLibraryNonFatalException("Couldn't execute Call Method: " + e.getMessage());
                }
            } else {
                Platform.runLater(() -> {
                    try {
                        m.invoke(o, arguments.toArray());
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new JavaFXLibraryNonFatalException("Couldn't execute Call Method: " + e.getMessage());
                    }
                });
            }
        } catch (NoSuchMethodException e) {
            throw new JavaFXLibraryNonFatalException(c + " has no method \"" + method + "\" with arguments " + Arrays.toString(aTypes.toArray()));
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Couldn't execute Call Method: " + e.getMessage(), e);
        }
        return null;
    }

    public static ArrayList<Node> getAllNodes(Parent root) {
        ArrayList<Node> nodes = new ArrayList<>();
        addAllDescendents(root, nodes);
        return nodes;
    }

    private static void addAllDescendents(Parent parent, ArrayList<Node> nodes) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            nodes.add(node);
            if (node instanceof Parent)
                addAllDescendents((Parent) node, nodes);
        }
    }

    public static void printTreeStructure(Parent root) {
        StringBuilder sb = new StringBuilder();
        sb.append("*HTML* <ul>");
        printDescendents(root, sb);
        sb.append("</ul>");
        System.out.println(sb.toString());
    }

    private static void printDescendents(Parent parent, StringBuilder sb) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            sb.append("<details open><summary>");
            sb.append(node.getTypeSelector());
            sb.append("</summary>");
            sb.append(node.toString());

            if (node instanceof Parent) {
                Parent subParent = (Parent) node;

                if (!subParent.getChildrenUnmodifiable().isEmpty()) {
                    sb.append("<ul>");
                    printDescendents(subParent, sb);
                    sb.append("</ul>");
                }
            }

            sb.append("</details>");
        }
    }

    public static void robotLog(String level, String message) {
        if (!level.toUpperCase().matches("INFO|DEBUG|TRACE|WARN|ERROR"))
            throw new IllegalArgumentException("Unsupported log level \"" + level + "\": Accepted levels are INFO, " +
                    "DEBUG, TRACE, WARN and ERROR.");
        System.out.println("*" + level.toUpperCase() + "* " + message);
    }


    // https://github.com/TestFX/TestFX/blob/master/subprojects/testfx-core/src/main/java/org/testfx/robot/Motion.java
    public static Motion getMotion(String motion) {
        try {
            return Motion.valueOf(motion);
        } catch (IllegalArgumentException e) {
            throw new JavaFXLibraryNonFatalException("\"" + motion + "\" is not a valid Motion. Accepted values are: "
                    + Arrays.asList(Motion.values()));
        }
    }

    // https://docs.oracle.com/javafx/2/api/javafx/scene/input/MouseButton.html
    public static MouseButton[] getMouseButtons(String[] slist) {
        MouseButton[] array = new MouseButton[slist.length];
        for (int i = 0; i < slist.length; i++) {
            try {
                array[i] = MouseButton.valueOf(slist[i]);
            } catch (IllegalArgumentException e) {
                throw new JavaFXLibraryNonFatalException("\"" + slist[i] + "\" is not a valid MouseButton. Accepted values are: "
                        + Arrays.asList(MouseButton.values()));
            }
        }
        return array;
    }

    // https://docs.oracle.com/javafx/2/api/javafx/scene/input/KeyCode.html
    public static KeyCode[] getKeyCode(String[] keyList) {
        KeyCode[] array = new KeyCode[keyList.length];
        for (int i = 0; i < keyList.length; i++) {
            try {
                array[i] = KeyCode.valueOf(keyList[i]);
            } catch (IllegalArgumentException e) {
                throw new JavaFXLibraryNonFatalException("\"" + keyList[i] + "\" is not a valid Keycode. Accepted values are: "
                        + Arrays.asList(KeyCode.values()));
            }
        }
        return array;
    }

    // https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/TimeUnit.html
    public static TimeUnit getTimeUnit(String timeUnit) {
        try {
            return TimeUnit.valueOf(timeUnit);
        } catch (IllegalArgumentException e) {
            throw new JavaFXLibraryNonFatalException("\"" + timeUnit + "\" is not a valid TimeUnit. Accepted values are: "
                    + Arrays.asList(TimeUnit.values()));
        }
    }

    // https://docs.oracle.com/javafx/2/api/javafx/geometry/VerticalDirection.html
    // Macs require inverted value regardless of the natural scroll setting, so the opposite direction is returned for them
    public static VerticalDirection getVerticalDirection(String direction) {
        if (isMac()) {
            switch (direction) {
                case "UP":
                    direction = "DOWN";
                    break;
                case "DOWN":
                    direction = "UP";
                    break;
            }
        }
        try {
            return VerticalDirection.valueOf(direction);
        } catch (IllegalArgumentException e) {
            throw new JavaFXLibraryNonFatalException("Direction: \"" + direction + "\" is not a valid direction. Accepted values are: "
                    + Arrays.asList(VerticalDirection.values()));
        }
    }


    // https://docs.oracle.com/javafx/2/api/javafx/geometry/HorizontalDirection.html
    // Macs require inverted value regardless of the natural scroll setting, so the opposite direction is returned for them
    public static HorizontalDirection getHorizontalDirection(String direction) {
        if (isMac()) {
            switch (direction) {
                case "LEFT":
                    direction = "RIGHT";
                    break;
                case "RIGHT":
                    direction = "LEFT";
                    break;
            }
        }
        try {
            return HorizontalDirection.valueOf(direction);
        } catch (IllegalArgumentException e) {
            throw new JavaFXLibraryNonFatalException("Direction: \"" + direction + "\" is not a valid direction. Accepted values are: "
                    + Arrays.asList(HorizontalDirection.values()));
        }
    }

    // https://docs.oracle.com/javafx/2/api/javafx/geometry/Pos.html
    public static Pos getPosition(String position) {
        try {
            return Pos.valueOf(position);
        } catch (IllegalArgumentException e) {
            throw new JavaFXLibraryNonFatalException("Position: \"" + position + "\" is not a valid position. Accepted values are: "
                    + Arrays.asList(Pos.values()));
        }
    }

    // Returns true if operating system is Windows
    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    // Returns true if operating system is Mac
    public static boolean isMac() {
        return System.getProperty("os.name").toLowerCase().contains("mac");
    }

    // Returns true if operating system is Unix
    public static boolean isUnix() {
        String osName = System.getProperty("os.name").toLowerCase();
        return osName.contains("nix") || osName.contains("nux") || osName.contains("aix");
    }

    public static void sleepFor(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteScreenshotsFrom(String path) {
        try {
            File directory = new File(path);
            File[] fileList = directory.listFiles();
            for (File file : fileList) {
                if (file.getName().endsWith(".png"))
                    file.delete();
            }
        } catch (NullPointerException e) {
            System.out.println("No directory found at " + path);
        }
    }

    public static void deleteScreenshotsFrom(String path, String regex) {
        try {
            File directory = new File(path);
            File[] fileList = directory.listFiles();
            for (File file : fileList) {
                if (file.getName().endsWith(".png") && file.getName().contains(regex))
                    file.delete();
            }
        } catch (NullPointerException e) {
            System.out.println("No directory found at " + path);
        }
    }

    public static void setSafeClicking(boolean value) {
        safeClicking = value;
    }

    public static void setWaitUntilTimeout(int value) {
        waitUntilTimeout = value;
    }

    public static void checkClickLocation(int x, int y) {
        checkClickLocation(new Point2D(x, y));
    }

    public static void checkClickLocation(Object object) {

        robotLog("TRACE", "Checking if target \"" + object.toString() + "\" is within active window");

        if (safeClicking) {

            Point2D point = getCenterPoint(objectToBounds(object));

            if (!visibleWindowsContain(robot.listWindows(), point)) {
                throw new JavaFXLibraryNonFatalException("Can't click " + object.getClass().getSimpleName() + " at [" + point.getX() +
                        ", " + point.getY() + "]: out of window bounds. " +
                        "To enable clicking outside of visible window bounds use keyword SET SAFE CLICKING | OFF");
            }
        }
        robotLog("TRACE", "Target location checks out OK, it is within active window");
    }

    public static Object checkClickTarget(Object target) {
        try {

            if (target instanceof String || target instanceof Node)
                target = waitUntilEnabled(waitUntilVisible(target, waitUntilTimeout), waitUntilTimeout);

            checkClickLocation(target);
            return target;

        } catch (Exception e) {
            if (e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Click target check failed: " + e.getMessage());
        }
    }

    // Returns true if given point is located inside a visible window
    public static boolean visibleWindowsContain(List<Window> windows, Point2D point) {
        boolean contains = false;
        for (Window window : windows) {
            if (window.isShowing()) {
                Bounds windowBounds = new BoundingBox(window.getX(), window.getY(), window.getWidth(), window.getHeight());
                if (windowBounds.contains(point))
                    contains = true;
            }
        }
        return contains;
    }

    public static Point2D getCenterPoint(Bounds bounds) {
        robotLog("TRACE", "Getting center point for " + bounds);
        return new Point2D(bounds.getMinX() + (bounds.getWidth() / 2), bounds.getMinY() + (bounds.getHeight() / 2));
    }

    public static boolean isCompatible(Object o) {
        return (o instanceof Integer || o instanceof Double || o instanceof Long ||
                o instanceof Float || o instanceof Character || o instanceof Boolean ||
                o instanceof Byte || o instanceof Short || o instanceof Void ||
                o instanceof String || o instanceof List);
    }

    public static Class<?> parseClass(String className) {
        switch (className) {
            case "boolean":
                return boolean.class;
            case "byte":
                return byte.class;
            case "char":
                return char.class;
            case "double":
                return double.class;
            case "float":
                return float.class;
            case "int":
                return int.class;
            case "long":
                return long.class;
            case "short":
                return short.class;
            case "void":
                return void.class;
            default:
                try {
                    return Class.forName(className);
                } catch (ClassNotFoundException e) {
                    throw new JavaFXLibraryNonFatalException("Could not parse class \"" + className + "\"");
                }
        }
    }

    public static String loadRobotLibraryVersion() {
        try {
            MavenXpp3Reader reader = new MavenXpp3Reader();
            Model model = reader.read(new FileReader("pom.xml"));
            return model.getVersion();
        } catch (Exception e) {
            return "unknown";
        }
    }

    public static Node objectToNode(Object target) {

        if (target instanceof String)
            return waitUntilExists((String) target, waitUntilTimeout, "SECONDS");
        else if (target instanceof Node) {
            return (Node) target;
        } else if (target == null) {
            throw new JavaFXLibraryNonFatalException("Target object was null");
        } else
            throw new JavaFXLibraryNonFatalException("Given target \"" + target.getClass().getName() +
                    "\" is not an instance of Node or a query string for node!");
    }

    public static Bounds objectToBounds(Object object) {
        if (object instanceof Window) {
            Window window = (Window) object;
            return new BoundingBox(window.getX(), window.getY(), window.getWidth(), window.getHeight());
        } else if (object instanceof Scene) {
            Scene scene = (Scene) object;
            double x = scene.getX() + scene.getWindow().getX();
            double y = scene.getY() + scene.getWindow().getY();
            return new BoundingBox(x, y, scene.getWidth(), scene.getHeight());
        } else if (object instanceof Point2D) {
            return robot.bounds((Point2D) object).query();
        } else if (object instanceof Node) {
            return robot.bounds((Node) object).query();
        } else if (object instanceof String) {
            waitUntilExists((String) object, waitUntilTimeout, "SECONDS");
            Node node = robot.lookup((String) object).query();
            return robot.bounds(node).query();
        } else if (object instanceof Bounds) {
            return (Bounds) object;
        } else if (object instanceof PointQuery) {
            return robot.bounds(((PointQuery) object).query()).query();
        } else if (object instanceof Rectangle2D) {
            Rectangle2D r2 = (Rectangle2D) object;
            return new BoundingBox(r2.getMinX(), r2.getMinY(), r2.getWidth(), r2.getHeight());
        } else
            throw new JavaFXLibraryNonFatalException("Unsupported parameter type: " + object.getClass().getName());
    }

    private static String remainingQueries(String query) {
        String[] queries = query.split(" ", 2);
        String currentQuery = queries[0];
        if (currentQuery.equals(query))
            return null;
        else
            return queries[1];
    }


    public static Node findNode(Node node, String query) {

        robotLog("INFO", "finding from node: " + node.toString() + " with query: " + query);

        if (query != null) {
            List<Node> nodelist = new ArrayList<>();

            String currentQuery = query.split(" ", 2)[0];
            String nextQuery = remainingQueries(query);
            robotLog("INFO", "currentQuery: " + currentQuery + ", nextQuery: " + nextQuery);

            if (currentQuery.startsWith("class=")) {
                nodelist.addAll(node.lookupAll((getQueryString(currentQuery)).replaceFirst("^class=", "")));
            } else {
                nodelist.addAll(robot.from(node).lookup(getQueryString(currentQuery)).queryAll());
            }

            if (nodelist.isEmpty()) {
                return null;
            } else {
                if (getQueryIndex(currentQuery) != -1) {
                    // if index [..] was given, continue search with only one match
                    return findNode(nodelist.get(getQueryIndex(currentQuery)), nextQuery);
                } else {
                    // no index given, continue search with all matches
                    Node newNode = null;

                    for (Node n : nodelist) {
                        newNode = findNode(n, nextQuery);
                        if (newNode != null)
                            return newNode;
                    }
                    return null;
                }
            }
        } else {
            return node;
        }
    }

    public static Node findNode(String query) {
//		return findNode(robot.lookup(getQueryString(query.split(" ", 2)[0])).query(), query.split(" ", 2)[1]);
        return findNode(robot.listTargetWindows().get(0).getScene().getRoot(), query);
    }

    public static String getQueryString(String query) {
        return query.replaceAll("\\[\\d]$", "");
    }

    public static int getQueryIndex(String query) {
        Pattern pattern = Pattern.compile(".*\\[\\d]$");
        Matcher matcher = pattern.matcher(query);
        if (matcher.matches()) {
            String index = StringUtils.substringBetween(query, "[", "]");
            return Integer.parseInt(index);
        }
        return -1;
    }

    public static Node getHoveredNode() {
        return getHoveredNode(robot.listTargetWindows().get(0).getScene().getRoot());
    }

    public static Node getHoveredNode(Parent root) {
        robotLog("DEBUG", "Checking parent: " + root);
        for (Node node : root.getChildrenUnmodifiable()) {
            robotLog("DEBUG", "Checking parent child: " + node);
            if (node.isHover()) {
                robotLog("DEBUG", "Parent child is hovered: " + node);
                if (node instanceof Parent)
                    return getHoveredNode((Parent) node);
                else {
                    robotLog("DEBUG", "Last hovered node in the chain has been found: " + node);
                    return node;
                }
            }
        }
        robotLog("DEBUG", "Last hovered node in the chain has been found: " + root);
        return root;
    }

    public static Object getFieldsValue(Object o, Class c, String fieldName) {

        try {
            Field[] fields = c.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getName().equals(fieldName)) {
                    return field.get(o);
                }
            }

            if (c.getSuperclass() != null) {
                return getFieldsValue(o, c.getSuperclass(), fieldName);
            }

        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Couldn't get value of field");
        }
        throw new JavaFXLibraryNonFatalException("Couldn't get value of field");
    }

    public static void printFields(Object o, Class c) {

        try {
            Field[] fields = c.getDeclaredFields();

            if (fields.length > 0) {
                System.out.println("*HTML*Fields from class <b>" + c.getSimpleName() + "</b>:");
                System.out.println("<ul>");

                for (Field field : fields) {
                    field.setAccessible(true);
                    System.out.println("<li>" + field.getName() + " : " + field.get(o) + "</li>");
                }
                System.out.println("</ul>");
                System.out.println("");
            }

            if (c.getSuperclass() != null) {
                printFields(o, c.getSuperclass());
            }

        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Couldn't get value of field");
        }
    }

    public static String getMenuItemText(Node menuNode) {
        for (Node node : menuNode.lookupAll(".label")) {
            if (node instanceof Labeled) {
                if (!((Labeled) node).getText().equals(""))
                    return ((Labeled) node).getText();
            }
        }
        throw new JavaFXLibraryNonFatalException("Unable to find menutext!");
    }

    public static String getTabHeaderText(TabPane tabPane, int index) {

        int i = 0;
        for (Node node : getTabPaneHeaderArea(tabPane).lookupAll(".tab")) {
            if (i == index)
                return getTabName(node);
            i++;
        }
        throw new JavaFXLibraryNonFatalException("Unable to find tabtext!");
    }

    public static Node getTabPaneHeaderArea(TabPane tabPane) {

        for (Node tabHeaderArea : tabPane.getChildrenUnmodifiable()) {
            if (tabHeaderArea.getStyleClass().contains("tab-header-area"))
                return tabHeaderArea;
        }
        throw new JavaFXLibraryNonFatalException("Given tabPane does not contain tab-header-area!");
    }

    public static String getSelectedTabName(TabPane tabPane) {
        robotLog("INFO", "Getting selected tab name");
        for (Node node : getTabPaneHeaderArea(tabPane).lookupAll(".tab")) {
            robotLog("INFO", "checking the name for: " + node);
            robotLog("INFO", "Styles: " + Arrays.asList(node.getPseudoClassStates()));

            if (node.getPseudoClassStates().stream().map(PseudoClass::getPseudoClassName).anyMatch("selected"::contains))
                return getTabName(node);
        }
        throw new JavaFXLibraryNonFatalException("Unable to get tab name");
    }

    public static String getTabName(Node node) {
        robotLog("INFO", "Getting tab name for: " + node);
        for (Node label : node.lookupAll(".tab-label")) {
            if (label instanceof Labeled) {
                String labelText = ((Labeled) label).getText();
                if (labelText != null && !labelText.equals("")) {
                    return labelText;
                }
            }
        }
        throw new JavaFXLibraryNonFatalException("given tab has no name!");
    }

    public static Node getSelectedTab(TabPane tabPane) {
        robotLog("INFO", "Getting selected tab");
        for (Node node : tabPane.getChildrenUnmodifiable()) {
            if (node.getStyleClass().contains("tab-content-area")) {
                if (node.isVisible())
                    return node;
            }
        }
        throw new JavaFXLibraryNonFatalException("Unable to get selected Tab!");
    }

    public static String getTableColumnName(TableView table, int index) {

        for (Node node : robot.from(table).lookup(".column-header-background .table-column").nth(index).lookup(".label").queryAll()) {
            if (node instanceof Labeled) {
                String columnName = ((Labeled) node).getText();
                if (columnName != null && !columnName.equals(""))
                    return columnName;
            }
        }
        // Return empty string if really nothing found
        return "";
    }

    public static Node getTableRowCell(TableView table, int row, int cell) {
        return robot.from(table).lookup(".table-row-cell").nth(row).lookup(".table-cell").nth(cell).query();
    }

    public static Class getMainClassFromJarFile(String appName) {
        try {
            JarFile jarFile = new JarFile(appName);
            String mainClassName = jarFile.getManifest().getMainAttributes().getValue("Main-Class");
            Enumeration<JarEntry> e = jarFile.entries();
            URL[] urls = {new URL("jar:file:" + appName + "!/")};
            URLClassLoader cl = URLClassLoader.newInstance(urls);

            while (e.hasMoreElements()) {
                JarEntry je = e.nextElement();

                if (je.isDirectory() || !je.getName().endsWith(".class"))
                    continue;

                String className = je.getName().substring(0, je.getName().length() - 6);
                className = className.replace('/', '.');

                if (className.equals(mainClassName)) {
                    return cl.loadClass(className);
                }
            }

            throw new ClassNotFoundException();

        } catch (FileNotFoundException e) {
            throw new JavaFXLibraryNonFatalException("Couldn't find file: " + appName);
        } catch (ClassNotFoundException e) {
            throw new JavaFXLibraryNonFatalException("Couldn't find main application class in " + appName);
        } catch (IOException e) {
            throw new JavaFXLibraryNonFatalException(e);
        }
    }

    public static Application createWrapperApplication(Class c, String... appArgs) {

        try {
            Method main = c.getMethod("main", String[].class);
            return new Application() {
                @Override
                public void start(Stage primaryStage) {
                    try {
                        main.invoke(null, (Object) appArgs);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new JavaFXLibraryNonFatalException("Unable to launch application: " + c.getName(), e);
                    }
                }
            };
        } catch (NoSuchMethodException e) {
            throw new JavaFXLibraryNonFatalException("Couldn't create wrapper application for " + c.getName(), e);
        }
    }
}


