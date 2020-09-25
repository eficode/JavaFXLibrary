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

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.exceptions.JavaFXLibraryTimeoutException;
import javafxlibrary.utils.HelperFunctions;
import javafxlibrary.utils.RobotLog;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.concurrent.TimeoutException;

import static javafxlibrary.utils.HelperFunctions.getHoveredNode;
import static org.testfx.util.WaitForAsyncUtils.waitFor;

public class ExtendedNodeMatchers {

    public static Matcher<Node> isHoverable() {
        return new BaseMatcher<Node>() {
            @Override
            public boolean matches(Object item) {
                return hoverable((Node)item);
            }
            @Override
            public void describeTo(Description description) {
                    description.appendText("Node is hoverable");
            }
            @Override
            public void describeMismatch(Object object, Description description) {
                description.appendText("Given target node is not hoverable, it seems to be hidden under this node: \"").
                        appendValue(getHoveredNode()).appendText("\"");

            }
        };
    }

    private static boolean hoverable(Node node) {
        try {
            new javafxlibrary.keywords.Keywords.MoveRobot().moveTo(node, "DIRECT");
            return node.isHover();
        } catch (JavaFXLibraryNonFatalException nfe) {
            throw nfe;
        } catch (Exception e) {
            RobotLog.trace("Exception in hoverable matcher: " + e + "\n" + e.getCause().toString());
            throw new JavaFXLibraryNonFatalException("hoverable matcher failed: ", e);
        }
    }

    public static boolean hasValidCoordinates(Node node) {
        Bounds bounds = HelperFunctions.objectToBounds(node);
        return !(Double.isNaN(bounds.getMinX()) || Double.isNaN(bounds.getMinY()) ||
                 Double.isNaN(bounds.getMaxX()) || Double.isNaN(bounds.getMaxY()));
    }
}