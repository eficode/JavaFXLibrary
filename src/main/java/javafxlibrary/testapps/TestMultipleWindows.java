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

package javafxlibrary.testapps;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafxlibrary.testapps.controllers.TestMultipleWindowsController;
import org.testfx.api.FxToolkit;

import java.io.IOException;

public class TestMultipleWindows extends Application {

    Stage stage;

    @Override
    public void init() throws Exception {
        FxToolkit.registerStage(() -> new Stage());
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/javafxlibrary/ui/TestMultipleWindowsUI.fxml"));
        FXMLLoader.setDefaultClassLoader(getClass().getClassLoader());
        Parent view = fxmlLoader.load();
        Scene scene = new Scene(view);
        TestMultipleWindowsController controller = fxmlLoader.getController();
        stage.setTitle("First window");
        stage.setScene(scene);

        stage.initStyle(StageStyle.DECORATED);
        stage.setX(Screen.getPrimary().getVisualBounds().getMinX());
        stage.setY(Screen.getPrimary().getVisualBounds().getMinY());
        stage.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
        stage.setHeight(Screen.getPrimary().getVisualBounds().getHeight());

        stage.getScene().setOnKeyPressed(event -> controller.keyCombinationListener(event));
        stage.getScene().setOnKeyReleased(event -> controller.keyReleaseListener(event));

        stage.show();
        stage.centerOnScreen();

        try {
            Stage secondWindow = new Stage();
            Stage thirdWindow = new Stage();
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

            // Second Window
            fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/javafxlibrary/ui/MultipleWindowsSubUIs/SecondUI.fxml"));
            Parent secondRoot = fxmlLoader.load();
            secondWindow.setScene(new Scene(secondRoot));
            secondWindow.setTitle("Second window");
            secondWindow.setX(screenBounds.getMinX() + 200);
            secondWindow.initStyle(StageStyle.DECORATED);
            secondWindow.getScene().setOnKeyPressed(event -> controller.keyCombinationListener(event));
            secondWindow.getScene().setOnKeyReleased(event -> controller.keyReleaseListener(event));
            secondWindow.show();

            // Third Window
            fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/javafxlibrary/ui/MultipleWindowsSubUIs/ThirdUI.fxml"));
            Parent thirdRoot = fxmlLoader.load();
            thirdWindow.setScene(new Scene(thirdRoot));
            thirdWindow.setTitle("Third window");
            thirdWindow.setX(screenBounds.getMinX() + 600);
            thirdWindow.initStyle(StageStyle.DECORATED);
            thirdWindow.getScene().setOnKeyPressed(event -> controller.keyCombinationListener(event));
            thirdWindow.getScene().setOnKeyReleased(event -> controller.keyReleaseListener(event));
            thirdWindow.show();

        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        FxToolkit.hideStage();
    }
}
