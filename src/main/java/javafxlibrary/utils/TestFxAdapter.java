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

import java.io.File;
import java.util.HashMap;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import org.testfx.api.FxRobotContext;
import org.testfx.api.FxRobotInterface;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class TestFxAdapter {

    // current robot instance in use
    protected static FxRobotInterface robot;
    public static void setRobot(FxRobotInterface robot) {
        TestFxAdapter.robot = robot;
    }

    // current robot context
    protected static FxRobotContext robotContext;
    public static void setRobotContext(FxRobotContext context) {
        TestFxAdapter.robotContext = context;
    }

    // TODO: consider adding support for multiple sessions
    private static Session activeSession = null;

    // internal book keeping for objects
    public static HashMap objectMap = new HashMap();

    public void createNewSession(String appName, String... appArgs) {

        /* Applications using FXML-files for setting controllers must have
           FXMLLoader.setDefaultClassLoader(getClass().getClassLoader());
           in their start method for the controller class to load properly */
        if (appName.endsWith(".jar")) {

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
                        Class c = cl.loadClass(className);
                        activeSession = new Session(c, appArgs);
                    }

                }

            } catch (FileNotFoundException e) {
                throw new JavaFXLibraryNonFatalException("Couldn't find file: " + appName);
            } catch (ClassNotFoundException e) {
                throw new JavaFXLibraryNonFatalException("Couldn't find main application class in " + appName);
            } catch (IOException e) {
                throw new JavaFXLibraryNonFatalException(e);
            }

        } else {
            activeSession = new Session(appName, appArgs);
        }

        setRobot(activeSession.sessionRobot);
        setRobotContext(activeSession.robotContext());

    }

    public void deleteSession() {

        // application clean-up
        activeSession.closeApplication();
    }

    public String getCurrentSessionApplicationName() {
        if (activeSession != null)
            return activeSession.applicationName;
        return null;
    }

    public String getCurrentSessionScreenshotDirectory() {
        if (activeSession != null)
            return activeSession.screenshotDirectory;
        else
            throw new JavaFXLibraryNonFatalException("Unable to get screenshot directory, no application is currently open!");
    }

    public void setCurrentSessionScreenshotDirectory(String dir){

        if(activeSession != null) {
            File errDir = new File(dir);
            if(!errDir.exists())
                errDir.mkdirs();
            activeSession.screenshotDirectory = dir;
        }
        else
            throw new JavaFXLibraryNonFatalException("Unable to set screenshot directory, no application is currently open!");
    }

    public static FxRobotContext robotContext() {
        return robotContext;
    }
}
