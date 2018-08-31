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

import javafx.stage.Screen;
import javafxlibrary.keywords.Keywords.ScreenCapturing;
import javafxlibrary.utils.RobotLog;
import javafxlibrary.utils.TestFxAdapter;
import org.robotframework.javalib.annotation.RobotKeywords;

@RobotKeywords
public class RunOnFailure extends TestFxAdapter{

    // The keyword to run an failure
    private String runOnFailureKeyword = "Take Screenshot";

    // Only run keyword on failure if false
    private boolean runningOnFailureRoutine = false;


    // ##############################
    // Keywords
    // ##############################

    // No keywords yet

    // ##############################
    // Internal Methods
    // ##############################

    public void runOnFailure() {

        RobotLog.debug("Executing cleanup functions by running: " + runOnFailureKeyword);
        RobotLog.debug("runningOnFailureRoutine: " + runningOnFailureRoutine);

        if (runningOnFailureRoutine) {
            RobotLog.debug("WARNING, runOnFailureKeyword is currently being executed!");
            return;
        }

        runningOnFailureRoutine = true;

        if (robot == null) {
            RobotLog.error("FxRobot not initialized, launch test application with the library");
        } else if (TestFxAdapter.logImages) {
            RobotLog.info("JavaFxLibrary keyword has failed! Below a screenshot from erroneous situation:");
            if (robot.targetWindow() != null) {
                new ScreenCapturing().captureImage(robot.targetWindow());
            } else
                new ScreenCapturing().captureImage(Screen.getPrimary().getBounds());
        } else {
            RobotLog.info("JavaFXLibrary keyword has failed!");
            RobotLog.info("Not taking a screenshot since Set Image Logging is set to off. To enable screenshots " +
                    "on failed keywords, use keyword 'Set Image Logging    ON'");
        }

        runningOnFailureRoutine = false;
    }
}