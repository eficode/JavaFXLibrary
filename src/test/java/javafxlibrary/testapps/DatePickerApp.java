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
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Locale;

import static java.time.temporal.ChronoUnit.DAYS;

public class DatePickerApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("DatePicker");

        Label textFieldLabel = new Label("Name of the day: ");
        TextField textField = new TextField();
        textField.setPrefWidth(300);
        HBox inputBox = new HBox(textFieldLabel, textField);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setTranslateY(-15);

        DatePicker datePicker = new DatePicker();

        Label label = new Label("");
        label.setTranslateY(15);

        final Locale defaultLocale = Locale.getDefault(Locale.Category.FORMAT);
        datePicker.setOnShowing(e -> Locale.setDefault(Locale.Category.FORMAT, Locale.ENGLISH));
        datePicker.setOnHiding(e -> Locale.setDefault(Locale.Category.FORMAT, defaultLocale));
        datePicker.setOnAction(e -> Locale.setDefault(Locale.Category.FORMAT, defaultLocale));

        datePicker.valueProperty().addListener(((observable, oldValue, newValue) -> {
            long daysBetween = DAYS.between(LocalDate.now(), newValue);
            StringBuilder sb = new StringBuilder(Long.toString(daysBetween).replace("-", ""));

            if (daysBetween < 0)
                sb.append(" days since ");
            else
                sb.append(" days until ");

            sb.append(textField.getText());
            label.setText(sb.toString());
        }));

        VBox vBox = new VBox(inputBox, datePicker, label);
        vBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vBox, 600, 300);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
