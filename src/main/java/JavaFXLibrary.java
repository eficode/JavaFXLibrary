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

import java.net.BindException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import javafxlibrary.exceptions.JavaFXLibraryFatalException;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.keywords.AdditionalKeywords.RunOnFailure;
import org.robotframework.javalib.annotation.Autowired;
import org.robotframework.javalib.library.AnnotationLibrary;
import org.robotframework.remoteserver.RemoteServer;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import static javafxlibrary.utils.HelperFunctions.*;
import static javafxlibrary.utils.TestFxAdapter.objectMap;

public class JavaFXLibrary extends AnnotationLibrary {

    public static final String ROBOT_LIBRARY_SCOPE = "GLOBAL";

    public static final String ROBOT_LIBRARY_VERSION = loadRobotLibraryVersion();

    static List<String> includePatterns = new ArrayList<String>() {{
        add("javafxlibrary/keywords/AdditionalKeywords/*.class");
        add("javafxlibrary/keywords/Keywords/*.class");
        add("javafxlibrary/keywords/*.class");
        add("javafxlibrary/tests/*.class");
	}};

    public JavaFXLibrary() {
        super(includePatterns);
        deleteScreenshotsFrom("report-images/imagecomparison");
   }

    @Autowired
    protected RunOnFailure runOnFailure;

    private void useMappedObjects(Object[] arr) {
        for(Object o : arr) {
            if(o.getClass().isArray()) {
                useMappedObjects((Object[]) o);
            } else {
                if (o instanceof String) {
                    if (objectMap.containsKey(o)) {
                        arr[Arrays.asList(arr).indexOf(o)] = objectMap.get(o);
                    }
                }
            }
        }
    }

    // overriding the run method to catch the control in case of failure, so that desired runOnFailureKeyword
    // can be executed in controlled manner.

    @Override
    public Object runKeyword(String keywordName, Object[] args) {

        useMappedObjects(args);

        try {
            return super.runKeyword(keywordName, args);
        } catch (RuntimeException e) {
            runOnFailure.runOnFailure();
            if ( e.getCause() instanceof JavaFXLibraryFatalException ) {
                robotLog("DEBUG", "JavaFXLibrary: Caught JavaFXLibrary FATAL exception");
                throw e;
            } else if ( e.getCause() instanceof JavaFXLibraryNonFatalException ) {
                robotLog("DEBUG", "JavaFXLibrary: Caught JavaFXLibrary NON-FATAL exception");
                throw e;
            } else {
                robotLog("DEBUG", "JavaFXLibrary: Caught JavaFXLibrary RUNTIME exception");
                throw e;
            }
        }
    }

    /*
    *   Just an empty function to get rid of unnecessary errors, currently get_keyword_tags interface is not implemented.
    */
    public String getKeywordTags(String keywordName) {
        return null;
    }


    @Override
    public String getKeywordDocumentation(String keywordName) {
        if (keywordName.equals("__intro__"))
            return "JavaFXLibrary is a test library for Robot Framework targeted for UI acceptance testing of JavaFX applications. "
                    + "JavaFXLibrary can be run with both Jython and Python version of Robot Framework and both in Local and Remote mode. "
                    + "In short, this library is a wrapper for TestFX tool, which is a unit test framework for testing JavaFX UI applications.\n\n"

            + "\n== 1. Preparations before running the tests ==\n"
                    + "First, JavaFXLibrary needs to be compiled and packaged. Download the repository and run _mvn package_ from the root folder. "
                    + "Second, the tested application and the JavaFXLibrary jars needs to be added to CLASSPATH. \n\n"

            + "\n== 2. Using the library ==\n"
                    + "Once the library jar -file is available, the library can be taken into use in two ways: *Local mode* with _Jython_ and *Remote mode* "
                    + "with both _Jython_ and _Python_ version of Robot Framework.\n\n"

            + "\n=== 2.1 Usage in local mode(Jython only) ===\n"
                    + "First, the JavaFXLibrary needs to be taken into use in the settings table. \n\n"
                    + "| *Settings * | *Value* |\n"
                    + "| Library | JavaFXLibrary |\n\n"

            + "\n=== 2.2 Usage in remote mode(Jython & Python) ===\n"
                    + "When using the test library in remote mode, the library needs to be started at the remote end first. This can be done as follows:\n\n"
                    + " - _java -jar JavaFXLibrary-<version>.jar_ \n\nThis will start the remote server listening at default port "
                    + "number 8270. In case there is a need for different port number, library can be started with optional parameter: \n\n"
                    + " - _java -jar JavaFXLibrary-<version>.jar 1234_ \n\nThis will start the remote server listening on port 1234.\n"
                    + "JavaFXLibrary can be taken into use as remote library in settings table as follows:\n"
                    + "| *Settings * | *Value* |\n"
                    + "| Library | Remote | http://localhost:8270/ | WITH NAME | JavaFXLibrary |\n"
                    + "Multiple JavaFXLibraries in remote mode:\n"
                    + "| *Settings * | *Value* |\n"
                    + "| Library | Remote | ip_address:8270/ | WITH NAME | my_application |\n"
                    + "| Library | Remote | ip_address:8271/ | WITH NAME | my_other_application |\n\n"

            + "\n== 3. Locating or specifying UI elements ==\n"
                    + "There are several ways of locating elements in UI. The most common way is using CSS queries, but UI elements can also "
                    + "be referenced using actual Java Objects."

            + "\n=== 3.1 Using queries ===\n"
                    + "Below a few examples of clicking a button using CSS queries: \n\r"
                    + "| Click On | some text | # using plain _text_ as locator |\n"
                    + "| Click On | .css | # using _css_ class name as locator |\n"
                    + "| Click On | \\#id | # using node _id_ as locator |\n"
                    + "Note, in case of id, # prefix needs to be escaped with \"\" back slash!\n\r"
                    + "CSS queries can also be chained together using _id_ and _css_ selectors:\n"
                    + "| Click On | \\#id .css-first .css-second | # using _id_ and _css_ class name | \n"
                    + "Above example would first try to find a node fulfilling a query using _id_, then continue search under previously found node using css class query \n"
                    + " _.css-first_, and then continue from there trying to locate css class _css-second_. \n\n"
                    + "Sometimes locating elements can be difficult in case there is a very limited amount of id's or css selector provided. In these cases, "
                    + "a special `Find With Path` -keyword might provide some extra locating power trying to mimic _xpath_ style searches:\n\r "
                    + "| ${my node}= | Find With Path | .main-view[0] .split-pane[0] \\#node-id class=GridPane .toggle-button[3] sometext | \n\r"
                    + "With this approach, user is able to give indexes([x]) to select a specific node in case there are multiple matches found. "
                    + "Also, this keyword provides a possibility to use class name based locators (class=) e.g. StackPane, GridPane etc... "

            + "\n=== 3.2 Using objects ===\n"
                    + "Elements on UI can also be referenced using actual Java Objects. Most of the keywords accepts a Node, Window, Scene, Bounds, "
                    + "Point2D, Rectangle2D or Image object as parameter for specifying which element to act on. Which object(s) is accepted as an argument "
                    + "is describer separately for each keyword in their own documentation. For Example, `Set Target Window` can take either String, "
                    + "Integer, Node or Scene as parameter for selecting a window. "
                    + "\nExample:\n"
                    + "| Set Target Window | ${1} | # this selects window based on integer value | \n"
                    + "| ${windows}= | List Windows | # this returns a list of window 'objects' | \n"
                    + "| Set Target Window | @{windows}[1] | # this selects window based on given Window object | \n"
                    + "Note! In this example the window object is actually a string in Robot Framework, but it gets converted into Window object in "
                    + "JavaFXLibrary side. Below section further clarifies this approach. "

            + "\n== 4. Argument types and return value types ==\n"
                    + "This library has a built in support for [https://github.com/ombre42/jrobotremoteserver|jrobotremoteserver], which provides a remote "
                    + "server interface for Robot Framework test libraries. This approach, however, has some limitations what comes to passing different "
                    + "[https://github.com/ombre42/jrobotremoteserver/wiki/User-Guide#Return_Types|return- and parameter types] between Robot Framework and "
                    + "Java libraries. All simple object types like Strings, Integers, Booleans etc.. remains as they are when passing them between Robot "
                    + " Framework and test libraries but in case of more complex ones, argument types are being converted into Strings. For this situation, "
                    + "JavaFXLibrary keeps internal book keeping for mapping complex objects as key:value pairs. This means that when e.g. JavaFX Node object "
                    + "is returned from library to Robot Framework as a return value, this object is mapped into internal book keeping and only the key(String) "
                    + "representation of JavaFX Node is returned. When this same key(String value) is passed back to JavaFXLibrary, it is converted back to "
                    + "actual JavaFX Node. So, even though the return values are Strings, tester is able to use them 'as if' they were actual Nodes and e.g. "
                    + "call object methods available for Nodes. \n"
                    + "Let's take an example of a table that can contain complex objects, not just simple string values:\n"
                    + "| ${table cells}= | Get Table Row Cells | \\#table-id | 2 | # table cell Nodes are stored in map and string representations are returned | \n"
                    + "| Node Should Be Enabled | @{table cells}[column 0] | | | # Library takes the string value as an argument and converts it back to Node | \n"
                    + "| Node Should Have Text | @{table cells}[column 1] | some text | | | \n"
                    + "| Click On | @{table cells}[column 2] | | | # in case this cell is clickable | \n"
                    + "| ${cell buttons}= | Find All From Node | @{table cells}[column 3] | .button  | # Finds all buttons from table cell Node | \n "
                    + "| Click On | @{cell buttons}[0] |  |  | | \n"
                    + "Most of the JavaFXLibrary keywords can use locators directly e.g. `Click On` keyword can take just css selector as an argument, but "
                    + "in some cases it can be convenient to be able to pass in a 'Node' as an argument, especially when dealing with complex data structures.\n"

            + "\n== 5. Used ENUMs ==\n"
                    + "| [https://github.com/TestFX/TestFX/blob/master/subprojects/testfx-core/src/main/java/org/testfx/robot/Motion.java|Motion] | "
                    + "DEFAULT, DIRECT, HORIZONTAL_FIRST, VERTICAL_FIRST | \n"
                    + "| [https://docs.oracle.com/javafx/2/api/javafx/scene/input/MouseButton.html|MouseButton] | MIDDLE, NONE, PRIMARY, SECONDARY | \n"
                    + "| [https://docs.oracle.com/javafx/2/api/javafx/scene/input/KeyCode.html|KeyCode] | Check the link | \n"
                    + "| [https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/TimeUnit.html|TimeUnit] | "
                    + "DAYS, HOURS, MICROSECONDS, MILLISECONDS, MINUTES, NANOSECONDS, SECONDS | \n"
                    + "| [https://docs.oracle.com/javafx/2/api/javafx/geometry/VerticalDirection.html|VerticalDirection] | UP, DOWN | \n"
                    + "| [https://docs.oracle.com/javafx/2/api/javafx/geometry/HorizontalDirection.html|HorizontalDirection] | LEFT, RIGHT | \n"
                    + "| [https://docs.oracle.com/javafx/2/api/javafx/geometry/Pos.html|Pos] | BASELINE_CENTER, BASELINE_LEFT, "
                    + "BASELINE_RIGHT, BOTTOM_CENTER, BOTTOM_LEFT, BOTTOM_RIGHT, CENTER, CENTER_LEFT, CENTER_RIGHT, TOP_CENTER, TOP_LEFT, TOP_RIGHT | \n";

        return super.getKeywordDocumentation(keywordName);
    }

    /**
     * Returns the currently active JavaFXLibrary instance.
     *
     * @return the library instance
     * @throws ScriptException - if error occurs in script
     */
    public static JavaFXLibrary getLibraryInstance() throws ScriptException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("python");
        engine.put("library", "JavaFXLibrary");
        engine.eval("from robot.libraries.BuiltIn import BuiltIn");
        engine.eval("instance = BuiltIn().get_library_instance(library)");
        return (JavaFXLibrary) engine.get("instance");
    }

    public static void main(String[] args) throws Exception {
        RemoteServer.configureLogging();
        System.out.println("-------------------- JavaFXLibrary --------------------- ");
        RemoteServer server = new RemoteServer();
        server.putLibrary("/", new JavaFXLibrary());
        int port = 8270;
        InetAddress ipAddr = InetAddress.getLocalHost();

        try {
            if(args.length > 0) {
                port = Integer.parseInt(args[0]);
            } else
                System.out.println("RemoteServer for JavaFXLibrary will be started at default port of: " + port + ". If you wish to use another port, restart the library and give port number as an argument. ");

            server.setPort(port);
            server.start();
            System.out.println("\n        JavaFXLibrary " + ROBOT_LIBRARY_VERSION + " is now available at: " + ipAddr.getHostAddress() + ":" + port + "\n ");

        } catch (NumberFormatException nfe) {
            System.out.println("\n        Error! Not a valid port number: " + args[0]);
            System.exit(1);
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
            System.exit(1);
        } catch (BindException be) {
            // TODO: Configure RemoteServer logging, stackTrace gets logged here with default config
            System.out.println("\n        Error! " + be.getMessage() + ": " + ipAddr.getHostAddress() +":" + port);
            System.exit(1);
        }
    }
}
