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

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuAppController implements Initializable {

    private @FXML Label textLabel;
    private @FXML Label total;
    private @FXML ComboBox amountComboBox;
    private @FXML ComboBox priceComboBox;
    private @FXML RadioMenuItem efiStyle;
    private @FXML RadioMenuItem javaStyle;
    private @FXML RadioMenuItem gradientStyle;
    private @FXML Menu fontMenu;
    private @FXML Rectangle bgRectangle;
    private String bnyCss = getClass().getResource("/fxml/javafxlibrary/ui/css/MenuApp/Bny.css").toExternalForm();
    private String javaCss = getClass().getResource("/fxml/javafxlibrary/ui/css/MenuApp/Javastyle.css").toExternalForm();
    private String gradientCss = getClass().getResource("/fxml/javafxlibrary/ui/css/MenuApp/Gradientstyle.css").toExternalForm();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        amountComboBox.getItems().addAll("25 pc", "50 pc", "100 pc");
        priceComboBox.getItems().addAll("50 €", "75 €", "100 €");
        amountComboBox.setValue("Select amount");
        priceComboBox.setValue("Select price");

        ToggleGroup styleToggleGroup = new ToggleGroup();
        efiStyle.setToggleGroup(styleToggleGroup);
        javaStyle.setToggleGroup(styleToggleGroup);
        gradientStyle.setToggleGroup(styleToggleGroup);

        ToggleGroup fontSizeGroup = new ToggleGroup();

        for (MenuItem menuItem : fontMenu.getItems()) {
            RadioMenuItem r = (RadioMenuItem) menuItem;
            r.setToggleGroup(fontSizeGroup);
            menuItem.setOnAction((ActionEvent event) -> {
                RadioMenuItem radioMenuItem = (RadioMenuItem) event.getSource();
                int size = Integer.parseInt(radioMenuItem.getText().substring(0,2));
                textLabel.setStyle("-fx-font-size: " + size + "px");
            });
        }

        final ContextMenu cm = new ContextMenu();
        cm.getItems().addAll(new MenuItem("JavaFXLibrary"), new MenuItem("Is easy"), new MenuItem("And fun to use"));

        for (MenuItem item : cm.getItems())
            item.setOnAction(e -> textLabel.setText(item.getText()));

        bgRectangle.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY)
                cm.show(textLabel, e.getScreenX(), e.getScreenY());
        });
    }

    public void navigate(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        textLabel.setText(menuItem.getText());
    }

    public void toggleStyle(ActionEvent event) {
        Scene scene = textLabel.getScene();
        RadioMenuItem r = (RadioMenuItem) event.getSource();

        switch(r.getText()) {
            case "JavaFX":
                scene.getStylesheets().clear();
                scene.getStylesheets().add(javaCss);
                break;
            case "Black and Yellow":
                scene.getStylesheets().clear();
                scene.getStylesheets().add(bnyCss);
                break;
            case "Gradient":
                scene.getStylesheets().clear();
                scene.getStylesheets().add(gradientCss);
                break;
        }
    }

    public void countTotal() {
        String amountValue = (String) amountComboBox.getValue();
        String priceValue = (String) priceComboBox.getValue();
        if (!amountValue.equals("Select amount") && !priceValue.equals("Select price")) {
            int a = Integer.parseInt(amountValue.substring(0, amountValue.length() - 3));
            int v = Integer.parseInt(priceValue.substring(0, priceValue.length() - 2));
            total.setText(a*v + " €");
        }
    }
}
