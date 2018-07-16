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

package javafxlibrary.keywords.AdditionalKeywords;

import java.util.List;
import com.google.common.collect.ImmutableSet;
import javafx.css.PseudoClass;
import javafx.scene.control.Button;
import javafxlibrary.TestFxAdapterTest;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.testfx.service.query.NodeQuery;
import static org.junit.Assert.fail;

public class ConvenienceKeywordsTest extends TestFxAdapterTest {

    @Mocked
    NodeQuery rootQuery;

    private Button button;
    private Button button2;
    private ConvenienceKeywords keywords = new ConvenienceKeywords();
    private Find find = new Find();

    @Before
    public void setup() {
        button = new Button();
        button2 = new Button();
        button.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), true);
        new Expectations() {
            {
                getRobot().lookup("rootId");
                result = rootQuery;
                minTimes = 0;
            }
        };
    }

    @Test
    public void findAllWithPseudoClass() {
        expectTwoButtonsFromNodeQuery();

        List<Object> allWithPseudoClass = keywords.findAllWithPseudoClass("rootId", "selected");
        Assert.assertEquals(HelperFunctions.mapObject(button), allWithPseudoClass.get(0));
    }

    @Test
    public void findNoPseudoClasses() {
        expectTwoButtonsFromNodeQuery();
        try {
            List<Object> hits = keywords.findAllWithPseudoClass("rootId", "something");
            Assert.assertEquals(0, hits.size());
        } catch (JavaFXLibraryNonFatalException e) {

        }
    }

    @Test
    public void findNoNodes() {
        new Expectations() {
            {
                rootQuery.queryAll();
                result = ImmutableSet.of();
            }
        };
        try {
            List<Object> hits = keywords.findAllWithPseudoClass("rootId", "something");
            Assert.assertEquals(0, hits.size());
        } catch (JavaFXLibraryNonFatalException e) {

        }
    }

    private void expectTwoButtonsFromNodeQuery() {
        new Expectations() {
            {
                rootQuery.queryAll();
                result = ImmutableSet.of(button, button2);
            }
        };
    }

    @Ignore
    @Test
    public void findGivesMeTheNode() {
        new Expectations() {
            {
                rootQuery.query();
                result = button;
            }
        };
        Object value = find.find("rootId");
        Assert.assertEquals(HelperFunctions.mapObject(button), value);
    }

    @Ignore
    @Test
    public void findNoMatching() {
        new Expectations() {
            {
                rootQuery.query();
                result = null;
            }
        };
        try {
            find.find("invalid", true);
            fail("Expected a JavaFXLibraryNonFatalException to be thrown");
        } catch (JavaFXLibraryNonFatalException e) {
            Assert.assertEquals("Unable to find anything with query: \"invalid\"", e.getMessage());
        }
    }
}
