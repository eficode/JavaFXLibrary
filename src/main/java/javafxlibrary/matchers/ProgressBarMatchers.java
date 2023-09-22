
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

import javafx.scene.control.ProgressBar;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.function.Predicate;

public class ProgressBarMatchers {

    // can be used with Objects implementing Toggle interface: RadioButton, ToggleButton and RadioMenuItem
    public static Matcher<ProgressBar> progressMatcher(final String descriptionText,
                                                       final Predicate<ProgressBar> predicate,
                                                       final String misMatchText) {
        return new BaseMatcher<ProgressBar>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("ProgressBar is " + descriptionText);
            }

            @Override
            public boolean matches(Object object) {
                return predicate.test((ProgressBar) object);
            }

            @Override
            public void describeMismatch(Object object, Description description) {
                description.appendText("ProgressBar is ").appendValue(object).appendText(" is " + misMatchText);
            }
        };
    }

    public static Matcher<ProgressBar> isComplete() {
        return progressMatcher("finished", ProgressBarMatchers::complete, "not finished!");
    }

    public static Matcher<ProgressBar> isLessThan(Double value) {
        return progressMatcher("less than " + value, pb -> lessThan(pb, value), "not less than " + value);
    }

    public static Matcher<ProgressBar> isMoreThan(Double value) {
        return progressMatcher("more than " + value, pb -> moreThan(pb, value), "not more than " + value);
    }


    private static boolean complete(ProgressBar pb) {
        return pb.getProgress() == 1d;
    }

    private static boolean lessThan(ProgressBar pb, Double value) {
        return pb.getProgress() <= value;
    }

    private static boolean moreThan(ProgressBar pb, Double value) {
        return pb.getProgress() >= value;
    }


}