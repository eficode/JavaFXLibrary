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
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.io.IOException;

@DefaultProperty("children")
public class ImageDemo extends VBox {

    public ImageDemo() {
        super();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/javafxlibrary/ui/uiresources/customcomponents/ImageDemo.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ObservableList<Node> getChildren() {
        return super.getChildren();
    }

    @Override
    public ObservableList<Node> getChildrenUnmodifiable() {
        return super.getChildrenUnmodifiable();
    }
}
