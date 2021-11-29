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
import javafx.scene.input.ScrollEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class TestScrollRobotController implements Initializable {

    @FXML
    private Label greenLabel;
    @FXML
    private Label redLabel;
    @FXML
    private Label totalDistanceVertical;
    @FXML
    private Label totalDistanceHorizontal;
    @FXML
    private Label actualDistanceVertical;
    @FXML
    private Label actualDistanceHorizontal;
    @FXML
    private Label eventsVertical;
    @FXML
    private Label eventsHorizontal;
    private int yActualAmount;
    private int xActualAmount;
    private int yTotalAmount;
    private int xTotalAmount;
    private int yEventCount;
    private int xEventCount;

    /*
        Labels:
        actualDistanceVertical      actual distance moved vertically by scrolling
        actualDistanceHorizontal    actual distance moved horizontally by scrolling
        totalDistanceVertical       total distance scrolled vertically (up & down combined)
        totalDistanceHorizontal     total distance scrolled horizontally (left & right combined)
        eventsVertical              number of mousewheel clicks recorded scrolling vertically
        eventsHorizontal            number of mousewheel clicks recorded scrolling horizontally

        Labels have a default text value "No scroll detected"
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        greenLabel.setOnScroll(evt -> verticalScrollListener(evt));
        redLabel.setOnScroll(evt -> horizontalScrollListener(evt));
    }

    public void verticalScrollListener(ScrollEvent evt) {
        if (evt.getDeltaY() != 0) {
            yActualAmount += evt.getDeltaY();
            yTotalAmount += Math.abs(evt.getDeltaY());
            yEventCount++;

            // Update texts in Labels
            greenLabel.setText(yActualAmount + " / " + yTotalAmount + " / " + yEventCount);
            actualDistanceVertical.setText(Integer.toString(yActualAmount));
            totalDistanceVertical.setText(Integer.toString(yTotalAmount));
            eventsVertical.setText(Integer.toString(yEventCount));
        }
    }

    public void horizontalScrollListener(ScrollEvent evt) {
        if (evt.getDeltaX() != 0) {
            xActualAmount += evt.getDeltaX();
            xTotalAmount += Math.abs(evt.getDeltaX());
            xEventCount++;

            // Update texts in Labels
            redLabel.setText(xActualAmount + " / " + xTotalAmount + " / " + xEventCount);
            actualDistanceHorizontal.setText(Integer.toString(xActualAmount));
            totalDistanceHorizontal.setText(Integer.toString(xTotalAmount));
            eventsHorizontal.setText(Integer.toString(xEventCount));
        }
    }

    public void resetButtonListener() {
        yActualAmount = 0;
        xActualAmount = 0;
        yTotalAmount = 0;
        xTotalAmount = 0;
        yEventCount = 0;
        xEventCount = 0;
        String def = "No scroll detected";

        // Update UI
        greenLabel.setText("Scroll vertically here!");
        redLabel.setText("Scroll horizontally here!");
        actualDistanceVertical.setText(def);
        totalDistanceVertical.setText(def);
        eventsVertical.setText(def);
        actualDistanceHorizontal.setText(def);
        totalDistanceHorizontal.setText(def);
        eventsHorizontal.setText(def);
    }
}
