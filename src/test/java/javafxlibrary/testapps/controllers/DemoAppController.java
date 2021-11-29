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
import javafxlibrary.testapps.customcomponents.ImageDemo;
import javafxlibrary.testapps.customcomponents.TextList;

import java.net.URL;
import java.util.ResourceBundle;

public class DemoAppController implements Initializable {

    @FXML
    Label imageViewLabel;
    @FXML
    Label textViewLabel;
    @FXML
    ImageDemo imageDemo;
    @FXML
    TextList textList;
    boolean toggled;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        toggled = false;
        imageViewLabel.getStyleClass().add("activeNavigation");
    }

    public void toggleContent(MouseEvent e) {
        if (!toggled && e.getSource() == textViewLabel) {
            imageDemo.setVisible(false);
            imageDemo.setManaged(false);
            textList.setVisible(true);
            textList.setManaged(true);
            imageViewLabel.getStyleClass().remove("activeNavigation");
            textViewLabel.getStyleClass().add("activeNavigation");
            toggled = true;
        } else if (toggled && e.getSource() == imageViewLabel) {
            textList.setVisible(false);
            textList.setManaged(false);
            imageDemo.setVisible(true);
            imageDemo.setManaged(true);
            textViewLabel.getStyleClass().remove("activeNavigation");
            imageViewLabel.getStyleClass().add("activeNavigation");
            toggled = false;
        }
    }
}
