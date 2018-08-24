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

import java.util.*;

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
        // If find is called with a query that contains an index, use findAll methods instead
        if (!findAll && query.containsIndex()) {
            return executeOverriddenLookup();
        } else if (query.containsIndex()) {
            Set<Node> lookupResults = new LinkedHashSet<>((Set<Node>)executeLookup(query.getPrefix(), query.getQuery()));
            lookupResults.remove(root);
            Node nodeAtIndex = getLookupResultByIndex(lookupResults, query.getIndex());

            if (nodeAtIndex != null)
                return new LinkedHashSet<>(Collections.singletonList(nodeAtIndex));
            return Collections.emptySet();
        }

        return executeLookup(query.getPrefix(), query.getQuery());
    }

    protected Object executeOverriddenLookup() {
        this.findAll = true;
        Set<Node> result = new LinkedHashSet<>((Set<Node>)executeLookup(query.getPrefix(), query.getQuery()));
        result.remove(root);
        return getLookupResultByIndex(result, query.getIndex());
    }

    private Object executeLookup(FindPrefix prefix, String lookupQuery) {
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

    private Node getLookupResultByIndex(Set<Node> lookupResults, int index) {
        try {
            return new ArrayList<>(lookupResults).get(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
}
