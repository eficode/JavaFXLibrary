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

import java.util.Arrays;

@RobotKeywords
public class MouseRobot extends TestFxAdapter {

    @RobotKeyword("Presses and holds mouse buttons.\n\n"
            + "``buttons`` is a list of mouse buttons to press. Defaults to _PRIMARY_, see `5. Used ENUMs` for different mouse buttons. "
            + "\nExample: \n"
            + "| Press Mouse Button | PRIMARY | \n")
    @ArgumentNames({"*buttons"})
    public FxRobotInterface pressMouseButton(String... buttons) {
        try {
            RobotLog.info("Pressing mouse buttons: \"" + Arrays.asList(buttons) + "\"");
            return robot.press(HelperFunctions.getMouseButtons(buttons));
        } catch (Exception e) {
            if (e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to press mouse buttons: \"" + Arrays.toString(buttons) + "\"", e);
        }
    }

    @RobotKeyword("Releases pressed mouse buttons.\n\n"
            + "``buttons`` is a list of mouse buttons to release. Defaults to _PRIMARY_, see `5. Used ENUMs` for different mouse buttons. "
            + "\nExample: \n"
            + "| Release Mouse Button | SECONDARY | \n")
    @ArgumentNames({"*buttons"})
    public FxRobotInterface releaseMouseButton(String... buttons) {
        try {
            RobotLog.info("Releasing mouse buttons: \"" + Arrays.asList(buttons) + "\"");
            return robot.release(HelperFunctions.getMouseButtons(buttons));
        } catch (Exception e) {
            if (e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to release mouse buttons: \"" + Arrays.toString(buttons) + "\"", e);
        }
    }
}