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
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class TestWindowManagementController implements Initializable {

    private @FXML VBox root;
    private @FXML VBox employeeDataContainer;
    private @FXML Rectangle loadingBar;
    private @FXML HBox navBar;
    private List<Node> contents;
    private Node activeNode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        contents = new ArrayList<>();
        contents.add(root.lookup("#informationAlertBody"));
        contents.add(root.lookup("#dialogBody"));
        activeNode = contents.get(0);
    }

    public void buttonListener() {
        Timeline timeline = new Timeline();

        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(loadingBar.translateXProperty(), 1),
                        new KeyValue(loadingBar.scaleXProperty(), 1)),
                new KeyFrame(Duration.seconds(1.5),
                        new KeyValue(loadingBar.translateXProperty(), 200),
                        new KeyValue(loadingBar.scaleXProperty(), 100))
        );
        timeline.setOnFinished(e -> showAlert());
        timeline.play();
    }

    public void addEmployeeButtonListener() {
        Dialog<Employee> dialog = new Dialog<>();
        dialog.setTitle("Add employee information");
        dialog.setHeaderText("New employee");

        Label nameLabel = new Label("Name: ");
        Label phoneLabel = new Label("Phone: ");
        TextField nameField = new TextField();
        TextField phoneField = new TextField();

        nameField.setId("nameField");
        phoneField.setId("phoneField");

        GridPane grid = new GridPane();
        grid.add(nameLabel, 1, 1);
        grid.add(nameField, 2, 1);
        grid.add(phoneLabel, 1, 2);
        grid.add(phoneField, 2, 2);
        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        dialog.setResultConverter((b) -> {
            if (b==buttonTypeOk)
                return new Employee(nameField.getText(), phoneField.getText());
            return null;
        });

        Optional<Employee> result = dialog.showAndWait();

        if (result.isPresent()) {
            Employee employee = result.get();
            Label nameCell = new Label(employee.getName());
            Label phoneCell = new Label(employee.getPhoneNumber());
            nameCell.getStyleClass().add("employeeDataCell");
            phoneCell.getStyleClass().add("employeeDataCell");
            HBox dataRow = new HBox(nameCell, phoneCell);
            dataRow.getStyleClass().add("employeeDataRow");
            employeeDataContainer.getChildren().add(dataRow);
        }
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("JavaFX Dialogs - Information Alert");
        alert.setHeaderText("JavaFXLibrary");
        alert.setContentText("Download complete!");
        alert.show();
    }

    public void navListener(MouseEvent e) {
        Label source = (Label) e.getSource();
        List<Node> navButtons = navBar.getChildren();
        navButtons.forEach((navButton) -> {
            navButton.getStyleClass().remove("activeNavButton");
            if (source == navButton) {
                navButton.getStyleClass().add("activeNavButton");
                changeView(navButtons.indexOf(navButton));
            }
        });
    }

    private void changeView(int index) {
        if (contents.get(index) != activeNode) {
            activeNode.getStyleClass().remove("activeView");
            activeNode.getStyleClass().add("hiddenView");
            activeNode.setManaged(false);
            contents.get(index).getStyleClass().remove("hiddenView");
            contents.get(index).getStyleClass().add("activeView");
            contents.get(index).setManaged(true);
            activeNode = contents.get(index);
        }
    }

    // Helper class
    public class Employee {

        String name;
        String phoneNumber;

        private Employee(String name, String phoneNumber) {
            this.name = name;
            this.phoneNumber = phoneNumber;
        }

        public String getName() {
            return name;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }
    }
}
