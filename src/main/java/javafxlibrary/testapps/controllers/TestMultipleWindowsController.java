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

package javafxlibrary.testapps.controllers;

import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class TestMultipleWindowsController implements Initializable {

    private boolean combinationPressed;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        combinationPressed = false;
    }

    public void keyCombinationListener(KeyEvent event) {
        // Close the current window when CMD + W is pressed
        if (event.isMetaDown() && event.getCode() == KeyCode.W && !combinationPressed) {
            Scene source = (Scene) event.getSource();
            source.getWindow().hide();
            combinationPressed = true;
        }
    }

    // Prevents closing multiple windows by accident
    public void keyReleaseListener(KeyEvent event) {
        if (!event.isMetaDown() || event.getCode() == KeyCode.W) {
            combinationPressed = false;
        }
    }
}
