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

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import java.net.URL;
import java.util.ResourceBundle;

public class TestPointLocationController implements Initializable {

    private @FXML Label locationLabel;
    private SequentialTransition textTransition;
    private BoxBlur blur;
    private double blurAmount = 5.0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        locationLabel.setText("- | -");
        textTransition = new SequentialTransition();
        textTransition.getChildren().addAll(zoomIn(), zoomOut());
        blur = new BoxBlur(5.0, 5.0, 1);
    }

    public void mouseListener(MouseEvent event) {
        int x = (int) event.getSceneX();
        int y = (int) event.getSceneY();
        locationLabel.setText(x + " | " + y);
        if(textTransition.getStatus() != Animation.Status.RUNNING)
            textTransition.play();
        locationLabel.setEffect(blur);
        blurAmount = 5.0;

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                blurAmount -= 0.01;
                locationLabel.setEffect(new BoxBlur(blurAmount, blurAmount, 1));
                if(blurAmount <= 0.0) {
                    stop();
                }
            }
        };

        timer.start();
    }

    public void mouseExitedListener() {
        locationLabel.setText("- | -");
    }

    public ScaleTransition zoomIn() {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(10), locationLabel);
        scaleTransition.setToX(1.75f);
        scaleTransition.setToY(1.75f);
        scaleTransition.setCycleCount(1);
        return scaleTransition;
    }

    public ScaleTransition zoomOut() {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(10), locationLabel);
        scaleTransition.setToX(1f);
        scaleTransition.setToY(1f);
        scaleTransition.setCycleCount(1);
        return scaleTransition;
    }
}
