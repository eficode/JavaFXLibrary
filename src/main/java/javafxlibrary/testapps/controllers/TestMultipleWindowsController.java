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

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TestMultipleWindowsController implements Initializable {

    private boolean combinationPressed;
    private Stage secondWindow;
    private Stage thirdWindow;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        openOtherWindows();
        combinationPressed = false;
    }

    private void openOtherWindows() {
        Parent root;
        try {
            secondWindow = new Stage();
            thirdWindow = new Stage();
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

            // Load FXML for secondWindow
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                    "/fxml/javafxlibrary/ui/MultipleWindowsSubUIs/SecondUI.fxml"));
            root = fxmlLoader.load();

            // Second window settings
            secondWindow.setScene(new Scene(root));
            secondWindow.setTitle("Second window");
            secondWindow.setX(screenBounds.getMinX() + 200);
            secondWindow.initStyle(StageStyle.DECORATED);
            secondWindow.getScene().setOnKeyPressed(event -> keyCombinationListener(event));
            secondWindow.getScene().setOnKeyReleased(event -> keyReleaseListener(event));
            secondWindow.show();

            // Load FXML for thirdWindow
            fxmlLoader = new FXMLLoader(getClass().getResource(
                    "/fxml/javafxlibrary/ui/MultipleWindowsSubUIs/ThirdUI.fxml"));
            root = fxmlLoader.load();

            // Third window settings
            thirdWindow.setScene(new Scene(root));
            thirdWindow.setTitle("Third window");
            thirdWindow.setX(screenBounds.getMinX() + 600);
            thirdWindow.initStyle(StageStyle.DECORATED);
            thirdWindow.getScene().setOnKeyPressed(event -> keyCombinationListener(event));
            thirdWindow.getScene().setOnKeyReleased(event -> keyReleaseListener(event));
            thirdWindow.show();

        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
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
