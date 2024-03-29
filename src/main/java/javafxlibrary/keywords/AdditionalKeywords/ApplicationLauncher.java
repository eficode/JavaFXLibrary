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
import javafx.application.Platform;
import javafxlibrary.exceptions.JavaFXLibraryFatalException;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.RobotLog;
import javafxlibrary.utils.TestFxAdapter;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileSystems;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.Semaphore;

import static javafxlibrary.utils.HelperFunctions.*;

@RobotKeywords
public class ApplicationLauncher extends TestFxAdapter {

    @RobotKeyword("Launches JavaFX application with the given arguments.\n\n"
            + "``appName`` is the name of the application to launch. \n\n"
            + "``appArgs`` is a list of arguments to be passed for the application. \n\n"
            + "Example:\n"
            + "| Launch JavaFX Application | _javafxlibrary.testapps.MenuApp_ |\n"
            + "| Launch JavaFX Application | _TestApplication.jar_ |\n")
    @ArgumentNames({"appName", "*args"})
    public void launchJavafxApplication(String appName, String... appArgs) {
        try {
            RobotLog.info("Starting application:" + appName);
            createNewSession(appName, appArgs);
            waitForEventsInFxApplicationThread(getLibraryKeywordTimeout());
            RobotLog.info("Application: " + appName + " started.");
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
            + "| Launch Swing Application | _javafxlibrary.testapps.SwingApplication_ |\n"
            + "| Launch Swing Application | _TestApplication.jar_ |\n")
    @ArgumentNames({"appName", "*args"})
    public void launchSwingApplication(String appName, String... appArgs) {
        RobotLog.info("Starting application:" + appName);
        Class mainClass = getMainClass(appName);
        Application app = createWrapperApplication(mainClass, appArgs);
        createNewSession(app);
        waitForEventsInFxApplicationThread(getLibraryKeywordTimeout());
        RobotLog.info("Application: " + appName + " started.");
    }

    @RobotKeyword("Creates a wrapper application the same way as in `Launch Swing Application`, but starts it in a new " +
            "thread. This is required when main method of the test application is blocked and execution does not " +
            "return after calling it until the application gets closed. Be sure to set the library timeout with " +
            "`Set Timeout` so that the test application will have enough time to load, as the test execution will " +
            "continue instantly after calling the main method.\n\n"
            + "``appName`` is the name of the application to launch. \n\n"
            + "``appArgs`` is a list of arguments to be passed for the application. \n\n"
            + "Example:\n"
            + "| Launch Swing Application In Separate Thread | _javafxlibrary.testapps.SwingApplication_ |\n"
            + "| Launch Swing Application In Separate Thread | _TestApplication.jar_ |\n")
    @ArgumentNames({"appName", "*args"})
    public void launchSwingApplicationInSeparateThread(String appName, String... appArgs) {
        RobotLog.info("Starting application:" + appName);
        Class c = getMainClass(appName);
        Application app = createThreadedWrapperApplication(c, appArgs);
        createNewSession(app);
        waitForEventsInFxApplicationThread(getLibraryKeywordTimeout());
        RobotLog.info("Application: " + appName + " started.");
    }

    private Class getMainClass(String appName) {
        try {
            if (appName.endsWith(".jar")) {
                return getMainClassFromJarFile(appName);
            } else {
                return Class.forName(appName);
            }
        } catch (ClassNotFoundException e) {
            throw new JavaFXLibraryNonFatalException("Unable to launch application: " + appName, e);
        }
    }

    private void addPathToClassPath(String path) {
        URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();

        RobotLog.info("Setting following path to classpath: " + path);

        try {
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(classLoader, (new File(path)).toURI().toURL());

        } catch (Exception e) {
            throw new JavaFXLibraryFatalException("Problem setting the classpath: " + path, e);
        }
    }

    @RobotKeyword("Loads given path to classpath.\n\n"
            + "``path`` is the path to add.\n\n"
            + "``failIfNotFound`` is either True or False, default False. In case of False error is written as warning.\n\n"
            + "If directory path has asterisk(*) after directory separator all jar files are added from directory.\n"
            + "\nExample:\n"
            + "| Set To Classpath | C:${/}users${/}my${/}test${/}folder | \n"
            + "| Set To Classpath | C:${/}users${/}my${/}test${/}folder${/}* | \n"
            + "| Set To Classpath | C:${/}users${/}my${/}test${/}folder2${/}* | failIfNotFound=${True} | \n")
    @ArgumentNames({"path", "failIfNotFound=False"})
    public void setToClasspath(String path, boolean failIfNotFound) {
        RobotLog.info("Setting \"" + path + "\" to classpath, failIfNotFound=\"" + failIfNotFound + "\"");
        if (path.endsWith("*")) {
            path = path.substring(0, path.length() - 1);
            RobotLog.info("Adding all jars from directory.");
            String fullPath = FileSystems.getDefault().getPath(path).normalize().toAbsolutePath().toString();

            try {
                File directory = new File(path);
                File[] fileList = directory.listFiles();
                boolean jarsFound = false;
                for (File file : Objects.requireNonNull(fileList)) {
                    if (file.getName().endsWith(".jar")) {
                        jarsFound = true;
                        addPathToClassPath(file.getAbsolutePath());
                    }
                }
                if (!jarsFound) {
                    String jarsNotFoundError = "No jar files found from classpath: " + fullPath;
                    if (failIfNotFound) {
                        throw new JavaFXLibraryNonFatalException(jarsNotFoundError);
                    } else {
                        RobotLog.warn(jarsNotFoundError);
                    }
                }
            } catch (NullPointerException e) {
                String directoryNotFoundError = "Directory not found: " + fullPath;
                if (failIfNotFound) {
                    throw new JavaFXLibraryFatalException(directoryNotFoundError);
                } else {
                    RobotLog.warn(directoryNotFoundError);
                }
            }
        } else {
            addPathToClassPath(path);
        }
    }

    @RobotKeyword("Logs current classpath content")
    public void logApplicationClasspath() {
        try {
            ClassLoader cl = ClassLoader.getSystemClassLoader();
            URL[] urls = ((URLClassLoader) cl).getURLs();
            RobotLog.info("Printing out classpaths: \n");
            for (URL url : urls) {
                RobotLog.info(url.getFile());
            }
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Unable to log application classpaths", e);
        }
    }

    @RobotKeyword("Sets system property ``name`` to ``value``. Equals command line usage `-Dname=value`.\n"
            + "\nExample:\n"
            + "| Set System Property | locale | en_US | \n")
    @ArgumentNames({"name", "value"})
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
    @ArgumentNames({"name"})
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
                RobotLog.info(key + "=" + value);
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
            RobotLog.info("Closing application...");
            deleteSession();
            RobotLog.info("Application closed.");
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Unable to close Java FX application.", e);
        }
    }

    @RobotKeyword("Closes Wrapped Swing application.\n\n"
            + "Example:\n"
            + "| Close Swing Application | \n")
    public void closeSwingApplication() {
        try {
            RobotLog.info("Closing application...");
            deleteSwingSession();
            RobotLog.info("Application closed.");
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Unable to close JavaFXLibrary Swing Wrapper application.", e);
        }
    }

    @RobotKeyword("Clears internal book keeping of all java objects.")
    public void clearObjectMap() {
        RobotLog.info("Clearing " + objectMap.size() + " objects from objectMap.");
        objectMap.clear();
    }

    @RobotKeyword("Returns the class name of currently active JavaFX Application")
    public String getCurrentApplication() {
        try {
            return getCurrentSessionApplicationName();
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Problem getting current application name.", e);
        }
    }

    @RobotKeyword("Waits for current events in Fx Application Thread event queue to finish before continuing.\n\n"
            + "``timeout`` is the maximum time in seconds that the events will be waited for. If the timeout is "
            + "exceeded the keyword will fail. Default timeout is 5 seconds.\n\n")
    @ArgumentNames({"timeout=5"})
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

    @RobotKeyword("Returns if JavaFXLibrary is started as java agent.")
    public boolean isJavaAgent() {
        return TestFxAdapter.isAgent;
    }

}
