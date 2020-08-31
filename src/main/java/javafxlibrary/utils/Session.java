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

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.concurrent.TimeoutException;

public class Session {

    public Stage primaryStage;
    public FxRobot sessionRobot;
    public Application sessionApplication;
    public String applicationName;
    public String screenshotDirectory;

    public Session(String appName, String... appArgs) {
        try {
            // start the client
            this.primaryStage = FxToolkit.registerPrimaryStage();
            this.sessionApplication = FxToolkit.setupApplication((Class)Class.forName(appName), appArgs);
            this.sessionRobot = new FxRobot();
            this.applicationName = appName;
            this.screenshotDirectory = System.getProperty("user.dir") + "/report-images/";
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
            this.screenshotDirectory = System.getProperty("user.dir") + "/report-images/";
        } catch (TimeoutException e) {
            throw new JavaFXLibraryNonFatalException("Problem launching the application: " + appClass.getSimpleName() + ", "
                    + e.getMessage(), e);
        }
    }

    public Session(Application application) {
        try {
            this.primaryStage = FxToolkit.registerPrimaryStage();
            this.sessionApplication = FxToolkit.setupApplication(() -> application);
            this.sessionRobot = new FxRobot();
            this.applicationName = "JavaFXLibrary SwingWrapper";
            this.screenshotDirectory = System.getProperty("user.dir") + "/report-images/";

        } catch (TimeoutException e) {
            throw new JavaFXLibraryNonFatalException("Problem launching the application: " + e.getMessage(), e);
        }

    }

    public void closeApplication() {
        try {
            FxToolkit.hideStage();
            FxToolkit.cleanupStages();
            sessionRobot.release(new KeyCode[] {});
            sessionRobot.release(new MouseButton[] {});
            FxToolkit.cleanupApplication(sessionApplication);
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Problem shutting down the application: " + e.getMessage(), e);
        }
    }

    public void closeSwingApplication() {
        Frame[] frames = Frame.getFrames();

        for (Frame frame : frames) {
            if (frame instanceof JFrame) {
                JFrame jFrame = (JFrame) frame;
                // EXIT_ON_CLOSE stops test execution on Jython (calls System.exit(0);)
                jFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
                jFrame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        }

        closeApplication();
    }
}
