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

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class TestPointLocationController implements Initializable {

    private @FXML Label locationLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        locationLabel.setText("- | -");
    }

    public void mouseListener(MouseEvent event) {
        int x = (int) event.getSceneX();
        int y = (int) event.getSceneY();
        locationLabel.setText(x + " | " + y);
    }

    public void mouseExitedListener() {
        locationLabel.setText("- | -");
    }
}
