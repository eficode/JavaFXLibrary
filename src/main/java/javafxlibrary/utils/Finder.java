package javafxlibrary.utils;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.matchers.InstanceOfMatcher;
import org.testfx.matcher.control.LabeledMatchers;

import java.util.Arrays;

import static javafxlibrary.utils.TestFxAdapter.robot;

public class Finder {

    public enum FindPrefix { ID, CSS, CLASS, TEXT, XPATH }
    private String[] prefixes;
    protected Parent currentRoot;

    public Finder() {
        this.prefixes = new String[]{"id=", "css=", "class=", "text=", "xpath="};
        this.currentRoot = robot.listWindows().get(0).getScene().getRoot();
    }

    public Node find(String query) {
        if (containsPrefixes(query))
            return newFind(parseWholeQuery(query));
        return robot.lookup(query).query();
    }

    public Node find(String query, Parent root) {
        if (containsPrefixes(query)) {
            this.currentRoot = root;
            return newFind(parseWholeQuery(query));
        }
        return robot.from(root).lookup(query).query();
    }

    // TODO: If nodes are not found start looking from other windows
    private Node newFind(String query) {
        FindPrefix prefix = getPrefix(query);
        return transformQuery(query, prefix);
    }

    private Node transformQuery(String query, FindPrefix prefix) {
        switch (prefix) {
            case ID:
                return this.currentRoot.lookup("#" + query.substring(3));
            case CSS:
                return this.currentRoot.lookup(query.substring(4));
            case CLASS:
                try {
                    Class<?> clazz = Class.forName(query.substring(6));
                    InstanceOfMatcher matcher = new InstanceOfMatcher(clazz);
                    return robot.from(this.currentRoot).lookup(matcher).query();
                } catch (ClassNotFoundException e) {
                    throw new JavaFXLibraryNonFatalException("Could not use \"" + query.substring(6) + "\" for " +
                            "Node lookup: class was not found");
                }
            case TEXT:
                return robot.from(this.currentRoot).lookup(LabeledMatchers.hasText(query)).query();
            case XPATH:
                return new XPathFinder().find(query.substring(6), currentRoot);
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
}
