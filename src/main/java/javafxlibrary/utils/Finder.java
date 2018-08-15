package javafxlibrary.utils;

import javafx.collections.ObservableSet;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Window;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.matchers.InstanceOfMatcher;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.service.query.NodeQuery;

import java.util.*;

import static javafxlibrary.utils.TestFxAdapter.robot;

public class Finder {

    public enum FindPrefix { ID, CSS, CLASS, TEXT, XPATH, PSEUDO }

    private String[] queries;
    private Set<Node> results = new LinkedHashSet<>();

    public Node find(String query) {
        // TODO: Remove old style lookup queries
        // Use TestFX lookup for queries with no prefixes
        if (!QueryParser.startsWithPrefix(query)) {
            RobotLog.warn("You are using deprecated lookup queries! See library documentation for information about " +
                    "the updated lookup query syntax.");
            return robot.lookup(query).query();
        }

        List<Window> windows = robot.listTargetWindows();
        RobotLog.debug("Finding with query \"" + query + "\" from " + windows.size() + " windows");

        for (Window window : windows) {
            RobotLog.debug("Finding from window " + window);
            Node result = find(query, window.getScene().getRoot());
            if (result != null)
                return result;

        }
        RobotLog.debug("Find finished, nothing was found with query: " + query);
        return null;
    }

    public Node find(String query, Parent root) {
        // TODO: Remove old style lookup queries
        // Use TestFX lookup for queries with no prefixes
        if (!QueryParser.startsWithPrefix(query)) {
            RobotLog.warn("You are using deprecated lookup queries! See library documentation for information about " +
                    "the updated lookup query syntax.");
            return robot.from(root).lookup(query).query();
        }

        this.queries = QueryParser.getIndividualQueries(query);
        return find(root, 0);
    }

    private Node find(Parent root, int queryIndex) {
        String query = queries[queryIndex];

        if (queryIndex < queries.length - 1) {
            // lookupResults might be unmodifiable, copy contents to a new Set
            Set<Node> lookupResults = executeFindAll(root, query);
            Set<Node> nodes = new LinkedHashSet<>();
            nodes.addAll(lookupResults);
            nodes.remove(root);

            for (Node node : nodes) {
                if (node instanceof Parent) {
                    Node result = find((Parent) node, queryIndex + 1);
                    if (result != null) {
                        return result;
                    }
                }
            }
            return null;
        } else {
            return executeFind(root, query);
        }
    }

    public Set<Node> findAll(String query) {
        // TODO: Remove old style lookup queries
        // Use TestFX lookup for queries with no prefixes
        if (!QueryParser.startsWithPrefix(query)) {
            RobotLog.warn("You are using deprecated lookup queries! See library documentation for information about " +
                    "the updated lookup query syntax.");
            return robot.lookup(query).queryAll();
        }

        List<Window> windows = robot.listTargetWindows();
        RobotLog.debug("Finding All with query \"" + query + "\" from " + windows.size() + " windows");

        for (Window window : windows) {
            RobotLog.debug("Finding all from window " + window);
            findAll(query, window.getScene().getRoot());
        }
        return results;
    }

    public Set<Node> findAll(String query, Parent root) {
        // TODO: Remove old style lookup queries
        // Use TestFX lookup for queries with no prefixes
        if (!QueryParser.startsWithPrefix(query)) {
            RobotLog.warn("You are using deprecated lookup queries! See library documentation for information about " +
                    "the updated lookup query syntax.");
            return robot.from(root).lookup(query).query();
        }

        this.queries = QueryParser.getIndividualQueries(query);
        return findAll(root, 0);
    }

    private Set<Node> findAll(Parent root, int queryIndex) {
        String query = queries[queryIndex];
        Set<Node> lookupResults = executeFindAll(root, query);
        Set<Node> nodes = new LinkedHashSet<>();
        nodes.addAll(lookupResults);
        nodes.remove(root);

        if (queryIndex < queries.length - 1) {
            for (Node node : nodes)
                if (node instanceof Parent)
                    findAll((Parent) node, queryIndex + 1);
        } else {
            results.addAll(nodes);
        }

        return results;
    }

    private Node executeFind(Parent root, String query) {
        RobotLog.debug("Executing find with root: " + root + " and query: " + query);
        FindPrefix prefix = getPrefix(query);

        switch (prefix) {
            case ID:
                return root.lookup("#" + query.substring(3));
            case CSS:
                return root.lookup(query.substring(4));
            case CLASS:
                return classLookup(root, query).query();
            case TEXT:
                query = query.substring(6, query.length() - 1);
                return robot.from(root).lookup(LabeledMatchers.hasText(query)).query();
            case XPATH:
                return new XPathFinder().find(query.substring(6), root);
            case PSEUDO:
                return pseudoLookup(root, query).query();
        }
        throw new IllegalArgumentException("FindPrefix value " + prefix + " of query " + query + " is not supported");
    }

    // TODO: Add support for using indexes in queries (css=VBox[3]), xPath already implements this
    private Set<Node> executeFindAll(Parent root, String query) {
        RobotLog.debug("Executing find all with root: " + root + " and query: " + query);
        FindPrefix prefix = getPrefix(query);

        switch (prefix) {
            case ID:
                return root.lookupAll("#" + query.substring(3));
            case CSS:
                return root.lookupAll(query.substring(4));
            case CLASS:
                return classLookup(root, query).queryAll();
            case TEXT:
                query = query.substring(6, query.length() - 1);
                return robot.from(root).lookup(LabeledMatchers.hasText(query)).queryAll();
            case XPATH:
                return new XPathFinder().findAll(query.substring(6), root);
            case PSEUDO:
                return pseudoLookup(root, query).queryAll();
        }
        throw new IllegalArgumentException("FindPrefix value " + prefix + " of query " + query + " is not supported");
    }

    protected FindPrefix getPrefix(String query) {

        try {
            String prefix = query.substring(0, query.indexOf('='));

            switch (prefix) {
                case "id":
                    return FindPrefix.ID;
                case "css":
                    return FindPrefix.CSS;
                case "class":
                    return FindPrefix.CLASS;
                case "text":
                    return FindPrefix.TEXT;
                case "xpath":
                    return FindPrefix.XPATH;
                case "pseudo":
                    return FindPrefix.PSEUDO;
                default:
                    throw new IllegalArgumentException("Query \"" + query + "\" does not contain any supported prefix");
            }
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Query \"" + query + "\" does not contain any supported prefix");
        }
    }

    private NodeQuery pseudoLookup(Parent root, String query) {
        String[] queries = query.substring(7).split(";");
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
            Class<?> clazz = Class.forName(query.substring(6));
            InstanceOfMatcher matcher = new InstanceOfMatcher(clazz);
            return robot.from(root).lookup(matcher);
        } catch (ClassNotFoundException e) {
            throw new JavaFXLibraryNonFatalException("Could not use \"" + query.substring(6) + "\" for " +
                    "Node lookup: class was not found");
        }
    }
}
