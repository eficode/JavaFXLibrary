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

import javafx.scene.control.Button;
import javafx.scene.text.Text;
import org.testfx.framework.junit.ApplicationTest;
import org.junit.Assert;
import org.junit.Test;

public class InstanceOfMatcherTest extends ApplicationTest {

    @Test
    public void matchesWithClass() {
        Button b = new Button();
        Text t = new Text();
        InstanceOfMatcher matcher = new InstanceOfMatcher(b.getClass());
        Assert.assertTrue(matcher.matches(b));
        Assert.assertFalse(matcher.matches(t));
    }

    @Test
    public void matchesWithString() throws ClassNotFoundException {
        Button b = new Button();
        Text t = new Text();
        InstanceOfMatcher matcher = new InstanceOfMatcher(b.getClass().getName());
        Assert.assertTrue(matcher.matches(b));
        Assert.assertFalse(matcher.matches(t));
    }

    @Test(expected = ClassNotFoundException.class)
    public void invalidClass() throws ClassNotFoundException {
        new InstanceOfMatcher("some.invalid.name");
    }

}