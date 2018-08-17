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

package javafxlibrary.utils.finder;

import javafx.collections.ObservableSet;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.matchers.InstanceOfMatcher;
import javafxlibrary.utils.TestFxAdapter;
import org.testfx.api.FxRobotInterface;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.service.query.NodeQuery;

public class FindOperation {

    private Parent root;
    private Query query;
    private boolean findAll;
    private FxRobotInterface robot;

    public FindOperation(Parent root, Query query, boolean findAll) {
        this.root = root;
        this.query = query;
        this.findAll = findAll;
        this.robot = TestFxAdapter.getRobot();
    }

    protected Object executeLookup() {
        FindPrefix prefix = query.getPrefix();
        String lookupQuery = query.getQuery();

        switch (prefix) {
            case ID:
            case CSS:
                return findAll ? root.lookupAll(lookupQuery) : root.lookup(lookupQuery);
            case CLASS:
                NodeQuery classLookupResults = classLookup(root, lookupQuery);
                return findAll ? classLookupResults.queryAll() : classLookupResults.query();
            case TEXT:
                NodeQuery textLookupResults = robot.from(root).lookup(LabeledMatchers.hasText(lookupQuery));
                return findAll ? textLookupResults.queryAll() : textLookupResults.query();
            case XPATH:
                XPathFinder xPathFinder = new XPathFinder();
                return findAll ? xPathFinder.findAll(lookupQuery, root) : xPathFinder.find(lookupQuery, root);
            case PSEUDO:
                NodeQuery pseudoLookupResults = pseudoLookup(root, lookupQuery);
                return findAll ? pseudoLookupResults.queryAll() : pseudoLookupResults.query();
        }
        throw new IllegalArgumentException("FindPrefix value " + prefix + " of query " + query + " is not supported");
    }

    private NodeQuery pseudoLookup(Parent root, String query) {
        String[] queries = query.split(";");
        return robot.from(root).lookup((Node n) -> {
            int matching = 0;
            ObservableSet<PseudoClass> pseudoStates = n.getPseudoClassStates();

            for (PseudoClass c : pseudoStates)
                for (String q : queries)
                    if (c.getPseudoClassName().equals(q))
                        matching++;

            return n != root && (matching == queries.length);
        });
    }

    private NodeQuery classLookup(Parent root, String query) {
        try {
            Class<?> clazz = Class.forName(query);
            InstanceOfMatcher matcher = new InstanceOfMatcher(clazz);
            return robot.from(root).lookup(matcher);
        } catch (ClassNotFoundException e) {
            throw new JavaFXLibraryNonFatalException("Could not use \"" + query + "\" for " +
                    "Node lookup: class was not found");
        }
    }
}
