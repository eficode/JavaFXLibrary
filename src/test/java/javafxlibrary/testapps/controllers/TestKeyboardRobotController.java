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

import javafx.scene.control.skin.TextAreaSkin;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class TestKeyboardRobotController implements Initializable {

    @FXML
    TextArea textArea;
    @FXML
    Label textAreaLabel;
    @FXML
    Label keyCombinationLabel;
    @FXML
    Button resetButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textArea.textProperty().addListener(e -> textAreaLabel.setText(textArea.getText()));

        // TAB changes focus to the button, SHIFT+TAB inserts 4 spaces into the text
        textArea.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.TAB)) {
                    textArea.setText(textArea.getText() + "    ");
                    textArea.positionCaret(textArea.getText().length());
                    event.consume();
                }
            }
        });

        keyCombinationLabel.setOnKeyPressed(evt -> keyCombinationLabelListener(evt));

        // Text can be reset with enter
        resetButton.defaultButtonProperty().bind(resetButton.focusedProperty());
    }

    // Changes keyCombinationLabels text to Passed if CTRL + SHIFT + G is pressed
    public void keyCombinationLabelListener(KeyEvent evt) {
        if (evt.getCode() == KeyCode.G && evt.isShiftDown() && evt.isControlDown()) {
            keyCombinationLabel.setText("Passed");
            keyCombinationLabel.setStyle("-fx-background-color: #00A000; -fx-text-fill: white");
        }
    }

    public void mouseListener(MouseEvent event) {
        keyCombinationLabel.requestFocus();
        // Reset keyCombinationLabel
        if (event.getButton() == MouseButton.SECONDARY) {
            keyCombinationLabel.setText("Click here and hold CTRL + SHIFT + G");
            keyCombinationLabel.setStyle("-fx-background-color: #ababab; -fx-text-fill: white");
        }
    }

    public void clearInputFields() {
        textArea.setText("");
        textAreaLabel.setText("Start typing");
    }
}
