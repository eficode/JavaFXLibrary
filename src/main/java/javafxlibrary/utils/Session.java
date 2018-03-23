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
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import java.util.concurrent.TimeoutException;

public class Session extends ApplicationTest {

    public Stage primaryStage;
    public FxRobot sessionRobot;
    public Application sessionApplication;
    public String applicationName = null;
    public String screenshotDirectory = null;

    public Session(String appName, String... appArgs) {
        try{
            // start the client
            primaryStage = FxToolkit.registerPrimaryStage();
            sessionApplication = FxToolkit.setupApplication((Class)Class.forName(appName), appArgs);
            sessionRobot = new FxRobot();
            applicationName = appName;
            screenshotDirectory = System.getProperty("user.dir") + "/report-images/";

        } catch (ClassNotFoundException e) {
            throw new JavaFXLibraryNonFatalException("Couldn't find main application class from " + appName);
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Problem launching the application: " + e.getMessage(), e);
        }
    }

    public Session(Class<Application> appClass, String... appArgs) {
        try {
            this.primaryStage = FxToolkit.registerPrimaryStage();
            this.sessionApplication = FxToolkit.setupApplication(appClass, appArgs);
            this.sessionRobot = new FxRobot();
            this.applicationName = appClass.toString();

        } catch (TimeoutException e) {
            throw new JavaFXLibraryNonFatalException("Problem launching the application: " + appClass.getSimpleName() + ", "
                    + e.getMessage(), e);
        }
    }

    public void closeApplication() {
        try {
            FxToolkit.hideStage();
            FxToolkit.cleanupStages();
            sessionRobot.release(new KeyCode[] {});
            sessionRobot.release(new MouseButton[] {});

            // If application processes are left running, use isMac() or other HelperFunctions to call cleanup.
            if(!HelperFunctions.isMac())
                FxToolkit.cleanupApplication(sessionApplication);
        } catch (Exception e){
            throw new JavaFXLibraryNonFatalException("Problem shutting down the application: " + e.getMessage(), e);
        }
    }
}
