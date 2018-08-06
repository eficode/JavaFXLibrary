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

import javafx.application.Application;
import javafxlibrary.exceptions.JavaFXLibraryFatalException;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import javafxlibrary.utils.TestFxAdapter;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

import static javafxlibrary.utils.HelperFunctions.createWrapperApplication;
import static javafxlibrary.utils.HelperFunctions.getMainClassFromJarFile;
import static javafxlibrary.utils.HelperFunctions.robotLog;

@RobotKeywords
public class ApplicationLauncher extends TestFxAdapter {

    @RobotKeyword("Launches JavaFX application with the given arguments.\n\n"
            + "``appName`` is the name of the application to launch. \n\n"
            + "``appArgs`` is a list of arguments to be passed for the application. \n\n"
            + "Example:\n"
            + "| Launch JavaFX Application | _javafxlibrary.testapps.MenuApp_ |\n"
            + "| Launch JavaFX Application | _TestApplication.jar_ |\n")
    @ArgumentNames({"appName", "*args"})
    public void launchJavafxApplication(String appName, String... appArgs)  {
        try {
            HelperFunctions.robotLog("INFO", "Starting application:" + appName);
            createNewSession(appName, appArgs);
            HelperFunctions.robotLog("INFO", "Application: " + appName + " started.");

        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Unable to launch application: " + appName, e);
        }
    }

    @RobotKeyword("Creates a JavaFX wrapper for the given Swing application and launches it. This allows testing "
            + "Swing embedded JavaFX components. Custom wrappers can be used with Launch Javafx Application keyword, "
            + "see [https://github.com/eficode/JavaFXLibrary/blob/master/src/main/java/javafxlibrary/testapps/"
            + "SwingApplicationWrapper.java|SwingApplicationWrapper.java] for example.\n\n"
            + "``appName`` is the name of the application to launch. \n\n"
            + "``appArgs`` is a list of arguments to be passed for the application. \n\n"
            + "Example:\n"
            + "| Launch Swing Application | _javafxlibrary.testapps.SwingApplication |\n"
            + "| Launch Swing Application | _TestApplication.jar_ |\n")
    @ArgumentNames({"appName", "*args"})
    public void launchSwingApplication(String appName, String... appArgs) {
        HelperFunctions.robotLog("INFO", "Starting application:" + appName);
        Class c;

        try {
            if (appName.endsWith(".jar"))
                c = getMainClassFromJarFile(appName);
            else
                c = Class.forName(appName);

        } catch (ClassNotFoundException e) {
            throw new JavaFXLibraryNonFatalException("Unable to launch application: " + appName, e);
        }

        Application app = createWrapperApplication(c, appArgs);
        createNewSession(app);
        HelperFunctions.robotLog("INFO", "Application: " + appName + " started.");
    }

    private void _addPathToClassPath(String path) {
        URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();

        HelperFunctions.robotLog("INFO", "Setting following path to Classpath: " + path );

        try{
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(classLoader, (new File(path)).toURI().toURL() );

        } catch (Exception e) {
            throw new JavaFXLibraryFatalException("Problem setting the classpath: " + path , e);
        }
    }

    @RobotKeyword("Loads given path to classpath.\n\n"
        + "``path`` is the path to add.\n\n"
        + "If directory path has asterisk(*) after directory separator all jar files are added from directory.\n"
        + "\nExample:\n"
        + "| Set To Classpath | C:${/}users${/}my${/}test${/}folder | \n"
        + "| Set To Classpath | C:${/}users${/}my${/}test${/}folder${/}* | \n")
    @ArgumentNames({"path"})
    public void setToClasspath(String path)  {
        if (path.endsWith("*")) {
          path = path.substring(0, path.length() - 1);
          HelperFunctions.robotLog("INFO", "Adding all jars from directory: " + path );
          try {
              File directory = new File(path);
              File[] fileList = directory.listFiles();
              for (File file : fileList) {
                  if (file.getName().endsWith(".jar"))
                      _addPathToClassPath(file.getAbsolutePath());
              }
          } catch (NullPointerException e) {
              throw new JavaFXLibraryFatalException("Directory not found: " + path + "\n" + e.getMessage(), e);
          }
        }
        else {
            _addPathToClassPath(path);
        }
    }

    @RobotKeyword("Logs current classpath content")
    public void logApplicationClasspath() {
        try {
            ClassLoader cl = ClassLoader.getSystemClassLoader();
            URL[] urls = ((URLClassLoader) cl).getURLs();
            HelperFunctions.robotLog("INFO", "Printing out classpaths: \n");
            for (URL url : urls) {
                HelperFunctions.robotLog("INFO", url.getFile());
            }
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Unable to log application classpaths", e);
        }
    }

    @RobotKeyword("Sets system property ``name`` to ``value``. Equals commmand line usage `-Dname=value`.\n"
            + "\nExample:\n"
            + "| Set System Property | locale | en_US | \n")
    @ArgumentNames({ "name", "value" })
    public void setSystemProperty(String name, String value) {
        try {
            System.setProperty(name, value);
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Unable to set system property: \"" + name + "\" to value: " + value, e);
        }
    }

    @RobotKeyword("Returns given system property value.\n"
            + "``name`` is the system property name to fetch. \n"
            + "\nExample:\n"
            + "| ${locale}= | Get System Property | locale | \n")
    @ArgumentNames({ "name" })
    public String getSystemProperty(String name) {
        try {
            return System.getProperty(name);
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Unable to get system property: " + name, e);
        }
    }

    @RobotKeyword("Prints all system properties that has been set with *Set System Property* -keyword\n")
    public void logSystemProperties() {
        try {
            Properties p = System.getProperties();
            Enumeration keys = p.keys();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                String value = (String) p.get(key);
                HelperFunctions.robotLog("INFO", key + "=" + value);
            }
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Unable to log system properties", e);
        }
    }

    @RobotKeyword("Closes JavaFX application.\n\n"
            + "Example:\n"
            + "| Close JavaFX Application | \n")
    public void closeJavafxApplication() {

        try {
            HelperFunctions.robotLog("INFO", "Closing application...");
            deleteSession();
            HelperFunctions.robotLog("INFO", "Application closed.");
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Unable to close Java FX application.", e);
        }
    }

    @RobotKeyword("Closes Wrapped Swing application.\n\n"
            + "Example:\n"
            + "| Close Swing Application | \n")
    public void closeSwingApplication() {
        try {
            HelperFunctions.robotLog("INFO", "Closing application...");
            deleteSwingSession();
            HelperFunctions.robotLog("INFO", "Application closed.");
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Unable to close JavaFXLibrary Swing Wrapper application.", e);
        }
    }

    @RobotKeyword("Clears internal book keeping of all java objects.")
    public void clearObjectMap() {
        robotLog("INFO", "Clearing " + objectMap.size() + " objects from objectMap.");
        objectMap.clear();
    }


    @RobotKeyword("Returns the class name of currently active JavaFX Application")
    public String currentApplication() {
        try {
            return getCurrentSessionApplicationName();
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Problem getting current application name.", e);
        }
    }
}
