package javafxlibrary.utils;

import javafx.collections.ObservableSet;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.matchers.InstanceOfMatcher;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.service.query.NodeQuery;

import java.util.Arrays;
import java.util.Set;

import static javafxlibrary.utils.TestFxAdapter.robot;

public class Finder {

    public enum FindPrefix { ID, CSS, CLASS, TEXT, XPATH, PSEUDO }
    private String[] prefixes;
    protected Parent currentRoot;
    private Set<Parent> rootNodes;
    private String originalQuery;

    public Finder() {
        this.prefixes = new String[]{"id=", "css=", "class=", "text=", "xpath=", "pseudo="};
        this.currentRoot = robot.listTargetWindows().get(0).getScene().getRoot();
    }

    public Node find(String query) {
        if (containsPrefixes(query)) {
            originalQuery = query;
            rootNodes = robot.fromAll().queryAll();
            return newFind(parseWholeQuery(query));
        }
        return robot.lookup(query).query();
    }

    public Node find(String query, Parent root) {
        if (containsPrefixes(query)) {
            this.currentRoot = root;
            return newFind(parseWholeQuery(query));
        }
        return robot.from(root).lookup(query).query();
    }

    public Set<Node> findAll(String query) {
        if (containsPrefixes(query)) {
            originalQuery = query;
            rootNodes = robot.fromAll().queryAll();
            return newFindAll(parseWholeQuery(query));
        }
        return robot.lookup(query).queryAll();
    }

    public Set<Node> findAll(String query, Parent root) {
        HelperFunctions.robotLog("DEBUG", "Executing Finder.findAll using query: " + query + " and root: " + root);
        if (containsPrefixes(query)) {
            this.currentRoot = root;
            return newFindAll(parseWholeQuery(query));
        }
        return robot.from(root).lookup(query).queryAll();
    }

    private Node newFind(String query) {
        FindPrefix prefix = getPrefix(query);
        Node result = executeLookup(query, prefix);

        if (result == null && rootNodes != null && rootNodes.size() > 1) {
            HelperFunctions.robotLog("DEBUG", "Could not find anything from " + currentRoot + ", moving " +
                    "to the next root node");
            rootNodes.remove(currentRoot);
            currentRoot = rootNodes.iterator().next();
            result = newFind(parseWholeQuery(originalQuery));
        }

        return result;
    }

    private Set<Node> newFindAll(String query) {
        FindPrefix prefix = getPrefix(query);
        Set<Node> nodes = executeLookupAll(query, prefix);

        if (rootNodes != null && rootNodes.iterator().hasNext() && rootNodes.size() > 1) {
            HelperFunctions.robotLog("DEBUG", "Finished lookup with root " + currentRoot + ", moving " +
                    "to the next root node");
            rootNodes.remove(currentRoot);
            currentRoot = rootNodes.iterator().next();
            nodes.addAll(newFindAll(parseWholeQuery(originalQuery)));
        }
        return nodes;
    }

    private Node executeLookup(String query, FindPrefix prefix) {
        switch (prefix) {
            case ID:
                return this.currentRoot.lookup("#" + query.substring(3));
            case CSS:
                return this.currentRoot.lookup(query.substring(4));
            case CLASS:
                return classLookup(query).query();
            case TEXT:
                return robot.from(this.currentRoot).lookup(LabeledMatchers.hasText(query)).query();
            case XPATH:
                return new XPathFinder().find(query.substring(6), currentRoot);
            case PSEUDO:
                return pseudoLookup(query).query();
        }
        throw new IllegalArgumentException("FindPrefix value " + prefix + " of query " + query + " is not supported");
    }

    private Set<Node> executeLookupAll(String query, FindPrefix prefix) {
        switch (prefix) {
            case ID:
                return this.currentRoot.lookupAll("#" + query.substring(3));
            case CSS:
                return this.currentRoot.lookupAll(query.substring(4));
            case CLASS:
                return classLookup(query).queryAll();
            case TEXT:
                return robot.from(this.currentRoot).lookup(LabeledMatchers.hasText(query)).queryAll();
            case XPATH:
                return new XPathFinder().findAll(query.substring(6), currentRoot);
            case PSEUDO:
                return pseudoLookup(query).queryAll();
        }
        throw new IllegalArgumentException("FindPrefix value " + prefix + " of query " + query + " is not supported");
    }

    private String parseWholeQuery(String query) {
        while (containsMultiplePrefixes(query)) {

            String[] queryArray = query.split(" ");

            for (int i = 1; i < queryArray.length; i++) {
                if (containsPrefixes(queryArray[i])) {
                    String[] rootQuery = Arrays.copyOfRange(queryArray, 0, i );
                    this.currentRoot = (Parent) newFind(String.join(" ", rootQuery));
                    String[] remainingQuery = Arrays.copyOfRange(queryArray, i, queryArray.length);
                    query = String.join(" ", remainingQuery);
                    break;
                }
            }

            /*  Break when the last query has been checked. Without this block query values containing accepted prefixes
                like xpath=//Rectangle[@id="nodeId"] will cause an infinite loop. */
            if (queryArray.length == 1)
                break;
        }
        return query;
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

    // True if starts with known prefix
    protected boolean containsPrefixes(String query) {
        for (String prefix : prefixes) {
            if (query.startsWith(prefix))
                return true;
        }
        return false;
    }

    protected boolean containsMultiplePrefixes(String query) {
        String subQuery = query.substring(query.indexOf('='));
        for (String prefix : prefixes) {
            if (subQuery.contains(prefix))
                return true;
        }
        return false;
    }

    private NodeQuery pseudoLookup(String query) {
        String[] queries = query.substring(7).split(";");
        return robot.from(this.currentRoot).lookup((Node n) -> {
            int matching = 0;
            ObservableSet<PseudoClass> pseudoStates = n.getPseudoClassStates();

            for (PseudoClass c : pseudoStates)
                for (String q : queries)
                    if (c.getPseudoClassName().equals(q))
                        matching++;

            return n != this.currentRoot && (matching == queries.length);
        });
    }

    private NodeQuery classLookup(String query) {
        try {
            Class<?> clazz = Class.forName(query.substring(6));
            InstanceOfMatcher matcher = new InstanceOfMatcher(clazz);
            return robot.from(this.currentRoot).lookup(matcher);
        } catch (ClassNotFoundException e) {
            throw new JavaFXLibraryNonFatalException("Could not use \"" + query.substring(6) + "\" for " +
                    "Node lookup: class was not found");
        }
    }
}
