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
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ResourceBundle;

public class TestScrollRobot2Controller implements Initializable {

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Label verticalScrollLocation;
    @FXML
    private Label horizontalScrollLocation;

    private ScrollBar verticalBar;
    private ScrollBar horizontalBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setupListeners() {
        // Get ScrollBars
        Object[] scrollBars = scrollPane.lookupAll(".scroll-bar").toArray();
        verticalBar = (ScrollBar) scrollBars[0];
        horizontalBar = (ScrollBar) scrollBars[1];
        NumberFormat formatter = new DecimalFormat("#0.00");

        // Add listener for verticalBar
        verticalBar.valueProperty().addListener((observable, oldValue, newValue) -> {
            if ((double) newValue == verticalBar.getMax()) {
                verticalScrollLocation.setText("max");
            } else if ((double) newValue == verticalBar.getMin()) {
                verticalScrollLocation.setText("min");
            } else {
                verticalScrollLocation.setText(formatter.format(newValue));
            }
        });

        // Add listener for horizontalBar
        horizontalBar.valueProperty().addListener((observable, oldValue, newValue) -> {
            if ((double) newValue == horizontalBar.getMax()) {
                horizontalScrollLocation.setText("max");
            } else if ((double) newValue == horizontalBar.getMin()) {
                horizontalScrollLocation.setText("min");
            } else {
                horizontalScrollLocation.setText(formatter.format(newValue));
            }
        });
    }

}
