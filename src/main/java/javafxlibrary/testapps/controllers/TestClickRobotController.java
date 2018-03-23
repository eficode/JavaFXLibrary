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
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class TestClickRobotController implements Initializable {
    @FXML private Button button;
    @FXML private Button doubleClickButton;
    @FXML private Button rightClickButton;
    @FXML private Label buttonLabel;
    @FXML private Label doubleClickButtonLabel;
    @FXML private Label rightClickButtonLabel;
    @FXML private Label coordinateLabel;
    private int clickCount;
    private  int doubleClickCount;
    private int rightClickCount;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        button.setOnMouseClicked(event -> clickListener(event));
        doubleClickButton.setOnMouseClicked(event -> doubleClickListener(event));
        rightClickButton.setOnMouseClicked(event -> rightClickListener(event));
        buttonLabel.setPadding(new Insets(25,25,25,25));
    }

    public void clickListener(MouseEvent event) {
        clickCount++;
        buttonLabel.setText("Button has been clicked " + clickCount + " times.");
        showCoordinates(event);
    }

    public void doubleClickListener(MouseEvent event) {
        // Allow faster tapping
        if (event.getClickCount() % 2 == 0) {
            doubleClickCount++;
            doubleClickButtonLabel.setText("Button has been double-clicked " + doubleClickCount + " times.");
        }
        showCoordinates(event);
    }

    public void rightClickListener(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY) {
            rightClickCount++;
            rightClickButtonLabel.setText("Button has been right-clicked " + rightClickCount + " times.");
        }
        showCoordinates(event);
    }

    public void showCoordinates(MouseEvent event) {
        String prefix = "click";
        if(event.getButton() == MouseButton.SECONDARY) {
            prefix = "rightclick";
        } else if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() % 2 == 0) {
            prefix = "doubleclick";
        }
        coordinateLabel.setText(prefix + " X" + (int) event.getScreenX() + " Y" + (int) event.getScreenY());
    }

    public void resetCounters() {
        buttonLabel.setText("Button has been clicked 0 times.");
        doubleClickButtonLabel.setText("Button has been double-clicked 0 times.");
        rightClickButtonLabel.setText("Button has been right-clicked 0 times.");
        coordinateLabel.setText("");
        clickCount = 0;
        doubleClickCount = 0;
        rightClickCount = 0;
    }
}
