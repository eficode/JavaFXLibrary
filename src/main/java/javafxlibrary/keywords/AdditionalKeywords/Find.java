package javafxlibrary.keywords.AdditionalKeywords;

import javafx.scene.Parent;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.Finder;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;

import java.util.ArrayList;
import java.util.List;

import static javafxlibrary.utils.HelperFunctions.mapObject;
import static javafxlibrary.utils.HelperFunctions.mapObjects;
import static javafxlibrary.utils.HelperFunctions.robotLog;

@RobotKeywords
public class Find {

    @RobotKeyword("Returns the *first* node matching the query. \n\n"
            + "``query`` is a query locator, see `3. Locating JavaFX Nodes`.\n\n"
            + "``failIfNotFound`` specifies if keyword should fail if nothing is found. By default it's false and "
            + "keyword returns null in case lookup returns nothing.\n\n"
            + "``root`` is an optional argument pointing to the element which is used as the origin of the lookup. If "
            + "root is defined only its children can be found. By default nodes are being looked from everywhere.\n\n"
            + "\nExample:\n"
            + "| ${my node}= | Find | some text | | | # finds node containing text _some text_ |\n"
            + "| ${my node}= | Find | .css | | | # finds node with matching style class |\n"
            + "| ${my node}= | Find | \\#id | | | # finds node with matching _id_ |\n"
            + "| ${my node}= | Find | css=VBox | | | # finds node matching the CSS selector |\n"
            + "| ${my node}= | Find | id=id | | | # finds node with matching _id_ |\n"
            + "| ${my node}= | Find | xpath=//Rectangle | | | # finds node matching the XPath |\n"
            + "| ${my node}= | Find | class=javafx.scene.shape.Rectangle | | | # finds node that is instance of the class |\n"
            + "| ${my node}= | Find | pseudo=hover | | | # finds node containing the given pseudo class state |\n"
            + "| ${my node}= | Find | \\#id | True | | # this search fails if nothing is found |\n"
            + "| ${my node}= | Find | css=VBox | False | ${root} | # finds node matching the CSS selector from the children of given root |\n\n"
            + "Or chaining multiple queries together:\n"
            + "| ${my node}= | Find | css=VBox HBox xpath=//Rectangle[@width=\"600.0\"] | \n"
            + "The example above would first look for a node matching the css selector _VBox HBox_, then continue the search "
            + "using the found HBox as a root node, while looking for a node matching the XPath.\n\n")
    @ArgumentNames({ "query", "failIfNotFound=False", "root=" })
    public Object find(String query, boolean failIfNotFound, Parent root) {
        robotLog("INFO", "Trying to find the first node matching the query: \"" + query
                + "\", failIfNotFound= \"" + Boolean.toString(failIfNotFound) + "\", root= \"" + root + "\"");
        try {
            return mapObject(new Finder().find(query, root));

        } catch (JavaFXLibraryNonFatalException e){
            if (failIfNotFound)
                throw new JavaFXLibraryNonFatalException("Unable to find anything with query: \"" + query + "\"");
            return "";

        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Find operation failed for query: \"" + query + "\"", e);
        }
    }

    @RobotKeywordOverload
    @ArgumentNames({ "query", "failIfNotFound=False" })
    public Object find(String query, boolean failIfNotFound) {
        robotLog("INFO", "Trying to find the first node matching the query: \"" + query
                + "\", failIfNotFound= \"" + Boolean.toString(failIfNotFound) + "\"");
        try {
            return mapObject(new Finder().find(query));

        } catch (JavaFXLibraryNonFatalException e){
            if (failIfNotFound)
                throw new JavaFXLibraryNonFatalException("Unable to find anything with query: \"" + query + "\"");
            return "";

        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Find operation failed for query: \"" + query + "\"", e);
        }
    }

    @RobotKeywordOverload
    @ArgumentNames({ "query" })
    public Object find(String query) {
        return find(query, false);
    }

    @RobotKeyword("Returns *all* nodes matching the query. \n\n"
            + "``query`` is a query locator, see `3.1 Using queries`.\n\n"
            + "``failIfNotFound`` specifies if keyword should fail if nothing is found. By default it's false and "
            + "keyword returns null in case lookup returns nothing.\n\n"
            + "``root`` is an optional argument pointing to the element which is used as the origin of the lookup. If "
            + "root is defined only its children can be found. By default nodes are being looked from everywhere.\n\n"
            + "See keyword `Find` for further examples of query usage.\n")
    @ArgumentNames({ "query", "failIfNotFound=False", "root=" })
    public List<Object> findAll(String query, boolean failIfNotFound, Parent root) {
        try {
            return mapObjects(new Finder().findAll(query, root));
        } catch (JavaFXLibraryNonFatalException e) {
            if (failIfNotFound)
                throw new JavaFXLibraryNonFatalException("Unable to find anything with query: \"" + query + "\"");
            return new ArrayList<>();
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Find operation failed for query: \"" + query + "\"", e);
        }
    }

    @RobotKeywordOverload
    @ArgumentNames({ "query", "failIfNotFound=False" })
    public List<Object> findAll(String query, boolean failIfNotFound) {
        try {
            return mapObjects(new Finder().findAll(query));
        } catch (JavaFXLibraryNonFatalException e) {
            if (failIfNotFound)
                throw new JavaFXLibraryNonFatalException("Unable to find anything with query: \"" + query + "\"");
            return new ArrayList<>();
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Find operation failed for query: \"" + query + "\"", e);
        }
    }

    @RobotKeywordOverload
    @ArgumentNames({ "query" })
    public List<Object> findAll(String query) {
        return findAll(query, false);
    }
}
