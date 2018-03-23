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

package javafxlibrary.matchers;

import javafx.scene.control.*;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import java.util.function.Predicate;

public class ToggleMatchers {

    // can be used with Objects implementing Toggle interface: RadioButton, ToggleButton and RadioMenuItem
    public static Matcher<Toggle> toggleMatcher(final String descriptionText,
                                             final Predicate<Toggle> predicate,
                                                     final String misMatchText) {
        return new BaseMatcher<Toggle>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Toggled object is " + descriptionText);
            }

            @Override
            public boolean matches(Object object) {
                return predicate.test((Toggle) object);
            }

            @Override
            public void describeMismatch(Object object, Description description) {
                description.appendText("Toggled object: ").appendValue(object).appendText(" is " + misMatchText);
            }
        };
    }

    public static Matcher<Toggle> isSelected() {
        return toggleMatcher("selected", Toggle::isSelected, "not selected!" );
    }

    public static Matcher<Toggle> isNotSelected() {
        return toggleMatcher("not selected", toggle -> !toggle.isSelected(), "selected!" );
    }

}