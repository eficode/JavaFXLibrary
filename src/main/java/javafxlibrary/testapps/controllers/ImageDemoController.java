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
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.io.File;
import java.net.URL;
import java.util.*;

public class ImageDemoController implements Initializable {

    private @FXML TextField search;
    private @FXML VBox rowWrapper;
    //private List<File> files;
    private List<File> imageFiles;
    private List<ImageFile> images;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //File folder = new File("src/main/resources/ScreenCapturing/comparison");
        //files = Arrays.asList(folder.listFiles());
        imageFiles = new ArrayList<>(/*files*/);
        imageFiles.add(new File("src/main/resources/fxml/javafxlibrary/ui/uiresources/ejlogo.png"));
        images = new ArrayList<>();
        Collections.shuffle(imageFiles);

        imageFiles.forEach((File file) -> {
            if(file.getName().endsWith(".png"))
                images.add(new ImageFile(file.toURI().toString(),215.0, 215.0, false, false));
        });

        drawImages();
    }

    public void searchListener() {
        String[] queries = search.getText().toLowerCase().split(" ");
        images.clear();

        imageFiles.forEach((File file) -> {
            boolean match = true;
            for(String query : queries) {
                if (!(file.getName().endsWith(".png") && file.getName().contains(query)))
                    match = false;
            }
            if(match)
                images.add(new ImageFile(file.toURI().toString(), 215.0, 215.0, false, false));
        });
        drawImages();
    }

    public void drawImages() {
        int rowAmount = images.size() / 3;
        rowWrapper.getChildren().clear();

        for(int i = 0; i < rowAmount; i++) {
            rowWrapper.getChildren().add(new HBox(new ImageView(images.get(i * 3)),
                    new ImageView(images.get(i * 3 + 1)), new ImageView(images.get(i * 3 + 2))));
        }

        int remainderImages = images.size() % 3;
        HBox lastRow = new HBox();

        for(int i = 0; i < remainderImages; i++) {
            lastRow.getChildren().add(new ImageView(images.get(images.size() - (remainderImages - i) )));
        }

        rowWrapper.getChildren().add(lastRow);
        rowWrapper.getChildren().forEach((Node node) -> node.getStyleClass().add("imageRow"));
        // Remove bottom padding from the last row
        rowWrapper.getChildren().get(rowWrapper.getChildren().size()-1).setStyle("-fx-padding: 15 44 0 41;");
    }

    class ImageFile extends Image {

        private final String url;

        public ImageFile(String url) {
            super(url);
            this.url = url;
        }

        public ImageFile(String url, double requestedWidth, double requestedHeight, boolean preserveRatio, boolean smooth) {
            super(url, requestedWidth, requestedHeight, preserveRatio, smooth);
            this.url = url;
        }

        public String getURL() {
            return url;
        }
    }
}
