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

import org.hamcrest.core.IsInstanceOf;
import javafx.scene.Node;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class InstanceOfMatcher extends BaseMatcher<Node> {

    private final IsInstanceOf matcher;
    private final Class<?> type;
    private Object last = null;

    public InstanceOfMatcher(Class<?> type) {
        this.type = type;
        this.matcher = new IsInstanceOf(type);
    }

    public InstanceOfMatcher(String name) throws ClassNotFoundException {
        this.type = Class.forName(name);
        this.matcher = new IsInstanceOf(this.type);
    }

    @Override
    public void describeTo(Description description) {
        if (last != null) {
        description.appendText(String.format("Expected type %s%n but got ", type, last));
        }
    }

    @Override
    public boolean matches(Object item) {
        this.last = item;
        return matcher.matches(item);
    }
}