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

import javafx.scene.control.ColorPicker;
import javafx.scene.input.KeyCode;
import org.testfx.api.FxRobotInterface;

public interface FxRobotColourPicker extends FxRobotInterface {
    /**
     * Selects a colour of the palette of the colour picker. The same colour is always selected.
     * @param picker The picker to use.
     */
    default void pickColour(final ColorPicker picker) {
        clickOn(picker).type(KeyCode.TAB).type(KeyCode.TAB).type(KeyCode.ENTER);
    }
}
