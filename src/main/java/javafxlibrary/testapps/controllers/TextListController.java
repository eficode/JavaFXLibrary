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
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafxlibrary.testapps.customcomponents.TextRow;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class TextListController implements Initializable {

    private @FXML VBox textRowWrapper;
    private @FXML TextField search;
    private List<TextRow> textRows;
    private List<TextRow> activeRows;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> paragraphList = new ArrayList<>();
        textRows = new ArrayList<>();

        paragraphList.add("Leverage agile frameworks to provide a robust synopsis for high level overviews. " +
                "Iterative approaches to corporate strategy foster collaborative thinking to further the overall " +
                "value proposition. Objectively innovate empowered manufactured products whereas parallel platforms. " +
                "Holisticly predominate extensible testing procedures for reliable supply chains.");
        paragraphList.add("Bring to the table win-win survival strategies to ensure proactive growth. At the end of the" +
                " day, going forward, a new normal that has evolved from generation X is on the runway heading towards" +
                " a streamlined cloud solution. Phosfluorescently engage worldwide methodologies with web-enabled technology.");
        paragraphList.add("Interactively coordinate proactive e-commerce via process-centric outside the box thinking. " +
                "Completely pursue scalable customer service through sustainable potentialities. User generated content " +
                "in real-time will have multiple touchpoints for offshoring. Capitalize on " +
                "low hanging fruit to identify a ballpark value added activity to beta test. Override the digital divide " +
                "with additional clickthroughs from DevOps.");
        paragraphList.add("Podcasting operational change management inside of workflows to establish a framework. " +
                "Taking seamless key performance indicators offline to maximise the long tail. Keeping your eye on " +
                "the ball while performing a deep dive on the start-up mentality to derive convergence on " +
                "cross-platform integration.");
        paragraphList.add("Collaboratively administrate empowered markets via plug-and-play networks. Dynamically " +
                "procrastinate B2C users after installed base benefits. Dramatically visualize customer directed " +
                "convergence without revolutionary ROI." +
                "\nEfficiently unleash cross-media information without cross-media value. Quickly maximize timely " +
                "deliverables for real-time schemas. Dramatically maintain clicks-and-mortar solutions without " +
                "functional solutions.");
        paragraphList.add("Quickly communicate enabled technology and turnkey leadership skills. Uniquely enable accurate " +
                "supply chains rather than frictionless technology. Globally network focused materials using cost " +
                "effective manufactured products.\nUniquely matrix economically sound value through cooperative " +
                "technology. Competently parallel task fully researched data and enterprise process improvements. " +
                "Collaboratively expedite quality manufactured products via client-focused results.");

        paragraphList.forEach((paragraph) -> textRows.add(new TextRow(generateHeading(paragraph), paragraph)));
        Collections.shuffle(textRows);
        activeRows = new ArrayList<>(textRows);
        drawParagrahps();
    }

    public void searchListener() {
        String[] queries = search.getText().toLowerCase().split(" ");
        activeRows.clear();

        textRows.forEach((textRow) -> {
            boolean match = true;

            for(String query : queries) {
                if (!(textRow.getContent().toLowerCase().contains(query)))
                    match = false;
            }

            if(match)
                activeRows.add(textRow);
        });

        drawParagrahps();
    }

    // Generate a heading using random words from the paragraph
    private String generateHeading(String paragraph) {
        String[] words = paragraph.split("\\s+");
        boolean validHeading = false;
        String heading = "";

        while(!validHeading) {
            String firstWord = words[ThreadLocalRandom.current().nextInt(0, words.length)];
            String secondWord = words[ThreadLocalRandom.current().nextInt(0, words.length)];

            if (firstWord.endsWith(",") || firstWord.endsWith("."))
                firstWord = firstWord.substring(0, firstWord.length() -1);

            if (secondWord.endsWith(",") || secondWord.endsWith("."))
                secondWord = secondWord.substring(0, secondWord.length() -1);

            if (firstWord.length() > 3 && secondWord.length() > 3 && !firstWord.equals(secondWord)) {
                validHeading = true;
                heading = firstWord + " " + secondWord;
                heading = heading.toLowerCase();
                heading = heading.substring(0, 1).toUpperCase() + heading.substring(1);
            }
        }
        return heading;
    }

    private void drawParagrahps() {
        textRowWrapper.getChildren().clear();
        activeRows.forEach((textRow) -> textRowWrapper.getChildren().add(textRow));
        textRowWrapper.getChildren().forEach(label -> label.getStyleClass().add("textRow"));
    }
}
