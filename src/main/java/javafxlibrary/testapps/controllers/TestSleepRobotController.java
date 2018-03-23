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

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class TestSleepRobotController implements Initializable {

    /*
        * Use totalMillis, totalSeconds and totalMinutes to get the elapsed time in same units that the test case is using
        * Button can be used by pressing Enter too, but mouseButtons seem to have more consistent results
     */

    @FXML private Label timeLabel;
    @FXML private Button toggleButton;
    @FXML private Label totalMillis;
    @FXML private Label totalSeconds;
    @FXML private Label totalMinutes;

    private long startTime;
    private volatile boolean isRunning;
    private Thread t;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        isRunning = false;
    }

    public void toggleTimer() {

        if (!isRunning) {
            startTime = System.nanoTime();
            isRunning = true;
            initializeGUIupdateThread();
            t.start();
            toggleButton.setText("Stop");
        } else {
            long endTime = System.nanoTime();
            isRunning = false;
            updateTimeStats((endTime - startTime) / 1000000);
        }
    }

    private void initializeGUIupdateThread() {
        t = new Thread(() -> {
            while(isRunning) {
                try {
                    // Update GUI in main JavaFX Thread
                    Platform.runLater(() -> {
                        if(isRunning)
                            updateTimeStats((System.nanoTime() - startTime) /1000000);
                    });
                    Thread.sleep(1);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Return time elapsed as a String in HH:MM:SS:mmm-format
    private String formatElapsedTime(long timeInMilliseconds) {
        return String.format("%02d:%02d:%02d:%03d",
                TimeUnit.MILLISECONDS.toHours(timeInMilliseconds),
                TimeUnit.MILLISECONDS.toMinutes(timeInMilliseconds) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeInMilliseconds)),
                TimeUnit.MILLISECONDS.toSeconds(timeInMilliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeInMilliseconds)),
                timeInMilliseconds - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(timeInMilliseconds)));
    }

    private void updateTimeStats(long timeInMilliseconds) {
        timeLabel.setText(formatElapsedTime(timeInMilliseconds));
        totalMillis.setText(Long.toString(timeInMilliseconds));
        totalSeconds.setText(Long.toString(TimeUnit.MILLISECONDS.toSeconds(timeInMilliseconds)));
        totalMinutes.setText(Long.toString(TimeUnit.MILLISECONDS.toMinutes(timeInMilliseconds)));
    }

    // Buttons can be pressed with enter
    public void buttonKeyboardListener(javafx.scene.input.KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER) {
            toggleTimer();
        }
    }

}
