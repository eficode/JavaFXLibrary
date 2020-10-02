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

package javafxlibrary.keywords.Keywords;

import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import javafxlibrary.utils.RobotLog;
import javafxlibrary.utils.TestFxAdapter;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;
import org.testfx.api.FxRobotInterface;

@RobotKeywords
public class PointPosition extends TestFxAdapter {

    @RobotKeyword("Stores the given position as the default offset for all point operations.\n\n"
            + "``pointPosition`` sets the default offset for every use of `Point To` -keyword. Defaults to _CENTER_, "
            + "see more at `5. Used ENUMs` and _Pos_ enum. \n\n"
            + "\nExample: \n"
            + "| Set Target Position | TOP_LEFT | \n")
    @ArgumentNames({ "pointPosition" })
    public FxRobotInterface setTargetPosition(String pointPosition) {
        try {
            RobotLog.info("Setting new target position as: \"" + pointPosition + "\"");
            return robot.targetPos(HelperFunctions.getPosition(pointPosition));
        } catch (Exception e) {
            if (e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to set target position: \"" + pointPosition + "\"", e);
        }
    }

}