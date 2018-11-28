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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TestDragRobotController implements Initializable {

    @FXML private Slider horizontalSlider;
    @FXML private Slider verticalSlider;
    @FXML private Circle circle;
    @FXML private Label sliderLabel;
    @FXML private Label verticalSliderLabel;
    @FXML private Label circleLabel;
    @FXML private Label circleScreenLocationLabel;
    private Stage secondStage;
    private String horizontalSliderValue;
    private String verticalSliderValue;
    private double circleInitialY;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        openNewWindow();

        // Horizontal slider listener
        horizontalSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                horizontalSliderValue = String.valueOf((int) horizontalSlider.getValue());
                sliderLabel.setText(horizontalSliderValue);
            }
        });

        // Vertical slider listener
        verticalSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                verticalSliderValue = String.valueOf((int) verticalSlider.getValue());
                verticalSliderLabel.setText(verticalSliderValue);
            }
        });

        // Circles listeners
        circle.setOnMouseDragged(event -> dragListener(event));
        circle.setOnDragDetected(event -> circle.startFullDrag());
        circle.setOnMousePressed(event -> pressListener(event));
        circle.setOnMouseReleased(event -> releaseListener(event));
        //circle.setOnMouseDragReleased(event -> dragReleaseListener(event));
    }

    private void dragReleaseListener(MouseEvent event) {
        circle.setCenterX(event.getX());
        circle.setCenterY(event.getY());
        circle.setTranslateX(event.getX());
        circle.setTranslateY(event.getY());
        circleLabel.setText("X" + (int) circle.getCenterX() + " Y" + (int) circle.getCenterY());
        circleScreenLocationLabel.setText("X" + (int) circle.getCenterX() + " Y" + (int) circle.getCenterY());
    }

    private void dragListener (MouseEvent event) {
        Circle secondCircle = (Circle) secondStage.getScene().lookup("#secondCircle");
        Stage primaryStage = (Stage) circle.getScene().getWindow();
        double xOffset = primaryStage.getX() + primaryStage.getScene().getX() + (primaryStage.getScene().getWidth() / 2);
        double yOffset = primaryStage.getY() + primaryStage.getScene().getY() + circleInitialY;
        circle.setCenterX((event.getScreenX() - xOffset));
        circle.setCenterY((event.getScreenY() - yOffset));
        circle.setTranslateX((event.getScreenX() - xOffset));
        circle.setTranslateY((event.getScreenY() - yOffset));

        circleLabel.setText("X" + (int) circle.getCenterX() + " Y" + (int) circle.getCenterY());
        circleScreenLocationLabel.setText("X" + (int) event.getScreenX() + " Y" + (int) event.getScreenY());

        if (checkCirclePosition()) {
            secondCircle.setVisible(true);
            secondCircle.setCenterX((event.getScreenX() - secondStage.getX()) / 2);
            secondCircle.setCenterY((event.getScreenY() - secondStage.getY()) / 2);
            secondCircle.setTranslateX((event.getScreenX() - secondStage.getX()) / 2);
            secondCircle.setTranslateY((event.getScreenY() - secondStage.getY()) / 2);
            circleLabel.setText("Second window");
        } else {
            secondCircle.setVisible(false);
        }
    }

    private void pressListener(MouseEvent event) {
        if(event.getButton() == MouseButton.SECONDARY) {
            circle.setScaleX(2);
            circle.setScaleY(2);
        }
    }

    private void releaseListener(MouseEvent event) {
        if(event.getButton() == MouseButton.SECONDARY) {
            circle.setScaleX(1);
            circle.setScaleY(1);
        }
    }

    private boolean checkCirclePosition() {
        Bounds windowBounds = new BoundingBox(secondStage.getX(), secondStage.getY(), secondStage.getWidth(),
                secondStage.getHeight());
        Bounds testi = circle.localToScreen(circle.getBoundsInLocal());
        return testi.intersects(windowBounds);
    }

    private void openNewWindow() {
        Parent root;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/javafxlibrary/ui/TestDragRobotSecondUI.fxml"));
            root = fxmlLoader.load();
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            secondStage = new Stage();
            secondStage.setScene(new Scene(root));
            secondStage.initStyle(StageStyle.UNDECORATED);
            secondStage.setX(screenBounds.getMinX() + screenBounds.getWidth() - 200);
            secondStage.setTitle("Second window");
            secondStage.show();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void resetCircleLocation() {
        Circle secondCircle = (Circle) secondStage.getScene().lookup("#secondCircle");
        secondCircle.setVisible(false);
        circle.setCenterX(0);
        circle.setCenterY(0);
        circle.setTranslateX(circle.getCenterX());
        circle.setTranslateY(circle.getCenterY());
        circleLabel.setText("Circle has not moved");
        circleScreenLocationLabel.setText("Circle has not moved");
    }

    // Must be called after the initialization or bounds will have wrong values
    public void afterInitialize() {
        circleInitialY = circle.localToScene(circle.getBoundsInLocal()).getMinY() + circle.getRadius();
    }
}
