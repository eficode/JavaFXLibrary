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
import javafx.application.Application;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import org.testfx.api.FxRobotContext;
import org.testfx.api.FxRobotInterface;

import static javafxlibrary.utils.HelperFunctions.getMainClassFromJarFile;

public class TestFxAdapter {

    // current robot instance in use
    protected static FxRobotInterface robot;
    public static void setRobot(FxRobotInterface robot) {
        TestFxAdapter.robot = robot;
    }
    public static FxRobotInterface getRobot() { return robot; }

    // current robot context
    protected static FxRobotContext robotContext;
    public static void setRobotContext(FxRobotContext context) {
        TestFxAdapter.robotContext = context;
    }

    // TODO: consider adding support for multiple sessions
    private static Session activeSession = null;

    protected static String logImages = "embedded";

    // internal book keeping for objects
    public static HashMap objectMap = new HashMap();

    public void createNewSession(String appName, String... appArgs) {

        /* Applications using FXML-files for setting controllers must have
           FXMLLoader.setDefaultClassLoader(getClass().getClassLoader());
           in their start method for the controller class to load properly */
        if (appName.endsWith(".jar")) {
            Class mainClass = getMainClassFromJarFile(appName);
            activeSession = new Session(mainClass, appArgs);
        } else {
            activeSession = new Session(appName, appArgs);
        }

        setRobot(activeSession.sessionRobot);
        setRobotContext(activeSession.robotContext());

    }

    public void createNewSession(Application application) {
        activeSession = new Session(application);
        setRobot(activeSession.sessionRobot);
        setRobotContext(activeSession.robotContext());
    }

    public void deleteSession() {
        activeSession.closeApplication();
    }

    public void deleteSwingSession() {
        activeSession.closeSwingApplication();
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
