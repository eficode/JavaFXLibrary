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

package javafxlibrary.testapps.customcomponents;

import javafx.beans.DefaultProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.concurrent.Callable;

@DefaultProperty("children")
public class TextRow extends VBox implements Runnable, Callable<Object> {

    @FXML
    Label headingLabel;
    @FXML
    Label contentLabel;

    public TextRow(String heading, String textContent) {
        super();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/javafxlibrary/ui/uiresources/customcomponents/TextRow.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        headingLabel.setText(heading);
        contentLabel.setText(textContent);
    }

    @Override
    public ObservableList<Node> getChildren() {
        return super.getChildren();
    }

    @Override
    public ObservableList<Node> getChildrenUnmodifiable() {
        return super.getChildrenUnmodifiable();
    }

    public String getContent() {
        return contentLabel.getText();
    }

    public void setContent(String content) {
        contentLabel.setText(content);
    }

    public String getHeading() {
        return headingLabel.getText();
    }

    public void setHeading(String heading) {
        headingLabel.setText(heading);
    }

    @Override
    public void run() {
        headingLabel.setText("Text was changed by run() method!");
    }

    @Override
    public Object call() throws Exception {
        Thread.sleep(1000);
        headingLabel.setText("Text was changed by call() method!");
        return "Hello from TextRow!";
    }
}
